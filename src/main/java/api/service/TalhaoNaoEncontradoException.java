// CAMADA: Service
// RESPONSABILIDADE: sinaliza um talhão inexistente para o tratamento HTTP 404.
// REGRAS: não implementa R1–R3; regras de Plantio ainda pendentes.
// ROTAS ATENDIDAS: /talhoes, conforme docs/contrato-da-api.md.
// SITUAÇÃO: implementada.
package api.service;

public class TalhaoNaoEncontradoException extends RecursoNaoEncontradoException {
    public TalhaoNaoEncontradoException(Long id) {
        super("Talhão não encontrado: " + id);
    }
}
