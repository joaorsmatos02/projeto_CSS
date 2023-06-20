package pt.ul.fc.css.example.demo.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pt.ul.fc.css.example.demo.entities.Poll;

public interface PollRepository extends JpaRepository<Poll, Long> {

  @Query("SELECT p FROM Poll p WHERE p.inCourse=true")
  List<Poll> findAllInCourse();
}
