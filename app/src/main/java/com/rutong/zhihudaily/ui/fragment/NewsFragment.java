package com.rutong.zhihudaily.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.rutong.zhihudaily.R;
import com.rutong.zhihudaily.base.BaseRecyclerAdapter;
import com.rutong.zhihudaily.model.Menu;
import com.rutong.zhihudaily.model.News;
import com.rutong.zhihudaily.model.Stories;
import com.rutong.zhihudaily.model.Themes;
import com.rutong.zhihudaily.model.TopStories;
import com.rutong.zhihudaily.net.RequestImage;
import com.rutong.zhihudaily.net.RequestImageLoader;
import com.rutong.zhihudaily.net.RequestManager;
import com.rutong.zhihudaily.net.RequestNews;
import com.rutong.zhihudaily.ui.NewsContentActivity;
import com.rutong.zhihudaily.view.Kanner;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by baymax on 2015/12/7.
 */
public class NewsFragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private NewsAdapter newsAdapter;
    private Kanner kanner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        getActionBar().setTitle("首页");
        initView(rootView);
        loadFirst();
        return rootView;
    }

    private void setHeader(RecyclerView view) {
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.news_header, view, false);
        kanner = (Kanner) header.findViewById(R.id.kanner);
        kanner.setOnItemClickListener(new Kanner.OnItemClickListener() {
            @Override
            public void click(View v, TopStories stories) {
                //Toast.makeText(getActivity(), stories.getTitle(), Toast.LENGTH_SHORT).show();
                //点击跳转
                Intent intent = new Intent(getActivity(), NewsContentActivity.class);
                intent.putExtra("id", stories.getId()+"");
                startActivity(intent);
            }
        });
        newsAdapter.setHeaderView(header);
    }

    private void loadFirst() {
            RequestManager.addRequest(new RequestNews<News>(Menu.URL_Latest, News.class, new Response.Listener<News>() {
                @Override
                public void onResponse(News news) {
                    ArrayList<Stories> stories = news.getStories();
                    for(int i = 0;i<stories.size();i++) {
                        newsAdapter = new NewsAdapter();
                        recyclerView.setAdapter(newsAdapter);
                        newsAdapter.addDatas(stories);
                        setHeader(recyclerView);
                    }
                    ArrayList<TopStories> topStories = news.getTopStories();
                    kanner.setData(topStories);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            }), "News");
    }

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.list_news);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_news);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                loadFirst();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }
    //主页新闻  适配器
    private class NewsAdapter extends BaseRecyclerAdapter<Stories>{
        public static final int TYPE_TIME = 1;
        public static final int TYPE_NORMAL = 2;

        @Override
        public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
            if(viewType == 1){
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_time,parent,false);
                return new timeHolder(v);
            }
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item,parent,false);
            return new itemHolder(v);
        }

        @Override
        public void onBind(RecyclerView.ViewHolder viewHolder, int RealPosition, final Stories data) {
            if(viewHolder instanceof timeHolder){
                ((timeHolder)viewHolder).getTimeTextView().setText("今日热文");
                ((timeHolder)viewHolder).getTextView().setText(data.getTitle());
                RequestImageLoader.loadImage(data.getImages().get(0), ((timeHolder) viewHolder).getImageView());
                ((timeHolder) viewHolder).cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳转 webView
                        Intent intent = new Intent(getActivity(), NewsContentActivity.class);
                        intent.putExtra("id", data.getId()+"");
                        startActivity(intent);
                    }
                });
            }
            if(viewHolder instanceof itemHolder){
                ((itemHolder)viewHolder).getTextView().setText(data.getTitle());
                RequestImageLoader.loadImage(data.getImages().get(0), ((itemHolder) viewHolder).getImageView());
                ((itemHolder) viewHolder).linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳转 webView
                        Intent intent = new Intent(getActivity(), NewsContentActivity.class);
                        intent.putExtra("id", data.getId()+"");
                        startActivity(intent);
                    }
                });
            }
        }

        @Override
        public int getItemView(Stories data, int position) {
            if(position == 1){
                return TYPE_TIME;
            }
            return TYPE_NORMAL;
        }

        //没有时间的布局
        public class itemHolder extends BaseRecyclerAdapter.Holder{
            private TextView textView;
            private ImageView imageView;
            private LinearLayout linearLayout;
            public itemHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.tv_news_item);
                imageView = (ImageView) itemView.findViewById(R.id.iv_news_item);
                linearLayout = (LinearLayout) itemView.findViewById(R.id.news_item_layout);
            }
            public TextView getTextView(){
                return textView;
            }
            public ImageView getImageView(){
                return imageView;
            }
        }
        //有时间的布局
        public class timeHolder extends BaseRecyclerAdapter.Holder{
            private TextView time;
            private TextView textView;
            private ImageView imageView;
            private CardView cardView;
            public timeHolder(View itemView) {
                super(itemView);
                time = (TextView) itemView.findViewById(R.id.tv_news_time);
                textView = (TextView) itemView.findViewById(R.id.tv_news_item_time);
                imageView = (ImageView) itemView.findViewById(R.id.iv_news_item_time);
                cardView = (CardView) itemView.findViewById(R.id.news_item_time_layout);
            }
            public TextView getTimeTextView(){
                return time;
            }
            public TextView getTextView(){
                return textView;
            }
            public ImageView getImageView(){
                return imageView;
            }
        }
    }
}
