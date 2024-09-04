package com.orbits.queuingsystem;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
//import android.util.Log;

public class DataLayer {
	private SQLiteDatabase mDatabase;
	private DbHelper mDbHelper;

	// private Context ctx;

	public DataLayer(Context context) {
		mDbHelper = new DbHelper(context);
		// ctx = context;
	}

	public LicenseDataHolder getLicenseKey() {
		// public String getLicenseKey() {
		LicenseDataHolder license = new LicenseDataHolder();
		// String strLicense = "";
		mDatabase = mDbHelper.getReadableDatabase();
		Cursor cursor = mDatabase.query("License", new String[] { "LicenseKey",
				"StartDate", "RunDate" , "TrialDays"}, null, null, null, null, null);
		/*
		 * Cursor cursor = mDatabase.query("License", new String[] {
		 * "LicenseKey", "StartDate", "RunDate","EndDate" }, null, null, null,
		 * null, null);
		 */
		if (cursor != null) {
			while (cursor.moveToNext()) {
				// strLicense = cursor.getString(0);
				license.setLicenseKey(cursor.getString(0));
				// Log.d(LicenseDialog.LOG_TAG,
				// "license retrieved as "+license.getLicenseKey());
				Calendar cal;
				try {
					cal = new GregorianCalendar();
					cal.setTimeInMillis(cursor.getLong(1));
					license.setStartDate(cal);
					String strLicenseStartDate = String.format(Locale.US,
							"%02d",
							license.getStartDate().get(Calendar.DAY_OF_MONTH))
							+ "-"
							+ String.format(Locale.US, "%02d", 1 + license
									.getStartDate().get(Calendar.MONTH))
							+ "-"
							+ license.getStartDate().get(Calendar.YEAR);
					
//					Log.d(MainActivity.LOG_TAG,
//							"reading license start date = "
//									+ strLicenseStartDate);
				} catch (Exception e) {
//					Log.d(MainActivity.LOG_TAG,
//							"error while reading start date");
				}
				try {
					cal = new GregorianCalendar();
					cal.setTimeInMillis(cursor.getLong(2));
					license.setLastRunDate(cal);
//					String strLicenseStartDate = String
//							.format(Locale.US, "%02d", license.getLastRunDate()
//									.get(Calendar.DAY_OF_MONTH))
//							+ "-"
//							+ String.format(Locale.US, "%02d", 1 + license
//									.getLastRunDate().get(Calendar.MONTH))
//							+ "-" + license.getLastRunDate().get(Calendar.YEAR);
//					Log.d(MainActivity.LOG_TAG,
//							"reading license last run date = "
//									+ strLicenseStartDate);
				} catch (Exception e) {
//					Log.d(MainActivity.LOG_TAG,
//							"error while reading last run date");
				}
				
				license.setTrialDays(cursor.getString(3));
				
				/*
				 * try { cal = new GregorianCalendar();
				 * cal.setTimeInMillis(cursor.getLong(3));
				 * license.setEndDate(cal); } catch (Exception e) {
				 * Log.d(LicenseDialog.LOG_TAG, "error while reading end date");
				 * }
				 */
			}
		}
		mDatabase.close();
		// return strLicense;
		return license;
	}

	/*public void saveLicenseKey(LicenseDataHolder license) {
		// public void saveLicenseKey(String strLicense) {
		mDatabase = mDbHelper.getWritableDatabase();
		// mDatabase.delete("License", null, null);
		// SimpleDateFormat dateFormat = new
		// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// Date date=new Date();
		Calendar calendar = new GregorianCalendar();
		calendar = license.getStartDate();
		String strLicenseStartDate = String.format(Locale.US, "%02d",
				calendar.get(Calendar.DAY_OF_MONTH))
				+ "-"
				+ String.format(Locale.US, "%02d",
						1 + calendar.get(Calendar.MONTH))
				+ "-"
				+ calendar.get(Calendar.YEAR);
		Log.d(LicenseDialog.LOG_TAG, "saving license start date = "
				+ strLicenseStartDate);
		ContentValues values = new ContentValues();
		Log.d(LicenseDialog.LOG_TAG,
				"saving license " + license.getLicenseKey());
		values.put("LicenseKey", license.getLicenseKey());
		values.put("StartDate", calendar.getTimeInMillis());
		int r = mDatabase.update("License", values, null, null);
		// values.put("Type", 1);
		// Toast.makeText(ctx,
		// "saved licence " + mDatabase.insert("License", null, values),
		// Toast.LENGTH_SHORT).show();
		// Log.d(LicenseDialog.LOG_TAG, r + " rows affected");
		mDatabase.close();
	}*/
	
