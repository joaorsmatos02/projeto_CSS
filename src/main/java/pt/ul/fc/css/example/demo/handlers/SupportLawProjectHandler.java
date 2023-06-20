package pt.ul.fc.css.example.demo.handlers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.dtos.ProjectDTO;
import pt.ul.fc.css.example.demo.entities.Poll;
import pt.ul.fc.css.example.demo.entities.Project;
import pt.ul.fc.css.example.demo.entities.Voter;
import pt.ul.fc.css.example.demo.entities.VoterVote;
import pt.ul.fc.css.example.demo.exceptions.*;
import pt.ul.fc.css.example.demo.repositories.PollRepository;
import pt.ul.fc.css.example.demo.repositories.ProjectRepository;
import pt.ul.fc.css.example.demo.repositories.VoterRepository;
import pt.ul.fc.css.example.demo.repositories.VoterVoteRepository;

@Component
public class SupportLawProjectHandler {

  private VoterRepository voterRepository;
  private ProjectRepository projectRepository;
  private PollRepository pollRepository;

  private VoterVoteRepository voterVoteRepository;

  private final int MIN_SUPPORTERS_TO_CREATE_POLL = 10000;

  public SupportLawProjectHandler(
      VoterRepository voterRepository,
      ProjectRepository projectRepository,
      PollRepository pollRepository,
      VoterVoteRepository voterVoteRepository) {
    this.voterRepository = voterRepository;
    this.projectRepository = projectRepository;
    this.pollRepository = pollRepository;
    this.voterVoteRepository = voterVoteRepository;
  }

  public List<ProjectDTO> getNotExpiredProjects() {

    List<ProjectDTO> result = new ArrayList<>();

    List<Project> notExpiredProjects = projectRepository.findAllNotExpired();

    for (Project p : notExpiredProjects)
      result.add(new ProjectDTO(p.getId(), p.getTheme().getTheme(), p.getTitle()));

    return result;
  }

  public void supportLawProject(String voterCC, long projectId)
      throws ParameterException, VoterNotFoundException, ProjectNotFoundException,
          PollAlreadyExistsException, VoterAlreadySupportsException {

    if (voterCC.length() == 0) throw new ParameterException("Invalid voter cc");

    Optional<Voter> voter = voterRepository.findByCC(voterCC);

    if (!voter.isPresent()) throw new VoterNotFoundException("Voter Not Found");

    Optional<Project> project = projectRepository.findById(projectId);

    if (!project.isPresent()) throw new ProjectNotFoundException("Project Not Found");

    if (project.get().getNrSupporters() >= MIN_SUPPORTERS_TO_CREATE_POLL || project.get().getExpirationDate().isBefore(LocalDateTime.now()))
      throw new PollAlreadyExistsException("This project is already a poll");

    project.get().addSupporter(voter.get());
    voter.get().getSupportedProjects().add(project.get());


    if (project.get().getNrSupporters() >= MIN_SUPPORTERS_TO_CREATE_POLL) {
      Poll poll = new Poll(project.get());
      pollRepository.save(poll);

      VoterVote voterVote = new VoterVote(project.get().getDelegate(), poll, true);
      voterVoteRepository.save(voterVote);
    }

    voterRepository.save(voter.get());
    projectRepository.save(project.get());
  }
}
