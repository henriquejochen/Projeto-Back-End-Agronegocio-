// CAMADA: Service
// RESPONSABILIDADE: sinaliza plantio inexistente para resposta HTTP 404.
// REGRAS: nenhuma; representa ausência do recurso.
// ROTAS ATENDIDAS: /plantios/{id} e /plantios/{id}/colher.
// SITUAÇÃO: implementada.
package api.service;

public class PlantioNaoEncontradoException extends RecursoNaoEncontradoException {
    public PlantioNaoEncontradoException(Long id) {
        super("Plantio não encontrado: " + id);
    }
}
