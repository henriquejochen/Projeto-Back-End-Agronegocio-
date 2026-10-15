// CAMADA: DTO
// RESPONSABILIDADE: valida a data informada para colheita.
// SITUAÇÃO: implementada.
package api.dto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;
public record ColheitaRequestDTO(@NotNull @PastOrPresent LocalDate dataColheita) {}
