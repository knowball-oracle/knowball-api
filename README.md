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

| Recurso      | Endpoint                   | Método HTTP | Descrição                                        |
|--------------|----------------------------|-------------|-------------------------------------------------|
| Championship | /championships             | GET         | Retorna todos os campeonatos cadastrados        |
| Championship | /championships/{id}        | GET         | Retorna campeonato pelo ID específico           |
| Championship | /championships             | POST        | Cria um novo campeonato                          |
| Championship | /championships/{id}        | PUT         | Atualiza dados de um campeonato existente       |
| Championship | /championships/{id}        | DELETE      | Remove um campeonato pelo ID                      |
| Game         | /games                     | GET         | Retorna todas as partidas cadastradas            |
| Game         | /games/{id}                | GET         | Retorna uma partida pelo ID                       |
| Game         | /games/championship/{id}   | GET         | Retorna partidas associadas a campeonato         |
| Game         | /games                     | POST        | Cria uma nova partida                            |
| Game         | /games/{id}                | PUT         | Atualiza dados de uma partida existente          |
| Game         | /games/{id}                | DELETE      | Remove uma partida pelo ID                        |
| Participation| /participations            | GET         | Retorna todas as participações                   |
| Participation| /participations/match/{id} | GET         | Retorna participações por partida                 |
| Participation| /participations            | POST        | Cria nova participação                           |
| Participation| /participations            | PUT         | Atualiza uma participação                        |
| Participation| /participations/match/{matchId}/team/{teamId} | DELETE  | Remove uma participação específica                |
| Referee      | /referees                  | GET         | Retorna todos os árbitros                         |
| Referee      | /referees/{id}             | GET         | Retorna árbitro por ID                            |
| Referee      | /referees                  | POST        | Cria novo árbitro                                |
| Referee      | /referees/{id}             | PUT         | Atualiza árbitro existente                        |
| Referee      | /referees/{id}             | DELETE      | Remove árbitro por ID                             |
| Refereeing   | /refereeing                | GET         | Retorna todos os papéis de arbitragem            |
| Refereeing   | /refereeing/match/{id}     | GET         | Retorna papéis de arbitragem por partida          |
| Refereeing   | /refereeing                | POST        | Cria novo papel de arbitragem                     |
| Report       | /reports                   | GET         | Retorna todas as denúncias                        |
| Report       | /reports/{id}              | GET         | Retorna denúncia por ID                           |
| Report       | /reports/status/{status}   | GET         | Retorna denúncias filtradas por status             |
| Report       | /reports                   | POST        | Cria nova denúncia                               |
| Team         | /teams                     | GET         | Retorna todos os times                            |
| Team         | /teams/{id}                | GET         | Retorna time por ID                              |
| Team         | /teams                     | POST        | Cria novo time                                  |
| Team         | /teams/{id}                | POST        | Atualiza time existente                          |
| Team         | /teams/{id}                | DELETE      | Remove time por ID                               |


### Testando API

- Para acessar a documentação **Swagger (UI e OpenAPI)**:

`https://localhost:8080/swagger-ui.html`

- Use ferramentas como **Insomnia ou Postman** para testar os endpoints REST da API.

# Integrantes

| Nome Completo           | Foto | Responsabilidade |
| ------------------------| ------ | ----- |
| Gabriel Oliveira Rossi  | <a href="https://github.com/GabrielRossi01"><img src="https://avatars.githubusercontent.com/u/179617228?v=4" height="50" style="border-radius:30px;"></a> | Responsável pela concepção, implementação, testes e documentação da API Knowball. |
| Rodrigo Naoki Yamasaki  | <a href="https://github.com/RodrygoYamasaki"><img src="https://avatars.githubusercontent.com/u/182231531?v=4" height="50" style="border-radius:30px;"></a> | Responsável pela participação nas etapas de design e revisão de código. |
| Patrick Castro Quintana | <a href="https://github.com/castropatrick"><img src="https://avatars.githubusercontent.com/u/179931043?v=4" height="50" style="border-radius:30px;"></a> | Responsável pela participação nas etapas de design e revisão de código. |

