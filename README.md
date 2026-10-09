# Agronegócio — Backend

Projeto da disciplina de Backend do curso de Engenharia de Software, com o tema **Agronegócio**.

Estrutura inicial baseada no [template da disciplina](https://github.com/placidodaniel/backend-aulas-satc/tree/main/Aula%2010-11/template-projeto-final). O problema específico, as entidades e as regras de negócio serão definidos pelo grupo.

## Tecnologias

- Java 25 e Spring Boot 3.5.16
- Maven, com Maven Wrapper
- PostgreSQL 16 e Docker Compose
- Spring Data JPA, Bean Validation e Flyway
- OpenAPI / Swagger UI (springdoc)

## Estrutura

~~~text
.
├── README.md
├── pom.xml
├── docker-compose.yml
├── mvnw
├── mvnw.cmd
├── .mvn/wrapper/
├── .gitignore
├── docs/
│   ├── modelo-de-dominio.md
│   ├── contrato-da-api.md
│   └── autoavaliacao.md
└── src/
    ├── main/
    │   ├── java/api/
    │   │   ├── Application.java
    │   │   ├── config/
    │   │   ├── controller/
    │   │   ├── dto/
    │   │   ├── mapper/
    │   │   ├── model/
    │   │   ├── repository/
    │   │   ├── service/
    │   │   └── exception/
    │   └── resources/
    │       ├── application.properties
    │       └── db/migration/
    └── test/java/
~~~

As camadas separam a entrada HTTP, os DTOs, as conversões, as entidades, a persistência e os serviços. A configuração e o tratamento genérico de erros vêm do template da disciplina.

## Status

Em desenvolvimento — somente a organização inicial e a infraestrutura do template.
Ainda não há entidades, tabelas de domínio, endpoints de negócio ou regras implementadas. Os documentos em `docs/` serão preenchidos nas próximas etapas.
