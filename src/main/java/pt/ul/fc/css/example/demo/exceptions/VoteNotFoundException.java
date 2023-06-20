package pt.ul.fc.css.example.demo.exceptions;

public class VoteNotFoundException extends Throwable {
  public VoteNotFoundException(String errorMessage) {
    super(errorMessage);
  }
}
