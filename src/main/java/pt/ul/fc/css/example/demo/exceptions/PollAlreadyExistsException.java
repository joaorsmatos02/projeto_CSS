package pt.ul.fc.css.example.demo.exceptions;

public class PollAlreadyExistsException extends Exception {
  public PollAlreadyExistsException(String errorMessage) {
    super(errorMessage);
  }
}
