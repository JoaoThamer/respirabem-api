# Roteiro: GitHub → Docker Hub → Azure (RespiraBem API)

Ordem recomendada. Prazo: **16/11/2026 23:55**. Faça tudo com antecedência: o Azure leva um tempo pra subir e a API precisa ficar **no ar durante a correção**.

Anote em um bloco de notas (fora do repositório!) os nomes que você escolher; vou usar estes de exemplo:

| O que | Exemplo |
|---|---|
| Usuário Docker Hub | `lucasfr` |
| Repositório GitHub | `respirabem-api` |
| Resource group | `rg-respirabem` |
| SQL Server (nome único global) | `sql-respirabem-lucas` |
| Banco | `respirabem` |
| Web App (nome único global) | `respirabem-api-lucas` |

---

## Etapa 0 – Testar local antes de tudo

1. Instale JDK 17+ e Maven (ou use só o Docker).
2. Dentro da pasta do projeto: `mvn verify` — deve terminar com `BUILD SUCCESS`.
   - Se der erro de compilação/dependência, **me mande o log**: o código foi escrito sem poder compilar aqui.
3. (Opcional) `docker compose up --build` e abra http://localhost:8080/.

## Etapa 1 – Docker Hub

1. Crie conta em https://hub.docker.com (se ainda não tem).
2. **Create repository**: nome `respirabem-api`, visibilidade **Public**.
3. Gere um token: avatar → **Account settings → Personal access tokens → Generate new token**, permissão **Read & Write**. **Copie e guarde** (só aparece uma vez). Esse token vai pro GitHub Secrets, nunca para o código.

## Etapa 2 – GitHub

1. Crie o repositório **público** `respirabem-api` (sem README, para não dar conflito).
2. Antes do primeiro push, abra `README.md` e `entrega-modelo.txt` e preencha nomes/RM e links (pode atualizar a URL do Azure depois).
3. **Configure os Secrets antes do push** (senão a Action falha no login):
   Repo → **Settings → Secrets and variables → Actions → New repository secret**
   - `DOCKERHUB_USERNAME` = seu usuário do Docker Hub
   - `DOCKERHUB_TOKEN` = o token da Etapa 1
4. No terminal, na pasta do projeto:
   ```bash
   git init
   git add .
   git commit -m "API RespiraBem"
   git branch -M main
   git remote add origin https://github.com/SEU_USUARIO/respirabem-api.git
   git push -u origin main
   ```
5. Aba **Actions**: o workflow "CI - Build e publicação no Docker Hub" deve rodar e ficar **verde**. Tire print (evidência).
6. Confira no Docker Hub: a imagem `SEU_USUARIO/respirabem-api` com as tags `latest` e o hash do commit. Tire print.

> Se o workflow falhar: clique na execução → no passo vermelho → leia o erro. Causas comuns: secret com nome errado, token sem permissão de escrita, teste falhando.

## Etapa 3 – Azure SQL Database

Use sua conta Azure for Students (portal.azure.com).

1. **Resource groups → Create**: `rg-respirabem`, região próxima (ex.: **Brazil South**; se o SQL der erro de capacidade na região, tente **East US 2** ou **Canada Central** para TODOS os recursos).
2. Pesquise **SQL databases → Create**:
   - Resource group: `rg-respirabem`; Database name: `respirabem`
   - Server → **Create new**: nome `sql-respirabem-lucas`, Authentication: **SQL authentication**, defina **login admin** (ex.: `respiraadmin`) e **senha forte** (anote!).
   - Workload environment: Development; Compute: **Serverless** ou o menor tier/Basic (barato/gratuito). Se aparecer a oferta "free offer" do Azure SQL, pode aplicar.
   - Backup redundancy: Locally-redundant.
3. Aba **Networking**: Connectivity method **Public endpoint**; marque **Allow Azure services and resources to access this server = Yes**; e **Add current client IP address = Yes** (para você conseguir conectar do seu PC).
4. **Review + create**.
5. Depois de criado, no **servidor SQL** → *Networking* confirme "Allow Azure services…" = Yes.
6. Monte a connection string JDBC (use o nome do seu servidor):
   ```
   jdbc:sqlserver://sql-respirabem-lucas.database.windows.net:1433;database=respirabem;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;
   ```
   - `DB_USERNAME` = login admin (ex.: `respiraadmin`)
   - `DB_PASSWORD` = a senha que você criou

