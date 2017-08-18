package com.gzgamut.vivitar.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gzgamut.vivitar.R;
import com.gzgamut.vivitar.global.Global;
import com.gzgamut.vivitar.helper.ShowValueHelper;

public class SettingActivity extends Activity {

	public static String SAVE_UNIT_NAME = "SAVE_UNIT_NAME";


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		initUI();
		// 判断是否第一次进入app
		checkFirst();

	}

	private void checkFirst() {
		if (sharedPreferences.getString(Global.APP_FRIST, "").equals(Global.APP_ENTERED)) {
			image_list.setVisibility(View.VISIBLE);
			but_setting_back.setVisibility(View.GONE);
			tv_setting_title.setText(R.string.setting);
			but_svae_unit.setVisibility(View.GONE);
		}
	}

	private OnClickListener myOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {

			switch (view.getId()) {
			case R.id.image_list:
				showListDialog(SettingActivity.this);
				break;
			case R.id.layout_home:
				Intent intent_home = new Intent(SettingActivity.this, MainActivity.class);
				intent_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent_home);
				finish();
				break;
			case R.id.layout_user:
				Intent intent_user = new Intent(SettingActivity.this, UserActivity.class);
				intent_user.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent_user);
				finish();
				break;
			case R.id.layout_setting:
				popupWindow.dismiss();
				break;
			// 单位kg
			case R.id.iv_unit_kg:
				editor.putString(Global.UNIT, Global.UNIT_KG);
				editor.commit();
				iv_unit_kg.setBackgroundResource(R.drawable.btn_selected_selected);
				iv_unit_bl.setBackgroundResource(R.drawable.btn_selected_normal);
				break;
			// 单位LB
			case R.id.iv_unit_bl:
				editor.putString(Global.UNIT, Global.UNIT_LB);
				editor.commit();
				iv_unit_bl.setBackgroundResource(R.drawable.btn_selected_selected);
				iv_unit_kg.setBackgroundResource(R.drawable.btn_selected_normal);
				break;
			// 单位in
			case R.id.iv_unit_in:
				editor.putString(Global.UNIT_CMIN, Global.UNIT_IN);
				editor.commit();
				Log.i("unit", "----iv_unit_in------");
				iv_unit_in.setBackgroundResource(R.drawable.btn_selected_selected);
				iv_unit_cm.setBackgroundResource(R.drawable.btn_selected_normal);
				break;
			// 单位 cm
			case R.id.iv_unit_cm:
				editor.putString(Global.UNIT_CMIN, Global.UNIT_CM);
				editor.commit();
				Log.i("unit", "----iv_unit_cm------");
				iv_unit_cm.setBackgroundResource(R.drawable.btn_selected_selected);
				iv_unit_in.setBackgroundResource(R.drawable.btn_selected_normal);
				break;

			case R.id.but_save_unit:
				if (sharedPreferences.getString(Global.UNIT_CMIN, "").equals("")) {
					editor.putString(Global.UNIT_CMIN, Global.UNIT_CM);
					editor.commit();
				}

				if (sharedPreferences.getString(Global.UNIT, "").equals("")) {
					editor.putString(Global.UNIT, Global.UNIT_KG);
					editor.commit();
				}
				Intent intent = new Intent(SettingActivity.this, MyProfile.class);
				startActivity(intent);
				finish();
				break;
			case R.id.but_setting_back:
				finish();
				System.exit(0);
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

			View popupView = getLayoutInflater().inflate(R.layout.innofit_left, null);

			popupWindow = new PopupWindow(popupView, (int) (width * 0.9), LayoutParams.MATCH_PARENT, true);
			popupWindow.setTouchable(true);
			popupWindow.setOutsideTouchable(true);
			popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
			popupWindow.setAnimationStyle(R.anim.pull_in_left);

			// ///////////////////////////////////
			View layout_home = popupView.findViewById(R.id.layout_home);
			layout_home.setOnClickListener(myOnClickListener);

			View layout_user = popupView.findViewById(R.id.layout_user);
			layout_user.setOnClickListener(myOnClickListener);

			View layout_setting = popupView.findViewById(R.id.layout_setting);
			layout_setting.setOnClickListener(myOnClickListener);

			popupWindow.showAtLocation(findViewById(R.id.layout_main), Gravity.LEFT, 0, 0);

		} else {
			popupWindow.showAtLocation(findViewById(R.id.layout_main), Gravity.LEFT, 0, 0);
		}

	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
		RelativeLayout layout_inchlb = (RelativeLayout) findViewById(R.id.layout_inchlb);
		layout_inchlb.setOnClickListener(myOnClickListener);
		but_svae_unit = (Button) findViewById(R.id.but_save_unit);
		but_svae_unit.setOnClickListener(myOnClickListener);
		but_setting_back = (Button) findViewById(R.id.but_setting_back);
		but_setting_back.setOnClickListener(myOnClickListener);
		image_list = (ImageView) findViewById(R.id.image_list);
		image_list.setOnClickListener(myOnClickListener);

		iv_unit_kg = (ImageView) findViewById(R.id.iv_unit_kg);
		iv_unit_kg.setOnClickListener(myOnClickListener);

		iv_unit_bl = (ImageView) findViewById(R.id.iv_unit_bl);
		iv_unit_bl.setOnClickListener(myOnClickListener);

		iv_unit_in = (ImageView) findViewById(R.id.iv_unit_in);
		iv_unit_in.setOnClickListener(myOnClickListener);

		iv_unit_cm = (ImageView) findViewById(R.id.iv_unit_cm);
		iv_unit_cm.setOnClickListener(myOnClickListener);

		//
		sharedPreferences = getSharedPreferences(SAVE_UNIT_NAME, SettingActivity.MODE_PRIVATE);
		editor = sharedPreferences.edit();

		// 重量单位
		if (sharedPreferences.getString(Global.UNIT, "").equals(Global.UNIT_KG)) {
			iv_unit_kg.setBackgroundResource(R.drawable.btn_selected_selected);
			iv_unit_bl.setBackgroundResource(R.drawable.btn_selected_normal);

		} else if (sharedPreferences.getString(Global.UNIT, "").equals(Global.UNIT_LB)) {
			iv_unit_bl.setBackgroundResource(R.drawable.btn_selected_selected);
			iv_unit_kg.setBackgroundResource(R.drawable.btn_selected_normal);

		}
		// 高度单位
		if (sharedPreferences.getString(Global.UNIT_CMIN, "").equals(Global.UNIT_CM)) {
			iv_unit_cm.setBackgroundResource(R.drawable.btn_selected_selected);
			iv_unit_in.setBackgroundResource(R.drawable.btn_selected_normal);

		} else if (sharedPreferences.getString(Global.UNIT_CMIN, "").equals(Global.UNIT_IN)) {
			iv_unit_in.setBackgroundResource(R.drawable.btn_selected_selected);
			iv_unit_cm.setBackgroundResource(R.drawable.btn_selected_normal);
		}
		tv_setting_title = (TextView) findViewById(R.id.tv_setting_title);
	}

	// 监听返回键，退出
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			new AlertDialog.Builder(this).setTitle(R.string.exit_tip).setMessage(R.string.whether_to_exit_)
					.setPositiveButton(R.string.exit_ok, new Dialog.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							finish();
							System.exit(0);
						}
					}).setNegativeButton(R.string.cancel, null).show();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	//
	private Button but_svae_unit, but_setting_back;
	private ImageView image_list;
	private TextView tv_setting_title;
	private PopupWindow popupWindow;
	private ImageView iv_unit_bl, iv_unit_kg, iv_unit_in, iv_unit_cm;
	private SharedPreferences sharedPreferences;
	Editor editor = null;
}
