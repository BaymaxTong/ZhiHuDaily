package com.rutong.zhihudaily.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by baymax on 2016/1/21.
 */
public class Stories implements Serializable {

    private String id;
    private String title;
    private ArrayList<String> images;
    private String type;

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Stories{" +
                "id=" + id +
                ", title='" + title + '\'' +
                /*", ga_prefix='" + ga_prefix + '\'' +*/
                ", images=" + images +
                ", type=" + type +
                '}';
    }
}
