# Trilha de estudos Kotlin/Compose

Este repo é a trilha de estudos do usuário. Antes de qualquer coisa, leia `docs/PROGRESSO.md`: a seção "COMO RETOMAR EM OUTRA SESSÃO" (regras de tutoria), o roadmap com `👈 VOCÊ ESTÁ AQUI`, as PENDÊNCIAS e a última entrada do LOG. A teoria fica em `docs/TEORIA.md`.

- Responder em português, tom franco e técnico. Dúvida = scaffold (dica 1 → 2 → 3 → solução só se pedir).
- Não usar em exemplo nenhum conceito da lista PENDÊNCIAS antes de explicá-lo.
- A cada progresso: atualizar `docs/PROGRESSO.md` (roadmap + LOG no topo) e `docs/TEORIA.md`, e regerar o PDF com `/usr/bin/python3 docs/gerar_teoria_pdf.py`.
- Cada projeto (`TemperatureConverter/` etc.) é um projeto Gradle independente. Builds rodam de dentro da pasta dele (`cd TemperatureConverter && ./gradlew assembleDebug`).
- Exercícios de Kotlin puro ficam no repo separado `~/IdeaProjects/kotlin-estudos`.
