# Validação

## Verificação automatizada de Plantio

A suíte src/test/java/api/PlantioApiTest.java executa a API HTTP em porta aleatória contra PostgreSQL 16-alpine temporário gerenciado por Testcontainers. As migrations V1, V2 e V3 são aplicadas do zero e o Hibernate valida as entidades. O banco local projeto não é usado nem apagado.

Validação em 15/10/2026: **9 testes executados, 0 falhas, 0 erros e 0 ignorados**. O comando `mvnw.cmd -B verify` concluiu com **BUILD SUCCESS**, executou a suíte e gerou o JAR executável. Resultado conferido em `target/surefire-reports/api.PlantioApiTest.txt`. A execução anterior pelo IntelliJ também terminou com código 0.

Ambiente utilizado nesta validação: Windows 11, Microsoft JDK 25.0.4.1, Maven 3.9.12 e Docker Desktop, com PostgreSQL 16-alpine temporário via Testcontainers. A execução ocorreu na cópia local do IntelliJ; a conferência em clone limpo permanece pendente.

| Teste | O que comprova |
|---|---|
| criaConsultaCalculaPrevisaoEIgnoraCamposControladosPelaApi | POST 201 e Location, DTOs aninhados, GET/listagem, V1–V3, R3 e campos extras sem sobrescrever previsão/colheita |
| permiteOcuparExatamenteALimiteEBloqueiaExcessoSemSalvar | Soma de plantios ativos, capacidade exata aceita, excesso 400 e ausência de registro rejeitado; plantio futuro ocupa área |
| bloqueiaIrrigacaoIncompativelSemOcuparArea | R2 retorna 400 e não persiste plantio inválido |
| referenciasAusentesEParametrosInvalidosRetornamErrosPadronizados | Talhão/cultura/plantio inexistentes 404; ID não numérico, área inválida, campos ausentes e JSON ilegível 400 |
| colheitaValidaLiberaAreaEPreservaPrevisao | Colheita 200, área reutilizável, previsão preservada e segunda colheita 400 |
| rejeitaDatasDeColheitaInvalidasSemLiberarArea | Colheita anterior, futura ou sem data rejeitada; plantio continua ativo |
| cadastrosSimultaneosNaoUltrapassamCapacidade | Dois pedidos de 6 ha em talhão de 10 ha: um 201, outro 400; total ativo 6 ha |
| colheitasSimultaneasNaoFinalizamDuasVezes | Dois pedidos simultâneos de colheita: um 200 e outro 400 |
| swaggerDescreveStatusEObjetosDaResposta | OpenAPI declara 201/400 e talhao como TalhaoResponseDTO |

Para repetir, com Docker Desktop aberto:

```powershell
.\mvnw.cmd test
```

Para compilar, testar e empacotar:

```powershell
.\mvnw.cmd verify
```

A primeira execução pode baixar dependências e imagens. A suíte exige Docker; não ignora silenciosamente os testes quando o banco temporário não pode iniciar. Relatórios são gerados em target/surefire-reports e não devem ser versionados.

## Talhão e Plantio — testes manuais em 15/10/2026

Validação pelo Swagger confirmada por Henrique:

- Talhão cadastrado com 10 hectares e irrigação habilitada; retornou nos dados dos plantios.
- Plantios de milho e aipim cadastrados com 6 hectares e ciclo de 100 dias; a listagem retornou os relacionamentos e a previsão 2027-01-09 para plantio em 2026-10-01.
- Colheita do milho registrada em 2026-10-15; sua área foi liberada e permitiu cadastrar o aipim no mesmo talhão.
- Nova tentativa de cadastrar 6 hectares com o aipim ainda ativo foi bloqueada pela R1, conforme confirmação do usuário.

Esses resultados confirmam os cenários descritos. Os demais casos manuais abaixo continuam como roteiro de conferência; R2, R3 e as regras de colheita também estão cobertas pela suíte automatizada aprovada.

## Cultura — histórico dos testes manuais

Os resultados abaixo foram compartilhados em capturas e confirmados por Henrique durante a sessão, com a API na porta 8081.

| Cenário | Resultado | Evidência |
|---|---|---|
| Cadastro de Milho com ciclo 120 e irrigação false | 201, ID 1 e Location | Captura de resposta |
| Consulta de /culturas/1 | 200 com dados de Milho | Captura de resposta |
| ID inexistente | 404 esperado | Confirmação manual de Henrique |
| Nome vazio | 400 esperado | Confirmação manual de Henrique |
| Ciclo 0 ou 3651 | 400 esperado | Confirmação manual de Henrique |
| Campo exigeIrrigacao ausente | 400 esperado | Confirmação manual de Henrique |
| Listagem | 200 com registros válidos | Confirmação manual de Henrique |
| Consulta após reiniciar | Registros preservados | Confirmação manual de Henrique |

Tentativas antigas de build offline não concluídas foram superadas nesta revisão: as dependências foram obtidas e testes/empacotamento passaram. A validação atual também passou com JDK 25; resta conferir a entrega em clone limpo e a versão publicada.

## Roteiro de demonstração manual

1. Cadastrar talhão com 10 hectares e irrigado true. Anotar o ID retornado.
2. Cadastrar cultura com ciclo 120 e exigeIrrigacao true. Anotar o ID retornado.
3. Usar esses IDs em POST /plantios com área 10 e dataPlantio 2026-10-01: 201, previsão 2027-01-29 e dataColheita nula.
4. Tentar outro plantio de 0,01 ha no mesmo talhão: 400 explicando R1.
5. Cadastrar talhão não irrigado e tentar usar a cultura que exige irrigação: 400 explicando R2.
6. Consultar o plantio por ID e na listagem: 200.
7. Registrar colheita com data não futura e não anterior ao plantio: 200. Confirmar previsão preservada.
8. Repetir a colheita: 400. Cadastrar novo plantio no talhão liberado: 201.
9. Usar referências inexistentes: 404; enviar JSON inválido ou omitir dados obrigatórios: 400.

As datas do roteiro são exemplos; ajuste-as se necessário. Para decidir se uma colheita é futura, o Service usa America/Sao_Paulo.

## Conferência final pendente

- Repetir clone, execução e verify com JDK 25 seguindo o README.
- Revisar os cenários manuais de Talhão: cadastro/consultas, 400/404, limites de área e persistência após reinício.
- Cultura: conferir nome maior que 100 caracteres, campos nulos, ciclos nos limites 1 e 3650 e lista vazia em ambiente isolado.
- Conferir a persistência dos cadastros manuais após reiniciar a aplicação local; o banco local não foi usado pela suíte automatizada.
- Conferir a versão publicada no GitHub e a apresentação com a equipe.
