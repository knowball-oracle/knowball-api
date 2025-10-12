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

## Problemas que a aplicação se propõe a solucionar

O **Knowball** busca resolver os desafios comuns encontrados na gestão e organização dos campeonatos e partidas esportivas, especialmente no **futebol das categorias de base do futebol brasileiro masculino.**
Entre os principais problemas estão:

- Necessidade de um sistema eficiente para registrar e gerenciar **denúncias relacionadas a manipulação de partidas**.
- Falta de uma plataforma unificada para controle de campeonatos, equipes, jogos e participações, que atualmente são gerenciados por sistemas fragmentados.
- Dificuldade de acompanhar e registrar a atuação dos árbitros e suas respectivas atribuições em cada partida.
- Falta de **APIs RESTful** flexíveis para integração com outras ferramentas e sistemas de gestão esportiva.

## Público-alvo da aplicação

O Knowball é voltado principalmente para:

- **Árbitros, atletas, técnicos e dirigentes de ligas masculinas das categorias de base do futebol brasileiro masculino(sub-13, sub-15, sub-17 e sub-20).**


## Diagrama de Classe das Entidades

![Imagem](https://drive.google.com/uc?export=view&id=1BYFaZ096cIoCFIdaYMZVoByM-ARmmr5T)


## Diagrama de Entidade e Relacionamento (DER) 

![Imagem](https://drive.google.com/uc?export=view&id=1SwRdekFADcd0YNgE0b47ojdpJCvtygpQ)

## Relacionamentos e constraints do banco de dados

| Relacionamento | Cardinalidade | Descrição | Foreign Key | Constraint |
|----------------|---------------|-----------|-------------|------------|
| **CAMPEONATO → PARTIDA** | 1:N | Um campeonato possui várias partidas | `id_campeonato` em `partida` | `fk_partida_campeonato` |
| **PARTIDA → ARBITRAGEM** | 1:N | Uma partida tem múltiplas arbitragens (árbitro principal + assistentes) | `id_partida` em `arbitragem` | `fk_arbitragem_partida` |
| **ÁRBITRO → ARBITRAGEM** | 1:N | Um árbitro pode atuar em várias partidas | `id_arbitro` em `arbitragem` | `fk_arbitragem_arbitro` |
| **PARTIDA → DENÚNCIA** | 1:N | Uma partida pode gerar várias denúncias | `id_partida` em `denuncia` | `fk_denuncia_partida` |
| **ÁRBITRO → DENÚNCIA** | 1:N | Um árbitro pode ser alvo de várias denúncias | `id_arbitro` em `denuncia` | - |
| **PARTIDA ↔ EQUIPE** | N:M | Relacionamento muitos-para-muitos via tabela `participacao` | `(id_partida, id_equipe)` | `fk_participacao_partida`, `fk_participacao_equipe` |

---

## Cronograma de desenvolvimento

| Etapa                                       | Descrição das atividades                                     | Data prevista/concluída      |
|----------------------------------------------|-------------------------------------------------------------|------------------------------|
| Levantamento de requisitos e modelagem       | Definição das entidades do domínio, regras, relacionamento  | 02 de outubro de 2025        |
| Modelagem UML inicial                        | Elaboração do diagrama de classes no Draw.io                | 03-05 de outubro de 2025     |
| Criação do projeto Spring Boot               | Geração do esqueleto e configuração inicial                 | 05 de outubro de 2025        |
| Implementação das entidades (models)         | Criar classes Championship, Game, Team, Referee, Report, etc| 06-07 de outubro de 2025     |
| Implementação dos repositórios               | JPARepository para cada entidade                            | 07-08 de outubro de 2025     |
| Criação dos services                         | Lógica de negócio e validações                              | 08-09 de outubro de 2025     |
| Implementação dos controllers (API REST)     | Endpoints REST de cada recurso                              | 10 de outubro de 2025        |
| Ajustes de nomenclatura (Match -> Game)      | Correção devido a palavras reservadas Oracle                | 10 de outubro de 2025        |
| Solução de erros de mapping/compostos        | Ajuste de participações, IDs compostos e anotações          | 10 de outubro de 2025        |
| Testes de endpoint no Insomnia               | Criação e validação de requisições GET/POST/PUT/DELETE      | 10 de outubro de 2025        |
| Refinamento do diagrama de classes           | Adiciona cardinalidades e enums, revisando clareza          | 10 de outubro de 2025        |
| Documentação da API com Swagger/OpenAPI      | Documentação automática e exemplos de uso                   | 11 de outubro de 2025        |
| Documentação final                           | Consolidação do cronograma, guias de teste e material de apoio | 11 de outubro de 2025     |


## Instruções de como rodar a aplicação

### Pré-requisitos

- Java 17 ou superior instalado
- Maven instalado
- Banco de dados Oracle (configurado conforme o projeto)

### Passo a passo

1. Clone o repositório:
```bash
git clone https://github.com/knowball-oracle/knowball-api.git
```

2. Entre na pasta do projeto
```bash
cd knowball
```

3. Compile o projeto com Maven:
```bash
mvn clean install
```

4. Execute a aplicação:
```bash
mvn spring-boot:run
```

5. A aplicação estará disponível em: `https://localhost:8080`

## Listagem de todos os endpoints

### Championship (Campeonatos)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/championships` | Listar todos os campeonatos |
| POST | `/championships` | Criar novo campeonato |
| GET | `/championships/{id}` | Buscar campeonato por ID |
| PUT | `/championships/{id}` | Atualizar campeonato |
| DELETE | `/championships/{id}` | Deletar campeonato |

### Game (Partidas)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/games` | Buscar todas as partidas |
| POST | `/games` | Criar nova partida |
| GET | `/games/{id}` | Buscar partida por ID |
| PUT | `/games/{id}` | Atualizar partida por ID |
| DELETE | `/games/{id}` | Deletar partida por ID |
| GET | `/games/championship/{championshipId}` | Buscar partidas por campeonato |

### Team (Times)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/teams` | Listar todos os times |
| POST | `/teams` | Criar novo time |
| GET | `/teams/{id}` | Buscar time por ID |
| PUT | `/teams/{id}` | Atualizar time |
| DELETE | `/teams/{id}` | Deletar time |

### Participation (Participações)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/participations` | Listar todas as participações |
| POST | `/participations` | Criar participação |
| PUT | `/participations` | Atualizar participação |
| GET | `/participations/game/{gameId}` | Listar participações por partida |
| DELETE | `/participations/game/{gameId}/team/{teamId}` | Deletar participação |

### Referee (Árbitros)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/referees` | Listar todos os árbitros |
| POST | `/referees` | Criar novo árbitro |
| GET | `/referees/{id}` | Buscar árbitro por ID |
| PUT | `/referees/{id}` | Atualizar árbitro |
| DELETE | `/referees/{id}` | Deletar árbitro |

### Refereeing (Arbitragem)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/refereeing` | Listar todos os papéis de arbitragem |
| POST | `/refereeing` | Criar papel de arbitragem |
| GET | `/refereeing/game/{gameId}` | Listar papéis de arbitragem por partida |

### Report (Denúncias)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/reports` | Listar todas as denúncias |
| POST | `/reports` | Criar nova denúncia |
| GET | `/reports/{id}` | Buscar denúncia por ID |
| GET | `/reports/status/{status}` | Buscar denúncias por status |

**Total de endpoints:** 31

---

### Testando API

- Para acessar a documentação **Swagger (UI e OpenAPI)**:

`https://localhost:8080/swagger-ui.html`

- Use ferramentas como **Insomnia ou Postman** para testar os endpoints REST da API.


### Importar collection no Insomnia

1. **Baixe a collection:**

[📥 Baixar knowball-api-tests.json](https://drive.google.com/uc?export=download&id=1x1jmJmGsbHSktv2ufjS996ZYl59iDCq1)
  
2. Abra o Insomnia
  
3. Clique em **Application** > **Import/Export** > **Import Data** > **From File**
  
4. Selecione o arquivo baixado
  
5. Pronto! Todas as requisições estarão disponíveis


# Integrantes

| Nome Completo           | Foto | Responsabilidade |
| ------------------------| ------ | ----- |
| Gabriel Oliveira Rossi  | <a href="https://github.com/GabrielRossi01"><img src="https://avatars.githubusercontent.com/u/179617228?v=4" height="50" style="border-radius:30px;"></a> | Responsável pela concepção, implementação, testes e documentação da API Knowball. |
| Rodrigo Naoki Yamasaki  | <a href="https://github.com/RodrygoYamasaki"><img src="https://avatars.githubusercontent.com/u/182231531?v=4" height="50" style="border-radius:30px;"></a> | Responsável pela participação nas etapas de design e revisão de código. |
| Patrick Castro Quintana | <a href="https://github.com/castropatrick"><img src="https://avatars.githubusercontent.com/u/179931043?v=4" height="50" style="border-radius:30px;"></a> | Responsável pela participação nas etapas de design e revisão de código. |

