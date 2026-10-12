package api.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// DTO: valida os dados de cadastro do talhão. Implementado.
public record TalhaoRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 100, message = "Nome deve ter até 100 caracteres")
        String nome,

        @NotNull(message = "Área é obrigatória")
        @DecimalMin(value = "0.01", message = "Área deve ser de pelo menos 0,01 hectare")
        @Digits(integer = 10, fraction = 2, message = "Área deve ter até 10 dígitos inteiros e 2 decimais")
        BigDecimal areaHectares,

        @NotNull(message = "Informe se o talhão possui irrigação")
        Boolean irrigado
) {
}
