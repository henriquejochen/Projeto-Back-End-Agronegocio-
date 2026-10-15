// CAMADA: Repository
// RESPONSABILIDADE: consulta ocupação ativa e bloqueia plantio durante a colheita.
// SITUAÇÃO: implementada.
package api.repository;
import api.model.Plantio;
import java.math.BigDecimal;
import java.util.Optional;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
public interface PlantioRepository extends JpaRepository<Plantio, Long> {
    @Query("select coalesce(sum(p.areaHectares), 0) from Plantio p where p.talhao.id = :talhaoId and p.dataColheita is null")
    BigDecimal somarAreaAtiva(@Param("talhaoId") Long talhaoId);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Plantio p where p.id = :id")
    Optional<Plantio> buscarParaColher(@Param("id") Long id);
}
