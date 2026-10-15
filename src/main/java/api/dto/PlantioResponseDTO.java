// CAMADA: DTO
// RESPONSABILIDADE: devolve plantio com dados de talhão e cultura sem expor entidades JPA.
// SITUAÇÃO: implementada.
package api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PlantioResponseDTO(Long id, TalhaoResponseDTO talhao, CulturaResponseDTO cultura,
        BigDecimal areaHectares, LocalDate dataPlantio, LocalDate previsaoColheita,
        LocalDate dataColheita) { }
