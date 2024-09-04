package com.orbits.queuingsystem

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.animation.LinearInterpolator
import android.widget.Scroller
import android.widget.TextView
import kotlin.math.max

class ScrollTextViewArabic : TextView {
    // scrolling feature
    private var arSlr: Scroller? = null

    // milliseconds for a round of scrolling
    var arRndDuration: Int = 20000

    // the X offset when paused
    private var arXPaused = 0

    // whether it's being paused
    var isArPaused: Boolean = true
        private set

    /*
	 * constructor
	 */
    constructor(context: Context?) : super(context) {
        // customize the TextView
        setSingleLine()
        ellipsize = null
        visibility = INVISIBLE
    }

    /*
	 * constructor
	 */
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        // customize the TextView
        setSingleLine()
        ellipsize = null
        visibility = INVISIBLE
    }

    /*
	 * constructor
	 */
    constructor(
        context: Context?, attrs: AttributeSet?,
        defStyle: Int
    ) : super(context, attrs, defStyle) {
        // customize the TextView
        setSingleLine()
        ellipsize = null
        visibility = INVISIBLE
    }

    /**
     * begin to scroll the text from the original position
     */
    fun startScroll_Arabic() {
        // begin from the very left side
        arXPaused = width
        // assume it's paused
        isArPaused = true
        resumeScroll_Arabic()
    }

    /**
     * resume the scroll from the pausing point
     */
    @SuppressLint("UseValueOf")
    fun resumeScroll_Arabic() {
        if (!isArPaused) return

        // Do not know why it would not scroll sometimes
        // if setHorizontallyScrolling is called in constructor.
        setHorizontallyScrolling(true)

        // use LinearInterpolator for steady scrolling
        arSlr = Scroller(this.context, LinearInterpolator())
        setScroller(arSlr)

        val arScrollingLen = calculateScrollingLen()

        // int distance = scrollingLen - (getWidth()+ mXPaused);
        // int duration = (new Double(mRndDuration * distance * 1.00000/
        // scrollingLen)).intValue();
        val arDistance = (arScrollingLen + max(width.toDouble(), 500.0)).toInt() // -
        // (getWidth()+
        // arXPaused);
        val arDuration = (arRndDuration * arDistance * 1.00000
                / arDistance).toInt()
        Log.d("nirmal", "duration = $arDuration")
        visibility = VISIBLE
        arSlr!!.startScroll(arScrollingLen, 0, (arDistance * -1), 0, arDuration)
        isArPaused = false
    }

    /**
     * calculate the scrolling length of the text in pixel
     *
     * @return the scrolling length in pixels
     */
    private fun calculateScrollingLen(): Int {
        val tp = paint
        var rect: Rect? = Rect()
        val strTxt = text.toString()
        tp.getTextBounds(strTxt, 0, strTxt.length, rect)
        val scrollingLen = rect!!.width()
        rect = null
        return scrollingLen
    }

    /**
     * pause scrolling the text
     */
    fun pauseScroll() {
        if (null == arSlr) return

        if (isArPaused) return

        isArPaused = true

        // abortAnimation sets the current X to be the final X,
        // and sets isFinished to be true
        // so current position shall be saved
        arXPaused = arSlr!!.currX
        arSlr!!.abortAnimation()
    }

    /*
	 * override the computeScroll to restart scrolling when finished so as that
	 * the text is scrolled forever
	 */ override fun computeScroll() {
        super.computeScroll()

        if (null == arSlr) return

        if (arSlr!!.isFinished && (!isArPaused)) {
            this.startScroll_Arabic()
        }
    }
}
