package com.gzgamut.vivitar.database;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import com.gzgamut.vivitar.been.Info;
import com.gzgamut.vivitar.been.User;
import com.gzgamut.vivitar.global.Global;
import com.gzgamut.vivitar.helper.CalendarHelper;

public class DatabaseProvider {

	public static List<Info> queryScale(Context context, int profileID) {
		List<Info> infos = new ArrayList<Info>();
		if (context != null) {
			DatabaseAdapter databaseAdapter = new DatabaseAdapter(context);
			databaseAdapter.openDatabase();
			Cursor cursor = databaseAdapter.query_scale(profileID);

			if (cursor.moveToFirst()) {
				do {
					Info info = new Info();
					try {
						Date _date = Global.sdf_4.parse(cursor.getString(cursor
								.getColumnIndex(DatabaseAdapter.KEY_DATE)));

						Calendar _cal = Calendar.getInstance();
						_cal.setTime(_date);
						Log.i("time", "_cal=" + _cal);
						info.setDate(_cal);
					} catch (ParseException e) {
						e.printStackTrace();

						Calendar _cal = Calendar.getInstance();
						info.setDate(_cal);
					}
					info.setpUser(cursor.getInt(cursor
							.getColumnIndex(DatabaseAdapter.KEY_PUSER)));
					info.setWeight(cursor.getDouble(cursor
							.getColumnIndex(DatabaseAdapter.KEY_WEIGHT)));
					info.setBmi(cursor.getDouble(cursor
							.getColumnIndex(DatabaseAdapter.KEY_BMI)));
					info.setFat(cursor.getDouble(cursor
							.getColumnIndex(DatabaseAdapter.KEY_FAT)));
					info.setWater(cursor.getDouble(cursor
							.getColumnIndex(DatabaseAdapter.KEY_WATER)));
					info.setBone(cursor.getDouble(cursor
							.getColumnIndex(DatabaseAdapter.KEY_BONE)));
					info.setMuscle(cursor.getDouble(cursor
							.getColumnIndex(DatabaseAdapter.KEY_MUSCLE)));
					info.setKcal(cursor.getDouble(cursor
							.getColumnIndex(DatabaseAdapter.KEY_KCAL)));
					infos.add(info);

				} while (cursor.moveToNext());
			}
			cursor.close();
			databaseAdapter.closeDatabase();

		}
		return infos;
	}

