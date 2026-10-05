# Regras e Objetivos: Kotlin + Mobile

> Este arquivo é a fonte da verdade do progresso. Sempre que perder o contexto da conversa (novo chat, sessão limpa), leia este arquivo primeiro.

---

## COMO RETOMAR EM OUTRA SESSÃO

> **Atualizado em 2026-10-05.** Tudo fica num repo só: `/Users/macbook/AndroidStudioProjects/kotlin-basic-projects` (GitHub `adrielhenry33/kotlin-basic-projects`). Os docs ficam em `docs/` e cada projeto Android numa pasta irmã (`TemperatureConverter/`, depois `GuessingGame/` etc.). **Jeito de trabalhar:** abrir a sessão do Claude na **raiz do repo** (`cd ~/AndroidStudioProjects/kotlin-basic-projects && claude`). O `CLAUDE.md` da raiz carrega as regras sozinho, sem precisar colar prompt. **Ele fica só local, fora do git (decisão de 2026-10-05):** num clone novo ele não vem, e é preciso recriá-lo a partir das regras de tutoria abaixo. No Android Studio, abrir **a pasta do projeto** (ex.: `TemperatureConverter/`), não a raiz. Os exercícios de Kotlin puro (console) continuam no repo separado `~/IdeaProjects/kotlin-estudos`. *Histórico: até 2026-10-04 estes docs ficavam no `kotlin-estudos`, e os projetos Android ficavam soltos em `~/AndroidStudioProjects`.*

**Arquivos:**
- Progresso (estado): `docs/PROGRESSO.md`
- Teoria (conteúdo): `docs/TEORIA.md`
- PDF da teoria: `~/Desktop/kotlin-estudos-teoria.pdf`, gerado com `/usr/bin/python3 docs/gerar_teoria_pdf.py` (rodar da raiz do repo)

**Regras de tutoria:**
1. Ler este `PROGRESSO.md` no início (roadmap, `👈 VOCÊ ESTÁ AQUI`, PENDÊNCIAS, último LOG) e seguir as regras de método/scaffold daqui.
2. **Não assumir conceito:** nada da lista PENDÊNCIAS entra em exemplo/exercício antes de ser explicado (o que é, por que existe, ponte React/TS). Ao ensinar, marcar ✅ com data.
3. **Padrão de mercado:** sempre mostrar a forma mais correta/idiomática de implementar, mesmo em curiosidades.
4. **Cenários variados:** não prender exemplos só a GodiTrack/Orchestror (e-commerce, música, clima, jogos, finanças...).
5. **Caderno:** o usuário anota à mão. Teoria com blocos `> 📝 Caderno` curtos, tabelas e diagramas ASCII copiáveis.
6. **Sincronizar a cada progresso:** atualizar `PROGRESSO.md` (roadmap + LOG no topo) e `TEORIA.md`, e regerar o PDF.
7. Responder em português, tom franco e técnico; dúvida = scaffold (dica 1 → 2 → 3 → solução só se pedir).
8. **O usuário faz tudo nos projetos (pedido em 2026-10-05).** O Claude **não edita** arquivo de projeto (código, Gradle, `.gitignore`, config do Studio), não roda comando que mude o projeto nem faz commit por ele: só ensina e explica o que fazer. A exceção são os docs da trilha (`docs/PROGRESSO.md`, `docs/TEORIA.md`, o PDF e o `CLAUDE.md`), que o Claude mantém atualizados.

---

## OBJETIVO GERAL

Dominar Kotlin nativo com Jetpack Compose para desenvolvimento Android/Mobile. Aplicar conhecimentos em projetos reais (GodiTrack e Orchestror). Transicionar de React Native/TypeScript pra Kotlin com compreensão profunda de padrões, genéricos e clean architecture.

---

## MÉTODO DE APRENDIZADO

Scaffold progressivo: começar com dicas leves, aumentar especificidade conforme necessário. Nunca entregar código pronto a menos que explicitamente pedido. Tom franco e técnico. Conectar padrões a genéricos (`<T, K, R>`) sempre que relevante. Sinalizar soluções over-engineered. Avisar quando a conversa aproximar do limite de tokens (migrar pra novo chat).

Estrutura: Teoria fundamentada + Exercícios práticos + Aplicações em projetos reais. Cada tópico tem Nível 1 (Fundações), Nível 2 (Prático Simples), Nível 3+ (Avançado), Nível 4 (Projetos Reais).

---

## ROADMAP VISUAL (onde estamos)

> Atualizar sempre que a posição mudar. `✅` concluído · `🔄` em andamento · `⬜` não iniciado · `👈 VOCÊ ESTÁ AQUI` marca a posição exata.

