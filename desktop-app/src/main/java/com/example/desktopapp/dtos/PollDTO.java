package com.example.desktopapp.dtos;

import java.time.LocalDateTime;
import java.util.Objects;

public class PollDTO {

  private long id;

  private String theme;

  private String title;

  private String description;

  private LocalDateTime expirationDate;

  protected PollDTO() {}

  public PollDTO(
      long id, String theme, String title, String description, LocalDateTime expirationDate) {
    this.id = id;
    this.theme = theme;
    this.title = title;
    this.description = description;
    this.expirationDate = expirationDate;
  }

  public long getId() {
    return this.id;
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

  public LocalDateTime getExpirationDate() {
    return expirationDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PollDTO pollDTO = (PollDTO) o;
    return id == pollDTO.id
            && Objects.equals(theme, pollDTO.theme)
            && Objects.equals(title, pollDTO.title)
            && Objects.equals(description, pollDTO.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, theme, title, description, expirationDate);
  }
}
