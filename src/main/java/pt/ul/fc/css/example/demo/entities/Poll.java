package pt.ul.fc.css.example.demo.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Set;

@Entity
public class Poll {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private long id;

  private boolean inCourse;

  private boolean isApproved;

  @OneToOne
  @JoinColumn(name = "project_id", referencedColumnName = "id")
  private Project project;

  @OneToMany(mappedBy = "poll", fetch = FetchType.EAGER)
  private Set<VoterVote> votes;

  private int positiveVotes;

  private int negativeVotes;

  @Column(name = "expiration_date", columnDefinition = "TIMESTAMP")
  private LocalDateTime expirationDate;

  protected Poll() {
    this.inCourse = false;
  }

  public Poll(Project project) {
    this.inCourse = true;
    this.isApproved = false;
    this.project = project;
    this.positiveVotes++;

    LocalDateTime now = LocalDateTime.now();

    long diffInDays = ChronoUnit.DAYS.between(now, this.project.getExpirationDate());

    if (Math.abs(diffInDays) < 15) {
      this.expirationDate = now.plusDays(15);
    } else if (Math.abs(diffInDays) > 60) {
      this.expirationDate = now.plusDays(60);
    } else this.expirationDate = this.project.getExpirationDate();
  }

  public Poll(Project project, boolean inCourse) {
    this.inCourse = inCourse;
    this.isApproved = false;
    this.project = project;
    this.positiveVotes++;

    LocalDateTime now = LocalDateTime.now();

    long diffInDays = ChronoUnit.DAYS.between(now, this.project.getExpirationDate());

    if (Math.abs(diffInDays) < 15) {
      this.expirationDate = now.plusDays(15);
    } else if (Math.abs(diffInDays) > 60) {
      this.expirationDate = now.plusDays(60);
    } else this.expirationDate = this.project.getExpirationDate();
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public boolean isInCourse() {
    return inCourse;
  }

  public void setInCourse(boolean inCourse) {
    this.inCourse = inCourse;
  }

  public Project getProject() {
    return project;
  }

  public void setProject(Project project) {
    this.project = project;
  }

  public void vote(boolean defaultVote) {
    if (defaultVote) this.positiveVotes++;
    else this.negativeVotes++;
  }

  public LocalDateTime getExpirationDate() {
    return expirationDate;
  }

  public void setExpirationDate(LocalDateTime expirationDate) {
    this.expirationDate = expirationDate;
  }

  public Set<VoterVote> getVotes() {
    return votes;
  }

  public void setVotes(Set<VoterVote> votes) {
    this.votes = votes;
  }

  public int getPositiveVotes() {
    return positiveVotes;
  }

  public void setPositiveVotes(int positiveVotes) {
    this.positiveVotes = positiveVotes;
  }

  public int getNegativeVotes() {
    return negativeVotes;
  }

  public void setNegativeVotes(int negativeVotes) {
    this.negativeVotes = negativeVotes;
  }

  public boolean isApproved() {
    return isApproved;
  }

  public void setApproved(boolean approved) {
    isApproved = approved;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Poll poll = (Poll) o;
    return id == poll.id
        && inCourse == poll.inCourse
        && isApproved == poll.isApproved
        && positiveVotes == poll.positiveVotes
        && negativeVotes == poll.negativeVotes
        && Objects.equals(project, poll.project)
        && Objects.equals(votes, poll.votes)
        && Objects.equals(expirationDate, poll.expirationDate);
  }
}
