package pt.ul.fc.css.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.ul.fc.css.example.demo.entities.VoterDelegation;
import pt.ul.fc.css.example.demo.entities.VoterDelegationKey;

public interface VoterDelegationRepository
    extends JpaRepository<VoterDelegation, VoterDelegationKey> {}
