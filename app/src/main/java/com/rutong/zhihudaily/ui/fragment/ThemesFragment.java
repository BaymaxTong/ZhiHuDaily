package com.rutong.zhihudaily.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.rutong.zhihudaily.R;
import com.rutong.zhihudaily.base.BaseRecyclerAdapter;
import com.rutong.zhihudaily.model.Menu;
import com.rutong.zhihudaily.model.Stories;
import com.rutong.zhihudaily.model.Themes;
import com.rutong.zhihudaily.net.RequestImage;
import com.rutong.zhihudaily.net.RequestImageLoader;
import com.rutong.zhihudaily.net.RequestManager;
import com.rutong.zhihudaily.net.RequestNews;
import com.rutong.zhihudaily.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by baymax on 2015/12/7.
 */
public class ThemesFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ThemesAdapter themesAdapter;
    private MainActivity mainActivity;
    private LinearLayout editor_layout;
    private TextView tv_header;
    private ImageView iv_header;
    private String id;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_themes, container, false);
        if(getArguments() != null){
            id = getArguments().getString("id");
        }
        initView(rootView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        loadFirst(id);
        return rootView;
    }

    public static ThemesFragment newInstance(String str){
        ThemesFragment fragment = new ThemesFragment();
        Bundle args = new Bundle();
        args.putString("id", str);
        fragment.setArguments(args);
        return fragment;
    }

    private void initView(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.list_themes);
    }

    private void setHeader(RecyclerView view) {
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.themes_header, view, false);
        tv_header = (TextView) header.findViewById(R.id.tv_themes_header);
        iv_header = (ImageView) header.findViewById(R.id.iv_themes_header);
        editor_layout = (LinearLayout) header.findViewById(R.id.editor_layout);
        editor_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //editor
            }
        });
        themesAdapter.setHeaderView(header);
    }

    private void loadFirst(String id){
        RequestManager.addRequest(new RequestNews<Themes>(Menu.URL_Theme+id, Themes.class, new Response.Listener<Themes>() {
            @Override
            public void onResponse(Themes themes) {
                ArrayList<Stories> stories = themes.getStories();
                for(int i = 0;i<stories.size();i++) {
                    themesAdapter = new ThemesAdapter();
                    recyclerView.setAdapter(themesAdapter);
                    themesAdapter.addDatas(stories);
                    setHeader(recyclerView);
                }
                RequestImageLoader.loadImage(themes.getBackground(), iv_header);
                tv_header.setText(themes.getDescription());
                //add editor
                int editor = themes.getEditors().size();
                ImageView[] imageViews = new ImageView[editor];
                for (int i = 0; i < imageViews.length; i++) {
                    ImageView imageView = new ImageView(getActivity());
                    LinearLayout.LayoutParams  l= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    imageView.setLayoutParams(l);
                    imageView.setPadding(0, 10, 10, 0);
                    RequestImage.loadEditorAvater(themes.getEditors().get(i).getAvatar(), imageView);
                    editor_layout.addView(imageView);
                }
                getActionBar().setTitle(themes.getName());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }),"Themes");
    }

    private ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof MainActivity) {
            mainActivity = (MainActivity) activity;
        } else {
            throw new IllegalArgumentException("The activity must be a MainActivity !");
        }
    }
    //主题日报  适配器
    private class ThemesAdapter extends BaseRecyclerAdapter<Stories> {
        public static final int TYPE_NORMAL = 1;
        public static final int TYPE_PICTUTRE = 2;
        @Override
        public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
            if(viewType == 1){
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.themes_item,parent,false);
                return new itemHolder(v);
            }
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.themes_item_picture,parent,false);
            return new myViewHolder(v);
        }

        @Override
        public void onBind(RecyclerView.ViewHolder viewHolder, int RealPosition, Stories data) {
            if(viewHolder instanceof myViewHolder){
                ((myViewHolder)viewHolder).getTextView().setText(data.getTitle());
                //((myViewHolder)viewHolder).getImageView().setImageResource(R.mipmap.ic_launcher);
                RequestImageLoader.loadImage(data.getImages().get(0), ((myViewHolder) viewHolder).getImageView());
                ((myViewHolder) viewHolder).item_RelativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳转 webView
                    }
                });
            }
            if(viewHolder instanceof itemHolder){
                ((itemHolder)viewHolder).getTextView().setText(data.getTitle());
                ((itemHolder)viewHolder).item_LinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳转 webView
                    }
                });
            }
        }

        @Override
        public int getItemView(Stories data, int position) {
            if(data.getImages()==null || data.getImages().size() == 0){
                return TYPE_NORMAL;
            }
            return TYPE_PICTUTRE;
        }

        //没有图片的布局
        public class itemHolder extends BaseRecyclerAdapter.Holder{
            private TextView textView;
            private LinearLayout item_LinearLayout;
            public itemHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.tv_themes_item);
                item_LinearLayout = (LinearLayout) itemView.findViewById(R.id.themes_item_layout);
            }
            public TextView getTextView(){
                return textView;
            }
        }
        //有图片的布局
        public class myViewHolder extends BaseRecyclerAdapter.Holder{
            private TextView textView;
            private ImageView imageView;
            private RelativeLayout item_RelativeLayout;
            public myViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.tv_themes_item_picture);
                imageView = (ImageView) itemView.findViewById(R.id.iv_themes_item_picture);
                item_RelativeLayout = (RelativeLayout) itemView.findViewById(R.id.themes_picture_layout);
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
