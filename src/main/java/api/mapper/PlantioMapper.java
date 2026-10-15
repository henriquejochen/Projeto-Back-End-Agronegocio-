// CAMADA: Mapper
// RESPONSABILIDADE: converte Plantio e seus relacionamentos em DTOs.
// SITUAÇÃO: implementada.
package api.mapper;
import api.dto.*;
import api.model.*;
import java.time.LocalDate;
import org.springframework.stereotype.Component;
@Component
public class PlantioMapper {
    private final TalhaoMapper talhaoMapper;
    private final CulturaMapper culturaMapper;
    public PlantioMapper(TalhaoMapper talhaoMapper, CulturaMapper culturaMapper) {
        this.talhaoMapper = talhaoMapper;
        this.culturaMapper = culturaMapper;
    }
    public Plantio toEntity(PlantioRequestDTO dto, Talhao talhao, Cultura cultura, LocalDate previsao) {
        return new Plantio(talhao, cultura, dto.areaHectares(), dto.dataPlantio(), previsao);
    }
    public PlantioResponseDTO toResponse(Plantio p) {
        return new PlantioResponseDTO(p.getId(), talhaoMapper.toResponse(p.getTalhao()),
            culturaMapper.toResponse(p.getCultura()), p.getAreaHectares(), p.getDataPlantio(),
            p.getPrevisaoColheita(), p.getDataColheita());
    }
}
