package pt.ul.fc.css.example.demo.exceptions;

public class VoterAlreadySupportsException extends Throwable {
  public VoterAlreadySupportsException(String errorMessage) {
    super(errorMessage);
  }
}
