// CAMADA: DTO
// RESPONSABILIDADE: valida os dados de cadastro da cultura.
// SITUAÇÃO: implementada.
package api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CulturaRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 100, message = "Nome deve ter até 100 caracteres")
        String nome,

        @NotNull(message = "Ciclo é obrigatório")
        @Min(value = 1, message = "Ciclo deve ser de pelo menos 1 dia")
        @Max(value = 3650, message = "Ciclo deve ser de até 3650 dias")
        Integer cicloDias,

        @NotNull(message = "Informe se a cultura exige irrigação")
        Boolean exigeIrrigacao
) {
}