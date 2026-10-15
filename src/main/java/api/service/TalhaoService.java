// CAMADA: Service
// RESPONSABILIDADE: coordena cadastro e consultas de talhões; atende POST e GET /talhoes.
// REGRAS: não implementa R1–R3; regras de Plantio ainda pendentes.
// ROTAS ATENDIDAS: /talhoes, conforme docs/contrato-da-api.md.
// SITUAÇÃO: implementada.
package api.service;

import java.util.List;
import api.dto.TalhaoRequestDTO;
import api.dto.TalhaoResponseDTO;
import api.mapper.TalhaoMapper;
import api.repository.TalhaoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TalhaoService {
    private final TalhaoRepository repository;
    private final TalhaoMapper mapper;

    public TalhaoService(TalhaoRepository repository, TalhaoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public TalhaoResponseDTO criar(TalhaoRequestDTO dto) {
        return mapper.toResponse(repository.save(mapper.toEntity(dto)));
    }

    @Transactional(readOnly = true)
    public List<TalhaoResponseDTO> listar() {
        return mapper.toResponseList(repository.findAll(Sort.by("id")));
    }

    @Transactional(readOnly = true)
    public TalhaoResponseDTO buscar(Long id) {
        return mapper.toResponse(repository.findById(id)
                .orElseThrow(() -> new TalhaoNaoEncontradoException(id)));
    }
}
