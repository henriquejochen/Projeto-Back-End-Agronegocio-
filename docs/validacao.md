> Atualização de Thiago: Plantio e colheita foram implementados nesta cópia.
> Veja [parte-thiago.md](parte-thiago.md) para os arquivos, regras e verificação pendente.
> Os registros abaixo foram preservados como histórico da base recebida.

# Validação

## Origem das evidências

Os resultados abaixo foram observados nas capturas/logs compartilhados e confirmados por Henrique durante os testes manuais desta sessão. A revisão inicial da documentação conferiu o código sem repetir os testes. Na revisão posterior de alinhamento ao template, a compilação foi verificada, conforme a seção final; os testes HTTP anteriores não foram repetidos.

O log apresentado mostra conexão ao PostgreSQL 16.15 na porta 5437, aplicação bem-sucedida de V1 e V2 e inicialização do JPA. A primeira tentativa encerrou porque a porta 8080 estava ocupada. Os testes seguintes ocorreram na porta 8081.

O pom.xml declara Java 25. O log apresentado mostra Java 27; ainda falta confirmar a execução em clone limpo com JDK 25, conforme o README.

## Cultura — testes manuais confirmados

| Cenário | Resultado observado | Evidência |
|---|---|---|
| Cadastro de Milho com ciclo 120 e exigeIrrigacao false | 201, ID 1 e Location: /culturas/1 | Captura da resposta |
| Consulta de /culturas/1 | 200 com os dados de Milho | Captura da resposta |
| Consulta de ID inexistente | Erro esperado 404 | Confirmação manual de Henrique; sem captura específica do status |
| Cadastro com nome vazio | Erro esperado 400 | Confirmação manual de Henrique |
| Cadastro com ciclo de 0 dias | Erro esperado 400 | Confirmação manual de Henrique |
| Cadastro com ciclo de 3651 dias | Erro esperado 400 | Confirmação manual de Henrique |
| Cadastro sem exigeIrrigacao | Erro esperado 400 | Confirmação manual de Henrique |
| Listagem de culturas | 200 com cadastros válidos; entradas rejeitadas ausentes | Confirmação manual de Henrique |
| Consulta após reiniciar a aplicação | Registros preservados | Confirmação manual de Henrique |

Esses testes cobrem cadastro, consultas e algumas validações de Cultura. Não comprovam as regras de Plantio, que ainda não existem.

## Histórico da base

Na preparação inicial, houve tentativas de build offline e com Java 21 que não foram concluídas por limitações do ambiente. Kauã posteriormente informou execução no IntelliJ. Esses registros são históricos: não significam que o banco continue indisponível, pois a sessão atual confirmou conexão, migrations e operações de Cultura.

## Testes complementares pendentes

- Cultura: nome ausente, só com espaços ou maior que 100 caracteres; ciclo ausente; irrigação nula; JSON malformado e ID não numérico.
- Cultura: confirmar que ciclos nos limites 1 e 3650 são aceitos; verificar listagem vazia em banco de testes isolado.
- Talhão: confirmar POST 201 e Location, listagem/consulta 200, ID inexistente 404 e ID não numérico 400.
- Talhão: conferir nome vazio, área zero/negativa, mais de duas casas decimais e irrigação ausente com retorno 400; confirmar false aceito e persistência após reinício.
- Em ambos: registrar resultados e conferir o formato padronizado de erros, sem marcar cenários ainda não executados como aprovados.
- Repetir a execução em clone limpo com JDK 25, Docker e o roteiro do README.

Não há testes automatizados implementados em src/test/java. Para a próxima etapa, priorizar testes das regras de Plantio e de sua integração com o banco.

## Plantio — roteiro para depois da implementação

1. Cadastrar talhão de 10 hectares e culturas com e sem exigência de irrigação.
2. Ocupar exatamente os 10 hectares com um ou mais plantios ativos: permitido.
3. Tentar exceder a área: 400 com explicação de R1.
4. Tentar cultura que exige irrigação em talhão não irrigado: 400 com explicação de R2.
5. Conferir R3: 2026-10-01 + 120 dias = 2027-01-29; previsão enviada como campo extra não deve substituir o cálculo.
6. Informar talhão ou cultura inexistente: 404.
7. Registrar colheita válida, confirmar liberação da área e permitir novo plantio. A previsão original não muda.
8. Tentar segunda colheita, data futura, data anterior ao plantio ou data ausente: 400. Plantio inexistente: 404.
9. Enviar cadastros simultâneos para o mesmo talhão: a soma das áreas ativas não pode ultrapassar sua capacidade.
10. Atualizar README, contrato e autoavaliação com resultados efetivamente verificados.

## Verificação do alinhamento ao template

- Padronizados os cabeçalhos das 23 classes/interfaces/records Java existentes, com camada, responsabilidade e situação; Services e Controllers também indicam suas rotas.
- Adicionado toResponseList a TalhaoMapper e CulturaMapper; listagens dos Services agora delegam essa conversão aos Mappers.
- OpenApiConfig atualizado para reconhecer Cultura implementada. Controllers receberam Tag, Operation e respostas explícitas 201/200/400/404 com schemas de DTOs.
- Contrato planejado de Plantio alterado para entrada por IDs e saída com TalhaoResponseDTO e CulturaResponseDTO aninhados, conforme o template. Plantio continua sem implementação.
- Compilação validada em uma cópia de trabalho com Maven Wrapper offline: compile concluiu com BUILD SUCCESS. JDK disponível: 27; release configurado pelo projeto: 25.
- A tentativa de package offline compilou os 23 arquivos, mas não concluiu o empacotamento por dependências do plugin Surefire ausentes no cache. Isso não é erro de compilação; o empacotamento completo ainda precisa ser verificado com as dependências disponíveis.
- Não foram criados testes automatizados nem repetidos os testes HTTP nesta revisão. Após reiniciar a aplicação, conferir o Swagger e repetir as consultas de Talhão e Cultura.
