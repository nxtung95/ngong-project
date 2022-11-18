package vn.ngong.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ngong.entity.CityAgentCTV;

import java.util.List;

@Repository
public interface CityAgentCTVRepository extends JpaRepository<CityAgentCTV, Integer> {
    List<CityAgentCTV> findAllByStatusOrderByOrderNumberAsc(int status);
}
