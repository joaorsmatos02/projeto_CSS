package pt.ul.fc.css.example.demo.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.dtos.PollDTO;
import pt.ul.fc.css.example.demo.dtos.VoteDTO;
import pt.ul.fc.css.example.demo.entities.*;
import pt.ul.fc.css.example.demo.exceptions.*;
import pt.ul.fc.css.example.demo.repositories.PollRepository;
import pt.ul.fc.css.example.demo.repositories.VoterDelegationRepository;
import pt.ul.fc.css.example.demo.repositories.VoterRepository;
import pt.ul.fc.css.example.demo.repositories.VoterVoteRepository;

@Component
public class VotePollHandler {

  private VoterRepository voterRepository;
  private PollRepository pollRepository;
  private VoterVoteRepository voterVoteRepository;
  private VoterDelegationRepository voterDelegationRepository;

  public VotePollHandler(
      VoterRepository voterRepository,
      PollRepository pollRepository,
      VoterVoteRepository voterVoteRepository,
      VoterDelegationRepository voterDelegationRepository) {
    this.voterRepository = voterRepository;
    this.pollRepository = pollRepository;
    this.voterVoteRepository = voterVoteRepository;
    this.voterDelegationRepository = voterDelegationRepository;
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

  public VoteDTO getDefaultVote(String voterCC, long pollId)
          throws VoterNotFoundException, VoteNotFoundException, PollNotFoundException, DelegateNotFoundException {

    Optional<Poll> currentPoll = this.pollRepository.findById(pollId);

    if (!currentPoll.isPresent()) throw new PollNotFoundException("Poll not found");

    // BUSCAR TEMA DA POLL, NO SEU PROJETO
    Theme theme = currentPoll.get().getProject().getTheme();

    // COM CITIZENCC E TEMA, IR AO VOTERCHOOSESDELEGATEREPOSITORY E RETIRAMOS O DELEGATE
    Optional<Voter> voter = voterRepository.findByCC(voterCC);

    if (!voter.isPresent()) throw new VoterNotFoundException("Citizen not found");

    VoterDelegationKey voterDelegationKey =
        new VoterDelegationKey(voter.get().getId(), theme.getId());
    Optional<VoterDelegation> voterChoosesDelegate =
        voterDelegationRepository.findById(voterDelegationKey);

    if (!voterChoosesDelegate.isPresent())
      throw new DelegateNotFoundException("Citizen has no Delegate for this Poll`s theme");

    Delegate voterDelegate = voterChoosesDelegate.get().getDelegate();
    // VAMOS AO CITIZENVOTESPOLL E RETIRAMOS O VOTO DO DELEGATO DO CITIZEN

    VoterVoteKey voterVoteKey = new VoterVoteKey(voterDelegate.getId(), currentPoll.get().getId());
    Optional<VoterVote> voterVotesPoll = voterVoteRepository.findById(voterVoteKey);

    if (!voterVotesPoll.isPresent())
      throw new VoteNotFoundException("Your Delegate for this Poll`s theme didn`t vote yet");

    return new VoteDTO(
        voterDelegate.getId(), voterDelegate.getName(), voterVotesPoll.get().isApproved());
  }

  public void confirmDefaultVote(String voterCC, long pollId, boolean defaultVote)
      throws VoteFoundException {

    Optional<Voter> voter = voterRepository.findByCC(voterCC);

    Optional<Poll> currentPoll = this.pollRepository.findById(pollId);

    VoterVoteKey voterVoteKey = new VoterVoteKey(voter.get().getId(), currentPoll.get().getId());
    Optional<VoterVote> voterVotesPoll = voterVoteRepository.findById(voterVoteKey);

    if (voterVotesPoll.isPresent())
      throw new VoteFoundException("You have already voted in this Poll");

    voterVoteRepository.save(new VoterVote(voter.get(), currentPoll.get()));

    currentPoll.get().vote(defaultVote);

    pollRepository.save(currentPoll.get());
  }

  public void vote(boolean vote, String voterCC, long pollId)
      throws VoteFoundException, VoterNotFoundException {
    Optional<Voter> voter = voterRepository.findByCC(voterCC);

    if (!voter.isPresent()) throw new VoterNotFoundException("Citizen not found");

    Optional<Poll> currentPoll = this.pollRepository.findById(pollId);

    VoterVoteKey voterVoteKey = new VoterVoteKey(voter.get().getId(), pollId);
    Optional<VoterVote> voterVotesPoll = voterVoteRepository.findById(voterVoteKey);

    if (voterVotesPoll.isPresent())
      throw new VoteFoundException("You have already voted in this Poll");

    if (voter.get() instanceof Delegate) {
      voterVoteRepository.save(new VoterVote(voter.get(), currentPoll.get(), vote));
    } else {
      voterVoteRepository.save(new VoterVote(voter.get(), currentPoll.get()));
    }

    currentPoll.get().vote(vote);

    pollRepository.save(currentPoll.get());
  }

  public void selectPoll(long pollId) throws PollNotFoundException {

    Optional<Poll> poll = pollRepository.findById(pollId);

    if (!poll.isPresent()) throw new PollNotFoundException("Poll not found");
  }
}
