package pt.ul.fc.css.example.demo.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.hibernate.TypeMismatchException;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.dtos.DelegateDTO;
import pt.ul.fc.css.example.demo.dtos.ThemeDTO;
import pt.ul.fc.css.example.demo.entities.*;
import pt.ul.fc.css.example.demo.exceptions.ThemeNotFoundException;
import pt.ul.fc.css.example.demo.exceptions.VoterNotFoundException;
import pt.ul.fc.css.example.demo.repositories.ThemeRepository;
import pt.ul.fc.css.example.demo.repositories.VoterDelegationRepository;
import pt.ul.fc.css.example.demo.repositories.VoterRepository;

@Component
public class ChooseDelegateHandler {

  private VoterDelegationRepository voterDelegationRepository;
  private VoterRepository voterRepository;
  private ThemeRepository themeRepository;

  public ChooseDelegateHandler(
      VoterDelegationRepository voterDelegationRepository,
      VoterRepository voterRepository,
      ThemeRepository themeRepository) {
    this.voterDelegationRepository = voterDelegationRepository;
    this.voterRepository = voterRepository;
    this.themeRepository = themeRepository;
  }

  public List<DelegateDTO> listDelegates() {
    List<DelegateDTO> result = new ArrayList<>();

    List<Delegate> delegates = voterRepository.findAllDelegates();

    for (Delegate d : delegates) result.add(new DelegateDTO(d.getId(), d.getName()));

    return result;
  }

  public List<ThemeDTO> listThemes() {
    List<ThemeDTO> result = new ArrayList<>();

    List<Theme> themes = themeRepository.findAll();

    for (Theme t : themes) result.add(new ThemeDTO(t.getId(), t.getTheme()));

    return result;
  }

  public void chooseDelegate(long delegateId, String voterCC, long themeId)
      throws VoterNotFoundException, ThemeNotFoundException {
    Optional<Voter> delegate = voterRepository.findById(delegateId);

    Optional<Voter> voter = voterRepository.findByCC(voterCC);

    Optional<Theme> theme = themeRepository.findById(themeId);

    if (!delegate.isPresent() || !voter.isPresent())
      throw new VoterNotFoundException("Delegate or Voter doesnt exist");

    if (!theme.isPresent()) throw new ThemeNotFoundException("Theme not found");

    if (delegate.get() instanceof Delegate)
      voterDelegationRepository.save(
          new VoterDelegation((Delegate) delegate.get(), voter.get(), theme.get()));
    else {
      throw new TypeMismatchException("Delegate type Invalid");
    }
  }
}
