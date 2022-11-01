package vn.ngong.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ngong.entity.RegisterAgentCTV;

@Repository
public interface RegisterAgentCTVRepository extends JpaRepository<RegisterAgentCTV, Integer> {
}
