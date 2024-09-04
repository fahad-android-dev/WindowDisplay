package com.orbits.queuingsystem

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper
import java.util.Calendar
import java.util.GregorianCalendar

class DbHelper : SQLiteOpenHelper {
    constructor(
        context: Context?, name: String?, factory: CursorFactory?,
        version: Int
    ) : super(context, name, factory, version)

    constructor(context: Context?) : super(context, DATABASE_NAME, null, DATABASE_VERSION)

    override fun onCreate(db: SQLiteDatabase) {
        // String strQuery =
        // "CREATE TABLE License (LicenseKey VARCHAR NOT NULL,InstallDate VARCHAR NOT NULL,Type INTEGER NOT NULL)";
        val strQuery =
            "CREATE TABLE License (LicenseKey VARCHAR NOT NULL,StartDate VARCHAR NOT NULL,RunDate VARCHAR NOT NULL, TrialDays VARCHAR NOT NULL)"
        db.execSQL(strQuery)
        val calendar: Calendar = GregorianCalendar()
        val values = ContentValues()
        // Log.d("lt", "saving license " + license.getLicenseKey());
        values.put("LicenseKey", "abc")
        values.put("StartDate", calendar.timeInMillis)
        values.put("RunDate", calendar.timeInMillis)
        values.put("TrialDays", "000")
        db.insert("License", null, values)
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // TODO Auto-generated method stub
        val strQuery = "DROP TABLE IF EXISTS License"
        db.execSQL(strQuery)
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "qs"
        private const val DATABASE_VERSION = 1
    }
}
