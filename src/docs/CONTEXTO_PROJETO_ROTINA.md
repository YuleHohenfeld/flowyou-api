# Contexto do Projeto — Rotina API (Backend Pessoal)

> Este documento existe para dar contexto a qualquer IA (Claude, Gemini, etc.)
> que for ajudar no desenvolvimento deste projeto. Leia isto por completo
> antes de escrever qualquer código.

---

## 1. Quem sou eu e o que estou construindo

Sou desenvolvedor Front-end (Flutter) com experiência no projeto **Biulix**
(app de recrutamento, arquitetura MVVMC). Agora estou construindo um
**projeto pessoal**, fora do contexto de trabalho, para organizar minha
própria rotina de vida. Sou **novo em back-end** — preciso de explicações
didáticas, não só código.

O projeto tem duas partes:
- **Backend**: Java + Spring Boot (este repositório)
- **Frontend**: Flutter (mobile) + Web, ambos consumindo esta API

---

## 2. Referência de arquitetura (Biulix)

Já tenho um projeto de referência real em produção, o **Biulix**, do qual
estou copiando os PADRÕES (não o código ou domínio de negócio). São dois
projetos separados que usei de base:

### 2.1 Biulix Front-end (Flutter)
- Padrão **MVVMC**: View → Controller → ViewModel → Repository → Model
- Provider (ChangeNotifier) para estado
- GoRouter centralizado para navegação
- Dio com interceptors customizados (refresh token automático, wipeout de sessão)
- Flag `isInicializando` em todo Controller que carrega dado assíncrono,
  para evitar "flash" de UI vazia
- Design System central (`core/Shared/`) — nunca recriar botão/input do zero

### 2.2 Biulix API (Backend — Spring Boot)
- Arquitetura em camadas clássica: `controller/ → service/ → repository/`
- Java 17, Spring Boot, PostgreSQL via JPA/Hibernate
- JWT + Redis para sessão (sliding session) — **eu NÃO vou usar Redis**,
  ver seção 4
- MapStruct para conversão DTO ↔ Entity
- Identidade central: uma entidade `Usuario` da qual outros perfis dependem
  (equivalente ao meu `Usuario` único, mais simples)

### 2.3 Débitos técnicos do Biulix que NÃO devo repetir
Estes problemas foram identificados no projeto de referência e são
exatamente o que quero evitar aqui:
- ❌ Segredo do JWT hardcoded no código-fonte → **aqui: sempre via variável
  de ambiente, aplicação deve falhar ao subir se não configurado**
- ❌ Credenciais de banco em texto puro no `application.properties`
  versionado → **aqui: sempre via variável de ambiente com fallback só
  para dev local**
- ❌ CORS liberado com `*` → **aqui: lista explícita de origins permitidas**
- ❌ Sem `@ControllerAdvice` global (cada controller repete try/catch) →
  **aqui: um `GlobalExceptionHandler` único trata todas as exceções**

---

## 3. Stack oficial deste projeto (decisões já tomadas — não mudar sem discutir)

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 17 |
| Framework | Spring Boot 4.0.7 (ou a versão estável gerada pelo Initializr) |
| Build | Maven |
| Banco de dados | **PostgreSQL** |
| ORM | Spring Data JPA / Hibernate |
| Autenticação | JWT **stateless puro, SEM Redis** (decisão consciente:
simplicidade > revogação instantânea de sessão, aceitável para projeto pessoal) |
| Login | **E-mail + senha** (não CPF, diferente do Biulix) |
| Conversão DTO↔Entity | MapStruct |
| Boilerplate | Lombok |
| Documentação da API | springdoc-openapi (Swagger em `/docs`) |
| Upload de arquivo | Salvo em disco/storage, banco guarda só o path (nunca
binário em coluna) |

### Arquitetura de camadas (MVC, não MVVMC — isso é backend, não tem View)

```
Controller  → recebe HTTP, valida entrada (@Valid), delega pro Service.
              Não tem lógica de negócio.
Service     → regra de negócio. Orquestra Repository(s). Lança exceções
              de domínio quando algo foge da regra.
Repository  → interface Spring Data JPA. Única camada que toca o banco.
Model       → entidade JPA (@Entity). Representa uma tabela.
DTO         → objeto de entrada/saída da API. Nunca expor a Entity direto
              (especialmente nunca expor senha/hash).
Mapper      → MapStruct. Converte Entity ↔ DTO automaticamente.
```

Fluxo de dependência estrito, igual ao Biulix:
`Controller → Service → Repository → Model`

---

## 4. Regra de segurança de autenticação (decisão registrada)

- Login por e-mail + senha (BCrypt hash, nunca texto puro)
- Ao logar com sucesso, gera um JWT contendo email (subject) + usuarioId
- **Sem Redis**: o token é válido até expirar (`app.jwt.expiration-ms`),
  não há como revogar antes da hora. Isso foi uma escolha consciente para
  simplificar a infraestrutura de um projeto pessoal. Se um dia eu quiser
  logout "de verdade" (invalidação imediata), a solução é reintroduzir
  Redis ou uma blocklist de tokens — não fazer isso sem eu pedir.
- O segredo do JWT (`app.jwt.secret`) DEVE vir de variável de ambiente
  (`JWT_SECRET`). A aplicação deve recusar subir se isso não estiver
  configurado. Nunca hardcodar.

---

## 5. Módulos do sistema (domínio do projeto)

Cada módulo é uma pasta própria dentro de `com.rotina.<artifact>`, seguindo
o princípio de "feature autocontida" (mesmo princípio do Biulix front-end,
adaptado pro backend). Dentro de cada módulo:
`controller/ service/ repository/ model/ model/dto/ model/mapper/`

