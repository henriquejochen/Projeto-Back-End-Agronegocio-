# Contrato da API

Base: http://localhost:8080. JSON em camelCase, datas yyyy-MM-dd, áreas em hectares com até duas casas decimais. Cada integrante completa os exemplos e verifica sua entidade.

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

## Cultura — planejado para Henrique

POST /culturas recebe:
~~~json
{"nome":"Arroz irrigado","cicloDias":120,"exigeIrrigacao":true}
~~~

Retorna 201 com o mesmo objeto acrescido de id. GET /culturas retorna lista (200); GET /culturas/{id} retorna objeto (200) ou 404. ID não numérico retorna 400.

Nome obrigatório até 100 caracteres; ciclo inteiro entre 1 e 3650 dias; exigeIrrigacao obrigatório. Campos inválidos retornam 400. Henrique deve completar exemplos das respostas.

## Plantio — planejado para Thiago

POST /plantios recebe:
~~~json
{"talhaoId":1,"culturaId":1,"areaHectares":4.50,"dataPlantio":"2026-10-01"}
~~~

Retorna 201:
~~~json
{"id":1,"talhaoId":1,"culturaId":1,"areaHectares":4.50,"dataPlantio":"2026-10-01","previsaoColheita":"2027-01-29","dataColheita":null}
~~~

IDs positivos obrigatórios; área com a validação do talhão; data obrigatória. A previsão e a colheita não pertencem ao DTO de cadastro. O sistema calcula a previsão independentemente de valores extras enviados.

Esta etapa permite data de plantio futura, mas o cadastro ocupa a área imediatamente; não há agendamento por intervalos.

R1: área ativa + nova área não ultrapassa a área do talhão. R2: exigência de irrigação deve ser atendida. Violações retornam 400 com mensagem explicativa. Referências inexistentes retornam 404. R3 soma cicloDias à dataPlantio.

PUT /plantios/{id}/colher recebe:
~~~json
{"dataColheita":"2026-10-11"}
~~~

Retorna 200 com o formato de resposta de plantio e dataColheita preenchida. A data é obrigatória, não futura e não anterior ao plantio. Segunda colheita retorna 400; plantio inexistente retorna 404. A previsão original não muda. Thiago deve completar exemplos de resposta e erros.

## Formato de erro

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
