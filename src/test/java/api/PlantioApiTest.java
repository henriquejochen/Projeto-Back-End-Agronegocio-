// CAMADA: Teste de integração
// RESPONSABILIDADE: verifica regras, contrato HTTP e concorrência em PostgreSQL isolado.
// SITUAÇÃO: implementada.
package api;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Supplier;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class PlantioApiTest {
    @Container
    static final PostgreSQLContainer<?> banco = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void configurarBanco(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", banco::getJdbcUrl);
        registry.add("spring.datasource.username", banco::getUsername);
        registry.add("spring.datasource.password", banco::getPassword);
    }

    @Autowired TestRestTemplate http;
    @Autowired JdbcTemplate jdbc;

    private long talhao(boolean irrigado) {
        return cadastrar("/talhoes", Map.of("nome", "Talhão teste", "areaHectares", 10, "irrigado", irrigado));
    }

    private long cultura(boolean exigeIrrigacao) {
        return cadastrar("/culturas", Map.of("nome", "Milho teste", "cicloDias", 120, "exigeIrrigacao", exigeIrrigacao));
    }

    private long cadastrar(String path, Object body) {
        var response = enviar(HttpMethod.POST, path, body);
        assertThat(response.getStatusCode().value()).isEqualTo(201);
        return response.getBody().path("id").asLong();
    }

    private Map<String, Object> pedido(long talhao, long cultura, String area, LocalDate data) {
        return Map.of("talhaoId", talhao, "culturaId", cultura, "areaHectares", new BigDecimal(area), "dataPlantio", data.toString());
    }

    private ResponseEntity<JsonNode> enviar(HttpMethod method, String path, Object body) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return http.exchange(path, method, new HttpEntity<>(body, headers), JsonNode.class);
    }

    private void erro(ResponseEntity<JsonNode> response, int status, String mensagem) {
        assertThat(response.getStatusCode().value()).isEqualTo(status);
        assertThat(response.getBody().path("status").asInt()).isEqualTo(status);
        assertThat(response.getBody().path("mensagem").asText()).contains(mensagem);
        assertThat(response.getBody().path("campos").isArray()).isTrue();
    }

    @Test
    void criaConsultaCalculaPrevisaoEIgnoraCamposControladosPelaApi() {
        long t = talhao(true), c = cultura(true);
        String body = """
                {"talhaoId":%d,"culturaId":%d,"areaHectares":4.50,"dataPlantio":"2026-10-01",
                 "previsaoColheita":"2000-01-01","dataColheita":"2026-10-02"}
                """.formatted(t, c);
        var response = enviar(HttpMethod.POST, "/plantios", body);
        assertThat(response.getStatusCode().value()).isEqualTo(201);
        JsonNode plantio = response.getBody();
        long id = plantio.path("id").asLong();
        assertThat(response.getHeaders().getLocation().toString()).isEqualTo("/plantios/" + id);
        assertThat(plantio.path("talhao").path("id").asLong()).isEqualTo(t);
        assertThat(plantio.path("cultura").path("id").asLong()).isEqualTo(c);
        assertThat(plantio.path("previsaoColheita").asText()).isEqualTo("2027-01-29");
        assertThat(plantio.path("dataColheita").isNull()).isTrue();
        assertThat(http.getForEntity("/plantios/" + id, JsonNode.class).getBody()).isEqualTo(plantio);
        var list = http.getForEntity("/plantios", JsonNode.class);
        assertThat(list.getStatusCode().value()).isEqualTo(200);
        assertThat(list.getBody()).anyMatch(item -> item.path("id").asLong() == id);
        assertThat(jdbc.queryForObject("select count(*) from flyway_schema_history where success", Integer.class)).isEqualTo(3);
    }

    @Test
    void permiteOcuparExatamenteALimiteEBloqueiaExcessoSemSalvar() {
        long t = talhao(false), c = cultura(false);
        LocalDate data = LocalDate.now().plusDays(10);
        cadastrar("/plantios", pedido(t, c, "4.50", data));
        cadastrar("/plantios", pedido(t, c, "5.50", data));
        erro(enviar(HttpMethod.POST, "/plantios", pedido(t, c, "0.01", data)), 400, "R1");
        assertThat(jdbc.queryForObject("select count(*) from plantios where talhao_id = ?", Integer.class, t)).isEqualTo(2);
    }

    @Test
    void bloqueiaIrrigacaoIncompativelSemOcuparArea() {
        long t = talhao(false), c = cultura(true);
        erro(enviar(HttpMethod.POST, "/plantios", pedido(t, c, "2", LocalDate.now())), 400, "R2");
        assertThat(jdbc.queryForObject("select count(*) from plantios where talhao_id = ?", Integer.class, t)).isZero();
    }

    @Test
    void referenciasAusentesEParametrosInvalidosRetornamErrosPadronizados() {
        long t = talhao(true), c = cultura(false);
        erro(enviar(HttpMethod.POST, "/plantios", pedido(Long.MAX_VALUE, c, "1", LocalDate.now())), 404, "Talhão não encontrado");
        erro(enviar(HttpMethod.POST, "/plantios", pedido(t, Long.MAX_VALUE, "1", LocalDate.now())), 404, "Cultura não encontrada");
        erro(http.getForEntity("/plantios/" + Long.MAX_VALUE, JsonNode.class), 404, "Plantio não encontrado");
        erro(http.getForEntity("/plantios/abc", JsonNode.class), 400, "Parâmetro inválido");
        erro(enviar(HttpMethod.POST, "/plantios", pedido(t, c, "0", LocalDate.now())), 400, "Dados inválidos");
        erro(enviar(HttpMethod.POST, "/plantios", pedido(t, c, "1.001", LocalDate.now())), 400, "Dados inválidos");
        erro(enviar(HttpMethod.POST, "/plantios", "{}"), 400, "Dados inválidos");
        erro(enviar(HttpMethod.POST, "/plantios", "{quebrado"), 400, "Corpo da requisição inválido");
        erro(enviar(HttpMethod.PUT, "/plantios/" + Long.MAX_VALUE + "/colher", Map.of("dataColheita", "2020-01-01")), 404, "Plantio não encontrado");
    }

    @Test
    void colheitaValidaLiberaAreaEPreservaPrevisao() {
        long t = talhao(false), c = cultura(false);
        LocalDate hoje = LocalDate.now(ZoneId.of("America/Sao_Paulo"));
        LocalDate data = hoje.minusDays(20);
        long id = cadastrar("/plantios", pedido(t, c, "10", data));
        var response = enviar(HttpMethod.PUT, "/plantios/" + id + "/colher", Map.of("dataColheita", hoje.toString()));
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody().path("previsaoColheita").asText()).isEqualTo(data.plusDays(120).toString());
        assertThat(response.getBody().path("dataColheita").asText()).isEqualTo(hoje.toString());
        cadastrar("/plantios", pedido(t, c, "10", hoje));
        erro(enviar(HttpMethod.PUT, "/plantios/" + id + "/colher", Map.of("dataColheita", hoje.toString())), 400, "Colheita já registrada");
    }

    @Test
    void rejeitaDatasDeColheitaInvalidasSemLiberarArea() {
        long t = talhao(false), c = cultura(false);
        LocalDate hoje = LocalDate.now(ZoneId.of("America/Sao_Paulo"));
        long id = cadastrar("/plantios", pedido(t, c, "10", hoje));
        String path = "/plantios/" + id + "/colher";
        erro(enviar(HttpMethod.PUT, path, Map.of("dataColheita", hoje.minusDays(1).toString())), 400, "anterior");
        erro(enviar(HttpMethod.PUT, path, Map.of("dataColheita", hoje.plusDays(1).toString())), 400, "futura");
        erro(enviar(HttpMethod.PUT, path, "{}"), 400, "Dados inválidos");
        erro(enviar(HttpMethod.POST, "/plantios", pedido(t, c, "1", hoje)), 400, "R1");
        assertThat(http.getForEntity("/plantios/" + id, JsonNode.class).getBody().path("dataColheita").isNull()).isTrue();
    }

    private List<Integer> simultaneos(Supplier<ResponseEntity<JsonNode>> acao) throws Exception {
        var prontos = new CountDownLatch(2);
        var iniciar = new CountDownLatch(1);
        try (var executor = Executors.newFixedThreadPool(2)) {
            Callable<Integer> chamada = () -> {
                prontos.countDown();
                assertThat(iniciar.await(10, TimeUnit.SECONDS)).isTrue();
                return acao.get().getStatusCode().value();
            };
            var primeiro = executor.submit(chamada);
            var segundo = executor.submit(chamada);
            assertThat(prontos.await(10, TimeUnit.SECONDS)).isTrue();
            iniciar.countDown();
            return List.of(primeiro.get(30, TimeUnit.SECONDS), segundo.get(30, TimeUnit.SECONDS));
        }
    }

    @Test
    void cadastrosSimultaneosNaoUltrapassamCapacidade() throws Exception {
        long t = talhao(true), c = cultura(false);
        var body = pedido(t, c, "6", LocalDate.now());
        assertThat(simultaneos(() -> enviar(HttpMethod.POST, "/plantios", body))).containsExactlyInAnyOrder(201, 400);
        assertThat(jdbc.queryForObject("select sum(area_hectares) from plantios where talhao_id = ? and data_colheita is null", BigDecimal.class, t)).isEqualByComparingTo("6");
    }

    @Test
    void colheitasSimultaneasNaoFinalizamDuasVezes() throws Exception {
        long t = talhao(true), c = cultura(false);
        LocalDate hoje = LocalDate.now(ZoneId.of("America/Sao_Paulo"));
        long id = cadastrar("/plantios", pedido(t, c, "10", hoje));
        var body = Map.of("dataColheita", hoje.toString());
        assertThat(simultaneos(() -> enviar(HttpMethod.PUT, "/plantios/" + id + "/colher", body))).containsExactlyInAnyOrder(200, 400);
    }

    @Test
    void swaggerDescreveStatusEObjetosDaResposta() {
        JsonNode spec = http.getForObject("/v3/api-docs", JsonNode.class);
        assertThat(spec.path("paths").path("/plantios").path("post").path("responses").has("201")).isTrue();
        assertThat(spec.path("paths").path("/plantios/{id}/colher").path("put").path("responses").has("400")).isTrue();
        assertThat(spec.path("components").path("schemas").path("PlantioResponseDTO").path("properties").path("talhao").path("$ref").asText()).endsWith("TalhaoResponseDTO");
    }
}
