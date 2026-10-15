// CAMADA: Model
// RESPONSABILIDADE: representa uma área de cultivo e sua disponibilidade de irrigação.
// SITUAÇÃO: implementada.
package api.model;

import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "talhoes")
public class Talhao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(name = "area_hectares", nullable = false, precision = 12, scale = 2)
    private BigDecimal areaHectares;

    @Column(nullable = false)
    private boolean irrigado;

    protected Talhao() {
    }

    public Talhao(String nome, BigDecimal areaHectares, boolean irrigado) {
        this.nome = nome;
        this.areaHectares = areaHectares;
        this.irrigado = irrigado;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public BigDecimal getAreaHectares() { return areaHectares; }
    public boolean isIrrigado() { return irrigado; }
}
