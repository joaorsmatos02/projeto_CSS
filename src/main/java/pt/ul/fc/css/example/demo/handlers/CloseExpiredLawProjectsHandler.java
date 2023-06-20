package pt.ul.fc.css.example.demo.handlers;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.entities.Project;
import pt.ul.fc.css.example.demo.repositories.ProjectRepository;

@Component
public class CloseExpiredLawProjectsHandler {

  private ProjectRepository projectRepository;

  public CloseExpiredLawProjectsHandler(ProjectRepository projectRepository) {
    this.projectRepository = projectRepository;
  }

  @Scheduled(fixedRate = 10000) //10 secs
  public void closeExpiredLawProjects() {
    List<Project> projectsNotExpired = projectRepository.findAllNotExpired();

    if (projectsNotExpired.size() == 0) return;
    LocalDateTime now = LocalDateTime.now();

    for (Project p : projectsNotExpired) {
      if (p.getExpirationDate().isBefore(now)){
        p.setExpired(true);
        projectRepository.save(p);
      }

    }
  }
}
