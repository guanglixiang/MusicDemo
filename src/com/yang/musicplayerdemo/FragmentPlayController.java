package com.yang.musicplayerdemo;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentPlayController extends Fragment{
	private static final String TAG = "FragmentPlay";
	private TextView currentTime,totalTime;
	private  String songtime;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.play_music_layout, container, false);  
		currentTime = (TextView) view.findViewById(R.id.current_progress);
		totalTime = (TextView) view.findViewById(R.id.final_progress);
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
	public void onStart(){
		Log.d(TAG, "-----onStart");
		super.onStart();
		currentTime.setTextColor(Color.BLUE);
	}
	@Override
	public void onResume(){
		Log.d(TAG, "-----onResume");
		super.onResume();
	}
	public void onShow(String time) {
		Log.d(TAG, "currentTime=="+currentTime);
		Log.d(TAG, "songtime=="+time);
		currentTime.setText(time);
		//this.songtime = time;
		
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
}
