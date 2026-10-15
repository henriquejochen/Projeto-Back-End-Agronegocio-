// CAMADA: DTO
// RESPONSABILIDADE: recebe a data real de colheita; regras temporais são conferidas no Service.
// SITUAÇÃO: implementada.
package api.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;

public record ColheitaRequestDTO(
        @NotNull(message = "Data de colheita é obrigatória") LocalDate dataColheita
) { }
