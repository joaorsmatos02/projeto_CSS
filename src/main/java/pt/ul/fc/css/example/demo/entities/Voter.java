package pt.ul.fc.css.example.demo.entities;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Voter {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private long id;

  @Column(unique = true)
  @Nonnull
  private String cc;

  private String name;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
    name = "voter_supports_project",
    joinColumns = @JoinColumn(name = "voter_id"),
    inverseJoinColumns = @JoinColumn(name = "project_id")
  )
  private List<Project> supportedProjects;

  @OneToMany(mappedBy = "voter")
  private Set<VoterVote> votes;

  @OneToMany(mappedBy = "voter")
  private Set<VoterDelegation> voterDelegation;

  protected Voter() {};

  public Voter(String cc, String name) {
    this.cc = cc;
    this.name = name;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    id = id;
  }

  public String getCc() {
    return cc;
  }

  public void setCc(String cc) {
    this.cc = cc;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Project> getSupportedProjects() {
    return supportedProjects;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Voter voter = (Voter) o;
    return id == voter.id
        && Objects.equals(cc, voter.cc)
        && Objects.equals(name, voter.name)
        && Objects.equals(supportedProjects, voter.supportedProjects)
        && Objects.equals(votes, voter.votes)
        && Objects.equals(voterDelegation, voter.voterDelegation);
  }
}
