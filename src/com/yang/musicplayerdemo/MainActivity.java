package com.yang.musicplayerdemo;

import android.os.Bundle;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
	private static long tempTime = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		RightFragment fragment = new RightFragment();
		fragmentTransaction.add(R.id.right_fragment, fragment).commit();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
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
}