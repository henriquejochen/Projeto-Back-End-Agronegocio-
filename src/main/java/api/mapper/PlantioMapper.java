// CAMADA: Mapper
// RESPONSABILIDADE: converte plantios e DTOs usando referências previamente buscadas pelo Service.
// SITUAÇÃO: implementada.
package api.mapper;

import java.time.LocalDate;
import java.util.List;
import api.dto.PlantioRequestDTO;
import api.dto.PlantioResponseDTO;
import api.model.Cultura;
import api.model.Plantio;
import api.model.Talhao;
import org.springframework.stereotype.Component;

@Component
public class PlantioMapper {
    private final TalhaoMapper talhaoMapper;
    private final CulturaMapper culturaMapper;

    public PlantioMapper(TalhaoMapper talhaoMapper, CulturaMapper culturaMapper) {
        this.talhaoMapper = talhaoMapper;
        this.culturaMapper = culturaMapper;
    }

    public Plantio toEntity(PlantioRequestDTO dto, Talhao talhao, Cultura cultura,
                           LocalDate previsaoColheita) {
        return new Plantio(talhao, cultura, dto.areaHectares(), dto.dataPlantio(), previsaoColheita);
    }

    public PlantioResponseDTO toResponse(Plantio plantio) {
        return new PlantioResponseDTO(plantio.getId(), talhaoMapper.toResponse(plantio.getTalhao()),
                culturaMapper.toResponse(plantio.getCultura()), plantio.getAreaHectares(),
                plantio.getDataPlantio(), plantio.getPrevisaoColheita(), plantio.getDataColheita());
    }

    public List<PlantioResponseDTO> toResponseList(List<Plantio> plantios) {
        return plantios.stream().map(this::toResponse).toList();
    }
}
