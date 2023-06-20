package pt.ul.fc.css.example.demo.entities;

import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
public class Delegate extends Voter {

  @OneToMany(mappedBy = "delegate")
  private Set<VoterDelegation> voterDelegation;

  @OneToMany(mappedBy = "delegate")
  private List<Project> ownedProjects;

  protected Delegate() {};

  public Delegate(String cc, String name) {
    super(cc, name);
  }

  public void addProject(Project newProject) {
    this.ownedProjects.add(newProject);
  }

  public Set<VoterDelegation> getVoterChoosesDelegate() {
    return voterDelegation;
  }

  public void setVoterChoosesDelegate(Set<VoterDelegation> voterDelegation) {
    this.voterDelegation = voterDelegation;
  }

  public List<Project> getOwnedProjects() {
    return ownedProjects;
  }

  public void setOwnedProjects(List<Project> ownedProjects) {
    this.ownedProjects = ownedProjects;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Delegate delegate = (Delegate) o;
    return Objects.equals(voterDelegation, delegate.voterDelegation)
        && Objects.equals(ownedProjects, delegate.ownedProjects);
  }
}
