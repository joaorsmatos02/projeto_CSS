package pt.ul.fc.css.example.demo.exceptions;

public class PollNotFoundException extends Exception {
  public PollNotFoundException(String errorMessage) {
    super(errorMessage);
  }
}
