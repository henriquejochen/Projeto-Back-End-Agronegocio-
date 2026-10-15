// CAMADA: Model
// RESPONSABILIDADE: representa a ocupação de um talhão por uma cultura e suas datas.
// SITUAÇÃO: implementada.
package api.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "plantios")
public class Plantio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "talhao_id", nullable = false)
    private Talhao talhao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cultura_id", nullable = false)
    private Cultura cultura;

    @Column(name = "area_hectares", nullable = false, precision = 12, scale = 2)
    private BigDecimal areaHectares;

    @Column(name = "data_plantio", nullable = false)
    private LocalDate dataPlantio;

    @Column(name = "previsao_colheita", nullable = false)
    private LocalDate previsaoColheita;

    @Column(name = "data_colheita")
    private LocalDate dataColheita;

    protected Plantio() { }

    public Plantio(Talhao talhao, Cultura cultura, BigDecimal areaHectares,
                   LocalDate dataPlantio, LocalDate previsaoColheita) {
        this.talhao = talhao;
        this.cultura = cultura;
        this.areaHectares = areaHectares;
        this.dataPlantio = dataPlantio;
        this.previsaoColheita = previsaoColheita;
    }

    public void registrarColheita(LocalDate dataColheita) {
        this.dataColheita = dataColheita;
    }

    public Long getId() { return id; }
    public Talhao getTalhao() { return talhao; }
    public Cultura getCultura() { return cultura; }
    public BigDecimal getAreaHectares() { return areaHectares; }
    public LocalDate getDataPlantio() { return dataPlantio; }
    public LocalDate getPrevisaoColheita() { return previsaoColheita; }
    public LocalDate getDataColheita() { return dataColheita; }
}
