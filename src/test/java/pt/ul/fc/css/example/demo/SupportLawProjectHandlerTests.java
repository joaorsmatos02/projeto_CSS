package pt.ul.fc.css.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import pt.ul.fc.css.example.demo.entities.*;
import pt.ul.fc.css.example.demo.exceptions.*;
import pt.ul.fc.css.example.demo.handlers.SupportLawProjectHandler;
import pt.ul.fc.css.example.demo.repositories.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SupportLawProjectHandlerTests {

  @Autowired private VoterRepository voterRepository;
  @Autowired private PollRepository pollRepository;
  @Autowired private ProjectRepository projectRepository;
  @Autowired private ThemeRepository themeRepository;
  @Autowired private VoterVoteRepository voterVoteRepository;

  @Test
  void supportLawProject()
      throws ProjectNotFoundException, VoterNotFoundException, PollAlreadyExistsException,
          ParameterException, VoterAlreadySupportsException {
    SupportLawProjectHandler handler =
        new SupportLawProjectHandler(
            voterRepository, projectRepository, pollRepository, voterVoteRepository);


    // ver que ainda nao foi criada uma poll e que o project tem 9999 votos
    List<Poll> polls = pollRepository.findAllInCourse();
    assertEquals(polls.size(), 0);

    long votes = projectRepository.findById(1L).get().getNrSupporters();
    assertEquals(votes, 9999);

    handler.supportLawProject("5", 1);

    // verificar que ja existe uma poll
    List<Poll> pollsResult = pollRepository.findAllInCourse();
    assertEquals(pollsResult.size(), 1);
  }

  @Test
  void wrongParameter() {
    SupportLawProjectHandler handler =
        new SupportLawProjectHandler(
            voterRepository, projectRepository, pollRepository, voterVoteRepository);

    assertThrows(ParameterException.class, () -> handler.supportLawProject("", 1));
  }

  @Test
  void voterNotFound() {
    SupportLawProjectHandler handler =
        new SupportLawProjectHandler(
            voterRepository, projectRepository, pollRepository, voterVoteRepository);

    assertThrows(VoterNotFoundException.class, () -> handler.supportLawProject("313", 1));
  }

  @Test
  void projectNotFound() {
    SupportLawProjectHandler handler =
        new SupportLawProjectHandler(
            voterRepository, projectRepository, pollRepository, voterVoteRepository);

    assertThrows(ProjectNotFoundException.class, () -> handler.supportLawProject("999", 2));
  }


  @Test
  void pollAlreadyExists()
      throws ProjectNotFoundException, VoterNotFoundException, PollAlreadyExistsException,
          ParameterException, VoterAlreadySupportsException {
    SupportLawProjectHandler handler =
        new SupportLawProjectHandler(
            voterRepository, projectRepository, pollRepository, voterVoteRepository);


    handler.supportLawProject("5", 1);

    assertThrows(PollAlreadyExistsException.class, () -> handler.supportLawProject("999", 1));
  }


  @BeforeEach
  void loadNecessaryObjects() {
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
    voterRepository.save(delegate1);

    Project project1 =
        new Project(
            general, "Project1", "test", null, LocalDateTime.of(2023, 10, 15, 20, 15), delegate1);
    project1.setNrSupporters(9999);
    projectRepository.save(project1);

    voterRepository.save(new Voter("999", "finalVoter"));
    voterRepository.save(new Voter("5", "finalVoter1"));
  }
}
