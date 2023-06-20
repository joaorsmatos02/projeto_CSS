package com.example.desktopapp.dtos;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

public class ProjectDTO {

  private long id;
  private String theme;
  private String title;
  private String description;

  private boolean expired;

  private LocalDateTime expirationDate;

  private long nrSupporters;

  private byte[] pdf;

  private String delegateName;

  protected ProjectDTO() {}

  public ProjectDTO(
      long id,
      String theme,
      String title,
      String description,
      boolean expired,
      LocalDateTime expirationDate,
      long nrSupporters,
      byte[] pdf,
      String delegateName) {
    this.id = id;
    this.theme = theme;
    this.title = title;
    this.description = description;
    this.expired = expired;
    this.expirationDate = expirationDate;
    this.nrSupporters = nrSupporters;
    this.pdf = pdf;
    this.delegateName = delegateName;
  }

  public ProjectDTO(long id, String theme, String title) {
    this.id = id;
    this.theme = theme;
    this.title = title;
  }

  public long getId() {
    return id;
  }

  public String getTheme() {
    return theme;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public boolean isExpired() {
    return expired;
  }

  public LocalDateTime getExpirationDate() {
    return expirationDate;
  }

  public long getNrSupporters() {
    return nrSupporters;
  }

  public byte[] getPdf() {
    return pdf;
  }

  public String getDelegateName() {
    return delegateName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ProjectDTO that = (ProjectDTO) o;
    return id == that.id
        && expired == that.expired
        && nrSupporters == that.nrSupporters
        && Objects.equals(theme, that.theme)
        && Objects.equals(title, that.title)
        && Objects.equals(description, that.description)
        && Objects.equals(expirationDate, that.expirationDate)
        && Arrays.equals(pdf, that.pdf)
        && Objects.equals(delegateName, that.delegateName);
  }

  @Override
  public int hashCode() {
    int result =
        Objects.hash(
            id, theme, title, description, expired, expirationDate, nrSupporters, delegateName);
    result = 31 * result + Arrays.hashCode(pdf);
    return result;
  }
}
