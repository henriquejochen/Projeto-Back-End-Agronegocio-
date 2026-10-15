// CAMADA: Service
// RESPONSABILIDADE: registra plantios e colheitas em transações.
// SITUAÇÃO: implementada.
// REGRAS: R1 área ativa, R2 irrigação, R3 previsão; colheita única com data válida.
// ROTAS ATENDIDAS: POST /plantios e PUT /plantios/{id}/colher.
package api.service;
import api.dto.*;
import api.mapper.PlantioMapper;
import api.model.*;
import api.repository.*;
import java.time.LocalDate;
import java.time.DateTimeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class PlantioService {
    private final PlantioRepository repository;
    private final TalhaoRepository talhoes;
    private final CulturaRepository culturas;
    private final PlantioMapper mapper;
    public PlantioService(PlantioRepository repository, TalhaoRepository talhoes,
                          CulturaRepository culturas, PlantioMapper mapper) {
        this.repository = repository;
        this.talhoes = talhoes;
        this.culturas = culturas;
        this.mapper = mapper;
    }
    @Transactional
    public PlantioResponseDTO criar(PlantioRequestDTO dto) {
        // O bloqueio dura até o fim da transação, incluindo o cadastro.
        Talhao talhao = talhoes.buscarParaPlantio(dto.talhaoId())
            .orElseThrow(() -> new TalhaoNaoEncontradoException(dto.talhaoId()));
        Cultura cultura = culturas.findById(dto.culturaId())
            .orElseThrow(() -> new CulturaNaoEncontradaException(dto.culturaId()));
        if (repository.somarAreaAtiva(talhao.getId()).add(dto.areaHectares())
                .compareTo(talhao.getAreaHectares()) > 0) {
            throw new RegraDeNegocioException("Área disponível do talhão insuficiente para este plantio");
        }
        if (cultura.isExigeIrrigacao() && !talhao.isIrrigado()) {
            throw new RegraDeNegocioException("A cultura exige um talhão irrigado");
        }
        LocalDate previsao;
        try {
            previsao = dto.dataPlantio().plusDays(cultura.getCicloDias());
        } catch (DateTimeException e) {
            throw new RegraDeNegocioException("Data de plantio fora do intervalo permitido para calcular a previsão");
        }
        return mapper.toResponse(repository.save(mapper.toEntity(dto, talhao, cultura, previsao)));
    }
    @Transactional
    public PlantioResponseDTO colher(Long id, ColheitaRequestDTO dto) {
        // Impede duas colheitas concorrentes do mesmo plantio.
        Plantio plantio = repository.buscarParaColher(id)
            .orElseThrow(() -> new PlantioNaoEncontradoException(id));
        if (plantio.getDataColheita() != null) {
            throw new RegraDeNegocioException("Este plantio já foi colhido");
        }
        if (dto.dataColheita().isAfter(LocalDate.now())) {
            throw new RegraDeNegocioException("Data de colheita não pode ser futura");
        }
        if (dto.dataColheita().isBefore(plantio.getDataPlantio())) {
            throw new RegraDeNegocioException("Data de colheita não pode ser anterior ao plantio");
        }
        plantio.registrarColheita(dto.dataColheita());
        // A soma de ocupação passa a ignorar este plantio; a previsão é preservada.
        return mapper.toResponse(repository.save(plantio));
    }
}