### 5.1 `shared/` (infraestrutura transversal, não é feature)
- `GlobalExceptionHandler` — intercepta toda exceção da aplicação inteira,
  devolve sempre o mesmo formato JSON de erro (`ErroResponse`)
- Exceções de domínio: `NegocioException` (base), `RecursoNaoEncontradoException`
  (404), `ConflitoException` (409), `CredenciaisInvalidasException` (401)

### 5.2 `usuario/` (identidade central — equivalente ao "UB" do Biulix)
Entidade raiz. TODO módulo abaixo depende de um `usuario_id`. Sem usuário,
nenhum dado de outro módulo pode existir.

```
Usuario (id, nome, email, senhaHash, dataCriacao, ativo)
```

### 5.3 `auth/` (não é uma tabela — é lógica de segurança)
- `security/JwtUtil` — gera/valida token
- `security/JwtAuthenticationFilter` — lê o token em cada requisição
- `security/UserDetailsServiceImpl` — ensina o Spring Security a achar
  usuário pelo e-mail
- `security/SecurityConfig` — liga tudo, define rotas públicas/privadas, CORS
- `controller/AuthController` — endpoints `POST /auth/cadastro` e
  `POST /auth/login`

### 5.4 `treino/`
```
Esporte (id, nome)                                        -- ex: Academia, Corrida
Treino (id, esporte_id, usuario_id, nome)                 -- ex: "Treino A - Peito"
Exercicio (id, treino_id, nome, series, repeticoes, carga, video_path, ordem)
```
Vídeo de execução = upload próprio, salvo em disco, só o path na entidade.

### 5.5 `estudo/`
```
Trilha (id, usuario_id, nome)          -- ex: "Java Backend"
Topico (id, trilha_id, nome, status[PENDENTE|EM_ANDAMENTO|CONCLUIDO], ordem)
```

### 5.6 `financas/`
```
Categoria (id, usuario_id, nome, tipo[RECEITA|DESPESA])
Lancamento (id, usuario_id, categoria_id, descricao, valor, tipo, data)
```
Gráficos de % são **calculados via query agregada no Service**
(`SUM(valor) GROUP BY categoria`), nunca guardados como dado derivado
no banco. Endpoint: `GET /financas/resumo?mes=...`.

### 5.7 `recordacoes/` (módulo de memórias/diário fotográfico)
```
Recordacao (id, usuario_id, foto_path, titulo, texto, data_registro)
```
Mesma regra do vídeo de treino: foto salva em disco/storage, banco só
guarda o path.

---

## 6. Regras de comportamento que a IA deve seguir NESTE projeto

1. **Sempre explicar antes de codar.** Sou novo em backend. Antes de criar
   qualquer arquivo, explique: o que é essa peça, por que ela existe, onde
   ela se encaixa no fluxo, e qual problema resolve. Assuma que talvez eu
   não conheça o conceito.
2. **Crie as pastas e arquivos você mesmo**, seguindo exatamente a estrutura
   descrita na seção 5. Não me peça para criar pastas manualmente — você
   tem acesso ao terminal/filesystem do projeto.
3. **Uma peça por vez.** Não gere o módulo inteiro de uma vez. Crie uma
   classe, explique, espere eu confirmar entendimento (ou pedir para
   continuar), então siga para a próxima.
4. **Nunca hardcode segredo, senha ou URL de banco.** Sempre variável de
   ambiente com fallback de dev documentado no `application.properties`.
5. **Nunca exponha a entidade JPA direto num endpoint.** Sempre passar por
   DTO + Mapper.
6. **Se faltar contexto** (ex: não sei se algo vai em Service ou num
   componente auxiliar), pare e pergunte — não assuma.
7. **Código cirúrgico**: ao alterar um arquivo já existente, mostre só o
   trecho relevante (diff), não reescreva o arquivo inteiro, a menos que
   seja um arquivo novo.
8. **Sempre que citar uma decisão de arquitetura**, diga rapidamente onde
   esse mesmo padrão é comum no mercado (ex: "isso é o padrão Repository,
   usado na maioria dos projetos Spring para isolar acesso a dados").

---

## 7. Status atual do projeto (atualizar conforme avança)

- [x] Projeto gerado via Spring Initializr (Maven, Java 17, Spring Boot
      4.0.7, dependências: Web, JPA, PostgreSQL Driver, Security,
      Validation, Lombok)
- [x] Módulo `shared/` (exceções + GlobalExceptionHandler) — desenhado,
      ainda não criado dentro do projeto real
- [x] Módulo `usuario/` (Entity, Repository, DTOs, Mapper, Service) —
      desenhado, ainda não criado dentro do projeto real
- [ ] Módulo `auth/` — não iniciado dentro do projeto real
- [ ] Módulo `treino/` — não iniciado
- [ ] Módulo `estudo/` — não iniciado
- [ ] Módulo `financas/` — não iniciado
- [ ] Módulo `recordacoes/` — não iniciado
- [ ] PostgreSQL instalado localmente — não confirmado
- [ ] `application.properties` configurado — não confirmado dentro do
      projeto real

---

## 8. Próximo passo sugerido

Criar o módulo `shared/` (exceções + handler global) primeiro, pois todos
os outros módulos dependem dele para tratar erro de forma consistente.
Depois `usuario/`, depois `auth/`, depois os módulos de domínio
(treino, estudo, finanças, recordações) — cada um seguindo o mesmo
padrão de camadas, então tende a ficar mais rápido a partir do segundo.
