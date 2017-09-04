package com.gzgamut.vivitar.main;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import com.gzgamut.vivitar.R;
import com.gzgamut.vivitar.adapter.ChartAdapter;
import com.gzgamut.vivitar.been.Chart;
import com.gzgamut.vivitar.been.Info;
import com.gzgamut.vivitar.been.User;
import com.gzgamut.vivitar.database.DatabaseProvider;
import com.gzgamut.vivitar.global.Global;
import com.gzgamut.vivitar.helper.CalculateHelper;
import com.gzgamut.vivitar.helper.CalendarHelper;

public class ChartActivity extends Activity {

	private int userId;
	private List<Chart> chart_list;
	private Calendar cal_month_1;
	private Calendar cal_month_2;
	private Calendar cal_month_3;
	private Calendar cal_month_4;
	private ImageView but_chart;
	private Spinner spinner_user;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chart);

		Intent intent = this.getIntent();
		if (intent.getExtras() != null) {
			userId = intent.getExtras().getInt(Global.KEY_USER_TYPE, -1);
		}

		Log.i("userid", "userId=" + userId);
		initUI();
		updateChart(Global.TYPE_MONTH_MONTH);
		spinner_user.setSelection(userId);

	}

	private OnClickListener myOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {

			switch (view.getId()) {
			case R.id.but_share:
				GetandSaveCurrentImage();
				String SavePath = getSDCardPath() + "/AndyDemo/ScreenImage";
				String filepath = SavePath + "/Screen_1.png";
				Log.i("imagepath", "imagePath=" + filepath);
				shareMsg(ChartActivity.this, getString(R.string.select_mail),
						"Weight Report from Bally", "", filepath);
				break;
			case R.id.image_back:
				finish();
				break;

			case R.id.but_chart:
				Intent i = new Intent(ChartActivity.this,
						UserDetailsTableActivity.class);
				Log.i("myid", "id="+userId+"----");
				i.putExtra("chartid", userId);
				startActivity(i);
				finish();
				break;

			case R.id.spinner_user:
				showUserSelectDialog();
				break;
			default:
				break;
			}
		}
	};

	public static void shareMsg(Context context, String activityTitle,
			String msgTitle, String msgText, String imgPath) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		if (imgPath == null || imgPath.equals("")) {
			intent.setType("text/plain"); // 纯文本
		} else {
			File f = new File(imgPath);
			if (f != null && f.exists() && f.isFile()) {
				intent.setType("image/png");
				Uri u = Uri.fromFile(f);
				intent.putExtra(Intent.EXTRA_STREAM, u);
			}
		}
		intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
		intent.putExtra(Intent.EXTRA_TEXT, msgText);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(Intent.createChooser(intent, activityTitle));
	}

	private int state;
	private OnCheckedChangeListener myOnCheckedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton view, boolean isChecked) {

			switch (view.getId()) {
			case R.id.radio_month:
				if (isChecked) {
					setMonthValue();
					state = Global.TYPE_MONTH_MONTH;
					updateChart(Global.TYPE_MONTH_MONTH);
				}

				break;
			case R.id.radio_quarter:
				if (isChecked) {
					setQuarterValue();
					state = Global.TYPE_MONTH_QUARTER;
					updateChart(Global.TYPE_MONTH_QUARTER);
				}

				break;
			case R.id.radio_half:
				if (isChecked) {
					setHalfValue();
					state = Global.TYPE_MONTH_HALF;
					updateChart(Global.TYPE_MONTH_HALF);
				}

				break;
			case R.id.radio_year:
				if (isChecked) {
					setYearValue();
					state = Global.TYPE_MONTH_YEAR;
					updateChart(Global.TYPE_MONTH_YEAR);
				}
				break;
			default:
				break;
			}
		}
	};

	private void setMonthValue() {
		text_month_1.setVisibility(View.VISIBLE);
		text_month_2.setVisibility(View.INVISIBLE);
		text_month_3.setVisibility(View.INVISIBLE);
		text_month_4.setVisibility(View.VISIBLE);
		cal_month_1 = CalendarHelper.getToday();
		cal_month_1 = CalendarHelper.minXMonth(cal_month_1, 1);
		cal_month_4 = CalendarHelper.getToday();
		CalendarHelper.setDateInformation(ChartActivity.this, cal_month_1,
				text_month_1);
		CalendarHelper.setDateInformation(ChartActivity.this, cal_month_4,
				text_month_4);
	}

	private void setQuarterValue() {
		text_month_1.setVisibility(View.VISIBLE);
		text_month_2.setVisibility(View.VISIBLE);
		text_month_3.setVisibility(View.VISIBLE);
		text_month_4.setVisibility(View.VISIBLE);
		cal_month_1 = CalendarHelper.getToday();
		cal_month_1 = CalendarHelper.minXMonth(cal_month_1, 3);
		cal_month_2 = CalendarHelper.getToday();
		cal_month_2 = CalendarHelper.minXMonth(cal_month_2, 2);
		cal_month_3 = CalendarHelper.getToday();
		cal_month_3 = CalendarHelper.minXMonth(cal_month_3, 1);
		cal_month_4 = CalendarHelper.getToday();
		CalendarHelper.setDateInformation(ChartActivity.this, cal_month_1,
				text_month_1);
		CalendarHelper.setDateInformation(ChartActivity.this, cal_month_2,
				text_month_2);
		CalendarHelper.setDateInformation(ChartActivity.this, cal_month_3,
				text_month_3);
		CalendarHelper.setDateInformation(ChartActivity.this, cal_month_4,
				text_month_4);
	}

	private void setHalfValue() {
		text_month_1.setVisibility(View.VISIBLE);
		text_month_2.setVisibility(View.VISIBLE);
		text_month_3.setVisibility(View.VISIBLE);
		text_month_4.setVisibility(View.VISIBLE);
		cal_month_1 = CalendarHelper.getToday();
		cal_month_1 = CalendarHelper.minXMonth(cal_month_1, 6);
		cal_month_2 = CalendarHelper.getToday();
		cal_month_2 = CalendarHelper.minXMonth(cal_month_2, 4);
		cal_month_3 = CalendarHelper.getToday();
		cal_month_3 = CalendarHelper.minXMonth(cal_month_3, 2);
		cal_month_4 = CalendarHelper.getToday();
		CalendarHelper.setDateInformation(ChartActivity.this, cal_month_1,
				text_month_1);
		CalendarHelper.setDateInformation(ChartActivity.this, cal_month_2,
				text_month_2);
		CalendarHelper.setDateInformation(ChartActivity.this, cal_month_3,
				text_month_3);
		CalendarHelper.setDateInformation(ChartActivity.this, cal_month_4,
				text_month_4);
	}

	private void setYearValue() {
		text_month_1.setVisibility(View.VISIBLE);
		text_month_2.setVisibility(View.VISIBLE);
		text_month_3.setVisibility(View.VISIBLE);
		text_month_4.setVisibility(View.VISIBLE);
		cal_month_1 = CalendarHelper.getToday();
		cal_month_1 = CalendarHelper.minXMonth(cal_month_1, 12);
		cal_month_2 = CalendarHelper.getToday();
		cal_month_2 = CalendarHelper.minXMonth(cal_month_2, 8);
		cal_month_3 = CalendarHelper.getToday();
		cal_month_3 = CalendarHelper.minXMonth(cal_month_3, 4);
		cal_month_4 = CalendarHelper.getToday();
		CalendarHelper.setDateInformation(ChartActivity.this, cal_month_1,
				text_month_1);
		CalendarHelper.setDateInformation(ChartActivity.this, cal_month_2,
				text_month_2);
		CalendarHelper.setDateInformation(ChartActivity.this, cal_month_3,
				text_month_3);
		CalendarHelper.setDateInformation(ChartActivity.this, cal_month_4,
				text_month_4);
	}

	private void showUserSelectDialog() {
		// Dialog dialog = new Dialog(ChartActivity.this);
		// LayoutInflater inflater = LayoutInflater.from(ChartActivity.this);
		// View view = inflater.inflate(R.layout.item, null);
		// dialog.addContentView(view, new ViewGroup.LayoutParams(-1, -2));
		// dialog.show();
	}

	private List<Info> infoList;

	private void updateChart(int typeMonth) {

		Calendar[] calArray = null;

		switch (typeMonth) {
		case Global.TYPE_MONTH_MONTH:
			calArray = CalendarHelper.getLastMonthToTomorrow(CalendarHelper
					.getToday());
			break;
		case Global.TYPE_MONTH_QUARTER:
			calArray = CalendarHelper.getLast3MonthToTomorrow(CalendarHelper
					.getToday());
			break;
		case Global.TYPE_MONTH_HALF:
			calArray = CalendarHelper.getLast6MonthToTomorrow(CalendarHelper
					.getToday());
			break;
		case Global.TYPE_MONTH_YEAR:
			calArray = CalendarHelper.getLast12MonthToTomorrow(CalendarHelper
					.getToday());
			break;

		default:
			break;
		}

		if (infoList != null) {
			infoList.clear();
		}
		List<User> userList = DatabaseProvider.queryUserAll(ChartActivity.this);
		Log.i("myid", "id="+userList.get(userId).getUserId());
		if (userId != -1) {
			infoList = DatabaseProvider.queryScale(ChartActivity.this,
					userList.get(userId).getUserId(), calArray[0], calArray[1]);

		}
		Log.i("userid", "infoList.size()=" + infoList.size());
		// 这个星期的日期map
		Map<Calendar, Info> map = CalendarHelper.getDayMap(calArray);
		List<Info> infoDayList = CalculateHelper.setInfoDay(map, infoList);

		List<Double> valueList_weight = new ArrayList<Double>();
		List<Double> valueList_fat = new ArrayList<Double>();
		List<Double> valueList_water = new ArrayList<Double>();
		List<Double> valueList_muscle = new ArrayList<Double>();
		List<Double> valueList_bone = new ArrayList<Double>();
		List<Double> valueList_bmr = new ArrayList<Double>();
		List<Double> valueList_bmi = new ArrayList<Double>();

		Map<Integer, List<Double>> valueMap = new HashMap<Integer, List<Double>>();
		for (Info info : infoDayList) {
			double value_weight = info.getWeight();
			double value_fat = info.getFat();
			double value_water = info.getWater();
			double value_muscle = info.getMuscle();
			double value_bone = info.getBone();
			double value_bmr = info.getKcal();
			double value_bmi = info.getBmi();
			Log.i("value", "value_bmi=" + value_bmi);
			valueList_weight.add(value_weight);
			valueList_fat.add(value_fat);
			valueList_water.add(value_water);
			valueList_muscle.add(value_muscle);
			valueList_bone.add(value_bone);
			valueList_bmr.add(value_bmr);
			valueList_bmi.add(value_bmi);
		}
		Log.i("userid", "valueList_weight=" + valueList_weight
				+ "--valueList_bmi" + valueList_bmi);
		valueMap.put(0, valueList_weight);
		valueMap.put(1, valueList_bmi);
		valueMap.put(2, valueList_water);
		valueMap.put(3, valueList_muscle);
		valueMap.put(4, valueList_bone);
		valueMap.put(5, valueList_bmr);
		valueMap.put(6, valueList_bmi);

		initChart(valueMap);
	}

	private void initChart(Map<Integer, List<Double>> valueMap) {
		chart_list = new ArrayList<Chart>();

		Chart chart_weight = new Chart();
		chart_weight.setTtile(getString(R.string.index_weight));

		chart_list.add(chart_weight);
		//

		Chart chart_bmi = new Chart();
		chart_bmi.setTtile("  " + getString(R.string.bmi) + " ");
//		chart_bmi.setUnit(" " + getString(R.string.bmi));
		chart_list.add(chart_bmi);

		ChartAdapter adapter = new ChartAdapter(getApplicationContext(),
				chart_list, valueMap);
		list_chart.setAdapter(adapter);

	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
		but_chart = (ImageView) findViewById(R.id.but_chart);
		but_chart.setOnClickListener(myOnClickListener);
		but_share = (ImageView) findViewById(R.id.but_share);
		but_share.setOnClickListener(myOnClickListener);
		//
		Button image_back = (Button) findViewById(R.id.image_back);
		image_back.setOnClickListener(myOnClickListener);
		//
		List<User> userList = DatabaseProvider.queryUserAll(ChartActivity.this);
		String[] strUser = new String[userList.size()];
		for (int i = 0; i < userList.size(); i++) {
			strUser[i] = userList.get(i).getUsername();
		}

		spinner_user = (Spinner) findViewById(R.id.spinner_user);
		final List<User> users = DatabaseProvider
				.queryUserAll(ChartActivity.this);
		final String[] str = new String[users.size()];
		for (int i = 0; i < users.size(); i++) {
			str[i] = users.get(i).getUsername();
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				ChartActivity.this, R.layout.view_checked, str) {

			@Override
			public View getDropDownView(int position, View convertView,
					ViewGroup parent) {
				LayoutInflater inflater = getLayoutInflater();
				View view = inflater.inflate(R.layout.view_spinner_item, null);
				TextView text_label = (TextView) view
						.findViewById(R.id.text_label);
				text_label.setText(str[position]);
				return view;
			}

		};
		spinner_user.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				userId = arg2;
				// updateChart(state);
				switch (arg2) {
				case 0:
					updateChart(state);
					break;
				case 1:
					updateChart(state);
					break;
				case 2:
					updateChart(state);
					break;
				case 3:
					updateChart(state);
					break;
				case 4:
					updateChart(state);
					break;
				case 5:
					updateChart(state);
					break;
				case 6:
					updateChart(state);
					break;
				case 7:
					updateChart(state);
					break;
				case 8:
					updateChart(state);
					break;
				case 9:
					updateChart(state);
					break;

				default:
					break;
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		spinner_user.setAdapter(adapter);
		spinner_user.setSelection(userId);
		text_month_1 = (TextView) findViewById(R.id.text_month_1);
		text_month_2 = (TextView) findViewById(R.id.text_month_2);
		text_month_3 = (TextView) findViewById(R.id.text_month_3);
		text_month_4 = (TextView) findViewById(R.id.text_month_4);

		RadioButton radio_month = (RadioButton) findViewById(R.id.radio_month);
		radio_month.setOnCheckedChangeListener(myOnCheckedChangeListener);

		RadioButton radio_quarter = (RadioButton) findViewById(R.id.radio_quarter);
		radio_quarter.setOnCheckedChangeListener(myOnCheckedChangeListener);

		RadioButton radio_half = (RadioButton) findViewById(R.id.radio_half);
		radio_half.setOnCheckedChangeListener(myOnCheckedChangeListener);

		RadioButton radio_year = (RadioButton) findViewById(R.id.radio_year);
		radio_year.setOnCheckedChangeListener(myOnCheckedChangeListener);

		list_chart = (ListView) findViewById(R.id.list_chart);

		radio_month.setChecked(true);
	}

	//
	/**
	 * 获取和保存当前屏幕的截图
	 */
	public void GetandSaveCurrentImage() {
		// 1.构建Bitmap
		WindowManager windowManager = this.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int w = display.getWidth();
		int h = display.getHeight();

		Bitmap Bmp = Bitmap.createBitmap(w, h, Config.ARGB_8888);

		// 2.获取屏幕
		View decorview = getWindow().getDecorView();
		decorview.setDrawingCacheEnabled(true);
		Bmp = decorview.getDrawingCache();

		String SavePath = getSDCardPath() + "/AndyDemo/ScreenImage";

		// 3.保存Bitmap
		try {
			File path = new File(SavePath);
			// 文件
			String filepath = SavePath + "/Screen_1.png";
			File file = new File(filepath);
			if (!path.exists()) {
				path.mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}

			FileOutputStream fos = null;
			fos = new FileOutputStream(file);
			if (null != fos) {
				Bmp.compress(Bitmap.CompressFormat.PNG, 90, fos);
				fos.flush();
				fos.close();
				// Toast.makeText(ChartActivity.this,
				// "截屏文件已保存至SDCard/AndyDemo/ScreenImage/下",
				// Toast.LENGTH_LONG).show();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//
	/**
	 * 获取SDCard的目录路径功能
	 * 
	 * @return
	 */
	private static String getSDCardPath() {
		File sdcardDir = null;
		// 判断SDCard是否存在
		boolean sdcardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (sdcardExist) {
			sdcardDir = Environment.getExternalStorageDirectory();
		}
		return sdcardDir.toString();
	}

	private ListView list_chart;
	private TextView text_month_1, text_month_2, text_month_3, text_month_4;
	private ImageView but_share;
}
