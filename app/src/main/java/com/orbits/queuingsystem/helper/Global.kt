package com.orbits.queuingsystem.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.*
import android.util.*
import android.view.*
import com.orbits.queuingsystem.R
import java.util.*


@SuppressLint("StaticFieldLeak")
object Global {

    fun getDimension(activity: Activity, size: Double): Int {
        return if (Constants.DEVICE_DENSITY > 0) {
            //density saved in constant calculated on first time in splash if in case its 0 then calculate again
            (Constants.DEVICE_DENSITY * size).toInt()
        } else {
            ((getDeviceWidthInDouble(activity) / 320) * size).toInt()

        }
    }

    fun getDeviceWidthInDouble(activity: Activity): Double {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels.toDouble()
    }


    fun setFontSize(activity: Activity, value: Float): Float {
        return value / (activity.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    /*@SuppressLint("NewApi")
    fun getTypeFace(context: Context, fontStyle: String): Typeface {
        return when (fontStyle) {
            Constants.fontRegular -> context.resources.getFont(R.font.font_regular)
            Constants.fontBold -> context.resources.getFont(R.font.font_bold)
            Constants.fontMedium -> context.resources.getFont(R.font.font_medium)
            Constants.fontRegularRev -> context.resources.getFont(R.font.font_regular_rev)
            else -> context.resources.getFont(R.font.font_regular)
        }
    }*/

}
