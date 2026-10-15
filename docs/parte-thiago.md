# Parte de Thiago — Plantio

## O que foi feito

- V3 cria plantios, duas chaves estrangeiras e índices; V1 e V2 foram preservadas.
- Plantio representa os dados gravados. ManyToOne liga cada plantio a um talhão e uma cultura.
- PlantioRequestDTO recebe IDs, área positiva e data. ColheitaRequestDTO recebe a data de colheita.
- PlantioResponseDTO devolve os dados e os DTOs de talhão e cultura aninhados.
- PlantioMapper monta a entidade e converte a resposta.
- PlantioRepository soma apenas áreas sem colheita e bloqueia o registro durante a colheita.
- TalhaoRepository ganhou buscarParaPlantio, com bloqueio PESSIMISTIC_WRITE.
- PlantioService concentra as regras. Controller recebe HTTP e usa @Valid.
- PlantioNaoEncontradoException usa o tratamento 404 já existente.

## Entendendo as regras

R1: em um talhão de 10 hectares com 6 ocupados, cabem mais 4; 4,01 é recusado.
BigDecimal faz a conta sem usar ponto flutuante. O talhão fica bloqueado até o fim
da transação: outro cadastro para ele espera antes de consultar a ocupação.

R2: cultura que exige irrigação é recusada quando o talhão não é irrigado.

R3: dataPlantio.plusDays(cicloDias) calcula a previsão, gravada com o plantio.

Colheita: verifica se já foi colhido, se a data é futura ou anterior ao plantio.
Ao preencher dataColheita, o registro deixa de entrar na soma da área ativa.
Não há contador de ocupação no talhão. A previsão original não muda.
O bloqueio do plantio impede duas colheitas simultâneas.

## Como executar no Windows

A base recebida exige JDK 25 e Docker com PostgreSQL. Na raiz do projeto:

```powershell
docker compose up -d
.\mvnw.cmd test
.\mvnw.cmd spring-boot:run
```

Acesse http://localhost:8080/swagger-ui.html. Primeiro cadastre talhão e cultura.
Use os IDs reais retornados. Não é necessário instalar Maven separadamente.

## Demonstração no Swagger

1. POST /talhoes: {"nome":"Norte","areaHectares":10,"irrigado":true}.
2. POST /culturas: {"nome":"Milho","cicloDias":120,"exigeIrrigacao":false}.
3. POST /plantios: {"talhaoId":1,"culturaId":1,"areaHectares":6,"dataPlantio":"2026-10-01"} → 201; previsão 2027-01-29.
4. Repita com área 4 → 201. Repita com 0,01 → 400 por ocupação.
5. PUT /plantios/1/colher: {"dataColheita":"2026-10-11"} → 200.
6. Repita a colheita → 400. Cadastre mais 6 hectares → 201, pois a área foi liberada.
7. Teste data anterior, data futura e ID inexistente: 400, 400 e 404.
8. Cadastre talhão não irrigado e cultura que exige irrigação: plantio deve retornar 400.

Datas de colheita devem ser ajustadas caso a demonstração ocorra antes das datas do exemplo.
Também conferir duas requisições concorrentes disputando a última área disponível
no PostgreSQL: apenas uma pode cadastrar se a soma exceder a área.

## Verificação e limites

Foram adicionados oito testes unitários para as regras do Service.
Eles não substituem os testes HTTP, migration e concorrência com PostgreSQL.
Este ambiente tem apenas Java 17, enquanto o projeto exige Java 25;
o Maven também falhou ao acessar repo.maven.apache.org por erro de DNS.
A execução completa não foi confirmada aqui. Execute os comandos acima antes do commit.

README e demais documentos históricos recebidos podem mencionar Plantio pendente;
esta página e o contrato atualizado descrevem esta implementação.

## Enviar sua parte ao GitHub

A partir de um clone do repositório, crie sua branch antes de copiar os arquivos
atualizados deste ZIP para a raiz (a pasta que contém pom.xml). Não crie uma pasta
extra dentro do projeto. Depois revise o diff e execute os testes:

```powershell
git switch -c feat/plantios
git status
git diff
.\mvnw.cmd test
git add src pom.xml README.md docs
git commit -m "feat: implementar plantios e registro de colheita"
git push -u origin feat/plantios
```

O ZIP não contém histórico Git. As alterações não foram enviadas ao GitHub.
