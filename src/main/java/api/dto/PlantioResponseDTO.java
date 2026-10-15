// CAMADA: DTO
// RESPONSABILIDADE: retorna o plantio sem expor entidades JPA.
// SITUAÇÃO: implementada.
package api.dto;
import java.math.BigDecimal;
import java.time.LocalDate;
public record PlantioResponseDTO(Long id, TalhaoResponseDTO talhao, CulturaResponseDTO cultura,
    BigDecimal areaHectares, LocalDate dataPlantio, LocalDate previsaoColheita,
    LocalDate dataColheita) {}
