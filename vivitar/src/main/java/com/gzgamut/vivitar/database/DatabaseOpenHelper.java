package com.gzgamut.vivitar.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
	private static String TAG = "DatabaseOpenHelper";

	public DatabaseOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DatabaseAdapter.CREATE_TABLE_USER);
		db.execSQL(DatabaseAdapter.CREATE_TABLE_INFO);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + DatabaseAdapter.TABLE_USER);
		db.execSQL("DROP TABLE IF EXISTS " + DatabaseAdapter.TABLE_INFO);
		onCreate(db);
	}

}
