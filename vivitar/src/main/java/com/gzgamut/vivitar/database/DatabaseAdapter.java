package com.gzgamut.vivitar.database;

import java.util.Calendar;
import java.util.Date;

import com.gzgamut.vivitar.global.Global;
import com.gzgamut.vivitar.helper.CalendarHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatabaseAdapter {
	// 数据库名称
	public static final String DATABASE_NAME = "innofit";
	// 数据库版本号
	public static final int DATABASE_VERSION = 1;
	//
	public static final String TABLE_USER = "TABLE_USER";
	public static final String KEY_USERID = "_id";
	public static final String KEY_USERNAME = "KEY_USERNAME";
	public static final String KEY_SEX = "KEY_SEX";
	public static final String KEY_AGE = "KEY_AGE";
	public static final String KEY_HEIGHT = "KEY_HEIGHT";
	public static final String KEY_HEAD = "KEY_HEAD";
	public static final String KEY_GOAL_WEIGHT = "KEY_GOAL_WEIGHT";
	public static final String KEY_UNIT = "KEY_UNIT";
	//
	public static final String TABLE_INFO = "TABLE_INFO";
	public static final String KEY_INFOID = "_id";
	public static final String KEY_WEIGHT = "KEY_WEIGHT";
	public static final String KEY_FAT = "KEY_FAT";
	public static final String KEY_WATER = "KEY_WATER";
	public static final String KEY_MUSCLE = "KEY_MUSCLE";
	public static final String KEY_BONE = "KEY_BONE";
	public static final String KEY_BMR = "KEY_BMR";
	public static final String KEY_BMI = "KEY_BMI";
	public static final String KEY_PUSER = "KEY_PUSER";
	public static final String KEY_DATE = "KEY_DATE";
	public static final String KEY_KCAL = "KEY_KCAL";
	//
	String TAG = "DatabaseAdapter";
	/*
	 * 数据表
	 */
	public static final String CREATE_TABLE_INFO = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_INFO
			+ "("
			+ KEY_INFOID
			+ " integer primary key autoincrement, "
			+ KEY_WEIGHT
			+ " double not null, "
			+ KEY_FAT
			+ " double not null, "
			+ KEY_WATER
			+ " double not null, "
			+ KEY_MUSCLE
			+ " double not null ,"
			+ KEY_BONE
			+ " double not null, "
			+ KEY_BMI
			+ " double not null,"
			+ KEY_KCAL
			+ " double not null,"
			+ KEY_DATE
			+ " text not null,"
			+ KEY_PUSER + " int not null" + "); ";

	/*
	 * 用户信息表
	 */
	public static final String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_USER
			+ "("
			+ KEY_USERID
			+ " integer primary key autoincrement, "
			+ KEY_USERNAME
			+ " text not null, "
			+ KEY_UNIT
			+ " text, "
			+ KEY_HEAD
			+ " blob not null, "
			+ KEY_SEX
			+ " int not null, "
			+ KEY_AGE
			+ " int not null, "
			+ KEY_GOAL_WEIGHT
			+ " double not null, "
			+ KEY_HEIGHT + " double not null" + "); ";

	private final Context context;
	private DatabaseOpenHelper databaseOpenHelper;
	private SQLiteDatabase db;

	public DatabaseAdapter(Context ctx) {
		this.context = ctx;
		databaseOpenHelper = new DatabaseOpenHelper(context, DATABASE_NAME,
				null, DATABASE_VERSION);
	}

	/**
	 * open database
	 * 
	 */
	public DatabaseAdapter openDatabase() {
		db = databaseOpenHelper.getWritableDatabase();
		return this;
	}

	/**
	 * 查询电子称数据
	 */
	public Cursor query_scale(int profileID, Calendar date) {

		String dateDay = CalendarHelper.getYyyy_MM_dd_HH_mm(date);
		Log.d(TAG, "query info " + dateDay);
		Cursor mCursor = db.query(true, TABLE_INFO, new String[] { KEY_DATE,
				KEY_WEIGHT, KEY_FAT, KEY_WATER, KEY_MUSCLE, KEY_BONE, KEY_KCAL,
				KEY_BMI, KEY_PUSER }, KEY_DATE + "='" + dateDay + "' and "
				+ KEY_PUSER + "=" + profileID, null, null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	/**
	 * 查询电子称数据
	 */
	public Cursor query_scale(int profileID) {

		Cursor mCursor = db.query(true, TABLE_INFO, new String[] { KEY_DATE,
				KEY_WEIGHT, KEY_FAT, KEY_WATER, KEY_MUSCLE, KEY_BONE, KEY_KCAL,
				KEY_BMI, KEY_PUSER }, KEY_PUSER + "=" + profileID, null, null,
				null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	/**
	 * 查询电子称数据
	 */
	public Cursor query_scale() {

		Cursor mCursor = db.query(true, TABLE_INFO, new String[] { KEY_DATE,
				KEY_WEIGHT, KEY_FAT, KEY_WATER, KEY_MUSCLE, KEY_BONE, KEY_KCAL,
				KEY_BMI, KEY_PUSER }, null, null, null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	/**
	 * query info
	 * 
	 * @param dateBegin
	 * @param dateEnd
	 * @return
	 */
	public Cursor query_info(int profileID, Calendar dateBegin, Calendar dateEnd) {
		Log.i("test", "query history userId: " + profileID + "  day_ begin:"
				+ Global.sdf_2.format(dateBegin.getTime()) + ", end:"
				+ Global.sdf_2.format(dateEnd.getTime()));
		String date_begin = Global.sdf_2.format(dateBegin.getTime());
		String date_end = Global.sdf_2.format(dateEnd.getTime());
		Cursor mCursor = db.query(true, TABLE_INFO, new String[] { KEY_DATE,
				KEY_WEIGHT, KEY_FAT, KEY_WATER, KEY_MUSCLE, KEY_BONE, KEY_KCAL,
				KEY_BMI, KEY_PUSER }, KEY_DATE + ">='" + date_begin + "' and "
				+ KEY_DATE + "<'" + date_end + "' and " + KEY_PUSER + "="
				+ profileID, null, null, null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	/**
	 * close database
	 */
	public void closeDatabase() {
		databaseOpenHelper.close();
	}

	/**
	 * 插入电子称数据
	 * 
	 * @param weight
	 * @return
	 */
	public long insert_scale(int profileID, Calendar date, double weight,
			double bmi, double fat, double water, double muscle, double bone,
			double kcal) {
		String dateDay = CalendarHelper.getYyyy_MM_dd_HH_mm_ss(date);
		Log.i("dateDay", "dateDay=" + dateDay);
		Log.d(TAG, "insert scale " + dateDay + ", weight:" + weight + ", bmi:"
				+ bmi + ", profile id:" + profileID);

		ContentValues initialValues = new ContentValues();

		initialValues.put(KEY_DATE, dateDay);
		initialValues.put(KEY_WEIGHT, weight);
		initialValues.put(KEY_BMI, bmi);
		initialValues.put(KEY_PUSER, profileID);
		initialValues.put(KEY_FAT, fat);
		initialValues.put(KEY_WATER, water);
		initialValues.put(KEY_MUSCLE, muscle);
		initialValues.put(KEY_KCAL, kcal);
		initialValues.put(KEY_BONE, bone);

		return db.insert(TABLE_INFO, null, initialValues);
	}

	/**
	 * 更新电子称数据
	 * 
	 * @param weight
	 * @return
	 */
	public int update_scale(int profileID, Calendar date, double weight,
			double bmi, double fat, double water, double muscle, double bone,
			double kcal) {
		String dateDay = CalendarHelper.getYyyy_MM_dd_HH_mm_ss(date);
		ContentValues initialValues = new ContentValues();

		initialValues.put(KEY_DATE, dateDay);
		initialValues.put(KEY_WEIGHT, weight);
		initialValues.put(KEY_BMI, bmi);
		initialValues.put(KEY_PUSER, profileID);
		initialValues.put(KEY_FAT, fat);
		initialValues.put(KEY_WATER, water);
		initialValues.put(KEY_MUSCLE, muscle);
		initialValues.put(KEY_BONE, bone);
		initialValues.put(KEY_KCAL, kcal);

		return db.update(TABLE_INFO, initialValues, KEY_DATE + "='" + dateDay
				+ "' and " + KEY_PUSER + "=" + profileID, null);
	}

	/**
	 * delete a day's history hour data
	 * 
	 * @param date
	 * @return
	 */
	public void deleteScale(Calendar id) {
		String testTime = Global.sdf_4.format(id.getTime());
		Log.i("infos", testTime + "        000");
		db.delete(TABLE_INFO, KEY_DATE + "='" + testTime + "'", null);

	}

	public void deleteScaleById(int id) {

		db.delete(TABLE_INFO, KEY_PUSER + "='" + id + "'", null);

	}

	public void deleteUser(int id) {
		// int ida=id+1;
		// Log.i("id", "deleteUser" + "ida" + ida);
		db.delete(TABLE_USER, KEY_USERID + "='" + id + "'", null);
	}

	/**
	 * 插入用户数据
	 * 
	 * @param weight
	 * @return
	 */
	public long insert_user(int profileID, String username, byte[] head,
			int sex, int age, double height, double goalWeight, String unit) {
		Log.i(TAG, "profileID=" + profileID + "username=" + username + "sex="
				+ sex + "age=" + age + "height=" + height + "unit=" + unit);

		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_USERID, profileID);
		initialValues.put(KEY_USERNAME, username);
		initialValues.put(KEY_HEAD, head);
		initialValues.put(KEY_SEX, sex);
		initialValues.put(KEY_AGE, age);
		initialValues.put(KEY_HEIGHT, height);
		initialValues.put(KEY_GOAL_WEIGHT, goalWeight);
		initialValues.put(KEY_UNIT, unit);

		return db.insert(TABLE_USER, null, initialValues);
	}

	/**
	 * 插入用户数据
	 * 
	 * @param weight
	 * @return
	 */
	public long insert_user_(String username, byte[] head, int sex, int age,
			double height, double goalWeight) {
		Log.i("app", "=======c======");
		Log.i(TAG, "username=" + username + "sex=" + sex + "age=" + age
				+ "height=" + height);

		ContentValues initialValues = new ContentValues();
		// initialValues.put(KEY_USERID, profileID);
		initialValues.put(KEY_USERNAME, username);
		initialValues.put(KEY_HEAD, head);
		initialValues.put(KEY_SEX, sex);
		initialValues.put(KEY_AGE, age);
		initialValues.put(KEY_HEIGHT, height);
		initialValues.put(KEY_GOAL_WEIGHT, goalWeight);

		return db.insert(TABLE_USER, null, initialValues);
	}

	/**
	 * 更新用户数据
	 * 
	 * @param weight
	 * @return
	 */
	public long update_user(int profileID, String username, byte[] head,
			int sex, int age, double height, double goalWeight, String unit) {
		Log.i(TAG, "profileID=" + profileID + "username=" + username + "sex="
				+ sex + "age=" + age + "height=" + height);

		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_USERID, profileID);
		initialValues.put(KEY_USERNAME, username);
		initialValues.put(KEY_HEAD, head);
		initialValues.put(KEY_SEX, sex);
		initialValues.put(KEY_AGE, age);
		initialValues.put(KEY_HEIGHT, height);
		initialValues.put(KEY_GOAL_WEIGHT, goalWeight);
		initialValues.put(KEY_UNIT, unit);

		return db.update(TABLE_USER, initialValues, KEY_USERID + "="
				+ profileID, null);
	}

	/**
	 * 查询用户数据
	 */
	public Cursor query_userAll() {

		Cursor mCursor = db.query(true, TABLE_USER, new String[] { KEY_USERID,
				KEY_USERNAME, KEY_HEAD, KEY_SEX, KEY_AGE, KEY_HEIGHT,
				KEY_GOAL_WEIGHT }, null, null, null, null, null, null);
		Log.i("Tag", mCursor.getColumnCount() + "--");
		// while (mCursor.moveToNext()) {
		// Log.i("tag", mCursor.getString(mCursor.getColumnIndex(KEY_USERID)));
		// Log.i("tag",
		// mCursor.getString(mCursor.getColumnIndex(KEY_USERNAME))+"00");
		// }
		// if (mCursor != null) {
		// mCursor.moveToFirst();
		// }

		return mCursor;
	}

	/**
	 * 查询用户数据
	 */
	public Cursor query_user(int profileID) {
		Log.i("user", "profileID=" + profileID);
		Cursor mCursor = db.query(true, TABLE_USER, new String[] {
				KEY_USERNAME, KEY_HEAD, KEY_SEX, KEY_AGE, KEY_HEIGHT,
				KEY_GOAL_WEIGHT, KEY_UNIT }, KEY_USERID + "=" + profileID,
				null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
}
