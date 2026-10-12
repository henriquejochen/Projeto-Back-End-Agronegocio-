# Validação

A implementação de Talhão inclui cadastro, listagem, busca por id, DTOs validados e tratamento padronizado de erros.

A execução completa com PostgreSQL e JDK 25 ainda precisa ser verificada: o ambiente disponível tem Java 21 e Docker sem daemon em execução.

## Conferência antes do envio

O Maven offline não concluiu o build: faltam dependências do plugin de compilação no cache. A tentativa alternativa com javac/Java 21 encontrou erro de acesso do ambiente e não serve como comprovação de build. Não há teste de API ou banco confirmado nesta entrega; execute o roteiro abaixo com JDK 25 e Docker antes de enviar.

1. Subir o banco e a API seguindo o README em um clone limpo.
2. Cadastrar um talhão com área 10,50 e conferir 201 e Location.
3. Listar e buscar o id criado; conferir 200 e dados persistidos.
4. Enviar nome vazio, área zero, três casas decimais ou irrigado ausente; conferir 400.
5. Buscar id inexistente e id não numérico; conferir 404 e 400 no formato ErroDTO.
6. Reiniciar a API e confirmar que o talhão permanece cadastrado.
7. Após integrar: ocupar exatamente a área do talhão (permitido), tentar exceder (400), colher e cadastrar novamente (permitido).
8. Tentar cultura que exige irrigação em talhão não irrigado (400).
9. Conferir a previsão: 2026-10-01 + 120 dias = 2027-01-29.
10. Tentar segunda colheita e data anterior ao plantio (400).

## Verificação posterior

Kauã confirmou que conseguiu executar o projeto no IntelliJ seguindo o roteiro. Essa confirmação não substitui a conferência final de todos os cenários após integrar Cultura e Plantio.

