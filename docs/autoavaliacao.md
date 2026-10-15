# Autoavaliação

Avaliação provisória baseada no código local, na compilação desta revisão e nos testes manuais de Cultura confirmados por Henrique. A equipe deve revisar as marcas no GitHub e repetir a execução em clone limpo antes da entrega.

- ✅ atende: 0,5 ponto
- ◐ atende em parte: 0,25 ponto
- ❌ não atende: 0 ponto

| Critério | Marca | Evidência e pendência |
|---|---|---|
| P1 A API sobe seguindo o README | ◐ | API executada localmente na porta 8081; falta conferir clone limpo com JDK 25. Compilação desta revisão passou com JDK 27 e release 25. |
| P2 Cada classe no pacote da sua camada | ✅ | Classes existentes organizadas em src/main/java/api, com nomes da convenção. Exceções em service, conforme o template. |
| P3 Migrations e @Entity de todas as tabelas, e a API sobe | ◐ | V1/Talhao e V2/Cultura presentes. Faltam V3, Plantio e relacionamentos. |
| P4 DTOs de entrada e saída de todas as entidades | ◐ | Talhão e Cultura têm DTOs validados; faltam Plantio e colheita. |
| P5 Repository, Mapper, Service e Controller de todas, comentários e mapa | ◐ | Talhão e Cultura completos, cabeçalhos padronizados e mapa no README. Faltam as classes de Plantio. |
| P6 API simples: POST, GET e GET por ID, com 400 e 404 | ✅ | CulturaController, CulturaService e CulturaNaoEncontradaException; testes manuais registrados em validacao.md. |
| P7 No mínimo 3 regras de negócio implementadas | ❌ | PlantioService ainda não existe. R1–R3 só estão documentadas. |
| P8 Regras atendem ao problema e ao tema | ◐ | Regras de área, irrigação e previsão são aderentes ao agronegócio, mas ainda sem demonstração pela API. |
| P9 Código igual ao desenho | ◐ | Talhão e Cultura coerentes; desenho/contrato de Plantio ainda sem implementação. |
| P10 README completo e bem escrito | ◐ | Seções e ordem alinhadas ao template; falta revisão da entrega no GitHub e confirmação dos comandos em clone limpo. |

**Pontos que o grupo espera no projeto: 2,75 / 5,0, como estimativa técnica provisória a confirmar pela equipe.** A soma considera 2 critérios atendidos, 7 parciais e 1 não atendido; não é nota atribuída pelo professor. A apresentação vale outros 5,0 pontos.

## Pendências para concluir

1. Implementar Plantio, V3, relacionamentos, camadas, cadastro e colheita conforme o contrato.
2. Implementar R1–R3 no Service, incluindo proteção do limite de área em cadastros simultâneos e validações da colheita.
3. Executar os testes pendentes em validacao.md e repetir os cenários da API após a integração.
4. Conferir a entrega em clone limpo com JDK 25, revisar no GitHub e atualizar a estimativa e as evidências desta tabela.

Os cabeçalhos das classes, toResponseList nos Mappers, descrição do OpenApiConfig e declarações de respostas no Swagger foram alinhados ao template nesta revisão. Isso não conclui as funcionalidades pendentes de Plantio.
