package com.king.bishe.chat.Pojo;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * @author king
 * @date 2020/11/10 22:32
 */
public class FilePojo implements Serializable {
    private MultipartFile file;
    private String name;
    private String password;

    public FilePojo() {
    }

    public FilePojo(MultipartFile file, String name, String password) {
        this.file = file;
        this.name = name;
        this.password = password;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "FilePojo{" +
                "file=" + file.getOriginalFilename() +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