	public void saveLicenseKey(LicenseDataHolder license) {
		// public void saveLicenseKey(String strLicense) {
		mDatabase = mDbHelper.getWritableDatabase();
		// mDatabase.delete("License", null, null);
		// SimpleDateFormat dateFormat = new
		// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// Date date=new Date();
		Calendar calendar = new GregorianCalendar();
		calendar = license.getStartDate();
//		String strLicenseStartDate = String.format(Locale.US, "%02d",
//				calendar.get(Calendar.DAY_OF_MONTH))
//				+ "-"
//				+ String.format(Locale.US, "%02d",
//						1 + calendar.get(Calendar.MONTH))
//				+ "-"
//				+ calendar.get(Calendar.YEAR);
//		Log.d(MainActivity.LOG_TAG, "saving license start date = "
//				+ strLicenseStartDate);
		ContentValues values = new ContentValues();
//		Log.d(MainActivity.LOG_TAG,
//				"saving license " + license.getLicenseKey());
		values.put("LicenseKey", license.getLicenseKey());
		values.put("StartDate", calendar.getTimeInMillis());
		values.put("TrialDays", license.getTrialDays());

		// values.put("EndDate", calendar.getTimeInMillis());

		int r = mDatabase.update("License", values, null, null);
		// values.put("Type", 1);
		// Toast.makeText(ctx,
		// "saved licence " + mDatabase.insert("License", null, values),
		// Toast.LENGTH_SHORT).show();
		// Log.d(LicenseDialog.LOG_TAG, r + " rows affected");
		mDatabase.close();
	}

	/*public void updateLastRunDate() {
		
		 * nirmal 04 march 2015 -- update last run date if current date is after
		 * last run date.
		 
		mDatabase = mDbHelper.getWritableDatabase();
		Cursor cursor = mDatabase.query("License", new String[] { "RunDate" },
				null, null, null, null, null);
		Calendar calLastRun = new GregorianCalendar();
		if (cursor != null) {
			while (cursor.moveToNext()) {
				try {
					calLastRun = new GregorianCalendar();
					calLastRun.setTimeInMillis(cursor.getLong(0));
				} catch (Exception e) {
					// TODO: handle exception
					calLastRun = new GregorianCalendar();
				}
			}
		}
		Calendar calNow = new GregorianCalendar();
		if (calNow.after(calLastRun)) {
			ContentValues values = new ContentValues();
			values.put("RunDate", new GregorianCalendar().getTimeInMillis());
			mDatabase.update("License", values, null, null);
		}
		mDatabase.close();
	}*/

	public void updateLastRunDate() {
		/*
		 * nirmal 04 march 2015 -- update last run date if current date is after
		 * last run date.
		 */
		mDatabase = mDbHelper.getWritableDatabase();
		Cursor cursor = mDatabase.query("License", new String[] { "RunDate" },
				null, null, null, null, null);
		Calendar calLastRun = new GregorianCalendar();
		if (cursor != null) {
			while (cursor.moveToNext()) {
				try {
					calLastRun = new GregorianCalendar();
					calLastRun.setTimeInMillis(cursor.getLong(0));
				} catch (Exception e) {
					// TODO: handle exception
					calLastRun = new GregorianCalendar();
				}
			}
		}
		Calendar calNow = new GregorianCalendar();
		if (calNow.after(calLastRun)) {
			ContentValues values = new ContentValues();
			values.put("RunDate", new GregorianCalendar().getTimeInMillis());
			mDatabase.update("License", values, null, null);
		}
		mDatabase.close();
	}
	
	/*public boolean clearSettings() {
		try {
			
			 * mDatabase = mDbHelper.getWritableDatabase();
			 * mDatabase.execSQL("DELETE FROM License"); mDatabase.close();
			 
			saveLicenseKey(new LicenseDataHolder());
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}*/

}
