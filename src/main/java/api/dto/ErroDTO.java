// DTO: formato comum das respostas de erro. Implementado.
package api.dto;
import java.time.LocalDateTime;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
@Schema(description = "Formato padrão de erro da API")
public record ErroDTO(
        @Schema(description = "Status HTTP", example = "404")
        int status,
        @Schema(description = "Nome do status HTTP", example = "Not Found")
        String erro,
        @Schema(description = "O que deu errado, em português", example = "Talhão não encontrado: 999")
        String mensagem,
        @Schema(description = "Caminho da requisição", example = "/talhoes/999")
        String caminho,
        @Schema(description = "Momento do erro", example = "2026-10-06T19:30:00")
        LocalDateTime timestamp,
        @Schema(description = "Erros de validação por campo (vazio quando não se aplica)")
        List<CampoErroDTO> campos
) {
}
