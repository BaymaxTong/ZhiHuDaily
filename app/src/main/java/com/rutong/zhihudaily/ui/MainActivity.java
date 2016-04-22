package com.rutong.zhihudaily.ui;

import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.rutong.zhihudaily.ui.fragment.NewsFragment;
import com.rutong.zhihudaily.R;
import com.rutong.zhihudaily.utils.NetWorkUtil;
/**
 * Created by baymax on 2015/12/7.
 */
public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkNetWork();
        initView();
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(true);
        mActionBar.setHomeButtonEnabled(true);

        mActionBarDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.app_name,R.string.app_name){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        replaceFragment(R.id.container_frame,new NewsFragment());
    }

    private void checkNetWork() {
        if(!NetWorkUtil.isNetWorkConnected(this)){
            Toast.makeText(this,"当前无网络连接，请连接网络！",Toast.LENGTH_SHORT).show();
        }
    }

    private void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    public void replaceFragment(int id_content, Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(id_content, fragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionBarDrawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void closeDrawer() {
        mDrawerLayout.closeDrawers();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mActionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
