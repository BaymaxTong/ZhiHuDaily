package com.rutong.zhihudaily.base;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by baymax on 2016/1/11.
 */
public class MyApplication extends Application{
    public static RequestQueue mQueues;

    @Override
    public void onCreate() {
        super.onCreate();
        mQueues = Volley.newRequestQueue(getApplicationContext());
    }

    public static RequestQueue getVolleyQueue(){
        return mQueues;
    }

}
