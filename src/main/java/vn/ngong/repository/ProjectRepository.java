package vn.ngong.repository;

import lombok.Getter;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.ngong.entity.Project;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
	List<Project> findAllByStatusOrderByCreatedAtAsc(int status);
}
