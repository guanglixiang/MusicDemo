package com.yang.musicplayerdemo;

import com.yang.musicplayerdemo.utils.PlayMusicService;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore.Audio;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements LocalMusicFragment.StartPlayMusic{
	private static long tempTime = 0;
	private MediaPlayer mediaPlayer;
	/*
	 * 当前播放的歌曲所在位置
	 */
	private static int  currentPosition ;
	/*
	 * 歌曲总数
	 */
	private static int musicCount;
	/*
	 * 存放歌曲的Cursor
	 */
	private static Cursor mycursor = null;
	private static final String NEXT = "next";
	private static final String LAST = "last";
	private static final String PAUSE = "pause";
	private static final String PLAY = "play";
	/**
	 * Service中onBind方法返回的对象。
	 */
	private PlayMusicService.MyBinder myBinder;
	
	private ServiceConnection connection = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
		}
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			myBinder = (PlayMusicService.MyBinder) service;
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mediaPlayer = new MediaPlayer();
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		RightFragment fragment = new RightFragment();
		fragmentTransaction.add(R.id.right_fragment, fragment).commit();
		/**
		 * 绑定播放的Service，只需要一次。
		 */
		Intent intent = new Intent(MainActivity.this,PlayMusicService.class);
		bindService(intent, connection, BIND_AUTO_CREATE);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (getSupportFragmentManager().findFragmentByTag("fp")!=null) {
			FragmentTransaction  fragmentTransaction = getSupportFragmentManager().beginTransaction();
			//注意此处要先设置动画，然后再remove掉，否则没有动画效果
			fragmentTransaction.setCustomAnimations(R.anim.playfragment_start, R.anim.playfragment_over);
			fragmentTransaction.remove(getSupportFragmentManager().findFragmentByTag("fp"));
			fragmentTransaction.commit();
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_BACK && System.currentTimeMillis()-tempTime>2000) {
			Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
			tempTime = System.currentTimeMillis();
		}else{
			showExitDialog();
		}
		return true;
	}

	private void showExitDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("确定要退出?");
		builder.setTitle("提示");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				unbindService(connection);
				MainActivity.this.finish();
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		})
		.create().show();
	}

	@Override
	public void startPlayMusic(Cursor cursor, int position) {
		//点击listview 中的某个item，开始播放音乐
		currentPosition = position;
		mycursor = cursor;
		musicCount = mycursor.getCount();
		playMusic(PLAY,getMusicPath(mycursor,currentPosition));
		FragmentPlayController fplay = new FragmentPlayController();
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		//fragmentTransaction.setCustomAnimations(R.anim.playfragment_over, R.anim.playfragment_start);
		fragmentTransaction.replace(R.id.right_fragment, fplay,"fp").commit();
		
	}

	private void playMusic(String op,String path) {
		Intent intent = new Intent(MainActivity.this,PlayMusicService.class);
		intent.putExtra("OPERATION_MSG", op);
		intent.putExtra("path", path);
		myBinder.operateMusic(intent);
	}
	private String getMusicPath(Cursor cursor,int index){
		cursor.moveToPosition(index);
		String musicpath = cursor.getString(cursor.getColumnIndexOrThrow(Audio.Media.DATA));
		return musicpath;
		
	}
	/**
	 * 响应播放界面的各种button点击事件。需要在布局xml里面加上onClick属性
	 * @param view
	 */
	public void onClick(View view) {	
		switch (view.getId()) {
		case R.id.play_music:
			playMusic(PAUSE, "");
			break;
		case R.id.next_music:
			if (++currentPosition>=musicCount) {
				currentPosition = musicCount-1;
				Toast.makeText(this, "你妹啊，已经最后一首了，还按？找发泄呢？", Toast.LENGTH_SHORT).show();
			}
			playMusic(PLAY,getMusicPath(mycursor,currentPosition));
			break;
		case R.id.previous_music:
			if (--currentPosition<0) {
				currentPosition = 0;
				Toast.makeText(this, "已经是第一首了，亲，不要在按我了", Toast.LENGTH_SHORT).show();
			}
			playMusic(PLAY,getMusicPath(mycursor,currentPosition));
			break;
		case R.id.play_queue:
			
			break;
		case R.id.ibtn_player_voice:
			
			break;
		case R.id.repeat_music:
			
			break;
		case R.id.shuffle_music:
			
			break;

		default:
			break;
		}
	}
}