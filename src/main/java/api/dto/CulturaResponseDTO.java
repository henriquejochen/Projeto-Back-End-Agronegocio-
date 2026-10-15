// CAMADA: DTO
// RESPONSABILIDADE: devolve os dados da cultura sem expor a entidade JPA.
// SITUAÇÃO: implementada.
package api.dto;

public record CulturaResponseDTO(
        Long id,
        String nome,
        Integer cicloDias,
        boolean exigeIrrigacao
) {
}