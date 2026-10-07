# Kotlin + Mobile — Teoria Completa

> Documento vivo. Atualizado automaticamente sempre que avançamos um tópico no `PROGRESSO.md`. Contém a teoria com exemplos de cada assunto já estudado — o `PROGRESSO.md` é a fonte da verdade do *estado* do aprendizado, este arquivo é a fonte da verdade do *conteúdo*.

Última atualização: 2026-09-30 (Temperature Converter: Etapa 1 ok, Etapa 2 tour do template + Gradle Fase 0)

---

## Roadmap em tempo real

- [x] **1. Tratamento de Erros** (Try/Catch, `Result<T>`) — CONCLUÍDO
- [x] **2. Collections Avançadas** (fold, reduce) — CONCLUÍDO
- [x] **3. Coroutines** (suspend, scopes, dispatchers, launch, async, await, timeout) — CONCLUÍDO
- [ ] **4. Delegação** (`by lazy`, `by delegate`) — EM PROGRESSO
  - [x] Nível 1: Fundações
  - [x] Nível 2: Prático Simples
  - [x] Nível 3.4: Cache + Lazy (`CacheDelegate` com expiração)
  - [ ] Nível 4: Aplicações Reais
- [ ] **5. Flow & StateFlow** — EM PROGRESSO
  - [x] Nível 1: Teoria (cold vs hot, `flow{}`, `emit`, `collect`, `map`/`filter`, `MutableStateFlow`/`StateFlow`)
  - [x] Nível 2: Prático Simples
  - [x] Nível 3+: Avançado (SharedFlow, combine, flatMapLatest, debounce, stateIn)
    - [x] Teoria de `SharedFlow` (replay, buffer, `extraBufferCapacity`, `onBufferOverflow`, `emit` vs `tryEmit`)
    - [x] Teoria de `combine`
    - [x] Exercícios `SharedFlowEx1` a `SharedFlowEx5` (`src/Flow/SharedFlow/`)
    - [x] `FlowEx5.kt` (`debounce` + `flatMapLatest`)
    - [x] `FlowEx6.kt` (`debounce` + `flatMapLatest`, Orchestror validação de e-mail)
    - [x] `FlowEx7.kt` (`combine`, carrinho de compras)
    - [x] Teoria de `stateIn`
    - [x] `StateInEx1.kt` (`src/Flow/StateIn/`) — Parte A e B3 (B4/B5 com o usuário)
    - [ ] `shareIn` / `callbackFlow` — adiado, aprender no caminho
  - [ ] Nível 4: Aplicações Reais
- [ ] **6. Jetpack Compose** — EM PROGRESSO
  - [x] Nível 1: Fundações (declarativo, `@Composable`, composição/recomposição, `remember`/`mutableStateOf`/`rememberSaveable`, state hoisting) — teoria 2026-09-29, perguntas corrigidas 2026-09-30
  - [ ] Projeto 1: Temperature Converter (Android Studio, `~/AndroidStudioProjects/TemperatureConverter`) — **ATUAL** (Etapa 1 ✅, Etapa 2 tour dado)
  - Objetivo: 5 a 10 projetos básicos de treino, nível crescente, assim que o tópico começar. Lista curada em `PROGRESSO.md`, extraída de `solygambas/kotlin-projects`.
- [ ] **7. Clean Architecture** — não iniciado
- [ ] **8. Room Database** — não iniciado
- [ ] **9. Gradle + CI/CD para Android** — não iniciado (plano em 4 fases na §9; leitura do Gradle começa no caminho, a fundo depois de Room)
- [ ] **10. Projeto avançado: app de streaming** — não iniciado (depois do CI/CD)

---

## 1. Tratamento de Erros — CONCLUÍDO

### Try/Catch

Kotlin trata `try/catch` como **expressão**, não só como statement — ou seja, pode retornar valor:

```kotlin
val numero: Int = try {
    texto.toInt()
} catch (e: NumberFormatException) {
    -1
}
```

Isso é diferente de Java/TypeScript, onde `try/catch` nunca é uma expressão. Em TS você precisaria de uma variável mutável (`let`) declarada fora do bloco.

Kotlin **não tem checked exceptions** (diferente de Java) — o compilador nunca obriga você a capturar uma exceção. Isso empurra o idioma pra um padrão diferente de tratamento de erro: `Result<T>`.

### `Result<T>`

`Result<T>` é uma classe selada da stdlib que representa **sucesso ou falha** sem lançar exceção — o equivalente conceitual de um `Either<Error, T>` ou de um retorno `{ data, error }` que você já usaria em TypeScript.

```kotlin
fun dividir(a: Int, b: Int): Result<Int> {
    return if (b == 0) {
        Result.failure(ArithmeticException("Divisão por zero"))
    } else {
        Result.success(a / b)
    }
}

val resultado = dividir(10, 0)

resultado
    .onSuccess { valor -> println("Resultado: $valor") }
    .onFailure { erro -> println("Erro: ${erro.message}") }

// ou de forma funcional:
val valorOuPadrao = resultado.getOrElse { -1 }
```

`runCatching { }` é o helper mais comum pra transformar uma chamada que pode lançar exceção em um `Result`:

```kotlin
val resultado = runCatching { texto.toInt() }
```

**Quando usar cada um:** `try/catch` pra fluxo imperativo local e simples. `Result<T>` quando o erro é parte esperada do domínio (ex: validação, parsing) e você quer forçar quem chama a lidar com sucesso/falha explicitamente, sem exceção não capturada estourando a call stack.

---

## 2. Collections Avançadas — CONCLUÍDO

### `fold`

`fold` acumula um valor percorrendo a coleção, começando de um valor inicial explícito:

```kotlin
val precos = listOf(10.0, 20.0, 30.0)
val total = precos.fold(0.0) { acumulado, preco -> acumulado + preco }
// total = 60.0
```

Assinatura genérica: `fun <T, R> Iterable<T>.fold(initial: R, operation: (acc: R, T) -> R): R` — repare que o acumulador (`R`) pode ser de um **tipo diferente** do elemento (`T`). Isso é o que torna `fold` mais poderoso que `reduce`: dá pra transformar uma `List<Produto>` num `Map<String, Double>`, por exemplo, num único fold.

Ponte com TypeScript: `fold` é o `Array.prototype.reduce(fn, initialValue)` do JS — o nome `reduce` do JS na verdade corresponde ao `fold` do Kotlin quando você passa valor inicial.

### `reduce`

`reduce` é como `fold`, mas **usa o primeiro elemento da coleção como valor inicial** — por isso não aceita coleção vazia (lança exceção) e o acumulador é obrigatoriamente do mesmo tipo do elemento:

```kotlin
val numeros = listOf(5, 3, 8, 1)
val maior = numeros.reduce { acc, atual -> if (atual > acc) atual else acc }
// maior = 8
```

**Diferença prática:** use `reduce` quando o "zero" da operação já é o primeiro elemento (ex: achar o maior, concatenar strings). Use `fold` sempre que precisar de um valor inicial customizado ou mudar de tipo no acumulador — e use `fold` como padrão seguro quando a coleção pode estar vazia.

---

## 3. Coroutines — CONCLUÍDO

### `suspend` functions

Uma função `suspend` pode **pausar** sua execução sem bloquear a thread, e ser retomada depois. É o bloco de construção básico de coroutines — só pode ser chamada de dentro de outra `suspend fun` ou de um `CoroutineScope`.

```kotlin
suspend fun buscarUsuario(id: Int): Usuario {
    delay(500) // não bloqueia a thread, só suspende a coroutine
    return Usuario(id, "Nome")
}
```

Ponte com TypeScript: `suspend fun` é conceitualmente próximo de uma `async function`, mas com uma diferença chave — em Kotlin, `suspend` sozinho **não** dispara execução em paralelo/background. Ele só marca que a função pode suspender. Quem decide em que thread/contexto ela roda é o `CoroutineScope` + `Dispatcher` usados pra lançá-la.

### `CoroutineScope`

Define o **ciclo de vida** de um grupo de coroutines — quando o escopo é cancelado, todas as coroutines filhas lançadas nele são canceladas junto. Isso resolve o problema clássico de "esqueci de cancelar essa promise/callback quando a tela fechou".

```kotlin
val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

scope.launch {
    buscarUsuario(1)
}

// ao destruir a tela/ViewModel:
scope.cancel()
```

Em produção Android, você quase nunca cria um `CoroutineScope` manualmente — usa `viewModelScope` (cancelado automaticamente quando o ViewModel morre) ou `lifecycleScope` (ligado ao ciclo de vida da Activity/Fragment).

### Dispatchers

Definem em qual conjunto de threads a coroutine roda:

- `Dispatchers.Main` — thread de UI (Android).
- `Dispatchers.IO` — otimizado pra operações bloqueantes (rede, disco, banco).
- `Dispatchers.Default` — otimizado pra trabalho pesado de CPU (parsing, cálculo).

```kotlin
suspend fun carregarDados(): List<Produto> = withContext(Dispatchers.IO) {
    api.buscarProdutos() // chamada de rede, sai da Main thread
}
```

### `launch` vs `async`/`await`

- **`launch`**: dispara uma coroutine e devolve um `Job`. Não retorna valor. Usado pra "fire and forget" (ex: uma ação que atualiza estado, mas você não precisa do resultado no ponto de chamada).
- **`async`**: dispara uma coroutine e devolve um `Deferred<T>` — que é como uma `Promise<T>` do TypeScript. Você chama `.await()` pra suspender até o resultado ficar pronto.

```kotlin
suspend fun buscarTudo() = coroutineScope {
    val usuarioDeferred = async { buscarUsuario(1) }
    val pedidosDeferred = async { buscarPedidos(1) }

    // as duas chamadas rodam EM PARALELO, não sequencialmente
    val usuario = usuarioDeferred.await()
    val pedidos = pedidosDeferred.await()
}
```

Ponte com TypeScript: `async {}.await()` é exatamente o `Promise.all` quando usado em paralelo como no exemplo acima — a diferença é que em Kotlin você controla explicitamente quando cada `async` é disparado e quando é aguardado.

### Timeout

`withTimeout` cancela a coroutine automaticamente se ela não terminar dentro do prazo, lançando `TimeoutCancellationException`:

```kotlin
try {
    val resultado = withTimeout(3000) {
        buscarUsuario(1)
    }
} catch (e: TimeoutCancellationException) {
    println("Demorou demais")
}
```

`withTimeoutOrNull` faz o mesmo, mas devolve `null` em vez de lançar exceção — geralmente preferível quando o timeout é um caso esperado do fluxo, não um erro excepcional.

---

## 4. Delegação — EM PROGRESSO

### `by lazy`

`lazy` cria uma propriedade cujo valor só é calculado **na primeira vez que é acessado**, e depois fica em cache pro resto da vida do objeto:

```kotlin
class ConfiguracaoApp {
    val configuracaoPesada: Configuracao by lazy {
        println("Calculando configuração...")
        carregarConfiguracaoDoDisco()
    }
}
```

`println` só roda na primeira leitura de `configuracaoPesada` — leituras seguintes retornam o valor já calculado sem reexecutar o bloco. Por padrão, `lazy` é thread-safe (usa `LazyThreadSafetyMode.SYNCHRONIZED`).

### `by` (delegação de propriedade genérica)

Delegação de propriedade é um contrato: qualquer objeto que implemente `getValue`/`setValue` (via operator functions) pode "assumir" o comportamento de leitura/escrita de uma propriedade:

```kotlin
class Preferencia<T>(private var valor: T) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        println("Lendo ${property.name}")
        return valor
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, novoValor: T) {
        println("Alterando ${property.name} de $valor pra $novoValor")
        valor = novoValor
    }
}

class Usuario {
    var nome: String by Preferencia("sem nome")
}
```

Isso é o mesmo mecanismo por trás de `by lazy`, `by Delegates.observable`, e de delegates customizados como cache. É comparável a um **getter/setter customizado reutilizável** — em TypeScript, o equivalente mais próximo seria um decorator de propriedade (`@observable`, por exemplo, do MobX), mas em Kotlin é um recurso nativo da linguagem, não uma convenção de biblioteca.

### Cache com expiração (`CacheDelegate`)

Padrão implementado no `Exercicio4.kt`: um delegate que guarda um valor calculado, mas o invalida depois de um tempo (TTL), forçando recálculo na próxima leitura. É a combinação de `by lazy` (evitar recálculo desnecessário) com um relógio (saber quando o cache expirou).

**Decisão de design em aberto** (registrada em 2026-09-14, não fechada como certo/errado): se `setValue` deve ser *write-through* (aceitar um valor direto, sobrescrevendo o cache) ou *invalidate-only* (só forçar releitura via um método `carregar()`, nunca aceitar valor externo direto).

**Nível 4 (aplicação real) ainda pendente** — aplicar em GodiTrack (cache de rotas) e Orchestror (validação de contato), seguindo MVVM + Clean Architecture.

---

## 5. Flow & StateFlow — EM PROGRESSO

### Cold vs Hot

- **Cold Flow** (`flow { }`): o código dentro do bloco só roda quando alguém chama `.collect()`, e roda **do zero pra cada coletor**. Se dois coletores diferentes coletarem o mesmo cold flow, cada um dispara sua própria execução independente.
- **Hot Flow** (`StateFlow`, `SharedFlow`): existe e "roda" independente de ter coletor ou não. Todos os coletores compartilham a **mesma** fonte de emissões — é broadcast, não replay individual.

Ponte com TypeScript/RxJS: cold flow é como um `Observable` "unicast" do RxJS (cada subscribe dispara uma nova execução); hot flow é como um `Subject`.

**O que "broadcast" quer dizer, na prática:** numa cold flow, cada `.collect()` reexecuta o bloco `flow { }` do zero — dois coletores geram duas execuções separadas, sem relação entre si:

```kotlin
val numeros = flow {
    println("Começou a produzir")
    emit(1)
}

launch { numeros.collect { println("A recebeu: $it") } } // imprime "Começou a produzir"
launch { numeros.collect { println("B recebeu: $it") } } // imprime "Começou a produzir" DE NOVO
```

Numa hot flow, existe **uma única emissão** compartilhada — os coletores só escutam, nenhum deles causa a emissão acontecer:

```kotlin
val eventos = MutableSharedFlow<Int>()

launch { eventos.collect { println("A recebeu: $it") } }
launch { eventos.collect { println("B recebeu: $it") } }

delay(100)
eventos.emit(1) // as DUAS coroutines acima recebem esse mesmo 1, ao mesmo tempo
```

`StateFlow` sempre carrega um valor atual justamente por ser broadcast: como pode existir um coletor que "sintoniza" depois que a transmissão já começou, precisa haver algo guardado (um replay implícito de 1 valor) pra entregar a ele. `SharedFlow` com `replay = 0` é broadcast sem gravação — quem não estava ouvindo no momento da emissão, perdeu.

### `flow { }`, `emit`, `collect`

```kotlin
fun contarAte(n: Int): Flow<Int> = flow {
    for (i in 1..n) {
        delay(100)
        emit(i)
    }
}

fun main() = runBlocking {
    contarAte(3).collect { valor -> println(valor) }
}
```

