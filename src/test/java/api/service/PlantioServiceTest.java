// CAMADA: Teste
// RESPONSABILIDADE: verifica regras de área, irrigação, previsão e colheita.
// SITUAÇÃO: implementada; execução depende do ambiente Maven/JDK.
package api.service;
import api.dto.*;
import api.mapper.*;
import api.model.*;
import api.repository.*;
import org.junit.jupiter.api.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class PlantioServiceTest {
    PlantioRepository repo;
    TalhaoRepository talhoes;
    CulturaRepository culturas;
    PlantioService service;
    LocalDate data = LocalDate.now().minusDays(10);
    @BeforeEach void preparar() {
        repo = mock(PlantioRepository.class);
        talhoes = mock(TalhaoRepository.class);
        culturas = mock(CulturaRepository.class);
        service = new PlantioService(repo, talhoes, culturas,
            new PlantioMapper(new TalhaoMapper(), new CulturaMapper()));
        when(talhoes.buscarParaPlantio(1L)).thenReturn(Optional.of(new Talhao("Norte", new BigDecimal("10"), true)));
        when(culturas.findById(1L)).thenReturn(Optional.of(new Cultura("Milho", 120, false)));
        when(repo.somarAreaAtiva(any())).thenReturn(new BigDecimal("6"));
        when(repo.save(any(Plantio.class))).thenAnswer(i -> i.getArgument(0));
    }
    PlantioRequestDTO pedido(String area) { return new PlantioRequestDTO(1L, 1L, new BigDecimal(area), data); }
    @Test void aceitaLimiteExatoECalculaPrevisao() {
        var resposta = service.criar(pedido("4"));
        assertEquals(data.plusDays(120), resposta.previsaoColheita());
        assertNull(resposta.dataColheita());
        assertEquals("Norte", resposta.talhao().nome());
    }
    @Test void rejeitaExcessoDeArea() {
        assertThrows(RegraDeNegocioException.class, () -> service.criar(pedido("4.01")));
        verify(repo, never()).save(any());
    }
    @Test void rejeitaFaltaDeIrrigacao() {
        when(talhoes.buscarParaPlantio(1L)).thenReturn(Optional.of(new Talhao("Seco", BigDecimal.TEN, false)));
        when(culturas.findById(1L)).thenReturn(Optional.of(new Cultura("Arroz", 120, true)));
        assertThrows(RegraDeNegocioException.class, () -> service.criar(pedido("1")));
    }
    @Test void rejeitaReferenciasInexistentes() {
        when(talhoes.buscarParaPlantio(1L)).thenReturn(Optional.empty());
        assertThrows(TalhaoNaoEncontradoException.class, () -> service.criar(pedido("1")));
    }
    @Test void rejeitaCulturaInexistente() {
        when(culturas.findById(1L)).thenReturn(Optional.empty());
        assertThrows(CulturaNaoEncontradaException.class, () -> service.criar(pedido("1")));
    }
    Plantio plantio() {
        Plantio p = new Plantio(new Talhao("Norte", BigDecimal.TEN, true),
            new Cultura("Milho", 120, false), BigDecimal.ONE, data, data.plusDays(120));
        when(repo.buscarParaColher(1L)).thenReturn(Optional.of(p));
        return p;
    }
    @Test void colherPreservaPrevisaoEImpedeRepeticao() {
        var p = plantio();
        var resposta = service.colher(1L, new ColheitaRequestDTO(data));
        assertEquals(data, resposta.dataColheita());
        assertEquals(p.getPrevisaoColheita(), resposta.previsaoColheita());
        assertThrows(RegraDeNegocioException.class, () -> service.colher(1L, new ColheitaRequestDTO(data)));
    }
    @Test void rejeitaDatasInvalidas() {
        plantio();
        assertThrows(RegraDeNegocioException.class, () -> service.colher(1L, new ColheitaRequestDTO(data.minusDays(1))));
        assertThrows(RegraDeNegocioException.class, () -> service.colher(1L, new ColheitaRequestDTO(LocalDate.now().plusDays(1))));
    }
    @Test void rejeitaPlantioInexistente() {
        when(repo.buscarParaColher(999L)).thenReturn(Optional.empty());
        assertThrows(PlantioNaoEncontradoException.class, () -> service.colher(999L, new ColheitaRequestDTO(data)));
    }
}
