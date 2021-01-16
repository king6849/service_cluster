package com.king.graduation.backend_system.Enties;


/**
 * @author king
 */
public class Video {

  private long id;
  private String title;
  private String videoUrl;
  private String img;
  private long duration;
  private String author;
  private String authorAvatar;
  private java.sql.Date publicTime;
  private long views;
  private String type;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getAuthorAvatar() {
    return authorAvatar;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setAuthorAvatar(String authorAvatar) {
    this.authorAvatar = authorAvatar;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }


  public String getVideoUrl() {
    return videoUrl;
  }

  public void setVideoUrl(String videoUrl) {
    this.videoUrl = videoUrl;
  }


  public String getImg() {
    return img;
  }

  public void setImg(String img) {
    this.img = img;
  }


  public long getDuration() {
    return duration;
  }

  public void setDuration(long duration) {
    this.duration = duration;
  }


  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }


  public java.sql.Date getPublicTime() {
    return publicTime;
  }

  public void setPublicTime(java.sql.Date publicTime) {
    this.publicTime = publicTime;
  }


  public long getViews() {
    return views;
  }

  public void setViews(long views) {
    this.views = views;
  }

}
