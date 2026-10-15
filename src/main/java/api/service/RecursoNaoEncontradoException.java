// CAMADA: Service
// RESPONSABILIDADE: base das exceções de recurso inexistente.
// REGRAS: não implementa R1–R3; regras de Plantio ainda pendentes.
// ROTAS ATENDIDAS: /talhoes e /culturas (tratamento de erros), conforme docs/contrato-da-api.md.
// SITUAÇÃO: implementada.
package api.service;
public class RecursoNaoEncontradoException extends RuntimeException {
    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
