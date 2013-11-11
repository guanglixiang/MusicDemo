package com.yang.musicplayerdemo;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class LocalMusicFragment extends Fragment {
	private static final String TAG = "LocalMusicFragment";
	private ListView listView;
	private List<Map<String, String>> musicInfoList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tab_viewpager1, container, false);  
		listView = (ListView) view.findViewById(R.id.tab_listview);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		listView.setAdapter(new SimpleAdapter(getActivity(), getMusicInfo(),
				R.layout.tab_viewpager1_listview_item, 
				new String[]{"index","song_name","singer"},
				new int[]{R.id.index,R.id.song_name,R.id.singer}));
	}

	private List<Map<String,String>> getMusicInfo() {
		//正则表达式用来顾虑包含 5个以上数字（含5个）或者 单个英文字符的歌曲,
		//匹配的是以[a-zA-Z_0-9]之一开头，含5个数字的字符串,或是单个英文字符,但奇怪的是为什么b222aa2可以匹配成功呢？
		Pattern pattern = Pattern.compile("^\\w+\\d.{5,}|\\w");
		musicInfoList = new ArrayList<Map<String,String>>();
		Cursor cursor = getActivity().getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, 
				null, null, null,
				MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		Log.d(TAG, "cursor="+cursor.getColumnCount());
		cursor.moveToFirst();
		int i = 1;
		while(!cursor.isAfterLast()){
			Map<String, String> musicInfoMap = new HashMap<String, String>();
			String music_name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)).trim();
			String singer = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
			int music_size = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
			musicInfoMap.put("index", String.valueOf(i));
			musicInfoMap.put("song_name",music_name);
			musicInfoMap.put("singer",singer);
			Matcher matcher = pattern.matcher(music_name);
			Log.d(TAG, " matcher.matches()="+ matcher.matches());
			//过滤掉歌曲小于指定大小并且非正则表达式的歌曲
			if (music_size>=819200 && !matcher.matches() ) {
				i++;
				Log.d(TAG, "music_name="+music_name);
				musicInfoList.add(musicInfoMap);
			}
			cursor.moveToNext();
		}
		return musicInfoList;
	}

}
