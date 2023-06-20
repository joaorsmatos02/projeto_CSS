package pt.ul.fc.css.example.demo.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import pt.ul.fc.css.example.demo.exceptions.VoterAlreadySupportsException;

@Entity
public class Project {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private long id;

  @ManyToOne
  @JoinColumn(name = "theme_id", nullable = false)
  private Theme theme;

  private String title;

  private String description;

  @Lob private byte[] pdf;

  private long nrSupporters;

  @Column(name = "expiration_date", columnDefinition = "TIMESTAMP")
  private LocalDateTime expirationDate;

  @ManyToMany(mappedBy = "supportedProjects", fetch = FetchType.EAGER)
  private List<Voter> supporters;

  @OneToOne(mappedBy = "project")
  private Poll poll;

  @ManyToOne
  @JoinColumn(name = "delegate_id", nullable = false)
  private Delegate delegate;

  private boolean isExpired;

  protected Project() {
    this.supporters = new ArrayList<>();
    this.nrSupporters = 1;
    this.isExpired = false;
  }

  public Project(
      Theme theme,
      String title,
      String description,
      byte[] pdf,
      LocalDateTime expirationDate,
      Delegate delegate) {
    this.theme = theme;
    this.title = title;
    this.description = description;
    this.pdf = null;
    this.expirationDate = expirationDate;
    this.delegate = delegate;
    this.supporters = new ArrayList<>();
    this.nrSupporters = 1;
    this.isExpired = false;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Theme getTheme() {
    return theme;
  }

  public void setTheme(Theme theme) {
    this.theme = theme;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public byte[] getPdf() {
    return pdf;
  }

  public void setPdf(byte[] pdf) {
    this.pdf = pdf;
  }

  public long getNrSupporters() {
    return nrSupporters;
  }

  public void setNrSupporters(long nrSupporters) {
    this.nrSupporters = nrSupporters;
  }

  public List<Voter> getSupporters() {
    return supporters;
  }

  public LocalDateTime getExpirationDate() {
    return expirationDate;
  }

  public void setExpirationDate(LocalDateTime expirationDate) {
    this.expirationDate = expirationDate;
  }

  public Delegate getDelegate() {
    return delegate;
  }

  public void setDelegate(Delegate delegate) {
    this.delegate = delegate;
    this.supporters.add(delegate);
  }

  public boolean isExpired() {
    return isExpired;
  }

  public void setExpired(boolean expired) {
    isExpired = expired;
  }

  public void addSupporter(Voter voter) throws VoterAlreadySupportsException {
    if (this.supporters.contains(voter))
      throw new VoterAlreadySupportsException("This citizen already supports this project");

    this.supporters.add(voter);
    this.nrSupporters++;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Project project = (Project) o;
    return id == project.id
        && nrSupporters == project.nrSupporters
        && isExpired == project.isExpired
        && theme.getId() == project.theme.getId()
        && Objects.equals(title, project.title)
        && Objects.equals(description, project.description)
        && Arrays.equals(pdf, project.pdf)
        && Objects.equals(expirationDate, project.expirationDate)
        && delegate.getId() == project.delegate.getId();
  }
}
