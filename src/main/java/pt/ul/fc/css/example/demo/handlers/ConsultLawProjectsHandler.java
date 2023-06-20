package pt.ul.fc.css.example.demo.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.dtos.ProjectDTO;
import pt.ul.fc.css.example.demo.entities.Project;
import pt.ul.fc.css.example.demo.exceptions.ProjectNotFoundException;
import pt.ul.fc.css.example.demo.repositories.ProjectRepository;

@Component
public class ConsultLawProjectsHandler {

  private ProjectRepository projectRepository;

  public ConsultLawProjectsHandler(ProjectRepository projectRepository) {
    this.projectRepository = projectRepository;
  }

  public List<ProjectDTO> getNotExpiredProjects() {

    List<ProjectDTO> result = new ArrayList<>();

    List<Project> notExpiredProjects = projectRepository.findAllNotExpired();

    for (Project p : notExpiredProjects)
      result.add(new ProjectDTO(p.getId(), p.getTheme().getTheme(), p.getTitle(), p.getDescription(),
                                p.isExpired(), p.getExpirationDate(), p.getNrSupporters(), p.getPdf(), p.getDelegate().getName()));

    return result;
  }

  public ProjectDTO getProject(long projectId) throws ProjectNotFoundException {

    Optional<Project> targetProject = projectRepository.findById(projectId);

    if (!targetProject.isPresent()) throw new ProjectNotFoundException("Project Not Found");

    return new ProjectDTO(
        targetProject.get().getId(),
        targetProject.get().getTheme().getTheme(),
        targetProject.get().getTitle(),
        targetProject.get().getDescription(),
        targetProject.get().isExpired(),
        targetProject.get().getExpirationDate(),
        targetProject.get().getNrSupporters(),
        targetProject.get().getPdf(),
        targetProject.get().getDelegate().getName());
  }
}
