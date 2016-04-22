package com.rutong.zhihudaily.net;


import com.android.volley.Request;
import com.rutong.zhihudaily.base.MyApplication;

/**
 * Created by baymax on 2016/1/13.
 */
public class RequestManager {

    private RequestManager(){
    }

    public static void addRequest(Request<?> request, Object obj){
        if(obj != null){
            request.setTag(obj);
        }
        MyApplication.getVolleyQueue().add(request);
    }

    public static void cancelRequest(Object obj){
        MyApplication.getVolleyQueue().cancelAll(obj);
    }

}
