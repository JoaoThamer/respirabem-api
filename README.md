# RespiraBem API

Microsserviço REST do **RespiraBem** (Global Solution 2026.2 – FIAP, tema CitizenTech, **ODS 3 – Saúde e Bem-Estar**).
O app Flutter RespiraBem mostra a qualidade do ar e orientações de saúde; esta API guarda cidades monitoradas, leituras da qualidade do ar (US AQI), grupos sensíveis (crianças, idosos, gestantes…) e as recomendações de saúde para cada grupo em cada nível de AQI.

> **Integrantes:** 

| RM | Nome completo |
|----|---------------|
| 556635 | Enzo Wakao |
| 555367 | João Pedro Thamer Perin |
| 556155 | Lucas Furquim de Campos Roma |

## Links

| Item | Link |
|---|---|
| Repositório GitHub | `https://github.com/JoaoThamer/respirabem-api` |
| API no Azure (Swagger na raiz) | `https://SEU-APP.azurewebsites.net/` |
| Imagem no Docker Hub | `https://hub.docker.com/r/joaothamer/respirabem-api` |

## Arquitetura

Camadas: `Controller → Service → Repository → SQL Server`. Controllers só conversam com Services; regras de negócio ficam nos Services. Entrada/saída via DTOs (`ModelMapper`).

```
controller/  endpoints /api/v1/...
service/     regras de negócio
repository/  Spring Data JPA
model/       entidades JPA
dto/         request e response
exception/   erros padronizados (GlobalExceptionHandler)
config/      ModelMapper e OpenAPI
```

### Entidades (tabelas no plural)

- **cidades** – nome, estado, latitude, longitude, população, dataCadastro
- **leituras_ar** – cidade (N:1), indiceAqi, nivelAqi, pm25, pm10, ozonio, dioxidoNitrogenio, dataHoraMedicao, fonte
- **grupos_sensiveis** – nome, descricao, faixaEtaria, riscoPrincipal, ativo
- **recomendacoes** – grupo (N:1), nivelAqi, titulo, descricao, prioridade, ativa

Relacionamentos: Cidade 1—N LeituraAr; GrupoSensivel 1—N Recomendacao.

### Regras de negócio

- Cidade: não pode haver duplicidade de nome + estado (409); UF é gravada em maiúsculas.
- Leitura: a cidade precisa existir (404); o `nivelAqi` é **calculado** pelo `indiceAqi` (0‑50 BOA, 51‑100 MODERADA, 101‑150 SENSIVEIS, 151‑200 INSALUBRE, 201‑300 MUITO_INSALUBRE, >300 PERIGOSA).
- Grupo sensível: nome único (409).
- Recomendação: o grupo precisa existir (404) e não é possível ativar recomendação para grupo inativo (400).

## Endpoints (`/api/v1`)

Cada recurso tem `POST` (201), `GET` (200), `GET /{id}` (200/404), `PUT /{id}` (200/400/404), `DELETE /{id}` (204/404):

- `/api/v1/cidades`
- `/api/v1/leituras` (filtro opcional `?cidadeId=`)
- `/api/v1/grupos-sensiveis`
- `/api/v1/recomendacoes` (filtros opcionais `?grupoId=` e `?nivelAqi=`)

Erros seguem o formato: `{ timestamp, status, erro, mensagem, caminho, detalhes[] }` — sem stack trace ou detalhes internos.

Swagger/OpenAPI: na raiz `/` (redireciona para `/swagger-ui/index.html`); JSON em `/v3/api-docs`.

## Como executar localmente

Requisitos: **Docker** (Docker Desktop no Windows/Mac). Para a opção B, também JDK 17+ e Maven 3.9+.

### Opção A — Tudo com Docker Compose (recomendado)

Sobe o SQL Server, **cria o banco `respirabem` automaticamente** (container `sqlserver-init`) e só então inicia a API.
Não é preciso instalar SQL Server nem nenhum cliente de banco.

```bash
cp .env.example .env        # Windows PowerShell: copy .env.example .env
# edite o .env e defina DB_PASSWORD (mín. 8 caracteres, com maiúscula, minúscula, número e símbolo)
docker compose up --build
```

