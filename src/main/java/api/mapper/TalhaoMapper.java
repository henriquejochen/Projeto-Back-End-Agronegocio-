// CAMADA: Mapper
// RESPONSABILIDADE: converte entre entidade e DTOs de talhão.
// SITUAÇÃO: implementada.
package api.mapper;

import api.dto.TalhaoRequestDTO;
import api.dto.TalhaoResponseDTO;
import api.model.Talhao;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TalhaoMapper {
    public Talhao toEntity(TalhaoRequestDTO dto) {
        return new Talhao(dto.nome().strip(), dto.areaHectares(), dto.irrigado());
    }

    public TalhaoResponseDTO toResponse(Talhao talhao) {
        return new TalhaoResponseDTO(talhao.getId(), talhao.getNome(),
                talhao.getAreaHectares(), talhao.isIrrigado());
    }

    public List<TalhaoResponseDTO> toResponseList(List<Talhao> entidades) {
        return entidades.stream().map(this::toResponse).toList();
    }
}