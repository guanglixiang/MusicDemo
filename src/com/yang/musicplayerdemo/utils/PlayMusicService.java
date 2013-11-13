package com.yang.musicplayerdemo.utils;

import java.io.IOException;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.IBinder;
import android.util.Log;
//每一个service必须要在manifest中注册
public class PlayMusicService extends Service implements OnCompletionListener,OnPreparedListener{
	public static MediaPlayer myMediaPlayer = null;
	private static final String NEXT = "next";
	private static final String LAST = "last";
	private static final String PAUSE = "pause";
	private static final String PLAY = "play";
	private String OPERATION_MSG = "OPERATION_MSG" ;
	private static final String TAG = "PlayMusicService";

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void onCreate(){
		Log.d(TAG, " PlayMusicService onCreate");
		super.onCreate();
		if (myMediaPlayer!=null) {
			myMediaPlayer.reset();
		}else {
			myMediaPlayer = new MediaPlayer();
			myMediaPlayer.setOnCompletionListener(this);
			myMediaPlayer.setOnPreparedListener(this);
		}
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		if (myMediaPlayer!=null) {
			myMediaPlayer.stop();
			myMediaPlayer.release();
			myMediaPlayer = null;
		}
		
	}
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//根据传进来的参数，执行对应的播放动作。
		Log.d(TAG, " PlayMusicService onStartCommand");
		Log.d(TAG, " PlayMusicService onStartCommand OPERATION_MSG="+intent.getStringExtra(OPERATION_MSG));
		if(PLAY.equals(intent.getStringExtra(OPERATION_MSG))){
			String path = intent.getStringExtra("path");
			PlayMusic(path);
			
		} 
		return super.onStartCommand(intent, flags, startId);
	}
	
	
	private void PlayMusic(String path) {
		myMediaPlayer.reset();
		Log.d(TAG, "myMediaPlay==="+myMediaPlayer);
		Log.d(TAG, "music path==="+path);
		try {
			myMediaPlayer.setDataSource(path);
			//在主线程里面不推荐用myMediaPlayer.prepare()方法，因为prepare有可能花费较长的时间
			//该方法执行完毕后会执行onPrepared的接口方法。
			Log.d(TAG, "before prepareAsync===");
			myMediaPlayer.prepareAsync();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void onCompletion(MediaPlayer arg0) {
		
	}
	@Override
	public void onPrepared(MediaPlayer mp) {
		Log.d(TAG, "onPrepared===");
		myMediaPlayer.start();
	}

}
