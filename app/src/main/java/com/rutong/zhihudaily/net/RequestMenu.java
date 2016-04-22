package com.rutong.zhihudaily.net;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.rutong.zhihudaily.model.Menu;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 获取目录列表
 * Created by baymax on 2016/1/13.
 */
public class RequestMenu extends Request<ArrayList<Menu>>{

    private Response.Listener<ArrayList<Menu>> listener;

    public RequestMenu(String url, Response.Listener<ArrayList<Menu>> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = listener;
    }


    @Override
    protected Response<ArrayList<Menu>> parseNetworkResponse(NetworkResponse networkResponse) {
        try{
            /**
             * 得到返回的数据
             */
            String resultStr = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
            JSONObject resultObj = new JSONObject(resultStr);
            JSONArray postArray = resultObj.optJSONArray("others");
            /**
             * 转化成对象
             */
            return Response.success(Menu.parse(postArray), HttpHeaderParser.parseCacheHeaders(networkResponse));
        }catch (Exception e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(ArrayList<Menu> menus) {
        listener.onResponse(menus);
    }
}
