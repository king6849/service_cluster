package com.sample;


public class Train {

  private long id;
  private java.sql.Date makeTime;
  private String members;
  private long cId;
  private long iId;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public java.sql.Date getMakeTime() {
    return makeTime;
  }

  public void setMakeTime(java.sql.Date makeTime) {
    this.makeTime = makeTime;
  }


  public String getMembers() {
    return members;
  }

  public void setMembers(String members) {
    this.members = members;
  }


  public long getCId() {
    return cId;
  }

  public void setCId(long cId) {
    this.cId = cId;
  }


  public long getIId() {
    return iId;
  }

  public void setIId(long iId) {
    this.iId = iId;
  }

}
