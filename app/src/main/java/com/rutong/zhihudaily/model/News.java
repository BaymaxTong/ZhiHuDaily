package com.rutong.zhihudaily.model;

import java.util.ArrayList;

/**
 * Created by baymax on 2016/1/25.
 */
public class News {
    private ArrayList<TopStories> top_stories;
    private ArrayList<Stories> stories;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<TopStories> getTopStories() {
        return top_stories;
    }

    public void setTopStories(ArrayList<TopStories> top_stories) {
        this.top_stories = top_stories;
    }

    public ArrayList<Stories> getStories() {
        return stories;
    }

    public void setStories(ArrayList<Stories> stories) {
        this.stories = stories;
    }

    @Override
    public String toString() {
        return "Latest{" +
                "top_stories=" + top_stories +
                ", stories=" + stories +
                ", date='" + date + '\'' +
                '}';
    }
}
