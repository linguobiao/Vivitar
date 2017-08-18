package com.gzgamut.vivitar.helper;

import java.util.List;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import com.gzgamut.vivitar.R.color;
import com.gzgamut.vivitar.been.Info;
import com.gzgamut.vivitar.global.Global;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.util.DisplayMetrics;
import android.util.Log;

public class ChartHelper {

//	
//	private static int TEXT_SIZE = 44;
//	public static float POINT_SIZE = 0f;
//	public static int PADDING_SIZE = 10;
//	public static int TOP = 40;
//	public static int LEFT = 40;
//	public static int RIGHT = 120;
//	public static int BOTTOM = 20;
//	public static int LINE_WIDTH = 4;
//	public static float SMOTHNESS = 0.0f;
	
	private static int TEXT_SIZE = 30;
	public static float POINT_SIZE = 0f;
	public static int PADDING_SIZE = 10;
	public static int TOP = 30;
	public static int LEFT = 30;
	public static int RIGHT = 80;
	public static int BOTTOM = -30;
	public static int LINE_WIDTH = 4;
	public static float SMOTHNESS = 0.0f;
	public static int RIGHT_SMALL = -30;

	public static void setTextSize(int densityDPI) {
		TEXT_SIZE = densityDPI * 1 / 10;
//		PADDING_SIZE = densityDPI * 5 / 80;
		TOP = densityDPI * 1 / 10;
		LEFT = densityDPI * 1 / 10;
		RIGHT = densityDPI * 5 / 20;
		BOTTOM = - densityDPI * 1 / 10 + densityDPI * 1 / 80;
//		LINE_WIDTH = densityDPI * 1 / 120;
		RIGHT_SMALL = - densityDPI * 1 / 10;

	}

	public static int LINE_COLOR;

	public static void setLineColor(int color) {
		LINE_COLOR = color;
	}

	public static int getDensityDpi(Activity context) {
		DisplayMetrics metric = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metric);
		int densityDpi = metric.densityDpi; // 屏幕密度DPI（120 / 160 / 240）