`emit` é `suspend` — só pode ser chamado de dentro do builder `flow { }` (ou de outro contexto suspenso). `collect` é o ponto onde a "produção" (emissão) e o "consumo" se conectam.

### `map` / `filter`

Operadores intermediários — transformam o flow sem coletar, e são preguiçosos (só executam quando alguém coleta o flow resultante):

```kotlin
contarAte(10)
    .filter { it % 2 == 0 }
    .map { it * 10 }
    .collect { println(it) }
```

### `MutableStateFlow` / `StateFlow`

`StateFlow` é um hot flow que **sempre tem um valor atual** — não dá pra criar um sem valor inicial, e todo novo coletor recebe imediatamente o valor vigente (é basicamente um "observable de estado", equivalente a um `BehaviorSubject` do RxJS).

```kotlin
class ContadorViewModel {
    private val _contador = MutableStateFlow(0)
    val contador: StateFlow<Int> = _contador.asStateFlow()

    fun incrementar() {
        _contador.value++
    }
}
```

Padrão de mercado: expor a versão mutável como `private`, e a pública como `StateFlow` somente-leitura (via `.asStateFlow()`) — quem está fora do ViewModel nunca deveria conseguir escrever o estado diretamente.

### `SharedFlow`

`SharedFlow` é um hot flow **sem conceito de "valor atual"** — ele é pra eventos, não pra estado. A diferença central pra `StateFlow`:

| | `StateFlow` | `SharedFlow` |
|---|---|---|
| Sempre tem valor atual? | Sim | Não (pode ter 0) |
| Coletor tardio recebe algo? | Sim, o valor vigente | Só se `replay > 0` |
| Uso típico | Estado da UI | Eventos pontuais (navegação, snackbar, cancelamento) |

```kotlin
class EventosViewModel {
    private val _eventos = MutableSharedFlow<String>(replay = 0)
    val eventos: SharedFlow<String> = _eventos.asSharedFlow()

    suspend fun disparar(evento: String) {
        _eventos.emit(evento)
    }
}
```

**`replay`**: quantos dos últimos valores emitidos ficam guardados pra entregar a um coletor que chega depois. `replay = 0` (padrão) significa que só quem estava coletando no momento da emissão recebe o valor.

**`extraBufferCapacity`**: espaço extra de buffer, além do `replay`, pra emissões que **já têm coletor(es) ativo(s)** mas que ainda não deram conta de processar o valor anterior. Ele existe só pra evitar que o `emit()` **suspenda** o produtor esperando um coletor lento — não tem nada a ver com histórico pra coletores futuros.

**Ponto de confusão comum (vale grifar): `replay` e `extraBufferCapacity` resolvem problemas diferentes, mesmo compartilhando o mesmo buffer interno.** Um coletor novo sempre começa a ler o buffer exatamente na posição `(total emitido) - replay` — nunca "mais pra trás" que isso, não importa o tamanho do `extraBufferCapacity`. Ou seja: **`extraBufferCapacity` NÃO estende quanto passado um coletor tardio consegue ver.** Só `replay` faz isso.

Prova prática (rodada nesta sessão): com `replay = 0` e `extraBufferCapacity = 10`, emitindo 3 valores e só depois iniciando um coletor — o coletor **não recebe nenhum dos três**, exatamente como se `extraBufferCapacity` fosse 0:

```kotlin
val flow = MutableSharedFlow<String>(replay = 0, extraBufferCapacity = 10)

flow.emit("aguardando")
flow.emit("motorista a caminho")
flow.emit("em andamento")

delay(50)

launch { flow.collect { println("Tela nova recebeu: $it") } }
delay(100)
// nada é impresso — extraBufferCapacity não ajuda coletor tardio, só replay ajudaria
```

**`onBufferOverflow`**: o que fazer quando o buffer (replay + extra) está cheio e chega uma nova emissão vinda de um produtor mais rápido que os coletores ativos:
- `BufferOverflow.SUSPEND` (padrão): o emissor suspende até haver espaço.
- `BufferOverflow.DROP_OLDEST`: descarta o valor mais antigo do buffer pra abrir espaço pro novo.
- `BufferOverflow.DROP_LATEST`: descarta o valor novo que está tentando entrar, mantendo o buffer como está.

### Exemplo completo comparando as 3 estratégias (`SharedFlowEx4`, 2026-09-22)

Cenário: um sensor de temperatura emite uma leitura a cada 10ms (`emitirLeitura`, via `tryEmit`); o painel que exibe (`collect`) é lento, gasta 100ms processando cada leitura. Buffer: `replay = 0`, `extraBufferCapacity = 2` (capacidade total: 2). Emite-se `1..6` seguidos.

**Regra de raciocínio pra rastrear qualquer um desses testes:** um valor só ocupa espaço no buffer se, no momento em que chega, o coletor **já estiver ocupado** processando outra coisa. Se o coletor estiver livre (parado esperando), a entrega é direta, sem passar pelo buffer.

**Trilha comum aos 3 casos**, antes de divergirem:
```
1 → emitido, mas ainda não existe inscrito nenhum (launch só agendou, não rodou) → PERDIDO
2 → coletor já inscrito e LIVRE (parado esperando) → entregue DIRETO, sem passar pelo buffer
    (a partir daqui o coletor fica ocupado, processando "2" por 100ms)
3 → coletor ocupado → vai pro buffer → buffer = {3}         (1/2)
4 → coletor ainda ocupado → vai pro buffer → buffer = {3,4} (2/2, CHEIO)
```
A partir daqui (`5` e `6` chegando com o buffer já cheio) é que cada estratégia se comporta diferente:

**`SUSPEND`** — `tryEmit` não pode esperar, então recusa na hora quando não cabe:
```
5 → buffer cheio {3,4} → tryEmit RECUSA → "Buffer cheio 5 descartado" (false)
6 → buffer ainda cheio → tryEmit RECUSA → "Buffer cheio 6 descartado" (false)
(coletor termina de processar "2", pega o buffer na ordem: "3", depois "4")
Painel recebeu: 2, 3, 4
```

**`DROP_OLDEST`** — sempre aceita o novo, expulsando o mais antigo do buffer pra abrir vaga:
```
5 → buffer cheio {3,4} → expulsa o mais antigo ("3") → buffer = {4,5} → tryEmit = true
6 → buffer cheio {4,5} → expulsa o mais antigo ("4") → buffer = {5,6} → tryEmit = true
(coletor termina de processar "2", pega o que sobrou no buffer: "5", depois "6")
Painel recebeu: 2, 5, 6
```
Ninguém que já está no buffer é "seguro" — a cada nova chegada, o mais velho de plantão é o próximo a cair. Favorece o dado **mais recente**.

**`DROP_LATEST`** — sempre aceita "com sucesso", mas descarta o próprio valor novo se não couber:
```
5 → buffer cheio {3,4} → descarta o PRÓPRIO "5" → buffer continua {3,4} → tryEmit = true (!)
6 → buffer cheio {3,4} → descarta o PRÓPRIO "6" → buffer continua {3,4} → tryEmit = true (!)
(coletor termina de processar "2", pega "3", depois "4")
Painel recebeu: 2, 3, 4
```
Quem já está no buffer é intocável pra sempre; só quem tenta entrar depois do buffer cheio corre risco. Favorece o dado **mais antigo já em fila**.

**Comparação final:**

| Estratégia | Painel recebeu | O que se perde | `tryEmit` avisa a perda? |
|---|---|---|---|
| `SUSPEND` | 2, 3, 4 | 5, 6 (recusados na hora) | Sim — devolve `false` |
| `DROP_OLDEST` | 2, 5, 6 | 3, 4 (expulsos do buffer pelos mais novos) | Não — sempre `true` |
| `DROP_LATEST` | 2, 3, 4 | 5, 6 (descartados em silêncio) | Não — sempre `true` |

**Pegadinha real, provada nesse exercício: com `DROP_OLDEST` e `DROP_LATEST`, `tryEmit` sempre retorna `true`, mesmo quando o valor foi descartado.** Só `SUSPEND` faz `tryEmit` devolver `false` de verdade quando algo se perde. Isso significa que, com `DROP_OLDEST`/`DROP_LATEST`, **o retorno booleano de `tryEmit` não serve pra saber se o SEU valor específico foi entregue** — do ponto de vista da API, a operação "não falhou" (o buffer sempre dá um jeito de acomodar a emissão, seja expulsando o mais antigo, seja ignorando o novo), só o dado em si que pode não ter sido guardado.

**Conexão com GodiTrack:** `DROP_OLDEST` é a escolha natural pra localização GPS em tempo real — se o app não consegue processar tudo a tempo, você quer a posição **mais recente** do motorista, não uma leitura antiga que já está obsoleta. Já um log de transações (onde perder qualquer evento seria inaceitável) pediria `SUSPEND`, aceitando que o produtor fique mais lento em vez de perder dado.

**`emit()` vs `tryEmit()`**: `emit()` é `suspend` — se o buffer estiver cheio e a estratégia for `SUSPEND`, ela espera até haver espaço. `tryEmit()` **não é suspend**: tenta emitir imediatamente e devolve `Boolean` dizendo se conseguiu (`true`) ou se foi descartado por falta de espaço no buffer (`false`) — essencial quando você precisa emitir de um contexto que não pode ser `suspend` (ex: um callback de hardware, um listener de UI, um `Thread` comum).

```kotlin
class RastreadorGps {
    private val _localizacoes = MutableSharedFlow<Localizacao>(
        replay = 0,
        extraBufferCapacity = 2 // aguenta 2 emissões "adiantadas" sem suspender
    )
    val localizacoes: SharedFlow<Localizacao> = _localizacoes.asSharedFlow()

    // callback do hardware — NÃO é suspend, então emit() nem compilaria aqui
    fun aoReceberDoHardware(localizacao: Localizacao) {
        val conseguiu = _localizacoes.tryEmit(localizacao)
        if (conseguiu) {
            println("Emitido: $localizacao")
        } else {
            println("Descartado (buffer cheio): $localizacao")
        }
    }
}
```

**Correção importante, descoberta e provada nesta sessão (2026-09-21): isso só acontece se já existir um coletor ativo.** Sem nenhum coletor coletando, `tryEmit`/`emit` **sempre têm sucesso**, não importa o `extraBufferCapacity` — não existe "buffer cheio" quando não tem ninguém esperando pra consumir o valor (não faz sentido recusar algo que ninguém vai ver mesmo).

Prova prática (buffer de tamanho 1, zero coletores, 5 tentativas de emissão):

```kotlin
val flow = MutableSharedFlow<Int>(replay = 0, extraBufferCapacity = 1)

repeat(5) { i ->
    println("tryEmit($i) sem coletor = ${flow.tryEmit(i)}")
}
// tryEmit(0) sem coletor = true
// tryEmit(1) sem coletor = true
// tryEmit(2) sem coletor = true
// tryEmit(3) sem coletor = true
// tryEmit(4) sem coletor = true   <- todas true, buffer de 1 "nunca enche"
```

**O cenário que realmente demonstra buffer cheio:** um coletor **ativo e lento** (que ainda não processou o valor anterior) recebendo emissões mais rápido do que consegue consumir. Só nesse caso o buffer (replay + extra) enche de verdade e `tryEmit` começa a devolver `false`:

```kotlin
launch {
    rastreador.localizacoes.collect {
        delay(200) // coletor lento — não dá conta do ritmo do produtor
        println("Processado: $it")
    }
}

delay(50) // garante que o coletor já está rodando
repeat(5) { i ->
    rastreador.aoReceberDoHardware(Localizacao(i.toDouble(), i.toDouble()))
    // a partir da 3ª chamada (replay=0 + extraBufferCapacity=2 = buffer de 2),
    // tryEmit começa a devolver false, porque o coletor lento ainda não abriu espaço
}
```

É esse o cenário que o exercício `SharedFlowEx3` pede pra observar.

**Caso de uso GodiTrack:** `StateFlow` pro **status da corrida** (sempre existe um status atual — "aguardando", "em andamento"), `SharedFlow` (`replay = 0`) pro **evento de corrida cancelada** — uma tela que abre depois do cancelamento não deveria "descobrir" um cancelamento que já passou, mas deveria ver o status atual imediatamente.

**Exemplo completo, verificado em exercício (`SharedFlowEx5`, 2026-09-22):**

```kotlin
class CorridaViewModel(private val scope: CoroutineScope) {
    private val _status = MutableStateFlow("aguardando")
    val status: StateFlow<String> = _status.asStateFlow()

    private val _evento = MutableSharedFlow<String>(replay = 0)
    val evento: SharedFlow<String> = _evento.asSharedFlow()

    fun atualizarStatus(novoStatus: String) { _status.value = novoStatus }

    fun cancelarCorrida() {
        scope.launch { _evento.emit("Corrida cancelada") }
        // sem job.cancel() aqui — emit() termina sozinho, não é um collect infinito
    }
}
```

Resultado observado com 3 coletores em momentos diferentes (status tardio, evento antes do cancelamento, evento depois do cancelamento):

```
Status recebido: em andamento                                  ← tardio, mas StateFlow entrega na hora
Cancelamento recebido (coletor de ANTES): Corrida cancelada     ← já estava ouvindo, recebe
                                                                 ← coletor de DEPOIS: nenhuma linha — SharedFlow não guarda nada pra quem chega atrasado
```

**Erro recorrente encontrado ao implementar isso:** cair de novo no padrão "`cancel()` logo após `launch`, sem nenhuma pausa no meio" — o mesmo bug do `SharedFlowEx4`, só que reaparecendo em 3 lugares diferentes do mesmo arquivo (dentro de `cancelarCorrida()`, e nos dois `launch` da `main`). Reforça a regra: **todo `launch` de um coletor precisa de um `delay` antes de qualquer `cancel()` ou emissão que dependa dele já estar rodando.**

### `combine`

Combina os **valores mais recentes** de dois ou mais flows, recalculando toda vez que **qualquer um** deles emite:

```kotlin
suspend fun <T1, T2, R> combine(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    transform: suspend (T1, T2) -> R
): Flow<R>
```

```kotlin
val email = MutableStateFlow("")
val telefone = MutableStateFlow("")

val formValido: Flow<Boolean> = combine(email, telefone) { email, tel ->
    validarEmail(email) && validarTelefone(tel)
}
```

`combine` só emite depois que **todos** os flows envolvidos já emitiram pelo menos um valor.

**Diferença de `zip`**: `zip` pareia por posição (1º com 1º, 2º com 2º) e espera ambos os lados; `combine` reage a qualquer emissão usando o último valor conhecido do outro lado, sem esperar pareamento.

Timeline:

```
FlowA:    --1--------2------------3-->
FlowB:    ------A---------B---------->
combine:  ------(1,A)-----(2,A)-(2,B)---(3,B)-->
```

Ponte com TypeScript/RxJS: `combine` é o `combineLatest` do RxJS.

**Caso de uso Orchestror:** validação de formulário com campos independentes — habilitar o botão "salvar" só quando email E telefone forem válidos, reagindo a mudança em qualquer um dos dois campos.

**Revisão (2026-09-28) — detalhes que importam na prática:**

