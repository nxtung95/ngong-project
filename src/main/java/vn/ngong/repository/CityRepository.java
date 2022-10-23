package vn.ngong.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ngong.entity.City;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {
	List<City> findAllByStatusOrderByOrderNumberAsc(int status);
}
