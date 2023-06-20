package pt.ul.fc.css.example.demo.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.dtos.*;
import pt.ul.fc.css.example.demo.entities.*;
import pt.ul.fc.css.example.demo.exceptions.*;
import pt.ul.fc.css.example.demo.handlers.*;
import pt.ul.fc.css.example.demo.repositories.*;

@Component
public class Democracia2ServiceImpl implements Democracia2Service {

  @Autowired private ListPollsInCourseHandler listPollsInCourseHandler;
  @Autowired private PresentLawProjectHandler presentLawProjectHandler;
  @Autowired private ConsultLawProjectsHandler consultLawProjectsHandler;
  @Autowired private SupportLawProjectHandler supportLawProjectHandler;
  @Autowired private ChooseDelegateHandler chooseDelegateHandler;
  @Autowired private VotePollHandler votePollHandler;

  @Autowired private PollRepository pollRepository;
  @Autowired private ProjectRepository projectRepository;
  @Autowired private ThemeRepository themeRepository;
  @Autowired private VoterRepository voterRepository;
  @Autowired private VoterVoteRepository voterVoteRepository;
  @Autowired private VoterDelegationRepository voterDelegationRepository;

  public void populate() {
    if (pollRepository.findAll().size() == 0) {
      pollRepository.deleteAll();
      projectRepository.deleteAll();
      themeRepository.deleteAll();
      voterRepository.deleteAll();
      voterVoteRepository.deleteAll();
      voterDelegationRepository.deleteAll();

      List<LocalDateTime> expirationDates = new ArrayList<>();
      expirationDates.add(LocalDateTime.of(2023, 10, 15, 20, 15));
      expirationDates.add(LocalDateTime.of(2023, 2, 15, 20, 15));

      LocalDateTime now = LocalDateTime.now();
      LocalDateTime plusOne = now.plusMinutes(1);
      expirationDates.add(plusOne);

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

      List<Voter> voters = voterRepository.findAll();

      for (Voter v : voters) System.out.println(v.getCc() + " " + v.getName());

      Delegate delegate1 = new Delegate("111", "delegate1");
      voterRepository.save(delegate1);

      Delegate delegate2 = new Delegate("222", "delegate2");
      voterRepository.save(delegate2);

      Delegate delegate3 = new Delegate("333", "delegate3");
      voterRepository.save(delegate3);

      Project project1 =
          new Project(general, "Project1", "test", null, expirationDates.get(0), delegate1);
      projectRepository.save(project1);

      Project project2 =
          new Project(general, "Project2", "test2", null, expirationDates.get(0), delegate1);
      projectRepository.save(project2);
      Project project3 =
          new Project(general, "Project3", "test3", null, expirationDates.get(0), delegate1);
      projectRepository.save(project3);
      Project project4 =
          new Project(general, "Project4", "test4", null, expirationDates.get(1), delegate1);
      projectRepository.save(project4);
      Project project5 =
          new Project(general, "Project5", "test5", null, expirationDates.get(2), delegate1);
      projectRepository.save(project5);
      project5.setNrSupporters(9999);
      Project project6 =
          new Project(general, "Project6", "test6", null, expirationDates.get(0), delegate1);
      project6.setNrSupporters(10000);
      project6.setExpired(true);
      projectRepository.save(project6);

      Poll poll1 = new Poll(project6, true);
      pollRepository.save(poll1);

      Voter voter1 = new Voter("12345", "Diogo");
      voterRepository.save(voter1);
      Voter voter2 = new Voter("12344", "Joao");
      voterRepository.save(voter2);
      Voter voter3 = new Voter("12343", "Tomas");
      voterRepository.save(voter3);

      Delegate delegate4 = new Delegate("444", "delegate4");
      voterRepository.save(delegate4);

      VoterDelegation vcd1 = new VoterDelegation(delegate1, voter1, general);
      voterDelegationRepository.save(vcd1);
      VoterDelegation vcd2 = new VoterDelegation(delegate3, voter2, general);
      voterDelegationRepository.save(vcd2);
      VoterDelegation vcd3 = new VoterDelegation(delegate2, voter3, general);
      voterDelegationRepository.save(vcd3);

      VoterVote vvp = new VoterVote(delegate1, poll1, true);
      voterVoteRepository.save(vvp);

      VoterVote vvp2 = new VoterVote(delegate3, poll1, false);
      voterVoteRepository.save(vvp2);

      poll1.vote(false);
      pollRepository.save(poll1);
    }
  }

