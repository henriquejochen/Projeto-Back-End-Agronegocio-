// CAMADA: Model
// RESPONSABILIDADE: representa uma cultura agrícola e suas necessidades.
// SITUAÇÃO: implementada.
package api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "culturas")
public class Cultura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(name = "ciclo_dias", nullable = false)
    private Integer cicloDias;

    @Column(name = "exige_irrigacao", nullable = false)
    private boolean exigeIrrigacao;

    protected Cultura() {
    }

    public Cultura(String nome, Integer cicloDias, boolean exigeIrrigacao) {
        this.nome = nome;
        this.cicloDias = cicloDias;
        this.exigeIrrigacao = exigeIrrigacao;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Integer getCicloDias() {
        return cicloDias;
    }

    public boolean isExigeIrrigacao() {
        return exigeIrrigacao;
    }
}