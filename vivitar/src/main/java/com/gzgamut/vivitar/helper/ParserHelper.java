package com.gzgamut.vivitar.helper;

import java.util.Calendar;

import android.util.Log;

import com.gzgamut.vivitar.been.Info;

public class ParserHelper {

	private static String TAG = "ParserHelper";

	/**
	 * 解析电子称数据
	 * 
	 * @param value
	 * @return
	 */
	public static Info parseScales(byte[] value, Calendar date) {
		// [-54, 9, -103, -91, 0, 103, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
		int[] dat = new int[16];
		double weight = 0;
		int pUser = 0;
		double bone = 0;
		double muscle = 0;
		// double bmr = 0;
		double fat = 0;
		double water = 0;
		double kcal = 0;
		Info info = new Info();
		for (int i = 0; i < 16; i++) {
			if (value[i] >= 0)
				dat[i] = (int) value[i];
			else {
				dat[i] = (int) (256 + value[i]);
			}
		}
		pUser = dat[1];
		weight = (double) (dat[4] * 256 + dat[5]) / 10;
		fat = (double) (dat[6] * 256 + dat[7]) / 10;
		bone = (double) dat[8] / 10;
		muscle = (double) (dat[9] * 256 + dat[10]) / 10;

		water = (double) (dat[12] * 256 + dat[13]) / 10;
		kcal = (double) (dat[14] * 256 + dat[15]);

		info.setWeight(weight);
		info.setFat(dat[6]);
		info.setpUser(pUser - 1);
		info.setBmi(bone);
		info.setMuscle(muscle);
		info.setWater(water);
		info.setFat(fat);
		info.setBone(bone);
		info.setKcal(kcal);
		int i0 = dat[0];

		Log.i("demo", "dat[0]==" + i0);
		Log.i(TAG, "wight=" + weight + " pUser=" + pUser + " fat" + fat
				+ "000000000" + " bone=" + bone + " muscle=" + muscle
				+ " water=" + water + " kcal=" + kcal);

		return info;
	}

	public static boolean isSpecialKey(byte[] value) {
		StringBuffer sb = new StringBuffer();
		for (byte b : value) {
			int x = 0;
			if (b >= 0)
				x = (int) b;
			else {
				x = (int) (256 + b);
			}

			sb.append(Integer.toHexString(x));
		}

		if (sb.toString().equals("aa111111111111111111bc")) {
			return true;
		}

		return false;
	}
}
