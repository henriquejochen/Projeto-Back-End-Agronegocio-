# Controle de Plantios

Projeto de Backend — SATC. Tema: Agronegócio. Etapa 1 em desenvolvimento, a partir do template da disciplina.

## Integrantes

| Integrante | Responsabilidade |
|---|---|
| Kauã Ghizzo | Preparação, Talhão, README e verificação da API simples |
| Henrique Jochen | Cultura e modelo de domínio |
| Thiago Devilla | Plantio, regras de negócio e exemplos no contrato |

Veja [a divisão detalhada](docs/divisao-da-equipe.md).

## O problema

O produtor rural precisa saber quanto de cada talhão está ocupado, quais culturas podem ser plantadas nele e quando a colheita é esperada. O sistema registra áreas, culturas e plantios para evitar ocupação acima da capacidade e cultivo sem irrigação necessária.

Quem usa: o produtor ou responsável pelo manejo cadastra áreas e culturas, registra plantios e conclui colheitas.

| Regra | Comportamento planejado | Implementação prevista |
|---|---|---|
| R1 | A soma das áreas dos plantios ativos, incluindo o novo, não pode ultrapassar a área do talhão. Igualar a área é permitido. | PlantioService.criar — Thiago |
| R2 | Cultura que exige irrigação só pode ser plantada em talhão irrigado. | PlantioService.criar — Thiago |
| R3 | A previsão de colheita é calculada pela data de plantio mais o ciclo da cultura em dias. | PlantioService.criar — Thiago |

R1 e R2 retornam 400 com a regra explicada. R3 é um cálculo automático. Plantio ativo não tem data de colheita; registrar a colheita libera sua área. A colheita não pode anteceder o plantio nem ser registrada duas vezes.

Fora do escopo: frontend, autenticação, estoque, meteorologia, edição e exclusão de cadastros. O ciclo é uma estimativa informada pelo produtor.

## Tecnologias

Java 25, Spring Boot 3.5.16, Maven Wrapper, PostgreSQL 16 via Docker Compose, JPA, Bean Validation, Flyway e Swagger UI. Versões mantidas da base da equipe.

## Arquitetura

Controller → Service → Repository → PostgreSQL. O Service usa o Mapper para converter entidades e DTOs. Controllers usam somente DTOs; regras ficam no Service; tabelas são criadas por migrations. Dependências são injetadas pelo construtor.

| Entidade | Migration | Model | Repository | DTOs | Mapper | Service | Controller | Situação |
|---|---|---|---|---|---|---|---|---|
| Talhão | V1__criar_talhoes.sql | Talhao | TalhaoRepository | TalhaoRequestDTO / TalhaoResponseDTO | TalhaoMapper | TalhaoService | TalhaoController | Implementada |
| Cultura | V2__criar_culturas.sql | Cultura | CulturaRepository | CulturaRequestDTO / CulturaResponseDTO | CulturaMapper | CulturaService | CulturaController | Planejada: arquivos não criados |
| Plantio | V3__criar_plantios.sql | Plantio | PlantioRepository | PlantioRequestDTO / PlantioResponseDTO / ColheitaRequestDTO | PlantioMapper | PlantioService | PlantioController | Planejada: arquivos não criados |

Infraestrutura: Application, OpenApiConfig, ApiExceptionHandler, ErroDTO, CampoErroDTO, RecursoNaoEncontradoException e RegraDeNegocioException. TalhaoNaoEncontradoException especializa o 404. Cultura e Plantio terão suas exceções próprias.

## Entidades

- Talhão: área física de cultivo em hectares, com indicação de irrigação.
- Cultura: cultivo cadastrado com ciclo estimado e exigência de irrigação.
- Plantio: ocupação de parte de um talhão por uma cultura.

Cada plantio pertence a um talhão e uma cultura; ambos podem aparecer em vários plantios.

## Rotas

| Método | Caminho | Sucesso | Erros | Situação |
|---|---|---|---|---|
| POST | /talhoes | 201 | 400 | Implementada |
| GET | /talhoes | 200 | — | Implementada |
| GET | /talhoes/{id} | 200 | 400, 404 | Implementada |
| POST | /culturas | 201 | 400 | Henrique implementará |
| GET | /culturas | 200 | — | Henrique implementará |
| GET | /culturas/{id} | 200 | 400, 404 | Henrique implementará |
| POST | /plantios | 201 | 400, 404 | Thiago implementará |
| PUT | /plantios/{id}/colher | 200 | 400, 404 | Thiago implementará |

## Como executar

Pré-requisitos: JDK 25 configurado em JAVA_HOME, Docker Desktop em execução e portas 5437 e 8080 disponíveis. Na raiz do projeto:

~~~powershell
docker compose up -d --wait
.\mvnw.cmd spring-boot:run
~~~

No Linux/macOS: ./mvnw spring-boot:run. O banco local usa nome, usuário e senha "projeto", conforme o Compose. Flyway cria as tabelas e Hibernate valida o schema.

- Swagger: http://localhost:8080/swagger-ui.html
- OpenAPI para importar no Postman: http://localhost:8080/v3/api-docs

POST /talhoes:

~~~json
{"nome":"Talhão Norte","areaHectares":10.50,"irrigado":true}
~~~

Consulte a listagem e o id retornado. Nome vazio deve retornar 400; id inexistente, 404. Listagem sem registros retorna [].

Para encerrar: Ctrl+C na API e docker compose stop. Alterações em tabelas já criadas exigem migration nova.

## Situação do projeto

Preparação e Talhão implementados. Cultura, Plantio, relacionamentos e as três regras dependem das entregas de Henrique e Thiago. Esta versão não é a entrega final da etapa.

Veja [validação](docs/validacao.md) para testes realizados e limitações do ambiente.

## Documentação

- [Divisão da equipe](docs/divisao-da-equipe.md)
- [Modelo de domínio](docs/modelo-de-dominio.md)
- [Contrato da API](docs/contrato-da-api.md)
- [Autoavaliação](docs/autoavaliacao.md)
