package com.rutong.zhihudaily.model;

/**
 * Created by baymax on 2016/1/21.
 */
public class Editors {
    private String id;
    private String bio;
    private String name;
    private String avatar;
    private String url;

    public void setId(String id) {
        this.id = id;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public String getBio() {
        return bio;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getUrl() {
        return url;
    }
}