- **Forma de extensão:** `flowA.combine(flowB) { a, b -> ... }` é equivalente a `combine(flowA, flowB) { a, b -> ... }`.
- **3 a 5 flows:** existem sobrecargas tipadas até 5 (`combine(f1, f2, f3) { a, b, c -> ... }`). Acima disso, a versão com lista/vararg entrega um `Array<T>`, e você perde a tipagem individual.
- **Com `StateFlow`, emite na hora:** `StateFlow` sempre tem valor, então `combine` de `StateFlow`s já emite a primeira combinação assim que é coletado. Com `flow { }` frio, só emite depois que todos emitiram pelo menos uma vez.
- **Retorna `Flow`, não `StateFlow`:** o resultado de `combine` é um `Flow` frio comum. Pra expor como estado, ou você coleta e joga num `MutableStateFlow` (o que estamos fazendo até agora), ou usa `stateIn(...)`, que é o padrão de mercado e o próximo assunto.
- **Não completa sozinho se as fontes forem hot:** `combine` só termina quando **todas** as fontes terminam. `StateFlow` nunca termina.
- **Conflation:** se duas fontes `StateFlow` mudam ao mesmo tempo, sem nenhuma suspensão no meio, o coletor pode ver só a combinação final e pular as intermediárias. É o mesmo comportamento de conflation do `StateFlow` que já vimos.

**`combine` vs `zip` vs `merge`:**

| Operador | Emite quando | Usa | Caso típico |
|---|---|---|---|
| `combine` | qualquer fonte emite | último valor de cada fonte | estado derivado (filtros + lista, formulário, carrinho) |
| `zip` | as duas fontes emitem o "par" seguinte | valores pareados por posição | juntar requisição N com resposta N |
| `merge` | qualquer fonte emite | só o valor que chegou (mesmo tipo) | juntar eventos de várias origens num stream só |

**Exemplo fora do escopo dos projetos — catálogo de filmes com filtros:**

```kotlin
val filmes = MutableStateFlow(listOf<Filme>())
val genero = MutableStateFlow<Genero?>(null)
val soNaoAssistidos = MutableStateFlow(false)

val filmesVisiveis: Flow<List<Filme>> =
    combine(filmes, genero, soNaoAssistidos) { lista, g, naoAssistidos ->
        lista
            .filter { g == null || it.genero == g }
            .filter { !naoAssistidos || !it.assistido }
    }
```

Mudar o gênero, marcar o toggle ou chegar um filme novo recalcula a lista visível, sempre com o último valor de cada filtro.

**Solução de referência (`FlowEx7.kt`, carrinho, 2026-09-28):**

```kotlin
init {
    scope.launch {
        combine(_itens, _cupom, _tipoEntrega) { itens, cupom, entrega ->
            calcularResumo(itens, cupom, entrega)     // última expressão = valor emitido
        }.collect { resumo ->
            _resumo.value = resumo                     // único lugar que escreve o estado
        }
    }
}

private fun calcularResumo(itens: List<ItemCarrinho>, cupom: String, entrega: TipoEntrega): ResumoCarrinho {
    val subtotal = itens.sumOf { it.precoUnitario * it.quantidade }
    val desconto = if (cupom == "DESCONTO10") subtotal * 0.10 else 0.0
    val frete = if (entrega == TipoEntrega.PADRAO && subtotal >= 200.0) 0.0 else entrega.valorFrete
    return ResumoCarrinho(subtotal, desconto, frete, subtotal - desconto + frete)
}
```

**Erros comuns ao escrever a lambda do `combine`:**

- **`{ a, b -> { ... } }`**: é o hábito do arrow function do JS/TS. Em Kotlin, as chaves de dentro criam **outra lambda**, então o `combine` emite uma função, não o seu resultado. O corpo já começa logo depois do `->`.
- **Escrever estado dentro do bloco** (`_resumo.value.subtotal = ...`): não compila, porque as propriedades de data class com `val` não podem ser reatribuídas. E mesmo que compilasse, estaria errado: o bloco do `combine` deve ser uma **transformação pura**, que recebe valores e devolve um objeto novo. Quem escreve o estado é o `collect`.
- **`map` usado como loop**: `map` serve pra transformar uma lista em outra lista. Pra somar, use `sumOf { ... }`, e pra só iterar, `forEach`.
- **Isolar a regra de negócio numa função pura** (`calcularResumo`) deixa o `combine` com uma linha só e a regra testável sem coroutine nenhuma.
- **Encerrar coletores de `StateFlow` na `main`**: `coroutineContext.cancelChildren()` cancela todos os filhos do `runBlocking` de uma vez (o collect do ViewModel e o da main). No Android, quem faz isso é o `viewModelScope`, que é cancelado no `onCleared()`.

### `debounce` e `flatMapLatest`

- **`debounce(tempoMs)`**: só deixa passar um valor se nenhum outro valor chegar dentro da janela de tempo especificada — usado pra evitar disparar uma busca a cada tecla digitada. Recebe `Duration` (ex: `300.milliseconds`).
- **`flatMapLatest`**: pra cada novo valor emitido, dispara um novo flow interno (ex: uma chamada de rede) e **cancela** o flow interno anterior se ele ainda não tiver terminado — evita que uma busca antiga "atropele" o resultado de uma busca mais recente.

**Opt-in necessário:** os dois ainda pedem anotação explícita — `debounce` exige `@OptIn(FlowPreview::class)`, `flatMapLatest` exige `@OptIn(ExperimentalCoroutinesApi::class)` (dá pra combinar os dois na mesma anotação: `@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)`). Isso é o mecanismo de opt-in do Kotlin: a biblioteca avisa que a API ainda pode mudar de forma incompatível numa versão futura, e você precisa reconhecer esse risco explicitamente pra usar.

**Pipeline completo verificado (`FlowEx5.kt`, 2026-09-22):**

```kotlin
termos
    .map { termo -> termo.trim().lowercase() }           // normaliza antes de tudo
    .debounce(300.milliseconds)                          // só passa após 300ms de silêncio
    .flatMapLatest { termo -> buscarNoServidor(termo) }   // busca, cancela a anterior se preciso
    .collect { resultado -> _busca.value = resultado }    // atualiza o estado exposto
```

Resultado observado com a sequência `"n"` → 50ms → `"no"` → 50ms → `"note"` → pausa de 600ms → `"nota"` → 600ms:

```
Resultado : []                                              ← valor inicial do StateFlow
Resultado : [Notebook, Notebook Gamer]                       ← só depois de "note" (300ms sem novo termo)
Resultado : [Nota Fiscal Impressora]                          ← só depois de "nota"
```

`"n"` e `"no"` nunca chegam a virar busca — são engolidos pelo `debounce` porque o próximo termo chega antes dos 300ms passarem.

Exercício de reforço (`FlowEx6.kt`, concluído em 2026-09-28): mesmo padrão, tema Orchestror — validação de e-mail em tempo real durante cadastro, verificando no "servidor" (simulado) se o e-mail já está cadastrado. Feito sem ajuda. Saída observada:

```
Resultado Flow.StatusEmail$Digitando@52d455b8                    ← valor inicial
Resultado JaCadastrado(email=adriel@goditransportes.com.br)      ← ~800ms (100 + 300 debounce + 400 servidor)
Resultado Disponivel(email=novo@empresa.com)                      ← ~1400ms (700 + 300 + 400)
```

**`object` vs `data object` em sealed class:** repare no `Digitando@52d455b8` — um `object` comum usa o `toString()` padrão do Java (nome da classe + hash). Desde o Kotlin 1.9, o idiomático é `data object Digitando : StatusEmail()`, que gera `toString()` = `"Digitando"` (além de `equals`/`hashCode` consistentes). Padrão de mercado pra estados sem dados dentro de `sealed class`/`sealed interface` de UI state.


### `stateIn` (teoria dada em 2026-09-28)

**Definição:** operador que transforma um `Flow` **frio** num `StateFlow` **quente**, compartilhado entre todos os coletores, com um valor atual sempre disponível. É o jeito de mercado de expor estado **derivado** num ViewModel (resultado de `combine`, `map`, consulta ao banco etc.).

```kotlin
fun <T> Flow<T>.stateIn(
    scope: CoroutineScope,      // onde a coleta do upstream vai rodar (no Android: viewModelScope)
    started: SharingStarted,    // QUANDO começar e parar de coletar o upstream
    initialValue: T             // valor do StateFlow antes do upstream emitir
): StateFlow<T>
```

Não precisa de `@OptIn`: é API estável.

**Antes e depois (carrinho do `FlowEx7`):**

```kotlin
// Antes: 4 peças manuais
private val _resumo = MutableStateFlow(ResumoCarrinho())
val resumo: StateFlow<ResumoCarrinho> = _resumo.asStateFlow()
init {
    scope.launch {
        combine(_itens, _cupom, _tipoEntrega) { i, c, e -> calcularResumo(i, c, e) }
            .collect { _resumo.value = it }
    }
}

// Depois: uma declaração
val resumo: StateFlow<ResumoCarrinho> =
    combine(_itens, _cupom, _tipoEntrega) { i, c, e -> calcularResumo(i, c, e) }
        .stateIn(scope, SharingStarted.WhileSubscribed(5_000), ResumoCarrinho())
```

As **fontes** (`_itens`, `_cupom`, `_tipoEntrega`) continuam `MutableStateFlow`, porque a View precisa alterar. Só o estado **derivado** vira `stateIn`, e ele é somente-leitura por natureza (não tem `.value =`).

**Por que "compartilhado" importa — frio vs quente:**

```kotlin
val cotacao = flow {
    println("abrindo conexão com a API")   // efeito caro
    emit(buscarCotacao())
}

// Sem stateIn: cada coletor roda o flow do zero → 2 conexões
launch { cotacao.collect { ... } }
launch { cotacao.collect { ... } }

// Com stateIn: 1 conexão, os 2 coletores recebem o mesmo valor
val cotacaoState = cotacao.stateIn(scope, SharingStarted.Lazily, null)
launch { cotacaoState.collect { ... } }
launch { cotacaoState.collect { ... } }
```

**As 3 estratégias de `SharingStarted`:**

| Estratégia | Começa a coletar o upstream | Para | Uso |
|---|---|---|---|
| `Eagerly` | imediatamente, mesmo sem coletor | nunca (só quando o scope é cancelado) | dado que precisa estar pronto antes da tela abrir |
| `Lazily` | no 1º coletor | nunca | começar sob demanda, mas manter pra sempre |
| `WhileSubscribed(ms)` | no 1º coletor | `ms` depois que o **último** coletor sai | **padrão no Android** |

```
coletores:   0 ──── 1 ──── 2 ──── 1 ──── 0 ········(5s)········ para upstream
upstream:    parado  ▶ roda ─────────────────────────────────── ■ parado
                                               ↑ se alguém voltar antes dos 5s,
                                                 o upstream nem chega a parar
```

**Por que `WhileSubscribed(5_000)` é a recomendação oficial do Android:**
- **Rotação de tela:** a Activity é destruída e recriada, e o coletor sai e volta em menos de 1s. Com os 5s de folga, o upstream **não reinicia**, então não refaz consulta nem requisição.
- **App em segundo plano:** a UI para de coletar (com `collectAsStateWithLifecycle`). Depois de 5s, o upstream para, o que economiza GPS, banco, rede e bateria. Quando o usuário volta, o `StateFlow` ainda tem o **último valor** (a tela não pisca vazia) e o upstream volta a rodar.
- `Eagerly`/`Lazily` nunca param. Isso serve pra dado leve, mas desperdiça recurso com fonte cara (localização, socket).

**Pegadinhas:**

1. **Declare como `val`, uma única vez.** `stateIn` dentro de uma função ou de um `get()` cria um `StateFlow` **novo** (e uma coleta nova) a cada chamada:
   ```kotlin
   fun resumo() = combine(...).stateIn(...)          // ERRADO: novo StateFlow a cada chamada
   val resumo = combine(...).stateIn(...)            // CERTO: um só, compartilhado
   ```
2. **O escopo mantém uma coroutine viva.** Mesmo com `WhileSubscribed`, o `stateIn` lança no `scope` uma coroutine que fica esperando coletores. Num `runBlocking`, o programa não termina sozinho: cancele com `coroutineContext.cancelChildren()` (no Android, o `viewModelScope` resolve isso no `onCleared()`).
3. **`initialValue` aparece primeiro.** Antes do upstream emitir, quem coleta recebe o valor inicial. Escolha um valor que faça sentido na tela: lista vazia, `null`, um estado `Carregando`...
4. **Conflation e `equals`:** como qualquer `StateFlow`, valores iguais ao atual (por `equals`) não são reemitidos.

**Variante `suspend` (sem valor inicial):** `val state = flow.stateIn(scope)` suspende até o upstream emitir o primeiro valor e usa esse valor como inicial. Ela usa `Eagerly` por baixo, e é útil fora da UI quando você não tem um valor inicial razoável.

**Irmão: `shareIn`** (detalhado depois): `flow.shareIn(scope, started, replay = 0)` devolve um `SharedFlow` em vez de `StateFlow`. Ele não tem valor inicial, não tem `.value` e não faz conflation por `equals`. Serve pra **eventos** ou quando não existe um "estado atual" que faça sentido.

**Ponte com RxJS:** `stateIn` ≈ `BehaviorSubject` / `shareReplay({ bufferSize: 1, refCount: true })`. `WhileSubscribed` ≈ `refCount` (com um atraso antes de desconectar). `shareIn` ≈ `share()` / `shareReplay(n)`.

---

## 6. Jetpack Compose — EM PROGRESSO

### Nível 1 — Fundações (teoria dada em 2026-09-29)

#### 1. O que é o Compose: UI declarativa

**Definição:** Jetpack Compose é o toolkit moderno de UI do Android. Você descreve **como a tela deve ser para um estado**, e o Compose se encarrega de atualizar a tela quando esse estado muda.

- **Imperativo (jeito antigo, View/XML):** você cria a tela uma vez e depois **muda ela na mão** (`textView.text = "..."`, `button.isEnabled = false`). Esquecer uma atualização gera bug de tela dessincronizada.
- **Declarativo (Compose):** você **não muda a tela**. Você muda o **estado**, e a função que descreve a tela roda de novo com o valor novo.

> 📝 Caderno
> UI = f(estado)
> Imperativo: "mude o texto para X"
> Declarativo: "a tela é assim quando o estado é X"
> Não mexo na tela, mexo no estado

**Ponte com React:** é o mesmo modelo mental do React. A diferença é que não existe JSX nem virtual DOM: são funções Kotlin normais, e um plugin do compilador rastreia quais estados cada função leu.

#### 2. Função `@Composable`

**Definição:** uma função marcada com `@Composable` descreve um pedaço da UI. Ela não devolve uma View: ela **emite** a UI para dentro da árvore do Compose.

```kotlin
@Composable
fun Saudacao(nome: String) {
    Text("Olá, $nome!")
}
```

