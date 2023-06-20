package pt.ul.fc.css.example.demo.exceptions;

public class VoterNotFoundException extends Exception {
  public VoterNotFoundException(String errorMessage) {
    super(errorMessage);
  }
}
