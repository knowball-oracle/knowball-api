![Imagem](https://drive.google.com/uc?export=view&id=1q4KTXTHXzJ55r81k-iWLjAIaXT2QPpc_)

[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white)](https://spring.io/projects/spring-data-jpa)
[![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)](https://spring.io/projects/spring-security)
[![Spring AI](https://img.shields.io/badge/Spring_AI-6DB33F?style=for-the-badge&logo=spring&logoColor=white)](https://spring.io/projects/spring-ai)
[![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![Lombok](https://img.shields.io/badge/Lombok-963484?style=for-the-badge&logo=projectlombok&logoColor=white)](https://projectlombok.org/)
[![Bean Validation](https://img.shields.io/badge/Bean_Validation-5A6980?style=for-the-badge&logo=hibernate&logoColor=white)](https://beanvalidation.org/)
[![Oracle](https://img.shields.io/badge/Oracle-F80000?style=for-the-badge&logo=oracle&logoColor=white)](https://www.oracle.com/database/)
[![Oracle APEX](https://img.shields.io/badge/Oracle_APEX-F80000?style=for-the-badge&logo=oracle&logoColor=white)](https://apex.oracle.com/)
[![JDBC](https://img.shields.io/badge/JDBC-007396.svg?style=for-the-badge&logo=java&logoColor=white)](https://www.oracle.com/java/technologies/javase/javase-apis-jdbc.html)
[![API REST](https://img.shields.io/badge/API_REST-000?style=for-the-badge&logo=api&logoColor=white)](https://www.redhat.com/pt-br/topics/api/what-is-a-rest-api)
[![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)](https://swagger.io/)
[![Insomnia](https://img.shields.io/badge/Insomnia-4000BF?style=for-the-badge&logo=insomnia&logoColor=white)](https://insomnia.rest/)
[![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)](https://jwt.io/)
[![OAuth2](https://img.shields.io/badge/OAuth2_Google-4285F4?style=for-the-badge&logo=google&logoColor=white)](https://developers.google.com/identity/protocols/oauth2)
[![Flyway](https://img.shields.io/badge/Flyway-CC0200?style=for-the-badge&logo=flyway&logoColor=white)](https://flywaydb.org/)
[![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)
[![Render](https://img.shields.io/badge/Render-46E3B7?style=for-the-badge&logo=render&logoColor=white)](https://knowball-api.onrender.com)
[![Vercel](https://img.shields.io/badge/Vercel-000000?style=for-the-badge&logo=vercel&logoColor=white)](https://knowball-web-henna.vercel.app)
[![Groq](https://img.shields.io/badge/Groq-F55036?style=for-the-badge&logo=groq&logoColor=white)](https://groq.com/)
[![Brevo](https://img.shields.io/badge/Brevo-0B996E?style=for-the-badge&logo=sendinblue&logoColor=white)](https://www.brevo.com/)
[![TheSportsDB](https://img.shields.io/badge/TheSportsDB-1E293B?style=for-the-badge&logo=databricks&logoColor=white)](https://www.thesportsdb.com/)

## Problemas que a aplicação se propõe a solucionar

O **Knowball** busca resolver os desafios comuns encontrados na gestão e organização dos campeonatos e partidas esportivas, especialmente no **futebol das categorias de base do futebol brasileiro masculino.**
Entre os principais problemas estão:

- Necessidade de um sistema eficiente para registrar e gerenciar **denúncias relacionadas a manipulação de partidas**.
- Falta de **rastreabilidade**: relatos sem protocolo, sem acompanhamento de status e sem retorno ao denunciante.
- Falta de uma plataforma unificada para controle de campeonatos, equipes, jogos e participações, que atualmente são gerenciados por sistemas fragmentados.
- Dificuldade de acompanhar e registrar a atuação dos árbitros e suas respectivas atribuições em cada partida.
- Falta de **APIs RESTful** flexíveis para integração com outras ferramentas e sistemas de gestão esportiva.
- Ausência de **controle de acesso** diferenciado, permitindo que apenas administradores realizem operações de escrita e gestão.
- Falta de **visão analítica** sobre as denúncias (volume, status, categorias e árbitros mais citados) para apoiar a tomada de decisão.

## Público-alvo da aplicação

O Knowball é voltado principalmente para:

- **Árbitros, atletas, técnicos e dirigentes de ligas masculinas das categorias de base do futebol brasileiro masculino (sub-13, sub-15, sub-17 e sub-20).**


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
| **USUÁRIO → DENÚNCIA** | 1:N | Um usuário pode registrar várias denúncias e acompanhá-las pelo protocolo | `id_usuario` em `denuncia` | - |
| **PARTIDA ↔ EQUIPE** | N:M | Relacionamento muitos-para-muitos via tabela `participacao` | `(id_partida, id_equipe)` | `fk_participacao_partida`, `fk_participacao_equipe` |

> A data da denúncia é armazenada como **timestamp com fuso horário** (`Instant` no Java), e a equipe possui a coluna `logo_url` para o emblema do time.

## Diagrama da arquitetura

![Imagem](https://drive.google.com/uc?export=view&id=12vT7y5XvQu60_gSqBE_s_Rr0VmlnEZsR)

---

## Cronograma de desenvolvimento

### Atividades concluídas

| Etapa | Descrição das atividades                                                                      | Data prevista/concluída    |
|-----|-----------------------------------------------------------------------------------------------|----------------------------|
| Levantamento de requisitos e modelagem | Definição das entidades do domínio, regras, relacionamento                                    | 02 de outubro de 2025      |
| Modelagem UML inicial | Elaboração do diagrama de classes no Draw.io                                                  | 03-05 de outubro de 2025   |
| Criação do projeto Spring Boot | Geração do esqueleto e configuração inicial                                                   | 05 de outubro de 2025      |
| Implementação das entidades (models) | Criar classes Championship, Game, Team, Referee, Report, etc                                  | 06-07 de outubro de 2025   |
| Implementação dos repositórios | JPARepository para cada entidade                                                              | 07-08 de outubro de 2025   |
| Criação dos services | Lógica de negócio e validações                                                                | 08-09 de outubro de 2025   |
| Implementação dos controllers (API REST) | Endpoints REST de cada recurso                                                                | 10 de outubro de 2025      |
| Ajustes de nomenclatura (Match -> Game) | Correção devido a palavras reservadas Oracle                                                  | 10 de outubro de 2025      |
| Solução de erros de mapping/compostos | Ajuste de participações, IDs compostos e anotações                                            | 10 de outubro de 2025      |
| Testes de endpoint no Insomnia | Criação e validação de requisições GET/POST/PUT/DELETE                                        | 10 de outubro de 2025      |
| Refinamento do diagrama de classes | Adiciona cardinalidades e enums, revisando clareza                                            | 10 de outubro de 2025      |
| Documentação da API com Swagger/OpenAPI | Documentação automática e exemplos de uso                                                     | 11 de outubro de 2025      |
| Documentação final (Sprint 1) | Consolidação do cronograma, guias de teste e material de apoio                                | 11 de outubro de 2025      |
| Implementação HATEOAS | Links hipermídia nos recursos REST, nível 3 do Richardson Maturity Model                      | 30-31 de outubro de 2025   |
| Paginação e filtros | Pageable em endpoints de usuários, filtros por nome e e-mail                                  | 24-25 de fevereiro de 2026 |
| Configuração do Spring Security | Setup de segurança com JWT assimétrico (RSA) e autenticação stateless                         | 10-20 de março de 2026     |
| Sistema de roles e permissões | Controle de acesso baseado em perfis: `ROLE_ADMIN` e `ROLE_USER`                              | 21-30 de março de 2026     |
| Controle de versão do banco com Flyway | Migrations versionadas para criação de tabelas e dados iniciais                               | 01 de abril de 2026        |
| Camada de visualização — Frontend | Projeto Angular com Tailwind CSS, guards, interceptors, formulários reativos                  | 01-08 de abril de 2026     |
| Preparação para produção | Criação do `application-prod.properties` com variáveis de ambiente                            | 12-13 de maio de 2026      |
| Preparação para produção | Criação do `Dockerfile` multi-stage para build e execução da aplicação                        | 13-14 de maio de 2026      |
| Deploy do Backend | Criação do Web Service no Render e configuração das variáveis de ambiente                     | 14 de maio de 2026         |
| Deploy do Frontend | Frontend disponível em [https://knowball-web-henna.vercel.app](https://knowball-web-henna.vercel.app)                                  | 14 de maio de 2026         |
| Assistente virtual Kiko (Spring AI + Groq) | Integração com modelo de linguagem via Spring AI para suporte ao denunciante                  | 11-14 de junho de 2026     |
| Protocolo automático, filtro por usuário e perfil | Geração automática do protocolo, listagem de denúncias do usuário e edição de perfil | 11-12 de junho de 2026     |
| Foto de perfil | Coluna `CLOB` via Flyway (V3) e suporte a imagem em Base64 no perfil                          | 11-16 de junho de 2026     |
| Notificações por e-mail (Brevo) | Envio de protocolo e atualizações de status; migração de SMTP para a API REST do Brevo         | 13-18 de junho de 2026     |
| Exclusão de denúncias | Endpoint `DELETE /reports/{id}` com verificação de permissão no `ReportService`               | 13-16 de junho de 2026     |
| Dashboard administrativo (Oracle APEX) | Integração da API com Oracle APEX para exibição de KPIs e gráficos analíticos                 | 14-15 de junho de 2026     |
| Endpoints de Analytics | Endpoints REST dedicados à análise de denúncias por status, campeonato, árbitro, categoria e período | 14-19 de junho de 2026 |
| `ReportResponseDTO` | Resposta enxuta na criação de denúncias, devolvendo apenas o que o front-end precisa           | 18 de junho de 2026        |
| Migration do schema completo | Migration Flyway para criar o schema completo em banco vazio e renomeação das versões          | 17 de setembro de 2026     |
| Login com Google (OAuth2) | `oauth2-client` do Spring Security com suporte a usuários OAuth (V8)                         | 18 de setembro de 2026     |
| Atualização do modelo de IA | Troca do modelo descontinuado `llama-3.3-70b-versatile` por `openai/gpt-oss-120b`            | 18 de setembro de 2026     |
| Verificação de e-mail no cadastro | Código de 6 dígitos enviado por e-mail antes do registro (V9)                              | 22 de setembro de 2026     |
| Correção da senha do admin | Nova migration (V10) para ajustar a senha do administrador com BCrypt                       | 22 de setembro de 2026     |
| Emblemas dos times (TheSportsDB) | Integração com a API TheSportsDB para exibir o escudo dos times (V11)                     | 25-26 de setembro de 2026  |
| E-mails de decisão e status | Novos e-mails ao dono da denúncia informando decisão e status definidos pelo admin          | 05 de outubro de 2026      |
| Regras de status e exclusão | Bloqueio de retorno para `NEW` após resolução e regras de exclusão por perfil              | 05 de outubro de 2026      |
| Refino do Kiko | Prompt de sistema aprimorado e papel (role) do usuário vindo do usuário autenticado          | 09 de outubro de 2026      |
| Novos data sources para o APEX | Evolução no tempo, resultado das análises, distribuição por categoria e atividade recente   | 13-14 de outubro de 2026   |
| Atualização de perfil sem senha | Novo DTO `UpdateUserRequest` para alterar o perfil sem exigir nova senha                   | 15 de outubro de 2026      |
| Endpoint de saúde da API | `GET /auth/health` para verificar se o serviço está ativo                                     | 20 de outubro de 2026      |
| Validade do JWT configurável | Propriedade `app.jwt.ttl-hours` (variável `JWT_TTL_HOURS`) no `TokenService`               | 22 de outubro de 2026      |

## Evolução do projeto — Sprint 1 → Sprint 2 → Sprint 3 → Sprint 4 → Ajustes

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
                           → Resposta com token, id, e-mail, nome, role e foto de perfil

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
| PUT | `/reports/{id}/status` | ❌ | ✅ |
| PUT | demais recursos (`/championships`, `/games`, etc.) | ❌ | ✅ |
| DELETE | `/reports/{id}` | ✅ (apenas o autor, com status `NEW`) | ✅ (apenas com status `RESOLVED`) |
| DELETE | demais recursos | ❌ | ✅ |
| GET/PUT | `/users/me` | ✅ | ✅ |
| GET/POST/PUT/DELETE | `/users/**` (gestão) | ❌ | ✅ |
| POST | `/chat` | ✅ | ✅ |
| GET | `/analytics/**` | ❌ | ✅ |
| POST | `/auth/login`, `/auth/register`, `/auth/verification/**` | ✅ (público) | ✅ (público) |
| GET | `/auth/health` | ✅ (público) | ✅ (público) |

> **Chaves RSA:** em desenvolvimento, geradas localmente e armazenadas em `src/main/resources/certs/` como `private_key.pem` e `public_key.pem`. Configuradas via `RsaKeyProperties` com `@ConfigurationProperties`. Em produção, lidas por variáveis de ambiente.

---

#### 🗄️ Flyway — Controle de versão do banco de dados

O Flyway gerencia todas as alterações no schema do banco de forma versionada e rastreável. As migrations são aplicadas automaticamente no startup da aplicação.

| Migration | Descrição |
|-----------|-----------|
| `V1__create_knowball_schema` | Criação do schema completo |
| `V2__insert_admin` | Usuário administrador inicial |
| `V3__add_profile_picture` | Coluna `CLOB` para a foto de perfil |
| `V4__create_email_verification_tokens` | Tabela de tokens de verificação de e-mail |
| `V5__add_email_verified_to_users` | Coluna `email_verified` na tabela de usuários |
| `V6__remove_email_verification` | Remoção da verificação (reimplementada na V9) |
| `V7__alter_report_date_to_timestamptz` | Data da denúncia passa a ser `TIMESTAMP` com fuso horário |
| `V8__add_oauth_support_to_users` | Suporte a usuários autenticados via Google |
| `V9__recreate_email_verification_tokens` | Recriação da tabela de verificação por código |
| `V10__fix_admin_password` | Senha do administrador com hash BCrypt |
| `V11__add_logo_url_to_team` | Coluna `logo_url` para o emblema dos times |

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
| Frontend (Angular) | Vercel | [https://knowball-web-henna.vercel.app](https://knowball-web-henna.vercel.app) |
| Backend (Spring Boot) | Render | [https://knowball-api.onrender.com](https://knowball-api.onrender.com) |
| Banco de dados | Oracle | oracle.fiap.com.br |

> 💤 **Cold Start:** O backend no Render Free Plan pode levar até 60 segundos para responder após períodos de inatividade. Acesse [`/auth/health`](https://knowball-api.onrender.com/auth/health) antes de demonstrações para garantir que o serviço está ativo. A resposta esperada é `{"status":"UP"}`.

---

#### ⚙️ Configuração de ambiente de produção

Criação de perfil `prod` separado do ambiente de desenvolvimento, garantindo que configurações sensíveis nunca sejam expostas no repositório.

- `application-prod.properties` com todas as variáveis sensíveis lidas via variáveis de ambiente
- `RsaKeyProdConfig` — leitura das chaves RSA via variáveis de ambiente no perfil `prod`
- `RsaKeyDevConfig` — ativação do `RsaKeyProperties` exclusivamente no perfil de desenvolvimento
- `SecurityProdConfig` — configuração de CORS apontando para o domínio da Vercel no perfil `prod`
- `@Profile("!prod")` adicionado na `SecurityConfig` original para evitar conflito de beans
- Pool de conexões **HikariCP** ajustado (`maximum-pool-size=5`, `keepalive-time`) para o plano gratuito

**Variáveis de ambiente configuradas no Render:**

| Variável | Descrição |
|----------|-----------|
| `SPRING_PROFILES_ACTIVE` | Ativa o perfil `prod` |
| `DB_URL` | Connection string do Oracle |
| `DB_USERNAME` / `DB_PASSWORD` | Credenciais do banco |
| `RSA_PRIVATE_KEY` | Chave privada RSA para assinatura JWT |
| `RSA_PUBLIC_KEY` | Chave pública RSA para validação JWT |
| `JWT_TTL_HOURS` | Validade do token JWT em horas (padrão: `1`) |
| `CORS_ALLOWED_ORIGINS` | URL do frontend na Vercel |
| `FRONTEND_URL` | URL do frontend, usada no redirecionamento do login com Google |
| `GOOGLE_CLIENT_ID` / `GOOGLE_CLIENT_SECRET` | Credenciais OAuth2 do Google |
| `GROQ_API_KEY` | Chave de autenticação da API Groq (Kiko) |
| `BREVO_API_KEY` | Chave da API REST do Brevo |
| `MAIL_USERNAME` | E-mail remetente cadastrado no Brevo |
| `FOOTBALL_API_KEY` | Chave da API TheSportsDB |
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

> O `Dockerfile` também foi atualizado para utilizar a variável de ambiente `PORT` do Render e garantir o carregamento do `application-prod.properties`. Consulte o arquivo no repositório para a versão em uso.

---

### Ajustes — IA, Notificações, Identidade e Dashboard Analítico

#### 🤖 Kiko — Assistente Virtual com Spring AI

O **Kiko** é o assistente virtual do Knowball, integrado diretamente à API usando **Spring AI** com o modelo **`openai/gpt-oss-120b`** hospedado na plataforma **Groq**. O modelo `llama-3.3-70b-versatile`, usado na primeira versão, foi substituído por ter sido descontinuado.

O Kiko foi projetado para orientar denunciantes durante e após o processo de denúncia, respondendo dúvidas sobre manipulação de partidas, explicando o fluxo de análise e fornecendo suporte contextualizado. A resposta é enviada em **streaming (Server-Sent Events)**, exibindo o texto em tempo real.

**Fluxo de funcionamento:**

```
Usuário → POST /chat  { "message": "..." }  (Authorization: Bearer <token>)
        → ChatController
            → identifica o usuário autenticado pelo e-mail do token
            → busca o usuário no banco e obtém o id e a role confiáveis
        → ChatService (system prompt + contexto do perfil)
        → Spring AI ChatClient
        → Groq API (openai/gpt-oss-120b)
        → Resposta transmitida em streaming (text/event-stream)
```

**Contexto confiável:** o papel do usuário (`USER` ou `ADMIN`) não é enviado pelo cliente. O `ChatController` busca o usuário autenticado pelo e-mail do token e repassa o `id` e a `role` ao `ChatService`, evitando que o perfil seja forjado na requisição.

**System Prompt configurado:** o Kiko possui um prompt de sistema detalhado, que o contextualiza como especialista no domínio do Knowball — conhece as categorias de base, os tipos de manipulação denunciáveis, o fluxo de análise de denúncias e os status do sistema (`NEW`, `UNDER_REVIEW`, `RESOLVED`). O prompt foi refinado em várias iterações para melhorar a qualidade das respostas.

**Tolerância a falhas:** se o serviço de IA estiver indisponível, a API devolve uma mensagem amigável em vez de um erro.

**Variáveis de ambiente necessárias:**

| Variável | Descrição |
|----------|-----------|
| `GROQ_API_KEY` | Chave de autenticação da API Groq |

**Endpoint:**

| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| POST | `/chat` | Enviar mensagem para o Kiko (resposta em streaming) | 🔒 |

***

#### 📧 Notificações por E-mail — Brevo

Integração com o serviço **Brevo** para envio de e-mails transacionais, garantindo que o denunciante seja informado em cada etapa da sua denúncia sem precisar acessar a plataforma.

> **Mudança de arquitetura:** o envio, que inicialmente usava SMTP (e depois o SDK do Brevo), passou a usar a **API REST do Brevo** por meio de `RestTemplate` no `EmailService`. Isso removeu a dependência do SDK e evitou bloqueios de portas SMTP em ambientes de hospedagem.

**Momentos de disparo:**

1. **Verificação de e-mail no cadastro:** o usuário recebe um **código de 6 dígitos** e precisa confirmá-lo antes de concluir o registro.

2. **Criação da denúncia:** ao registrar uma nova denúncia, o usuário recebe um e-mail com:
   - Número de protocolo único gerado automaticamente
   - Detalhes da denúncia (partida, árbitro, categoria)
   - Instruções de acompanhamento

3. **Atualização de status e decisão:** toda vez que o admin altera o status da denúncia, o usuário recebe um e-mail informando a mudança. Para denúncias com status `RESOLVED`, o e-mail inclui também o **parecer/resultado da análise**.

O layout dos e-mails (`buildHtml()`) foi refinado para melhor leitura em diferentes dispositivos.

**Status contemplados nos e-mails:**

| Status | Label no e-mail | Cor |
|--------|----------------|-----|
| `NEW` | Nova | 🔵 Azul |
| `UNDER_REVIEW` | Em Análise | 🟡 Amarelo |
| `RESOLVED` | Resolvida + Parecer | 🟢 Verde |

**Variáveis de ambiente necessárias:**

| Variável | Descrição |
|----------|-----------|
| `BREVO_API_KEY` | Chave da API REST do Brevo |
| `MAIL_USERNAME` | Endereço remetente configurado no Brevo |

***

#### 🔑 Login com Google e verificação de e-mail

- **Login com Google (OAuth2):** autenticação via `oauth2-client` do Spring Security, com escopos `email` e `profile`. Após o login, o usuário é redirecionado ao frontend (`FRONTEND_URL`) com o token da aplicação. Usuários criados por esse fluxo são identificados na tabela de usuários (migration V8).
- **Verificação por código de e-mail:** no cadastro tradicional, o endpoint `POST /auth/verification/send` envia um código de 6 dígitos, e `POST /auth/verification/confirm` valida o código. O `POST /auth/register` só conclui o cadastro se houver uma verificação recente do e-mail.
- **Validade do token configurável:** o tempo de vida do JWT é definido por `app.jwt.ttl-hours` (variável `JWT_TTL_HOURS`, padrão de 1 hora).

***

#### 📋 Regras de negócio das denúncias

- Toda nova denúncia nasce com o status **`NEW`** e recebe um **protocolo único** gerado automaticamente.
- A criação devolve um `ReportResponseDTO` enxuto (`id`, `protocol` e e-mail do autor).
- O admin atualiza o status e o resultado da análise em `PUT /reports/{id}/status`. Depois de **resolvida**, a denúncia **não pode voltar** ao status `NEW`.
- **Exclusão:** o autor só pode excluir a própria denúncia enquanto o status for `NEW`, e o admin só pode excluir denúncias com status `RESOLVED`. Os demais casos retornam `403 Forbidden`.
- A data da denúncia é registrada como `Instant` (timestamp com fuso horário), garantindo consistência nos relatórios mensais.

***

#### 👤 Perfil do usuário

- `GET /users/me` retorna os dados do usuário autenticado, incluindo a foto de perfil.
- `PUT /users/me` altera o nome de exibição e a foto (Base64, com limite de 5 MB). Não altera e-mail, senha ou role, e permite **remover** a foto.
- Na gestão de usuários pelo admin, o `UpdateUserRequest` permite atualizar o perfil e a role **sem exigir uma nova senha**.

***

#### ⚽ Emblemas dos times — TheSportsDB

Integração com a API **TheSportsDB** (`TeamLogoService`) para buscar o emblema dos times do futebol brasileiro. A URL do escudo é armazenada na coluna `logo_url` da tabela de times (migration V11) e retornada pela API. Requer a variável `FOOTBALL_API_KEY`.

***

#### 📊 Dashboard Administrativo — Oracle APEX

Integração da API Knowball com **Oracle APEX** para disponibilizar um painel administrativo com visualização analítica completa dos dados de denúncias. O dashboard é consumido por usuários com perfil `ROLE_ADMIN` e exibe dados diretamente da API, por meio de **REST Data Sources**.

**KPIs exibidos:**

| Indicador | Descrição |
|-----------|-----------|
| Total de Denúncias | Volume geral de denúncias registradas no sistema |
| Denúncias este Mês | Total de denúncias criadas no mês corrente |
| Resolvidas | Quantidade de denúncias com status `RESOLVED` |
| Pendentes | Denúncias com status `NEW` ou `UNDER_REVIEW` |

**Gráficos e visualizações disponíveis:**

- **Denúncias por Status** — distribuição percentual entre `NEW`, `UNDER_REVIEW` e `RESOLVED`
- **Resultado das análises** — distribuição das denúncias resolvidas por resultado
- **Evolução mensal** — volume de denúncias ao longo dos últimos meses (padrão 6, máximo 24)
- **Denúncias por Categoria** — distribuição entre sub-13, sub-15, sub-17 e sub-20
- **Denúncias por Campeonato** — volume agrupado por campeonato e categoria
- **Árbitros mais denunciados** — ranking dos árbitros com maior volume de denúncias
- **Atividade recente** — lista das denúncias mais recentes, sem o texto do relato

**Endpoints de analytics expostos pela API:**

| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| GET | `/analytics/reports/kpis` | KPIs gerais (total, mês atual, resolvidas, pendentes) | 👑 |
| GET | `/analytics/reports/summary` | Resumo consolidado: totais, por status, por campeonato e do mês atual | 👑 |
| GET | `/analytics/reports/by-status` | Contagem agrupada por status | 👑 |
| GET | `/analytics/reports/by-result` | Denúncias resolvidas agrupadas por resultado da análise | 👑 |
| GET | `/analytics/reports/by-category` | Denúncias agrupadas por categoria de base | 👑 |
| GET | `/analytics/reports/by-championship` | Denúncias agrupadas por campeonato e categoria | 👑 |
| GET | `/analytics/reports/by-month?months=6` | Volume de denúncias por mês nos últimos N meses (padrão 6, máx. 24) | 👑 |
| GET | `/analytics/reports/by-referee` | Ranking dos árbitros mais denunciados | 👑 |
| GET | `/analytics/reports/recent?limit=10` | Denúncias mais recentes (padrão 10, máx. 50) | 👑 |

**Arquitetura de integração APEX ↔ API:**

```
Oracle APEX (REST Data Source)
    → GET /analytics/reports/... (Bearer Token via Web Credential)
    → Classic Report / Chart Region
    → Dashboard renderizado para ADMIN
```

***

## Listagem de todos os endpoints

> Endpoints marcados com 🔒 exigem autenticação. Endpoints marcados com 👑 exigem `ROLE_ADMIN`.

### Auth (Autenticação)

| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| POST | `/auth/login` | Autenticar e obter token JWT | Público |
| POST | `/auth/register` | Registrar novo usuário (`ROLE_USER`); exige e-mail verificado | Público |
| POST | `/auth/verification/send` | Enviar código de verificação de 6 dígitos por e-mail | Público |
| POST | `/auth/verification/confirm` | Confirmar o código de verificação | Público |
| GET | `/auth/health` | Verificar se a API está ativa | Público |

> O login com Google é iniciado pelo fluxo OAuth2 do Spring Security (`/oauth2/authorization/google`).

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
| GET | `/reports` | Listar denúncias (o usuário vê as suas; o admin vê todas) | 🔒 |
| GET | `/reports/{id}` | Buscar denúncia por ID | 🔒 |
| GET | `/reports/status/{status}` | Buscar por status | 🔒 |
| POST | `/reports` | Criar nova denúncia (status inicial `NEW`, protocolo automático) | 🔒 |
| PUT | `/reports/{id}/status` | Atualizar status e resultado da análise | 👑 |
| DELETE | `/reports/{id}` | Excluir denúncia (autor com status `NEW`; admin com status `RESOLVED`) | 🔒 |

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
| GET | `/users` | Listar usuários (paginado, com filtros por nome e e-mail) | 👑 |
| GET | `/users/{id}` | Buscar usuário por ID | 👑 |
| POST | `/users` | Criar usuário | 👑 |
| PUT | `/users/{id}` | Atualizar usuário (sem exigir nova senha) | 👑 |
| DELETE | `/users/{id}` | Deletar usuário | 👑 |
| GET | `/users/me` | Buscar o perfil do usuário autenticado | 🔒 |
| PUT | `/users/me` | Atualizar nome e foto do próprio perfil | 🔒 |

### Chat — Kiko (Assistente Virtual)

| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| POST | `/chat` | Conversar com o Kiko (resposta em streaming via SSE) | 🔒 |

### Analytics (Dashboard)

| Método | Endpoint | Descrição | Acesso |
|--------|----------|-----------|--------|
| GET | `/analytics/reports/kpis` | KPIs gerais de denúncias | 👑 |
| GET | `/analytics/reports/summary` | Resumo consolidado do dashboard | 👑 |
| GET | `/analytics/reports/by-status` | Denúncias agrupadas por status | 👑 |
| GET | `/analytics/reports/by-result` | Denúncias resolvidas por resultado da análise | 👑 |
| GET | `/analytics/reports/by-category` | Denúncias por categoria de base | 👑 |
| GET | `/analytics/reports/by-championship` | Denúncias por campeonato e categoria | 👑 |
| GET | `/analytics/reports/by-month` | Volume de denúncias por mês | 👑 |
| GET | `/analytics/reports/by-referee` | Árbitros mais denunciados | 👑 |
| GET | `/analytics/reports/recent` | Denúncias mais recentes | 👑 |

**Total de endpoints:** 59

---

## Instruções de uso

### 🌐 Acesso online (deploy)

A aplicação está publicada e disponível sem necessidade de instalação local:

| Camada | URL |
|--------|-----|
| **Frontend** | [https://knowball-web-henna.vercel.app](https://knowball-web-henna.vercel.app) |
| **Backend** | [https://knowball-api.onrender.com](https://knowball-api.onrender.com) |
| **Swagger (API docs)** | [https://knowball-api.onrender.com/swagger-ui.html](https://knowball-api.onrender.com/swagger-ui.html) |
| **Health check** | [https://knowball-api.onrender.com/auth/health](https://knowball-api.onrender.com/auth/health) |

---

### 🖥️ Execução local

#### Pré-requisitos

- Java 21 (versão utilizada no `Dockerfile`)
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

3. Defina as variáveis de ambiente do banco. O `application.properties` já aponta para o Oracle da FIAP e lê o usuário e a senha do ambiente:
```bash
export DB_USERNAME=<seu_usuario>
export DB_PASSWORD=<sua_senha>
```

4. Defina as variáveis de IA, e-mail, login com Google e emblemas:
```bash
export GROQ_API_KEY=<sua_chave_groq>

export BREVO_API_KEY=<sua_chave_api_brevo>
export MAIL_USERNAME=<remetente_configurado_no_brevo>

export GOOGLE_CLIENT_ID=<seu_client_id>
export GOOGLE_CLIENT_SECRET=<seu_client_secret>

export FOOTBALL_API_KEY=<sua_chave_thesportsdb>

# Opcionais
export FRONTEND_URL=http://localhost:4200
export JWT_TTL_HOURS=1
```

> No Windows (PowerShell), use `$env:NOME="valor"` no lugar de `export`.

5. Gere o par de chaves RSA para assinatura JWT:
```bash
openssl genrsa -out src/main/resources/certs/private_key.pem 2048
```

> ⚠️ **Atenção:** o arquivo `private_key.pem` está no `.gitignore` e **não é versionado** por segurança. É obrigatório gerá-lo localmente antes de executar a aplicação. Sem ele, o Spring Security não conseguirá assinar nem validar os tokens JWT e a aplicação falhará no startup. Caso o repositório não inclua a chave pública `public_key.pem`, gere-a a partir da chave privada.

6. Compile e execute:
```bash
mvn spring-boot:run
```

7. A aplicação estará disponível em: `http://localhost:8080`

> O Flyway aplicará as migrations (V1 a V11) automaticamente no startup — as tabelas e os dados iniciais serão criados sem nenhuma ação manual.

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

> O endpoint `POST /chat` retorna `text/event-stream`. Em clientes como o Insomnia, use o modo de resposta em streaming para ver o texto sendo gerado.

## Integração multidisciplinar

Este projeto também se conecta com a disciplina de **Mastering Relational and Non Relational Database**, especialmente nos pontos abaixo:

- Modelagem e estruturação do banco de dados
- Scripts SQL e versionamento de schema (Flyway, migrations V1 a V11)
- Integração da aplicação com banco Oracle
- Validação das regras de persistência e consistência dos dados
- Consumo dos dados por dashboards no Oracle APEX

## Vídeo demonstração da aplicação funcionando

> 🎬 Clique no link abaixo para assistir no YouTube

[Assista ao vídeo](https://youtu.be/NW1oRrdz700?si=WxiPAh2nUEeeYe0e)

## Integrantes

| Nome Completo           | Foto | Responsabilidade |
| ------------------------| ------ | ----- |
| Gabriel Oliveira Rossi  | <a href="https://github.com/GabrielRossi01"><img src="https://avatars.githubusercontent.com/u/179617228?v=4" height="50" style="border-radius:30px;"></a> | Responsável pela concepção, implementação, testes e documentação da API Knowball. |
| Rodrigo Naoki Yamasaki  | <a href="https://github.com/RodrygoYamasaki"><img src="https://avatars.githubusercontent.com/u/182231531?v=4" height="50" style="border-radius:30px;"></a> | Responsável pela participação nas etapas de design e revisão de código. |
| Patrick Castro Quintana | <a href="https://github.com/castropatrick"><img src="https://avatars.githubusercontent.com/u/179931043?v=4" height="50" style="border-radius:30px;"></a> | Responsável pela participação nas etapas de design e revisão de código. |



---
