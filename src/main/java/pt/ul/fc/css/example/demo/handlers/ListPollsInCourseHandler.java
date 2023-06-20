package pt.ul.fc.css.example.demo.handlers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.dtos.PollDTO;
import pt.ul.fc.css.example.demo.entities.Poll;
import pt.ul.fc.css.example.demo.repositories.PollRepository;

@Component
public class ListPollsInCourseHandler {

  private PollRepository pollRepository;

  public ListPollsInCourseHandler(PollRepository pollRepository) {
    this.pollRepository = pollRepository;
  }

  public List<PollDTO> listPollsInCourse() {

    List<Poll> pollsList = pollRepository.findAllInCourse();

    List<PollDTO> result = new ArrayList<>();

    for (Poll p : pollsList) {
      result.add(
          new PollDTO(
              p.getId(),
              p.getProject().getTheme().getTheme(),
              p.getProject().getTitle(),
              p.getProject().getDescription(),
              p.getProject().getExpirationDate()));
    }

    return result;
  }
}
