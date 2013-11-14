package com.yang.musicplayerdemo;


import android.app.Activity;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

public class FragmentPlayController extends Fragment{
	private static final String TAG = "FragmentPlay";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.play_music_layout, container, false);  
		return view;
	}

	@Override
	public void onDestroyView() {
		Log.d(TAG, "-----onDestroyView");
		
		super.onDestroyView();
	}

	@Override
	public void onDetach() {
		Log.d(TAG, "-----onDetach");
		super.onDetach();
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
}
