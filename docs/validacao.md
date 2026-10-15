# Validação

## Verificação automatizada de Plantio

A suíte src/test/java/api/PlantioApiTest.java executa a API HTTP em porta aleatória contra PostgreSQL 16-alpine temporário gerenciado por Testcontainers. As migrations V1, V2 e V3 são aplicadas do zero e o Hibernate valida as entidades. O banco local projeto não é usado nem apagado.

Resultado desta revisão: **9 testes executados, 0 falhas, 0 erros e 0 ignorados**. Maven test concluiu com BUILD SUCCESS. O empacotamento posterior com package -DskipTests também concluiu com BUILD SUCCESS e gerou o JAR executável.

Ambiente utilizado: JDK 27, compilação com release 25 declarado no pom.xml, Docker e PostgreSQL 16.15. Falta repetir a entrega em clone limpo com JDK 25.

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

Tentativas antigas de build offline não concluídas foram superadas nesta revisão: as dependências foram obtidas e testes/empacotamento passaram. Isso não substitui a conferência final com JDK 25 e a versão publicada.

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
- Reiniciar a aplicação local para carregar a implementação e aplicar V3; o banco de produção local não foi alterado pelos testes.
- Revisar e enviar os arquivos ao GitHub e conferir a apresentação com a equipe.
