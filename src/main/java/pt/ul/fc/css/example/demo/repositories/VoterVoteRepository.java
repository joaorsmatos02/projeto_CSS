package pt.ul.fc.css.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.ul.fc.css.example.demo.entities.VoterVote;
import pt.ul.fc.css.example.demo.entities.VoterVoteKey;

public interface VoterVoteRepository extends JpaRepository<VoterVote, VoterVoteKey> {}
