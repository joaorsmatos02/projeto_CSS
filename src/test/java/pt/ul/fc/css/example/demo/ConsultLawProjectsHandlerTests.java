package pt.ul.fc.css.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import pt.ul.fc.css.example.demo.dtos.ProjectDTO;
import pt.ul.fc.css.example.demo.entities.Delegate;
import pt.ul.fc.css.example.demo.entities.Project;
import pt.ul.fc.css.example.demo.entities.Theme;
import pt.ul.fc.css.example.demo.exceptions.ProjectNotFoundException;
import pt.ul.fc.css.example.demo.handlers.ConsultLawProjectsHandler;
import pt.ul.fc.css.example.demo.repositories.ProjectRepository;
import pt.ul.fc.css.example.demo.repositories.ThemeRepository;
import pt.ul.fc.css.example.demo.repositories.VoterRepository;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ConsultLawProjectsHandlerTests {

  @Autowired private ProjectRepository projectRepository;
  @Autowired private ThemeRepository themeRepository;
  @Autowired private VoterRepository voterRepository;

  @Test
  void getLawProjects() {
    ConsultLawProjectsHandler handler = new ConsultLawProjectsHandler(projectRepository);
    List<ProjectDTO> projectDTOs = new ArrayList<>();
    projectDTOs.add(new ProjectDTO(
            1,
            "General",
            "Project1",
            "test",
            false,
            LocalDateTime.of(2023, 10, 15, 20, 15),
            1,
            null,
            "delegate1"));
    projectDTOs.add(new ProjectDTO(
            2,
            "Health",
            "Project2",
            "test",
            false,
            LocalDateTime.of(2023, 9, 17, 12, 30),
            1,
            null,
            "delegate2"));
    projectDTOs.add(new ProjectDTO(3,
            "Schools",
            "Project3",
            "test",
            false,
            LocalDateTime.of(2023, 6, 30, 9, 45),
            1,
            null,
            "delegate3"));

    List<ProjectDTO> result = handler.getNotExpiredProjects();

    assertEquals(projectDTOs, result);
  }

  @Test
  void getProject() throws ProjectNotFoundException {
    ConsultLawProjectsHandler handler = new ConsultLawProjectsHandler(projectRepository);
    ProjectDTO test =
        new ProjectDTO(
            1,
            "General",
            "Project1",
            "test",
            false,
            LocalDateTime.of(2023, 10, 15, 20, 15),
            1,
            null,
            "delegate1");
    ProjectDTO result = handler.getProject(1);

    assertEquals(test, result);
  }

  @Test
  void getProjectException() {
    ConsultLawProjectsHandler handler = new ConsultLawProjectsHandler(projectRepository);
    assertThrows(ProjectNotFoundException.class, () -> handler.getProject(0));
  }

  @BeforeEach
  public void loadNecessaryObjects() {

    List<LocalDateTime> expirationDates = new ArrayList<>();
    expirationDates.add(LocalDateTime.of(2023, 10, 15, 20, 15));
    expirationDates.add(LocalDateTime.of(2023, 9, 17, 12, 30));
    expirationDates.add(LocalDateTime.of(2023, 6, 30, 9, 45));

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
  }
}
