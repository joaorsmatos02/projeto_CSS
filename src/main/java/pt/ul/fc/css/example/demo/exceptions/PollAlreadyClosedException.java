package pt.ul.fc.css.example.demo.exceptions;

public class PollAlreadyClosedException extends Exception {
  public PollAlreadyClosedException(String errorMessage) {
    super(errorMessage);
  }
}