Regras e convenções:
- Nome em **PascalCase** (`Saudacao`, não `saudacao`), como um componente React.
- A que emite UI devolve `Unit`, ou seja, não tem `return` de valor.
- Os **parâmetros são as "props"**: dados entram por parâmetro.
- Ela **só pode ser chamada de outra `@Composable`**, assim como uma `suspend` só pode ser chamada de outra `suspend` ou de uma coroutine. Não é coincidência: nos dois casos o compilador adiciona um parâmetro escondido (a `suspend` recebe `Continuation`, a `@Composable` recebe o `Composer`).
- Deve ser **rápida e sem efeito colateral**: nada de chamar API, gravar em banco ou lançar coroutine direto no corpo. Ela pode rodar muitas vezes (ver recomposição).

> 📝 Caderno
> @Composable = função que desenha um pedaço da tela
> PascalCase · devolve Unit · parâmetros = props
> Só chamada por outra @Composable (igual suspend)
> Corpo sem efeito colateral: pode rodar N vezes

**Peças mínimas usadas nos exemplos** (a fundo depois, no Nível 2):
- `Text("...")`: mostra texto.
- `Button(onClick = { ... }) { Text("...") }`: botão. A última lambda é o **conteúdo** do botão, o "children" do React. Esse padrão se chama **slot**.
- `Column { ... }`: empilha os filhos **na vertical**, como um `flex-direction: column`.

#### 3. Composição e árvore de UI

Quando a tela abre, o Compose executa as funções `@Composable` e monta uma **árvore** com o que elas emitiram. Esse processo se chama **composição**.

```
TelaClima()
 └─ Column
     ├─ Text("São Paulo")
     └─ Button
         └─ Text("Atualizar")
```

> 📝 Caderno
> Composição = rodar as @Composable e montar a árvore da UI
> Composição inicial: 1ª vez que a tela aparece

#### 4. Recomposição

**Definição:** quando um **estado lido** por uma função `@Composable` muda, o Compose **roda essa função de novo** para atualizar a árvore. É o "re-render" do React.

Ciclo:

```
estado muda ──► Compose marca quem LEU esse estado
            ──► roda de novo só essas funções (recomposição)
            ──► pula as funções cujos parâmetros não mudaram
            ──► tela atualizada
```

Consequências práticas:
- A recomposição é **granular**: só re-executa quem leu o estado que mudou, não a tela inteira.
- Uma função pode rodar **muitas vezes** (em animação, até a cada frame). Por isso o corpo não pode ter efeito colateral: uma chamada de API ali seria disparada várias vezes.
- Variável comum dentro da função é **recriada do zero** a cada recomposição. Guardar valor entre recomposições exige `remember` (próximo item).

> 📝 Caderno
> Recomposição = rodar de novo a @Composable quando um estado que ela LEU muda
> Granular: só quem leu · pula quem não mudou
> Pode rodar muitas vezes → sem efeito colateral
> Variável local morre a cada recomposição

#### 4.1 "Quem leu" = qual pedaço recompõe? (dúvida de 2026-09-29)

**Nem a tela inteira nem só a linha.** O que recompõe é o **escopo de recomposição** mais próximo que leu o estado:
- toda função `@Composable` comum é um escopo;
- toda **lambda de conteúdo** comum também é um escopo (ex.: o `{ }` do `Button`);
- **exceção:** `Column`, `Row` e `Box` são `inline`, então o `{ }` deles **não** é um escopo próprio. Uma leitura lá dentro conta como leitura da função que está em volta.

"Ler" = acessar o valor **durante a composição** (no corpo, ao montar a UI). Ler ou escrever dentro de um `onClick` **não** conta, porque ele roda no clique e não na composição.

```kotlin
@Composable
fun Tela() {
    var cliques by remember { mutableStateOf(0) }
    Column {
        Cabecalho("Loja")                         // parâmetro igual → PULADO
        Contador(cliques, onClique = { cliques++ })  // Tela LEU cliques aqui
        Rodape()                                  // sem parâmetro mudado → PULADO
    }
}
```

Quando `cliques` muda:
1. `Tela` leu `cliques` (para passar como argumento) → `Tela` roda de novo.
2. `Cabecalho` e `Rodape` recebem os mesmos parâmetros → o Compose **pula** as duas.
3. `Contador` recebe um valor novo → roda de novo.

Agora com a leitura dentro do conteúdo do `Button`:

```kotlin
@Composable
fun Contador() {
    var cliques by remember { mutableStateOf(0) }
    Text("Título")                                // NÃO roda de novo
    Button(onClick = { cliques++ }) {             // onClick: não é leitura de composição
        Text("Cliquei $cliques vezes")            // só ESTE bloco { } recompõe
    }
}
```

> 📝 Caderno
> Recompõe o ESCOPO mais próximo que leu o estado
> Escopo = função @Composable ou lambda de conteúdo
> Column/Row/Box são inline → não criam escopo
> Filho com parâmetros iguais → pulado
> Ler no onClick não conta (não é composição)
> Regra prática: ler o estado o mais perto possível de onde é usado

#### 5. Estado: `mutableStateOf` e `remember`

São duas peças com funções diferentes:

| Peça | O que faz | Ponte |
|---|---|---|
| `mutableStateOf(v)` | Cria um valor **observável**: quando muda, dispara recomposição de quem o leu | Parecido com `MutableStateFlow`, mas feito para o Compose |
| `remember { ... }` | **Guarda** um valor entre recomposições (roda o bloco só na 1ª vez) | Parecido com o `by lazy` (calcula uma vez e guarda) |
| `remember { mutableStateOf(v) }` | Estado observável que **sobrevive** às recomposições | `useState(v)` do React |

```kotlin
@Composable
fun Contador() {
    var cliques by remember { mutableStateOf(0) }

    Button(onClick = { cliques++ }) {
        Text("Cliquei $cliques vezes")
    }
}
```

Leitura linha a linha:
- `mutableStateOf(0)`: estado observável que começa em 0.
- `remember { ... }`: na 1ª composição cria o estado, e nas recomposições devolve **o mesmo** objeto.
- `by`: é **delegação de propriedade** (tópico 4!). Permite escrever `cliques` e `cliques++` em vez de `cliques.value`. Precisa dos imports `androidx.compose.runtime.getValue` e `setValue`, que são exatamente o `getValue`/`setValue` de um delegate.
- O clique muda o estado → o **bloco `{ }` do `Button`** (onde `cliques` é lido para montar a string) recompõe → o `Text` recebe uma string nova → o número muda na tela. Quem "lê" é o escopo em que `$cliques` aparece, não o `Text` em si (ver §4.1).

Erros clássicos:

```kotlin
var cliques by mutableStateOf(0)            // ❌ sem remember: volta a 0 a cada recomposição
var cliques = 0                             // ❌ não é observável: muda, mas a tela não sabe
var lista by remember { mutableStateOf(mutableListOf<String>()) }
lista.add("x")                              // ❌ mutou por dentro: o estado não percebe
lista = lista + "x"                         // ✅ valor novo → recompõe (igual imutabilidade no React)
```

Genéricos por trás (mesmo desenho de `StateFlow`/`MutableStateFlow`):

```kotlin
fun <T> mutableStateOf(value: T): MutableState<T>
interface State<out T> { val value: T }                  // só leitura
interface MutableState<T> : State<T> { override var value: T }  // leitura + escrita
inline fun <T> remember(calculation: () -> T): T
```

**`remember` vs `rememberSaveable`:**
- `remember` sobrevive à **recomposição**, mas se perde ao **girar a tela** (a Activity é recriada) ou quando o composable sai da tela.
- `rememberSaveable` sobrevive também a girar a tela e à morte do processo, porque salva num `Bundle`. Serve para tipos simples (texto digitado, número, booleano).

> 📝 Caderno
> mutableStateOf = valor observável (muda → recompõe)
> remember = guarda entre recomposições
> var x by remember { mutableStateOf(0) } ≈ useState(0)
> Sem remember → reseta · sem mutableStateOf → tela não sabe
> Lista: criar nova (lista + item), nunca .add()
> rememberSaveable = sobrevive a girar a tela

#### 5.1 `remember` vs `rememberSaveable` a fundo (2026-09-30)

**Por que existe:** no Android, uma **mudança de configuração** (girar a tela, tema claro/escuro, idioma, redimensionar janela) faz o sistema **destruir e recriar a Activity**. A composição inteira começa do zero.

- `remember` guarda o valor **na composição** → morre junto com ela.
- `rememberSaveable` guarda na composição **e** num `Bundle` (pacotinho que o Android preserva ao destruir a Activity e devolve ao recriar).
- Ponte React: `useState` não tem esse problema, o navegador não destrói o componente ao girar a tela.

**O que sobrevive a quê:**

| Evento | variável comum | `remember` | `rememberSaveable` |
|---|---|---|---|
| Recomposição | ❌ | ✅ | ✅ |
| Girar tela / tema / idioma | ❌ | ❌ | ✅ |
| Sistema mata o app em segundo plano e o usuário volta | ❌ | ❌ | ✅ |
| Composable sai da tela (`if` virou `false`) | ❌ | ❌ | ❌ |
| Usuário fecha o app (tira dos recentes) | ❌ | ❌ | ❌ |

`rememberSaveable` **não** é banco de dados: persistir de verdade é Room/DataStore/servidor.

**Limitação: só entra no Bundle o que o Bundle aceita.**

| Tipo | Funciona direto? |
|---|---|
| `Int`, `Boolean`, `Double`, `String` e arrays desses | ✅ |
| `data class` própria (ex.: `Endereco(rua, numero, cidade)`) | ❌ crash em tempo de execução |
| `data class` com `@Parcelize` (plugin `kotlin-parcelize`) | ✅ (ver depois) |
| Tipo com `Saver` próprio | ✅ (ver depois) |

Saída mais simples pra `data class`: um `rememberSaveable` de `String` por campo. O Bundle também é **pequeno** (centenas de KB, estourar = `TransactionTooLargeException`): lista grande da API fica no ViewModel.

**Quando usar cada um (padrão de mercado), exemplo num app de música:**

```kotlin
@Composable
fun TelaBiblioteca() {
    var busca by rememberSaveable { mutableStateOf("") }          // texto digitado
    var abaSelecionada by rememberSaveable { mutableStateOf(0) }  // aba escolhida
    var menuAberto by remember { mutableStateOf(false) }          // tudo bem fechar ao girar
    val formatador = remember { DecimalFormat("#,##0") }          // objeto, não estado
}
```

Regra das 3 perguntas, nesta ordem:
1. É **estado** (muda por ação do usuário) ou **objeto fixo** (formatador, calculadora)? Objeto → `remember` (é o `by lazy` da composição: evita recriar a cada recomposição, e recriar depois de girar devolve um objeto idêntico).
2. Precisa sobreviver a **fechar o app**? Sim → Room/DataStore/servidor.
3. Se **sumir ao girar**, o usuário reclama? Sim → `rememberSaveable` (tipo simples!). Não → `remember`. Dado de tela/negócio → ViewModel.

Pegadinhas vistas no quiz do checkout (2026-09-30):
- `rememberSaveable` **não** leva dado pra outra tela (isso é argumento de navegação ou ViewModel compartilhado).
- A escolha `remember`/`saveable` **não** decide quando consultar a API (isso é evento). Os dois guardam só o valor.
- `rememberSaveable` não é "o `remember` mais seguro": tem custo (serialização) e restrição (tipos simples).

> 📝 Caderno
> Girar tela = Activity recriada = composição do zero
> remember → só recomposição
> rememberSaveable → + girar tela + morte do processo (Bundle)
> Nenhum sobrevive a: sair da tela (if) / fechar o app
> data class no Saveable → crash (separar campos / @Parcelize / Saver)
> 1. objeto fixo? → remember  2. fechar app? → Room  3. sumir irrita? → Saveable
> Texto digitado → rememberSaveable, sempre

#### 6. State hoisting (elevar o estado) e fluxo unidirecional

**Definição:** tirar o estado de dentro de um composable e passá-lo **por parâmetro**, junto com uma função de evento. O composable vira **stateless**: ele só mostra o que recebe e avisa quando algo acontece.

Padrão de assinatura: `valor: T` + `onValorChange: (T) -> Unit`.

```kotlin
// stateless: não guarda nada, só mostra e avisa
@Composable
fun CampoCidade(cidade: String, onCidadeChange: (String) -> Unit) {
    TextField(value = cidade, onValueChange = onCidadeChange)
}

// stateful: é dono do estado
@Composable
fun TelaClima() {
    var cidade by remember { mutableStateOf("") }

    Column {
        CampoCidade(cidade = cidade, onCidadeChange = { cidade = it })
        Text("Buscando: $cidade")
    }
}
```

Fluxo unidirecional (UDF):

```
        estado desce ▼
TelaClima ───────────────► CampoCidade
          ◄───────────────
        ▲ evento sobe (onCidadeChange)
```

Por que fazer assim:
- **Uma fonte da verdade:** o `Text` e o campo mostram o mesmo `cidade`.
- **Reuso:** `CampoCidade` serve em qualquer tela.
- **Teste e preview:** um stateless é só função com parâmetro.
- **Casa com o ViewModel:** mais para frente, o dono do estado deixa de ser o `remember` e passa a ser o `StateFlow` do ViewModel. É o mesmo desenho do `ClimaViewModel` do `StateInEx1`: `trocarCidade()` é o evento que sobe, `uiState` é o estado que desce.

> 📝 Caderno
> Hoisting = estado sobe pro pai; filho recebe (valor, onChange)
> Estado desce ▼ · evento sobe ▲ (UDF)
> Stateless: só mostra e avisa · Stateful: dono do estado
> = "lifting state up" do React

#### 7. Resumo: ponte React ↔ Compose

| React | Compose |
|---|---|
| Componente | Função `@Composable` |
| Props | Parâmetros |
| `children` | Lambda de conteúdo (slot) |
| Re-render | Recomposição |
| `useState(0)` | `var x by remember { mutableStateOf(0) }` |
| Lifting state up | State hoisting |
| Imutabilidade (`[...lista, item]`) | `lista + item` |
| `useEffect` | `LaunchedEffect` / `DisposableEffect` (**ainda não visto, Nível 2**) |

#### Onde fica cada estado (visão geral, detalhes depois)

```
remember/rememberSaveable → estado de UI local (campo aberto, aba selecionada, texto digitado)
ViewModel + StateFlow     → estado da tela/negócio (dados, carregando, erro)
```

A ponte ViewModel → Compose (`viewModelScope`, `collectAsStateWithLifecycle()`) ainda **não** foi vista. Está na lista de pendências do `PROGRESSO.md`.

#### Exercício Nível 1: perguntas para responder sem consultar

1. Explique com suas palavras a diferença entre UI imperativa e declarativa, e escreva a "fórmula" do Compose.
2. Por que uma função `@Composable` não pode chamar uma API direto no corpo?
3. Em que é parecido o fato de `@Composable` só poder ser chamada por outra `@Composable` com a regra do `suspend`?
4. O que acontece com `var x by mutableStateOf(0)` sem `remember`? E com `var x = 0` com um botão fazendo `x++`?
5. Um `remember { mutableStateOf("") }` guarda o texto digitado. O usuário gira o celular. O que acontece e como resolver?
6. Uma lista em estado recebe `.add(item)` e a tela não atualiza. Por quê? Como corrigir?
7. Transforme mentalmente um composable `CampoBusca` que tem `remember` dentro numa versão stateless: qual é a assinatura?
8. Desenhe (no caderno) o fluxo estado/evento entre uma `TelaPlayer` (dona de `tocando: Boolean`) e um `BotaoPlay`.

