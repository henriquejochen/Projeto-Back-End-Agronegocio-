// CAMADA: Service
// RESPONSABILIDADE: controla ocupação, irrigação, previsão e registro de colheitas.
// REGRAS: R1 (capacidade do talhão), R2 (irrigação), R3 (previsão); colheita única e com data válida.
// ROTAS ATENDIDAS: POST/GET /plantios, GET /plantios/{id}, PUT /plantios/{id}/colher.
// SITUAÇÃO: implementada.
package api.service;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import api.dto.ColheitaRequestDTO;
import api.dto.PlantioRequestDTO;
import api.dto.PlantioResponseDTO;
import api.mapper.PlantioMapper;
import api.model.Cultura;
import api.model.Plantio;
import api.model.Talhao;
import api.repository.CulturaRepository;
import api.repository.PlantioRepository;
import api.repository.TalhaoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlantioService {
    private final PlantioRepository repository;
    private final TalhaoRepository talhaoRepository;
    private final CulturaRepository culturaRepository;
    private final PlantioMapper mapper;

    public PlantioService(PlantioRepository repository, TalhaoRepository talhaoRepository,
                         CulturaRepository culturaRepository, PlantioMapper mapper) {
        this.repository = repository;
        this.talhaoRepository = talhaoRepository;
        this.culturaRepository = culturaRepository;
        this.mapper = mapper;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public PlantioResponseDTO criar(PlantioRequestDTO dto) {
        // O bloqueio dura até o commit. A soma posterior inclui os cadastros já confirmados.
        Talhao talhao = talhaoRepository.buscarParaAtualizar(dto.talhaoId())
                .orElseThrow(() -> new TalhaoNaoEncontradoException(dto.talhaoId()));
        Cultura cultura = culturaRepository.findById(dto.culturaId())
                .orElseThrow(() -> new CulturaNaoEncontradaException(dto.culturaId()));

        BigDecimal ocupada = repository.somarAreaAtiva(talhao.getId());
        if (ocupada.add(dto.areaHectares()).compareTo(talhao.getAreaHectares()) > 0) {
            throw new RegraDeNegocioException("R1: a área dos plantios ativos não pode ultrapassar a área do talhão");
        }
        if (cultura.isExigeIrrigacao() && !talhao.isIrrigado()) {
            throw new RegraDeNegocioException("R2: cultura que exige irrigação precisa de um talhão irrigado");
        }

        LocalDate previsao;
        try {
            previsao = dto.dataPlantio().plusDays(cultura.getCicloDias());
        } catch (DateTimeException e) {
            throw new RegraDeNegocioException("R3: data de plantio fora do intervalo permitido para calcular a previsão");
        }
        return mapper.toResponse(repository.save(mapper.toEntity(dto, talhao, cultura, previsao)));
    }

    @Transactional(readOnly = true)
    public List<PlantioResponseDTO> listar() {
        return mapper.toResponseList(repository.findAll(Sort.by("id")));
    }

    @Transactional(readOnly = true)
    public PlantioResponseDTO buscar(Long id) {
        return mapper.toResponse(repository.findById(id)
                .orElseThrow(() -> new PlantioNaoEncontradoException(id)));
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public PlantioResponseDTO colher(Long id, ColheitaRequestDTO dto) {
        // Sempre bloqueia talhão antes de plantio; a projeção evita carregar estado antigo da entidade.
        Long talhaoId = repository.buscarTalhaoId(id)
                .orElseThrow(() -> new PlantioNaoEncontradoException(id));
        talhaoRepository.buscarParaAtualizar(talhaoId)
                .orElseThrow(() -> new TalhaoNaoEncontradoException(talhaoId));
        Plantio plantio = repository.buscarParaAtualizar(id)
                .orElseThrow(() -> new PlantioNaoEncontradoException(id));
        if (plantio.getDataColheita() != null) {
            throw new RegraDeNegocioException("Colheita já registrada para este plantio");
        }
        if (dto.dataColheita().isBefore(plantio.getDataPlantio())) {
            throw new RegraDeNegocioException("A data de colheita não pode ser anterior à data de plantio");
        }
        if (dto.dataColheita().isAfter(LocalDate.now(ZoneId.of("America/Sao_Paulo")))) {
            throw new RegraDeNegocioException("A data de colheita não pode ser futura");
        }
        plantio.registrarColheita(dto.dataColheita());
        return mapper.toResponse(repository.save(plantio));
    }
}
