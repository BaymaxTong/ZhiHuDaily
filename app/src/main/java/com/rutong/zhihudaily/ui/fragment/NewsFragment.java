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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.rutong.zhihudaily.R;
import com.rutong.zhihudaily.base.BaseRecyclerAdapter;
import com.rutong.zhihudaily.model.Before;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by baymax on 2015/12/7.
 */
public class NewsFragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private NewsAdapter newsAdapter;
    private Kanner kanner;
    private int data = 0;

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
                intent.putExtra("id", stories.getId() + "");
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
                    for (int i = 0; i < stories.size(); i++) {
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
                    //error
                }
            }), "News");
    }

    private void loadMore(String id) {
        RequestManager.addRequest(new RequestNews<Before>(Menu.URL_Before + id, Before.class, new Response.Listener<Before>() {
            @Override
            public void onResponse(Before before) {
                before.getStories();
                ArrayList<Stories> stories = before.getStories();
                for(int i = 0;i<stories.size();i++) {
//                    newsAdapter = new NewsAdapter();
//                    recyclerView.setAdapter(newsAdapter);
                    newsAdapter.addDatas(stories);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //error
            }
        }), "Before");
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
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        //用户在手指离开屏幕之前，由于滑了一下，视图仍然依靠惯性继续滑动
                        //刷新
                        break;
                    case RecyclerView.SCROLL_STATE_IDLE:
                        //视图已经停止滑动
//                    }
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        //手指没有离开屏幕，视图正在滑动
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visible = layoutManager.getChildCount();
                int total = layoutManager.getItemCount();
                int past = layoutManager.findFirstCompletelyVisibleItemPosition();
                if ((visible + past) >= total) {
                    //加载更多
                    loadMore(getdate(--data));
                }
            }
        });
    }

    public String getdate(int i) // //获取前后日期 i为正数 向后推迟i天，负数时向前提前i天
    {
        Date dat = null;
        Calendar cd = Calendar.getInstance();
        cd.add(Calendar.DATE, i);
        dat = cd.getTime();
        SimpleDateFormat dformat = new SimpleDateFormat("yyyyMMdd");
        String date = String.valueOf(dformat.format(dat));
        return date;
    }

    private ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }
    //主页新闻  适配器
    private class NewsAdapter extends BaseRecyclerAdapter<Stories>{
        public static final int TYPE_TIME = 1;
        public static final int TYPE_NORMAL = 2;
        private int lastPosition = -1;

        private void setAnimation(View viewToAnimate, int position) {
            if (position > lastPosition) {
                Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R
                        .anim.item_bottom_in);
                viewToAnimate.startAnimation(animation);
                lastPosition = position;
            }
        }

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
                        intent.putExtra("id", data.getId() + "");
                        startActivity(intent);
                    }
                });
                //setAnimation(((timeHolder) viewHolder).cardView, RealPosition);
            }
            if(viewHolder instanceof itemHolder){
                ((itemHolder)viewHolder).getTextView().setText(data.getTitle());
                RequestImageLoader.loadImage(data.getImages().get(0), ((itemHolder) viewHolder).getImageView());
                ((itemHolder) viewHolder).linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳转 webView
                        Intent intent = new Intent(getActivity(), NewsContentActivity.class);
                        intent.putExtra("id", data.getId() + "");
                        startActivity(intent);
                    }
                });
                //setAnimation(((itemHolder) viewHolder).linearLayout, RealPosition);
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
            //用于处理多次点击造成的网络访问
            private boolean isClickFinish;

            public itemHolder(View itemView) {
                super(itemView);
                isClickFinish = true;
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
            //用于处理多次点击造成的网络访问
            private boolean isClickFinish;

            public timeHolder(View itemView) {
                super(itemView);
                isClickFinish = true;
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
