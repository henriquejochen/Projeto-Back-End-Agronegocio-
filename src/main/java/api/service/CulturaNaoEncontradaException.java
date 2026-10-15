// CAMADA: Service
// RESPONSABILIDADE: sinaliza uma cultura inexistente para o tratamento HTTP 404.
// REGRAS: não implementa R1–R3; regras de Plantio ainda pendentes.
// ROTAS ATENDIDAS: /culturas, conforme docs/contrato-da-api.md.
// SITUAÇÃO: implementada.
package api.service;

public class CulturaNaoEncontradaException
        extends RecursoNaoEncontradoException {

    public CulturaNaoEncontradaException(Long id) {
        super("Cultura não encontrada: " + id);
    }
}