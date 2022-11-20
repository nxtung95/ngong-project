package vn.ngong.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ngong.entity.District;

import java.util.List;

@Repository
public interface DistrictRepository extends JpaRepository<District, Integer> {
//	List<District> findAllByCityCodeAndStatusOrderByOrderNumberAsc(String cityCode, int status);

	List<District> findAllByStatusOrderByOrderNumberAsc(int status);
}
