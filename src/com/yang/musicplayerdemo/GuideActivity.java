package com.yang.musicplayerdemo;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;


public class GuideActivity extends Activity{
	private static ViewPager guideViewPager = null;
	private static final String SHAREDPREFERENCES_NAM = "musicplayer_sp";
	private static final String RECORD_ENTRANCE = "record_entrance";
	private static  boolean isFirstUse;
	/**
	 * 引导界面下方的小圆点
	 */
	private ViewGroup pointsView; 
	
	/**
	 * 存放小圆点的数组
	 */
	private ImageView[] imageViews;
	/**
	 * 单个小圆点实例
	 */
	private ImageView pointImageView;
	
	private  static final String TAG = "GuideActivity";
	/**
	 * @see 用来存储ViewPager里面的每一帧图片
	 */
	private List<View> views = new ArrayList<View>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.guide_layout);
		guideViewPager = (ViewPager) findViewById(R.id.guide_viewpager);
		pointsView = (ViewGroup) findViewById(R.id.index_points);
		isFirstUse = judgeIdentity();
		if (isFirstUse) {
			//加载引到界面
			initViewPager();
		}else {
			//不是第一次使用，直接进入主界面
		}
	}

	private boolean judgeIdentity() {
		return this.getSharedPreferences(SHAREDPREFERENCES_NAM, this.MODE_WORLD_READABLE)
				.getBoolean(RECORD_ENTRANCE, true);
	}

	private void initViewPager() {
		views.add(getLayoutInflater().inflate(R.layout.viewpager1, null));
		views.add(getLayoutInflater().inflate(R.layout.viewpager2, null));
		views.add(getLayoutInflater().inflate(R.layout.viewpager3, null));
		views.add(getLayoutInflater().inflate(R.layout.viewpager4, null));
		guideViewPager.setAdapter(new ViewpageAdapter(views));
		guideViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				for (int i = 0; i < imageViews.length; i++) {
					if (i==arg0) {
						imageViews[i].setBackgroundResource(R.drawable.ic_dot_default_selected);
					}else {
						imageViews[i].setBackgroundResource(R.drawable.ic_dot_default_unselected);
					}
				}
				if (3==arg0) {
					getSharedPreferences(SHAREDPREFERENCES_NAM, MODE_WORLD_WRITEABLE)
					.edit()
					.putBoolean(RECORD_ENTRANCE, true)
					.commit();
					Log.d(TAG, "setOnPageChangeListener position="+arg0);
					Intent intent = new Intent();
					intent.setClass(GuideActivity.this, MainActivity.class);
					startActivity(intent);
					GuideActivity.this.overridePendingTransition(R.anim.activity_start, R.anim.activity_over);
					//GuideActivity.this.finish();
				}
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
			}
		});
		imageViews = new ImageView[views.size()];
		//初始化图片下方的指示小圆点
		for (int i = 0; i < views.size(); i++) {
			pointImageView  = new ImageView(GuideActivity.this);
			pointImageView.setLayoutParams(new LayoutParams(10,10));
			pointImageView.setPadding(20, 0, 20, 0);
			Log.d(TAG, "pointImageView="+pointImageView);
			imageViews[i] =  pointImageView;
			if (0==i) {
				imageViews[i].setBackgroundResource(R.drawable.ic_dot_default_selected);
			}else {
				imageViews[i].setBackgroundResource(R.drawable.ic_dot_default_unselected);
			}
			pointsView.addView(imageViews[i]);
		}
	}



class ViewpageAdapter extends PagerAdapter{
	
	private List<View> myAdapterViews;
	
	public ViewpageAdapter(List<View> views) {
		this.myAdapterViews = views;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		Log.d(TAG, "destroyItem position="+position);
		container.removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(myAdapterViews.get(position));
		Log.d(TAG, "instantiateItem position="+position);
		return myAdapterViews.get(position);
	}

	@Override
	public int getCount() {
		return myAdapterViews.size();
	}
	
	//判断是否由对象生成界面
	@Override
	public boolean isViewFromObject(View view, Object arg1) {
		return view == arg1;
	}
	
}

}