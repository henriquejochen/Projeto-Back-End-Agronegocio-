# Modelo de domínio

Responsável por completar: Henrique Jochen, alinhando os relacionamentos com Thiago.
Este é o acordo inicial; somente talhoes está implementada. Henrique deve acrescentar o diagrama ER e conferir os tipos das três tabelas.

| Entidade | Campos combinados |
|---|---|
| Talhão | id: BIGINT; nome: VARCHAR(100); area_hectares: NUMERIC(12,2); irrigado: BOOLEAN |
| Cultura | id: BIGINT; nome: VARCHAR(100); ciclo_dias: INTEGER; exige_irrigacao: BOOLEAN |
| Plantio | id: BIGINT; talhao_id: BIGINT; cultura_id: BIGINT; area_hectares: NUMERIC(12,2); data_plantio: DATE; previsao_colheita: DATE; data_colheita: DATE |

Todos são obrigatórios, exceto data_colheita. IDs gerados pelo banco. Áreas usam BigDecimal; datas, LocalDate.

Um talhão tem muitos plantios; uma cultura tem muitos plantios. As duas chaves estrangeiras ficam em plantios. Não usar exclusão em cascata.

Plantio ativo tem data_colheita nula. A ocupação soma apenas esses plantios. A previsão é salva no cadastro e preservada ao registrar a colheita real.

Migrations: V1__criar_talhoes.sql (Kauã, implementada), V2__criar_culturas.sql (Henrique, pendente), V3__criar_plantios.sql (Thiago, pendente).
