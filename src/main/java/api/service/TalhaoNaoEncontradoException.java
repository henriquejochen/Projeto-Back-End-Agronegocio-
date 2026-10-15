// CAMADA: Service
// RESPONSABILIDADE: sinaliza um talhão inexistente para o tratamento HTTP 404.
// REGRAS: R1–R3 pertencem ao PlantioService; esta classe não aplica essas regras.
// ROTAS ATENDIDAS: /talhoes, conforme docs/contrato-da-api.md.
// SITUAÇÃO: implementada.
package api.service;

public class TalhaoNaoEncontradoException extends RecursoNaoEncontradoException {
    public TalhaoNaoEncontradoException(Long id) {
        super("Talhão não encontrado: " + id);
    }
}
