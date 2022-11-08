package vn.ngong.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ngong.entity.Sogao;

import java.util.List;

@Repository
public interface SoGaoRepository extends JpaRepository<Sogao, Integer> {
    List<Sogao> findAllByStatusOrderByOrderNumber(int status);

    Sogao findById(int id);
}
