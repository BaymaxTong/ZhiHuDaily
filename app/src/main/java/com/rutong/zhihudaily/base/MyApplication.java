package com.rutong.zhihudaily.base;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.greendao.DaoMaster;
import com.example.greendao.DaoSession;
import com.rutong.zhihudaily.db.DateBaseInfo;

/**
 * Created by baymax on 2016/1/11.
 */
public class MyApplication extends Application{
    public static RequestQueue mQueues;
    private static Context mContext;
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;
    private static SQLiteDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();
        mQueues = Volley.newRequestQueue(getApplicationContext());
        mContext = this;
    }

    public static RequestQueue getVolleyQueue(){
        return mQueues;
    }

    public static DaoMaster getDaoMaster(Context context) {
        if (daoMaster == null) {
            DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context, DateBaseInfo.DB_NAME, null);
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    public static DaoSession getDaoSession(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(context);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

    public static SQLiteDatabase getDb() {
        return db;
    }

}