#### Correção das perguntas (2026-09-30)

| 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 |
|---|---|---|---|---|---|---|---|
| ⚠️ | ✅ | ✅ | ⚠️ | ⚠️ | ✅ com dica | ✅ com ajuste | ✅ corpo com solução |

1. **Imperativa** = você diz *como mudar* a tela passo a passo (`findViewById(...).setText(...)`). **Declarativa** = você descreve *como a tela deve ser* para um estado; o framework calcula o que mudou. Fórmula: **`UI = f(estado)`**.
2. Corpo roda várias vezes → várias requisições; chamar API é efeito colateral. Lugar certo: side effect (`LaunchedEffect`, a ver) ou ViewModel.
3. Compilador injeta parâmetro escondido: `Continuation` (suspend) ↔ `Composer` (@Composable). Porta de entrada: `launch { }` ↔ `setContent { }`.
4. `mutableStateOf` sem `remember`: clique muda, recompõe e **recria em 0**. `var x = 0`: muda a variável, mas o Compose **não sabe** → tela congelada.
5. Perde o texto (Activity recriada). Solução: `rememberSaveable`.
6. `.add()` mexe na **mesma** lista; `lista = lista` também não funciona (mesma referência, `==` igual). Certo: `List` imutável + `itens = itens + item` (≈ `[...lista, item]`). Alternativa: `mutableStateListOf()` (ver depois).
7. `fun CampoBusca(texto: String, onTextoChange: (String) -> Unit)`: nome obrigatório, convenção `valor` + `onValorChange`.
8. Solução:

```kotlin
@Composable
fun TelaPlayer() {
    var tocando by rememberSaveable { mutableStateOf(false) }   // dona do estado

    Column {
        Text(if (tocando) "♪ Tocando agora" else "Pausado")
        BotaoPlay(
            isPlaying = tocando,                    // estado desce ▼
            onPlayerChange = { tocando = it }       // evento sobe ▲
        )
    }
}

@Composable
fun BotaoPlay(isPlaying: Boolean, onPlayerChange: (Boolean) -> Unit) {
    Button(onClick = { onPlayerChange(!isPlaying) }) {   // valor INVERTIDO
        Text(if (isPlaying) "Pausar" else "Tocar")        // if como expressão
    }
}
```

Erros do rascunho: `onPlayerChange = it` (sem lambda, `it` não existe ali), nomes diferentes na chamada e na definição, `TextField` no lugar de `Button` (digitar ≠ clicar; e `onValueChange` entrega `String`, não `Boolean`), ternário `? :` (não existe em Kotlin), `onPlayerChange(isPlaying)` mandando o mesmo valor (botão morto). Variação de mercado: `onPlayPauseClick: () -> Unit` e o pai faz `tocando = !tocando`.

> 📝 Caderno
> Button(onClick = { ... }) { Text(...) } → onClick nos ( ), conteúdo nas { }
> Toggle: onChange(!valor) · mandar o mesmo valor = botão morto
> Kotlin não tem ternário: if (x) "a" else "b"  (?: é elvis, só pra null)
> Texto do botão = AÇÃO do clique ("Pausar" quando está tocando)
> Lista em estado: List imutável + (lista = lista + item)

### Projeto 1: Temperature Converter

Pasta: `~/AndroidStudioProjects/TemperatureConverter` (pacote `com.adriel.temperatureconverter`). Etapa 1 concluída em 2026-09-30: projeto Empty Activity (Compose) criado e build debug gerado.

#### Etapa 1: tropeços e o que eles ensinam (2026-09-30)

**1. Erro de AAR metadata: `core-ktx 1.19.0` exige `compileSdk` ≥ 37.** O template trouxe a biblioteca mais nova, mas compilava contra a 36.1. A correção de mercado é subir **só o `compileSdk`** (SDK Manager → aba **SDK Platforms** → API 37, e depois `compileSdk { version = release(37) }`), e não baixar a versão da biblioteca. Build-Tools (aba SDK Tools) é outra coisa.

| Campo | O que decide |
|---|---|
| `minSdk` | o Android **mais antigo** em que o app instala |
| `compileSdk` | quais APIs o **compilador conhece**. Só afeta o build |
| `targetSdk` | para qual versão o app **declara estar pronto** (liga comportamentos novos do sistema) |

> 📝 Caderno
> minSdk ≤ targetSdk ≤ compileSdk
> compileSdk sobe logo (só build) · targetSdk sobe depois de testar (muda comportamento)
> Play Store: targetSdk no máx. ~1 ano atrás da versão mais nova

**2. Editor vermelho com o build passando** (`ComposableFunction0` / `ComposableFunction1<PaddingValues, Unit>` expected). O analisador do editor ficou com cache velho e não aplicou o plugin do Compose nas lambdas do `setContent` (Function0) e do `Scaffold` (Function1, recebe o `innerPadding`). O `./gradlew assembleDebug` passava.

> 📝 Caderno
> Build passa + editor vermelho = cache do IDE, não o código
> Quem manda é o Gradle · remédio: Sync → Invalidate Caches

**3. Emulador lento (Mac Intel i5, 4 núcleos).** A imagem Android 37.1 + Play Store + 16KB, com 2 GB de RAM, é pesada demais para essa máquina. Emulador leve: Pixel 6, **API 34 Google APIs x86_64** (sem Play Store), 3 GB de RAM, gráficos Hardware, Quick Boot, e **não fechar o emulador entre execuções**. O iOS Simulator é rápido porque não emula um celular, ele roda o app nativo no Mac. As melhores saídas são o celular de verdade (USB/Wi-Fi) e o `@Preview`.

#### Etapa 2: tour pelo template (2026-09-30)

**Mapa do projeto** (só o que importa agora):

```
TemperatureConverter/
├─ settings.gradle.kts        ← QUAIS módulos existem (include(":app")) e de onde baixar libs
├─ build.gradle.kts           ← raiz: declara plugins "apply false" (só versão, não aplica)
├─ gradle/libs.versions.toml  ← version catalog: TODAS as versões num lugar só
└─ app/
   ├─ build.gradle.kts        ← módulo do app: SDKs, compose = true, dependencies { }
   └─ src/main/
      ├─ AndroidManifest.xml  ← "RG" do app: nome, ícone, qual Activity abre primeiro
      └─ java/com/adriel/temperatureconverter/
         ├─ MainActivity.kt   ← porta de entrada
         └─ ui/theme/         ← Color.kt, Type.kt, Theme.kt
```

**Ponte React/RN:** `settings.gradle.kts` + `libs.versions.toml` + `app/build.gradle.kts` ≈ `package.json` dividido em três (workspaces / versões / deps do pacote). Gradle Sync ≈ `npm install`. `AndroidManifest.xml` ≈ `app.json` do Expo.

**1. `MainActivity`: a porta de entrada**

```kotlin
class MainActivity : ComponentActivity() {                // herda de ComponentActivity (tela do Android)
    override fun onCreate(savedInstanceState: Bundle?) {  // ciclo de vida: a tela foi criada
        super.onCreate(savedInstanceState)                // sempre chamar o pai primeiro
        enableEdgeToEdge()                                // desenha atrás da status bar/nav bar
        setContent {                                      // AQUI começa o mundo Compose
            TemperatureConverterTheme { ... }
        }
    }
}
```

- `Activity` = uma "tela" do sistema Android. Em app Compose moderno existe **uma só** (single-activity); as telas viram composables + Navigation.
- `onCreate` roda quando a Activity é criada, **inclusive ao girar a tela** (por isso `remember` perde o valor e `rememberSaveable` não, §5.1). O `savedInstanceState: Bundle?` é o Bundle de onde o `rememberSaveable` restaura.
- `setContent { }` é a ponte entre a Activity e o Compose, o equivalente ao `root.render(<App />)` / `AppRegistry.registerComponent`. Paralelo do Nível 1: `launch { }` abre o mundo suspend, `setContent { }` abre o mundo `@Composable`.
- `enableEdgeToEdge()`: o conteúdo ocupa a tela inteira, até atrás das barras do sistema. Consequência: **alguém** precisa empurrar o conteúdo pra não ficar embaixo da status bar. Esse alguém é o `Scaffold`.

**2. `Scaffold` e `innerPadding`**

```kotlin
Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
    Greeting(name = "Android", modifier = Modifier.padding(innerPadding))
}
```

- `Scaffold` = esqueleto de tela do Material 3, com slots pra `topBar`, `bottomBar`, `floatingActionButton`, `snackbarHost` e o conteúdo.
- A lambda recebe `innerPadding: PaddingValues`, o espaço ocupado pelas barras (do sistema e as do próprio Scaffold). **Regra:** aplicar esse padding no conteúdo, senão o texto fica embaixo da status bar. ≈ `SafeAreaView` / `useSafeAreaInsets()` do RN.
- `{ innerPadding -> ... }` é trailing lambda com parâmetro nomeado (em vez de `it`), o mesmo padrão do `combine { a, b -> }`.

**3. Tema: `ui/theme/`**

- `Color.kt`: constantes de cor (`Color(0xFFD0BCFF)`, ARGB em hexa).
- `Type.kt`: tipografia (tamanhos/pesos de `bodyLarge`, `titleLarge`...).
- `Theme.kt`: `TemperatureConverterTheme(content)` escolhe o `ColorScheme` (claro/escuro; **dynamic color** no Android 12+, com cores tiradas do papel de parede) e chama `MaterialTheme(colorScheme, typography, content)`.
- Tudo **dentro** do tema lê cores/fontes via `MaterialTheme.colorScheme.primary`, `MaterialTheme.typography.titleLarge`. ≈ `<ThemeProvider>` + `useTheme()` (Context do React). Por baixo é um `CompositionLocal` (ver depois).
- `content: @Composable () -> Unit` como último parâmetro = **slot**, o `children` do React. É o que permite escrever `TemperatureConverterTheme { ... }`.

**4. `Greeting`: a assinatura padrão de mercado**

```kotlin
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}
```

Convenção oficial (API guidelines do Compose): todo composable que emite UI recebe `modifier: Modifier = Modifier` como **primeiro parâmetro opcional** e o repassa pro elemento raiz. Quem chama decide tamanho/espaço/posição de fora; o componente não "chumba" layout. ≈ aceitar `style` via props. (Modifier a fundo: Etapa 3.)

**O que é o `Modifier`, em 1 minuto** (dúvida de 2026-09-30):
- Objeto que **configura um composable por fora**: tamanho, espaçamento, fundo, borda, clique, alinhamento.
- Ponte RN: faz o papel da prop `style`, mas é **encadeável** e **aplicado em ordem**: `Modifier.fillMaxSize().padding(16.dp)`.
- `fillMaxSize()` ≈ `flex: 1` (ocupa todo o espaço disponível). `padding(...)` = recuo.
- `16.dp`: `dp` é o pixel independente de densidade, a mesma unidade que o RN usa por padrão.
- Por que existe: sem ele, cada componente precisaria de dezenas de parâmetros (`width`, `padding`, `background`...). Com ele, é **um parâmetro igual em todos**.
- `= Modifier` no parâmetro é o valor padrão: um Modifier vazio, que não faz nada.

> 📝 Caderno
> Modifier = "style" encadeável e em ordem
> fillMaxSize ≈ flex:1 · padding = recuo · dp = unidade (igual RN)
> modifier: Modifier = Modifier → padrão vazio; quem chama decide o layout de fora

**5. `@Preview`**

```kotlin
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TemperatureConverterTheme { Greeting("Android") }
}
```

- Renderiza o composable **dentro do Android Studio** (modo Split/Design), sem emulador. ≈ Storybook.
- Preview **não recebe parâmetros** (só via `@PreviewParameter`), então usa dados fixos. Envolver no tema pra ver as cores certas.
- Preview não roda o app de verdade: sem rede, sem Activity real; só é clicável no "Interactive Mode".
- `debugImplementation(libs.androidx.compose.ui.tooling)` é o que faz a preview funcionar, e só existe no build debug.

**`@Preview`, segunda explicação (2026-10-02)**

- A preview **não roda o app**. O Android Studio pega **uma função** marcada com `@Preview`, executa só ela e tira uma "foto" do resultado. Sem `MainActivity`, sem `onCreate`, sem `Scaffold` (a não ser que você coloque).
- `@Preview` só funciona em função **sem parâmetros**. Por isso existe uma função "embrulho" (`GreetingPreview`) que chama o composable de verdade (`Greeting`) com dados fixos. ≈ uma story do Storybook.
- O código da preview fica no arquivo, mas **nunca é chamado pelo app**.
- Os parâmetros da anotação configuram o "aparelho de mentira": `showBackground`, `uiMode` (claro/escuro), `widthDp`, `fontScale`, `device`, `name`. Dá pra empilhar várias `@Preview` na mesma função (uma por cenário).
- **Precisa de emulador?** Preview estática e Interactive Mode: **não**, rodam dentro do Android Studio (mas precisam compilar: o 1º render demora, e com erro de compilação a preview não aparece). O botão **Run Preview** (▶ ao lado da preview): **sim**, instala só aquele composable num emulador/celular.
- Pegadinha: `Greeting` dentro do tema mas **fora de uma `Surface`/`Scaffold`** ignora o modo escuro no texto. O `MaterialTheme` muda o `colorScheme`, mas quem aplica a cor do texto (`LocalContentColor`) é a `Surface`. No app, o `Scaffold` faz isso.

> 📝 Caderno
> Preview = foto de UMA função, sem rodar o app
> Função sem parâmetros que chama o composable real com dado fixo
> @Preview(uiMode, widthDp, fontScale...) = aparelho de mentira · pode empilhar várias
> Tema muda cores, Surface/Scaffold aplica a cor do texto

**`uiMode = Configuration.UI_MODE_NIGHT_YES` (2026-10-05)**

- `Configuration` (`android.content.res.Configuration`) é o objeto em que o Android guarda o **estado atual do aparelho**: idioma, orientação, tamanho de tela, densidade e **modo noturno** (campo `uiMode`). ≈ `Appearance.getColorScheme()` / `useColorScheme()` do RN, só que junto com todo o resto.
- `UI_MODE_NIGHT_YES` / `UI_MODE_NIGHT_NO` / `UI_MODE_NIGHT_UNDEFINED` são **constantes `Int`** (flags). Na `@Preview`, `uiMode = ...` diz: "finja que o aparelho está no escuro".
- Caminho até a cor: `uiMode` → `isSystemInDarkTheme()` lê esse campo → `darkTheme = true` no `TemperatureConverterTheme` → `when` escolhe `dynamicDarkColorScheme` (Android 12+) ou `DarkColorScheme` → `MaterialTheme` → `Surface`/`Scaffold` aplicam.
- **A anotação não chega no celular.** No app de verdade, quem manda é o tema escuro do sistema (Configurações → Tela → Tema escuro). Trocar o tema com o app aberto muda a `Configuration` → a Activity é **recriada** (igual girar a tela: `remember` perde, `rememberSaveable` volta).
- Trocar pelo terminal, com o celular pareado: `adb shell cmd uimode night yes` / `night no`.
- Padrão de mercado: deixar `darkTheme = isSystemInDarkTheme()` (respeitar o sistema) e testar os dois modos com **duas `@Preview` empilhadas** (`name = "Claro"` / `name = "Escuro"`). Forçar um tema fixo só se o produto pedir (ex.: player de vídeo sempre escuro).

