package pt.ul.fc.css.example.demo.handlers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.demo.dtos.PollDTO;
import pt.ul.fc.css.example.demo.entities.*;
import pt.ul.fc.css.example.demo.exceptions.*;
import pt.ul.fc.css.example.demo.repositories.PollRepository;
import pt.ul.fc.css.example.demo.repositories.VoterDelegationRepository;
import pt.ul.fc.css.example.demo.repositories.VoterRepository;
import pt.ul.fc.css.example.demo.repositories.VoterVoteRepository;

@Component
public class ClosePollHandler {

  private PollRepository pollRepository;
  private VoterRepository voterRepository;
  private VoterVoteRepository voterVoteRepository;
  private VoterDelegationRepository voterDelegationRepository;

  public ClosePollHandler(
      PollRepository pollRepository,
      VoterRepository voterRepository,
      VoterVoteRepository voterVoteRepository,
      VoterDelegationRepository voterDelegationRepository) {
    this.pollRepository = pollRepository;
    this.voterRepository = voterRepository;
    this.voterVoteRepository = voterVoteRepository;
    this.voterDelegationRepository = voterDelegationRepository;
  }

  public List<PollDTO> getPolls() {

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

  @Scheduled(fixedRate = 10000) //10 secs
  public void closePoll()
      throws VoteNotFoundException {

    List<Poll> allPollsInCourse = pollRepository.findAllInCourse();

    LocalDateTime now = LocalDateTime.now();

    for (Poll poll : allPollsInCourse) {

      if (poll.getExpirationDate().isBefore(now)) {
        poll.setInCourse(false);

        submitAllDefaultVotes(poll);

        if (poll.getPositiveVotes() > poll.getNegativeVotes()) poll.setApproved(true);

        pollRepository.save(poll);
      }
    }
  }

  private void submitAllDefaultVotes(Poll poll) throws VoteNotFoundException {
    List<Voter> allVoters = voterRepository.findAll();

    for (Voter voter : allVoters) {

      VoterVoteKey key = new VoterVoteKey(voter.getId(), poll.getId());
      Optional<VoterVote> voterVotesPoll = voterVoteRepository.findById(key);

      if (!voterVotesPoll.isPresent()) {

        Theme theme = poll.getProject().getTheme();

        VoterDelegationKey voterDelegationKey =
            new VoterDelegationKey(voter.getId(), theme.getId());
        Optional<VoterDelegation> voterChoosesDelegate =
            voterDelegationRepository.findById(voterDelegationKey);

        if (!voterChoosesDelegate.isPresent()) {

          while (!voterChoosesDelegate.isPresent() && theme.getParent() != null) {
            voterDelegationKey = new VoterDelegationKey(voter.getId(), theme.getParent().getId());
            voterChoosesDelegate = voterDelegationRepository.findById(voterDelegationKey);
            theme = theme.getParent();
          }

          if (voterChoosesDelegate.isPresent()) {
            Delegate delegate = voterChoosesDelegate.get().getDelegate();

            VoterVoteKey voterVoteKey = new VoterVoteKey(delegate.getId(), poll.getId());
            Optional<VoterVote> vote = voterVoteRepository.findById(voterVoteKey);

            if (!vote.isPresent()) throw new VoteNotFoundException("Delegate vote not found");

            if (vote.get().isApproved() != null) {
              boolean defaultVote = vote.get().isApproved();

              voterVoteRepository.save(new VoterVote(voter, poll));

              poll.vote(defaultVote);
            }
          }
        }
      }
    }
  }
}
