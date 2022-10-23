package vn.ngong.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ngong.entity.SystemParameter;

import java.util.List;

@Repository
public interface SystemParameterRepository extends JpaRepository<SystemParameter, Integer> {
	List<SystemParameter> findAllByStatusc(int status);
}
