package com.rutong.zhihudaily.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.rutong.zhihudaily.R;
import com.rutong.zhihudaily.base.BaseRecyclerAdapter;
import com.rutong.zhihudaily.model.Menu;
import com.rutong.zhihudaily.net.RequestManager;
import com.rutong.zhihudaily.net.RequestMenu;
import com.rutong.zhihudaily.ui.MainActivity;
import com.rutong.zhihudaily.utils.NetWorkUtil;

import java.util.ArrayList;


/**
 * Created by baymax on 2015/12/7.
 */
public class DrawerFragment extends Fragment{
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MenuAdapter menuAdapter;
    private MainActivity mainActivity;
    private LinearLayout login;
    private TextView tv_main,tv_favorites,tv_download;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_drawer, container, false);

        initView(rootView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        loadFirst();
        return rootView;
    }

    private void setHeader(RecyclerView view) {
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.menu_header, view, false);
        tv_main = (TextView) header.findViewById(R.id.tv_main);
        tv_favorites = (TextView) header.findViewById(R.id.tv_favorites);
        tv_download = (TextView) header.findViewById(R.id.tv_download);
        login = (LinearLayout) header.findViewById(R.id.login);
        tv_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.replaceFragment(R.id.container_frame,new NewsFragment());
                mainActivity.closeDrawer();
            }
        });
        menuAdapter.setHeaderView(header);
    }

    private void initView(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.list_menu);
    }

    private void loadFirst(){
        if(NetWorkUtil.isNetWorkConnected(getActivity())){
            RequestManager.addRequest(new RequestMenu(Menu.getURL_Themes(), new Response.Listener<ArrayList<Menu>>() {
                @Override
                public void onResponse(ArrayList<Menu> menus) {
                    for(int i = 0;i<menus.size();i++){
                        menuAdapter = new MenuAdapter();
                        recyclerView.setAdapter(menuAdapter);
                        menuAdapter.addDatas(menus);
                        setHeader(recyclerView);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            }), "Menu");
        }else{
            Toast.makeText(getActivity(), "当前无网络连接，请连接网络！", Toast.LENGTH_SHORT).show();
        }
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

    //  目录列表 适配器
    private class MenuAdapter extends BaseRecyclerAdapter<Menu>{
        public static final int TYPE_NORMAL = 1;

        @Override
        public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item,parent,false);
            return new myViewHolder(v);
        }

        @Override
        public void onBind(RecyclerView.ViewHolder viewHolder, int RealPosition, final Menu data) {
            if(viewHolder instanceof myViewHolder){
                ((myViewHolder)viewHolder).getTextView().setText(data.getName());
                ((myViewHolder)viewHolder).getImageView().setImageResource(data.getResourceID());
                ((myViewHolder)viewHolder).menu_LinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try{
                            //Fragment fragment = (Fragment) Class.forName(data.getFragment().getName()).newInstance();
                            ThemesFragment themesFragment = ThemesFragment.newInstance(data.getId());
                            mainActivity.replaceFragment(R.id.container_frame, themesFragment);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mainActivity.closeDrawer();
                    }
                });
            }
        }

        @Override
        public int getItemView(Menu data,int positon) {
            return TYPE_NORMAL;
        }

        public class myViewHolder extends BaseRecyclerAdapter.Holder{
            private TextView textView;
            private ImageView imageView;
            private LinearLayout menu_LinearLayout;
            public myViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.tv_menu_item);
                imageView = (ImageView) itemView.findViewById(R.id.iv_menu_item);
                menu_LinearLayout = (LinearLayout) itemView.findViewById(R.id.menu_item_layout);
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