> 📝 Caderno
> Configuration = estado do aparelho (idioma, orientação, uiMode...)
> UI_MODE_NIGHT_YES = constante Int "modo escuro ligado"
> uiMode → isSystemInDarkTheme() → darkTheme → colorScheme
> @Preview(uiMode) só no Studio · celular segue o sistema
> Trocou tema no sistema = Activity recriada (igual girar)
> adb shell cmd uimode night yes|no

**Como ver as mudanças no aparelho ("reload", 2026-10-02)**

Android não tem um Fast Refresh automático que serve pra tudo, como o RN. São 3 níveis, do mais leve ao mais pesado:

| Ferramenta | O que faz | Quando serve |
|---|---|---|
| **Live Edit** | atualiza o composable no aparelho enquanto você digita/salva (≈ Fast Refresh) | mudar o **corpo** de um composable (texto, cor, Modifier, layout). Aparelho com Android 11+ |
| **Apply Changes** (ícones ao lado do ▶) | troca o código sem reinstalar; pode reiniciar a Activity | mudanças em código Kotlin comum, sem mexer em assinatura/estrutura |
| **Run ▶ de novo** | recompila e reinstala tudo | sempre funciona: Manifest, Gradle, classe nova, assinatura de função mudou |

Ligar o Live Edit: Settings → Editor → Live Edit (modo automático ou "ao salvar", `Cmd+S`). Funciona com o celular pareado por Wi-Fi. Se a mudança não aparecer, a regra é: **Run ▶ de novo**.

> 📝 Caderno
> Live Edit ≈ Fast Refresh (só corpo de composable, Android 11+)
> Apply Changes = troca código sem reinstalar
> Não apareceu / mexeu em Manifest, Gradle, assinatura → Run ▶ de novo
> Preview atualiza sozinha (às vezes pede Build & Refresh)

**6. Gradle, Fase 0 (só leitura)**

`app/build.gradle.kts`:

| Trecho | Significa |
|---|---|
| `namespace` / `applicationId` | pacote do código / ID único do app na Play Store |
| `minSdk = 24` | roda do Android 7.0 pra cima |
| `targetSdk` / `compileSdk` | versão com que o app foi testado / compilado |
| `buildFeatures { compose = true }` | liga o compilador do Compose |
| `implementation(platform(libs.androidx.compose.bom))` | **BOM** = "pacote de versões" do Compose: as libs do Compose ficam **sem versão** no catalog e o BOM garante que todas são compatíveis |
| `implementation` / `testImplementation` / `androidTestImplementation` / `debugImplementation` | dep do app / de teste unitário (JVM) / de teste no aparelho / só no build debug |

`libs.androidx.compose.material3` no Gradle = a linha `androidx-compose-material3` do `libs.versions.toml` (hífen vira ponto). Adicionar lib = 1 linha no `[libraries]` do toml + 1 linha em `dependencies { }` + Sync.

> 📝 Caderno
> MainActivity.onCreate → setContent { } → Tema { Scaffold { conteúdo } }
> setContent = porta do mundo Compose (≈ root.render(<App/>))
> onCreate roda de novo ao girar → remember perde, rememberSaveable volta do Bundle
> enableEdgeToEdge → Scaffold dá innerPadding → aplicar no conteúdo (≈ SafeArea)
> Tema = ThemeProvider · MaterialTheme.colorScheme / .typography = useTheme()
> content: @Composable () -> Unit = slot = children
> Assinatura padrão: fun X(dados, onEvento, modifier: Modifier = Modifier)
> @Preview = Storybook, sem parâmetros, dentro do tema
> Gradle: settings (módulos) · toml (versões) · app/build (deps) · Sync = npm install
> BOM = versões do Compose casadas; libs do Compose sem versão


#### Etapa 2, aprofundamento: o template peça por peça (2026-10-02)

**1. Estrutura de pastas completa**

```
TemperatureConverter/
├─ settings.gradle.kts      ← lista de módulos (include(":app")) + repositórios (google(), mavenCentral())
├─ build.gradle.kts         ← raiz: plugins com "apply false" (só declara versão)
├─ gradle.properties        ← flags do Gradle (memória da JVM, configuration cache)
├─ local.properties         ← caminho do SDK NA SUA MÁQUINA. Fora do git
├─ gradlew / gradlew.bat    ← wrapper: baixa e roda a versão certa do Gradle
├─ gradle/
│  ├─ libs.versions.toml    ← version catalog (todas as versões)
│  └─ wrapper/gradle-wrapper.properties ← qual versão do Gradle usar
└─ app/                     ← módulo do app (projeto grande tem vários: :core, :feature-x)
   ├─ build.gradle.kts      ← SDKs, compose = true, dependencies { }
   └─ src/
      ├─ main/              ← o que vai pro APK
      │  ├─ AndroidManifest.xml
      │  ├─ java/com/adriel/temperatureconverter/   ← "java" por tradição; aceita .kt
      │  │  ├─ MainActivity.kt
      │  │  └─ ui/theme/  Color.kt · Type.kt · Theme.kt
      │  ├─ res/            ← recursos (não é código)
      │  │  ├─ values/      strings.xml · colors.xml · themes.xml
      │  │  ├─ mipmap-*/    ícone do app em cada densidade
      │  │  ├─ drawable/    imagens e vetores
      │  │  └─ xml/         regras de backup
      │  └─ keepRules/rules.keep ← regras do R8 (encolhe/ofusca o release; antigo proguard-rules.pro)
      ├─ test/              ← teste unitário: roda na JVM do Mac (rápido)
      └─ androidTest/       ← teste instrumentado: roda no aparelho/emulador
```

| Android | React Native / web |
|---|---|
| `src/main/java/...` | `src/` |
| `res/` | `assets/` + arquivos de tradução |
| `mipmap-mdpi/hdpi/xhdpi/xxhdpi/xxxhdpi` | imagem `@1x`, `@1.5x`, `@2x`, `@3x`, `@4x` |
| `test/` vs `androidTest/` | Jest vs Detox |
| `local.properties` | `.env` local (não vai pro git) |
| `gradlew` | `npx` com versão travada |

**`res/` e a classe `R`:** o Gradle gera a classe `R` com um ID pra cada recurso. No XML: `@string/app_name`. No Kotlin: `R.string.app_name`. No Compose: `stringResource(R.string.app_name)`. Padrão de mercado: texto que o usuário vê vai pro `strings.xml`, porque é assim que o app ganha tradução (`values-pt/strings.xml`, `values-en/...`).

**Dois temas convivem:** o `res/values/themes.xml` é o tema da **janela** (XML), que o sistema mostra no instante em que o app abre, antes do Compose desenhar. Depois disso quem manda é o `Theme.kt` (Compose).

**2. `AndroidManifest.xml`: o RG do app**

```xml
<application
    android:icon="@mipmap/ic_launcher"          ← ícone
    android:label="@string/app_name"           ← nome embaixo do ícone
    android:theme="@style/Theme.TemperatureConverter"  ← tema da janela (themes.xml)
    android:allowBackup="true" ...>            ← backup automático do Google
    <activity
        android:name=".MainActivity"           ← "." = pacote do namespace
        android:exported="true"                ← outros apps (o launcher) podem abrir
        android:windowSoftInputMode="adjustResize">  ← teclado encolhe a tela (importa pro TextField)
        <intent-filter>
            <action android:name="android.intent.action.MAIN" />
            <category android:name="android.intent.category.LAUNCHER" />
        </intent-filter>                       ← MAIN + LAUNCHER = "abre esta ao tocar no ícone"
    </activity>
</application>
```

Toda Activity precisa estar declarada aqui; se não estiver, dá crash ao abrir. Permissões (`INTERNET`, localização) também entram aqui.

```
toque no ícone → sistema lê o Manifest → acha a Activity com MAIN/LAUNCHER
   → cria MainActivity → onCreate() → setContent { } → Compose desenha
```

**3. `MainActivity.kt` linha a linha**

| Linha | O que é | Ponte TS |
|---|---|---|
| `package com.adriel.temperatureconverter` | endereço do arquivo; convenção: bate com a pasta | caminho do módulo |
| `import androidx.compose...` | cada função/composable vem de um pacote (Alt+Enter importa) | `import { X } from '...'` |
| `class MainActivity : ComponentActivity()` | `:` = herda; `()` chama o construtor do pai | `class MainActivity extends ComponentActivity` |
| `override fun onCreate(savedInstanceState: Bundle?)` | `override` é **obrigatório** em Kotlin; `Bundle?` pode ser `null` | método sobrescrito |
| `super.onCreate(savedInstanceState)` | executa a versão do pai; sem isso, crash | `super.method()` |
| `enableEdgeToEdge()` | conteúdo ocupa a tela toda, atrás das barras | `StatusBar translucent` |
| `setContent { ... }` | função que recebe uma lambda `@Composable` (trailing lambda) | `root.render(<App/>)` |

- **`ComponentActivity`**: a classe base de Activity com suporte a ciclo de vida, `setContent` e `rememberSaveable`. Você não cria a Activity com `MainActivity()`: quem cria é o **sistema**, e ele chama `onCreate`, `onStart`, `onResume`... ≈ `componentDidMount`, só que chamado pelo Android.
- **`Bundle?`**: `null` na primeira abertura; preenchido quando a Activity é recriada (girar a tela, sistema matou o app em segundo plano).
- **`TemperatureConverterTheme(darkTheme = isSystemInDarkTheme(), dynamicColor = true, content)`**: o `when` escolhe o `ColorScheme`. Android 12+ (`Build.VERSION.SDK_INT >= S`) com `dynamicColor` → cores do papel de parede; senão, `DarkColorScheme`/`LightColorScheme` (cores do `Color.kt`). No fim chama `MaterialTheme(colorScheme, typography, content)`.
- **`Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding -> ... }`**: argumento nomeado + trailing lambda (é o parâmetro `content` do Scaffold).
- **`"Hello $name!"`**: string template ≈ `` `Hello ${name}!` ``.

Árvore que o template monta:

```
MainActivity                (Android: Activity)
└─ setContent               (porta do Compose)
   └─ TemperatureConverterTheme   (escolhe cores)
      └─ MaterialTheme            (fornece cores/fontes pra árvore)
         └─ Scaffold              (esqueleto; calcula innerPadding)
            └─ Greeting           (seu composable)
               └─ Text            (Material 3)
```

**4. `Modifier` a fundo**

- **O que é:** uma lista **ordenada** de instruções que você entrega a um composable, dizendo como ele deve ser medido, desenhado e como reage a toques.
- **Três famílias:**

| Família | Exemplos |
|---|---|
| Tamanho/layout | `fillMaxSize()`, `fillMaxWidth()`, `size(48.dp)`, `padding(16.dp)`, `weight(1f)` |
| Desenho | `background(cor)`, `border(...)`, `clip(RoundedCornerShape(8.dp))`, `alpha(0.5f)` |
| Comportamento | `clickable { }`, `verticalScroll(...)` |

- **Como a cadeia funciona:** `Modifier` sozinho é um objeto vazio. Cada chamada devolve um Modifier **novo** com um elemento a mais (imutável, como um builder ou `[...arr, item]`):

```
Modifier                      → [ ]
  .fillMaxSize()              → [fillMaxSize]
  .padding(16.dp)             → [fillMaxSize, padding 16]
```

- **A ordem importa** (cada elemento embrulha o próximo): `padding` antes de `background` deixa a margem de fora sem cor; depois de `background` a cor ocupa tudo e o recuo fica dentro. Prática disso na Etapa 3.
- **`modifier: Modifier = Modifier`:** o primeiro `Modifier` é o **tipo** (interface); o segundo é o **valor** vazio (o companion object). "Recebo um Modifier; se ninguém mandar, uso o vazio."
- **Regra do parâmetro:** aplicar o `modifier` recebido no elemento **raiz** do componente, e estender a partir dele:

```kotlin
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text("Hello $name!", modifier = Modifier.padding(8.dp))   // ❌ ignora o que o pai mandou
    Text("Hello $name!", modifier = modifier.padding(8.dp))   // ✅ estende o modifier do pai
}
```

- `padding(innerPadding)` recebe `PaddingValues` (4 lados de uma vez); `padding(16.dp)` recebe um `Dp`. `dp` vem de `import androidx.compose.ui.unit.dp`.

> 📝 Caderno
> app/src/main = APK · test = JVM (rápido) · androidTest = aparelho
> res/ → classe R → stringResource(R.string.x) · texto visível no strings.xml
> mipmap-*dpi = ícone @1x..@4x · local.properties fora do git
> Manifest: MAIN + LAUNCHER = tela que abre no ícone · toda Activity declarada
> themes.xml = janela antes do Compose · Theme.kt = dentro do Compose
> class A : B() = extends · override obrigatório · Bundle? null na 1ª vez
> Quem cria a Activity e chama onCreate é o SISTEMA
> Modifier = lista ordenada e imutável: Modifier → .a() → .b()
> modifier: Modifier = Modifier → tipo = valor vazio
> Dentro do componente: modifier (minúsculo) no raiz, nunca Modifier novo

#### Checagem da Etapa 2: perguntas para responder sem consultar (2026-10-05)

1. Quem cria a `MainActivity` e chama o `onCreate`? O que o `setContent { }` faz (ponte com o React)?
2. Para que serve o `innerPadding` que o `Scaffold` entrega? O que acontece na tela se você ignorá-lo, com o `enableEdgeToEdge()` ligado?
3. Em `modifier: Modifier = Modifier`, o que é cada um dos dois `Modifier`? Dentro do componente, por que usar o `modifier` (minúsculo) no elemento raiz em vez de começar um `Modifier` novo?
4. `themes.xml` e `Theme.kt`: o que cada um controla, e por que existem os dois?
5. No `AndroidManifest.xml`, o que faz uma Activity ser a que abre ao tocar no ícone do app?
6. `minSdk`, `compileSdk` e `targetSdk`: o que cada um decide, e qual você sobe primeiro sem medo? Por quê?
7. Para adicionar uma biblioteca nova, em quais dois arquivos você mexe? Por que as libs do Compose aparecem sem versão no `build.gradle.kts`?
8. Por que a função de `@Preview` não recebe parâmetros? E por que um `Text` fora de `Surface` não muda de cor numa preview com `uiMode = UI_MODE_NIGHT_YES`?

#### Etapa 3, parte 1: ordem do Modifier e layouts (2026-10-05)

> A checagem da Etapa 2 foi pulada a pedido (ficou nas PENDÊNCIAS). Etapa 3 em 3 exercícios: **3.1** Modifier + layouts (playground com previews) → **3.2** `TextField` numérico + `toDoubleOrNull` → **3.3** o conversor (enunciado final).

**1. A ordem do Modifier: de fora para dentro**

A cadeia é lida da esquerda para a direita, e **cada item embrulha o que vem depois**. O primeiro é a casca mais externa.

