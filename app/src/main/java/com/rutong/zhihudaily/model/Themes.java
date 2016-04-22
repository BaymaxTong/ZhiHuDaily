package com.rutong.zhihudaily.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baymax on 2016/1/21.
 */
public class Themes {

    private ArrayList<Stories> stories;
    private String color;
    private String description;
    private String name;
    private String background;
    private String image;
    private ArrayList<Editors> editors;
    private String image_source;

    public void setStories(ArrayList<Stories> stories) {
        this.stories = stories;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setEditors(ArrayList<Editors> editors) {
        this.editors = editors;
    }

    public void setImage_source(String image_source) {
        this.image_source = image_source;
    }

    public ArrayList<Stories> getStories() {
        return stories;
    }

    public String getColor() {
        return color;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getBackground() {
        return background;
    }

    public String getImage() {
        return image;
    }

    public ArrayList<Editors> getEditors() {
        return editors;
    }

    public String getImage_source() {
        return image_source;
    }
}
