package com.rutong.zhihudaily.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.rutong.zhihudaily.R;
import com.rutong.zhihudaily.model.Menu;
import com.rutong.zhihudaily.net.RequestImage;
import com.rutong.zhihudaily.net.RequestImageLoader;
import com.rutong.zhihudaily.net.RequestManager;
import com.rutong.zhihudaily.utils.NetWorkUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SplashActivity extends Activity {
    private ImageView iv_start;
    private TextView tv_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        iv_start = (ImageView) findViewById(R.id.iv_start);
        tv_start = (TextView) findViewById(R.id.tv_start);
        initImage();
    }

    private void initImage() {
        File dir = getFilesDir();
        final File imgFile = new File(dir, "splash.png");
        if(NetWorkUtil.isNetWorkConnected(this)){
            RequestImage.loadSplash(Menu.URL_Splash,iv_start,tv_start,imgFile);
        }else{
            Toast.makeText(this, "当前无网络连接，请连接网络！", Toast.LENGTH_SHORT).show();
            if (imgFile.exists()) {
                iv_start.setImageBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
                tv_start.setText("知乎日报");
            } else {
                iv_start.setImageResource(R.drawable.splash);
                tv_start.setText("知乎日报");
            }
        }
        final ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        scaleAnim.setFillAfter(true);
        scaleAnim.setDuration(3000);
        scaleAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        iv_start.startAnimation(scaleAnim);
    }

    private void startActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
        finish();
    }
}
