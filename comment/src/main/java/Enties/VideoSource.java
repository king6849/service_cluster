package Enties;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author king
 */
@Getter
@Setter
public class VideoSource {

    private long id;
    private String title;
    private String videoUrl;
    private String pic;
    private long duration;
    private String author;
    private String authorAvatar;
    private java.util.Date publicTime;
    private long views;
    private long type;
    private String description;
    private String code;

    public VideoSource() {
    }

    public VideoSource(String title, String videoUrl, String pic, long duration, String author, String authorAvatar, Date publicTime, int type, String describe, String code) {
        this.title = title;
        this.videoUrl = videoUrl;
        this.pic = pic;
        this.duration = duration;
        this.author = author;
        this.authorAvatar = authorAvatar;
        this.publicTime = publicTime;
        this.type = type;
        this.description = describe;
        this.code = code;
    }



}
