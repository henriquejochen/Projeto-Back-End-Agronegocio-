// Service: sinaliza violações de regras de negócio. Implementada.
package api.service;
public class RegraDeNegocioException extends RuntimeException {
    public RegraDeNegocioException(String mensagem) {
        super(mensagem);
    }
}
