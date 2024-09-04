package com.orbits.queuingsystem;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "qs";
	private static final int DATABASE_VERSION = 1;

	public DbHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// String strQuery =
		// "CREATE TABLE License (LicenseKey VARCHAR NOT NULL,InstallDate VARCHAR NOT NULL,Type INTEGER NOT NULL)";
		String strQuery = "CREATE TABLE License (LicenseKey VARCHAR NOT NULL,StartDate VARCHAR NOT NULL,RunDate VARCHAR NOT NULL, TrialDays VARCHAR NOT NULL)";
		db.execSQL(strQuery);
		Calendar calendar = new GregorianCalendar();
		ContentValues values = new ContentValues();
		// Log.d("lt", "saving license " + license.getLicenseKey());
		values.put("LicenseKey", "abc");
		values.put("StartDate", calendar.getTimeInMillis());
		values.put("RunDate", calendar.getTimeInMillis());
		values.put("TrialDays", "000");
		db.insert("License", null, values);
	}
	
	

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		String strQuery = "DROP TABLE IF EXISTS License";
		db.execSQL(strQuery);
		onCreate(db);
	}

}
