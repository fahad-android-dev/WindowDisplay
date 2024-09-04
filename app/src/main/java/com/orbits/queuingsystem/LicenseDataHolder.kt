package com.orbits.queuingsystem

import java.util.Calendar
import java.util.GregorianCalendar

class LicenseDataHolder {
    //
    /*
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
    var licenseKey: String
    private var mCalStartDate: Calendar
    private var mCalLastRunDate: Calendar? = null
    private var mCalEndDate: Calendar? = null
    private var mContainsError = false
    var message: String? = null
    var errorMsg: String? = null
    var referenceNo: String? = null
    var responseCode: String? = null

    //sarvajeet 11-5
    var trialDays: String

    constructor() {
        this.licenseKey = ""
        this.mCalStartDate = GregorianCalendar()
        this.mCalLastRunDate = GregorianCalendar()
        this.trialDays = ""
        this.mCalEndDate = GregorianCalendar()
        this.mContainsError = true
        this.message = ""
        this.errorMsg = ""
        this.referenceNo = ""
        this.responseCode = ""
    }

    fun ContainsError(): Boolean {
        return mContainsError
    }

    fun setContainsError(containsError: Boolean) {
        this.mContainsError = containsError
    }

    constructor(mStrLicenseKey: String, mCalInstallDate: Calendar, days: String) {
        this.licenseKey = mStrLicenseKey
        this.mCalStartDate = mCalInstallDate.clone() as Calendar
        this.trialDays = days
    }

    var startDate: Calendar
        get() = mCalStartDate.clone() as Calendar
        set(startDate) {
            this.mCalStartDate = startDate.clone() as Calendar
        }

    var lastRunDate: Calendar
        get() = mCalLastRunDate!!.clone() as Calendar
        set(lastRunDate) {
            this.mCalLastRunDate = lastRunDate.clone() as Calendar
        }

    var endDate: Calendar
        get() = mCalEndDate!!.clone() as Calendar
        set(calEndDate) {
            this.mCalEndDate = calEndDate.clone() as Calendar
        }
}
