// CAMADA: Service
// RESPONSABILIDADE: sinaliza violações de regras de negócio.
// REGRAS: R1–R3 pertencem ao PlantioService; esta classe não aplica essas regras.
// ROTAS ATENDIDAS: /talhoes, /culturas e /plantios (tratamento de erros), conforme docs/contrato-da-api.md.
// SITUAÇÃO: implementada.
package api.service;
public class RegraDeNegocioException extends RuntimeException {
    public RegraDeNegocioException(String mensagem) {
        super(mensagem);
    }
}
