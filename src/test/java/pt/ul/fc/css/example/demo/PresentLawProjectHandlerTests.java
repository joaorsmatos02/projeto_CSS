package pt.ul.fc.css.example.demo;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import pt.ul.fc.css.example.demo.entities.Delegate;
import pt.ul.fc.css.example.demo.entities.Project;
import pt.ul.fc.css.example.demo.entities.Theme;
import pt.ul.fc.css.example.demo.exceptions.ParameterException;
import pt.ul.fc.css.example.demo.exceptions.ThemeNotFoundException;
import pt.ul.fc.css.example.demo.exceptions.VoterNotFoundException;
import pt.ul.fc.css.example.demo.handlers.PresentLawProjectHandler;
import pt.ul.fc.css.example.demo.repositories.ProjectRepository;
import pt.ul.fc.css.example.demo.repositories.ThemeRepository;
import pt.ul.fc.css.example.demo.repositories.VoterRepository;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PresentLawProjectHandlerTests {

  @Autowired private ProjectRepository projectRepository;
  @Autowired private VoterRepository voterRepository;
  @Autowired private ThemeRepository themeRepository;

  @Test
  public void presentLawProject()
      throws VoterNotFoundException, ThemeNotFoundException, ParameterException {
    PresentLawProjectHandler handler =
        new PresentLawProjectHandler(projectRepository, voterRepository, themeRepository);

    LocalDateTime dateTime = LocalDateTime.of(2023, 10, 15, 20, 15);
    LocalDateTime invalidDateTime = LocalDateTime.now().plusYears(1).plusSeconds(1);
    byte[] pdf = new byte[] {1, 2, 3, 4, 5};

    handler.presentLawProject(1, "Project1", "test", pdf, dateTime, "111");

    Project expected =
        new Project(
            themeRepository.findById(1L).get(),
            "Project1",
            "test",
            pdf,
            dateTime,
            (Delegate) voterRepository.findByCC("111").get());
    expected.setId(2);

    Optional<Project> project = projectRepository.findById(2L);

    assertEquals(expected, project.get());
  }

  @Test
  public void invalidTheme() {
    PresentLawProjectHandler handler =
        new PresentLawProjectHandler(projectRepository, voterRepository, themeRepository);

    LocalDateTime dateTime = LocalDateTime.of(2023, 10, 15, 20, 15);
    LocalDateTime invalidDateTime = LocalDateTime.now().plusYears(1).plusSeconds(1);
    byte[] pdf = new byte[] {1, 2, 3, 4, 5};

    assertThrows(
        ThemeNotFoundException.class,
        () -> handler.presentLawProject(10, "Project1", "test", pdf, dateTime, "111"));
  }

  @Test
  public void invalidTitle() {
    PresentLawProjectHandler handler =
        new PresentLawProjectHandler(projectRepository, voterRepository, themeRepository);

    LocalDateTime dateTime = LocalDateTime.of(2023, 10, 15, 20, 15);
    LocalDateTime invalidDateTime = LocalDateTime.now().plusYears(1).plusSeconds(1);
    byte[] pdf = new byte[] {1, 2, 3, 4, 5};

    assertThrows(
        ParameterException.class,
        () -> handler.presentLawProject(1, "", "test", pdf, dateTime, "111"));
  }

  @Test
  public void invalidDescription() {
    PresentLawProjectHandler handler =
        new PresentLawProjectHandler(projectRepository, voterRepository, themeRepository);

    LocalDateTime dateTime = LocalDateTime.of(2023, 10, 15, 20, 15);
    LocalDateTime invalidDateTime = LocalDateTime.now().plusYears(1).plusSeconds(1);
    byte[] pdf = new byte[] {1, 2, 3, 4, 5};

    assertThrows(
        ParameterException.class,
        () -> handler.presentLawProject(1, "Project1", "", pdf, dateTime, "111"));
  }

  @Test
  public void invalidPDF() {
    PresentLawProjectHandler handler =
        new PresentLawProjectHandler(projectRepository, voterRepository, themeRepository);

    LocalDateTime dateTime = LocalDateTime.of(2023, 10, 15, 20, 15);
    LocalDateTime invalidDateTime = LocalDateTime.now().plusYears(1).plusSeconds(1);
    byte[] pdf = new byte[] {1, 2, 3, 4, 5};

    assertThrows(
        ParameterException.class,
        () -> handler.presentLawProject(1, "Project1", "test", null, dateTime, "111"));
  }

  @Test
  public void invalidExpirationDate1() {
    PresentLawProjectHandler handler =
        new PresentLawProjectHandler(projectRepository, voterRepository, themeRepository);

    LocalDateTime dateTime = LocalDateTime.of(2023, 10, 15, 20, 15);
    LocalDateTime invalidDateTime = LocalDateTime.now().plusYears(1).plusSeconds(1);
    byte[] pdf = new byte[] {1, 2, 3, 4, 5};

    assertThrows(
        ParameterException.class,
        () -> handler.presentLawProject(1, "Project1", "test", pdf, null, "111"));
  }

  @Test
  public void invalidExpirationDate2() {
    PresentLawProjectHandler handler =
        new PresentLawProjectHandler(projectRepository, voterRepository, themeRepository);

    LocalDateTime dateTime = LocalDateTime.of(2023, 10, 15, 20, 15);
    LocalDateTime invalidDateTime = LocalDateTime.now().plusYears(1).plusSeconds(1);
    byte[] pdf = new byte[] {1, 2, 3, 4, 5};

    assertThrows(
        ParameterException.class,
        () -> handler.presentLawProject(1, "Project1", "test", pdf, invalidDateTime, "111"));
  }

  @Test
  public void invalidVoterCC1() {
    PresentLawProjectHandler handler =
        new PresentLawProjectHandler(projectRepository, voterRepository, themeRepository);

    LocalDateTime dateTime = LocalDateTime.of(2023, 10, 15, 20, 15);
    LocalDateTime invalidDateTime = LocalDateTime.now().plusYears(1).plusSeconds(1);
    byte[] pdf = new byte[] {1, 2, 3, 4, 5};

    assertThrows(
        ParameterException.class,
        () -> handler.presentLawProject(1, "Project1", "test", pdf, dateTime, null));
  }

  @Test
  public void invalidVoterCC2() {
    PresentLawProjectHandler handler =
        new PresentLawProjectHandler(projectRepository, voterRepository, themeRepository);

    LocalDateTime dateTime = LocalDateTime.of(2023, 10, 15, 20, 15);
    LocalDateTime invalidDateTime = LocalDateTime.now().plusYears(1).plusSeconds(1);
    byte[] pdf = new byte[] {1, 2, 3, 4, 5};

    assertThrows(
        VoterNotFoundException.class,
        () -> handler.presentLawProject(1, "Project1", "test", pdf, dateTime, "000"));
  }

  @BeforeEach
  public void loadNecessaryObjects() {
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
    projectRepository.save(project1);
  }
}
