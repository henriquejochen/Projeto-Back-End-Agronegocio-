> Atualização de Thiago: Plantio e colheita foram implementados nesta cópia.
> Veja [parte-thiago.md](parte-thiago.md) para os arquivos, regras e verificação pendente.
> Os registros abaixo foram preservados como histórico da base recebida.

# Divisão da equipe

Cada integrante implementa, testa e commita a própria parte. A base atual contém Talhão e Cultura implementados. Os testes manuais de Cultura foram confirmados por Henrique; Plantio e suas regras ainda estão pendentes. As listas abaixo mantêm as responsabilidades acordadas, sem afirmar que commits ou integrações já foram realizados.

## Kauã Ghizzo — preparação e Talhão

- Aproveitar a infraestrutura já colocada no repositório por Henrique.
- Organizar configurações, documentação e comentários curtos.
- Entregar V1, Talhao, Repository, DTOs, Mapper, Service, Controller e exceção de não encontrado.
- Implementar POST, GET e GET por id, validações e erros 400/404.
- Manter README, mapa das classes e contrato de Talhão.
- Conferir execução em clone limpo após as integrações.

Commits sugeridos:
1. docs: definir escopo de plantios e dividir tarefas da equipe
2. chore: organizar configuracao e comentarios da base
3. feat: implementar cadastro e consultas de talhoes

## Henrique Jochen — Cultura e modelo de domínio

Situação atual: V2, entidade, camadas, exceção 404 e contrato de Cultura implementados; testes manuais registrados em validacao.md. O modelo de domínio agora inclui tipos, obrigatoriedade e diagrama ER, distinguindo Plantio planejado das tabelas existentes. Falta revisar os relacionamentos com Thiago durante a implementação e confirmar os testes complementares e a integração no Git.

- Criar V2__criar_culturas.sql e Cultura conforme os campos combinados.
- Criar CulturaRepository, CulturaRequestDTO, CulturaResponseDTO, CulturaMapper, CulturaService, CulturaController e CulturaNaoEncontradaException.
- Implementar POST, GET e GET por id de /culturas, com validações e 404.
- Completar modelo-de-dominio.md: tipos, obrigatoriedade, chaves estrangeiras e diagrama ER das três tabelas; conferir os relacionamentos com Thiago.
- Completar exemplos das rotas de Cultura no contrato e testar 201, 200, 400 e 404.

Commits sugeridos:
1. feat: adicionar migration e estrutura de culturas
2. feat: implementar cadastro e consultas de culturas
3. docs: completar modelo de dominio e contrato de culturas

## Thiago Devilla — Plantio e regras

- Criar V3__criar_plantios.sql com duas chaves estrangeiras e Plantio com relacionamentos JPA.
- Criar Repository, DTOs de plantio e colheita, Mapper, Service, Controller e PlantioNaoEncontradoException.
- Implementar POST /plantios com R1, R2 e R3 no Service. Referências inexistentes retornam 404.
- Implementar PUT /plantios/{id}/colher. Impedir repetição, data futura e data anterior ao plantio.
- Calcular ocupação somando os plantios sem colheita; não manter outro contador no talhão.
- Fazer verificação da área e cadastro na mesma transação. Bloquear o talhão durante a operação (por exemplo, PESSIMISTIC_WRITE) para cadastros simultâneos não ultrapassarem sua área; coordenar com Kauã o método adicional em TalhaoRepository.
- Completar exemplos de plantio/colheita no contrato e demonstrar as regras.

Commits sugeridos:
1. feat: adicionar estrutura e relacionamentos de plantios
2. feat: validar area e irrigacao e calcular previsao de colheita
3. feat: registrar colheita e liberar ocupacao do talhao
4. docs: documentar e verificar regras de plantio

## Integração

1. Talhão e Cultura estão disponíveis nesta cópia local. Thiago desenvolve Plantio usando o contrato combinado; a equipe confere a integração no repositório.
2. Reservar V1 para Talhão, V2 para Cultura e V3 para Plantio. Integrar V2 antes da V3. Não alterar migrations já aplicadas.
3. Cada pessoa parte da main atualizada e usa sua branch: feat/talhoes, feat/culturas ou feat/plantios.
4. Configurar o próprio nome/e-mail no Git. Revisar, testar, commitar e enviar a própria branch. Preservar a autoria na integração.
5. Cada um atualiza o status de sua parte no README e suas rotas no contrato. Alterações de campos exigem alinhar modelo, código e contrato.
6. Os três preenchem a autoavaliação depois dos testes e garantem a main pronta antes da Aula 11.

Não é necessário ter a mesma quantidade de commits. Eles devem representar entregas reais e compreensíveis. Toda classe deve ter o cabeçalho do template: CAMADA, RESPONSABILIDADE e SITUAÇÃO. Services também indicam REGRAS; Controllers e Services indicam ROTAS ATENDIDAS. O DTO de entrada de Plantio recebe IDs; a resposta usa TalhaoResponseDTO e CulturaResponseDTO aninhados.

## Apresentação

- Kauã: execução, camadas e API simples, com 400 e 404 — 3 minutos.
- Henrique: problema, Cultura e diagrama — 3 minutos.
- Thiago: regras no Service e demonstração de plantio/colheita — 4 minutos.

Todos revisam o projeto completo para responder às perguntas do professor.