```
✅ 1. Tratamento de Erros (Try/Catch, Result<T>)
     │
     ▼
✅ 2. Collections Avançadas (fold, reduce)
     │
     ▼
✅ 3. Coroutines (suspend, scopes, dispatchers, launch, async, timeout)
     │
     ▼
🔄 4. Delegação (by lazy, by delegate)
     ├─ ✅ Nível 1: Fundações
     ├─ ✅ Nível 2: Prático Simples
     ├─ ✅ Nível 3.4: Cache + Lazy (CacheDelegate com expiração)
     └─ ⬜ Nível 4: Aplicações Reais (GodiTrack/Orchestror)
     │
     ▼
🔄 5. Flow & StateFlow
     ├─ ✅ Nível 1: Teoria (cold vs hot, flow{}, emit, collect, StateFlow)
     ├─ ✅ Nível 2: Prático (FlowEx1 a FlowEx4)
     ├─ 🔄 Nível 3+: Avançado
     │    ├─ ✅ Teoria: SharedFlow (replay, buffer, extraBufferCapacity,
     │    │             onBufferOverflow, emit vs tryEmit)
     │    ├─ ✅ Teoria: combine
     │    ├─ 🔄 Exercícios SharedFlow (src/Flow/SharedFlow/)
     │    │    ├─ ✅ Ex1 — broadcast básico multi-coletor (feito e corrigido)
     │    │    ├─ ✅ Ex2 — replay com coletor tardio (concluído em 2026-09-22 —
     │    │    │         replay = 2 ajustado, últimos 2 status entregues certinho)
     │    │    ├─ ✅ Ex3 — emit vs tryEmit + extraBufferCapacity (concluído em
     │    │    │         2026-09-21 — buffer cheio com coletor lento observado corretamente)
     │    │    ├─ ✅ Ex4 — comparação das 3 estratégias onBufferOverflow (concluído em
     │    │    │         2026-09-22 — resultado bateu com a teoria nas 3 estratégias;
     │    │    │         descoberta de bônus: tryEmit sempre true em DROP_OLDEST/DROP_LATEST)
     │    │    └─ ✅ Ex5 — StateFlow (status) + SharedFlow (evento), GodiTrack (concluído em
     │    │              2026-09-22 — resolvido com apoio direto após 3 tentativas com o mesmo
     │    │              padrão de bug: cancel() logo após launch, sem dar tempo do coletor rodar)
     │    ├─ ✅ FlowEx5.kt — debounce + flatMapLatest (concluído em 2026-09-22 — pipeline
     │    │         map/debounce/flatMapLatest/collect certo, timing conferido e bateu)
     │    ├─ ✅ FlowEx6.kt — debounce + flatMapLatest, tema Orchestror (validação de
     │    │         e-mail em tempo real) — concluído em 2026-09-28 sem ajuda, saída e
     │    │         timing conferidos
     │    ├─ ✅ FlowEx7.kt — combine, carrinho de compras: itens + cupom + tipo de
     │    │         entrega → resumo — concluído em 2026-09-28 COM solução entregue
     │    │         (usuário se perdeu no bloco do combine; revisar sem olhar depois)
     │    ├─ ✅ stateIn — teoria ✅ (2026-09-28); exercícios em src/Flow/StateIn/
     │    │    └─ ✅ StateInEx1 — Parte A ✅ (3 estratégias observadas, A3 corrigida);
     │    │              Parte B: B1/B2 sozinho, B3 (leitura + uiState) COM solução
     │    │              entregue; B4/B5 ficaram pro usuário fechar sozinho (2026-09-29)
     │    └─ ⏭️ shareIn / callbackFlow — adiado: aprender "no caminho", dentro do Compose
     └─ ⬜ Nível 4: Aplicações Reais
     │
     ▼
🔄 6. Jetpack Compose
     ├─ ✅ Nível 1: Fundações — teoria (2026-09-29, TEORIA.md §6) + remember vs
     │         rememberSaveable a fundo (§5.1) + 8 perguntas corrigidas (2026-09-30)
     ├─ 🔄 Projeto 1: Temperature Converter (`TemperatureConverter/` neste repo)
     │    ├─ ✅ Etapa 1: criar projeto (Empty Activity/Compose), build debug OK (2026-09-30)
     │    ├─ ✅ Etapa 2: tour pelo template (MainActivity, setContent, Theme, Greeting,
     │    │         @Preview, Gradle Fase 0) — teoria 2026-09-30 + aprofundamento 2026-10-02
     │    │         + revisão completa 2026-10-05; checagem PULADA a pedido (ver PENDÊNCIAS)
     │    └─ 🔄 Etapa 3: ✅ 3.1 ordem do Modifier + Column/Row/Box (teoria e
     │              LayoutPlayground.kt concluído 2026-10-05)
     │              → 3.2 TextField numérico + toDoubleOrNull   👈 VOCÊ ESTÁ AQUI
     │              → 3.3 enunciado do conversor (rememberSaveable + state hoisting, sem ViewModel)
     └─ 9 projetos curados (Temperature Converter → To-Do Notes,
        ver lista completa abaixo), nível crescente
     │
     ▼
⬜ 7. Clean Architecture
     │
     ▼
⬜ 8. Room Database
     │
     ▼
⬜ 9. Gradle + CI/CD para Android (plano detalhado em 2026-09-30)
     ├─ 🔄 Fase 0: ler/mexer no Gradle "no caminho" (desde a Etapa 2 do projeto 1)
     ├─ ⬜ Fase A: Gradle a fundo (depois de Clean Architecture + Room)
     ├─ ⬜ Fase B: CI com GitHub Actions (depois do projeto 9, To-Do Notes = testes)
     └─ ⬜ Fase C: CD (assinar release, AAB, Play Console / Firebase App Distribution)
     │
     ▼
⬜ 10. Projeto avançado: app de streaming (arquitetura estilo CloudStream)
```

---

## ROADMAP KOTLIN

- [x] **1. Tratamento de Erros** (Try/Catch, `Result<T>`) — CONCLUÍDO
- [x] **2. Collections Avançadas** (fold, reduce) — CONCLUÍDO
- [x] **3. Coroutines** (suspend, scopes, dispatchers, launch, async, await, timeout) — CONCLUÍDO
- [ ] **4. Delegação** (`by lazy`, `by delegate`) — EM PROGRESSO
  - [x] Nível 1: Fundações
  - [x] Nível 2: Prático Simples (2.1, 2.2, 2.4)
  - [x] Nível 3.4: Cache + Lazy (`Exercicio4.kt` — CacheDelegate, com expiração)
  - [ ] Nível 4: Aplicações Reais
