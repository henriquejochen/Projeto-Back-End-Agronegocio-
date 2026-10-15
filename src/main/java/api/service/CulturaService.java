// CAMADA: Service
// RESPONSABILIDADE: coordena cadastro e consultas de culturas.
// REGRAS: R1–R3 pertencem ao PlantioService; esta classe não aplica essas regras.
// ROTAS ATENDIDAS: /culturas, conforme docs/contrato-da-api.md.
// SITUAÇÃO: implementada.
package api.service;

import java.util.List;

import api.dto.CulturaRequestDTO;
import api.dto.CulturaResponseDTO;
import api.mapper.CulturaMapper;
import api.repository.CulturaRepository;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CulturaService {

    private final CulturaRepository repository;
    private final CulturaMapper mapper;

    public CulturaService(
            CulturaRepository repository,
            CulturaMapper mapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public CulturaResponseDTO criar(CulturaRequestDTO dto) {
        return mapper.toResponse(
                repository.save(mapper.toEntity(dto))
        );
    }

    @Transactional(readOnly = true)
    public List<CulturaResponseDTO> listar() {
        return mapper.toResponseList(repository.findAll(Sort.by("id")));
    }

    @Transactional(readOnly = true)
    public CulturaResponseDTO buscar(Long id) {
        return mapper.toResponse(
                repository.findById(id)
                        .orElseThrow(
                                () -> new CulturaNaoEncontradaException(id)
                        )
        );
    }
}