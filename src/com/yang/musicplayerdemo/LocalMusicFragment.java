package com.yang.musicplayerdemo;

import android.R.integer;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
/**
 * 显示本地的所有音乐文件到一个listview中
 * @author c_kxiang
 */
public class LocalMusicFragment extends Fragment {


	private static final String TAG = "LocalMusicFragment";
	private ListView listView;
	private Cursor cursor;
	private StartPlayMusic callStartPlayMusic;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tab_viewpager1, container, false);  
		listView = (ListView) view.findViewById(R.id.tab_listview);
		String section = MediaStore.Audio.Media.SIZE+">=?";
		cursor = getActivity().getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, 
				null, section , new String[]{"800000"},
				MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//没能弄清楚查询时如何写多个匹配条件，因而没能实现查询数据库过滤掉异常歌名的功能，
		//另外查询返回的cursor里面也没有索引，所以没有实现歌曲显示索引的功能
		//String section = "("+MediaStore.Audio.Media.SIZE+">=?) OR("+MediaStore.Audio.Media.TITLE+" LIKE ?"+")";
		listView.setAdapter(new SimpleCursorAdapter(getActivity(),
				R.layout.tab_viewpager1_listview_item, 
				cursor,
				new String[]{MediaStore.Audio.Media.TITLE,MediaStore.Audio.Media.ARTIST},
				new int[]{R.id.song_name,R.id.singer}));
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				//启动一个新的Fragment播放界面，此处没有用getChildFragmentManager,因为新的界面要覆盖掉整个父界面
//				Bundle bundle = new Bundle();
//				FragmentPlay fplay = new FragmentPlay();
//				getFragmentManager().beginTransaction().replace(R.id.right_fragment, fplay).commit();
				callStartPlayMusic.startPlayMusic(cursor, arg2);
			}
		});
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			callStartPlayMusic = (StartPlayMusic) getActivity();
		} catch (Exception e) {
			throw new ClassCastException(getActivity().toString()
					+ " must implement OnHeadlineSelectedListener");
		}
	}
	
	public interface StartPlayMusic{
		public void startPlayMusic(Cursor cursor,int position);
	}
}













