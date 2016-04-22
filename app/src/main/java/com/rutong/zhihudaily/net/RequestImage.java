package com.rutong.zhihudaily.net;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.rutong.zhihudaily.R;
import com.rutong.zhihudaily.model.Menu;
import com.rutong.zhihudaily.utils.CircleImageUtil;
import com.rutong.zhihudaily.utils.ImageUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;

/**
 * Created by baymax on 2016/1/22.
 */
public class RequestImage {

    //加载圆形头像
    public static void loadEditorAvater(String url,final ImageView imageView){
        RequestManager.addRequest(new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                imageView.setImageBitmap(CircleImageUtil.toRoundBitmap(bitmap));
            }
        }, 0, 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                imageView.setImageResource(R.drawable.ic_logo);
            }
        }),"avater");
    }

    //加载Splash图片
    public static void loadSplashImage(String url,final ImageView imageView, final File file){
        RequestManager.addRequest(new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
                ImageUtil.saveImage(file, ImageUtil.Bitmap2Bytes(bitmap));
            }
        }, 0, 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                imageView.setImageResource(R.drawable.splash);
            }
        }), "image");
    }
    //获取Splash资源信息
    public static void loadSplash(String url,final ImageView imageView, final TextView textView,final File file){
        RequestManager.addRequest(new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    String url = jsonObject.getString("img");
                    String text = jsonObject.getString("text");
                    loadSplashImage(url, imageView, file);
                    if(text != null || text != ""){
                        textView.setText(text);
                    }else {
                        textView.setText("知乎日报");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }), "splash");
    }
}
