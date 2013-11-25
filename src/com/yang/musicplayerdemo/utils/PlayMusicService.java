package com.yang.musicplayerdemo.utils;

import java.io.IOException;

import com.yang.musicplayerdemo.FragmentPlayController;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;


//每一个service必须要在manifest中注册
public class PlayMusicService extends Service implements OnCompletionListener,OnPreparedListener{
	public static MediaPlayer myMediaPlayer = null;
	private static final String PAUSE = "pause";
	private static final String PLAY = "play";
	private String OPERATION_MSG = "OPERATION_MSG" ;
	private MyBinder myBinder = new MyBinder();
	private static final String ACTION_NAME = "complete_play";
	private static final String TAG = "PlayMusicService";

	@Override
	public IBinder onBind(Intent intent) {
		return myBinder;
	}
	@Override
	public void onCreate(){
		Log.d(TAG, " ------------------>PlayMusicService onCreate");
		super.onCreate();
		if (myMediaPlayer!=null) {
			myMediaPlayer.reset();
		}else {
			myMediaPlayer = new MediaPlayer();
			myMediaPlayer.setOnCompletionListener(this);
			myMediaPlayer.setOnPreparedListener(this);
		}
	}
	/**
	 * 在Service一定要将MediaPlayer释放掉
	 */
	@Override
	public void onDestroy(){
		Log.d(TAG, " ------------------>PlayMusicService onDestroy");
		super.onDestroy();
		if (myMediaPlayer!=null) {
			myMediaPlayer.stop();
			myMediaPlayer.release();
			myMediaPlayer = null;
		}
		
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		Log.d(TAG, "--------------> PlayMusicService onUnbind");
		return super.onUnbind(intent);
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
	public void onCompletion(MediaPlayer mediaPlayer) {
		Intent intent = new Intent(ACTION_NAME);
		sendBroadcast(intent);
	}
	@Override
	public void onPrepared(MediaPlayer mp) {
		Log.d(TAG, "onPrepared===");
		myMediaPlayer.start();
	}
	
	public class MyBinder extends Binder{
		public void operateMusic(Intent intent) {
			String OP = intent.getStringExtra(OPERATION_MSG);
			if(PLAY.equals(OP)){
				String path = intent.getStringExtra("path");
				PlayMusic(path);
			}else if (PAUSE.equals(OP)) {
				if (myMediaPlayer.isPlaying()) {
					Log.d(TAG, " PlayMusicService start---->pause");
					myMediaPlayer.pause();
				}else {
					Log.d(TAG, " PlayMusicService PAUSE-->start");
					myMediaPlayer.start();
				}
			}
		}
	}

}
