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
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MyProfileActivity extends Activity implements CompoundButton.OnCheckedChangeListener {

	private static double meValue = 0;
	private double value_us, value_metric;

	private static Handler myhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 111111:
				et_height_profile.setText(msg.arg1 + "'");
				break;
			case 2222222:
				et_height_profile.setText(msg.obj + "''");
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_profile);
		initUI();
		// 初始化身高和体重的单位
//		putUnit();

	}

	private void putUnit() {
		//
		if (sharedPreferences.getString(Global.UNIT_CMIN, "").equals(Global.UNIT_CM)) {
			tv_unit_cm.setText("cm");
			et_height_profile.setFilters(new InputFilter[] { new InputFilter.LengthFilter(3) });
		} else if (sharedPreferences.getString(Global.UNIT_CMIN, "").equals(Global.UNIT_IN)) {
			tv_unit_cm.setText("ft");
		}
		//
		if (sharedPreferences.getString(Global.UNIT, "").equals(Global.UNIT_KG)) {
			tv_unit_kg.setText("Kg");
		} else if (sharedPreferences.getString(Global.UNIT, "").equals(Global.UNIT_LB)) {
			tv_unit_kg.setText("Lb");
		}

	}

	private void initUI() {
		sharedPreferences = getSharedPreferences(SettingActivity.SAVE_UNIT_NAME, MODE_PRIVATE);
		context = MyProfileActivity.this;
		Button iv_button_back_profile = (Button) findViewById(R.id.iv_button_back_profile);
		iv_button_back_profile.setOnClickListener(myOnClickListener);
		Button iv_button_save_profile = (Button) findViewById(R.id.iv_button_save_profile);
		iv_button_save_profile.setOnClickListener(myOnClickListener);
		but_us = findViewById(R.id.but_us);
		but_us.setOnCheckedChangeListener(this);
		but_metric = findViewById(R.id.but_metric);
		but_metric.setOnCheckedChangeListener(this);
		//
		but_meal_profile = findViewById(R.id.but_meal_profile);
		but_female_profile = findViewById(R.id.but_female_profile);
		//
		image_head = (MyCircleImageView) findViewById(R.id.image_head_profile);
		findViewById(R.id.layout_picture).setOnClickListener(myOnClickListener);
		//
		tv_unit_cm = (TextView) findViewById(R.id.tv_unit_cm);
		tv_unit_kg = (TextView) findViewById(R.id.tv_unit_kg);
		// et_username_profile,et_age_profile,et_height_profile,et_goal_weight;
		et_username_profile = (EditText) findViewById(R.id.et_username_profile);
		et_age_profile = (EditText) findViewById(R.id.et_age_profile);
		et_height_profile = (EditText) findViewById(R.id.et_height_profile);
		et_goal_weight = (EditText) findViewById(R.id.et_goal_weight);
		//
		setEditTextCount(et_height_profile, 0, 300);
		//
		setEditTextCountWeight(et_goal_weight, 0, 0);
		//
		setEditTextCountAge(et_age_profile, 0, 120);

		but_metric.setChecked(true);
		but_meal_profile.setChecked(true);
	}

	private OnClickListener myOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.iv_button_back_profile:
				finish();
				break;
			// 保存到数据库，插入第一条数据
			case R.id.iv_button_save_profile:
				saveData();
				break;
			case R.id.layout_picture:
				actionHead();
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
		switch (compoundButton.getId()) {
			case R.id.but_us:
				if (isChecked) tabUs();
				break;
			case R.id.but_metric:
				if (isChecked) tabMetric();
				break;
			default:break;
		}
	}


	protected void tabMetric() {
		et_height_profile.setFilters(new InputFilter[] { new InputFilter.LengthFilter(3) });
		// 身高
		String heightStr = et_height_profile.getText().toString();
		if (TextUtils.isEmpty(heightStr)) {
			et_height_profile.setHint(Global.DEF_CM);
		} else {
			if (tv_unit_cm.getText().toString().equals("ft")) {
				int ifirst = 0;
				int iSend = 0;
				String demo = "";

				if (heightStr.length() == 2) {
					ifirst = Integer.parseInt(heightStr.substring(0, 1));
					demo = ifirst + "";
				}
				if (heightStr.length() == 5) {
					ifirst = Integer.parseInt(heightStr.substring(0, 1));
					iSend = Integer.parseInt(heightStr.substring(2, 3));
					demo = ifirst + "." + iSend;
				}
				if (demo.equals("") || demo == null) {

				} else {
					et_height_profile.setText(CalculateHelper.feetToCm(Double.parseDouble(demo)) + "");
				}
			}
		}


		// 目标体重
		if (TextUtils.isEmpty(et_goal_weight.getText().toString())) {
			et_goal_weight.setHint(Global.DEF_KG);
		} else {
			if (tv_unit_kg.getText().toString().equals("Lb")) {
				// 66
				if (meValue == value_us) {
					et_goal_weight.setText(value_metric + "");
				} else {
					value_us = meValue;
					et_goal_weight.setText(CalculateHelper.LbsToKg(meValue) + "");
				}
				value_metric = meValue;
			}
		}

		tv_unit_cm.setText(R.string.cm);
		tv_unit_kg.setText(R.string.kg);

	}

	protected void tabUs() {
		setEditTextCount(et_height_profile, 0, 255);
		et_height_profile.setFilters(new InputFilter[] { new InputFilter.LengthFilter(5) });
		//
		String heightStr = et_height_profile.getText().toString();
		if (TextUtils.isEmpty(heightStr)) {
			et_height_profile.setHint(Global.DEF_IN);
		} else {
			if (tv_unit_cm.getText().toString().equals("cm")) {

				double number = CalculateHelper.cmToFeet(Double.parseDouble(heightStr));

				int i = (int) CalculateHelper.decimal(number);
				double id = (CalculateHelper.decimal(number) - i) * 10;
				et_height_profile.setText(i + "'" + (int) id + "''");
			}
		}

		// 目标体重
		if (TextUtils.isEmpty(et_goal_weight.getText().toString())) {
			et_goal_weight.setHint(Global.DEF_LB);
		} else {
			if (tv_unit_kg.getText().toString().equals("Kg")) {
				// 66
				value_metric = meValue;
				if (CalculateHelper.kgToLbs(meValue) == value_us) {
					et_goal_weight.setText(value_us + "");
				} else {
					et_goal_weight.setText(CalculateHelper.kgToLbs(meValue) + "");
				}
				value_us = meValue;

			}
		}
		tv_unit_cm.setText(R.string.ft);
		tv_unit_kg.setText(R.string.lb);

	}

	protected void saveData() {
		// et_username_profile,et_age_profile,et_height_profile,et_goal_weight;
		String name_str = et_username_profile.getText().toString().trim();
		if (TextUtils.isEmpty(name_str)) name_str = et_username_profile.getHint().toString().trim();
		String age_str = et_age_profile.getText().toString();
		if (TextUtils.isEmpty(age_str)) age_str = et_age_profile.getHint().toString();
		String height_str = et_height_profile.getText().toString();
		if (TextUtils.isEmpty(height_str)) {
			height_str = Global.DEF_CM;
		}else {
			if (tv_unit_cm.getText().toString().equals("cm")) {

			} else {
				String ia = "", ib = "";
				if (height_str.length() == 2) {
					ia = height_str.substring(0, 1);
				} else if (height_str.length() == 5) {
					ia = height_str.substring(0, 1);
					ib = height_str.substring(2, 3);
				}
				String demo = ia + "." + ib;

				height_str = CalculateHelper.feetToCm(Double.parseDouble(demo)) + "";
			}
		}


		String weight_str = et_goal_weight.getText().toString();
		if (TextUtils.isEmpty(weight_str)){
			weight_str = Global.DEF_KG;
		} else{
			if (tv_unit_kg.getText().toString().equals("Kg")) {
			} else {
				double i = CalculateHelper.LbsToKg(Double.parseDouble(weight_str));
				double f1 = CalculateHelper.decimal(i);
				weight_str = f1 + "";

			}
		}

		image_head.setDrawingCacheEnabled(true);

		User user = new User();
		user.setUsername(name_str);
		user.setAge(Integer.parseInt(age_str));
		user.setHead(image_head.getDrawingCache(true));
		user.setSex(but_female_profile.isChecked() ? Global.TYPE_SEX_FEMALE : Global.TYPE_SEX_MALE);
		user.setHeight(Double.parseDouble(height_str));
		user.setGoalWeight(Double.parseDouble(weight_str));
		user.setUnit("defalut");
		DatabaseProvider.insertUser_(MyProfileActivity.this, user);
		Editor editor = sharedPreferences.edit();
		if (sharedPreferences.getString(Global.APP_FRIST, "").equals(Global.APP_ENTERED)) {
			Intent i = new Intent(MyProfileActivity.this, UserActivity.class);
			startActivity(i);
			finish();
		} else {
			Intent i = new Intent(MyProfileActivity.this, MainActivity.class);
			editor.putString(Global.APP_FRIST, Global.APP_ENTERED);
			editor.commit();
			startActivity(i);
			finish();
		}
	}

	/**
	 * 身高
	 */
	public static void setEditTextCount(final EditText editText, final int min, final int max) {

		editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				Log.i("log", "s = " + s + "   start = " + start + "   before = " + before + "   count = " + count);
				if (tv_unit_cm.getText().toString().equals("cm")) {
					if (start > 0) {
						int num = Integer.parseInt(s.toString());
						Log.i("log", "num=" + num);
						if (num > max) {
							editText.setText(String.valueOf(""));
							Toast.makeText(context, "Height of not more than 300cm", Toast.LENGTH_SHORT).show();
						} else if (num < min) {
							// editText.setText(String.valueOf(min));
						}
					}
				} else {
					int testnum = -1;
					if (!s.toString().equals("")) {
						testnum = Integer.parseInt(s.toString().substring(0, 1));
					}
					if (testnum == 0) {
						Toast.makeText(context, "The first is not 0", Toast.LENGTH_SHORT).show();
						editText.setText("");
					}
					et_height_profile.setOnKeyListener(new OnKeyListener() {

						@Override
						public boolean onKey(View v, int keyCode, KeyEvent event) {
							if (keyCode == KeyEvent.KEYCODE_DEL) {
								et_height_profile.setText("");
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

	/**
	 * 年龄
	 */
	public static void setEditTextCountAge(final EditText editText, final int min, final int max) {

		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				Log.i("test", "s = " + s + "   start = " + start + "   before = " + before + "   count = " + count);
				int num = -1;
				if (!s.toString().equals("")) {
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

				if (!et_goal_weight.getText().toString().equals("") && et_goal_weight.getText().toString() != null) {
					meValue = Double.parseDouble(et_goal_weight.getText().toString());
				} else {
					meValue = Double.parseDouble(et_goal_weight.getHint().toString());
				}

				int num = -1;
				if (!s.toString().equals("")) {
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
	protected void actionHead() {
		dialog_portrait = new AlertDialog.Builder(MyProfileActivity.this).setItems(new String[] { getString(R.string.take_photo), getString(R.string.choose_photo) }, new DialogInterface.OnClickListener() {

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

	//
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

	//
	private RadioButton but_us, but_metric, but_meal_profile, but_female_profile;
	private MyCircleImageView image_head;
	private AlertDialog dialog_portrait;
	private Bitmap head;// 头像Bitmap
	private static TextView tv_unit_cm;
	private static TextView tv_unit_kg;
	private EditText et_username_profile, et_age_profile;
	private static EditText et_height_profile;
	private static EditText et_goal_weight;
	private static Context context;
	private SharedPreferences sharedPreferences;
}
