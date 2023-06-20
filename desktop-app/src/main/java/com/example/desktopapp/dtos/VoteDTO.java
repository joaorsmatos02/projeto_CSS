package com.example.desktopapp.dtos;

public class VoteDTO {

  private boolean content;

  private long delegateId;

  private String delegateName;

  protected VoteDTO() {}

  public VoteDTO(long delegateId, String delegateName, boolean content) {
    this.delegateId = delegateId;
    this.delegateName = delegateName;
    this.content = content;
  }

  public boolean isContent() {
    return content;
  }

  public void setContent(boolean content) {
    this.content = content;
  }

  public long getDelegateId() {
    return delegateId;
  }

  public void setDelegateId(long delegateId) {
    this.delegateId = delegateId;
  }

  public String getDelegateName() {
    return delegateName;
  }

  public void setDelegateName(String delegateName) {
    this.delegateName = delegateName;
  }
}
