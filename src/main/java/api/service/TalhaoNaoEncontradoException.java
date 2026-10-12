package api.service;

// Service: sinaliza um talhão inexistente para o tratamento HTTP 404. Implementada.
public class TalhaoNaoEncontradoException extends RecursoNaoEncontradoException {
    public TalhaoNaoEncontradoException(Long id) {
        super("Talhão não encontrado: " + id);
    }
}
