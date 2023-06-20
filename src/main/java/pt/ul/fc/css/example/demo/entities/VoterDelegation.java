package pt.ul.fc.css.example.demo.entities;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
public class VoterDelegation {

  @EmbeddedId private VoterDelegationKey id;

  @ManyToOne
  @MapsId("themeId")
  @JoinColumn(name = "theme_id", nullable = false)
  private Theme theme;

  @ManyToOne
  @MapsId("voterId")
  @JoinColumn(name = "voter_id", nullable = false)
  private Voter voter;

  @ManyToOne
  @JoinColumn(name = "delegate_id", nullable = false)
  private Delegate delegate;

  protected VoterDelegation() {};

  public VoterDelegation(Delegate delegate, Voter voter, Theme theme) {
    this.delegate = delegate;
    this.voter = voter;
    this.theme = theme;
    this.id = new VoterDelegationKey(voter.getId(), theme.getId());
  }

  public Voter getVoter() {
    return voter;
  }

  public void setVoter(Voter voter) {
    this.voter = voter;
  }

  public Delegate getDelegate() {
    return delegate;
  }

  public void setDelegate(Delegate delegate) {
    this.delegate = delegate;
  }

  public VoterDelegationKey getId() {
    return id;
  }

  public void setId(VoterDelegationKey id) {
    this.id = id;
  }

  public Theme getTheme() {
    return theme;
  }

  public void setTheme(Theme theme) {
    this.theme = theme;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    VoterDelegation that = (VoterDelegation) o;
    return Objects.equals(id, that.id)
        && Objects.equals(theme, that.theme)
        && Objects.equals(voter, that.voter)
        && Objects.equals(delegate, that.delegate);
  }
}
