# Contrato da API

Base padrão: http://localhost:8080. Ao iniciar com --server.port=8081, use http://localhost:8081, como nos testes manuais de Cultura. JSON em camelCase, datas yyyy-MM-dd, áreas em hectares com até duas casas decimais.

Talhão, Cultura e Plantio estão implementados. Cadastro, consultas e colheita de Plantio foram verificados em testes de integração com PostgreSQL. Exemplos de IDs e horários são ilustrativos.

## Convenções

| Item | Valor |
|---|---|
| Endereço base | http://localhost:8080 (8081 quando configurada) |
| Formato | JSON na requisição e na resposta |
| Datas | yyyy-MM-dd |
| Erros | ErroDTO, com lista campos |
| Documentação viva | /swagger-ui.html e /v3/api-docs |
| Relacionamentos | Entrada por IDs; saída por objetos DTO, nunca entidades JPA |

## Rotas

| Verbo | Caminho | O que faz | Entrada | Saída | Sucesso | Erros | Funciona |
|---|---|---|---|---|---|---|---|
| POST | /talhoes | Cadastra talhão | TalhaoRequestDTO | TalhaoResponseDTO | 201 | 400 | sim |
| GET | /talhoes | Lista por ID | Nenhuma | Lista de TalhaoResponseDTO | 200 | — | sim |
| GET | /talhoes/{id} | Busca talhão | ID no caminho | TalhaoResponseDTO | 200 | 400, 404 | sim |
| POST | /culturas | Cadastra cultura | CulturaRequestDTO | CulturaResponseDTO | 201 | 400 | sim |
| GET | /culturas | Lista por ID | Nenhuma | Lista de CulturaResponseDTO | 200 | — | sim |
| GET | /culturas/{id} | Busca cultura | ID no caminho | CulturaResponseDTO | 200 | 400, 404 | sim |
| POST | /plantios | Registra plantio | PlantioRequestDTO | PlantioResponseDTO | 201 | 400, 404 | sim |
| GET | /plantios | Lista por ID | Nenhuma | Lista de PlantioResponseDTO | 200 | — | sim |
| GET | /plantios/{id} | Busca plantio | ID no caminho | PlantioResponseDTO | 200 | 400, 404 | sim |
| PUT | /plantios/{id}/colher | Registra colheita | ColheitaRequestDTO | PlantioResponseDTO | 200 | 400, 404 | sim |

### Rotas das regras de negócio

| Verbo | Caminho | Regra | Sucesso | Erros | Situação |
|---|---|---|---|---|---|
| POST | /plantios | R1 (área), R2 (irrigação), R3 (previsão) | 201 | 400, 404 | Implementada |
| PUT | /plantios/{id}/colher | Impedir repetição e datas inválidas; liberar área | 200 | 400, 404 | Implementada |

Edição e exclusão genéricas não fazem parte do escopo. As rotas de colheita acima são necessárias para o fluxo de negócio.

## DTOs

| DTO | Direção | Campos | Validações / situação |
|---|---|---|---|
| TalhaoRequestDTO | entrada | nome, areaHectares, irrigado | Nome não vazio até 100 caracteres; área de 0,01 a 9999999999,99, até duas casas; Boolean obrigatório |
| TalhaoResponseDTO | saída | id, nome, areaHectares, irrigado | Implementado |
| CulturaRequestDTO | entrada | nome, cicloDias, exigeIrrigacao | Nome não vazio até 100 caracteres; ciclo obrigatório de 1 a 3650; Boolean obrigatório |
| CulturaResponseDTO | saída | id, nome, cicloDias, exigeIrrigacao | Implementado |
| PlantioRequestDTO | entrada | talhaoId, culturaId, areaHectares, dataPlantio | Implementado: IDs positivos obrigatórios, área com limites de Talhão e data obrigatória |
| PlantioResponseDTO | saída | id, talhao (TalhaoResponseDTO), cultura (CulturaResponseDTO), areaHectares, dataPlantio, previsaoColheita, dataColheita | Implementado: objetos DTO aninhados, previsão calculada e colheita inicialmente nula |
| ColheitaRequestDTO | entrada | dataColheita | Implementado: obrigatória e não futura; Service impede data anterior ao plantio e segunda colheita |

## Exemplos e comportamento

## Talhão — implementado por Kauã

POST /talhoes recebe:
~~~json
{"nome":"Talhão Norte","areaHectares":10.50,"irrigado":true}
~~~

Retorna 201, cabeçalho Location: /talhoes/1 e:
~~~json
{"id":1,"nome":"Talhão Norte","areaHectares":10.50,"irrigado":true}
~~~

GET /talhoes retorna 200 e lista desse objeto, ou []. GET /talhoes/{id} retorna 200 e objeto, ou 404 se inexistente. ID não numérico retorna 400.

Nome obrigatório até 100 caracteres; área obrigatória de 0,01 a 9999999999,99, até duas casas; irrigado obrigatório, aceitando false.

## Cultura — implementada

### POST /culturas

Cadastra uma cultura.

Corpo da requisição:

~~~json
{
  "nome": "Milho",
  "cicloDias": 120,
  "exigeIrrigacao": false
}
~~~

Retorna 201 Created, com cabeçalho Location: /culturas/{id}.

Exemplo de resposta:

~~~json
{
  "id": 1,
  "nome": "Milho",
  "cicloDias": 120,
  "exigeIrrigacao": false
}
~~~

