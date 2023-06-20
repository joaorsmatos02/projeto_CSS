package pt.ul.fc.css.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.hibernate.TypeMismatchException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import pt.ul.fc.css.example.demo.dtos.DelegateDTO;
import pt.ul.fc.css.example.demo.entities.Delegate;
import pt.ul.fc.css.example.demo.entities.Theme;
import pt.ul.fc.css.example.demo.entities.Voter;
import pt.ul.fc.css.example.demo.entities.VoterDelegation;
import pt.ul.fc.css.example.demo.exceptions.ThemeNotFoundException;
import pt.ul.fc.css.example.demo.exceptions.VoterNotFoundException;
import pt.ul.fc.css.example.demo.handlers.ChooseDelegateHandler;
import pt.ul.fc.css.example.demo.repositories.ThemeRepository;
import pt.ul.fc.css.example.demo.repositories.VoterDelegationRepository;
import pt.ul.fc.css.example.demo.repositories.VoterRepository;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ChooseDelegateHandlerTests {

  @Autowired private VoterDelegationRepository voterDelegationRepository;
  @Autowired private VoterRepository voterRepository;
  @Autowired private ThemeRepository themeRepository;

  @Test
  void chooseDelegate() throws VoterNotFoundException, ThemeNotFoundException {
    ChooseDelegateHandler handler =
        new ChooseDelegateHandler(voterDelegationRepository, voterRepository, themeRepository);

    List<DelegateDTO> delegates = handler.listDelegates();
    assertEquals(1, delegates.size());

    List<VoterDelegation> votersToDelegates = voterDelegationRepository.findAll();
    assertEquals(0, votersToDelegates.size());

    handler.chooseDelegate(1, "222", 1);

    List<VoterDelegation> votersToDelegatesResult = voterDelegationRepository.findAll();
    assertEquals(1, votersToDelegatesResult.size());
  }

  @Test
  void voterNotFound() {
    ChooseDelegateHandler handler =
        new ChooseDelegateHandler(voterDelegationRepository, voterRepository, themeRepository);

    assertThrows(VoterNotFoundException.class, () -> handler.chooseDelegate(1, "21", 1));
  }

  @Test
  void themeNotFound() {
    ChooseDelegateHandler handler =
        new ChooseDelegateHandler(voterDelegationRepository, voterRepository, themeRepository);

    assertThrows(ThemeNotFoundException.class, () -> handler.chooseDelegate(1, "222", 20));
  }

  @Test
  void typeMismatch() {
    ChooseDelegateHandler handler =
        new ChooseDelegateHandler(voterDelegationRepository, voterRepository, themeRepository);

    assertThrows(TypeMismatchException.class, () -> handler.chooseDelegate(2, "222", 1));
  }

  @Test
  void delegateNotFound() {
    ChooseDelegateHandler handler =
        new ChooseDelegateHandler(voterDelegationRepository, voterRepository, themeRepository);

    assertThrows(VoterNotFoundException.class, () -> handler.chooseDelegate(3, "222", 1));
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

    Voter voter1 = new Voter("222", "voter1");
    voterRepository.save(voter1);
  }
}
