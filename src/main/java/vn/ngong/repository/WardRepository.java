package vn.ngong.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ngong.entity.Ward;

import java.util.List;

@Repository
public interface WardRepository extends JpaRepository<Ward, Integer> {
	List<Ward> findAllByStatusOrderByOrderNumberAsc(int status);
}
