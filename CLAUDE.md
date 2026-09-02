# Rotina API — Contexto do Projeto

> Backend pessoal (Java + Spring Boot) de um app de organização de rotina de
> vida. Frontend Flutter (mobile + web) consome esta API. Projeto pessoal,
> fora de contexto de trabalho — o usuário é dev Front-end (Flutter) com
> experiência no projeto Biulix, mas **novo em back-end**.

## Como trabalhar neste projeto (regras do usuário)

1. **Sempre explicar antes de codar.** O usuário é novo em back-end — antes
   de criar qualquer arquivo, explicar o que é a peça, por que existe, onde
   se encaixa no fluxo, e que problema resolve.
2. **Uma peça por vez.** Não gerar um módulo inteiro de uma vez; criar uma
   classe, explicar, esperar confirmação antes da próxima.
3. **Nunca hardcode segredo/senha/URL de banco** — sempre variável de
   ambiente. `JWT_SECRET` não tem fallback nenhum (a app recusa subir sem
   ele). Credenciais de banco têm fallback só de dev local.
4. **Nunca expor Entity JPA direto num endpoint** — sempre DTO + Mapper
   (MapStruct).
5. **Diffs cirúrgicos**: ao alterar arquivo existente, editar só o trecho
   relevante.
6. **Só commitar/dar push quando pedido explicitamente** (ou confirmado via
   pergunta direta antes de cada push).

## Stack

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 17 |
| Framework | Spring Boot 4.0.7 (Spring Security 7, Hibernate 7, **Jackson 3** — pacote `tools.jackson.*`, não `com.fasterxml.jackson.*`) |
| Build | Maven |
| Banco de dados | PostgreSQL, hospedado no **Neon** (`neon.tech`, free tier) |
| ORM | Spring Data JPA / Hibernate |
| Autenticação | JWT stateless, **sem Redis** (decisão consciente: sem revogação instantânea de token) |
| Conversão DTO↔Entity | MapStruct |
| Boilerplate | Lombok |
| Documentação | springdoc-openapi — `/swagger-ui/index.html` |
| Deploy da API | **Render** (free tier, via `Dockerfile`, branch `develop`) |
| Keep-alive | `cron-job.org` pingando `GET /health` a cada 10min (evita o cold start do free tier) |

Pacote base: `com.rotina.rotina_api`.

## Arquitetura em camadas

```
Controller  → recebe HTTP, valida entrada (@Valid), delega pro Service.
Service     → regra de negócio, lança exceções de domínio.
Repository  → interface Spring Data JPA, única camada que toca o banco.
Model       → entidade JPA (@Entity).
DTO         → objeto de entrada/saída da API (nunca expor Entity/senha).
Mapper      → MapStruct, converte Entity ↔ DTO (em memória, não toca banco).
```
Estrutura de pastas por módulo: `controller/ service/ repository/ model/
model/dto/ model/mapper/`.

`auth/` é exceção — não é módulo de domínio (sem tabela própria), é
infraestrutura transversal de segurança (`security/`), usada por todos os
outros módulos via `@AuthenticationPrincipal UsuarioPrincipal`.

## Módulos e status

- **`shared/`** ✅ completo — `GlobalExceptionHandler` (`@RestControllerAdvice`,
  loga erros 500), exceções de domínio (`NegocioException` base,
  `RecursoNaoEncontradoException` 404, `ConflitoException` 409,
  `CredenciaisInvalidasException` 401, `AcessoNegadoException` 403),
  `PasswordEncoderConfig` (BCrypt), `OpenApiConfig` (Swagger),
  `HealthController` (`GET /health`, público).
- **`usuario/`** ✅ completo — Entity, Repository, Service (cadastro com
  e-mail normalizado + `@Transactional`, busca por id), DTOs + Mapper,
  Controller (`GET /usuarios/{id}`, só o próprio perfil).
