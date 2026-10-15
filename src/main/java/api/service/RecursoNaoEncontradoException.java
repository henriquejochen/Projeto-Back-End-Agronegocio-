// CAMADA: Service
// RESPONSABILIDADE: base das exceções de recurso inexistente.
// REGRAS: R1–R3 pertencem ao PlantioService; esta classe não aplica essas regras.
// ROTAS ATENDIDAS: /talhoes, /culturas e /plantios (tratamento de erros), conforme docs/contrato-da-api.md.
// SITUAÇÃO: implementada.
package api.service;
public class RecursoNaoEncontradoException extends RuntimeException {
    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
