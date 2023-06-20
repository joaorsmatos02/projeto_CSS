package pt.ul.fc.css.example.demo.handlers;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.dtos.ThemeDTO;
import pt.ul.fc.css.example.demo.entities.Delegate;
import pt.ul.fc.css.example.demo.entities.Project;
import pt.ul.fc.css.example.demo.entities.Theme;
import pt.ul.fc.css.example.demo.entities.Voter;
import pt.ul.fc.css.example.demo.exceptions.ParameterException;
import pt.ul.fc.css.example.demo.exceptions.ThemeNotFoundException;
import pt.ul.fc.css.example.demo.exceptions.VoterNotFoundException;
import pt.ul.fc.css.example.demo.repositories.ProjectRepository;
import pt.ul.fc.css.example.demo.repositories.ThemeRepository;
import pt.ul.fc.css.example.demo.repositories.VoterRepository;

@Component
public class PresentLawProjectHandler {

  private ProjectRepository projectRepository;

  private VoterRepository voterRepository;

  private ThemeRepository themeRepository;

  public PresentLawProjectHandler(
      ProjectRepository projectRepository,
      VoterRepository voterRepository,
      ThemeRepository themeRepository) {
    this.projectRepository = projectRepository;
    this.voterRepository = voterRepository;
    this.themeRepository = themeRepository;
  }

  public List<ThemeDTO> listThemes() {
    List<ThemeDTO> result = new ArrayList<>();

    List<Theme> themes = themeRepository.findAll();

    for (Theme t : themes) result.add(new ThemeDTO(t.getId(), t.getTheme()));

    return result;
  }

  public void presentLawProject(
      long themeId,
      String title,
      String description,
      byte[] pdf,
      LocalDateTime expirationDateTime,
      String delegateCC)
      throws ParameterException, VoterNotFoundException, ThemeNotFoundException {
    if (!isFilled(title)) throw new ParameterException("Invalid Title");

    if (!isFilled(description)) throw new ParameterException("Invalid Description");

    if (!isPDFAvailable(pdf)) throw new ParameterException("Invalid PDF");

    if (!isValidDate(expirationDateTime)) throw new ParameterException("Invalid Expiration Date");

    if (!isFilled(delegateCC)) throw new ParameterException("Invalid DelegateCC");

    Optional<Voter> delegate = voterRepository.findByCC(delegateCC);

    Optional<Theme> theme = themeRepository.findById(themeId);

    if (!delegate.isPresent() || !(delegate.get() instanceof Delegate))
      throw new VoterNotFoundException("Delegate Not Found");

    if (!theme.isPresent()) throw new ThemeNotFoundException("Theme not found");

    Project newProject =
        new Project(
            theme.get(), title, description, pdf, expirationDateTime, (Delegate) delegate.get());

    projectRepository.save(newProject);
  }

  private boolean isFilled(String param) {
    return param != null && param.length() != 0;
  }

  private boolean isPDFAvailable(byte[] pdf) {
    return pdf != null && pdf.length != 0;
  }

  private boolean isValidDate(LocalDateTime expirationDateTime) {
    if (expirationDateTime == null) return false;

    LocalDateTime from = LocalDateTime.from(LocalDateTime.now());
    long years = from.until(expirationDateTime, ChronoUnit.YEARS);

    if (years == 0) return true;

    return false;
  }
}