O ID é gerado automaticamente pelo banco.

Validações:

- nome obrigatório, não vazio e com até 100 caracteres;
- cicloDias obrigatório, entre 1 e 3650;
- exigeIrrigacao obrigatório, aceitando true ou false.

Dados inválidos retornam 400 no formato de erro padrão da API.

Exemplo de resposta para nome vazio (o timestamp varia a cada requisição):

~~~json
{
  "status": 400,
  "erro": "Bad Request",
  "mensagem": "Dados inválidos: confira a lista de campos",
  "caminho": "/culturas",
  "timestamp": "2026-10-14T23:30:00",
  "campos": [{"campo": "nome", "mensagem": "Nome é obrigatório"}]
}
~~~

### GET /culturas

Retorna 200 OK com a lista de culturas ordenada por ID.

Exemplo de resposta:

~~~json
[
  {
    "id": 1,
    "nome": "Milho",
    "cicloDias": 120,
    "exigeIrrigacao": false
  }
]
~~~

Sem cadastros, retorna uma lista vazia: [].

### GET /culturas/{id}

Retorna 200 OK com os dados da cultura encontrada,
no mesmo formato da resposta de cadastro.

ID inexistente retorna 404 no formato de erro padrão,
com mensagem como: "Cultura não encontrada: 999999".

ID não numérico retorna 400.

Exemplo de resposta para ID inexistente:

~~~json
{
  "status": 404,
  "erro": "Not Found",
  "mensagem": "Cultura não encontrada: 999999",
  "caminho": "/culturas/999999",
  "timestamp": "2026-10-14T23:30:00",
  "campos": []
}
~~~

## Plantio — implementado

R1, R2 e R3 são aplicadas por PlantioService.criar. O Service bloqueia o talhão durante a transação antes de consultar a ocupação; a colheita usa o mesmo bloqueio antes de bloquear o plantio. Assim, cadastros e colheitas concorrentes são serializados por talhão.

POST /plantios recebe:
~~~json
{"talhaoId":1,"culturaId":1,"areaHectares":4.50,"dataPlantio":"2026-10-01"}
~~~

Retorna 201 com Location: /plantios/{id}:
~~~json
{
  "id": 1,
  "talhao": {"id": 1, "nome": "Talhão Norte", "areaHectares": 10.50, "irrigado": true},
  "cultura": {"id": 1, "nome": "Milho", "cicloDias": 120, "exigeIrrigacao": false},
  "areaHectares": 4.50,
  "dataPlantio": "2026-10-01",
  "previsaoColheita": "2027-01-29",
  "dataColheita": null
}
~~~

IDs positivos obrigatórios; área com a validação do talhão; data obrigatória. A previsão e a colheita não pertencem ao DTO de cadastro. O sistema calcula a previsão independentemente de valores extras enviados.

Esta etapa permite data de plantio futura, mas o cadastro ocupa a área imediatamente; não há agendamento por intervalos.

R1: área ativa + nova área não ultrapassa a área do talhão. R2: exigência de irrigação deve ser atendida. Violações retornam 400 com mensagem explicativa. Referências inexistentes retornam 404. R3 soma cicloDias à dataPlantio.

GET /plantios retorna 200 com lista de PlantioResponseDTO ordenada por ID, ou [] se vazia. GET /plantios/{id} retorna 200 com o mesmo objeto mostrado acima; ID inexistente retorna 404, e ID não numérico retorna 400.

PUT /plantios/{id}/colher recebe:
~~~json
{"dataColheita":"2026-10-11"}
~~~

Retorna 200 com o formato de resposta de plantio (incluindo talhao e cultura como DTOs) e dataColheita preenchida. A data é obrigatória, não futura e não anterior ao plantio. Segunda colheita retorna 400; plantio inexistente, 404. A previsão original não muda. O Service verifica datas usando America/Sao_Paulo e mantém as regras temporais de colheita na camada de negócio.

Exemplo da resposta de colheita (para o plantio acima):

~~~json
{
  "id": 1,
  "talhao": {"id": 1, "nome": "Talhão Norte", "areaHectares": 10.50, "irrigado": true},
  "cultura": {"id": 1, "nome": "Milho", "cicloDias": 120, "exigeIrrigacao": false},
  "areaHectares": 4.50,
  "dataPlantio": "2026-10-01",
  "previsaoColheita": "2027-01-29",
  "dataColheita": "2026-10-11"
}
~~~

Exemplo de R1 violada, com horário ilustrativo:

~~~json
{
  "status": 400,
  "erro": "Bad Request",
  "mensagem": "R1: a área dos plantios ativos não pode ultrapassar a área do talhão",
  "caminho": "/plantios",
  "timestamp": "2026-10-15T00:00:00",
  "campos": []
}
~~~

R2 retorna mensagem indicando necessidade de talhão irrigado. Colheita repetida, anterior ao plantio ou futura retorna 400 com a respectiva explicação. R3 calcula a previsão; datas que não permitem esse cálculo retornam 400.

## Erros

~~~json
{
  "status":404,
  "erro":"Not Found",
  "mensagem":"Talhão não encontrado: 999",
  "caminho":"/talhoes/999",
  "timestamp":"2026-10-11T19:30:00",
  "campos":[]
}
~~~

Na validação: status 400, erro Bad Request e campos identifica cada campo inválido. JSON ilegível também retorna 400. Regras violadas usam RegraDeNegocioException e campos vazio.
