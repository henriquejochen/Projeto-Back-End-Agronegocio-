// CAMADA: DTO
// RESPONSABILIDADE: identifica um campo inválido.
// SITUAÇÃO: implementada.
package api.dto;
import io.swagger.v3.oas.annotations.media.Schema;
@Schema(description = "Erro de validação de um campo do corpo da requisição")
public record CampoErroDTO(
        @Schema(description = "Nome do campo no JSON", example = "nome")
        String campo,
        @Schema(description = "Mensagem da validação que falhou", example = "Nome é obrigatório")
        String mensagem
) {
}
