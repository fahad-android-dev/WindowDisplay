package com.orbits.queuingsystem

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Locale

//import android.util.Log;
class DataLayer(context: Context?) {
    private var mDatabase: SQLiteDatabase? = null
    private val mDbHelper = DbHelper(context)

    // private Context ctx;
    init {
        // ctx = context;
    }

    val licenseKey: LicenseDataHolder
        get() {
            // public String getLicenseKey() {
            val license = LicenseDataHolder()
            // String strLicense = "";
            mDatabase = mDbHelper.readableDatabase
            val cursor = mDatabase?.query(
                "License", arrayOf(
                    "LicenseKey",
                    "StartDate", "RunDate", "TrialDays"
                ), null, null, null, null, null
            )
            /*
		 * Cursor cursor = mDatabase.query("License", new String[] {
		 * "LicenseKey", "StartDate", "RunDate","EndDate" }, null, null, null,
		 * null, null);
		 */
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    // strLicense = cursor.getString(0);
                    license.licenseKey = cursor.getString(0)
                    // Log.d(LicenseDialog.LOG_TAG,
                    // "license retrieved as "+license.getLicenseKey());
                    var cal: Calendar
                    try {
                        cal = GregorianCalendar()
                        cal.setTimeInMillis(cursor.getLong(1))
                        license.startDate = cal
                        val strLicenseStartDate = (String.format(
                            Locale.US,
                            "%02d",
                            license.startDate[Calendar.DAY_OF_MONTH]
                        ) + "-"
                                + String.format(
                            Locale.US, "%02d", 1 + license
                                .startDate[Calendar.MONTH]
                        ) + "-"
                                + license.startDate[Calendar.YEAR])


//					Log.d(MainActivity.LOG_TAG,
//							"reading license start date = "
//									+ strLicenseStartDate);
                    } catch (e: Exception) {
//					Log.d(MainActivity.LOG_TAG,
//							"error while reading start date");
                    }
                    try {
                        cal = GregorianCalendar()
                        cal.setTimeInMillis(cursor.getLong(2))
                        license.lastRunDate = cal
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
                    } catch (e: Exception) {
//					Log.d(MainActivity.LOG_TAG,
//							"error while reading last run date");
                    }

                    license.trialDays = cursor.getString(3)


                    /*
				 * try { cal = new GregorianCalendar();
				 * cal.setTimeInMillis(cursor.getLong(3));
				 * license.setEndDate(cal); } catch (Exception e) {
				 * Log.d(LicenseDialog.LOG_TAG, "error while reading end date");
				 * }
				 */
                }
            }
            mDatabase?.close()
            // return strLicense;
            return license
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
    fun saveLicenseKey(license: LicenseDataHolder?) {
        // public void saveLicenseKey(String strLicense) {
        mDatabase = mDbHelper.writableDatabase
        // mDatabase.delete("License", null, null);
        // SimpleDateFormat dateFormat = new
        // SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // Date date=new Date();
        var calendar: Calendar? = GregorianCalendar()
        calendar = license?.startDate
        //		String strLicenseStartDate = String.format(Locale.US, "%02d",
//				calendar.get(Calendar.DAY_OF_MONTH))
//				+ "-"
//				+ String.format(Locale.US, "%02d",
//						1 + calendar.get(Calendar.MONTH))
//				+ "-"
//				+ calendar.get(Calendar.YEAR);
//		Log.d(MainActivity.LOG_TAG, "saving license start date = "
//				+ strLicenseStartDate);
        val values = ContentValues()
        //		Log.d(MainActivity.LOG_TAG,
//				"saving license " + license.getLicenseKey());
        values.put("LicenseKey", license?.licenseKey)
        values.put("StartDate", calendar!!.timeInMillis)
        values.put("TrialDays", license?.trialDays)

        // values.put("EndDate", calendar.getTimeInMillis());
        val r = mDatabase?.update("License", values, null, null)
        // values.put("Type", 1);
        // Toast.makeText(ctx,
        // "saved licence " + mDatabase.insert("License", null, values),
        // Toast.LENGTH_SHORT).show();
        // Log.d(LicenseDialog.LOG_TAG, r + " rows affected");
        mDatabase?.close()
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
    fun updateLastRunDate() {
        /*
		 * nirmal 04 march 2015 -- update last run date if current date is after
		 * last run date.
		 */
        mDatabase = mDbHelper.writableDatabase
        val cursor = mDatabase?.query(
            "License", arrayOf("RunDate"),
            null, null, null, null, null
        )
        var calLastRun: Calendar = GregorianCalendar()
        if (cursor != null) {
            while (cursor.moveToNext()) {
                try {
                    calLastRun = GregorianCalendar()
                    calLastRun.setTimeInMillis(cursor.getLong(0))
                } catch (e: Exception) {
                    // TODO: handle exception
                    calLastRun = GregorianCalendar()
                }
            }
        }
        val calNow: Calendar = GregorianCalendar()
        if (calNow.after(calLastRun)) {
            val values = ContentValues()
            values.put("RunDate", GregorianCalendar().timeInMillis)
            mDatabase?.update("License", values, null, null)
        }
        mDatabase?.close()
    } /*public boolean clearSettings() {
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
