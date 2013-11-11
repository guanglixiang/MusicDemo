package com.yang.musicplayerdemo;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RightFragment extends Fragment{
	private static final int TAB_COUNT = 3;
	private ViewPager viewPager;
	private ArrayList<Fragment> fragments;
	private static final String TAG = "RightFragment";
	/**
	 * 存放每个viewpager里面的View
	 */
	private View[] views;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View view = inflater.inflate(R.layout.right_fragment_layout, container,false);
		viewPager = (ViewPager) view.findViewById(R.id.vPager);
		InitViewPager();
		return view;
	}
	private void InitViewPager() {
		fragments = new ArrayList<Fragment>();
		Fragment localMusicFragment = new LocalMusicFragment();
		Fragment netMusicFragment = new NetMusicFragment();
		Fragment findJobFragment = new FindJobFragment();
		fragments.add(localMusicFragment);
		fragments.add(netMusicFragment);
		fragments.add(findJobFragment);
		/*
		 * 此处使用getChildFragmentManager（）方法，当初使用getActivity.getSupportFragmentManager(),导致
		 * 不能正常显示子Fragment界面的问题,因待加载的Fragment是嵌套在一个Fragment里面的。用后者的方法导致子
		 * Fragment和父Fragment处于同等的位置，故而引起显示异常
		 */
		viewPager.setAdapter(new MyRightPanelPagerAdapter(getChildFragmentManager(),fragments) );
		viewPager.setCurrentItem(0);
	}
}
class MyRightPanelPagerAdapter extends FragmentPagerAdapter{
	private ArrayList<Fragment> fragmentlist;

	public MyRightPanelPagerAdapter(FragmentManager fm,ArrayList<Fragment> frlist) {
		super(fm);
		this.fragmentlist = frlist;
	}
	@Override
	public Fragment getItem(int arg0) {
		return fragmentlist.get(arg0);
	}
	@Override
	public int getCount() {
		return fragmentlist.size();
	}
}
