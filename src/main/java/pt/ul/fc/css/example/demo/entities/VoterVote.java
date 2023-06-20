package pt.ul.fc.css.example.demo.entities;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
public class VoterVote {

  @EmbeddedId private VoterVoteKey id;

  @ManyToOne
  @MapsId("voterId")
  @JoinColumn(name = "voter_id", nullable = false)
  private Voter voter;

  @ManyToOne
  @MapsId("pollId")
  @JoinColumn(name = "poll_id", nullable = false)
  private Poll poll;

  private Boolean isApproved;

  protected VoterVote() {}

  public VoterVote(Voter voter, Poll poll, boolean isApproved) {
    this.voter = voter;
    this.poll = poll;
    this.isApproved = isApproved;
    this.id = new VoterVoteKey(voter.getId(), poll.getId());
  }

  public VoterVote(Voter voter, Poll poll) {
    this.voter = voter;
    this.poll = poll;
    this.id = new VoterVoteKey(voter.getId(), poll.getId());
  }

  public VoterVoteKey getId() {
    return id;
  }

  public void setId(VoterVoteKey id) {
    this.id = id;
  }

  public Voter getCitizen() {
    return voter;
  }

  public void setCitizen(Voter voter) {
    this.voter = voter;
  }

  public Poll getPoll() {
    return poll;
  }

  public void setPoll(Poll poll) {
    this.poll = poll;
  }

  public Boolean isApproved() {
    return isApproved;
  }

  public void setApproved(Boolean approved) {
    isApproved = approved;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    VoterVote voterVote = (VoterVote) o;
    return Objects.equals(id, voterVote.id)
        && Objects.equals(voter, voterVote.voter)
        && Objects.equals(poll, voterVote.poll)
        && Objects.equals(isApproved, voterVote.isApproved);
  }
}
