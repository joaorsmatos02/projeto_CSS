package pt.ul.fc.css.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
import pt.ul.fc.css.example.demo.handlers.CloseExpiredLawProjectsHandler;
import pt.ul.fc.css.example.demo.handlers.ListPollsInCourseHandler;
import pt.ul.fc.css.example.demo.repositories.PollRepository;
import pt.ul.fc.css.example.demo.repositories.ProjectRepository;
import pt.ul.fc.css.example.demo.repositories.ThemeRepository;
import pt.ul.fc.css.example.demo.repositories.VoterRepository;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CloseExpiredLawProjectsHandlerTests {

  @Autowired private PollRepository pollRepository;
  @Autowired private ProjectRepository projectRepository;
  @Autowired private ThemeRepository themeRepository;
  @Autowired private VoterRepository voterRepository;

  @Test
  void closeExpiredProjects() {
    CloseExpiredLawProjectsHandler handler = new CloseExpiredLawProjectsHandler(projectRepository);
    ListPollsInCourseHandler listHandler = new ListPollsInCourseHandler(pollRepository);

    List<LocalDateTime> expirationDates = new ArrayList<>();
    expirationDates.add(LocalDateTime.of(2023, 1, 15, 20, 15));
    expirationDates.add(LocalDateTime.of(2023, 9, 17, 12, 30));
    expirationDates.add(LocalDateTime.of(2023, 1, 30, 9, 45));

    handler.closeExpiredLawProjects();

    List<PollDTO> expected = new ArrayList<>();
    expected.add(new PollDTO(2, "Health", "Project2", "test", expirationDates.get(1)));

    List<PollDTO> result = listHandler.listPollsInCourse();

    assertEquals(expected, result);
  }

  @BeforeEach
  public void loadNecessaryObjects() {
    List<LocalDateTime> expirationDates = new ArrayList<>();
    expirationDates.add(LocalDateTime.of(2023, 1, 15, 20, 15));
    expirationDates.add(LocalDateTime.of(2023, 9, 17, 12, 30));
    expirationDates.add(LocalDateTime.of(2023, 1, 30, 9, 45));

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
        new Project(schools, "Project3", "test", null, expirationDates.get(2), delegate3);
    projectRepository.save(project1);
    projectRepository.save(project2);
    projectRepository.save(project3);

    Poll poll1 = new Poll(project1, false);
    Poll poll2 = new Poll(project2, true);
    Poll poll3 = new Poll(project3, false);
    pollRepository.save(poll1);
    pollRepository.save(poll2);
    pollRepository.save(poll3);
  }
}