- [ ] **5. Flow & StateFlow** — INICIADO (adicionado ao roadmap por ser pré-requisito direto do Compose)
  - **Regra especial pros exercícios de Flow (pedido em 2026-09-14):** modo "sofrer um pouco" — dar só o enunciado, SEM scaffold de dicas não solicitadas. Só informar nome de função/método do Kotlin/Java se explicitamente perguntado. Não dizer o que fazer, a não ser que peça. Isso substitui o scaffold progressivo padrão (dica 1→2→3) apenas para este tópico.
  - [x] Nível 1: Teoria (cold vs hot, `flow{}`, `emit`, `collect`, `map`/`filter`, `MutableStateFlow`/`StateFlow`)
  - [x] Nível 2: Prático Simples (`FlowEx1.kt` pipeline; `FlowEx2.kt`/`FlowEx2b.kt` StateFlow; `FlowEx3.kt` combinação Flow+StateFlow; `FlowEx4.kt` checkpoint final — sensor de temperatura, feito sem ajuda/scaffold em 2026-09-17, confirma domínio do padrão StateFlow + launch/collect)
  - [ ] Nível 3+: Avançado (SharedFlow, operators avançados — combine, flatMapLatest, debounce) — **ATUAL** (decidido em 2026-09-17: fechar Flow/StateFlow antes de ir pra Compose)
    - Teoria de `SharedFlow`/`combine`/`flatMapLatest`/`debounce` explicada em 2026-09-17.
    - [ ] `FlowEx5.kt` criado (evolução do `BuscaViewModel` do Ex3 com `debounce`+`flatMapLatest`, simulando busca assíncrona) — sem scaffold, ainda pendente.
    - **`SharedFlow`** — teoria detalhada dada em 2026-09-17 (definição, `replay`/buffer, diferença de `StateFlow`, `MutableSharedFlow`/`.asSharedFlow()`, exemplo de múltiplos coletores, caso de uso GodiTrack "corrida cancelada" vs "status da corrida"). Continuação em 2026-09-18: `extraBufferCapacity`, `onBufferOverflow`, `BufferOverflow.SUSPEND`/`DROP_OLDEST`/`DROP_LATEST`, e `emit()` vs `tryEmit()` explicados. Exercício prático ainda não criado — **PRÓXIMO PASSO**.
    - **`combine`** — teoria dada em 2026-09-18 (definição, precisa que todos os flows já tenham emitido pra rodar, diferença de `zip`, ponte com `combineLatest` do RxJS, caso de uso Orchestror: validação de formulário com campos independentes). Exercício prático ainda não criado.
    - **Exercícios de `SharedFlow` criados em 2026-09-18** em `src/Flow/SharedFlow/` (pasta/pacote próprio, separado dos exercícios genéricos de Flow) — `SharedFlowEx1.kt` a `SharedFlowEx5.kt`, nível fundação, crescente, modo "sofrer um pouco": Ex1 broadcast básico multi-coletor — **CONCLUÍDO** (bug encontrado e corrigido pelo próprio usuário: coletores nunca cancelados travavam o `runBlocking`, já que `collect` de `SharedFlow` nunca completa sozinho — resolvido guardando os `Job` e cancelando no fim). Ex2 `replay` com coletor tardio, Ex3 `emit` vs `tryEmit` + `extraBufferCapacity`, Ex4 comparação das 3 estratégias de `onBufferOverflow`, Ex5 caso de uso GodiTrack (`StateFlow` status + `SharedFlow` evento) — pendentes, **PRÓXIMO PASSO**.
    - **Ordem combinada em 2026-09-18:** terminar `SharedFlowEx2` a `SharedFlowEx5` → `FlowEx5.kt` (debounce/flatMapLatest) → exercício de `combine` (fecha Nível 3+ de Flow) → só depois entrar em `callbackFlow`/`shareIn` (padrão de mercado pra conectar fontes externas tipo SSE/WebSocket a um `SharedFlow`, discutido em 2026-09-18 mas propositalmente adiado).
  - [ ] Nível 4: Aplicações Reais
