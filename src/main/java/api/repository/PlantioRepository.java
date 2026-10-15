// CAMADA: Repository
// RESPONSABILIDADE: persiste plantios, consulta ocupação ativa e bloqueia colheitas concorrentes.
// SITUAÇÃO: implementada.
package api.repository;

import java.math.BigDecimal;
import java.util.Optional;
import api.model.Plantio;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlantioRepository extends JpaRepository<Plantio, Long> {
    @Query("select coalesce(sum(p.areaHectares), 0) from Plantio p "
            + "where p.talhao.id = :talhaoId and p.dataColheita is null")
    BigDecimal somarAreaAtiva(@Param("talhaoId") Long talhaoId);

    @Query("select p.talhao.id from Plantio p where p.id = :id")
    Optional<Long> buscarTalhaoId(@Param("id") Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Plantio p where p.id = :id")
    Optional<Plantio> buscarParaAtualizar(@Param("id") Long id);
}
