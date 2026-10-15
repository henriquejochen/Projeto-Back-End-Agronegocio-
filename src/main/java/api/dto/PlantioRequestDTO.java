// CAMADA: DTO
// RESPONSABILIDADE: valida os dados de entrada do plantio.
// SITUAÇÃO: implementada.
package api.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
public record PlantioRequestDTO(
    @NotNull @Positive Long talhaoId,
    @NotNull @Positive Long culturaId,
    @NotNull @DecimalMin("0.01") @Digits(integer = 10, fraction = 2) BigDecimal areaHectares,
    @NotNull LocalDate dataPlantio
) {}
