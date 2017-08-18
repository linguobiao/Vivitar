package com.gzgamut.vivitar.helper;

import android.app.Activity;
import android.util.DisplayMetrics;

public class ShowValueHelper {
	
	/**
	 * 计算所有竖线的X值
	 * @param context
	 * @param amount
	 * @return
	 */
	public static float[] calculateXArray(Activity context, int amount, int sideWidth) {
		// 保存所有竖线的X值
		float[] xArray = new float[amount + 1];
		
		// 屏幕宽度
//		int width = context.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getRight();
		DisplayMetrics metric = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metric);
	    int width = metric.widthPixels;	
		
		// 显示数值view的宽度
		float length = width - sideWidth - sideWidth;
		// 间距
		float interval = length / amount;
		
		int mark = sideWidth;
		for (int i = 0; i < amount + 1; i++) {
			xArray[i] = mark + interval * i;
		}
		
		return xArray;
	}
	
	
	/**
	 * 根据滑动坐标X，获取划线的X坐标值
	 * @param xEvent
	 * @param xArray
	 * @return
	 */
	public static float getLineXValue(int xEvent, float[] xArray) {
		for (int i = 0; i + 1 < xArray.length; i++) {
			int middle = (int) (xArray[i] + (xArray[i + 1] - xArray[i]) /2) ;
			
			// 在两条竖线之间，值为左边竖线的值
//			if (xEvent >= xArray[i] && xEvent < xArray[i + 1]) {
//				
//				return xArray[i];
//			}
			
			if (xEvent >= xArray[i] && xEvent < middle) {
				return xArray[i];
				
			} else if (xEvent >= middle && xEvent < xArray[i + 1]) {
				return xArray[i + 1];
			}
		}
		
		return 0;
	}
	
	
	/**
	 * 根据滑动坐标X，获取下标
	 * @param xEvent
	 * @param xArray
	 * @return
	 */
	public static int getXValueIndex(int xEvent, float[] xArray) {
		for (int i = 0; i + 1 < xArray.length; i++) {
			// 在两条竖线之间，值为左边竖线的值
//			if (xEvent >= xArray[i] && xEvent < xArray[i + 1]) {
//
//				return i;
//			}
			
			int middle = (int) (xArray[i] + (xArray[i + 1] - xArray[i]) /2) ;
			
			if (xEvent >= xArray[i] && xEvent < middle) {
				return i;
				
			} else if (xEvent >= middle && xEvent < xArray[i + 1]) {
				return i + 1;
			}
		}
		
		return 0;
	}
	
	
	/**
	 * 获取屏幕DPI
	 * @param context
	 * @return
	 */
	public static int getDensityDpi(Activity context) {
		DisplayMetrics metric = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metric);
	    int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）	
	
		return densityDpi;
	}
	
	
	/**
	 * 获取屏幕宽度
	 * @param context
	 * @return
	 */
	public static int getScreenWidth(Activity context) {
		DisplayMetrics metric = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metric);
	    int width = metric.widthPixels;	
	    
	    return width;
	}
}
