package com.rutong.zhihudaily.view;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.rutong.zhihudaily.R;
import com.rutong.zhihudaily.model.TopStories;

import java.util.ArrayList;
import java.util.List;

public class Kanner extends FrameLayout implements View.OnClickListener {
    private ArrayList<TopStories> topStories;
    private int count;
    private ImageLoader mImageLoader;
    private List<View> views;
    private Context context;
    private ViewPager vp;
    private boolean isAutoPlay;
    private int currentItem;
    private int delayTime;
    private LinearLayout ll_dot;
    private List<ImageView> iv_dots;
    private Handler handler = new Handler();
    private DisplayImageOptions options;
    private OnItemClickListener mItemClickListener;

    public Kanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initImageLoader(context);
        initData();
    }

    public Kanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Kanner(Context context) {
        this(context, null);
    }

    private void initData() {
        views = new ArrayList<View>();
        iv_dots = new ArrayList<ImageView>();
        delayTime = 2000;
    }

    public void setData(ArrayList<TopStories> topStories) {
        this.topStories = topStories;
        initLayout();
        initImgFromNet(topStories);
        showTime();
    }

    private void initLayout() {
        views.clear();
        View view = LayoutInflater.from(context).inflate(
                R.layout.kanner_layout, this, true);
        vp = (ViewPager) view.findViewById(R.id.vp);
        ll_dot = (LinearLayout) view.findViewById(R.id.ll_dot);
        ll_dot.removeAllViews();
    }

    private void initImgFromNet(ArrayList<TopStories> topStories) {
        count = topStories.size();
        for (int i = 0; i < count; i++) {
            ImageView iv_dot = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 5;
            params.rightMargin = 5;
            ll_dot.addView(iv_dot, params);
            iv_dots.add(iv_dot);
        }

        for (int i = 0; i <= count + 1; i++) {
            View fm = LayoutInflater.from(context).inflate(
                    R.layout.kanner_content_layout, null);
            ImageView iv = (ImageView) fm.findViewById(R.id.iv_title);
            TextView tv = (TextView) fm.findViewById(R.id.tv_title);
            iv.setScaleType(ScaleType.CENTER_CROP);
            if (i == 0) {
                mImageLoader.displayImage(topStories.get(count - 1).getImage(), iv, options);
                tv.setText(topStories.get(count - 1).getTitle());
            } else if (i == count + 1) {
                mImageLoader.displayImage(topStories.get(0).getImage(), iv, options);
                tv.setText(topStories.get(0).getTitle());
            } else {
                mImageLoader.displayImage(topStories.get(i - 1).getImage(), iv, options);
                tv.setText(topStories.get(i - 1).getTitle());
            }
            views.add(fm);
            fm.setOnClickListener(this);
        }
    }

    private void showTime() {
        vp.setAdapter(new KannerPagerAdapter());
        vp.setFocusable(true);
        vp.setCurrentItem(1);
        currentItem = 1;
        vp.addOnPageChangeListener(new MyOnPageChangeListener());
        startPlay();
    }

    private void startPlay() {
        isAutoPlay = true;
        handler.postDelayed(task, 2000);
    }

    public void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs().build();
        ImageLoader.getInstance().init(config);
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        mImageLoader = ImageLoader.getInstance();
    }

    private final Runnable task = new Runnable() {

        @Override
        public void run() {
            if (isAutoPlay) {
                currentItem = currentItem % (count + 1) + 1;
                if (currentItem == 1) {
                    vp.setCurrentItem(currentItem, false);
                    handler.post(task);
                } else {
                    vp.setCurrentItem(currentItem);
                    handler.postDelayed(task, 3000);
                }
            } else {
                handler.postDelayed(task, 5000);
            }
        }
    };

    class KannerPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }

    }

    class MyOnPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            switch (arg0) {
                case 1:
                    isAutoPlay = false;
                    break;
                case 2:
                    isAutoPlay = true;
                    break;
                case 0:
                    if (vp.getCurrentItem() == 0) {
                        vp.setCurrentItem(count, false);
                    } else if (vp.getCurrentItem() == count + 1) {
                        vp.setCurrentItem(1, false);
                    }
                    currentItem = vp.getCurrentItem();
                    isAutoPlay = true;
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            for (int i = 0; i < iv_dots.size(); i++) {
                if (i == arg0 - 1) {
                    iv_dots.get(i).setImageResource(R.drawable.dot_focus);
                } else {
                    iv_dots.get(i).setImageResource(R.drawable.dot_blur);
                }
            }
        }

    }

    public void setOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        public void click(View v, TopStories stories);
    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener != null) {
            TopStories stories = topStories.get(vp.getCurrentItem() - 1);
            mItemClickListener.click(v, stories);
        }
    }

}
