package pt.ul.fc.css.example.demo.dtos;

public class DelegateDTO {

  private long id;
  private String name;

  public DelegateDTO(long id, String name) {
    this.id = id;
    this.name = name;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