		return densityDpi;
	}

	private static XYMultipleSeriesRenderer setNormalXYMultipleSeriesRenderer(XYMultipleSeriesRenderer mRenderer, Typeface face) {
		if (mRenderer == null) {
			mRenderer = new XYMultipleSeriesRenderer();
		}

		// mRenderer.setTextTypeface(face);

		mRenderer.setShowGridY(true);
		// 是否支持图表移动
		mRenderer.setPanEnabled(true, false);
		// 坐标滑动上、下限
		mRenderer.setPanLimits(new double[] { 0, 0, 0, 0 });
		// 是否支持图表缩放
		mRenderer.setZoomEnabled(false, false);
		mRenderer.setClickEnabled(true); // 是否可以点击
		// mRenderer.setSelectableBuffer(50); //点击区域的大小

		// 边框的背景颜色
		mRenderer.setMarginsColor(Color.argb(0, 255, 255, 255));
		mRenderer.setBackgroundColor(Color.argb(0, 255, 255, 255));
		mRenderer.setApplyBackgroundColor(true);
		mRenderer.setGridColor(color.color_default);
		mRenderer.setLabelsColor(Color.BLACK);
		mRenderer.setXLabelsColor(Color.BLACK);
		mRenderer.setYLabelsColor(0, Color.BLACK);
		mRenderer.setAxesColor(Color.BLACK);
		mRenderer.setLegendHeight(60);

		mRenderer.setXAxisMin(0);
		mRenderer.setYAxisMin(0);

		mRenderer.setYLabelsAlign(Align.LEFT);
		mRenderer.setYLabelsPadding(PADDING_SIZE);

		mRenderer.setLabelsTextSize(TEXT_SIZE);
		mRenderer.setLegendTextSize(TEXT_SIZE);
		mRenderer.setAxisTitleTextSize(TEXT_SIZE);

		mRenderer.setPointSize(POINT_SIZE);

		mRenderer.setMargins(new int[] { TOP, LEFT - 20, BOTTOM, RIGHT });

		mRenderer.setYLabelsVerticalPadding(-15); // 设置Y轴标签上下移动

		return mRenderer;
	}

	private static XYSeriesRenderer setNormalXYSeriesRenderer(XYSeriesRenderer r) {
		if (r == null) {
			r = new XYSeriesRenderer();
		}
		r.setLineWidth(LINE_WIDTH);
		r.setPointStyle(PointStyle.CIRCLE);
		r.setFillPoints(true);

		return r;
	}

	/**
	 * 获取最大
	 * 
	 * @param valueList
	 * @return
	 */
	private static double getMaxValue(List<Double> valueList) {
		if (valueList != null && valueList.size() > 0) {
			double max = 0;
			for (Double value : valueList) {
				if (value > max) {
					max = value;
				}
			}
			if (max == 0) {
				return 100;
			}
			return max;
		}
		return 100;
	}

	/**
	 * 获取最大
	 * 
	 * @param valueList
	 * @return
	 */
	private static double getMaxValue_small(List<Info> valueList, int type) {
		if (valueList != null && valueList.size() > 0) {
			double max = 0;
			for (Info info : valueList) {
				if (info != null) {
					double value = 0;
				if (type == Global.TYPE_CHART_WEIGHT) {
					value = info.getWeight();
				} else if (type == Global.TYPE_CHART_BMI) {
					value = info.getBmi();
				} else if (type == Global.TYPE_CHART_WEIGHT) {
					value = info.getFat();
				}
					if (value > max) {
						max = value;
					}
				}
			}
			if (max == 0) {
				return 100;
			}
			return max;
		}
		return 100;
	}

	/**
	 * 设置today图表的样式
	 * 
	 * @param face
	 * @param length
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static XYMultipleSeriesRenderer setRenderer_chart(Context context, List<Double> valueList) {

		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
		mRenderer = setNormalXYMultipleSeriesRenderer(mRenderer, null);

		mRenderer.setXAxisMax(valueList.size());
		// mRenderer.setXLabels(22);
		mRenderer.setYAxisAlign(Align.RIGHT, 0);

		mRenderer.setShowAxes(false);
		mRenderer.setShowGridX(true);
		mRenderer.setShowGridY(false);
		mRenderer.setXLabels(0);
		mRenderer.setYLabels(0);
		mRenderer.setGridColor(Color.GRAY);

		mRenderer.setLabelsTextSize(TEXT_SIZE);
		mRenderer.setLegendTextSize(TEXT_SIZE);
		mRenderer.setAxisTitleTextSize(TEXT_SIZE);

		int maxValue = (int) getMaxValue(valueList);

		mRenderer.setYAxisMax(maxValue + (int) maxValue / 10);
		mRenderer.addYTextLabel(0.0, String.valueOf(0));
		mRenderer.addYTextLabel((maxValue + (int) maxValue / 10) / 2, String.valueOf((maxValue + (int) maxValue / 10) / 2));
		mRenderer.addYTextLabel(maxValue + (int) maxValue / 10, String.valueOf(maxValue + (int) maxValue / 10));
		mRenderer.setShowCustomTextGrid(true); // 显示自己定制的网格

		XYSeriesRenderer r = new XYSeriesRenderer();
		r = setNormalXYSeriesRenderer(r);
		r.setColor(Color.TRANSPARENT);
		r.setGradientEnabled(false);
		r.setFillBelowLine(true);
		r.setFillBelowLineColor(context.getResources().getColor(color.color_chart));

		mRenderer.addSeriesRenderer(r);

		return mRenderer;
	}

	// public static final DecimalFormat df_3 = new DecimalFormat("0.00");
	/**
	 * 设置 progress day awake图表的数据
	 * 
	 * @param historyList
	 * @return
	 */
	public static XYMultipleSeriesDataset setDataset_chart(Context context, List<Double> valueList) {

		XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
		XYSeries series = new XYSeries("");

		if (valueList.size() > 0) {
			for (int i = 0; i < valueList.size(); i++) {
				double value = valueList.get(i);
				Log.i("test", "value = " + value);
//				if (value != 0) {
					series.add(i, value);
//				}
			}

		}

		if (series.getItemCount() == 0) {
			series.add(valueList.size(), 0);
		}
		mDataset.addSeries(series);

		return mDataset;
	}

	/**
	 * 设置small图表的样式
	 * 
	 * @param face
	 * @param length
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static XYMultipleSeriesRenderer setRenderer_chart_small(Context context, List<Info> valueList, int type) {

		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
		mRenderer = setNormalXYMultipleSeriesRenderer(mRenderer, null);

		if (valueList != null) {
			Log.i("test", "**valueList size = " + valueList.size());
			mRenderer.setXAxisMax(valueList.size());
		} else {
			mRenderer.setXAxisMax(0);
		}
		
		mRenderer.setYLabelsVerticalPadding(0); // 设置Y轴标签上下移动
		
		// mRenderer.setXLabels(22);
		mRenderer.setYAxisAlign(Align.RIGHT, 0);

		mRenderer.setShowAxes(false);
		mRenderer.setShowGridX(true);
		mRenderer.setShowGridY(false);
		mRenderer.setXLabels(0);
		mRenderer.setYLabels(0);
		mRenderer.setGridColor(Color.GRAY);
		
		mRenderer.setYLabelsPadding(0);


		mRenderer.setMargins(new int[] { 0,0, BOTTOM, RIGHT_SMALL });


		int maxValue = (int) getMaxValue_small(valueList, type);

		mRenderer.setYAxisMax(maxValue + (int) maxValue / 10);
//		mRenderer.addYTextLabel(0.0, String.valueOf(0));
//		mRenderer.addYTextLabel((maxValue + (int) maxValue / 10) / 2, String.valueOf((maxValue + (int) maxValue / 10) / 2));
//		mRenderer.addYTextLabel(maxValue + (int) maxValue / 10, String.valueOf(maxValue + (int) maxValue / 10));
//		mRenderer.setShowCustomTextGrid(true); // 显示自己定制的网格

		XYSeriesRenderer r = new XYSeriesRenderer();
		r = setNormalXYSeriesRenderer(r);
		r.setColor(Color.TRANSPARENT);
		r.setGradientEnabled(false);
		r.setFillBelowLine(true);
		r.setFillBelowLineColor(context.getResources().getColor(color.color_chart));

		mRenderer.addSeriesRenderer(r);

		return mRenderer;
	}

	// public static final DecimalFormat df_3 = new DecimalFormat("0.00");
	/**
	 * 设置 small图表的数据
	 * 
	 * @param historyList
	 * @return
	 */
	public static XYMultipleSeriesDataset setDataset_chart_small(Context context, List<Info> valueList, int type) {

		XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
		XYSeries series = new XYSeries("");

		
		if (valueList != null && valueList.size() > 0) {
			for (int i = 0; i < valueList.size(); i++) {
				double value = 0;
				if (type == Global.TYPE_CHART_WEIGHT)  {
					value = valueList.get(i).getWeight();
				} else if (type == Global.TYPE_CHART_BMI) {
					value = valueList.get(i).getBmi();
				} else if (type == Global.TYPE_CHART_FAT) {
					value = valueList.get(i).getFat();
				}
				Log.i("test", "value = " + value);
				if (value != 0) {
					series.add(i, value);
				}
			}
		}
		if (series.getItemCount() == 0) {
			series.add(0, 0);
		}
		mDataset.addSeries(series);

		return mDataset;
	}

}
