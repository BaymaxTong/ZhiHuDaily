package com.rutong.zhihudaily.model;

/**
 * Created by baymax on 2016/1/25.
 */
public class TopStories {
    private String id;
    private String title;
    private String ga_prefix;
    private String image;
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "TopStories{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", ga_prefix='" + ga_prefix + '\'' +
                ", image='" + image + '\'' +
                ", type=" + type +
                '}';
    }
}
