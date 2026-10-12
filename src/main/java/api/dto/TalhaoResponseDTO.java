package api.dto;

import java.math.BigDecimal;

// DTO: devolve os dados do talhão sem expor a entidade JPA. Implementado.
public record TalhaoResponseDTO(Long id, String nome, BigDecimal areaHectares, boolean irrigado) {
}
