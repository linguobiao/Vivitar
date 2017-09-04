package com.gzgamut.vivitar.adapter;

import java.util.List;
import java.util.Map;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import com.gzgamut.vivitar.R;
import com.gzgamut.vivitar.been.Chart;
import com.gzgamut.vivitar.global.LanguageManager;
import com.gzgamut.vivitar.helper.ChartHelper;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChartAdapter extends BaseAdapter {
	private Context mContext;
	private List<Chart> list;
	private Map<Integer, List<Double>> valueMap;

	public ChartAdapter(Context context, List<Chart> list, Map<Integer, List<Double>> valueMap) {
 
		mContext = context;
		this.list = list;
		this.valueMap = valueMap;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			// 按当前所需的样式，确定new的布局
			convertView = inflater.inflate(R.layout.view_item_chart, parent, false);
			holder.text_title = (TextView) convertView.findViewById(R.id.text_title);
			holder.text_unit = (TextView) convertView.findViewById(R.id.text_unit);
			holder.layout_chart = (RelativeLayout) convertView.findViewById(R.id.layout_chart);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		setValue(holder, list.get(position), valueMap.get(position));
		return convertView;
	}

	public class ViewHolder {
		TextView text_title;
		TextView text_unit;
		RelativeLayout layout_chart;
	}

	private void setValue(ViewHolder holder, Chart chart, List<Double> valueList) {

		if (LanguageManager.LOCALE_ES.equals(LanguageManager.getLanguage(mContext))) {
			holder.text_title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 8);
		} else {
			holder.text_title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
		}
		holder.text_title.setText(chart.getTitle());
		holder.text_unit.setText(chart.getUnit());
		holder.layout_chart.removeAllViews();

		XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
		GraphicalView mChartView;

		mDataset = ChartHelper.setDataset_chart(mContext, valueList);
		mRenderer = ChartHelper.setRenderer_chart(mContext, valueList);

		mChartView = ChartFactory.getCubeLineChartView(mContext, mDataset, mRenderer, ChartHelper.SMOTHNESS);

		holder.layout_chart.addView(mChartView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

}
