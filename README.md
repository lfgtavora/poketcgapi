# PokéTCG API

API REST de catálogo de cartas e coleções (sets) do Pokémon TCG, construída com **Kotlin + Spring Boot 4** em uma arquitetura de **monólito modular** (Spring Modulith). Os dados são sincronizados automaticamente a partir do dataset público [`PokemonTCG/pokemon-tcg-data`](https://github.com/PokemonTCG/pokemon-tcg-data) e servidos localmente, sem depender de API de terceiros em tempo de consulta.

![Kotlin](https://img.shields.io/badge/Kotlin-2.3-7F52FF?logo=kotlin&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1-6DB33F?logo=springboot&logoColor=white)
![Java](https://img.shields.io/badge/JVM-17-ED8B00?logo=openjdk&logoColor=white)
![OpenAPI](https://img.shields.io/badge/OpenAPI-Swagger_UI-85EA2D?logo=swagger&logoColor=black)

## Destaques técnicos

- **Monólito modular com fronteiras verificadas** — os módulos `catalog` e `sync` são validados por teste (`ApplicationModules.verify()`), impedindo que um módulo acesse o interior do outro. Diagramas PlantUML são gerados automaticamente a partir do código.
- **Encapsulamento por pacote** — cada módulo expõe apenas interfaces (`CardService`, `SetService`, `ReplaceSetUseCase`, `GetExistingSetRevisionUseCase`); implementações, entidades JPA e repositórios ficam em `internal/`.
- **Modelo de domínio separado da persistência** — `data class`es imutáveis no domínio (`Card`, `Set`) e entidades JPA mapeadas por extension functions (`toEntity` / `toDomain`).
- **Sincronização incremental e idempotente** — carga inicial via download do `.zip` do repositório; nas execuções seguintes, compara `updatedAt` de cada set e baixa apenas o que mudou. A substituição de um set (set + cartas) ocorre em **uma única transação**.
- **Resiliência configurável** — falhas de sync não derrubam a aplicação por padrão (`poketcg.sync.fail-on-error`), e falha em um set não interrompe os demais.
- **Contrato documentado** — OpenAPI/Swagger UI com exemplos, códigos de resposta e `operationId` explícitos.
- **Tratamento global de erros** — `@RestControllerAdvice` padroniza respostas `404` e `400` (com detalhes por campo).
- **Paginação validada** — Bean Validation em `page` (≥ 1) e `pageSize` (1–250); resposta paginada com `totalPages` e `totalElements`.
- **Batch writes** — JDBC batching e `order_inserts/order_updates` habilitados para carga eficiente de milhares de cartas.

## Arquitetura

```
com.lfgtavora.poketcgapi
├── catalog                      # Módulo: consulta ao catálogo
│   ├── CardService / SetService # API pública do módulo
│   ├── ReplaceSetUseCase        # Porta usada pelo módulo sync
│   ├── model/                   # Domínio (Card, Set, Attack, Legalities, preços...)
│   ├── exception/               # Exceções de domínio
│   ├── web/                     # Controllers REST
│   └── internal/                # Encapsulado: services, use cases, JPA
│       ├── persistence/
│       ├── service/
│       └── usecase/
└── sync                         # Módulo: ingestão do dataset externo
    └── internal/
        ├── TcgDataClient        # Download do zip e fetch incremental (JSON)
        ├── TcgDataSyncService   # Estratégia cold start vs. incremental
        ├── TcgDataSyncRunner    # ApplicationRunner no startup
        ├── mapper/              # JSON externo -> domínio
        └── config/              # @ConfigurationProperties
```

O módulo `sync` depende de `catalog` apenas através de **use cases** públicos, nunca de repositórios ou entidades.

```
 GitHub (pokemon-tcg-data)
          │  zip (1ª carga) / JSON por set (incremental)
          ▼
   ┌─────────────┐   ReplaceSetUseCase    ┌───────────────┐   REST   ┌─────────┐
   │    sync     │ ─────────────────────► │    catalog    │ ◄─────── │ Cliente │
   └─────────────┘  GetExistingSetRevision└───────┬───────┘          └─────────┘
                                                  ▼
                                           H2 (arquivo local)
```

## Stack

| Camada | Tecnologia |
|---|---|
| Linguagem | Kotlin 2.3 (JVM 17) |
| Framework | Spring Boot 4.1 (Web MVC, Data JPA, Validation) |
| Modularização | Spring Modulith 2.1 |
| Persistência | Hibernate / JPA, H2 (modo arquivo) |
| Documentação | springdoc-openapi 3.1 (Swagger UI) |
| Serialização | Jackson Kotlin Module |
| Testes | JUnit 5, kotlin-test, Spring Boot Test, Spring Modulith Test |
| Build | Gradle (Kotlin DSL) + version catalog |

## Como executar

Pré-requisito: **JDK 17+**.

```bash
git clone https://github.com/lfgtavora/poketcgapi.git
cd poketcgapi
./gradlew bootRun
```

Na primeira execução a aplicação baixa o dataset completo e popula o banco em `./data/poketcg`. Nas seguintes, só atualiza os sets alterados.

| Recurso | URL |
|---|---|
| Swagger UI | http://localhost:8080/swagger-ui.html |
| OpenAPI JSON | http://localhost:8080/v3/api-docs |
| Console H2 | http://localhost:8080/h2-console |

### Configuração

```yaml
poketcg:
  sync:
    enabled: true          # desliga a sincronização no startup
    fail-on-error: false   # true = aborta o boot se o sync falhar
    base-url: https://cdn.jsdelivr.net/gh/PokemonTCG/pokemon-tcg-data@master
    zip-url: https://codeload.github.com/PokemonTCG/pokemon-tcg-data/zip/refs/heads/master
```

Qualquer propriedade pode ser sobrescrita por variável de ambiente, ex.: `POKETCG_SYNC_ENABLED=false`.

## Endpoints

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/api/cards/{id}` | Busca carta por id (ex.: `base1-4`) |
| `GET` | `/api/cards` | Lista cartas paginadas |
| `GET` | `/api/cards/search?name=` | Busca por trecho do nome, case-insensitive |
| `GET` | `/api/sets/{id}` | Busca set por id (ex.: `base1`) |
| `GET` | `/api/sets` | Lista sets paginados |

Parâmetros de paginação: `page` (padrão `1`), `pageSize` (padrão e máximo `250`).

```bash
curl "http://localhost:8080/api/cards/search?name=charizard&page=1&pageSize=5"
```

```jsonc
{
  "content": [ { "id": "base1-4", "name": "Charizard", "supertype": "Pokémon", "hp": "120", "...": "..." } ],
  "page": 1,
  "size": 5,
  "totalPages": 3,
  "totalElements": 14
}
```

Erros seguem um formato consistente:

```json
{ "error": "Card not found: xyz" }
```

## Testes

```bash
./gradlew test
```

- **`ModularityTests`** — garante que existem exatamente os módulos `catalog` e `sync` e que nenhuma regra de dependência é violada; também gera a documentação PlantUML.
- **`TcgDataClientTest`** — cliente HTTP/zip do dataset.
- **`TcgDataSyncServiceTest`** — estratégias cold start e incremental, e política de erro.
- **`GithubCardMapperTest`** — mapeamento do JSON externo para o domínio.
- **`PoketcgapiApplicationTests`** — carregamento do contexto Spring.

## Decisões de design

| Decisão | Motivo |
|---|---|
| Spring Modulith em vez de microsserviços | Separação de responsabilidades e fronteiras explícitas sem o custo operacional de sistemas distribuídos; extração futura facilitada. |
| Dataset local (H2) em vez de proxy da API oficial | Latência baixa, sem rate limit e funciona offline após a primeira carga. |
| Comparação por `updatedAt` do set | Sync incremental barato, sem hash de conteúdo nem diff por carta. |
| Replace por set em uma transação | Evita estado parcial (set atualizado com cartas antigas). |
| Domínio desacoplado das entidades JPA | Permite evoluir o schema sem vazar detalhes de persistência para a API. |

## Roadmap

- [ ] Implementar o filtro `q` (sintaxe de consulta) e `orderBy` em `/api/cards` e `/api/sets`
- [ ] Implementar `select` (projeção de campos na resposta)
- [ ] Migrar H2 para PostgreSQL com Flyway
- [ ] Sync agendado (`@Scheduled`) além do startup
- [ ] Testes de integração dos controllers e Testcontainers
- [ ] Dockerfile e pipeline de CI (GitHub Actions)
- [ ] Cache de leitura e métricas via Actuator

## Autor

**Luiz Filipe Távora** — [GitHub](https://github.com/lfgtavora)

Os dados de cartas pertencem aos seus respectivos detentores; este projeto é educacional e não é afiliado à The Pokémon Company.
