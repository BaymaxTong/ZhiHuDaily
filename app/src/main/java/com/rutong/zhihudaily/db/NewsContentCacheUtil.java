package com.rutong.zhihudaily.db;

import android.content.Context;

import com.example.greendao.DaoSession;
import com.example.greendao.NewsContent;
import com.example.greendao.NewsContentDao;
import com.rutong.zhihudaily.base.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by baymax on 2016/4/24.
 */
public class NewsContentCacheUtil extends BaseCacheUtil{
    private static NewsContentCacheUtil instance;
    private static DaoSession mDaoSession;
    private static NewsContentDao mNewsContentDao;

    public NewsContentCacheUtil(){
    }

    public static NewsContentCacheUtil getInstance(Context context){
        if(instance == null){
            synchronized (NewsContentCacheUtil.class){
                if(instance == null){
                    instance = new NewsContentCacheUtil();
                }
            }
            mDaoSession = MyApplication.getDaoSession(context);
            mNewsContentDao = mDaoSession.getNewsContentDao();
        }
        return  instance;
    }
    /**
     * 清楚全部缓存
     */
    @Override
    public void clearAllCache() {
        mNewsContentDao.deleteAll();
    }
    /**
     * 添加NewsContent缓存
     *
     * @param content
     * @param id
     */
    public void addNewsContentCache(String content, String id) {

        NewsContent newsContent = new NewsContent();
        newsContent.setNewsId(id);
        newsContent.setContent(content);

        mNewsContentDao.insert(newsContent);
    }
    /**
     * 根据ID获取缓存数据
     *
     * @param id
     * @return
     */
    public NewsContent getCacheById(String id) {

        Query query  = mNewsContentDao.queryBuilder().where(NewsContentDao.Properties.NewsId.eq(id)).build();
        List<NewsContent> list = query.list();
        if (list.size() > 0) {
            try {
                return list.get(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new NewsContent();
    }
}