```kotlin
Text("A", Modifier.background(Color.Yellow).padding(16.dp))
// background primeiro → a cor pinta TUDO; o padding vem depois → recuo DENTRO da cor
// ≈ CSS: padding: 16px

Text("B", Modifier.padding(16.dp).background(Color.Yellow))
// padding primeiro → 16dp de espaço SEM cor em volta; a cor só pinta dali pra dentro
// ≈ CSS: margin: 16px
```

- **Compose não tem `margin`.** Margem = `padding` **antes** do `background`; padding interno = `padding` **depois**. Dá pra usar os dois na mesma cadeia: `Modifier.padding(8.dp).background(cor).padding(16.dp)`.
- Mesmo raciocínio com tamanho: `Modifier.padding(8.dp).size(100.dp)` ocupa 116dp; `Modifier.size(100.dp).padding(8.dp)` ocupa 100dp e o conteúdo fica com 84dp.
- E com clique: `clickable` antes do `padding` = a área de toque inclui o recuo; depois = só o miolo.

**2. `Column`, `Row`, `Box` e `Spacer`** (≈ flexbox)

| Composable | Faz | Ponte CSS/RN |
|---|---|---|
| `Column` | empilha na vertical | `flex-direction: column` |
| `Row` | lado a lado | `flex-direction: row` |
| `Box` | empilha **um sobre o outro** | `position: absolute` dentro de um `relative` |
| `Spacer(Modifier.height(8.dp))` | espaço vazio | `<View style={{height: 8}} />` |

Dois parâmetros controlam a posição dos filhos:

| | Eixo principal (≈ `justifyContent`) | Eixo cruzado (≈ `alignItems`) |
|---|---|---|
| `Column` | `verticalArrangement = Arrangement.spacedBy(8.dp)` / `.Center` / `.SpaceBetween` | `horizontalAlignment = Alignment.CenterHorizontally` |
| `Row` | `horizontalArrangement = Arrangement.spacedBy(8.dp)` / ... | `verticalAlignment = Alignment.CenterVertically` |
| `Box` | — | `contentAlignment = Alignment.Center` |

- `Arrangement.spacedBy(8.dp)` ≈ `gap: 8px`. Padrão de mercado no lugar de um `Spacer` entre cada filho.
- **Gap + centralizar juntos (2026-10-06):** `spacedBy` aceita um alinhamento como 2º argumento. Na `Column`: `verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)` (16dp entre cada filho **e** o grupo no meio). Na `Row`: `spacedBy(8.dp, Alignment.CenterHorizontally)`. Substitui o `Arrangement.Center` sem precisar de `Spacer`.
- **`Spacer` só pra espaço desigual:** quando um ponto específico precisa de um espaço diferente dos outros (ex.: 8dp entre título e subtítulo, 32dp antes do botão).
- **Layout pai é obrigatório (2026-10-06):** dois composables soltos dentro do tema (ou de qualquer coisa que não seja layout) são desenhados no mesmo ponto, sobrepostos. O tema só fornece cores/tipografia, não posiciona. Solução: envolver numa `Column`/`Row`. `Spacer` não resolve: é uma folha, também precisa de pai.
- **Modifier vs parâmetros:** `modifier` = como **eu** sou (tamanho → espaço → aparência; se nada, não passa). `Arrangement`/`Alignment` = como organizo **meus filhos**. Centralizar na vertical exige altura sobrando (`fillMaxSize()`); no preview, `showSystemUi = true` simula a tela do celular.
- Pegadinha do nome: na `Column` o eixo principal é vertical, então é `verticalArrangement` + `horizontalAlignment` (cruzado). Na `Row`, o inverso.
- Imports: `androidx.compose.foundation.layout.*` (Column, Row, Box, Spacer, Arrangement, padding, size), `androidx.compose.foundation.background`, `androidx.compose.ui.Alignment`, `androidx.compose.ui.graphics.Color`, `androidx.compose.ui.unit.dp`. `Alt+Enter` resolve.
- Cores fixas (`Color.Yellow`) só no playground. Em tela de verdade: `MaterialTheme.colorScheme.primaryContainer` etc., pra respeitar o tema claro/escuro.

> 📝 Caderno
> Modifier: de fora pra dentro · o 1º é a casca externa
> padding ANTES do background = margem · DEPOIS = padding interno · Compose não tem margin
> Column = vertical · Row = horizontal · Box = um sobre o outro
> Arrangement = eixo principal (justifyContent) · Alignment = eixo cruzado (alignItems)
> spacedBy(8.dp) ≈ gap · spacedBy(8.dp, Alignment.CenterVertically) = gap + centro
> Spacer = só pra espaço desigual · sem layout pai = tudo sobreposto
> modifier = como EU sou · Arrangement/Alignment = como organizo MEUS FILHOS

**Exercício 3.1: `LayoutPlayground.kt`** (arquivo novo em `ui/`, só previews; não mexer na `MainActivity`)

1. **Previsão antes de rodar.** Duas previews com os `Text` A e B de cima. Antes de olhar o resultado, escrever num comentário qual vai ter a cor colada no texto e qual vai ter margem. Depois conferir.
2. **Margem + padding na mesma cadeia.** Um `Text` com borda preta de 2dp, 8dp de margem **dentro da borda e fora da cor**, fundo amarelo e 16dp de padding interno. Uma cadeia só. (`border(2.dp, Color.Black)` vem de `androidx.compose.foundation.border`.)
3. **Layout: card de playlist.** Um composable `CardPlaylist(modifier: Modifier = Modifier)` com:
   - uma `Column` com padding 16dp e itens espaçados 8dp;
   - título "Rock Nacional" e, embaixo, "42 músicas";
   - uma `Row` com 3 "chips" (`Box` 72×32dp, fundo colorido, texto centralizado): "Anos 80", "Anos 90", "Ao vivo", com 8dp entre eles.
   - Assinatura padrão: o `modifier` recebido vai no elemento raiz.
   - Preview com `TemperatureConverterTheme` em volta.

**Erros comuns vistos no 3.1 (2026-10-05)**

- `Column(modifier) { modifier.padding(16.dp) ... }`: **não faz nada**. As chaves recebem só os filhos. O Modifier é imutável e `.padding()` devolve um novo, que ali é descartado. Padding vai **no parâmetro**: `Column(modifier = modifier.padding(16.dp))`.
- Dois elementos soltos na raiz (`Column` e depois `Row`) em vez de um card: a `Row` tem que ser **filha** da `Column`.
- O `modifier` recebido vai **só na raiz**. Os filhos começam com `Modifier` (maiúsculo).
- Itens repetidos (3 chips iguais) → extrair um composable (`private fun Chip(texto: String, modifier: Modifier = Modifier)`), como extrair um componente no React.

**3.1b: componente reutilizável (2026-10-06/07)**

- **Cada layout posiciona só os filhos diretos.** O espaço *entre* irmãos é decisão do pai (`spacedBy`). O componente cuida só do espaço *interno* (`padding(16.dp)`); margem externa embutida no componente (`padding(top = 40.dp)`) briga com quem o reutiliza.
- **`Modifier` × `modifier`:** `Modifier` = cadeia vazia (começa do zero; usado no topo/preview e nos filhos internos). `modifier` = cadeia de quem chamou (a raiz do componente continua ela: `modifier.padding(16.dp)`). Usar `Modifier` na raiz de quem recebe `modifier` = bug silencioso: o que o pai passa é ignorado.
- **O pai estiliza o lado de fora (modifier); o conteúdo muda por parâmetros de dados** (`titulo`, `quantidade`, `chips`) ≈ props. Convenção: obrigatórios primeiro, `modifier: Modifier = Modifier` como **1º opcional**.
- **Laço dentro de composable:** composable é função normal; `if`/`for`/`when` valem. Cada composable chamado no `for` vira um elemento (≈ `lista.map(item => <Chip/>)`). O `for` envolve **o que se repete** (o chip inteiro), não só o `Text`: `for` dentro do `Box` = vários textos sobrepostos num chip só. Pra lista longa/rolável: `LazyColumn` (pendente).
- **`private` em composable:** peça interna (o `Chip` do card) começa `private`; só vira público quando surgir um 2º uso real. Kotlin é `public` por padrão (≈ não dar `export` no React).
- **Plural:** `if` é expressão em Kotlin (≈ ternário). Em app real o texto vai pro `strings.xml` e o plural usa `pluralStringResource` (pendente).

> 📝 Caderno
> cada layout posiciona só os filhos diretos · espaço ENTRE irmãos = pai
> Modifier = começa do zero · modifier = continua a cadeia de quem chamou · raiz usa modifier
> modifier = pai estiliza o LADO DE FORA · parâmetros = pai muda o CONTEÚDO · modifier = 1º opcional
> for no composable ≈ .map no JSX · o for envolve o que se repete
> peça interna = private · promove a público no 2º uso

#### Etapa 3.2: `TextField` numérico + `toDoubleOrNull` (teoria 2026-10-07)

**1. `TextField` é um componente controlado.** Igual ao `<input value={x} onChange={...}>` do React: o campo **não guarda o texto**. Ele mostra o `value` que recebe e avisa pelo `onValueChange` o que o usuário quer digitar. Se ninguém atualizar o estado, você digita e nada aparece.

```kotlin
var texto by rememberSaveable { mutableStateOf("") }   // texto digitado → rememberSaveable

OutlinedTextField(
    value = texto,
    onValueChange = { novo -> texto = novo },           // evento sobe, estado desce (UDF)
    label = { Text("Valor da conta") },
    singleLine = true,
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
)
```

| Parâmetro | O que faz | Ponte |
|---|---|---|
| `value` | o texto mostrado (sempre `String`) | `value` do input |
| `onValueChange` | recebe o texto novo a cada tecla | `onChange` / `onChangeText` do RN |
| `label` | **slot**: recebe um composable (`{ Text(...) }`), não uma `String` | render prop / `children` |
| `singleLine` | uma linha só, Enter não quebra | `multiline={false}` |
| `keyboardOptions` | qual teclado abre | `keyboardType="decimal-pad"` do RN |
| `isError` | pinta o campo de vermelho | — |
| `supportingText` | slot pra mensagem embaixo do campo (erro/ajuda) | — |

`TextField` (preenchido) e `OutlinedTextField` (com contorno) têm a mesma API; muda só o visual do Material 3.

**2. O teclado numérico não valida nada.** `KeyboardType.Decimal` só escolhe qual teclado aparece. O valor continua `String` e pode vir qualquer coisa: colar "abc", teclado físico, `-`, `.` sozinho, campo vazio. Em pt-BR a tecla decimal costuma ser **vírgula**.

**3. `String` → número: `toDoubleOrNull()`.**

| Entrada | `toDouble()` | `toDoubleOrNull()` |
|---|---|---|
| `"36.5"` | `36.5` | `36.5` |
| `"36,5"` | 💥 `NumberFormatException` | `null` (Kotlin só entende ponto) |
| `""` / `"-"` / `"abc"` | 💥 | `null` |

`toDouble()` exigiria `try/catch` (tópico 1). Padrão de mercado: `toDoubleOrNull()` + tratar o `null`. Para aceitar vírgula: `texto.replace(',', '.').toDoubleOrNull()`.

**4. Regra de ouro: o estado guarda o TEXTO, não o número.** Se o estado fosse `Double`, como representar "3." no meio da digitação, ou o campo vazio? O campo "brigaria" com o usuário (apaga o ponto, põe 0). Então: estado = `String`; o número é **derivado** a cada recomposição, numa `val` comum:

```kotlin
val valor: Double? = texto.replace(',', '.').toDoubleOrNull()   // recalcula a cada recomposição
val erro = texto.isNotEmpty() && valor == null                  // vazio não é erro, é "ainda não digitou"
```

Ponte React: é o mesmo que calcular no corpo do componente em vez de criar outro `useState`. (O `derivedStateOf` ≈ `useMemo` existe, mas está nas PENDÊNCIAS e não é necessário pra uma conta barata dessas.)

**5. Ver o campo funcionando no preview:** o preview comum é estático (não dá pra digitar). Use o **modo interativo** (ícone de "dedo"/Start Interactive Mode no painel do preview) ou o ▶️ do `@Preview` para rodar no emulador.

Imports: `androidx.compose.material3.OutlinedTextField`, `androidx.compose.foundation.text.KeyboardOptions`, `androidx.compose.ui.text.input.KeyboardType`, `androidx.compose.runtime.saveable.rememberSaveable`, `androidx.compose.runtime.getValue`/`setValue` (pro `by`). `⌥↩` resolve.

> 📝 Caderno
> TextField = controlado (value + onValueChange) ≈ input do React
> label/supportingText = slot → { Text(...) }, não String
> KeyboardType.Decimal só troca o teclado, NÃO valida
> toDouble() explode · toDoubleOrNull() devolve null · vírgula → replace(',', '.')
> estado = TEXTO (String) · número = val derivada · vazio ≠ erro

**Exercício 3.2: `CampoDecimal.kt`** (arquivo novo em `ui/`; cenário: gorjeta de restaurante, não é o conversor ainda)

1. `CampoDecimal` **stateless**: recebe `valor: String`, `onValorChange: (String) -> Unit`, `rotulo: String` e `modifier` (na ordem certa). Dentro, um `OutlinedTextField` com teclado decimal, uma linha, o `rotulo` no slot `label`.
2. Erro visual: se o texto não estiver vazio e não virar número, `isError = true` e uma mensagem "Valor inválido" no `supportingText`. Vírgula tem que ser aceita.
3. `TelaGorjeta` **stateful** (dona do estado, `rememberSaveable`): usa o `CampoDecimal` e, embaixo, um `Text` com "Gorjeta (10%): X" quando o valor for válido, ou "Digite o valor da conta" quando não for.
4. Preview da `TelaGorjeta` e teste no modo interativo: `""`, `"50"`, `"50,5"`, `"abc"`, `"-"`.
5. **Pergunta pra responder num comentário:** por que o estado é `String` e não `Double`?
- O conteúdo do `Box` vai na lambda final (≈ `children`). O centro vem de `contentAlignment = Alignment.Center`, nos parênteses.

- `@Preview(showBackground = true)` pinta um fundo branco **só na preview** (sem ele o fundo é transparente e mostra a cor do Studio). Cor própria: `backgroundColor = 0xFFEEEEEE` (ARGB). Diferente de `Modifier.background(cor)`, que pinta o componente no app de verdade.
- Gap na `Row` = `horizontalArrangement = Arrangement.spacedBy(8.dp)`. Um `Row(Modifier.padding(8.dp))` só cria espaço **em volta** da Row, e os filhos continuam colados.
- Argumentos posicionais funcionam (`Box(mod, Alignment.Center)`), mas o padrão é **nomeado** (`contentAlignment = Alignment.Center`): fica legível e não quebra se a ordem mudar.

> 📝 Caderno
> { } = só filhos · Modifier = parâmetro (solto nas chaves é descartado)
> modifier recebido → só na raiz · filhos → Modifier novo
> repetiu 3x → extrai composable

#### Git em projeto Android: o que versionar e o que ignorar (2026-10-05)

