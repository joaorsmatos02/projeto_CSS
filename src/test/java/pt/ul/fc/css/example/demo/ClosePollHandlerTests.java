package pt.ul.fc.css.example.demo;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import pt.ul.fc.css.example.demo.dtos.PollDTO;
import pt.ul.fc.css.example.demo.entities.*;
import pt.ul.fc.css.example.demo.exceptions.PollAlreadyClosedException;
import pt.ul.fc.css.example.demo.exceptions.PollInCourseException;
import pt.ul.fc.css.example.demo.exceptions.PollNotFoundException;
import pt.ul.fc.css.example.demo.exceptions.VoteNotFoundException;
import pt.ul.fc.css.example.demo.handlers.ClosePollHandler;
import pt.ul.fc.css.example.demo.repositories.PollRepository;
import pt.ul.fc.css.example.demo.repositories.ProjectRepository;
import pt.ul.fc.css.example.demo.repositories.ThemeRepository;
import pt.ul.fc.css.example.demo.repositories.VoterDelegationRepository;
import pt.ul.fc.css.example.demo.repositories.VoterRepository;
import pt.ul.fc.css.example.demo.repositories.VoterVoteRepository;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ClosePollHandlerTests {

  @Autowired private PollRepository pollRepository;
  @Autowired private ProjectRepository projectRepository;
  @Autowired private ThemeRepository themeRepository;
  @Autowired private VoterRepository voterRepository;
  @Autowired private VoterVoteRepository voterVoteRepository;
  @Autowired private VoterDelegationRepository voterDelegationRepository;

  private ClosePollHandler handler;

  @Test
  void closePoll()
      throws PollNotFoundException, PollAlreadyClosedException, PollInCourseException,
          VoteNotFoundException {

    handler.closePoll();

    List<PollDTO> polls = handler.getPolls();

    boolean result = true;
    for (PollDTO p : polls) {
      System.out.println(p.getId());
      if (p.getId() == 1 || p.getId() == 3) {
        result = false;
        break;
      }
    }

    assertEquals(1, polls.size());

    assertTrue(result);
  }

  @BeforeEach
  public void loadNecessaryObjects() {
    List<LocalDateTime> expirationDates = new ArrayList<>();
    expirationDates.add(LocalDateTime.of(2023, 9, 17, 12, 30));
    expirationDates.add(LocalDateTime.of(2023, 6, 30, 9, 45));

    handler =
        new ClosePollHandler(
            pollRepository, voterRepository, voterVoteRepository, voterDelegationRepository);

    Theme general = new Theme("General", null);
    Theme health = new Theme("Health", general);
    Theme education = new Theme("Education", general);
    Theme hospitals = new Theme("Hospitals", health);
    Theme schools = new Theme("Schools", education);
    themeRepository.save(general);
    themeRepository.save(health);
    themeRepository.save(education);
    themeRepository.save(hospitals);
    themeRepository.save(schools);

    Delegate delegate1 = new Delegate("111", "delegate1");
    Delegate delegate2 = new Delegate("222", "delegate2");
    Delegate delegate3 = new Delegate("333", "delegate3");
    voterRepository.save(delegate1);
    voterRepository.save(delegate2);
    voterRepository.save(delegate3);

    Project project1 =
        new Project(general, "Project1", "test", null, expirationDates.get(0), delegate1);
    Project project2 =
        new Project(health, "Project2", "test", null, expirationDates.get(1), delegate2);
    Project project3 =
        new Project(schools, "Project3", "test", null, expirationDates.get(1), delegate3);
    projectRepository.save(project1);
    projectRepository.save(project2);
    projectRepository.save(project3);

    Poll poll1 = new Poll(project1, true);
    poll1.setExpirationDate(LocalDateTime.of(2023, 2, 20, 9, 45));
    Poll poll2 = new Poll(project2, true);
    Poll poll3 = new Poll(project3, false);
    pollRepository.save(poll1);
    pollRepository.save(poll2);
    pollRepository.save(poll3);
  }
}
