package com.gzgamut.vivitar.global;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.UUID;

import android.annotation.SuppressLint;

public class Global {

	public static final String LOG_TAG = "LOG_TAG";
	public static final boolean DEBUG = true;

	// 蓝牙设备名称
	public static String DEVICE_NAME_ELECSCALESBH = "ElecScalesBH";
	
	public static final String PACKAGE_NAME = "com.gagzmut.innofit";
	public static final int REQUEST_ENABLE_BLUETOOTH = 11;

	public final static String ACTION_DEVICE_FOUND = Global.PACKAGE_NAME
			+ "ACTION_DEVICE_FOUND";

	public static UUID UUID_SERVICE = UUID
			.fromString("0000fff0-0000-1000-8000-00805f9b34fb");
	public static UUID UUID_CHARACTERISTIC_BOUND_DEVICE = UUID
			.fromString("0000fff0-0000-1000-8000-00805f9b34fb");
	public static UUID UUID_CHARACTERISTIC_COMMUNICATION = UUID
			.fromString("0000fff1-0000-1000-8000-00805f9b34fb");
	public static UUID UUID_CHARACTERISTIC_COMMUNICATION_SCALE = UUID
			.fromString("0000fff4-0000-1000-8000-00805f9b34fb");
	public static UUID UUID_CHARACTERISTIC_LOST_MODE = UUID
			.fromString("0000fff3-0000-1000-8000-00805f9b34fb");
	public static final UUID UUID_DESCRIPTOR_CONFIGURATION = UUID
			.fromString("00002902-0000-1000-8000-00805f9b34fb");

	// ///////////////////////////////////////////////////////////////////////////////

	public static final int TYPE_SEX_MALE = 0;
	public static final int TYPE_SEX_FEMALE = 1;

	public static final int TYPE_MONTH_MONTH = 0;
	public static final int TYPE_MONTH_QUARTER = 1;
	public static final int TYPE_MONTH_HALF = 2;
	public static final int TYPE_MONTH_YEAR = 3;
	// 0xce=206
	public static final byte TYPE_RECEIVE_DATA_OK = (byte) 0xce;
	// 0xca=202
	public static final byte TYPE_RECEIVE_DATA_NO = (byte) 0xca;

	public static final int TYPE_CHART_WEIGHT = 0;
	public static final int TYPE_CHART_BMI = 1;
	public static final int TYPE_CHART_FAT = 2;

	// ///////////////////////////////////////////////////////////////////////////////

	public static final double DEFAULT_HEIGHT = 180;
	/**
	 * 0.00
	 */
	public static final DecimalFormat df_double_2 = new DecimalFormat("0.00");
	/**
	 * 00
	 */
	public static final DecimalFormat df_int_2 = new DecimalFormat("00");
	/**
	 * yyyy-MM-dd HH:mm
	 */
	@SuppressLint("SimpleDateFormat")
	public static final SimpleDateFormat sdf_1 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");
	/**
	 * HH:mm
	 */
	@SuppressLint("SimpleDateFormat")
	public static final SimpleDateFormat sdf_0 = new SimpleDateFormat("HH:mm");
	/**
	 * yyy-MM-dd
	 */
	@SuppressLint("SimpleDateFormat")
	public static final SimpleDateFormat sdf_2 = new SimpleDateFormat(
			"yyyy-MM-dd");
	/**
	 * yyy-MM-dd HH
	 */

	@SuppressLint("SimpleDateFormat")
	public static final SimpleDateFormat sdf_3 = new SimpleDateFormat(
			"yyyy-MM-dd HH");

	/**
	 * yyy-MM-dd HH:mm:ss
	 */
	@SuppressLint("SimpleDateFormat")
	public static final SimpleDateFormat sdf_4 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	//
	public static final String KEY_USER_TYPE = "KEY_USER_TYPE";
	// handle what
	public static final int HANDLE_NUMBER1 = 222222;
	public static final int HANDLE_NUMBER2 = 111111;
	public static final int HANDLE_NUMBER3 = 3333333;
	public static final int HANDLE_NUMBER4 = 4444444;
	//
	public static final String BMI_NAME = "BMI_NAME";
	public static final String SAVETIME = "SAVETIME";
	public static final int HANDLE_TIME = 55555555;
	// 单位切换
	public static String UNIT = "UNIT";
	public static String UNIT_KG = "kg";
	public static String UNIT_LB = "lb";

	public static String UNIT_CMIN = "UNIT_CMIN";
	public static String UNIT_CM = "cm";
	public static String UNIT_IN = "in";
	// 判断是否第一次进入app
	public static String APP_FRIST = "APP_FRIST";
	public static String APP_ENTERED = "APP_ENTERED";
	public static String APP_ENTERING = "APP_ENTERING";
	//
	
}
