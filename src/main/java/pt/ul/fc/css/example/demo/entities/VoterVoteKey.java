package pt.ul.fc.css.example.demo.entities;

import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class VoterVoteKey implements Serializable {

  private long voterId;

  private long pollId;

  protected VoterVoteKey() {}

  public VoterVoteKey(long voterId, long pollId) {
    this.voterId = voterId;
    this.pollId = pollId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    VoterVoteKey that = (VoterVoteKey) o;
    return voterId == that.voterId && pollId == that.pollId;
  }
}
