package vn.ngong.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ngong.entity.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {

}