Quando aparecer `Started RespirabemApiApplication` no log, abra http://localhost:8080/ (Swagger).

- Parar: `docker compose down` (os dados são mantidos no volume `sqldata`)
- Apagar tudo e recomeçar do zero: `docker compose down -v`

### Opção B — Só o SQL Server no Docker e a API via Maven (desenvolvimento)

```bash
cp .env.example .env
docker compose up -d sqlserver sqlserver-init   # SQL Server + criação do banco
```

Variáveis de ambiente lidas pela API:

| Variável | Padrão | Descrição |
|---|---|---|
| `DB_URL` | `jdbc:sqlserver://localhost:1433;databaseName=respirabem;encrypt=true;trustServerCertificate=true` | URL JDBC |
| `DB_USERNAME` | `sa` | usuário |
| `DB_PASSWORD` | – | senha (**nunca** versionar; use a mesma do `.env`) |
| `DDL_AUTO` | `update` | Hibernate cria/atualiza as tabelas |

```bash
# Linux/macOS
export DB_PASSWORD='SuaSenha#123'
mvn spring-boot:run
```
```powershell
# Windows PowerShell
$env:DB_PASSWORD='SuaSenha#123'
mvn spring-boot:run
```

Abra http://localhost:8080/

### Conferir os dados no banco (sem ferramenta gráfica)

```bash
docker compose exec sqlserver /opt/mssql-tools18/bin/sqlcmd -C -S localhost -U sa -P "SuaSenha#123" -d respirabem -Q "SELECT name FROM sys.tables"
docker compose exec sqlserver /opt/mssql-tools18/bin/sqlcmd -C -S localhost -U sa -P "SuaSenha#123" -d respirabem -Q "SELECT * FROM cidades"
```
(Se o caminho não existir, use `/opt/mssql-tools/bin/sqlcmd`.)

## Testes

```bash
mvn verify
```
Os testes usam H2 em memória (perfil `test`) e cobrem o CRUD, validação (400), não encontrado (404), conflito (409) e regras de negócio.

### Teste manual rápido (curl)

```bash
URL=http://localhost:8080
curl -X POST $URL/api/v1/cidades -H "Content-Type: application/json" \
  -d '{"nome":"São Paulo","estado":"SP","latitude":-23.55,"longitude":-46.63,"populacao":12300000}'
curl -X POST $URL/api/v1/leituras -H "Content-Type: application/json" \
  -d '{"cidadeId":1,"indiceAqi":42,"pm25":10.5,"dataHoraMedicao":"2026-11-05T12:00:00","fonte":"Open-Meteo"}'
curl $URL/api/v1/leituras
```

## Docker Hub

Imagem: `joaothamer/respirabem-api` (tags `latest` e hash do commit).

```bash
docker pull joaothamer/respirabem-api:latest
docker run -p 8080:8080 \
  -e DB_URL='jdbc:sqlserver://HOST:1433;databaseName=respirabem;encrypt=true;trustServerCertificate=true' \
  -e DB_USERNAME=sa -e DB_PASSWORD='SuaSenha#123' \
  joaothamer/respirabem-api:latest
```

Build manual: `docker build -t joaothamer/respirabem-api:latest .`

## GitHub Actions (CI/CD)

Arquivo: `.github/workflows/ci-docker-publish.yml`. A cada `push` na `main`: compila e testa com Maven, faz login no Docker Hub e publica a imagem. Usa os *secrets* `DOCKERHUB_USERNAME` e `DOCKERHUB_TOKEN` (configurados em Settings → Secrets and variables → Actions; não estão no repositório).

## Deploy no Azure

A imagem do Docker Hub roda em um **Azure App Service (Web App for Containers, Linux)** conectado a um **Azure SQL Database**. As credenciais ficam nas *Application settings* do App Service (`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `WEBSITES_PORT=8080`) — nunca no código. Passo a passo em `docs/ROTEIRO-DOCKER-AZURE.md`.

URL pública: `https://SEU-APP.azurewebsites.net/`
