# Autoavaliação

Avaliação provisória do estado local, baseada nos arquivos, nos testes manuais de Cultura e na suíte de integração com PostgreSQL. Revisar a cópia publicada no GitHub e executar com JDK 25 antes da entrega.

- ✅ atende: 0,5 ponto
- ◐ atende em parte: 0,25 ponto
- ❌ não atende: 0 ponto

| Critério | Marca | Evidência e pendência |
|---|---|---|
| P1 A API sobe seguindo o README | ◐ | Aplicação iniciou nos testes com PostgreSQL novo e V1–V3; empacotamento passou. Falta conferir clone limpo com JDK 25; validação realizada com JDK 27 e release 25. |
| P2 Cada classe no pacote da sua camada | ✅ | Três entidades organizadas em controller, service, mapper, repository, model e dto, com exceções em service. |
| P3 Migrations e @Entity de todas as tabelas | ✅ | V1/Talhao, V2/Cultura e V3/Plantio; duas FKs, schema validado pelo Hibernate e migrations aplicadas no PostgreSQL dos testes. |
| P4 DTOs de entrada e saída de todas as entidades | ✅ | DTOs das três entidades, ColheitaRequestDTO e respostas com DTOs aninhados; nenhuma entidade exposta no Controller. |
| P5 Estrutura completa e documentada | ✅ | Repositories, Mappers, Services e Controllers com injeção pelo construtor e comentários; mapa no README. |
| P6 API simples com 400 e 404 | ✅ | Testes manuais de Cultura e suíte HTTP de Plantio: POST, GET, GET por ID e erros padronizados. |
| P7 No mínimo 3 regras no Service | ✅ | PlantioService.criar aplica R1, R2 e R3; testes conferem rejeição por área/irrigação e previsão automática. |
| P8 Regras aderentes ao tema | ✅ | Ocupação de talhão, compatibilidade de irrigação, previsão e liberação de área após colheita. |
| P9 Código igual ao desenho | ✅ | Três entidades e relacionamentos documentados; contrato atualizado com consultas e colheita; JSON e Swagger verificados nos testes. |
| P10 README completo e bem escrito | ◐ | Seções, links, regras e execução atualizados; falta revisar a versão final publicada no GitHub. |

**Pontos que o grupo espera no projeto: 4,50 / 5,0, estimativa técnica provisória a confirmar pela equipe.** São 8 critérios atendidos e 2 parciais; não é nota atribuída pelo professor. A apresentação vale outros 5,0 pontos.

## Antes de entregar

1. Revisar a implementação com os três integrantes, incluindo bloqueios e regras no PlantioService.
2. Reiniciar a aplicação na pasta agronegocio-git para aplicar V3 no banco local e conferir o Swagger.
3. Executar o roteiro do README em clone limpo com JDK 25 e verificar o resultado de mvnw verify.
4. Fazer commit e enviar as alterações à main; conferir README, diagrama e autoavaliação no GitHub.
5. Preparar a demonstração de cadastro, consultas, 400/404, R1/R2 e previsão calculada.