	/**
	 * query Info
	 * 
	 * @param context
	 * @return
	 */
	public static Info queryScale(Context context, int profileID, Calendar date) {

		if (context != null && date != null) {

			DatabaseAdapter databaseAdapter = new DatabaseAdapter(context);
			databaseAdapter.openDatabase();
			Cursor cursor = databaseAdapter.query_scale(profileID, date);

			if (cursor.moveToFirst()) {
				Info info = new Info();
				Date _date = new Date();
				try {
					_date = Global.sdf_2.parse(cursor.getString(0));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				Calendar _cal = Calendar.getInstance();
				_cal.setTime(_date);

				info.setDate(_cal);

				info.setWeight(cursor.getDouble(1));
				info.setBmi(cursor.getDouble(2));
				databaseAdapter.closeDatabase();
				return info;
			}

			cursor.close();
			databaseAdapter.closeDatabase();
		}

		return null;

	}

	/**
	 * 查询一个时间段的历史数据
	 * 
	 * @param context
	 * @param begin
	 * @param end
	 * @return
	 */
	public static List<Info> queryScale(Context context, int profileID,
			Calendar begin, Calendar end) {

		List<Info> infoList = new ArrayList<Info>();

		if (context != null && begin != null && end != null) {

			begin = CalendarHelper.setDayFormat(begin);
			end = CalendarHelper.setDayFormat(end);

			DatabaseAdapter databaseAdapter = new DatabaseAdapter(context);
			// 打开数据库
			databaseAdapter.openDatabase();
			Cursor cursor = databaseAdapter.query_info(profileID, begin, end);
			if (cursor.moveToFirst()) {
				do {
					Info info = new Info();
					Date _date = new Date();
					try {
						_date = Global.sdf_2.parse(cursor.getString(0));
					} catch (ParseException e) {
						e.printStackTrace();
					}

					// KEY_DATE,
					// KEY_WEIGHT, KEY_FAT, KEY_WATER, KEY_MUSCLE, KEY_BONE,
					// KEY_BMR,KEY_KCAL,
					// KEY_BMI, KEY_PUSER

					Calendar _cal = Calendar.getInstance();
					_cal.setTime(_date);

					info.setDate(_cal);

					info.setWeight(cursor.getDouble(1));
					info.setFat(cursor.getDouble(2));
					info.setWater(cursor.getDouble(3));
					info.setMuscle(cursor.getDouble(4));
					info.setBone(cursor.getDouble(5));
					// info.setBmr(cursor.getDouble(6));
					info.setKcal(cursor.getDouble(6));
					info.setBmi(cursor.getDouble(7));

					infoList.add(info);
				} while (cursor.moveToNext());
			}

			databaseAdapter.closeDatabase();
		}

		return infoList;
	}

	/*
	 * 查询全部
	 */
	public static List<Info> queryScale(Context context) {
		List<Info> infos = new ArrayList<Info>();
		if (context != null) {
			DatabaseAdapter databaseAdapter = new DatabaseAdapter(context);
			databaseAdapter.openDatabase();
			Cursor cursor = databaseAdapter.query_scale();
			Log.i("info", cursor.getColumnCount() + "");

			while (cursor.moveToNext()) {
				Info info = new Info();
				info.setpUser(cursor.getInt(cursor
						.getColumnIndex(DatabaseAdapter.KEY_PUSER)));
				info.setWeight(cursor.getDouble(cursor
						.getColumnIndex(DatabaseAdapter.KEY_WEIGHT)));
				info.setBmi(cursor.getDouble(cursor
						.getColumnIndex(DatabaseAdapter.KEY_BMI)));
				// info.setBmr(cursor.getDouble(cursor
				// .getColumnIndex(DatabaseAdapter.KEY_BMR)));
				info.setFat(cursor.getDouble(cursor
						.getColumnIndex(DatabaseAdapter.KEY_FAT)));
				info.setWater(cursor.getDouble(cursor
						.getColumnIndex(DatabaseAdapter.KEY_WATER)));
				info.setBone(cursor.getDouble(cursor
						.getColumnIndex(DatabaseAdapter.KEY_BONE)));
				info.setMuscle(cursor.getDouble(cursor
						.getColumnIndex(DatabaseAdapter.KEY_MUSCLE)));
				info.setKcal(cursor.getDouble(cursor
						.getColumnIndex(DatabaseAdapter.KEY_KCAL)));
				infos.add(info);
			}
			cursor.close();
			databaseAdapter.closeDatabase();
		}

		return infos;

	}

	public static List<User> queryUserAll(Context context) {

		DatabaseAdapter databaseAdapter = new DatabaseAdapter(context);
		databaseAdapter.openDatabase();
		Cursor cursor = databaseAdapter.query_userAll();
		List<User> listUser = new ArrayList<User>();

		while (cursor.moveToNext()) {
		
			User user = new User();
			user.setUserId(cursor.getInt(cursor
					.getColumnIndex(DatabaseAdapter.KEY_USERID)));

			user.setUsername(cursor.getString(cursor
					.getColumnIndex(DatabaseAdapter.KEY_USERNAME)));

			byte[] in = cursor.getBlob(cursor
					.getColumnIndex(DatabaseAdapter.KEY_HEAD));
			if (in != null) {
				user.setHead(BitmapFactory.decodeByteArray(in, 0, in.length));
			}

			user.setAge(cursor.getInt(cursor
					.getColumnIndex(DatabaseAdapter.KEY_AGE)));
			user.setSex(cursor.getInt(cursor
					.getColumnIndex(DatabaseAdapter.KEY_SEX)));
			user.setHeight(cursor.getDouble(cursor
					.getColumnIndex(DatabaseAdapter.KEY_HEIGHT)));
			user.setGoalWeight(cursor.getDouble(cursor
					.getColumnIndex(DatabaseAdapter.KEY_GOAL_WEIGHT)));
			listUser.add(user);
		}

		databaseAdapter.closeDatabase();
		cursor.close();
		return listUser;

	}

	/**
	 * insert info
	 * 
	 * @param context
	 * @param info
	 */

	public static void insertScale(Context context, int profileID, Info info,
			Calendar calendar) {
		if (info != null) {
			DatabaseAdapter databaseAdapter = new DatabaseAdapter(context);
			databaseAdapter.openDatabase();

			databaseAdapter.insert_scale(profileID, calendar, info.getWeight(),
					info.getBmi(), info.getFat(), info.getWater(),
					info.getMuscle(), info.getBone(), info.getKcal());

			databaseAdapter.closeDatabase();
		}
	}

	/**
	 * update scale
	 * 
	 * @param context
	 * @param scale
	 */
	public static void updateScale(Context context, int profileID, Info info,
			Calendar calendar) {
		if (info != null) {
			DatabaseAdapter databaseAdapter = new DatabaseAdapter(context);
			databaseAdapter.openDatabase();
			databaseAdapter.update_scale(profileID, calendar, info.getWeight(),
					info.getBmi(), info.getFat(), info.getWater(),
					info.getMuscle(), info.getBone(), info.getKcal());
			databaseAdapter.closeDatabase();
		}
	}

	public static void insertUser(Context context, int profileID, User user) {
		if (user != null) {
			DatabaseAdapter databaseAdapter = new DatabaseAdapter(context);
			databaseAdapter.openDatabase();

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			Bitmap bmp = user.getHead();
			if (bmp != null) {
				bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
			}

			databaseAdapter.insert_user(profileID, user.getUsername(),
					os.toByteArray(), user.getSex(), user.getAge(),
					user.getHeight(), user.getGoalWeight(), user.getUnit());

			databaseAdapter.closeDatabase();
		}
	}

	public static void insertUser_(Context context, User user) {
		Log.i("app", "=======b======");
		if (user != null) {
			DatabaseAdapter databaseAdapter = new DatabaseAdapter(context);
			databaseAdapter.openDatabase();

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			Bitmap bmp = user.getHead();
			if (bmp != null) {
				bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
			}

			databaseAdapter.insert_user_(user.getUsername(), os.toByteArray(),
					user.getSex(), user.getAge(), user.getHeight(),
					user.getGoalWeight());

			databaseAdapter.closeDatabase();
		}
	}

	public static void updateUser(Context context, int profileID, User user) {
		if (user != null) {
			DatabaseAdapter databaseAdapter = new DatabaseAdapter(context);
			databaseAdapter.openDatabase();

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			Bitmap bmp = user.getHead();
			if (bmp != null) {
				bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
			}
			databaseAdapter.update_user(profileID, user.getUsername(),
					os.toByteArray(), user.getSex(), user.getAge(),
					user.getHeight(), user.getGoalWeight(), user.getUnit());
			databaseAdapter.closeDatabase();
		}
	}

	public static User queryUser(Context context, int profileID) {

		if (context != null) {

			DatabaseAdapter databaseAdapter = new DatabaseAdapter(context);
			databaseAdapter.openDatabase();
			Cursor cursor = databaseAdapter.query_user(profileID);
			Log.i("tag", cursor.moveToFirst() + "---dd");
			if (cursor != null && cursor.getCount() != 0) {
				User user = new User();
				user.setUsername(cursor.getString(cursor
						.getColumnIndex(DatabaseAdapter.KEY_USERNAME)));

				byte[] in = cursor.getBlob(cursor
						.getColumnIndex(DatabaseAdapter.KEY_HEAD));
				if (in != null) {
					user.setHead(BitmapFactory
							.decodeByteArray(in, 0, in.length));
				}

				user.setAge(cursor.getInt(cursor
						.getColumnIndex(DatabaseAdapter.KEY_AGE)));
				user.setSex(cursor.getInt(cursor
						.getColumnIndex(DatabaseAdapter.KEY_SEX)));
				user.setHeight(cursor.getDouble(cursor
						.getColumnIndex(DatabaseAdapter.KEY_HEIGHT)));
				user.setGoalWeight(cursor.getDouble(cursor
						.getColumnIndex(DatabaseAdapter.KEY_GOAL_WEIGHT)));

				user.setUnit(cursor.getString(cursor
						.getColumnIndex(DatabaseAdapter.KEY_UNIT)));
				databaseAdapter.closeDatabase();
				return user;
			}

			cursor.close();
			databaseAdapter.closeDatabase();
		}

		return null;

	}

	//

	public static void deleteScale(Context context, Calendar id) {
		Log.i("infos", "provideid==" + id);
		DatabaseAdapter databaseAdapter = new DatabaseAdapter(context);
		databaseAdapter.openDatabase();
		databaseAdapter.deleteScale(id);
		databaseAdapter.closeDatabase();
	}
	public static void deleteScaleById(Context context, int id) {
		Log.i("infos", "provideid==" + id);
		DatabaseAdapter databaseAdapter = new DatabaseAdapter(context);
		databaseAdapter.openDatabase();
		databaseAdapter.deleteScaleById(id);
		databaseAdapter.closeDatabase();
	}

	public static void deleteUser(Context context, int id) {
		DatabaseAdapter databaseAdapter = new DatabaseAdapter(context);
		databaseAdapter.openDatabase();
		databaseAdapter.deleteUser(id);
		databaseAdapter.closeDatabase();
	}

}
