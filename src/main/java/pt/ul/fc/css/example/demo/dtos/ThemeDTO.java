package pt.ul.fc.css.example.demo.dtos;

public class ThemeDTO {

  private long id;

  private String theme;

  public ThemeDTO(long id, String theme) {
    this.id = id;
    this.theme = theme;
  }

  public long getId() {
    return id;
  }

  public String getTheme() {
    return theme;
  }
}
