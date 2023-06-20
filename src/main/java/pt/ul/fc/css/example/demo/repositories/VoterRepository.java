package pt.ul.fc.css.example.demo.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pt.ul.fc.css.example.demo.entities.Delegate;
import pt.ul.fc.css.example.demo.entities.Voter;

public interface VoterRepository extends JpaRepository<Voter, Long> {

  @Query("SELECT d FROM Voter d WHERE d.cc = :voterCC")
  Optional<Voter> findByCC(@Param("voterCC") String voterCC);

  @Query("SELECT d FROM Delegate d")
  List<Delegate> findAllDelegates();
}
