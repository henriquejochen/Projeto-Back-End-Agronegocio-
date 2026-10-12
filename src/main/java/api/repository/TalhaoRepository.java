package api.repository;

import api.model.Talhao;
import org.springframework.data.jpa.repository.JpaRepository;

// Repository: persiste e consulta os talhões. Implementado.
public interface TalhaoRepository extends JpaRepository<Talhao, Long> {
}
