// CAMADA: Repository
// RESPONSABILIDADE: persiste e consulta os talhões.
// SITUAÇÃO: implementada.
package api.repository;

import api.model.Talhao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TalhaoRepository extends JpaRepository<Talhao, Long> {
}
