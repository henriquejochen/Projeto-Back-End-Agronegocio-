// CAMADA: Mapper
// RESPONSABILIDADE: converte entre entidade e DTOs de cultura.
// SITUAÇÃO: implementada.
package api.mapper;

import api.dto.CulturaRequestDTO;
import api.dto.CulturaResponseDTO;
import api.model.Cultura;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CulturaMapper {

    public Cultura toEntity(CulturaRequestDTO dto) {
        return new Cultura(
                dto.nome().strip(),
                dto.cicloDias(),
                dto.exigeIrrigacao()
        );
    }

    public CulturaResponseDTO toResponse(Cultura cultura) {
        return new CulturaResponseDTO(
                cultura.getId(),
                cultura.getNome(),
                cultura.getCicloDias(),
                cultura.isExigeIrrigacao()
        );
    }

    public List<CulturaResponseDTO> toResponseList(List<Cultura> entidades) {
        return entidades.stream().map(this::toResponse).toList();
    }
}