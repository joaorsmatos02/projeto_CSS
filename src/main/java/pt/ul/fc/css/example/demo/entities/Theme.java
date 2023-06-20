package pt.ul.fc.css.example.demo.entities;

import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Theme {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private long id;

  private String theme;

  @ManyToOne(fetch = FetchType.LAZY)
  private Theme parent;

  @OneToMany(mappedBy = "parent")
  private List<Theme> children;

  @OneToMany(mappedBy = "theme")
  private List<VoterDelegation> voterDelegations;

  @OneToMany(mappedBy = "theme")
  private List<Project> projects;

  protected Theme() {}

  public Theme(String theme, Theme parent) {
    this.theme = theme;
    this.parent = parent;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    id = id;
  }

  public String getTheme() {
    return theme;
  }

  public void setTheme(String theme) {
    this.theme = theme;
  }

  public Theme getParent() {
    return parent;
  }

  public void setParent(Theme parent) {
    this.parent = parent;
  }

  public List<Theme> getChildren() {
    return children;
  }

  public void setChildren(List<Theme> children) {
    this.children = children;
  }

  public List<VoterDelegation> getVoterChoosesDelegates() {
    return voterDelegations;
  }

  public void setVoterChoosesDelegates(List<VoterDelegation> voterDelegations) {
    this.voterDelegations = voterDelegations;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Theme theme1 = (Theme) o;
    return id == theme1.id
        && Objects.equals(theme, theme1.theme)
        && Objects.equals(parent, theme1.parent)
        && Objects.equals(children, theme1.children)
        && Objects.equals(voterDelegations, theme1.voterDelegations)
        && Objects.equals(projects, theme1.projects);
  }
}
