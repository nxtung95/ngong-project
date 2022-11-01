package vn.ngong.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.ngong.entity.RegisterProject;

@Repository
public interface RegisterProjectRepository extends JpaRepository<RegisterProject, Integer> {
}