**Regra-mãe:** versione o que outra pessoa (ou o CI) precisa para **reconstruir o projeto do zero** e que é **igual para todo mundo**. Ignore três tipos de coisa: o que é **gerado** (dá para recriar com um build), o que é **pessoal da sua máquina** (caminhos, janelas abertas, aparelho escolhido) e o que é **segredo** (senha, keystore, chave de API).

**Ponte com JS/React Native:**

| JS / RN | Android | Git |
|---|---|---|
| `dist/`, `.expo/` | `build/` (em cada módulo) | ignora |
| `node_modules/` (cache baixado) | `.gradle/`, `.kotlin/` | ignora |
| `.env.local` | `local.properties` | ignora |
| `package.json` | `build.gradle.kts` + `libs.versions.toml` | versiona |
| versão do yarn/pnpm travada no repo | `gradlew` + `gradle/wrapper/*` | versiona |
| `.vscode/settings.json` pessoal | `.idea/workspace.xml` | ignora |

**Versionar ✅**

| Arquivo/pasta | Por quê |
|---|---|
| `app/src/**` | o código e os recursos do app |
| `settings.gradle.kts`, `build.gradle.kts` (raiz e `app/`) | definem o build |
| `gradle/libs.versions.toml` | versões das dependências |
| `gradle.properties` | flags do build iguais para todos (sem segredo) |
| `gradlew`, `gradlew.bat`, `gradle/wrapper/gradle-wrapper.jar` e `.properties` | o wrapper. **Sim, o `.jar` vai pro git:** sem ele o `./gradlew` não roda no CI nem no clone de outra pessoa |
| `app/proguard-rules.pro` / regras do R8 | configuram o build de release |
| os `.gitignore` (todos) | as regras valem para todo mundo |

**Ignorar ❌**

| Arquivo/pasta | Por quê |
|---|---|
| `build/`, `.gradle/`, `.kotlin/`, `captures/`, `.cxx/`, `.externalNativeBuild/` | gerados pelo build, recriados a qualquer hora |
| `local.properties` | `sdk.dir` aponta para o SDK **da sua máquina**. Também é o lugar padrão para guardar chave/senha local |
| `*.iml` | arquivo de módulo que o Studio regera a partir do Gradle |
| `.idea/workspace.xml`, `caches/`, `libraries/`, `modules.xml`, `deploymentTargetSelector.xml`, `deviceManager.xml`, `shelf/` | estado pessoal do IDE (abas abertas, celular selecionado) |
| `*.jks`, `*.keystore`, senhas de assinatura | segredo. Vai para cofre/secrets do CI, nunca para o git |
| `.DS_Store` | lixo do macOS |

**A pasta `.idea/`: duas escolhas aceitas no mercado**
- **(A) Padrão do template do Google:** ignora só o pessoal (lista acima) e versiona o compartilhável: `codeStyles/`, `inspectionProfiles/`, `runConfigurations.xml`. Faz sentido em time, para todo mundo formatar igual.
- **(B) Ignorar a pasta inteira (`/.idea`):** mais simples, comum em projeto solo ou quando o estilo é garantido por ktlint/detekt no CI. O Studio recria a pasta sozinho ao abrir o projeto.
- O que **não** pode é ficar no meio do caminho: a regra dizer "ignora" e os arquivos continuarem rastreados (pegadinha 1).

**Pegadinhas:**
1. **`.gitignore` não desversiona.** Ele só vale para arquivo **ainda não rastreado**. Se o arquivo já foi commitado, é preciso tirar do índice sem apagar do disco: `git rm -r --cached <caminho>` e depois commit.
2. **O caminho é relativo à pasta do `.gitignore`.** `/.idea` dentro de `TemperatureConverter/.gitignore` pega só `TemperatureConverter/.idea`, e não a `.idea` da raiz do repo. Sem a `/` inicial (`.idea/`), a regra pega a pasta em qualquer nível abaixo. Barra no fim (`build/`) = só pasta.
3. **Os `.gitignore` se somam.** Raiz, projeto, `app/` e o `.idea/.gitignore` que o Studio cria valem juntos, cada um para a sua pasta.
4. **Segredo commitado continua no histórico.** Ignorar depois não resolve: a chave tem que ser trocada.
5. **`!padrao` reinclui**, mas não funciona se a pasta-mãe inteira foi ignorada (o git nem entra nela).

**Como ficou neste repo (2026-10-05):** uma regra só, `.idea/`, no `.gitignore` da **raiz**. Sem `/` no começo, ela pega a `.idea` da raiz e a de todos os projetos (inclusive os próximos). Os arquivos da `TemperatureConverter/.idea` que já estavam commitados saíram com `git rm -r --cached`. O `CLAUDE.md` também foi para o `.gitignore` (instruções só locais).

**Desfazer coisas no stage:**
- `git restore --staged <arquivo>`: desfaz o `git add`. A alteração continua no disco. **Seguro.**
- `git restore <arquivo>` (sem `--staged`): **descarta a alteração do disco** e volta para a versão do último commit. Não tem como recuperar.
- No `git status --short`, a 1ª coluna é o stage (vai no commit) e a 2ª é o que está só no disco: `M ` = pronto para commitar, ` M` = mudou mas não foi adicionado.

**Ferramentas de conferência:**
- `git status --ignored`: mostra também o que está sendo ignorado.
- `git check-ignore -v <caminho>`: diz **qual arquivo e qual linha** ignorou aquele caminho.
- `git ls-files <pasta>`: lista o que está rastreado (para achar arquivo que devia estar ignorado e já foi commitado).

> 📝 Caderno
> Versiona: reconstruir do zero + igual pra todos
> Ignora: gerado (build/, .gradle/) · pessoal (local.properties, workspace.xml) · segredo (*.jks)
> gradle-wrapper.jar VAI pro git · local.properties NÃO
> .idea: ignora só o pessoal (template) OU a pasta toda. Escolher um
> .gitignore não desversiona → git rm -r --cached <caminho>
> /x = relativo à pasta do .gitignore · x/ = qualquer nível, só pasta
> git check-ignore -v <caminho> = quem ignorou
> git restore --staged x = desfaz o add (seguro) · git restore x = DESCARTA a alteração
> status --short: 1ª coluna = stage (vai no commit) · 2ª = só no disco

#### Onde rodar o `./gradlew` neste repo (2026-10-05)

O repo é um **monorepo de projetos independentes**: a raiz não tem `settings.gradle.kts` nem `gradlew`. Cada projeto (`TemperatureConverter/`, depois `GuessingGame/`) tem o seu wrapper e o seu `settings.gradle.kts`. O Gradle procura o `settings.gradle.kts` a partir da pasta onde roda, então:

- **Terminal:** `cd TemperatureConverter && ./gradlew assembleDebug`. Da raiz também dá, sem entrar na pasta: `TemperatureConverter/gradlew -p TemperatureConverter assembleDebug` (`-p` = pasta do projeto).
- **Android Studio:** abrir a pasta `TemperatureConverter/`, não a raiz. Aberta na raiz, o Studio não acha um projeto Gradle: sem Sync não tem Run, autocomplete nem cor de sintaxe no Kotlin (o código vira texto). Sintoma visto em 2026-10-05: uma `.idea/` nova apareceu na raiz, criada ao abrir a raiz. Correção: File → Close Project → Open → escolher a pasta `TemperatureConverter/` → Trust → esperar o Sync terminar.
- **Painel de arquivos só mostra `app` e `Gradle Scripts`?** É a visão **Android** (lógica e filtrada, esconde `build/`, `gradlew`, `.gitignore`...). Seletor no topo do painel → **Project** mostra as pastas reais do disco. Só muda o desenho da árvore: Sync/Gradle continuam iguais (2026-10-05).
- **Pasta "sumida" (`ui.theme` numa linha só):** é o **Compact Middle Packages**, que junta pastas que têm um filho só. A opção fica em `⋮` do painel → Appearance e vale **por visão** (Android e Project têm cada uma a sua). Para mover um arquivo de pacote sem ver a pasta: editar a linha `package` → `Alt+Enter` → "Move file to ..." (o Studio move para a pasta que bate com o package) (2026-10-05).
- **Sessão do Claude:** continua na raiz (é onde ficam `docs/` e `CLAUDE.md`). Abrir o Claude e rodar o build são coisas separadas.
- **Git é diferente:** roda de qualquer pasta do repo (sobe até achar o `.git`). Só o caminho passado no comando é relativo a onde você está (`TemperatureConverter/.idea` na raiz = `.idea` dentro do projeto).

> 📝 Caderno
> Raiz do repo = docs + CLAUDE.md (sem Gradle)
> Build: cd <Projeto> && ./gradlew assembleDebug  (ou -p <Projeto>)
> Studio abre a pasta do projeto · Claude abre na raiz
> git roda de qualquer pasta do repo, caminho relativo a onde você está


**Objetivo combinado em 2026-09-21:** ao chegar em Compose e começar a fazer projetos, criar entre 5 e 10 projetos básicos pra treinar Kotlin/Compose na prática, com nível crescente a cada um — treino solto, separado dos projetos reais de Nível 4 (GodiTrack/Orchestror).

**Lista curada (2026-09-21), extraída do repo [`solygambas/kotlin-projects`](https://github.com/solygambas/kotlin-projects)** (25 projetos didáticos de um curso de Android Kotlin; a maioria no original usa View system/XML/LiveData — a ideia é reproduzir cada um adaptado pra **Compose + StateFlow/Coroutines**, que já é o padrão desta trilha, não copiar a stack antiga). Ordem crescente de dificuldade:

| # | Projeto original | O que treina | Por que entra na lista |
|---|---|---|---|
| 1 | Temperature Converter | Compose básico, sem estado complexo | Aquecimento — já é Compose no original |
| 2 | Guessing Game | Compose + ViewModel + estado observável | Trocar `LiveData` (original) por `StateFlow` (o que vocês já dominam) |
| 3 | Todo List | CRUD em memória + lista | `LazyColumn` no lugar do RecyclerView original |
| 4 | Stopwatch | Cronômetro, ciclo de vida | Compose + Coroutines (`LaunchedEffect`/`delay` em loop) — ponto forte de vocês |
| 5 | Tasks | MVVM + Room + lista | Entrada natural assim que Room (item 8) começar |
| 6 | Mars Photos | Consumo de API REST com Retrofit | Primeira rede de verdade, junto com Compose |
| 7 | DevBytes | Room + Retrofit + Coroutines + cache offline | Capstone antes de fechar Clean Architecture (item 7) — repository/single-source-of-truth |
| 8 | Wander | Google Maps + localização | Conecta direto com o domínio do GodiTrack (rotas, rastreamento) |
| 9 | To-Do Notes | Testes automatizados (Room + Coroutines) | Ponte direta pro item 9 (CI/CD) — CI sem teste automatizado não faz muito sentido |

## 7. Clean Architecture — não iniciado

_Teoria será adicionada quando o tópico começar._

## 8. Room Database — não iniciado

_Teoria será adicionada quando o tópico começar._

## 9. Gradle + CI/CD para Android — não iniciado (plano de 2026-09-30)

_Teoria completa será adicionada quando o tópico começar. Abaixo está o plano e o porquê de cada coisa._

**O que é, em uma frase:** o **Gradle** é a ferramenta de build do Android. Ele baixa as dependências, compila Kotlin, empacota o app (APK/AAB), roda testes e lint. **CI** (integração contínua) é um servidor que roda essas mesmas tarefas do Gradle sozinho a cada push/PR. **CD** (entrega contínua) vai um passo além e assina e publica o app.

**Ponte com o mundo JS/React Native:**

| JS / React Native | Android / Gradle |
|---|---|
| `package.json` (dependências) | `build.gradle.kts` do módulo + `gradle/libs.versions.toml` |
| `package.json` (versões centralizadas) / lockfile | Version catalog (`libs.versions.toml`) |
| `npm run build` / `npm test` (scripts) | Tasks: `./gradlew assembleDebug`, `./gradlew test` |
| `npx` com versão fixa do projeto | Gradle **wrapper** (`gradlew`): todo mundo e o CI usam a mesma versão |
| `.env.development` / `.env.production` | `buildTypes` (debug/release) e `productFlavors` (dev/prod) + `BuildConfig` |
| Monorepo com pacotes (`packages/*`) | Multi-módulo (`:app`, `:core:data`, `:feature:busca`) |
| GitHub Actions rodando `npm test` | GitHub Actions rodando `./gradlew test` |
| EAS Build / Submit (Expo) | Assinatura + `bundleRelease` + publicação na Play Console |

**Plano em 4 fases:**

```
Fase 0  LER o Gradle (no caminho) ── desde o projeto 1, Etapa 2
   │    settings.gradle.kts · build.gradle.kts · libs.versions.toml · Sync · add dependência
   ▼
Fase A  Gradle A FUNDO ──────────── depois de Clean Architecture (7) + Room (8)
   │    wrapper · tasks · fases do build · plugins · implementation/api/ksp
   │    buildTypes + R8 · flavors + BuildConfig · signing · multi-módulo · build-logic
   ▼
Fase B  CI ──────────────────────── depois do projeto 9 (To-Do Notes = testes)
   │    GitHub Actions: JDK 17 + cache do Gradle + lint/test/assembleDebug
   │    check obrigatório no PR · APK como artefato · ktlint/detekt
   ▼
Fase C  CD ──────────────────────── por último, aplicado no app de streaming (10)
        keystore em secrets · bundleRelease (AAB) · versionCode automático
        Play Console (trilha interna) ou Firebase App Distribution
```

**Por que nessa ordem:**
- **Leitura já, estudo depois:** todo projeto tem Gradle e toda lib nova (ViewModel, Room, Retrofit) entra por ele. Não dá pra fugir dele, mas no começo basta saber **onde** mexer.
- **A fundo só depois de Room/Clean Architecture:** build types, flavors e multi-módulo resolvem problemas de app grande (ambiente dev/prod, camadas separadas). Estudar antes seria decorar configuração sem o problema que ela resolve.
- **CI depois de testes:** um CI sem teste só confirma que compila. O valor está em barrar PR que quebra teste.
- **CD por último:** assinatura e publicação só importam quando existe um app que vale publicar.

> 📝 Caderno
> Gradle = ferramenta de build (dependências + compilar + empacotar + testar)
> gradlew = wrapper, versão fixa do Gradle (≈ npx com versão travada)
> libs.versions.toml = versões centralizadas (≈ package.json)
> CI = servidor rodando ./gradlew a cada push/PR
> CD = CI + assinar + publicar
> Ordem: ler já → a fundo pós-Room → CI pós-testes → CD por último

## 10. Projeto avançado: app de streaming — não iniciado

_Teoria será adicionada quando o tópico começar._

**Objetivo combinado em 2026-09-21:** depois do CI/CD, projeto de fechamento mais avançado. Referência conceitual: o app open-source **CloudStream** (Kotlin, arquitetura de plugins carregados dinamicamente, Media3/ExoPlayer pra reprodução de vídeo, OkHttp/Jsoup pros provedores). A ideia é reproduzir a **arquitetura** — catálogo consumido de uma API legal, player com Media3, cache/favoritos com Room, paginação (Paging 3), Clean Architecture completa — e não as fontes de conteúdo pirateado do projeto original.
