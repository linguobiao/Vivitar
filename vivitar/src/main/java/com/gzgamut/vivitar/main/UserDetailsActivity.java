package com.gzgamut.vivitar.main;

import java.io.File;

import com.gzgamut.vivitar.R;
import com.gzgamut.vivitar.been.User;
import com.gzgamut.vivitar.database.DatabaseProvider;
import com.gzgamut.vivitar.global.Global;
import com.gzgamut.vivitar.helper.CalculateHelper;
import com.gzgamut.vivitar.view.MyCircleImageView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UserDetailsActivity extends Activity {
	private int gender;
	private int profileID = -1;
	private int userid;
	private static Context context;
	private static Handler myhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 111111:
				et_height.setText(msg.arg1 + "'");
				break;
			case 2222222:
				et_height.setText(msg.obj + "''");
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_details);
		context = UserDetailsActivity.this;
		initUI();
		// 查询数据库中的用户信息
		queryUserDetailes();
	}

	private void queryUserDetailes() {
		User user = new User();
		Intent i = getIntent();
		userid = i.getIntExtra(UserActivity.USER_ID, -1);
		user = queryUser(UserDetailsActivity.this, userid);

		if (user != null) {
			tv_username_head.setText(user.getUsername());
			et_username.setText(user.getUsername());
			// 身高
			if (sharedPreferences.getString(Global.UNIT_CMIN, "").equals(Global.UNIT_IN)) {
				et_height.setFilters(new InputFilter[] { new InputFilter.LengthFilter(5) });
				tv_unit_cm.setText("ft");
				double hei = CalculateHelper.cmToFeet(user.getHeight());
				int ia = (int) hei;
				int ib = (int) ((hei * 10) % 10);
				String apped = ia + "'" + ib + "''";
				et_height.setText(apped);
			} else {
				et_height.setText(user.getHeight() + "");
				et_height.setFilters(new InputFilter[] { new InputFilter.LengthFilter(3) });
			}
			// 年龄
			et_age.setText(user.getAge() + "");
			// 性别
			if (user.getSex() == 0) {
				but_sex_meal.setBackgroundResource(R.drawable.button_mf_yes);
				but_sex_femeal.setBackgroundResource(R.drawable.button_mf_no);
				gender = 0;
			} else if (user.getSex() == 1) {
				but_sex_femeal.setBackgroundResource(R.drawable.button_mf_yes);
				but_sex_meal.setBackgroundResource(R.drawable.button_mf_no);
				gender = 1;
			}
			// 头像
			head = user.getHead();
			if (head != null) {
				image_head.setImageBitmap(head);
			}
			// 体重
			if (sharedPreferences.getString(Global.UNIT, "").equals(Global.UNIT_LB)) {
				tv_unit_kg.setText("Lb");
				et_goal_weight.setText(CalculateHelper.kgToLbs(user.getGoalWeight()) + "");
			} else {
				et_goal_weight.setText(user.getGoalWeight() + "");
			}

		}

	}

	private User queryUser(Context context, int profileID) {
		User list = DatabaseProvider.queryUser(context, profileID);
		return list;
	}

	private void initUI() {
		sharedPreferences = getSharedPreferences(SettingActivity.SAVE_UNIT_NAME, SettingActivity.MODE_PRIVATE);
		Button iv_button_back = (Button) findViewById(R.id.iv_button_back);
		iv_button_back.setOnClickListener(myOnClickListener);
		Button iv_button_save = (Button) findViewById(R.id.iv_button_save);
		iv_button_save.setOnClickListener(myOnClickListener);

		but_sex_meal = (Button) findViewById(R.id.but_sex_meal);
		but_sex_femeal = (Button) findViewById(R.id.but_sex_female);

		but_sex_meal.setOnClickListener(myOnClickListener);
		but_sex_femeal.setOnClickListener(myOnClickListener);
		et_age = (EditText) findViewById(R.id.et_age);
		et_height = (EditText) findViewById(R.id.et_height);
		et_username = (EditText) findViewById(R.id.et_username);
		//
		layout_picture = (LinearLayout) findViewById(R.id.layout_picture);
		layout_picture.setOnClickListener(myOnClickListener);
		// 头像
		image_head = (MyCircleImageView) findViewById(R.id.image_head);
		//
		tv_username_head = (TextView) findViewById(R.id.tv_username_head);
		// tv_user_details_p_00 = (TextView)
		// findViewById(R.id.tv_user_details_p_00);
		//
		et_goal_weight = (EditText) findViewById(R.id.et_goal_weight);
		//
		tv_unit_cm = (TextView) findViewById(R.id.tv_unit_cm);
		tv_unit_kg = (TextView) findViewById(R.id.tv_unit_kg);
		//
		setEditTextCountWeight(et_goal_weight, 0, 0);
		setEditTextCountAge(et_age, 0, 120);
		setEditTextCount(et_height, 0, 300);
	}

	private OnClickListener myOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {

			switch (view.getId()) {
			case R.id.iv_button_back:
				Intent intent = new Intent(UserDetailsActivity.this, UserActivity.class);
				startActivity(intent);
				finish();
				break;
			// 把用戶信息保存到数据库
			case R.id.iv_button_save:
				saveUserInfo();
				break;
			// 用户头像选择
			case R.id.layout_picture:
				actionClickHead();
				break;
			// 男
			case R.id.but_sex_meal:
				if (but_sex_meal.isClickable()) {
					but_sex_meal.setBackgroundResource(R.drawable.button_mf_yes);
					but_sex_femeal.setBackgroundResource(R.drawable.button_mf_no);
					gender = 0;
				}
				break;
			// 女
			case R.id.but_sex_female:
				but_sex_femeal.setBackgroundResource(R.drawable.button_mf_yes);
				but_sex_meal.setBackgroundResource(R.drawable.button_mf_no);
				gender = 1;
				break;
			default:
			case R.id.but_take_photo:
				Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(camera, 1);
				break;
			case R.id.but_choose_photo:
				Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, 2);
				break;
			}

		}

		private void saveUserInfo() {
			Intent i = getIntent();
			profileID = i.getIntExtra(UserActivity.USER_ID, -1);
			String username = et_username.getText().toString().trim();
			String goal_weight = et_goal_weight.getText().toString();
			String age_str = et_age.getText().toString();
			String height_str = et_height.getText().toString();
			image_head.setDrawingCacheEnabled(true);
			User user = new User();
			user.setUsername(username);

			if (goal_weight == null || "".equals("")) {
				if (tv_unit_kg.getText().toString().equals("Lb")) {
					double f1 = CalculateHelper.decimal(CalculateHelper.LbsToKg(Double.parseDouble(goal_weight)));
					user.setGoalWeight(f1);
				} else {
					user.setGoalWeight(Double.parseDouble(goal_weight));
				}
			}

			if (age_str.equals("") || age_str == null) {
				user.setAge(25);
			} else {
				int age = Integer.parseInt(age_str);
				user.setAge(age);
			}
			if (height_str.equals("") || height_str == null) {
				user.setHeight(175);
			} else {
				if (tv_unit_cm.getText().toString().equals("cm")) {
					Double height = Double.parseDouble(height_str);
					user.setHeight(height);
				} else {
					String ia = "", ib = "";
					if (height_str.length() == 2) {
						ia = height_str.substring(0, 1);
					} else if (height_str.length() == 5) {
						ia = height_str.substring(0, 1);
						ib = height_str.substring(2, 3);
					}

					String demo = ia + "." + ib;
					if (height_str.equals("") || height_str == null) {

					} else {
						height_str = CalculateHelper.feetToCm(Double.parseDouble(demo)) + "";
						user.setHeight(Double.parseDouble(height_str));
					}

				}

			}

			user.setSex(gender);
			user.setHead(image_head.getDrawingCache(true));

			if (username.equals("") || username == null) {

				Toast.makeText(UserDetailsActivity.this, R.string.username_toast_tip, Toast.LENGTH_SHORT).show();
			} else {
				saveUser(profileID, user);
				Intent intent = new Intent(UserDetailsActivity.this, UserActivity.class);
				startActivity(intent);
				finish();
			}

		}

		private void saveUser(int profileID, User user) {
			Log.i("tag", "把用户信息数据到数据库。。。。。。");
			if (user != null) {
				User temp = DatabaseProvider.queryUser(UserDetailsActivity.this, profileID);
				if (temp == null) {
					DatabaseProvider.insertUser(UserDetailsActivity.this, profileID, user);
				} else {
					DatabaseProvider.updateUser(UserDetailsActivity.this, profileID, user);
				}

			}
		}

	};

	/**
	 * 调用系统的裁剪
	 * 
	 * @param uri
	 */
	public void cropPhoto(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		// intent.putExtra("scale", true);//黑边
		// intent.putExtra("scaleUpIfNeeded", true);//黑边
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}

	private void actionClickHead() {
		System.out.println("clicked portrait");
		dialog_portrait = new AlertDialog.Builder(UserDetailsActivity.this).setItems(new String[] { getString(R.string.take_photo), getString(R.string.choose_photo) }, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int positon) {

				switch (positon) {
				case 0:
					Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "head.jpg")));
					startActivityForResult(intent2, 2);// 采用ForResult打开
					break;
				case 1:
					Intent intent1 = new Intent(Intent.ACTION_PICK, null);
					intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
					startActivityForResult(intent1, 1);
					break;
				default:
					break;
				}

			}
		}).show();
		dialog_portrait.setCanceledOnTouchOutside(true);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			if (dialog_portrait != null) {
				dialog_portrait.dismiss();
			}
			if (resultCode == UserDetailsActivity.RESULT_OK) {
				cropPhoto(data.getData());// 裁剪图片
			}
			break;
		case 2:
			if (dialog_portrait != null) {
				dialog_portrait.dismiss();
			}
			if (resultCode == UserDetailsActivity.RESULT_OK) {
				File temp = new File(Environment.getExternalStorageDirectory() + "/head.jpg");
				cropPhoto(Uri.fromFile(temp));// 裁剪图片
			}

			break;
		case 3:
			if (data != null) {
				Bundle extras = data.getExtras();
				head = extras.getParcelable("data");
				if (head != null) {
					// 用ImageView显示出来
					image_head.setImageBitmap(head);
				}
			}
			break;
		default:
			break;
		}
	}

	// 监听返回键，退出
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = new Intent(UserDetailsActivity.this, UserActivity.class);
			startActivity(intent);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	//
	public static void setEditTextCount(final EditText editText, final int min, final int max) {

		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				Log.i("test", "s = " + s + "   start = " + start + "   before = " + before + "   count = " + count);

				if (tv_unit_cm.getText().toString().equals("cm")) {
					if (start > 0) {
						int num = Integer.parseInt(s.toString());
						Log.i("text", "num=" + num);
						if (num > max) {
							editText.setText(String.valueOf(""));
							Toast.makeText(context, "Height of not more than 300cm", Toast.LENGTH_SHORT).show();
						} else if (num < min) {
							// editText.setText(String.valueOf(min));
						}
					}
				} else {
					int testnum = -1;
					if (s.toString().equals("")) {

					} else {
						testnum = Integer.parseInt(s.toString().substring(0, 1));
					}
					if (testnum == 0) {
						Toast.makeText(context, "The first is not 0", Toast.LENGTH_SHORT).show();
						editText.setText("");
					}

					et_height.setOnKeyListener(new OnKeyListener() {

						@Override
						public boolean onKey(View v, int keyCode, KeyEvent event) {
							if (keyCode == KeyEvent.KEYCODE_DEL) {
								et_height.setText("");
							}
							return false;
						}
					});
					if (start == 0) {
						if (s.toString().length() == 1) {
							Message message = new Message();
							message.arg1 = Integer.parseInt(s.toString());
							message.what = 111111;
							myhandler.sendMessage(message);
						}

					} else if (start == 2) {
						if (s.toString().length() == 3) {
							Message message = new Message();
							// 4'4
							message.obj = s;
							message.what = 2222222;
							myhandler.sendMessage(message);
						}
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				editText.setSelection(editText.length());
			}

			@Override
			public void afterTextChanged(Editable s) {
				editText.setSelection(editText.length());
			}
		});
	}

	//
	public static void setEditTextCountAge(final EditText editText, final int min, final int max) {

		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				Log.i("test", "s = " + s + "   start = " + start + "   before = " + before + "   count = " + count);
				int num = -1;
				if (s.toString().equals("")) {

				} else {
					num = Integer.parseInt(s.toString());
				}
				if (start > 0) {

					if (num > max) {
						editText.setText(String.valueOf("25"));
						Toast.makeText(context, "Age of not more than 120", Toast.LENGTH_SHORT).show();
					} else if (num < min) {
						// editText.setText(String.valueOf(min));
					}
				}

				if (num == 0) {
					Toast.makeText(context, "can not input 0", Toast.LENGTH_SHORT).show();
					editText.setText("25");
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				editText.setSelection(editText.length());
			}

			@Override
			public void afterTextChanged(Editable s) {
				editText.setSelection(editText.length());
			}
		});
	}

	//
	public static void setEditTextCountWeight(final EditText editText, final int min, final int max) {

		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				Log.i("test", "s = " + s + "   start = " + start + "   before = " + before + "   count = " + count);
				int num = -1;
				if (s.toString().equals("")) {

				} else {
					num = (int) Double.parseDouble(s.toString());
				}
				if (start > 0) {
					Log.i("test", "numdd=" + num);
					if (tv_unit_kg.getText().toString().equals("Kg")) {
						Log.i("test", "num=" + num);
						if (num > 400) {
							editText.setText(String.valueOf(""));
							Toast.makeText(context, "weight of not more than 400 kg", Toast.LENGTH_SHORT).show();
						}
					} else {
						if (num > 800) {
							editText.setText(String.valueOf(""));
							Toast.makeText(context, "weight of not more than 800 lb", Toast.LENGTH_SHORT).show();
						}
					}

				}

				// if (num == 0) {
				// Toast.makeText(context, "can not input 0",
				// Toast.LENGTH_SHORT).show();
				// editText.setText("25");
				// }

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				editText.setSelection(editText.length());
			}

			@Override
			public void afterTextChanged(Editable s) {
				editText.setSelection(editText.length());
			}
		});
	}

	//
	public static void setEditTextCountGoalWeight(final EditText editText, final int min, final int max) {

		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (et_goal_weight.getText().toString().equals("") || et_goal_weight.getText().toString() == null) {

				} else {
					// meValue = Double.parseDouble(et_goal_weight.getText()
					// .toString());
				}

				// Log.i("test", "s = " + s + "   start = " + start
				// + "   before = " + before + "   count = " + count
				// + "   meValue=" + meValue);

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				editText.setSelection(editText.length());

			}

			@Override
			public void afterTextChanged(Editable s) {
				editText.setSelection(editText.length());
				Log.i("test", "afterTextChanged---------------------------");
			}
		});
	}

	// ----------------
	private Bitmap head;// 头像Bitmap
	private AlertDialog dialog_portrait;
	private TextView tv_username_head;
	private static EditText et_goal_weight;
	private SharedPreferences sharedPreferences;
	private static TextView tv_unit_cm;
	private static TextView tv_unit_kg;
	private Button but_sex_meal, but_sex_femeal;
	private EditText et_age;
	private static EditText et_height;
	private EditText et_username;
	private LinearLayout layout_picture;
	private MyCircleImageView image_head;
}
