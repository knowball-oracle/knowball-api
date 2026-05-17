![Imagem](https://drive.google.com/uc?export=view&id=1q4KTXTHXzJ55r81k-iWLjAIaXT2QPpc_)

[![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.java.com/)
[![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![Lombok](https://img.shields.io/badge/Lombok-963484?style=for-the-badge&logo=projectlombok&logoColor=white)](https://projectlombok.org/)
[![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white)](https://spring.io/projects/spring-data-jpa)
[![Bean Validation](https://img.shields.io/badge/Bean_Validation-5A6980?style=for-the-badge&logo=hibernate&logoColor=white)](https://beanvalidation.org/)
[![Oracle](https://img.shields.io/badge/Oracle-F80000?style=for-the-badge&logo=oracle&logoColor=white)](https://www.oracle.com/database/)
[![JDBC](https://img.shields.io/badge/JDBC-007396.svg?style=for-the-badge&logo=java&logoColor=white)](https://www.oracle.com/java/technologies/javase/javase-apis-jdbc.html)
[![API REST](https://img.shields.io/badge/API_REST-000?style=for-the-badge&logo=api&logoColor=white)](https://www.redhat.com/pt-br/topics/api/what-is-a-rest-api)
[![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)](https://swagger.io/)
[![Insomnia](https://img.shields.io/badge/Insomnia-4000BF?style=for-the-badge&logo=insomnia&logoColor=white)](https://insomnia.rest/)
[![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)](https://spring.io/projects/spring-security)
[![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)](https://jwt.io/)
[![Flyway](https://img.shields.io/badge/Flyway-CC0200?style=for-the-badge&logo=flyway&logoColor=white)](https://flywaydb.org/)
[![Render](https://img.shields.io/badge/Render-46E3B7?style=for-the-badge&logo=render&logoColor=white)](https://knowball-api.onrender.com)
[![Vercel](https://img.shields.io/badge/Vercel-000000?style=for-the-badge&logo=vercel&logoColor=white)](https://knowball-web-henna.vercel.app)

## Problemas que a aplicação se propõe a solucionar

O **Knowball** busca resolver os desafios comuns encontrados na gestão e organização dos campeonatos e partidas esportivas, especialmente no **futebol das categorias de base do futebol brasileiro masculino.**
Entre os principais problemas estão:

- Necessidade de um sistema eficiente para registrar e gerenciar **denúncias relacionadas a manipulação de partidas**.
- Falta de uma plataforma unificada para controle de campeonatos, equipes, jogos e participações, que atualmente são gerenciados por sistemas fragmentados.
- Dificuldade de acompanhar e registrar a atuação dos árbitros e suas respectivas atribuições em cada partida.
- Falta de **APIs RESTful** flexíveis para integração com outras ferramentas e sistemas de gestão esportiva.
- Ausência de **controle de acesso** diferenciado, permitindo que apenas administradores realizem operações de escrita.

## Público-alvo da aplicação

O Knowball é voltado principalmente para:

- **Árbitros, atletas, técnicos e dirigentes de ligas masculinas das categorias de base do futebol brasileiro masculino(sub-13, sub-15, sub-17 e sub-20).**


## Diagrama de Classe das Entidades

![Imagem](https://drive.google.com/uc?export=view&id=11HwrNN-SC59HeAK3lF1Ax_RHMsIZQyvY)


## Diagrama de Entidade e Relacionamento (DER) 

![Imagem](https://drive.google.com/uc?export=view&id=1KzUn9k_QbjVizTI6ui4CZs3pfGP9PC7O)

## Relacionamentos e constraints do banco de dados

| Relacionamento | Cardinalidade | Descrição | Foreign Key | Constraint |
|----------------|---------------|-----------|-------------|------------|
| **CAMPEONATO → PARTIDA** | 1:N | Um campeonato possui várias partidas | `id_campeonato` em `partida` | `fk_partida_campeonato` |
| **PARTIDA → ARBITRAGEM** | 1:N | Uma partida tem múltiplas arbitragens (árbitro principal + assistentes) | `id_partida` em `arbitragem` | `fk_arbitragem_partida` |
| **ÁRBITRO → ARBITRAGEM** | 1:N | Um árbitro pode atuar em várias partidas | `id_arbitro` em `arbitragem` | `fk_arbitragem_arbitro` |
| **PARTIDA → DENÚNCIA** | 1:N | Uma partida pode gerar várias denúncias | `id_partida` em `denuncia` | `fk_denuncia_partida` |
| **ÁRBITRO → DENÚNCIA** | 1:N | Um árbitro pode ser alvo de várias denúncias | `id_arbitro` em `denuncia` | - |
| **PARTIDA ↔ EQUIPE** | N:M | Relacionamento muitos-para-muitos via tabela `participacao` | `(id_partida, id_equipe)` | `fk_participacao_partida`, `fk_participacao_equipe` |

## Diagrama da arquitetura

![Imagem](https://drive.google.com/uc?export=view&id=12vT7y5XvQu60_gSqBE_s_Rr0VmlnEZsR)

---

## Cronograma de desenvolvimento

### Atividades concluídas

| Etapa | Descrição das atividades | Data prevista/concluída |
|-------|--------------------------|------------------------|
| Levantamento de requisitos e modelagem | Definição das entidades do domínio, regras, relacionamento | 02 de outubro de 2025 |
| Modelagem UML inicial | Elaboração do diagrama de classes no Draw.io | 03-05 de outubro de 2025 |
| Criação do projeto Spring Boot | Geração do esqueleto e configuração inicial | 05 de outubro de 2025 |
| Implementação das entidades (models) | Criar classes Championship, Game, Team, Referee, Report, etc | 06-07 de outubro de 2025 |
| Implementação dos repositórios | JPARepository para cada entidade | 07-08 de outubro de 2025 |
| Criação dos services | Lógica de negócio e validações | 08-09 de outubro de 2025 |
| Implementação dos controllers (API REST) | Endpoints REST de cada recurso | 10 de outubro de 2025 |
| Ajustes de nomenclatura (Match -> Game) | Correção devido a palavras reservadas Oracle | 10 de outubro de 2025 |
| Solução de erros de mapping/compostos | Ajuste de participações, IDs compostos e anotações | 10 de outubro de 2025 |
| Testes de endpoint no Insomnia | Criação e validação de requisições GET/POST/PUT/DELETE | 10 de outubro de 2025 |
| Refinamento do diagrama de classes | Adiciona cardinalidades e enums, revisando clareza | 10 de outubro de 2025 |
| Documentação da API com Swagger/OpenAPI | Documentação automática e exemplos de uso | 11 de outubro de 2025 |
| Documentação final (Sprint 1) | Consolidação do cronograma, guias de teste e material de apoio | 11 de outubro de 2025 |
| Implementação HATEOAS | Links hipermídia nos recursos REST, nível 3 do Richardson Maturity Model | 30-31 de outubro de 2025 |
| Paginação e filtros | Pageable em endpoints de usuários, filtros por nome e e-mail | 24-25 de fevereiro de 2026 |
| Configuração do Spring Security | Setup de segurança com JWT assimétrico (RSA) e autenticação stateless | 10-20 de março de 2026 |
| Sistema de roles e permissões | Controle de acesso baseado em perfis: `ROLE_ADMIN` e `ROLE_USER` | 21-30 de março de 2026|
| Controle de versão do banco com Flyway | Migrations versionadas para criação de tabelas e dados iniciais | 01 de abril de 2026 |
| Camada de visualização — Frontend | Projeto Angular com Tailwind CSS, guards, interceptors, formulários reativos | 01-08 de abril de 2026 |
| Preparação para produção | Criação do `application-prod.properties` com variáveis de ambiente | 12-13 de maio de 2026 |
| Preparação para produção | Criação do `Dockerfile` multi-stage para build e execução da aplicação | 13-14 de maio de 2026 |
| Deploy do Backend | Criação do Web Service no Render e configuração das variáveis de ambiente | 14 de maio de 2026 |
| Deploy do Frontend | Frontend disponível em https://knowball-web-henna.vercel.app | 14 de maio de 2026 |

## Evolução do projeto — Sprint 1 → Sprint 2 → Sprint 3 -> Sprint 4

### Sprint 1 — Base da API REST

1. **Estrutura base do projeto**
   - Configuração do projeto Spring Boot com Java
   - Integração com Oracle Database

2. **Modelagem de dados**
   - Criação do modelo relacional completo
   - Implementação de 7 entidades principais: `Championship`, `Game`, `Team`, `Participation`, `Referee`, `Refereeing`, `Report`
   - Relacionamentos ManyToOne e chaves compostas com `@EmbeddedId`

3. **Implementação CRUD completa**
   - 31 endpoints REST funcionais
   - Services com lógica de negócio
   - Repositories com JPA
   - Validações com Bean Validation
   - Tratamento de exceções customizado

4. **Documentação**
   - Swagger/OpenAPI integrado
   - Collection do Insomnia exportada
   - README com instruções de uso
   - Diagramas do banco de dados/classes das entidades

5. **Testes**
   - Testes manuais via Insomnia
   - Validação de todos os endpoints

---
  
### Sprint 2 - HATEOAS

Elevar o nível de maturidade da API REST através da implementação de **HATEOAS (Hypermedia as the Engine of Application State)**, alcançando o nível 3 do [*Richardson Maturiy Model.*](https://martinfowler.com/articles/richardsonMaturityModel.html)

![Imagem](https://drive.google.com/uc?export=view&id=1o0nE6SiImMPiHIDpz-kCpVIT4CGJNWFm)

### Sprint 3 — Segurança, Migrations e Frontend

#### 🔐 Spring Security + JWT (RSA)

Implementação de autenticação **stateless** com tokens JWT assinados com chaves **RSA assimétricas**, garantindo que o backend nunca armazene estado de sessão.

**Fluxo de autenticação:**

```
Cliente → POST /auth/login → AuthController
                           → AuthenticationManager (BCrypt)
                           → TokenService → JWT assinado com chave privada RSA
                           → Resposta com token

Cliente → GET /championships (Authorization: Bearer <token>)
        → JwtDecoder (chave pública RSA)
        → JwtAuthenticationConverter → authorities
        → SecurityFilterChain → acesso concedido/negado
```

**Configuração de permissões por endpoint:**

| Método | Endpoint | `ROLE_USER` | `ROLE_ADMIN` |
|--------|----------|:-----------:|:------------:|
| GET | `/championships/**`, `/games/**`, `/referees/**`, `/teams/**`, `/reports/**` | ✅ | ✅ |
| POST | `/reports` | ✅ | ✅ |
| POST | `/championships`, `/games`, `/referees`, `/teams`, `/participations`, `/refereeing` | ❌ | ✅ |
| PUT | `/**` | ❌ | ✅ |
| DELETE | `/**` | ❌ | ✅ |
| GET/POST/PUT/DELETE | `/users/**` | ❌ | ✅ |
| POST | `/auth/login`, `/auth/register` | ✅ (público) | ✅ (público) |

> **Chaves RSA:** geradas localmente e armazenadas em `src/main/resources/certs/` como `private_key.pem` e `public_key.pem`. Configuradas via `RsaKeyProperties` com `@ConfigurationProperties`.

---

#### 🗄️ Flyway — Controle de versão do banco de dados

O Flyway gerencia todas as alterações no schema do banco de forma versionada e rastreável. As migrations são aplicadas automaticamente no startup da aplicação.

### Sprint 4 - Deploy, Produção e Segurança de credenciais

#### 🚀 Deploy da aplicação em produção

Nesta sprint, a aplicação foi publicada online com frontend e backend acessíveis publicamente, tornando a solução funcional de ponta a ponta em ambiente de produção.

### Arquitetura de deploy

A aplicação foi separada nas seguintes camadas de deploy:

- **Frontend:** Angular hospedado na **Vercel**
- **Backend:** API Java com Spring Boot hospedada no **Render**
- **Banco de dados:** Oracle, acessado pela API por meio de variáveis de ambiente seguras

---

| Camada | Plataforma | URL |
|--------|------------|-----|
| Frontend (Angular) | Vercel | https://knowball-web-henna.vercel.app |
| Backend (Spring Boot) | Render | https://knowball-api.onrender.com |
| Banco de dados | Oracle | oracle.fiap.com.br |

> 💤 **Cold Start:** O backend no Render Free Plan pode levar até 60 segundos para responder após períodos de inatividade. Acesse [`/actuator/health`](https://knowball-api.onrender.com/actuator/health) antes de demonstrações para garantir que o serviço está ativo.

---

#### ⚙️ Configuração de ambiente de produção

Criação de perfil `prod` separado do ambiente de desenvolvimento, garantindo que configurações sensíveis nunca sejam expostas no repositório.

- `application-prod.properties` com todas as variáveis sensíveis lidas via variáveis de ambiente
- `RsaKeyProdConfig` — leitura das chaves RSA via variáveis de ambiente no perfil `prod`
- `RsaKeyDevConfig` — ativação do `RsaKeyProperties` exclusivamente no perfil de desenvolvimento
- `SecurityProdConfig` — configuração de CORS apontando para o domínio da Vercel no perfil `prod`
- `@Profile("!prod")` adicionado na `SecurityConfig` original para evitar conflito de beans

**Variáveis de ambiente configuradas no Render:**

| Variável | Descrição |
|----------|-----------|
| `SPRING_PROFILES_ACTIVE` | Ativa o perfil `prod` |
| `DB_URL` | Connection string do Oracle |
| `DB_USERNAME` / `DB_PASSWORD` | Credenciais do banco |
| `RSA_PRIVATE_KEY` | Chave privada RSA para assinatura JWT |
| `RSA_PUBLIC_KEY` | Chave pública RSA para validação JWT |
| `CORS_ALLOWED_ORIGINS` | URL do frontend na Vercel |
| `PORT` | Porta de execução da aplicação |

---

#### 🐳 Dockerfile

Criação do `Dockerfile` multi-stage para build e execução da aplicação no Render:

```dockerfile
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## Listagem de todos os endpoints

> Endpoints marcados com 🔒 exigem autenticação. Endpoints marcados com 👑 exigem `ROLE_ADMIN`.

### Auth (Autenticação)

| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| POST | `/auth/login` | Autenticar e obter token JWT | Público |
| POST | `/auth/register` | Registrar novo usuário (`ROLE_USER`) | Público |

### Championship (Campeonatos)

| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| GET | `/championships` | Listar todos os campeonatos | 🔒 |
| GET | `/championships/{id}` | Buscar campeonato por ID | 🔒 |
| POST | `/championships` | Criar novo campeonato | 👑 |
| PUT | `/championships/{id}` | Atualizar campeonato | 👑 |
| DELETE | `/championships/{id}` | Deletar campeonato | 👑 |

### Game (Partidas)

| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| GET | `/games` | Buscar todas as partidas | 🔒 |
| GET | `/games/{id}` | Buscar partida por ID | 🔒 |
| GET | `/games/championship/{championshipId}` | Buscar partidas por campeonato | 🔒 |
| POST | `/games` | Criar nova partida | 👑 |
| PUT | `/games/{id}` | Atualizar partida | 👑 |
| DELETE | `/games/{id}` | Deletar partida | 👑 |

### Participation (Participações)

| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| GET | `/participations` | Listar todas as participações | 🔒 |
| GET | `/participations/game/{gameId}` | Listar por partida | 🔒 |
| GET | `/participations/game/{gameId}/team/{teamId}` | Buscar por partida e time | 🔒 |
| POST | `/participations` | Criar participação | 👑 |
| PUT | `/participations` | Atualizar participação | 👑 |
| DELETE | `/participations/game/{gameId}/team/{teamId}` | Deletar participação | 👑 |

### Referee (Árbitros)

| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| GET | `/referees` | Listar todos os árbitros | 🔒 |
| GET | `/referees/{id}` | Buscar árbitro por ID | 🔒 |
| POST | `/referees` | Criar novo árbitro | 👑 |
| PUT | `/referees/{id}` | Atualizar árbitro | 👑 |
| DELETE | `/referees/{id}` | Deletar árbitro | 👑 |

### Refereeing (Arbitragem)

| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| GET | `/refereeing` | Listar todas as arbitragens | 🔒 |
| GET | `/refereeing/game/{gameId}` | Listar por partida | 🔒 |
| GET | `/refereeing/game/{gameId}/referee/{refereeId}` | Buscar por partida e árbitro | 🔒 |
| POST | `/refereeing` | Criar arbitragem | 👑 |

### Report (Denúncias)

| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| GET | `/reports` | Listar todas as denúncias | 🔒 |
| GET | `/reports/{id}` | Buscar denúncia por ID | 🔒 |
| GET | `/reports/status/{status}` | Buscar por status | 🔒 |
| POST | `/reports` | Criar nova denúncia | 🔒 |
| PUT | `/reports/{id}/status`| Atualiza status da denúncia | 👑 |

### Team (Times)

| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| GET | `/teams` | Listar todos os times | 🔒 |
| GET | `/teams/{id}` | Buscar time por ID | 🔒 |
| POST | `/teams` | Criar novo time | 👑 |
| PUT | `/teams/{id}` | Atualizar time | 👑 |
| DELETE | `/teams/{id}` | Deletar time | 👑 |

### Users (Usuários)

| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| GET | `/users` | Listar usuários (paginado, com filtros) | 👑 |
| GET | `/users/{id}` | Buscar usuário por ID | 👑 |
| POST | `/users` | Criar usuário | 👑 |
| PUT | `/users/{id}` | Atualizar usuário | 👑 |
| DELETE | `/users/{id}` | Deletar usuário | 👑 |

**Total de endpoints:** 43

---

## Instruções de uso

### 🌐 Acesso online (deploy)

A aplicação está publicada e disponível sem necessidade de instalação local:

| Camada | URL |
|--------|-----|
| **Frontend** | [https://knowball-web-henna.vercel.app](https://knowball-web-henna.vercel.app) |
| **Backend** | [https://knowball-api.onrender.com](https://knowball-api.onrender.com) |
| **Swagger (API docs)** | [https://knowball-api.onrender.com/swagger-ui.html](https://knowball-api.onrender.com/swagger-ui.html) |

---

### 🖥️ Execução local

#### Pré-requisitos

- Java 17 ou superior
- Maven instalado
- Banco de dados Oracle acessível
- Node.js 18+ e Angular CLI (para o frontend)

#### Passo a passo

1. Clone o repositório:
```bash
git clone https://github.com/knowball-oracle/knowball-api.git
```

2. Entre na pasta do projeto:
```bash
cd knowball-api
```

3. Configure as credenciais do banco no `application.properties`:
```properties
spring.datasource.url=jdbc:oracle:thin:@<host>:<porta>:<sid>
spring.datasource.username=<seu_usuario>
spring.datasource.password=<sua_senha>
```

4. Compile e execute:
```bash
mvn spring-boot:run
```

5. A aplicação estará disponível em: `http://localhost:8080`

> O Flyway aplicará as migrations automaticamente no startup — as tabelas e os dados iniciais serão criados sem nenhuma ação manual.

---

### 🧪 Testando a API

**Via Swagger (recomendado):**

- Online: [https://knowball-api.onrender.com/swagger-ui.html](https://knowball-api.onrender.com/swagger-ui.html)
- Local: `http://localhost:8080/swagger-ui.html`

Para testar endpoints protegidos no Swagger:
1. Faça login via `POST /auth/login` e copie o token retornado
2. Clique em **Authorize** (cadeado) no topo da página
3. Digite `Bearer <seu_token>` e confirme

**Via Insomnia ou Postman:**

Importe a collection disponível no repositório e aponte a variável de ambiente `baseUrl` para:
- **Online:** `https://knowball-api.onrender.com`
- **Local:** `http://localhost:8080`

## Integração multidisciplinar

Este projeto também se conecta com a disciplina de **Mastering Relational and Non Relational Database**, especialmente nos pontos abaixo:

- Modelagem e estruturação do banco de dados
- Scripts SQL e versionamento de schema
- Integração da aplicação com banco Oracle
- Validação das regras de persistência e consistência dos dados

## Vídeo demonstração da aplicação funcionando

> 🎬 Clique no link abaixo para assistir no YouTube

[Assista ao vídeo](https://youtu.be/NW1oRrdz700?si=WxiPAh2nUEeeYe0e)

## Integrantes

| Nome Completo           | Foto | Responsabilidade |
| ------------------------| ------ | ----- |
| Gabriel Oliveira Rossi  | <a href="https://github.com/GabrielRossi01"><img src="https://avatars.githubusercontent.com/u/179617228?v=4" height="50" style="border-radius:30px;"></a> | Responsável pela concepção, implementação, testes e documentação da API Knowball. |
| Rodrigo Naoki Yamasaki  | <a href="https://github.com/RodrygoYamasaki"><img src="https://avatars.githubusercontent.com/u/182231531?v=4" height="50" style="border-radius:30px;"></a> | Responsável pela participação nas etapas de design e revisão de código. |
| Patrick Castro Quintana | <a href="https://github.com/castropatrick"><img src="https://avatars.githubusercontent.com/u/179931043?v=4" height="50" style="border-radius:30px;"></a> | Responsável pela participação nas etapas de design e revisão de código. |