- [ ] **6. Jetpack Compose**
  - **Objetivo combinado em 2026-09-21:** assim que começar Compose e partir pra projetos básicos, criar entre **5 e 10 projetos básicos** pra treinar Kotlin/Compose na prática, aumentando o nível de dificuldade a cada um (não são os projetos reais de Nível 4 do GodiTrack/Orchestror — são treino solto, mais numeroso e mais leve).
  - **Lista curada em 2026-09-21**, extraída do repo [`solygambas/kotlin-projects`](https://github.com/solygambas/kotlin-projects) (25 projetos didáticos, análise feita nesta sessão — a maioria usa View system/XML/LiveData no original; reproduzir adaptando pra **Compose + StateFlow/Coroutines**, que já é o padrão desta trilha, em vez de copiar a stack antiga). Ordem crescente de dificuldade:
    1. **Temperature Converter** (`08-temperature-converter`) — já é Compose puro, conversor simples, sem estado complexo. Aquecimento.
    2. **Guessing Game** (`06-guessing-game`) — Compose + ViewModel + estado observável (no original usa LiveData; reproduzir com `StateFlow`, que vocês já dominam).
    3. **Todo List** (`01-todo-list`) — CRUD em memória + lista (`LazyColumn` em vez do RecyclerView original).
    4. **Stopwatch** (`02-stopwatch`) — cronômetro; boa combinação Compose + Coroutines (`LaunchedEffect`/loop com `delay`), que já é ponto forte de vocês.
    5. **Tasks** (`07-tasks`) — MVVM + Room + lista. Ponto de entrada natural assim que o item 8 (Room) entrar no roadmap.
    6. **Mars Photos** (`10-mars-photos`) — consumo de API REST com Retrofit + Compose.
    7. **DevBytes** (`12-devbytes`) — Room + Retrofit + Coroutines + cache offline (repository/single-source-of-truth) — bom capstone antes de fechar Clean Architecture "de verdade" (item 7).
    8. **Wander** (`18-wander`) — Google Maps + localização do usuário. Tematicamente conecta direto com GodiTrack (rotas, rastreamento) — vale priorizar por relevância de domínio, mesmo não sendo o próximo da lista por dificuldade.
    9. **To-Do Notes** (`25-to-do-notes`) — testes automatizados de um projeto Android (Room + Coroutines). Ponte direta pro item 9 (CI/CD) — CI sem teste automatizado não faz muito sentido.
- [ ] **7. Clean Architecture**
- [ ] **8. Room Database**
- [ ] **9. Gradle + CI/CD para Android**: objetivo adicionado em 2026-09-21 e **detalhado em 2026-09-30** (pedido do usuário: "configurar Gradle, entender pra que serve, montar um CI", bem mais pra frente). Usuário nunca configurou CI pra Android. Plano completo na TEORIA.md §9.
  - **Quando (recomendação dada em 2026-09-30):** Gradle é aprendido em duas camadas. A **leitura** começa já (Fase 0), porque todo projeto tem `build.gradle.kts` e toda lib nova (ViewModel, Room, Retrofit) passa por ele. O **estudo a fundo** (Fase A) só depois de Clean Architecture (item 7) + Room (item 8): build types, flavors e multi-módulo só fazem sentido com um app que tenha camadas/módulos e ambiente dev/prod. **CI** (Fase B) só depois do projeto 9 (To-Do Notes, testes automatizados), porque CI sem teste só compila. **CD** (Fase C) por último, e o projeto de streaming (item 10) é o lugar pra aplicar tudo junto.
  - [ ] Fase 0, no caminho: `settings.gradle.kts`, `build.gradle.kts` (raiz e `:app`), `gradle/libs.versions.toml`, Gradle Sync, adicionar uma dependência. Começa na Etapa 2 do Temperature Converter e se repete a cada projeto.
  - [ ] Fase A, Gradle a fundo: wrapper (`gradlew`), tasks, fases do build, plugins (AGP/Kotlin/Compose), `implementation`/`api`/`testImplementation`/`ksp`, version catalog, `buildTypes` (debug/release, R8), `productFlavors` + `BuildConfig`, `signingConfigs` e segredos fora do git, multi-módulo e convention plugins (`build-logic`), build cache.
  - [ ] Fase B, CI: GitHub Actions (`setup-java`, `gradle/actions/setup-gradle`, `./gradlew lint test assembleDebug`), check obrigatório no PR, APK como artefato, ktlint/detekt, testes instrumentados em emulador (opcional).
  - [ ] Fase C, CD: assinar release no CI com secrets, `bundleRelease` (AAB), `versionCode` automático, publicar na trilha interna da Play Console ou no Firebase App Distribution.
- [ ] **10. Projeto avançado: app de streaming** — **objetivo adicionado em 2026-09-21**, a ser feito depois da leva de projetos básicos de Compose e do CI/CD, como projeto de fechamento mais avançado. Referência conceitual discutida: o app open-source **CloudStream** (Kotlin, plugin architecture, Media3/ExoPlayer) — reproduzir a arquitetura (catálogo via API legal, player com Media3, cache/favoritos com Room, paginação, Clean Architecture completa), **não** as fontes de conteúdo pirateado do projeto original.

---

## REGRAS DE PROGRESSO

Não avançar de nível sem dominar o anterior. "Dominar" = conseguir explicar e implementar sem scaffold. Exercícios são obrigatórios, não opcionais (mesmo que pareça trivial). Desafios extras são opcionais, mas fazer fortalece aprendizado. Se ficar perdido, voltar pra teoria antes de prosseguir com novo exercício. Conectar cada tópico com TypeScript/React Native que você já conhece (ponte entre linguagens).

Dúvidas durante exercícios = oportunidade de aprender, não bloqueio. Avisar quando estiver aprofundando demais em um ponto (risco de over-engineering). Sessões devem respeitar seu tempo e evitar token burnout.

---

## ESTRUTURA DE CADA TÓPICO

1. Definição técnica sólida (sem código ainda).
2. Conceitos isolados com exemplos simples.
3. Fluxos visuais e diagramas.
4. Genéricos e padrões (conexão com TypeScript).
5. Casos de uso reais (GodiTrack/Orchestror).
6. Exercícios com scaffold progressivo.

---

## CRITÉRIO DE CONCLUSÃO POR NÍVEL

- **Nível 1:** Entender definição, interfaces, contratos. Responder perguntas sem consultar código.
- **Nível 2:** Implementar 4+ exercícios (validação, logging, genérico, transformação) sem skeleton.
- **Nível 3.4 (Delegação):** Implementar cache com expiração. Demonstrar por que lazy e cache são diferentes.
- **Nível 4:** Aplicar em 2+ cenários reais (GodiTrack + Orchestror). Código funcionando em produção ou simulado.

---

## REGRAS COM DÚVIDAS

Sempre responder com scaffold: dica 1 (leve) → dica 2 (média) → dica 3 (específica) → solução completa (só se pedir "código pronto"). Dúvida sobre genéricos? Ponte com TypeScript que você conhece. Dúvida sobre padrão? Conectar com Android/mobile real.

Não deixar dúvida sem resolver. Se algo não ficar claro após 2 explicações, reformular completamente (mudar analogia, exemplo, abordagem).

---

## REGRAS COM PROJETOS REAIS

**Regra de variedade (pedido em 2026-09-28):** não prender exemplos e exercícios só ao GodiTrack/Orchestror — "nem tudo é sobre eles". Variar domínios (e-commerce, streaming, jogos, clima, finanças etc.). Os dois projetos ainda podem aparecer como exemplo de vez em quando, e continuam sendo o alvo do Nível 4.

- **GodiTrack:** foco em performance (lazy loading rotas), logging de transações motorista, sincronização servidor.
- **Orchestror:** foco em validação (email, telefone, CPF), auditoria (quem mudou contato), integridade de dados (status com transições).

Código deve ser reutilizável: 1 delegate pra múltiplas propriedades. Considerar thread-safety quando aplicável (múltiplos motoristas/usuários simultâneos). Documentar padrão usado e por quê.

**Regra fixa (pedido em 2026-09-14):** todo projeto/exercício de aplicação real (Nível 4 de qualquer tópico, GodiTrack, Orchestror) deve seguir **MVVM com princípios de Clean Architecture** (separação View / ViewModel / (Use Cases) / Repository / Data Source). Sempre que aplicável, indicar também qual é o **padrão de mercado/indústria** pra aquele problema específico (ex: como empresas resolvem isso normalmente em produção Android/Kotlin), não só a solução didática do exercício.

**Regra ampliada (pedido em 2026-09-17, ajustada no mesmo dia):** isso não é fixo/automático — avaliar o nível de aprendizado no momento. Se o conceito ainda está sendo fundamentado (ex: primeiro contato com `launch`+`collect`), ficar no básico primeiro. A versão de mercado/produção (ex: `viewModelScope`, `collectAsStateWithLifecycle()`, separação Repository) entra como evolução progressiva depois que o básico foi entendido, não como resposta obrigatória em toda pergunta de curiosidade. Avaliar a situação, não aplicar de forma rígida.

---

## PACING E RITMO

Cada sessão: máximo 2 novos exercícios ou 1 tópico completo. Equilibrar teoria (30%) com prático (70%). Se a conversa ficar muito longa, sugerir novo chat (sem perder contexto — tudo salvo aqui).

Não perder tempo em sub-tópicos que emergem naturalmente depois. Exemplo: Thread-Safety volta quando estudar Coroutines + Compose, não agora isolado.

---

## FERRAMENTAS E RECURSOS

Código roda em Kotlin local (IntelliJ). Exercícios começam com TODOs, você preenche. Desafios extras são opcionais (fortalecem, não obrigam). Sempre ter acesso a scaffold progressivo (dicas aninhadas, revelar conforme precisa).

---

## SUCESSO = QUANDO

- Você consegue explicar delegate (`by lazy` vs `by delegate`) pra alguém sem consultar código.
- Implementar validador que funciona com `String`, `Int`, `Boolean` (genéricos).
- Aplicar em GodiTrack (cache de rotas) e Orchestror (validação de contato).
- Reconhecer padrão delegate em código existente e saber quando usar.

---

## PENDÊNCIAS EM ABERTO (ver antes de usar)

> **Regra (pedido em 2026-09-29):** nada desta lista pode aparecer em exemplo ou exercício como se já fosse conhecido. Antes do primeiro uso: explicar o conceito (o que é, por que existe, ponte com React/TS), e só depois usar no código. Quando um item for ensinado, marcar ✅ com a data e onde foi praticado.

**Deixados pra trás no Flow (aprender "no caminho"):**
- [ ] `shareIn`: como o `stateIn`, só que gera um `SharedFlow` (eventos, replay configurável)
- [ ] `callbackFlow`: transformar uma API de callback (localização, sensor, WebSocket/SSE) em Flow. Conecta com o GodiTrack
- [ ] `StateInEx1`: fechar B4/B5 e o desafio extra (coletor novo depois de 6s com `WhileSubscribed(5_000)`)
- [ ] Nível 4 de Flow e Nível 4 de Delegação (aplicações reais), ainda abertos no roadmap

**Ponte ViewModel/Coroutines → Compose:**
- [ ] `ViewModel` do Jetpack e `viewModelScope` (substitui o `scope` passado no construtor dos exercícios)
- [ ] `collectAsStateWithLifecycle()` vs `collectAsState()`: como a tela observa o `StateFlow`, e por que isso casa com `WhileSubscribed(5_000)`
- [ ] Eventos únicos (navegar, snackbar): `SharedFlow`/`Channel` vs estado. Conecta com o SharedFlowEx5

**Estado no Compose:**
- [x] Recomposição: o que é, quando acontece, por que a função composable roda várias vezes (teoria 2026-09-29)
- [x] `remember` / `mutableStateOf` / `rememberSaveable` (≈ `useState`) (teoria 2026-09-29)
- [x] State hoisting / UDF (estado desce, evento sobe), o equivalente a "lifting state up" no React (teoria 2026-09-29)
- [x] `remember` vs `rememberSaveable` a fundo: o que sobrevive a quê, limites do Bundle, regra das 3 perguntas (2026-09-30, TEORIA.md §5.1)
- [ ] `@Parcelize` e `Saver`: `data class` própria no `rememberSaveable` (hoje: crash; saída provisória = um campo por `rememberSaveable`)
- [ ] `mutableStateListOf()`: lista observável (hoje o padrão ensinado é `List` imutável + `lista = lista + item`)
- [ ] `derivedStateOf` (≈ `useMemo`)
- [ ] Estabilidade (`@Stable`/`@Immutable`) e por que listas mutáveis causam recomposição extra (tópico avançado, ver depois do básico)

**Efeitos colaterais (side effects), o equivalente ao `useEffect`:**
- [ ] `LaunchedEffect(key)`: coroutine ligada ao ciclo de vida do composable; o que a `key` faz (≈ array de dependências)
- [ ] `rememberCoroutineScope()`: lançar coroutine a partir de um clique (evento), não da composição
- [ ] `DisposableEffect`: limpeza ao sair da tela (≈ o return do `useEffect`)
- [ ] `SideEffect`, `produceState`, `snapshotFlow`: ponte entre o estado do Compose e o Flow
- [ ] `rememberUpdatedState`: evitar valor "velho" dentro de um efeito longo

**Estrutura e UI:**
- [ ] Checagem da Etapa 2 (8 perguntas na TEORIA.md §6): pulada em 2026-10-05 a pedido, depois de uma revisão completa. Retomar quando o usuário quiser
- [ ] `Modifier`: introduzido em 2026-09-30; a fundo em 2026-10-02 (lista ordenada e imutável, 3 famílias, tipo vs valor em `Modifier = Modifier`, usar o `modifier` recebido no raiz). Ordem praticada em 2026-10-05 (3.1: margem = padding antes do background, borda+margem+fundo+padding numa cadeia) ✅
- [ ] Layouts: `Column`/`Row`/`Box`/`Spacer` + `Arrangement`/`Alignment` ensinados e praticados em 2026-10-05 (Etapa 3.1, `LayoutPlayground.kt` ✅); falta `LazyColumn` + `key`
- [ ] Material 3, tema e `Scaffold` (apresentados no tour da Etapa 2, 2026-09-30: `Scaffold` + `innerPadding`, `MaterialTheme.colorScheme`; slots da topBar etc. ainda não praticados)
- [ ] Navigation Compose (rotas, argumentos, ViewModel por tela). Obs.: é assim, e não com `rememberSaveable`, que um dado vai pra outra tela
- [x] Estrutura do projeto Android: `MainActivity`, `setContent`, tema, `build.gradle.kts` do app (teoria 2026-09-30, Etapa 2 do Temperature Converter)
- [x] Gradle Fase 0 (1ª passada, 2026-09-30: settings/toml/app build, BOM, tipos de `implementation`): ler `build.gradle.kts`/`libs.versions.toml` e adicionar dependência (só leitura, sem aprofundar; o a fundo é o item 9 do roadmap)
- [x] Previews (`@Preview`) (teoria 2026-09-30)
- [ ] Injeção de dependência (Hilt): só quando entrar Clean Architecture/Repository

---

## LOG DE PROGRESSO

> Cada entrada nova vai no topo, com data.

- **2026-10-05** — **Exercício 3.1 concluído pelo próprio usuário** (depois de reverter a solução aplicada pelo Claude). Com dicas: espaço sem cor = `padding` e não `border`; padding da Column no `modifier` (continuando a cadeia do recebido); gap da Row = `spacedBy`, não `padding`; `contentAlignment`; `showBackground = true` explicado (só preview × `Modifier.background`). Compila. Ficaram ajustes de estilo: argumentos posicionais (`Column(modifier, Arrangement...)`, `Box(..., Alignment.Center)`) em vez de nomeados, comentário entre a anotação e o `fun`, imports sem uso, `Theme() { }` com parênteses vazios, chips repetidos (extrair `Chip` opcional). Próximo: **3.2** (TextField numérico + `toDoubleOrNull`).

- **2026-10-05** — Sessão reaberta sem querer: teoria da Etapa 3.1 e enunciado do `LayoutPlayground.kt` repassados. Dúvida rápida: painel mostrando só `app`/`Gradle Scripts` = visão **Android**; trocar para **Project** no seletor do topo (não afeta o Gradle). Registrado na TEORIA.md §6 (bloco do monorepo). Arquivo criado em `ui/theme/` por engano; movido para `ui/` editando o `package` + `Alt+Enter` (o F6 travou no campo de package). Explicado o Compact Middle Packages (por visão). **1ª correção do 3.1:** A ✅; B com previsão certa (confundiu fundo com cor do texto), mas misturou a parte 2 dentro dela (e usou `border(8.dp, Yellow)` como espaço, que deveria ser sem cor). CardPlayList: ❌ `modifier.padding()` como instrução dentro das chaves (Modifier imutável, valor descartado: chaves = só filhos); ❌ `Row` fora da `Column` + `modifier` repassado para os filhos; faltou `= Modifier`, `spacedBy` na Row, `contentAlignment`, preview; `;` de novo. Dicas dadas: conteúdo do Box via lambda final (≈ children), gap da Row. Usuário pediu a correção: **solução completa entregue** (código pra ele colar, regra 8), com `Chip` extraído como `private` composable reutilizável. **Reforçar:** chaves `{ }` = só filhos, Modifier = parâmetro; `modifier` só na raiz. Sugerido refazer a parte 3 sem olhar. **Exceção pontual à regra 8:** o usuário autorizou o Claude a aplicar a solução no `LayoutPlayground.kt` só desta vez; `compileDebugKotlin` passou. Logo depois o usuário **reverteu para a versão dele** e preferiu receber só os comentários corretos, para corrigir sozinho. Próximo: refazer a parte 3 → 3.2 (TextField numérico + `toDoubleOrNull`).

- **2026-10-05** — **Revisão completa da Etapa 2** a pedido (fluxo ícone → Manifest → Activity → setContent → tema → Scaffold → Greeting/Modifier → Preview → Gradle; acrescentada a tabela min/compile/targetSdk, com "compileSdk sobe sem medo, targetSdk muda comportamento"). Usuário **pulou a checagem** e pediu prática → Etapa 2 ✅, checagem nas PENDÊNCIAS. **Etapa 3 iniciada:** parte 1 da mini-teoria (ordem do Modifier de fora pra dentro, "Compose não tem margin", `Column`/`Row`/`Box`/`Spacer`, `Arrangement` vs `Alignment` ≈ justifyContent/alignItems, `spacedBy` ≈ gap) na TEORIA.md §6 e **Exercício 3.1** (`LayoutPlayground.kt`: previsão A/B, margem+padding numa cadeia, `CardPlaylist`). Próximo: corrigir o 3.1 → 3.2 (TextField numérico + `toDoubleOrNull`).

- **2026-10-05** — **Git em projeto Android: o que versionar/ignorar** (pedido: "me relembrar quais arquivos ignorar"). Registrado na TEORIA.md §6, depois da Etapa 2: regra-mãe (gerado/pessoal/segredo), tabelas versionar vs ignorar, `gradle-wrapper.jar` vai pro git, as duas escolhas para a `.idea/`, pegadinhas (`.gitignore` não desversiona → `git rm -r --cached`; `/x` é relativo à pasta do `.gitignore`), `git check-ignore -v`, `restore --staged` vs `restore`. **Praticado pelo usuário:** pôs `/.idea` no `.gitignore` do projeto (não alcançava a `.idea` da raiz), depois `.idea/` no do projeto; achou o `.gitignore` da raiz (oculto no Finder, invisível no Studio) e fechou com uma regra `.idea/` lá + `git rm -r --cached TemperatureConverter/.idea` (commit `24cdcde`). Decidiu deixar o **`CLAUDE.md` só local**: `.gitignore` + `git rm --cached` (commit `b23c940`, push feito; a versão antiga segue no histórico do 1º commit). No caminho, um `git restore` sem `--staged` descartou as atualizações dos docs, refeitas pelo Claude. Dúvidas respondidas: o `./gradlew` roda dentro de `TemperatureConverter/` (ou com `-p`); git roda de qualquer pasta do repo. **Nova regra de tutoria 8:** o usuário faz tudo nos projetos, o Claude só ensina (e mantém os docs). Próximo: checagem da Etapa 2 → Etapa 3.

- **2026-10-05** — **Reorganização: repo único `kotlin-basic-projects`** (`~/AndroidStudioProjects/kotlin-basic-projects`, GitHub `adrielhenry33/kotlin-basic-projects`). `PROGRESSO.md`, `TEORIA.md` e o gerador do PDF **movidos** do `kotlin-estudos` para `docs/` (lá ficou só um aviso apontando pra cá). `TemperatureConverter/` entrou como subpasta, e os próximos projetos vêm como pastas irmãs. `CLAUDE.md` na raiz carrega as regras automaticamente. O `kotlin-estudos` continua sendo o repo dos exercícios de console.

- **2026-10-05** — App rodou no **celular real via adb Wi-Fi** (Etapa 1 confirmada no aparelho). Dúvida: o que é `uiMode = Configuration.UI_MODE_NIGHT_YES`. Explicado: `Configuration` = estado do aparelho, constantes `UI_MODE_NIGHT_*`, caminho `uiMode → isSystemInDarkTheme() → colorScheme`, anotação só vale no Studio (celular segue o sistema; trocar tema recria a Activity), `adb shell cmd uimode night yes|no`, padrão de mercado = 2 previews empilhadas. Registrado na TEORIA.md §6 (Etapa 2, depois do item 5). Próximo: checagem da Etapa 2 (quando o usuário pedir) → Etapa 3.

- **2026-10-02** — Usuário passou a rodar o app no **celular real por pareamento Wi-Fi** (emulador lento demais no Mac Intel). Pendente: confirmar o "Hello Android!" no celular (fecha a Etapa 1) e responder as 2 perguntas do `@Preview`.

- **2026-10-02** — `@Preview` reexplicado a pedido (usuário quis confirmar o entendimento): preview = foto de uma função sem rodar o app, função embrulho sem parâmetros, parâmetros da anotação, várias previews empilhadas, pegadinha do texto fora de `Surface` não mudar no modo escuro. Usuário testou `uiMode = UI_MODE_NIGHT_YES` no `GreetingPreview`. Registrado na TEORIA.md §6 (Etapa 2, item 5).

- **2026-10-02** — **Etapa 2, aprofundamento do template** (pedido: "entender melhor o template padrão, tudo do MainActivity, o que é Modifier, estrutura de pastas"). Explicado e registrado na TEORIA.md §6 ("Etapa 2, aprofundamento"): pastas completas (`gradle.properties`, `local.properties`, `gradlew`, `res/` e classe `R`, `mipmap-*dpi`, `keepRules/rules.keep` do R8, `test/` vs `androidTest/`), dois temas (`themes.xml` da janela vs `Theme.kt`), Manifest (MAIN + LAUNCHER, `exported`, `adjustResize`), `MainActivity` linha a linha (herança `: ComponentActivity()`, `override` obrigatório, `Bundle?`, sistema chama o ciclo de vida), `TemperatureConverterTheme` (`when` + dynamic color), árvore de composables, Modifier a fundo (cadeia imutável, ordem só apresentada, erro clássico `Modifier` vs `modifier`). Ajustes no PROGRESSO: removida a nota antiga sobre `SharedFlowEx3` e atualizado o prompt de retomada. Próximo: dúvidas do usuário → checagem da Etapa 2 (quando ele pedir) → Etapa 3.

- **2026-09-30** — **Etapa 1: tropeços resolvidos na sessão da raiz** (em paralelo com a sessão aberta no projeto Android). (1) AAR metadata: o `core-ktx 1.19.0` exige compileSdk 37. O usuário instalou primeiro o Build-Tools no lugar da plataforma; resolvido com a API 37 no SDK Platforms + `release(37)`, e `targetSdk` mantido em 36. Pergunta dele: "não deveria alterar o target?" → explicado compile vs target. (2) Editor com `ComposableFunction0/1 expected` enquanto o `./gradlew assembleDebug` passava = cache do IDE. (3) Emulador lento: Mac **Intel i5 4 núcleos/16 GB**, AVD Android 37.1 Play Store 16KB com 2 GB → recomendado um AVD leve (Pixel 6, API 34 Google APIs x86_64, 3 GB) + celular real/@Preview. A imagem arm64 baixada não serve num Intel. Tudo registrado na TEORIA.md §6 Projeto 1 ("Etapa 1: tropeços"), com um bloco extra "O que é o Modifier" (o usuário perguntou o que é cada parte do `MainActivity`). O "Hello Android!" no emulador ainda não foi confirmado visualmente.

- **2026-09-30** — **Sessão retomada no terminal do Android Studio**, aberta direto em `~/AndroidStudioProjects/TemperatureConverter` (sem as memórias; regras lidas daqui). **Etapa 1 ✅:** projeto criado (pacote `com.adriel.temperatureconverter`, AGP 9.3.3, Kotlin 2.2.10, Compose BOM 2026.02.01, minSdk 24) e `app-debug.apk` gerado. **Etapa 2:** tour dado (TEORIA.md §6, "Projeto 1"): mapa de pastas, `MainActivity`/`onCreate`/`setContent`, `enableEdgeToEdge` + `Scaffold`/`innerPadding`, tema (`MaterialTheme`, dynamic color, slot `content`), assinatura padrão com `modifier: Modifier = Modifier`, `@Preview`, Gradle Fase 0 (BOM, tipos de `implementation`, catalog). Próximo: 4 perguntas de checagem → Etapa 3.

- **2026-09-30** — **Item 9 (Gradle + CI/CD) detalhado a pedido.** O usuário quer aprender a configurar Gradle, entender pra que serve e montar CI, "bem futuramente". Plano em 4 fases (0 no caminho → A Gradle a fundo → B CI → C CD), no roadmap e na TEORIA.md §9. Recomendação de quando: leitura já na Etapa 2 do projeto 1; a fundo depois de Clean Architecture + Room; CI depois do projeto 9 (testes); CD por último, aplicado no projeto de streaming. Etapa 1 do Temperature Converter continua **sem status**: o projeto não foi encontrado em `~/AndroidStudioProjects`. **Decisão final:** projetos Android no lugar tradicional (`~/AndroidStudioProjects/<Projeto>`). A ideia de colocá-los dentro do repo foi considerada e descartada. A sessão do Claude continua na raiz do `kotlin-estudos`, com acesso extra liberado em `.claude/settings.local.json`, e o `kotlin-estudos` fica aberto numa segunda janela do Android Studio.

- **2026-09-30** — **Compose Nível 1 concluído.** `remember` vs `rememberSaveable` a fundo (Activity recriada ao girar, tabela do que sobrevive a quê, Bundle só aceita tipos simples, `data class` dá crash, regra das 3 perguntas; TEORIA.md §5.1). Quiz do checkout: CEP ✅ com critério errado (achou que `saveable` leva dado pra outra tela/decide consulta), `NumberFormat` ❌ (é `remember`, objeto fixo), `Endereco` ⚠️ (caiu na pegadinha do Bundle), carrinho ✅ (nenhum, precisa persistência). Padrão do erro: usar `rememberSaveable` como "o mais seguro". Dúvida respondida: o stateless (`CampoCidade`/`TextField`) aparece **dentro** do stateful (`Column` com campo + `Text`), e stateful/stateless diz quem guarda o estado, não o que aparece. 8 perguntas do Nível 1: 2 e 3 ✅; 1 (imperativa descrita errado, faltou `UI = f(estado)`), 4 (faltou `var x = 0` → tela congelada) e 5 (faltou a solução) ⚠️; 6 com dica (`lista = lista + item`); 7 com ajuste (faltava o nome da função, convenção `onValorChange`); 8: desenho certo (dono + `rememberSaveable` + desce/sobe), mas o corpo do `BotaoPlay` saiu com **solução entregue** a pedido (erros: `onPlayerChange = it` sem lambda, `TextField` no lugar de `Button`, ternário `? :`, mandar o mesmo valor em vez de `!isPlaying`). **Pontos fracos a reforçar:** sintaxe Kotlin vs JS (ternário, lambda `{ x = it }`), escolher componente pelo tipo de interação. Android Studio + SDK + emulador já instalados. Próximo: projeto 1 **Temperature Converter**, Etapa 1 (criar projeto). O usuário vai continuar numa **sessão nova no terminal do Android Studio**, e por isso foi criada a seção "COMO RETOMAR EM OUTRA SESSÃO" com o prompt.

- **2026-09-29** — Início de **Compose, Nível 1** (teoria). Visto: declarativo vs imperativo (UI = f(estado)); `@Composable` (PascalCase, `Unit`, parâmetros = props, só chamada por outra composable: paralelo com `suspend`/`Continuation` ↔ `Composer`); composição e árvore; recomposição granular (e por que o corpo não pode ter efeito colateral); `mutableStateOf` + `remember` + `by` (ponte com Delegação `getValue`/`setValue`), erros clássicos (sem remember, lista mutada com `.add`), `rememberSaveable`; state hoisting/UDF (ponte com o `ClimaViewModel`). Teoria em TEORIA.md §6 com blocos "📝 Caderno" (usuário anota à mão) e PDF regerado. Próximo: responder as 8 perguntas do Nível 1 sem consultar → correção → setup do projeto Android (Temperature Converter).

- **2026-09-29** — `StateInEx1.kt`: Parte A concluída. Roteiro de `testarEstrategia` montado com ajuda: o padrão de bug antigo voltou (`delay` dentro do `collect`, `job.cancel()` logo depois do `launch`, faltava a etapa 1 e o encerramento; tentou `this.cancel()`, que cancelaria a `main`, trocado por `coroutineContext.cancelChildren()`). Rodado e conferido: só a Eagerly liga sem coletor, só a WhileSubscribed desliga sozinha (depois do timeout, dentro da etapa 5), e Eagerly/Lazily só param com o cancelamento do scope. Na A3 acertou a/b, errou a c (achou que parava "depois dos 1000ms") e não soube a d. Bônus observado: o coletor tardio na Eagerly recebe o último valor (21.0), não o `initialValue`. Parte B: B1/B2 feitos sozinho (bug: "Sao Paulo" sem acento); na B3 não enxergou que o `combine` é entre **o resultado da busca** e `_unidade`, e não entre `_cidade` e `_unidade`. Solução entregue (`leitura = _cidade.flatMapLatest { buscarTemperatura(it) }` + `combine(...).stateIn(scope, WhileSubscribed(5_000), Carregando)`). Explicado o erro "Cannot infer type for T1" (a propriedade `leitura` não existia) e a ordem de inicialização das propriedades. **Decisão do usuário:** fechar B4/B5 sozinho e ir direto pra **Compose**, aprendendo `shareIn`/`callbackFlow` no caminho. **Pontos fracos a reforçar no Compose:** timing do roteiro de coroutines (launch → dar tempo → cancelar) e pensar pipelines como "de onde vem cada dado".

- **2026-09-28** — Teoria de `stateIn` dada (assinatura, frio→quente compartilhado, `Eagerly`/`Lazily`/`WhileSubscribed(5_000)` e o porquê no Android, pegadinhas: declarar como `val` uma vez, coroutine viva no scope, `initialValue`, variante `suspend`; `shareIn` apresentado por cima; ponte com RxJS `shareReplay`/`refCount`). Criado `src/Flow/StateIn/StateInEx1.kt` (pasta/pacote próprio `Flow.StateIn`, a pedido do usuário), com solução testada por fora antes de entregar e saída esperada conferida.

- **2026-09-28** — `FlowEx7.kt` (combine, carrinho) finalizado **com solução completa entregue** a pedido do usuário. Ele acertou sozinho os StateFlows de origem, o `update { it + item }` e as funções públicas, mas travou no bloco do `combine`. Erros conceituais: (1) `{ a, b, c -> { ... } }` (o hábito do arrow function do JS cria uma lambda que devolve outra lambda); (2) tentar atribuir `_resumo.value.subtotal = ...` dentro do combine (as propriedades são `val` e o combine não escreve estado, só devolve um valor novo); (3) usar `map` como loop em vez de `sumOf`; (4) `aplicarCupom` ignorando string vazia impedia remover o cupom. TODO 8 resolvido com `coroutineContext.cancelChildren()`. Todos os totais bateram (165/200/180/210/165). **Ponto fraco a reforçar:** lambda que devolve valor (última expressão) vs lambda com efeito colateral. Próximo: `stateIn` (substitui o padrão launch+collect+`_state.value`), depois `shareIn`/`callbackFlow`.

- **2026-09-28** — `FlowEx6.kt` concluído sem ajuda (map → debounce → flatMapLatest → collect, StateFlow exposto via `asStateFlow()`, coletor cancelado no fim). Saída conferida rodando: `Digitando` → `JaCadastrado(adriel@...)` → `Disponivel(novo@...)`. Observação de estilo: `;` no fim das linhas não é idiomático em Kotlin, e `object` em sealed class deveria ser `data object` (toString legível). Criado `FlowEx7.kt` (exercício de `combine`; cenário trocado no mesmo dia pra carrinho de compras, a pedido do usuário, pra sair do escopo GodiTrack/Orchestror). Revisão de `combine` feita antes do exercício — último exercício do Nível 3+ de Flow antes de `callbackFlow`/`shareIn`/`stateIn` e do Nível 4.

- **2026-09-22** — `SharedFlowEx2.kt` corrigido e concluído (trocou `replay` de `1`/`0` pra `2`, coletor tardio agora recebe os últimos 2 status certinho). **Fecha os 5 exercícios de `SharedFlow`** — próximo passo é `FlowEx5.kt` (debounce/flatMapLatest) e o exercício de `combine`, pra fechar o Nível 3+ de Flow/StateFlow.

- **2026-09-22** — `SharedFlowEx5.kt` concluído (caso GodiTrack: `StateFlow` pro status da corrida + `SharedFlow` pro evento de cancelamento). Usuário caiu 3 vezes seguidas no mesmo padrão de bug (`cancel()`/nested-`collect` matando o coletor antes dele rodar) em posições diferentes do arquivo; pediu a solução completa no fim, entregue e explicada — fecha o **Nível 3+ de Flow/StateFlow inteiro em exercícios de `SharedFlow`** (falta só revisar o `Ex2`, `FlowEx5` e o exercício de `combine` antes do Nível 4).

- **2026-09-22** — `SharedFlowEx4.kt` concluído (comparação `SUSPEND`/`DROP_OLDEST`/`DROP_LATEST`), depois de dois bugs de scheduling corrigidos com ajuda de review (job.cancel() logo após o launch matava o coletor antes dele se inscrever; e faltava o `delay(100)` dentro do `collect` pra simular coletor lento). Resultado final bateu exatamente com a teoria, com uma descoberta extra: **`tryEmit` sempre retorna `true` em `DROP_OLDEST`/`DROP_LATEST`, mesmo quando o valor é descartado** — só `SUSPEND` reporta `false` de verdade. Registrado no `TEORIA.md`.

- **2026-09-21** — Descoberta importante durante o `SharedFlowEx3`: provado por teste isolado que, sem nenhum coletor ativo, `tryEmit`/`emit` de um `SharedFlow` **sempre têm sucesso**, não importa o `extraBufferCapacity` — o "buffer cheio" só existe de verdade com um coletor ativo e lento. O enunciado original do TODO 4 pedia pra testar isso sem coletor (premissa errada, corrigida no arquivo e no `TEORIA.md`/PDF). Usuário também identificou e corrigiu sozinho um feedback loop que tinha colocado (chamar o "hardware" de dentro do próprio `collect` do mesmo flow).

- **2026-09-14** — Delegação Nível 3.4 concluído: `Exercicio4.kt` (CacheDelegate com expiração) implementado e explicado (getValue/setValue, `!!` vs `as T`, diferença lazy vs cache com TTL). Discussão em aberto sobre `setValue` ser write-through (aceita valor direto) vs invalidate-only (força recarregar via `carregar()`) — decisão de design registrada no exercício, não fechada como certo/errado.

- **2026-09-11** — Teoria de Flow/StateFlow explicada; criados `src/Flow/FlowEx1.kt`, `FlowEx2.kt`, `FlowEx3.kt` (pipeline, StateFlow isolado, combinação — pendentes). Delegação Nível 3.4 (`Exercicio4.kt`, CacheDelegate) segue em progresso.
