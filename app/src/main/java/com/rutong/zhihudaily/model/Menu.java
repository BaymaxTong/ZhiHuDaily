package com.rutong.zhihudaily.model;

import android.support.v4.app.Fragment;

import com.rutong.zhihudaily.R;
import com.rutong.zhihudaily.ui.fragment.ThemesFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by baymax on 2016/1/8.
 */
public class Menu implements Serializable{

    public static final String URL_Splash = "http://news-at.zhihu.com/api/4/start-image/1080*1776";
    public static final String URL_Latest = "http://news-at.zhihu.com/api/4/news/latest";
    public static final String URL_Themes = "http://news-at.zhihu.com/api/4/themes";
    public static final String URL_Theme = "http://news-at.zhihu.com/api/4/theme/";    //remember add id
    public static final String URL_News = "http://news-at.zhihu.com/api/4/news/";      //remember add id

    //该主题日报的编号
    private String id;
    //主题日报的介绍
    private String description;
    //供显示的图片地址
    private String thumbnail;
    // 供显示的主题日报名称
    private String name;
    //内部资源图片 标志是否被收藏
    private int resourceID;

    private Class<? extends Fragment> fragment;

    public Menu(){
    }

    public Menu(String name, int resourceID, Class<? extends Fragment> fragment){
        this.name = name;
        this.resourceID = resourceID;
        this.fragment = fragment;
    }

    public Menu(String id, String description, String thumbnail,String name, int resourceID, Class<? extends Fragment> fragment){
        this.id = id;
        this.description = description;
        this.thumbnail = thumbnail;
        this.name = name;
        this.resourceID = resourceID;
        this.fragment = fragment;
    }

    public static ArrayList<Menu> parse(JSONArray postArray){

        ArrayList<Menu> menus = new ArrayList<>();

        for(int i = 0;i < postArray.length();i++){
            Menu menu = new Menu();
            JSONObject jsonObject = postArray.optJSONObject(i);
            menu.setResourceID(R.drawable.ic_menu_follow);
            menu.setFragment(ThemesFragment.class);
            menu.setDescription(jsonObject.optString("description"));
            menu.setId(jsonObject.optString("id"));
            menu.setName(jsonObject.optString("name"));
            menu.setThumbnail(jsonObject.optString("thumbnail"));

            menus.add(menu);
        }
        return menus;
    }

    @Override
    public String toString() {
        return "Menu{"+
                "thumbnail"+thumbnail +
                ", description"+description +
                ", id"+id +
                ", name"+name +
                "}";
    }

    public static String getURL_Themes() {
        return URL_Themes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Class<? extends Fragment> getFragment() {
        return fragment;
    }

    public void setFragment(Class<? extends Fragment> fragment) {
        this.fragment = fragment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResourceID() {
        return resourceID;
    }

    public void setResourceID(int resourceID) {
        this.resourceID = resourceID;
    }
}
