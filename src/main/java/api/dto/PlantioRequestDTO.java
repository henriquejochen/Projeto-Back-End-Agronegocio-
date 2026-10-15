// CAMADA: DTO
// RESPONSABILIDADE: valida a entrada do cadastro de plantio, com referências por ID.
// SITUAÇÃO: implementada.
package api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.*;

public record PlantioRequestDTO(
        @NotNull(message = "Talhão é obrigatório")
        @Positive(message = "ID do talhão deve ser positivo") Long talhaoId,
        @NotNull(message = "Cultura é obrigatória")
        @Positive(message = "ID da cultura deve ser positivo") Long culturaId,
        @NotNull(message = "Área é obrigatória")
        @DecimalMin(value = "0.01", message = "Área deve ser de pelo menos 0,01 hectare")
        @Digits(integer = 10, fraction = 2, message = "Área deve ter até 10 dígitos inteiros e 2 decimais")
        BigDecimal areaHectares,
        @NotNull(message = "Data de plantio é obrigatória") LocalDate dataPlantio
) { }
