package com.yang.musicplayerdemo;

import com.yang.musicplayerdemo.utils.PlayMusicService;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore.Audio;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements LocalMusicFragment.StartPlayMusic{
	private static long tempTime = 0;
	private MediaPlayer mediaPlayer;
	private static final String NEXT = "next";
	private static final String LAST = "last";
	private static final String PAUSE = "pause";
	private static final String PLAY = "play";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mediaPlayer = new MediaPlayer();
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		RightFragment fragment = new RightFragment();
		fragmentTransaction.add(R.id.right_fragment, fragment).commit();
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
		cursor.moveToPosition(position);
		String storagePath = cursor.getString(cursor.getColumnIndexOrThrow(Audio.Media.DATA));
		playMusic(PLAY,storagePath);
		FragmentPlay fplay = new FragmentPlay();
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		//fragmentTransaction.setCustomAnimations(R.anim.playfragment_over, R.anim.playfragment_start);
		fragmentTransaction.replace(R.id.right_fragment, fplay,"fp").commit();
		
	}

	private void playMusic(String op,String path) {
		Intent intent = new Intent(MainActivity.this,PlayMusicService.class);
		intent.putExtra("OPERATION_MSG", "play");
		intent.putExtra("path", path);
		startService(intent);
	}
}