  // listPollsInCourseHandler
  @Override
  public List<PollDTO> listPollsInCourse() {
    return listPollsInCourseHandler.listPollsInCourse();
  }

  // PresentLawProjectsHandler
  @Override
  public void presentLawProject(
      long themeId,
      String title,
      String description,
      byte[] pdf,
      LocalDateTime expirationDateTime,
      String delegateCC)
      throws VoterNotFoundException, ThemeNotFoundException, ParameterException {
    presentLawProjectHandler.presentLawProject(
        themeId, title, description, pdf, expirationDateTime, delegateCC);
  }

  @Override
  public List<ThemeDTO> getThemes() {
    return presentLawProjectHandler.listThemes();
  }

  // ConsultLawProjectsHandler
  @Override
  public List<ProjectDTO> getNotExpiredProjects() {
    return consultLawProjectsHandler.getNotExpiredProjects();
  }

  @Override
  public ProjectDTO getProject(long projectId) throws ProjectNotFoundException {
    return consultLawProjectsHandler.getProject(projectId);
  }

  // SupportLawProjectsHandler
  @Override
  public void supportLawProject(String voterCC, long projectId)
      throws ProjectNotFoundException, VoterNotFoundException, PollAlreadyExistsException,
          ParameterException, VoterAlreadySupportsException {
    supportLawProjectHandler.supportLawProject(voterCC, projectId);
  }

  public List<ProjectDTO> getNotExpiredProjectsToSupp() {
    return supportLawProjectHandler.getNotExpiredProjects();
  }

  // ChooseDelegateHandler
  @Override
  public List<DelegateDTO> listDelegates() {
    return chooseDelegateHandler.listDelegates();
  }

  @Override
  public List<ThemeDTO> listThemes() {
    return chooseDelegateHandler.listThemes();
  }

  @Override
  public void chooseDelegate(long delegateId, String voterCC, long themeId)
      throws VoterNotFoundException, ThemeNotFoundException {
    chooseDelegateHandler.chooseDelegate(delegateId, voterCC, themeId);
  }

  // VotePollHandler
  @Override
  public List<PollDTO> listPollsInCourseToVote() {
    return votePollHandler.listPollsInCourse();
  }

  @Override
  public VoteDTO getDefaultVote(String voterCC, long pollId)
      throws VoterNotFoundException, VoteNotFoundException, PollNotFoundException,
          DelegateNotFoundException {
    return votePollHandler.getDefaultVote(voterCC, pollId);
  }

  @Override
  public void confirmDefaultVote(String voterCC, long pollId, boolean defaultVote)
      throws VoteFoundException {
    votePollHandler.confirmDefaultVote(voterCC, pollId, defaultVote);
  }

  @Override
  public void vote(boolean vote, String voterCC, long pollId)
      throws VoterNotFoundException, VoteFoundException {
    votePollHandler.vote(vote, voterCC, pollId);
  }

  public void createUser(String voterCC, String type, String name) throws VoterFoundException {
    Optional<Voter> voter = voterRepository.findByCC(voterCC);
    if (voter.isPresent()) {
      throw new VoterFoundException("Already Exists");
    }

    if (type.equals("Delegado")) {
      voterRepository.save(new Delegate(voterCC, name));
    } else voterRepository.save(new Voter(voterCC, name));
  }

  public void login(String voterCC) throws VoterFoundException {
    Optional<Voter> voter = voterRepository.findByCC(voterCC);
    if (voter.isPresent()) {
      throw new VoterFoundException("Already Exists");
    }
  }
}
