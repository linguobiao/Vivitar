package com.gzgamut.vivitar.main;

import com.gzgamut.vivitar.R;
import com.gzgamut.vivitar.helper.ShowValueHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class AboutActivity extends Activity {

	private PopupWindow popupWindow;
	private String urlText = "";
 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		initUI();
	}

	private OnClickListener myOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {

			switch (view.getId()) {
			case R.id.image_list:
				showListDialog(AboutActivity.this);
				break;

			case R.id.layout_home:
				Intent intent_home = new Intent(AboutActivity.this,
						MainActivity.class);
				intent_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent_home);
				finish();
				break;
			case R.id.layout_user:
				Intent intent_user = new Intent(AboutActivity.this,
						UserActivity.class);
				intent_user.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent_user);
				finish();
				break;
			case R.id.layout_setting:
				Intent intent_setting = new Intent(AboutActivity.this,
						SettingActivity.class);
				intent_setting.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent_setting);
				finish();
				break;
			// case R.id.layout_about:
			// Intent intent_about = new Intent(AboutActivity.this,
			// AboutActivity.class);
			// intent_about.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// startActivity(intent_about);
			// break;
			case R.id.tv_url:
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				Uri content_url = Uri.parse("http:\\" + urlText);
				intent.setData(content_url);
				startActivity(intent);
				break;

			default:
				break;
			}

		}
	};

	/**
	 * 显示list对话框
	 */
	private void showListDialog(Activity context) {
		if (popupWindow == null) {
			int width = ShowValueHelper.getScreenWidth(context);

			View popupView = getLayoutInflater().inflate(R.layout.innofit_left,
					null);

			popupWindow = new PopupWindow(popupView, (int) (width * 0.9),
					LayoutParams.MATCH_PARENT, true);
			popupWindow.setTouchable(true);
			popupWindow.setOutsideTouchable(true);
			popupWindow.setBackgroundDrawable(new BitmapDrawable(
					getResources(), (Bitmap) null));
			popupWindow.setAnimationStyle(R.anim.pull_in_left);

			// ///////////////////////////////////
			View layout_home = popupView.findViewById(R.id.layout_home);
			layout_home.setOnClickListener(myOnClickListener);

			View layout_user = popupView.findViewById(R.id.layout_user);
			layout_user.setOnClickListener(myOnClickListener);

			View layout_setting = popupView.findViewById(R.id.layout_setting);
			layout_setting.setOnClickListener(myOnClickListener);

			// View layout_about = popupView.findViewById(R.id.layout_about);
			// layout_about.setOnClickListener(myOnClickListener);
			// ///////////////////////////////////

			popupWindow.showAtLocation(findViewById(R.id.layout_main),
					Gravity.LEFT, 0, 0);

		} else {
			popupWindow.showAtLocation(findViewById(R.id.layout_main),
					Gravity.LEFT, 0, 0);
		}

	}

	/**
	 * 初始化UI
	 */
	private void initUI() {

		ImageView image_list = (ImageView) findViewById(R.id.image_list);
		image_list.setOnClickListener(myOnClickListener);
		TextView tv_url = (TextView) findViewById(R.id.tv_url);
		tv_url.setOnClickListener(myOnClickListener);
		urlText = tv_url.getText().toString().trim();
	}

	// 监听返回键，退出
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			new AlertDialog.Builder(this).setTitle("Tip")
					.setMessage("Whether to exit?")
					.setPositiveButton("OK", new Dialog.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							finish();
							System.exit(0);
						}
					}).setNegativeButton("Cancel", null).show();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
