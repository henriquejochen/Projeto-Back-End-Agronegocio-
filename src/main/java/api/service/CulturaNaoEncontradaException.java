// CAMADA: Service
// RESPONSABILIDADE: sinaliza uma cultura inexistente para o tratamento HTTP 404.
// REGRAS: R1–R3 pertencem ao PlantioService; esta classe não aplica essas regras.
// ROTAS ATENDIDAS: /culturas, conforme docs/contrato-da-api.md.
// SITUAÇÃO: implementada.
package api.service;

public class CulturaNaoEncontradaException
        extends RecursoNaoEncontradoException {

    public CulturaNaoEncontradaException(Long id) {
        super("Cultura não encontrada: " + id);
    }
}