// CAMADA: Service
// RESPONSABILIDADE: sinaliza violações de regras de negócio.
// REGRAS: não implementa R1–R3; regras de Plantio ainda pendentes.
// ROTAS ATENDIDAS: /talhoes e /culturas (tratamento de erros), conforme docs/contrato-da-api.md.
// SITUAÇÃO: implementada.
package api.service;
public class RegraDeNegocioException extends RuntimeException {
    public RegraDeNegocioException(String mensagem) {
        super(mensagem);
    }
}
