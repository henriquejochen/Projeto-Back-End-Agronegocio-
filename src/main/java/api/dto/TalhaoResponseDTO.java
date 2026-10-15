// CAMADA: DTO
// RESPONSABILIDADE: devolve os dados do talhão sem expor a entidade JPA.
// SITUAÇÃO: implementada.
package api.dto;

import java.math.BigDecimal;

public record TalhaoResponseDTO(Long id, String nome, BigDecimal areaHectares, boolean irrigado) {
}
