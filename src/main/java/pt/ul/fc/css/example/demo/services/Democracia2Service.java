package pt.ul.fc.css.example.demo.services;

import java.time.LocalDateTime;
import java.util.List;
import pt.ul.fc.css.example.demo.dtos.*;
import pt.ul.fc.css.example.demo.exceptions.*;

public interface Democracia2Service {

  // listPollsInCourseHandler
  public List<PollDTO> listPollsInCourse();

  // PresentLawProjectsHandler
  public void presentLawProject(
      long themeId,
      String title,
      String description,
      byte[] pdf,
      LocalDateTime expirationDateTime,
      String delegateCC)
      throws VoterNotFoundException, ThemeNotFoundException, ParameterException;

  public List<ThemeDTO> getThemes();

  // ConsultLawProjectsHandler
  public List<ProjectDTO> getNotExpiredProjects();

  public ProjectDTO getProject(long projectId) throws ProjectNotFoundException;

  // SupportLawProjectsHandler
  public void supportLawProject(String voterCC, long projectId)
      throws ProjectNotFoundException, VoterNotFoundException, PollAlreadyExistsException,
          ParameterException, VoterAlreadySupportsException;

  public List<ProjectDTO> getNotExpiredProjectsToSupp();

  // ChooseDelegateHandler
  public List<DelegateDTO> listDelegates();

  public List<ThemeDTO> listThemes();

  public void chooseDelegate(long delegateId, String voterCC, long themeId)
      throws VoterNotFoundException, ThemeNotFoundException;

  // VotePollHandler
  public List<PollDTO> listPollsInCourseToVote();

  public VoteDTO getDefaultVote(String voterCC, long pollId)
          throws VoterNotFoundException, VoteNotFoundException, PollNotFoundException, DelegateNotFoundException;

  public void confirmDefaultVote(String voterCC, long pollId, boolean defaultVote)
      throws VoteFoundException;

  public void vote(boolean vote, String voterCC, long pollId)
      throws VoterNotFoundException, VoteFoundException;
}
