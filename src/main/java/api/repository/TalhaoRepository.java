// CAMADA: Repository
// RESPONSABILIDADE: persiste e consulta os talhões.
// SITUAÇÃO: implementada.
package api.repository;

import api.model.Talhao;
import java.util.Optional;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TalhaoRepository extends JpaRepository<Talhao, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select t from Talhao t where t.id = :id")
    Optional<Talhao> buscarParaPlantio(@Param("id") Long id);
}
