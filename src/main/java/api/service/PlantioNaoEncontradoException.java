// CAMADA: Service
// RESPONSABILIDADE: sinaliza plantio inexistente para resposta 404.
// SITUAÇÃO: implementada.
// REGRAS: referência inexistente.
// ROTAS ATENDIDAS: PUT /plantios/{id}/colher.
package api.service;
public class PlantioNaoEncontradoException extends RecursoNaoEncontradoException {
    public PlantioNaoEncontradoException(Long id) { super("Plantio não encontrado: " + id); }
}
