package com.gzgamut.vivitar.main;

import java.util.ArrayList;
import java.util.List;
import com.gzgamut.vivitar.R;
import com.gzgamut.vivitar.adapter.UserAdapter;
import com.gzgamut.vivitar.been.User;
import com.gzgamut.vivitar.database.DatabaseProvider;
import com.gzgamut.vivitar.helper.ShowValueHelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

public class UserActivity extends Activity {

	private PopupWindow popupWindow;
	private ListView lv_userlist;
	private List<User> userList;
	public static String USER_ID = "USER_ID";
	UserAdapter userAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user);
		initUI();
		itemClick();
	}

	private void itemClick() {
		lv_userlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int positiong, long arg3) {
				Intent intent0 = new Intent(UserActivity.this,
						UserDetailsActivity.class);
				intent0.putExtra(USER_ID, userList.get(positiong).getUserId());
				startActivity(intent0);
				finish();
			}
		});

		lv_userlist.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				new AlertDialog.Builder(UserActivity.this)
						.setTitle("delete ?")
						.setMessage(null)
						.setPositiveButton(R.string.exit_ok,
								new Dialog.OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										int id = userList.get(arg2).getUserId();
										Log.i("queryUserAll","id="+id );
										if (DatabaseProvider.queryUserAll(
												UserActivity.this).size() == 1) {
										
											String toast = getResources()
													.getString(
															R.string.tip_no_user_keepOne);
											Toast.makeText(UserActivity.this,
													toast, Toast.LENGTH_SHORT)
													.show();
										} else {
											
											DatabaseProvider.deleteUser(
													UserActivity.this, id);
											userList.remove(arg2);
											 DatabaseProvider.deleteScaleById(UserActivity.this,
													 id);
											userAdapter.notifyDataSetChanged();
										}

									}
								}).setNegativeButton(R.string.cancel, null)
						.show();

				return true;
			}
		});
	}

	private OnClickListener myOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {

			switch (view.getId()) {
			case R.id.iv_button_add_user:
				Intent i = new Intent(UserActivity.this, MyProfileActivity.class);
				startActivity(i);
				// finish();
				break;
			case R.id.image_list:
				showListDialog(UserActivity.this);
				break;

			case R.id.layout_home:
				Intent intent_home = new Intent(UserActivity.this,
						MainActivity.class);
				intent_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent_home);
				finish();
				break;
			case R.id.layout_user:
				popupWindow.dismiss();
				break;
			case R.id.layout_setting:
				Intent intent_setting = new Intent(UserActivity.this,
						SettingActivity.class);
				intent_setting.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent_setting);
				finish();
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
		Button iv_button_add_user = (Button) findViewById(R.id.iv_button_add_user);
		iv_button_add_user.setOnClickListener(myOnClickListener);

		ImageView image_list = (ImageView) findViewById(R.id.image_list);
		image_list.setOnClickListener(myOnClickListener);
		lv_userlist = (ListView) findViewById(R.id.lv_userlist);
		userList = new ArrayList<User>();
		// 查询数据库
		List<User> userListQ = new ArrayList<User>();
		userListQ = queryUser(UserActivity.this);
		
		for (int i = 0; i < userListQ.size(); i++) {
			if (userListQ.size() > 0) {
				if (userListQ.get(i).getUserId() >= 0) {
					User u0 = new User();
					u0.setUserId(userListQ.get(i).getUserId());
					u0.setUsername(userListQ.get(i).getUsername());
					u0.setSex(userListQ.get(i).getSex());
					u0.setHeight(userListQ.get(i).getHeight());
					u0.setAge(userListQ.get(i).getAge());
					u0.setHead(userListQ.get(i).getHead());
					userList.add(u0);
				}
			}
		}

		userAdapter = new UserAdapter(getApplicationContext(), userList);
		lv_userlist.setAdapter(userAdapter);
		userAdapter.notifyDataSetChanged();
	}

	private List<User> queryUser(Context context) {

		List<User> list = DatabaseProvider.queryUserAll(context);
		return list;
	}

	// 监听返回键，退出
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			new AlertDialog.Builder(this)
					.setTitle(R.string.exit_tip)
					.setMessage(R.string.whether_to_exit_)
					.setPositiveButton(R.string.exit_ok,
							new Dialog.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									finish();
									System.exit(0);
								}
							}).setNegativeButton(R.string.cancel, null).show();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	//
}
