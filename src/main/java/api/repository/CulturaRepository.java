// CAMADA: Repository
// RESPONSABILIDADE: persiste e consulta as culturas.
// SITUAÇÃO: implementada.
package api.repository;

import api.model.Cultura;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CulturaRepository extends JpaRepository<Cultura, Long> {
}