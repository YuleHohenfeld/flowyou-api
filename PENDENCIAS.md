# Pendências

Lista de dívidas técnicas/segurança identificadas em auditoria (2026-09-01,
módulo `auth/` + `usuario/`). Resolver antes de avançar pros próximos
módulos de domínio (`treino/`, `estudo/`, `financas/`, `recordacoes/`).

## Prioridade alta

- [x] **Campo `ativo` do usuário não é checado no login.** ~~Resolvido
  2026-09-01: `UsuarioPrincipal` (nova classe) implementa `UserDetails`
  com `isEnabled()` refletindo `ativo`; `AuthController.login` captura
  `AuthenticationException` (cobre `DisabledException`) com mensagem
  genérica.~~ Um usuário
  desativado (`ativo = false`) continua conseguindo logar normalmente.
  Arquivo: `auth/security/UserDetailsServiceImpl.java:24` — usa o
  construtor de 3 argumentos de `User`, que hardcoda `enabled = true`.
  Corrigir usando `User.withUsername(...).disabled(!usuario.isAtivo()).build()`
  (ou construtor de 7 args), e mapear `DisabledException` no
  `AuthController` pra não cair no catch genérico.

- [x] **Cadastro duplo simultâneo pode virar 500 em vez de 409.** ~~Resolvido
  2026-09-01: `@Transactional` em `cadastrar()` + novo
  `@ExceptionHandler(DataIntegrityViolationException.class)` no
  `GlobalExceptionHandler`, devolvendo 409.~~
  `usuario/service/UsuarioService.java:19-23` — `existsByEmail` +
  `save` em statements separados, sem `@Transactional`. Dois
  `POST /auth/cadastro` com o mesmo e-mail ao mesmo tempo: o `UNIQUE`
  do banco protege os dados, mas o segundo request estoura
  `DataIntegrityViolationException`, sem handler, cai no catch-all →
  500 "Erro interno inesperado." em vez do 409 esperado. Corrigir:
  `@Transactional` no `cadastrar()` + `@ExceptionHandler(DataIntegrityViolationException.class)`
  no `GlobalExceptionHandler` devolvendo 409.

- [x] **E-mail não é normalizado (case/espaço).** ~~Resolvido 2026-09-01:
  normalizado (`trim().toLowerCase(Locale.ROOT)`) em `UsuarioService.cadastrar`
  e `AuthController.login`. Nota: e-mail com espaço nas bordas ainda falha
  na validação `@Email` do DTO antes da normalização rodar (edge case raro,
  não corrigido).~~ `"Yule@Gmail.com"` e
  `"yule@gmail.com"` viram contas diferentes hoje — quebra login (falso
  negativo) e permite duplicata (falso positivo de conta nova). Nenhum
  ponto do fluxo (`UsuarioCadastroRequestDTO`, mapper, `cadastrar`,
  `login`) faz `trim()`/`toLowerCase()`. Corrigir normalizando
  `email.trim().toLowerCase(Locale.ROOT)` num único ponto de entrada
  (no `cadastrar()` do Service e antes do `authenticate()`/`findByEmail()`
  no `AuthController`).

- [x] **Token expirado/inválido devolve 403 em vez de 401.** ~~Resolvido
  2026-09-01: `SecurityConfig` ganhou `.exceptionHandling(...)` com
  `authenticationEntryPoint` (401) e `accessDeniedHandler` (403)
  customizados, serializando `ErroResponse` (mesmo formato do resto da
  API). `JwtAuthenticationFilter` agora captura `UsernameNotFoundException`
  internamente (usuário do token não existe mais) em vez de deixar
  propagar como 500 cru.~~

## Decisão de arquitetura a tomar

- [x] **Identidade do token: `usuarioId` ou e-mail?** ~~Resolvido
  2026-09-01: decidido `usuarioId` (imutável). Subject do JWT agora é o
  `id`, e-mail virou claim informativa. Nova classe `UsuarioPrincipal`
  (implementa `UserDetails`, carrega `usuarioId`). `UsuarioController`
  compara `principal.getUsuarioId()` com o `id` da URL, não mais e-mails.~~ O JWT grava um
  claim `usuarioId` (`auth/security/JwtUtil.java:30`) que **nunca é lido
  em lugar nenhum** — toda a identidade real do sistema hoje é o e-mail
  (subject do token). Não é bug ainda, mas quebra no dia em que existir
  edição de e-mail: todo token vivo do usuário passaria a apontar pra um
  subject inexistente. Decidir agora entre usar `usuarioId` (imutável,
  recomendado) como subject/identidade real, ou remover o claim
  `usuarioId` se decidir manter e-mail. Propaga pra todo endpoint com
  dono nos próximos módulos — o padrão recomendado ali é
  `findByIdAndUsuarioId(id, usuarioAtualId)` em vez de buscar e comparar
  manualmente (como faz hoje `usuario/controller/UsuarioController.java`).

## Prioridade média

- [ ] **Charset do `JWT_SECRET` não é explícito** (`JwtUtil.java:20`,
  `secret.getBytes()` sem `StandardCharsets.UTF_8`) — segredo com
  acento pode gerar chaves diferentes em ambientes com locale
  diferente (Mac vs. container Docker no Render).
- [ ] **`orElseThrow()` sem mensagem no login** (`AuthController.java:52`)
  vira 500 genérico num caso raro (usuário deletado entre autenticar e
  buscar). Trocar por `orElseThrow(() -> new CredenciaisInvalidasException(...))`.
- [ ] **Swagger público em produção sem restrição** (`SecurityConfig.java:34`)
  — expõe o mapa completo da API pra qualquer um. Condicionar por
  profile/property antes de ir pra produção "de verdade".
- [ ] **Sem rate limiting no login** — vulnerável a força bruta e a DoS
  barato (cada tentativa custa ~100ms de BCrypt no servidor).
- [ ] **Sem revogação de token** — troca de senha ou "logout de todos os
  dispositivos" não invalida tokens já emitidos (janela de até 24h).
- [ ] **`JwtUtil.tokenValido()` engole toda exceção sem logar**
  (`JwtUtil.java:41-48`) — perde visibilidade de possíveis ataques
  (assinatura inválida) vs. erros normais (expiração).
- [x] **`GlobalExceptionHandler` não loga o erro 500** ~~Resolvido
  2026-09-01 (bônus, junto com o item 2): `@Slf4j` + `log.error(...)`
  adicionado ao catch-all.~~
- [ ] **`ddl-auto=update` e `show-sql=true` fixos** — ok para dev, mas
  arriscado/ruidoso em produção real. Trocar por env vars com fallback.

## Observação (fora de escopo por agora)

- `AuthController.cadastrar` monta a `Usuario` via `usuarioMapper.toEntity(dto)`
  e só depois passa pro Service, ou seja, a senha em texto puro trafega
  dentro do campo `senhaHash` até o Service codificar. Funciona hoje, mas
  seria mais seguro por construção se o Controller passasse o DTO direto
  pro Service e o mapeamento acontecesse lá dentro.
- Quando criar o primeiro endpoint de edição (`PATCH /usuarios/{id}`),
  cuidado: mapear o DTO direto pra uma `Usuario` nova e dar `save()`
  sobrescreve `senhaHash`/`dataCriacao` com `null`. Buscar a entidade
  existente e aplicar só os campos não-nulos do DTO.
