package pt.ul.fc.css.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import pt.ul.fc.css.example.demo.dtos.PollDTO;
import pt.ul.fc.css.example.demo.entities.Delegate;
import pt.ul.fc.css.example.demo.entities.Poll;
import pt.ul.fc.css.example.demo.entities.Project;
import pt.ul.fc.css.example.demo.entities.Theme;
import pt.ul.fc.css.example.demo.entities.Voter;
import pt.ul.fc.css.example.demo.entities.VoterDelegation;
import pt.ul.fc.css.example.demo.entities.VoterVote;
import pt.ul.fc.css.example.demo.entities.VoterVoteKey;
import pt.ul.fc.css.example.demo.exceptions.*;
import pt.ul.fc.css.example.demo.handlers.VotePollHandler;
import pt.ul.fc.css.example.demo.repositories.PollRepository;
import pt.ul.fc.css.example.demo.repositories.ProjectRepository;
import pt.ul.fc.css.example.demo.repositories.ThemeRepository;
import pt.ul.fc.css.example.demo.repositories.VoterDelegationRepository;
import pt.ul.fc.css.example.demo.repositories.VoterRepository;
import pt.ul.fc.css.example.demo.repositories.VoterVoteRepository;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class VotePollHandlerTests {

  @Autowired private PollRepository pollRepository;
  @Autowired private ProjectRepository projectRepository;
  @Autowired private ThemeRepository themeRepository;
  @Autowired private VoterRepository voterRepository;
  @Autowired private VoterVoteRepository voterVoteRepository;
  @Autowired private VoterDelegationRepository voterDelegationRepository;
  private VotePollHandler handler;

  @Test
  void votePollByOmissionAndPersonalVote()
          throws PollNotFoundException, VoterNotFoundException, VoteNotFoundException,
          VoteFoundException, DelegateNotFoundException {
    VotePollHandler handler =
        new VotePollHandler(
            voterRepository, pollRepository, voterVoteRepository, voterDelegationRepository);

    List<LocalDateTime> expirationDates = new ArrayList<>();
    expirationDates.add(LocalDateTime.of(2023, 10, 15, 20, 15));

    List<PollDTO> pollDTOs = new ArrayList<>();
    pollDTOs.add(new PollDTO(1, "General", "Project1", "test", expirationDates.get(0)));

    List<PollDTO> result = handler.listPollsInCourse();

    assertEquals(pollDTOs, result);

    assertTrue(handler.getDefaultVote("12345", 1).isContent());
    assertFalse(handler.getDefaultVote("12344", 1).isContent());

    handler.getDefaultVote("12345", 1);
    handler.confirmDefaultVote("12345", 1, true);
    assertEquals(pollRepository.findById(1L).get().getPositiveVotes(), 2);

    handler.getDefaultVote("12344", 1);
    handler.vote(false, "12344", 1);

    assertEquals(pollRepository.findById(1L).get().getPositiveVotes(), 2);
    assertEquals(pollRepository.findById(1L).get().getNegativeVotes(), 2);

    handler.vote(true, "444", 1);
    Optional<VoterVote> vote = voterVoteRepository.findById(new VoterVoteKey(7, 1));
    assertTrue(vote.get().isApproved());
    assertEquals(pollRepository.findById(1L).get().getPositiveVotes(), 3);
    assertEquals(pollRepository.findById(1L).get().getNegativeVotes(), 2);
  }

  @Test
  void PollNotFound() {
    VotePollHandler handler =
        new VotePollHandler(
            voterRepository, pollRepository, voterVoteRepository, voterDelegationRepository);

    assertThrows(PollNotFoundException.class, () -> handler.selectPoll(0));
  }

  @Test
  void VoterNotFound() throws PollNotFoundException {
    VotePollHandler handler =
        new VotePollHandler(
            voterRepository, pollRepository, voterVoteRepository, voterDelegationRepository);

    assertThrows(VoterNotFoundException.class, () -> handler.getDefaultVote("", 1));
  }

  @Test
  void VoteNotFound() throws PollNotFoundException {
    VotePollHandler handler =
        new VotePollHandler(
            voterRepository, pollRepository, voterVoteRepository, voterDelegationRepository);

    assertThrows(VoteNotFoundException.class, () -> handler.getDefaultVote("12343", 1));
  }

  @Test
  void VoteAlreadyFound() throws PollNotFoundException, VoterNotFoundException, VoteFoundException {
    VotePollHandler handler =
        new VotePollHandler(
            voterRepository, pollRepository, voterVoteRepository, voterDelegationRepository);

    handler.vote(true, "444", 1);
    assertThrows(VoteFoundException.class, () -> handler.vote(true, "444", 1));
  }

  @BeforeEach
  public void loadNecessaryObjects() {
    List<LocalDateTime> expirationDates = new ArrayList<>();
    expirationDates.add(LocalDateTime.of(2023, 10, 15, 20, 15));

    Theme general = new Theme("General", null);
    themeRepository.save(general);

    List<Voter> voters = voterRepository.findAll();

    for (Voter v : voters) System.out.println(v.getCc() + " " + v.getName());

    Delegate delegate1 = new Delegate("111", "delegate1");
    voterRepository.save(delegate1);

    Delegate delegate2 = new Delegate("222", "delegate2");
    voterRepository.save(delegate2);

    Delegate delegate3 = new Delegate("333", "delegate3");
    voterRepository.save(delegate3);

    Project project1 =
        new Project(general, "Project1", "test", null, expirationDates.get(0), delegate1);
    projectRepository.save(project1);

    Poll poll1 = new Poll(project1, true);
    pollRepository.save(poll1);

    Voter voter1 = new Voter("12345", "Joao");
    voterRepository.save(voter1);
    Voter voter2 = new Voter("12344", "Joao");
    voterRepository.save(voter2);
    Voter voter3 = new Voter("12343", "Joao");
    voterRepository.save(voter3);

    Delegate delegate4 = new Delegate("444", "delegate4");
    voterRepository.save(delegate4);

    VoterDelegation vcd1 = new VoterDelegation(delegate1, voter1, general);
    voterDelegationRepository.save(vcd1);
    VoterDelegation vcd2 = new VoterDelegation(delegate3, voter2, general);
    voterDelegationRepository.save(vcd2);
    VoterDelegation vcd3 = new VoterDelegation(delegate2, voter3, general);
    voterDelegationRepository.save(vcd3);

    assertEquals(voterDelegationRepository.findAll().size(), 3);

    VoterVote vvp = new VoterVote(delegate1, poll1, true);
    voterVoteRepository.save(vvp);

    VoterVote vvp2 = new VoterVote(delegate3, poll1, false);
    voterVoteRepository.save(vvp2);

    poll1.vote(false);
    pollRepository.save(poll1);
  }
}
