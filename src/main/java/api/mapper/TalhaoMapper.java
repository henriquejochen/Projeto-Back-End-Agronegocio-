package api.mapper;

import api.dto.TalhaoRequestDTO;
import api.dto.TalhaoResponseDTO;
import api.model.Talhao;
import org.springframework.stereotype.Component;

// Mapper: converte entre entidade e DTOs de talhão. Implementado.
@Component
public class TalhaoMapper {
    public Talhao toEntity(TalhaoRequestDTO dto) {
        return new Talhao(dto.nome().strip(), dto.areaHectares(), dto.irrigado());
    }

    public TalhaoResponseDTO toResponse(Talhao talhao) {
        return new TalhaoResponseDTO(talhao.getId(), talhao.getNome(),
                talhao.getAreaHectares(), talhao.isIrrigado());
    }
}