(Para ver o banco "funcionando", use **Query editor** no portal do banco ou o Azure Data Studio / SSMS; as tabelas serão criadas automaticamente pela API na primeira subida.)

## Etapa 4 – Azure App Service (container)

1. Pesquise **App Services → Create → Web App**:
   - Resource group: `rg-respirabem`; Name: `respirabem-api-lucas` (vira `https://respirabem-api-lucas.azurewebsites.net`)
   - **Publish: Container**; **Operating System: Linux**; mesma região do SQL (ou próxima)
   - Pricing plan: **Free F1** ou **Basic B1** (F1 pode "dormir" e demorar a responder; B1 é mais estável durante a correção, se seu crédito permitir)
2. Aba **Container**:
   - Image Source: **Docker Hub**; Access type: **Public**
   - Image and tag: `SEU_USUARIO/respirabem-api:latest`
3. **Review + create**.
4. Depois de criado: **Settings → Environment variables (App settings) → Add**:
   | Nome | Valor |
   |---|---|
   | `DB_URL` | a JDBC da Etapa 3 |
   | `DB_USERNAME` | login admin do SQL |
   | `DB_PASSWORD` | senha do SQL |
   | `WEBSITES_PORT` | `8080` |
   Clique **Apply/Save** e confirme o restart.
5. **Overview → Restart** e aguarde ~2–5 min (a primeira subida baixa a imagem). Se quiser acompanhar: **Log stream** (ou *Diagnose and solve problems*).
6. Abra `https://SEU-APP.azurewebsites.net/` → deve cair no **Swagger**.

## Etapa 5 – Provar persistência (evidências)

1. No Swagger do Azure: `POST /api/v1/cidades`, `POST /api/v1/grupos-sensiveis`, depois `POST /api/v1/leituras` (use o id retornado) e `POST /api/v1/recomendacoes`. Faça `GET` de cada um.
2. No **Query editor** do banco no portal (login com o admin): `SELECT * FROM cidades;` — os dados aparecem. Print.
3. **Restart** do App Service e faça `GET` de novo: dados continuam lá (prova de persistência).
4. Teste também um erro: `GET /api/v1/cidades/9999` → 404 com corpo padronizado.

## Etapa 6 – Deploy automático (opcional, bônus)

Quando a Action publicar uma nova imagem `latest`, o App Service não baixa sozinho. Faça **Restart** no portal para puxar a versão nova (ou habilite *Continuous deployment* em Deployment Center → Docker Hub).

## Etapa 7 – Entrega no Portal do Aluno

1. Atualize `README.md` (nomes, links reais) e faça `git add . && git commit -m "Atualiza README" && git push` — isso dispara a Action de novo (mais uma evidência verde).
2. Gere o `.txt` seguindo `entrega-modelo.txt`, com **nomes e RM de todos os integrantes**, link do GitHub, da API no Azure e do Docker Hub.
3. Envie no Portal do Aluno até **16/11/2026 23:55**.

## Checklist final (conforme o PDF)

- [ ] Repositório GitHub **público**, com README completo
- [ ] Workflow rodou em push na `main` (histórico visível na aba Actions)
- [ ] Imagem pública no Docker Hub (nome/tag no README)
- [ ] API Azure abre o Swagger na raiz `/`
- [ ] SQL Server (Azure SQL) com dados persistidos
- [ ] **Nenhuma senha/token** no repositório, README ou respostas da API
- [ ] TXT com nomes + RM de todos, e os 3 links
- [ ] Recursos Azure **ligados** até a nota sair (não apague o resource group!)

## Cuidados e problemas comuns

- **Site fica em "Application Error"/timeout:** confira `WEBSITES_PORT=8080`, as 3 variáveis do banco e o Log stream. Erro de login no SQL = usuário/senha/URL errados.
- **Erro de firewall ao conectar no SQL:** ative "Allow Azure services…" no servidor.
- **Serverless pausado:** o 1º acesso pode demorar (~1 min) enquanto o banco "acorda". Faça um acesso de aquecimento antes de entregar.
- **Custos:** acompanhe o crédito em *Cost Management*. Apague os recursos só depois da nota.
- **Segurança:** se um token/senha vazar no Git, revogue/troque na hora; remover o arquivo do commit não basta.
