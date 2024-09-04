package com.orbits.queuingsystem;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class LicenseDataHolder {/*
	private String mStrLicenseKey;
	private Calendar mCalStartDate;
	private Calendar mCalLastRunDate;
	private boolean mContainsError;
	private String mStrMsg;
	private String mErrorMsg;
	private String mReferenceNo;
	private String mResponseCode;

	public LicenseDataHolder() {
		this.mStrLicenseKey = "";
		this.mCalStartDate = new GregorianCalendar();
		this.mCalLastRunDate = new GregorianCalendar();
		this.mContainsError = true;
		this.mStrMsg = "";
		this.mErrorMsg = "";
		this.mReferenceNo = "";
		this.mResponseCode="";
	}

	public String getResponseCode() {
		return mResponseCode;
	}

	public void setResponseCode(String responseCode) {
		this.mResponseCode = responseCode;
	}

	public String getReferenceNo() {
		return mReferenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.mReferenceNo = referenceNo;
	}

	public String getErrorMsg() {
		return mErrorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.mErrorMsg = errorMsg;
	}

	public String getMessage() {
		return mStrMsg;
	}

	public void setMessage(String strMsg) {
		this.mStrMsg = strMsg;
	}

	public boolean ContainsError() {
		return mContainsError;
	}

	public void setContainsError(boolean containsError) {
		this.mContainsError = containsError;
	}

	public LicenseDataHolder(String mStrLicenseKey, Calendar mCalInstallDate) {
		this.mStrLicenseKey = mStrLicenseKey;
		this.mCalStartDate = (Calendar) mCalInstallDate.clone();
	}

	public String getLicenseKey() {
		return mStrLicenseKey;
	}

	public void setLicenseKey(String strLicenseKey) {
		this.mStrLicenseKey = strLicenseKey;
	}

	public Calendar getStartDate() {
		return (Calendar) mCalStartDate.clone();
	}

	public void setStartDate(Calendar startDate) {
		this.mCalStartDate = (Calendar) startDate.clone();
	}

	public Calendar getLastRunDate() {
		return (Calendar) mCalLastRunDate.clone();
	}

	public void setLastRunDate(Calendar lastRunDate) {
		this.mCalLastRunDate = (Calendar) lastRunDate.clone();
	}*/
	

	private String mStrLicenseKey;
	private Calendar mCalStartDate;
	private Calendar mCalLastRunDate;
	private Calendar mCalEndDate;
	private boolean mContainsError;
	private String mStrMsg;
	private String mErrorMsg;
	private String mReferenceNo;
	private String mResponseCode;
	private String mStrTrialDays;
	
	public LicenseDataHolder() {
		this.mStrLicenseKey = "";
		this.mCalStartDate = new GregorianCalendar();
		this.mCalLastRunDate = new GregorianCalendar();
		this.mStrTrialDays= "";
		this.mCalEndDate = new GregorianCalendar();
		this.mContainsError = true;
		this.mStrMsg = "";
		this.mErrorMsg = "";
		this.mReferenceNo = "";
		this.mResponseCode = "";
	}

	public String getResponseCode() {
		return mResponseCode;
	}

	public void setResponseCode(String responseCode) {
		this.mResponseCode = responseCode;
	}

	public String getReferenceNo() {
		return mReferenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.mReferenceNo = referenceNo;
	}

	public String getErrorMsg() {
		return mErrorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.mErrorMsg = errorMsg;
	}

	public String getMessage() {
		return mStrMsg;
	}

	public void setMessage(String strMsg) {
		this.mStrMsg = strMsg;
	}

	public boolean ContainsError() {
		return mContainsError;
	}

	public void setContainsError(boolean containsError) {
		this.mContainsError = containsError;
	}

	public LicenseDataHolder(String mStrLicenseKey, Calendar mCalInstallDate, String days) {
		this.mStrLicenseKey = mStrLicenseKey;
		this.mCalStartDate = (Calendar) mCalInstallDate.clone();
		this.mStrTrialDays= days;
	}
//sarvajeet 11-5
	public String getTrialDays() {
		return mStrTrialDays;
	}

	public void setTrialDays(String days) {
		this.mStrTrialDays = days;
	}
	//
	public String getLicenseKey() {
		return mStrLicenseKey;
	}

	public void setLicenseKey(String strLicenseKey) {
		this.mStrLicenseKey = strLicenseKey;
	}

	public Calendar getStartDate() {
		return (Calendar) mCalStartDate.clone();
	}

	public void setStartDate(Calendar startDate) {
		this.mCalStartDate = (Calendar) startDate.clone();
	}

	public Calendar getLastRunDate() {
		return (Calendar) mCalLastRunDate.clone();
	}

	public void setLastRunDate(Calendar lastRunDate) {
		this.mCalLastRunDate = (Calendar) lastRunDate.clone();
	}

	public Calendar getEndDate() {
		return (Calendar) mCalEndDate.clone();
	}

	public void setEndDate(Calendar calEndDate) {
		this.mCalEndDate = (Calendar) calEndDate.clone();
	}

}
