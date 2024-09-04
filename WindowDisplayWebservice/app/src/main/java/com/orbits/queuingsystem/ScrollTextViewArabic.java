package com.orbits.queuingsystem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;
import android.widget.TextView;

public class ScrollTextViewArabic extends TextView {

	// scrolling feature
	private Scroller arSlr;

	// milliseconds for a round of scrolling
	private int arRndDuration = 20000;

	// the X offset when paused
	private int arXPaused = 0;

	// whether it's being paused
	private boolean arPaused = true;

	/*
	 * constructor
	 */
	public ScrollTextViewArabic(Context context) {
		super(context);
		// customize the TextView
		setSingleLine();
		setEllipsize(null);
		setVisibility(INVISIBLE);
	}

	/*
	 * constructor
	 */
	public ScrollTextViewArabic(Context context, AttributeSet attrs) {
		super(context, attrs);
		// customize the TextView
		setSingleLine();
		setEllipsize(null);
		setVisibility(INVISIBLE);
	}

	/*
	 * constructor
	 */
	public ScrollTextViewArabic(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// customize the TextView
		setSingleLine();
		setEllipsize(null);
		setVisibility(INVISIBLE);
	}

	/**
	 * begin to scroll the text from the original position
	 */
	public void startScroll_Arabic() {
		// begin from the very left side
		arXPaused = getWidth();
		// assume it's paused
		arPaused = true;
		resumeScroll_Arabic();
	}

	/**
	 * resume the scroll from the pausing point
	 */
	@SuppressLint("UseValueOf")
	public void resumeScroll_Arabic() {
		if (!arPaused)
			return;

		// Do not know why it would not scroll sometimes
		// if setHorizontallyScrolling is called in constructor.
		setHorizontallyScrolling(true);

		// use LinearInterpolator for steady scrolling
		arSlr = new Scroller(this.getContext(), new LinearInterpolator());
		setScroller(arSlr);

		int arScrollingLen = calculateScrollingLen();
		// int distance = scrollingLen - (getWidth()+ mXPaused);
		// int duration = (new Double(mRndDuration * distance * 1.00000/
		// scrollingLen)).intValue();

		int arDistance = arScrollingLen + Math.max(getWidth(), 500); // -
																		// (getWidth()+
																		// arXPaused);
		int arDuration = (new Double(arRndDuration * arDistance * 1.00000
				/ arDistance)).intValue();
		Log.d("nirmal", "duration = "+arDuration);
		setVisibility(VISIBLE);
		arSlr.startScroll(arScrollingLen, 0, (arDistance * -1), 0, arDuration);
		arPaused = false;
	}

	/**
	 * calculate the scrolling length of the text in pixel
	 * 
	 * @return the scrolling length in pixels
	 */
	private int calculateScrollingLen() {
		TextPaint tp = getPaint();
		Rect rect = new Rect();
		String strTxt = getText().toString();
		tp.getTextBounds(strTxt, 0, strTxt.length(), rect);
		int scrollingLen = rect.width();
		rect = null;
		return scrollingLen;
	}

	/**
	 * pause scrolling the text
	 */
	public void pauseScroll() {
		if (null == arSlr)
			return;

		if (arPaused)
			return;

		arPaused = true;

		// abortAnimation sets the current X to be the final X,
		// and sets isFinished to be true
		// so current position shall be saved
		arXPaused = arSlr.getCurrX();
		arSlr.abortAnimation();
	}

	@Override
	/*
	 * override the computeScroll to restart scrolling when finished so as that
	 * the text is scrolled forever
	 */
	public void computeScroll() {
		super.computeScroll();

		if (null == arSlr)
			return;

		if (arSlr.isFinished() && (!arPaused)) {
			this.startScroll_Arabic();
		}
	}

	public int getArRndDuration() {
		return arRndDuration;
	}

	public void setArRndDuration(int duration) {
		this.arRndDuration = duration;
	}

	public boolean isArPaused() {
		return arPaused;
	}
}
