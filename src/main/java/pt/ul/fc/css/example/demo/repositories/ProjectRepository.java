package pt.ul.fc.css.example.demo.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pt.ul.fc.css.example.demo.entities.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {

  @Query("SELECT p FROM Project p WHERE p.isExpired = false")
  List<Project> findAllNotExpired();

  @Query("SELECT p FROM Project p WHERE (p.isExpired = false AND p.title = :projectTitle)")
  Optional<Project> findNotExpiredByTitle(@Param("projectTitle") String projectTitle);
}
