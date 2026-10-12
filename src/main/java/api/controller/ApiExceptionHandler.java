// Controller: padroniza os erros HTTP da API. Implementado.
package api.controller;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import jakarta.servlet.http.HttpServletRequest;
import api.dto.CampoErroDTO;
import api.dto.ErroDTO;
import api.service.RecursoNaoEncontradoException;
import api.service.RegraDeNegocioException;
@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroDTO> tratarParametroInvalido(MethodArgumentTypeMismatchException e,
                                                          HttpServletRequest request) {
        return responder(HttpStatus.BAD_REQUEST, "Parâmetro inválido: " + e.getName(), request, List.of());
    }
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroDTO> tratarNaoEncontrado(RecursoNaoEncontradoException e, HttpServletRequest request) {
        return responder(HttpStatus.NOT_FOUND, e.getMessage(), request, List.of());
    }
    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<ErroDTO> tratarRegraDeNegocio(RegraDeNegocioException e, HttpServletRequest request) {
        return responder(HttpStatus.BAD_REQUEST, e.getMessage(), request, List.of());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroDTO> tratarValidacao(MethodArgumentNotValidException e, HttpServletRequest request) {
        List<CampoErroDTO> campos = e.getBindingResult().getFieldErrors().stream()
                .map(erro -> new CampoErroDTO(erro.getField(), erro.getDefaultMessage()))
                .sorted(Comparator.comparing(CampoErroDTO::campo).thenComparing(CampoErroDTO::mensagem))
                .toList();
        return responder(HttpStatus.BAD_REQUEST, "Dados inválidos: confira a lista de campos", request, campos);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroDTO> tratarCorpoIlegivel(HttpMessageNotReadableException e, HttpServletRequest request) {
        return responder(HttpStatus.BAD_REQUEST,
                "Corpo da requisição inválido: confira o JSON e o formato das datas (yyyy-MM-dd)", request, List.of());
    }
    private ResponseEntity<ErroDTO> responder(HttpStatus status, String mensagem, HttpServletRequest request,
                                              List<CampoErroDTO> campos) {
        ErroDTO erro = new ErroDTO(status.value(), status.getReasonPhrase(), mensagem,
                request.getRequestURI(), LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), campos);
        return ResponseEntity.status(status).body(erro);
    }
}