- **`auth/`** ✅ completo — `AuthController` (`POST /auth/cadastro`,
  `POST /auth/login`), `SecurityConfig`, `JwtUtil`, `JwtAuthenticationFilter`,
  `UserDetailsServiceImpl`, `UsuarioPrincipal`. **Identidade do JWT é o
  `usuarioId`** (imutável), e-mail é só claim informativa.
- **`treino/`** ✅ CRUD básico completo — `Esporte` (4 linhas fixas
  seedadas por `EsporteSeeder` no startup: Academia, Corrida, Pular Corda,
  Calistenia), `Treino` (nome + `tempoMinutos`/`distanciaKm` opcionais,
  usados só por Corrida/Pular Corda) e `Exercicio` (nome, series,
  repeticoes, tempoSegundos, midiaPath, ordem — usado só por
  Academia/Calistenia). Nenhuma delas usa relacionamento JPA
  (`@ManyToOne`/`@OneToMany`) — todo FK é um `Long` simples, e quem junta
  os dados é o Controller, orquestrando `TreinoService` +
  `EsporteService` + os Mappers. `TreinoRepository.findByIdAndUsuarioId`
  já filtra pelo dono na query (devolve 404 pra quem não é dono, sem
  precisar de checagem manual como fizemos em `usuario/`). Endpoints:
  `GET /esportes`, `POST /treinos`, `GET /treinos`, `GET /treinos/{id}`.
  **Falta**: endpoints de editar/excluir treino e exercício, e o upload
  de mídia (vídeo/foto) do exercício (`midiaPath` só guarda o campo, sem
  endpoint de upload ainda). GPS/rastreamento ao vivo de corrida foi
  decidido como fora de escopo do backend — km/tempo são só campos
  manuais, e computá-los via GPS (se um dia quiser) é 100% trabalho do
  Flutter, sem mudar a API.
- **`estudo/`, `financas/`, `recordacoes/`**: não iniciados.

## Decisões de segurança já tomadas (não refazer sem necessidade)

- Identidade do token = `usuarioId`, não e-mail (e-mail pode mudar no
  futuro).
- `UsuarioPrincipal` (implementa `UserDetails`) reflete `usuario.ativo` via
  `isEnabled()` — conta desativada não loga.
- Toda checagem de "é meu recurso?" deve comparar **id com id**
  (`principal.getUsuarioId()`), nunca e-mail. Padrão recomendado pros
  próximos módulos: filtrar já na query (`findByIdAndUsuarioId(...)`) em vez
  de buscar e comparar manualmente no Controller.
- E-mail sempre normalizado (`trim().toLowerCase(Locale.ROOT)`) no
  cadastro e no login.
- Erros de autenticação (senha errada, conta desativada, token ausente)
  sempre com mensagem genérica — nunca revelar qual caso específico
  ocorreu.
- `SecurityConfig` tem `authenticationEntryPoint`/`accessDeniedHandler`
  customizados (401/403 no formato `ErroResponse`, não a página padrão do
  Spring).

## Pendências conhecidas

Ver [`PENDENCIAS.md`](PENDENCIAS.md) — itens de prioridade média da última
auditoria de segurança (rate limiting no login, revogação de token, etc),
ainda não resolvidos.

## Ambiente / infraestrutura

- **Neon**: banco `neondb`. Credenciais reais só em `.env` local
  (gitignored) — nunca commitadas. `.env.example` documenta as variáveis
  esperadas (`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET`).
- **Render**: serviço `rotina-api`, deploy automático a partir da branch
  `develop`, via `Dockerfile` (multi-stage: build com Maven/JDK 17,
  runtime só com JRE 17). URL pública: `https://rotina-api.onrender.com`.
  Free tier — dorme após inatividade (mitigado pelo keep-alive do
  cron-job.org).
- Rodar localmente: `source .env && sh mvnw spring-boot:run` (o `mvnw`
  local não tem permissão de execução direta, por isso `sh mvnw` em vez de
  `./mvnw`).
