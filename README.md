# Controle de Plantios

Backend para produtores e responsáveis pelo manejo cadastrarem talhões e culturas. O sistema está sendo desenvolvido para controlar a ocupação das áreas, a necessidade de irrigação e a previsão de colheita.

Projeto Final da disciplina de Backend (Engenharia de Software, SATC). **Etapa 1: arquitetura do backend.**

## Índice

- [Integrantes](#integrantes)
- [O problema](#o-problema)
- [Tecnologias](#tecnologias)
- [Arquitetura](#arquitetura)
- [Entidades](#entidades)
- [Rotas](#rotas)
- [Como executar](#como-executar)
- [Situação do projeto](#situação-do-projeto)
- [Documentação](#documentação)

## Integrantes

| Nome | GitHub | Responsabilidade |
|---|---|---|
| Kauã Ghizzo | [@KauaGhizzo](https://github.com/KauaGhizzo) | Preparação, Talhão e execução |
| Henrique Jochen | [@henriquejochen](https://github.com/henriquejochen) | Cultura e modelo de domínio |
| Thiago Devilla | [@thiagodevilla](https://github.com/thiagodevilla) | Plantio e regras de negócio |

Veja a [divisão detalhada](docs/divisao-da-equipe.md). As responsabilidades não comprovam autoria de commits ou integração no GitHub.

## O problema

**Tema sorteado:** Agronegócio.

O produtor rural precisa saber quanto de cada talhão está ocupado, quais culturas podem ser plantadas nele e quando a colheita é esperada. Sem esse controle, pode planejar plantios acima da área disponível ou cultivar uma cultura que exige irrigação em um talhão sem essa estrutura. A proposta registra áreas, culturas e plantios e libera a área quando a colheita é registrada. Atualmente, apenas cadastro e consultas de Talhão e Cultura estão implementados.

**Quem usa o sistema:**

| Quem | O que faz no sistema |
|---|---|
| Produtor ou responsável pelo manejo | Cadastra talhões e culturas; registra plantios e colheitas |

**Regras de negócio:** especificadas, ainda não implementadas. Validações de campos obrigatórios e números positivos nos DTOs não substituem essas regras.

| # | Regra | Onde está no código | Rota que usa |
|---|---|---|---|
| R1 | A soma das áreas dos plantios ativos, incluindo o novo, não pode ultrapassar a área do talhão; igualdade é permitida | PlantioService.criar | POST /plantios (implementada) |
| R2 | Cultura que exige irrigação só pode ser plantada em talhão irrigado | PlantioService.criar | POST /plantios (implementada) |
| R3 | A previsão de colheita é calculada pela data de plantio mais o ciclo da cultura em dias | PlantioService.criar | POST /plantios (implementada) |

R1 e R2 violadas deverão retornar 400 com a regra explicada. R3 será um cálculo automático. Plantio ativo terá data de colheita nula; registrar a colheita liberará sua área. A colheita não poderá anteceder o plantio, ser futura ou ser registrada duas vezes. O cadastro deverá proteger o limite de área também em requisições simultâneas.

**Fora do escopo:** frontend, autenticação, estoque, meteorologia, edição e exclusão de cadastros. Registrar colheita será uma operação própria, não uma edição genérica. O ciclo informado pelo produtor é uma estimativa.

## Tecnologias

| O quê | Qual |
|---|---|
| Linguagem | Java 25 definido no pom.xml |
| Framework | Spring Boot 3.5.16: Web, Validation e Data JPA |
| Banco de dados | PostgreSQL 16, em Docker Compose |
| Migrations | Flyway |
| Documentação da API | springdoc-openapi 2.9.1 e Swagger UI |
| Build | Maven Wrapper |

## Arquitetura

Controller → Service → Repository → PostgreSQL. O Service usa o Mapper para converter entidades, DTOs e listas. Controllers usam somente DTOs; regras ficam no Service; tabelas são criadas por migrations. Dependências são injetadas pelo construtor.

**Mapa da estrutura:** todas as entidades do modelo, incluindo Plantio implementado. “Pendente” significa que o arquivo não existe; não equivale a classe montada.

| Entidade | Migration | Model | Repository | DTOs | Mapper | Service | Controller |
|---|---|---|---|---|---|---|---|
| Talhão | V1__criar_talhoes.sql, pronta | Talhao, implementada | TalhaoRepository, implementado | TalhaoRequestDTO / TalhaoResponseDTO, implementados | TalhaoMapper, implementado | TalhaoService, implementado | TalhaoController, implementado |
| Cultura | V2__criar_culturas.sql, pronta | Cultura, implementada | CulturaRepository, implementado | CulturaRequestDTO / CulturaResponseDTO, implementados | CulturaMapper, implementado | CulturaService, implementado | CulturaController, implementado |
| Plantio | V3__criar_plantios.sql | Plantio | PlantioRepository | PlantioRequestDTO / PlantioResponseDTO / ColheitaRequestDTO | PlantioMapper | PlantioService | PlantioController |

Infraestrutura implementada: Application, OpenApiConfig, ApiExceptionHandler, ErroDTO, CampoErroDTO, RecursoNaoEncontradoException e RegraDeNegocioException. TalhaoNaoEncontradoException e CulturaNaoEncontradaException especializam o erro 404. PlantioNaoEncontradoException especializa o erro 404 de Plantio. As exceções ficam em service, conforme o template da disciplina.

## Entidades

| Entidade | O que representa | Como se relaciona |
|---|---|---|
| Talhão | Área física de cultivo em hectares, com indicação de irrigação | Poderá receber vários plantios |
| Cultura | Cultivo com ciclo estimado e exigência de irrigação | Poderá aparecer em vários plantios |
| Plantio | Ocupação de parte de um talhão por uma cultura | Pertence a um talhão e uma cultura |

Os relacionamentos ainda não existem no banco nem no código. Atributos, tipos e diagrama estão em [docs/modelo-de-dominio.md](docs/modelo-de-dominio.md).

## Rotas

### As que já funcionam

Implementadas no código; os testes manuais confirmados e os cenários pendentes estão em [validação](docs/validacao.md).

| Verbo | Caminho | O que faz | Regra | Sucesso | Erros |
|---|---|---|---|---|---|
| POST | /talhoes | Cadastra talhão | Validações do DTO | 201 | 400 |
| GET | /talhoes | Lista talhões por ID | Nenhuma | 200 | — |
| GET | /talhoes/{id} | Consulta talhão | Nenhuma | 200 | 400, 404 |
| POST | /culturas | Cadastra cultura | Validações do DTO | 201 | 400 |
| GET | /culturas | Lista culturas por ID | Nenhuma | 200 | — |
| GET | /culturas/{id} | Consulta cultura | Nenhuma | 200 | 400, 404 |

Exemplo de requisição:

```http
POST /culturas
Content-Type: application/json

{"nome":"Milho","cicloDias":120,"exigeIrrigacao":false}
```

Resposta: 201 Created, com Location: /culturas/1. O ID é gerado pelo banco e pode variar.

```json
{"id":1,"nome":"Milho","cicloDias":120,"exigeIrrigacao":false}
```

### Erros

Campo inválido retorna 400; ID inexistente retorna 404. Exemplo de nome vazio, com horário ilustrativo:

```json
{
  "status": 400,
  "erro": "Bad Request",
  "mensagem": "Dados inválidos: confira a lista de campos",
  "caminho": "/culturas",
  "timestamp": "2026-10-14T23:30:00",
  "campos": [{"campo": "nome", "mensagem": "Nome é obrigatório"}]
}
```

JSON ilegível e ID não numérico também retornam 400. As regras de Plantio usam RegraDeNegocioException, com mensagem explicativa e campos vazio.

### Plantio e colheita

Implementadas no código; execução local ainda precisa ser validada.

| Recurso | O que vai fazer |
|---|---|
| POST /plantios | Registrar plantio aplicando R1, R2 e R3 |
| PUT /plantios/{id}/colher | Registrar colheita válida e liberar área |

O contrato completo, os DTOs e exemplos estão em [docs/contrato-da-api.md](docs/contrato-da-api.md).

## Como executar

Pré-requisitos: JDK 25 configurado em JAVA_HOME, Docker Desktop aberto, Git e portas 5437 e 8080 disponíveis. Na primeira execução é necessário acesso à internet para baixar Maven, dependências e imagem do banco.

Para obter uma cópia nova:

```powershell
git clone https://github.com/henriquejochen/Projeto-Back-End-Agronegocio-.git
cd Projeto-Back-End-Agronegocio-
```

Se já baixou o ZIP, abra a pasta que contém pom.xml, docker-compose.yml e mvnw.cmd. Ela pode estar dentro de outra pasta com o mesmo nome. Não precisa clonar novamente.

```powershell
docker compose up -d --wait
.\mvnw.cmd spring-boot:run
```

No Linux/macOS, use ./mvnw spring-boot:run. O banco local usa nome, usuário e senha projeto. Flyway aplica as migrations; Hibernate valida o schema. Não crie tabelas manualmente.

| Endereço | O que é |
|---|---|
| http://localhost:8080/swagger-ui.html | Swagger UI para testar as rotas |
| http://localhost:8080/v3/api-docs | Contrato OpenAPI para importar no Postman |

No Postman: Import → Link → endereço de /v3/api-docs.

Se a porta 8080 estiver ocupada:

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.arguments=--server.port=8081"
```

Nesse caso, use http://localhost:8081/swagger-ui.html e http://localhost:8081/v3/api-docs. A porta 8081 foi usada nos testes manuais de Cultura. Mantenha o terminal aberto durante os testes.

Para parar: Ctrl+C na API e docker compose stop no banco. Alterações em tabelas já criadas exigem migration nova; não altere V1 ou V2 já aplicadas.

## Situação do projeto

| Parte | Situação |
|---|---|
| Desenho | Problema, regras, modelo, diagrama e contrato documentados; Plantio implementado |
| Estrutura | Talhão e Cultura com todas as camadas; Plantio com todas as camadas |
| API e regras | Cadastro e consultas de Talhão e Cultura implementados; testes manuais de Cultura confirmados; R1–R3 implementadas; testes de execução pendentes |

Plantio e colheita estão implementados. Oito testes unitários foram adicionados, mas não executados aqui: o Maven não conseguiu acessar o repositório de dependências e o ambiente possui Java 17, não 25. Falta validar HTTP, migration e concorrência no PostgreSQL e integrar no GitHub. Veja [a explicação e o roteiro de Thiago](docs/parte-thiago.md).

## Documentação

| Documento | O que tem |
|---|---|
| [Modelo de domínio](docs/modelo-de-dominio.md) | Entidades, tipos, relacionamentos, diagrama e migrations |
| [Contrato da API](docs/contrato-da-api.md) | Rotas, DTOs, exemplos e erros |
| [Autoavaliação](docs/autoavaliacao.md) | P1–P10, marcas, evidências e estimativa provisória |
| [Validação](docs/validacao.md) | Testes confirmados e cenários pendentes |
| [Divisão da equipe](docs/divisao-da-equipe.md) | Responsabilidades e integração |
