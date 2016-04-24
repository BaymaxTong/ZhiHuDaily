package com.rutong.zhihudaily.db;

import com.example.greendao.DaoSession;

import java.util.ArrayList;

/**
 * Created by baymax on 2016/4/24.
 */
public abstract class BaseCacheUtil<T> {

    protected static DaoSession mDaoSession;
    /**
     * 清楚全部缓存
     */
    public abstract void clearAllCache();

}
