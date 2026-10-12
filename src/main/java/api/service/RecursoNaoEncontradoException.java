// Service: base das exceções de recurso inexistente. Implementada.
package api.service;
public class RecursoNaoEncontradoException extends RuntimeException {
    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
