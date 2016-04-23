package com.rutong.zhihudaily.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.rutong.zhihudaily.R;
import com.rutong.zhihudaily.model.Menu;
import com.rutong.zhihudaily.model.NewsContent;
import com.rutong.zhihudaily.net.RequestManager;
import com.rutong.zhihudaily.net.RequestNews;

/**
 * Created by baymax on 2016/4/23.
 */
public class NewsContentActivity extends AppCompatActivity {
    private CoordinatorLayout coordinatorLayout;
    private WebView mWebView;
    private String storiesID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);

        storiesID = getIntent().getStringExtra("id");
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        coordinatorLayout.setVisibility(View.VISIBLE);
        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 开启DOM storage API 功能
        mWebView.getSettings().setDomStorageEnabled(true);
        // 开启database storage API功能
        mWebView.getSettings().setDatabaseEnabled(true);
        // 开启Application Cache功能
        mWebView.getSettings().setAppCacheEnabled(true);

        loadFirst(storiesID);
    }

    private void loadFirst(String id) {
        RequestManager.addRequest(new RequestNews<NewsContent>(Menu.URL_News+id, NewsContent.class, new Response.Listener<NewsContent>() {
            @Override
            public void onResponse(NewsContent newsContent) {
                //Toast.makeText(NewsContentActivity.this, newsContent.getTitle(), Toast.LENGTH_SHORT).show();
                String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/css/news.css\" type=\"text/css\">";
                String html = "<html><head>" + css + "</head><body>" + newsContent.getBody() + "</body></html>";
                html = html.replace("<div class=\"img-place-holder\">", "");
                //Log.d("html",html);
                mWebView.loadDataWithBaseURL("x-data://base", html, "text/html", "UTF-8", null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }), "NewsContent");
    }
}
