// CAMADA: Repository
// RESPONSABILIDADE: persiste e consulta os talhões.
// SITUAÇÃO: implementada.
package api.repository;

import api.model.Talhao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TalhaoRepository extends JpaRepository<Talhao, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select t from Talhao t where t.id = :id")
    Optional<Talhao> buscarParaAtualizar(@Param("id") Long id);
}
