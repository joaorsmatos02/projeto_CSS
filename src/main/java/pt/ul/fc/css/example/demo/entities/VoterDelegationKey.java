package pt.ul.fc.css.example.demo.entities;

import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class VoterDelegationKey implements Serializable {

  private long voterId;

  private long themeId;

  protected VoterDelegationKey() {}

  public VoterDelegationKey(long voterId, long themeId) {
    this.voterId = voterId;
    this.themeId = themeId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    VoterDelegationKey that = (VoterDelegationKey) o;
    return voterId == that.voterId && themeId == that.themeId;
  }
}
