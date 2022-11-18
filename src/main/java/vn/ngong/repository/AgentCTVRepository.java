package vn.ngong.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ngong.entity.AgentCTV;
import vn.ngong.entity.CityAgentCTV;

import java.util.List;

@Repository
public interface AgentCTVRepository extends JpaRepository<AgentCTV, Integer> {
    List<AgentCTV> findAllByStatusOrderByOrderNumberAsc(int i);
//    List<>
}
