package com.gzgamut.vivitar.adapter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import com.gzgamut.vivitar.R;
import com.gzgamut.vivitar.been.Info;
import com.gzgamut.vivitar.been.User;
import com.gzgamut.vivitar.been.Value;
import com.gzgamut.vivitar.global.Global;
import com.gzgamut.vivitar.helper.CalculateHelper;
import com.gzgamut.vivitar.helper.ChartHelper;
import com.gzgamut.vivitar.main.SettingActivity;
import com.gzgamut.vivitar.view.MyCircleImageView;
import com.gzgamut.vivitar.view.MySeekBar;
import com.gzgamut.vivitar.view.MyViewFlipper;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HomeAdapter extends BaseAdapter {
	private Context mContext;
	private List<Value> list;
	public static int a = 0;
	private List<User> userList;
	private Map<Integer, List<Info>> infoChartMap;
	private SharedPreferences sharedPreferences;

	public HomeAdapter(Context context, List<Value> userList, List<User> users,
			Map<Integer, List<Info>> infoChartMap) {
		mContext = context;
		this.list = userList;
		this.userList = users;
		this.infoChartMap = infoChartMap;
		sharedPreferences = mContext.getSharedPreferences(
				SettingActivity.SAVE_UNIT_NAME, mContext.MODE_PRIVATE);
	}

	@Override
	public int getCount() {
		if (list != null) {
			return list.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (list != null) {
			return list.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	int index;

	@Override
	public final View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.slide_item, parent, false);
			holder.text_name = (TextView) convertView
					.findViewById(R.id.text_name);
			holder.text_sex = (TextView) convertView
					.findViewById(R.id.text_sex);
			holder.text_age = (TextView) convertView
					.findViewById(R.id.text_age);
			holder.text_height = (TextView) convertView
					.findViewById(R.id.text_height);
			holder.image_head = (MyCircleImageView) convertView
					.findViewById(R.id.image_head);
			// Flipper
			holder.viewFlipper = (MyViewFlipper) convertView
					.findViewById(R.id.viewFlipper);
			holder.text_weight = (TextView) convertView
					.findViewById(R.id.text_weight);
			holder.tv_unit_kg = (TextView) convertView
					.findViewById(R.id.tv_unit_kg);
			holder.text_bmi = (TextView) convertView
					.findViewById(R.id.text_bmi);
			// holder.text_fat = (TextView) convertView
			// .findViewById(R.id.text_fat);
			// seekBar
			holder.seek_weight = (MySeekBar) convertView
					.findViewById(R.id.seek_weight);
			holder.seek_bmi = (MySeekBar) convertView
					.findViewById(R.id.seek_bmi);
			// holder.seek_fat = (MySeekBar) convertView
			// .findViewById(R.id.seek_fat);
			// chart
			holder.chart_weight = (RelativeLayout) convertView
					.findViewById(R.id.chart_weight);
			holder.chart_bmi = (RelativeLayout) convertView
					.findViewById(R.id.chart_bmi);
			// holder.chart_fat = (RelativeLayout) convertView
			// .findViewById(R.id.chart_fat);
			// date
			holder.text_date_weight = (TextView) convertView
					.findViewById(R.id.text_date_weight);
			holder.tv_main_cm = (TextView) convertView
					.findViewById(R.id.tv_main_cm);

			holder.text_date_bmi = (TextView) convertView
					.findViewById(R.id.text_date_bmi);
			// holder.text_date_fat = (TextView) convertView
			// .findViewById(R.id.text_date_fat);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		setValue(holder, list.get(position), userList.get(position),
				infoChartMap.get(position));

		return convertView;
	}

	private void setValue(ViewHolder holder, Value value, User user,
			List<Info> infoList) {
		// holder.text_id.setText("P-" + Global.df_int_2.format(value.getId()));
		// holder.text_id.setText("P-0" + user.getUserId());
		holder.viewFlipper.setId(value.getId());
		//
		holder.text_name.setText(user.getUsername());
		if (user.getSex() == Global.TYPE_SEX_MALE) {
			holder.text_sex.setText("Male");
		} else {
			holder.text_sex.setText("Female");
		}
		Bitmap head = user.getHead();
		if (head != null) {
			holder.image_head.setImageBitmap(head);
		} else {
			holder.image_head.setImageResource(R.drawable.image_head_big);
		}

		holder.text_age.setText(String.valueOf(user.getAge()));

		if (sharedPreferences.getString(Global.UNIT_CMIN, "").equals(
				Global.UNIT_IN)) {

			double height = CalculateHelper.cmToFeet(user.getHeight());
			int ia = (int) height;
			int ib = (int) ((height * 10) % 10);
			String apped = ia + "'" + ib + "''";
			holder.tv_main_cm.setText("ft");
			holder.text_height.setText(apped);
		} else {
			BigDecimal b = new BigDecimal(user.getHeight());
			double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			holder.text_height.setText(String.valueOf(f1));
		}

		//
		Log.i("test", "weight *** = " + String.valueOf(value.getWeight()));
		holder.text_weight.setText(String.valueOf(value.getWeight()));

		holder.text_bmi.setText(String.valueOf(value.getBmi()));
		// holder.text_fat.setText(String.valueOf(value.getFat()));
		holder.tv_unit_kg.setText(value.getJudgeUnit());
		//
		holder.seek_weight.setProgress(value.getSeekWeight());
		holder.seek_bmi.setProgress(value.getSeekBmi());
		// holder.seek_fat.setProgress(value.getSeekFat());

		String date = value.getDate();
		if (date != null) {
			holder.text_date_weight.setText(value.getDate());
			holder.text_date_bmi.setText(value.getDate());
			// holder.text_date_fat.setText(value.getDate());
		} else {
			holder.text_date_weight.setText(mContext
					.getString(R.string.No_records));
			holder.text_date_bmi.setText(mContext
					.getString(R.string.No_records));
			// holder.text_date_fat.setText(mContext
			// .getString(R.string.No_records));
		}

		XYMultipleSeriesDataset mDataset_weight = new XYMultipleSeriesDataset();
		XYMultipleSeriesRenderer mRenderer_weight = new XYMultipleSeriesRenderer();
		GraphicalView mChartView_weight;
		mDataset_weight = ChartHelper.setDataset_chart_small(mContext,
				infoList, Global.TYPE_CHART_WEIGHT);
		mRenderer_weight = ChartHelper.setRenderer_chart_small(mContext,
				infoList, Global.TYPE_CHART_WEIGHT);
		mChartView_weight = ChartFactory.getCubeLineChartView(mContext,
				mDataset_weight, mRenderer_weight, ChartHelper.SMOTHNESS);
		holder.chart_weight.removeAllViews();
		holder.chart_weight.addView(mChartView_weight, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		XYMultipleSeriesDataset mDataset_bmi = new XYMultipleSeriesDataset();
		XYMultipleSeriesRenderer mRenderer_bmi = new XYMultipleSeriesRenderer();
		GraphicalView mChartView_bmi;
		mDataset_bmi = ChartHelper.setDataset_chart_small(mContext, infoList,
				Global.TYPE_CHART_BMI);
		mRenderer_bmi = ChartHelper.setRenderer_chart_small(mContext, infoList,
				Global.TYPE_CHART_BMI);
		mChartView_bmi = ChartFactory.getCubeLineChartView(mContext,
				mDataset_bmi, mRenderer_bmi, ChartHelper.SMOTHNESS);
		holder.chart_bmi.removeAllViews();
		holder.chart_bmi.addView(mChartView_bmi, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		XYMultipleSeriesDataset mDataset_fat = new XYMultipleSeriesDataset();
		XYMultipleSeriesRenderer mRenderer_fat = new XYMultipleSeriesRenderer();
		GraphicalView mChartView_fat;
		mDataset_fat = ChartHelper.setDataset_chart_small(mContext, infoList,
				Global.TYPE_CHART_FAT);
		mRenderer_fat = ChartHelper.setRenderer_chart_small(mContext, infoList,
				Global.TYPE_CHART_FAT);
		mChartView_fat = ChartFactory.getCubeLineChartView(mContext,
				mDataset_fat, mRenderer_fat, ChartHelper.SMOTHNESS);
		// holder.chart_fat.removeAllViews();
		// holder.chart_fat.addView(mChartView_fat, new LayoutParams(
		// LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

	}

	public class ViewHolder {

		MyViewFlipper viewFlipper;
		MyCircleImageView image_head;
		TextView text_name, text_sex, text_age, text_height, tv_unit_kg;
		TextView text_weight, text_bmi;
		MySeekBar seek_weight, seek_bmi;
		RelativeLayout chart_weight, chart_bmi;
		TextView text_date_weight, text_date_bmi, text_date_fat;
		TextView tv_main_cm;
	}

}
