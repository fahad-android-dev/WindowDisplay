package com.orbits.queuingsystem;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

//import android.util.Log;

public class LicenseDialog extends DialogFragment {

    private static final String APP_ID = "queue_window_display";
    private static final String FULL_LIC = "2";
    private static final String TRIAL_LIC = "3";
    private OnLicenseDialogFinishListener mLicenseDialogListener;
    // private EditText mEtLicense;
    private Button mBtnSaveLicense, mBtnRequestFullLicense,
            mBtnRequestTrialLicense, mBtnExitLicense;// , mBtnClearLicense;
    //	static final String LOG_TAG = "qs";
    // private TextView mTvAndroidId, mTvMac;
    private EditText mEtCustCode, mEtInvoiceNo, mEtBranchCode, mEtStaffId,
            mEtPwd, mEtEmail, mEtPhone;
    private String strAndroidId = "";
    private String strMacAddress = "";
    private LicenseDataHolder mLicense;
    private TextView mTvInfo, mTvCustCodeLbl, mTvInvoiceNumberLbl,
            mTvBranchCodeLbl, mTvEmailLbl, mTvPhoneLbl;
    private String reqType = TRIAL_LIC;
    private View.OnClickListener mSaveBtnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            Context activity = getActivity();
            if (activity != null) {
                try {
                    mLicenseDialogListener = (OnLicenseDialogFinishListener) activity;
                    getDialog().dismiss();
                    // mLicenseDialogListener.onLicenseDialogFinished(mEtLicense
                    // .getText().toString());
//					Log.d(LOG_TAG, "license = " + mLicense.getLicenseKey());
                    mLicenseDialogListener.onLicenseDialogFinished(mLicense);
                } catch (Exception e) {
                    // TODO: handle exception
//					Log.d(LOG_TAG, "error while saving to database");
                }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
		/*View view = inflater.inflate(R.layout.dialog_license_entry, container,
				false);
		// mTvAndroidId = (TextView) view.findViewById(R.id.tv_android_id);
		// mTvMac = (TextView) view.findViewById(R.id.tv_mac);
		// mEtLicense = (EditText) view.findViewById(R.id.et_license_key);
		mTvCustCodeLbl = (TextView) view.findViewById(R.id.tv_cust_code);
		mTvCustCodeLbl.setText(Html
				.fromHtml("Customer Code <sup><small>*</small></sup>"));
		mTvInvoiceNumberLbl = (TextView) view.findViewById(R.id.tv_inv_num);
		mTvInvoiceNumberLbl.setText(Html
				.fromHtml("Invoice Number  <sup><small>*</small></sup>"));
		mTvBranchCodeLbl = (TextView) view.findViewById(R.id.tv_branch_code);
		mTvBranchCodeLbl.setText(Html
				.fromHtml("Branch Code  <sup><small>*</small></sup>"));
		mTvEmailLbl = (TextView) view.findViewById(R.id.tv_email);
		mTvEmailLbl
				.setText(Html.fromHtml("Email  <sup><small>#</small></sup>"));
		mTvPhoneLbl = (TextView) view.findViewById(R.id.tv_phone);
		mTvPhoneLbl.setText(Html
				.fromHtml("Contact No.  <sup><small>#</small></sup>"));
		mEtCustCode = (EditText) view.findViewById(R.id.et_cust_code);
		mEtInvoiceNo = (EditText) view.findViewById(R.id.et_inv_num);
		mEtBranchCode = (EditText) view.findViewById(R.id.et_branch_code);
		mEtEmail = (EditText) view.findViewById(R.id.et_email);
		mEtPhone = (EditText) view.findViewById(R.id.et_phone);
		mEtStaffId = (EditText) view.findViewById(R.id.et_staff_id);
		mEtPwd = (EditText) view.findViewById(R.id.et_staff_pwd);
		mTvInfo = (TextView) view.findViewById(R.id.tvInfo);
		mBtnSaveLicense = (Button) view.findViewById(R.id.btn_save_license);
		mBtnSaveLicense.setOnClickListener(mSaveBtnClickListener);
		mBtnRequestFullLicense = (Button) view
				.findViewById(R.id.btn_get_full_license);
		// mBtnRequestFullLicense.setTag("full");
		// mBtnRequestFullLicense.setTag("2");
		mBtnRequestFullLicense.setTag(FULL_LIC);
		// mBtnRequestFullLicense.setOnClickListener(mRequestLicenseBtnClickListener);
		mBtnRequestFullLicense
				.setOnClickListener(new RequestLicenseBtnClickListener());
		mBtnRequestTrialLicense = (Button) view
				.findViewById(R.id.btn_get_trial_license);
		// mBtnRequestTrialLicense.setTag("trial");
		// mBtnRequestTrialLicense.setTag("3");
		mBtnRequestTrialLicense.setTag(TRIAL_LIC);
		mBtnRequestTrialLicense
				.setOnClickListener(new RequestLicenseBtnClickListener());
		getDialog().setTitle("License");
		mBtnClearLicense = (Button) view.findViewById(R.id.btn_clear_license);
		mBtnClearLicense.setOnClickListener(new ClearLicenseBtnClickListener());
		// getDialog().setCancelable(false);

		// return super.onCreateView(inflater, container, savedInstanceState);
		return view;*/
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater
                .inflate(R.layout.dialog_license_entry, null, false);
        builder.setView(view);

        // builder.setCustomTitle(inflater.inflate(R.layout.license_dialog_title,
        // null));
        // mTvAndroidId = (TextView) view.findViewById(R.id.tv_android_id);
        // mTvMac = (TextView) view.findViewById(R.id.tv_mac);
        // mEtLicense = (EditText) view.findViewById(R.id.et_license_key);
        mTvCustCodeLbl = (TextView) view.findViewById(R.id.tv_cust_code);
        mTvCustCodeLbl.setText(Html
                .fromHtml("Customer Code <sup><small>*</small></sup>"));
        mTvInvoiceNumberLbl = (TextView) view.findViewById(R.id.tv_inv_num);
        mTvInvoiceNumberLbl.setText(Html
                .fromHtml("Invoice Number  <sup><small>*</small></sup>"));
        mTvBranchCodeLbl = (TextView) view.findViewById(R.id.tv_branch_code);
        mTvBranchCodeLbl.setText(Html
                .fromHtml("Branch Code  <sup><small>*</small></sup>"));
        mTvEmailLbl = (TextView) view.findViewById(R.id.tv_email);
        mTvEmailLbl
                .setText(Html.fromHtml("Email  <sup><small>#</small></sup>"));
        mTvPhoneLbl = (TextView) view.findViewById(R.id.tv_phone);
        mTvPhoneLbl.setText(Html
                .fromHtml("Contact No.  <sup><small>#</small></sup>"));
        mEtCustCode = (EditText) view.findViewById(R.id.et_cust_code);
        mEtInvoiceNo = (EditText) view.findViewById(R.id.et_inv_num);
        mEtBranchCode = (EditText) view.findViewById(R.id.et_branch_code);
        mEtEmail = (EditText) view.findViewById(R.id.et_email);
        mEtPhone = (EditText) view.findViewById(R.id.et_phone);
        mEtStaffId = (EditText) view.findViewById(R.id.et_staff_id);
        mEtPwd = (EditText) view.findViewById(R.id.et_staff_pwd);
        mTvInfo = (TextView) view.findViewById(R.id.tvInfo);
        mBtnSaveLicense = (Button) view.findViewById(R.id.btn_save_license);
        mBtnExitLicense = (Button) view.findViewById(R.id.btn_exit_license);
        mBtnRequestFullLicense = (Button) view
                .findViewById(R.id.btn_get_full_license);
        // mBtnRequestFullLicense.setTag("full");
        // mBtnRequestFullLicense.setTag("2");
        mBtnRequestFullLicense.setTag(FULL_LIC);
        // mBtnRequestFullLicense.setOnClickListener(mRequestLicenseBtnClickListener);

        mBtnRequestTrialLicense = (Button) view
                .findViewById(R.id.btn_get_trial_license);
        // mBtnRequestTrialLicense.setTag("trial");
        // mBtnRequestTrialLicense.setTag("3");
        mBtnRequestTrialLicense.setTag(TRIAL_LIC);
        // getDialog().setTitle("License");
        // getDialog().setTitle("License Authentication");
        // mBtnClearLicense = (Button)
        // view.findViewById(R.id.btn_clear_license);
        mBtnRequestFullLicense
                .setOnClickListener(new RequestLicenseBtnClickListener());
        mBtnRequestTrialLicense
                .setOnClickListener(new RequestLicenseBtnClickListener());
        mBtnSaveLicense.setOnClickListener(mSaveBtnClickListener);
        mBtnExitLicense.setOnClickListener(new
                ExitLicenseBtnClickListener());
        // mBtnClearLicense.setOnClickListener(new
        // ClearLicenseBtnClickListener());
        // return super.onCreateDialog(savedInstanceState);

        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //AlertDialog dialog = builder.create();
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        /*
         * String strDeviceId = Secure.getString(getActivity()
         * .getContentResolver(), Secure.ANDROID_ID);
         */

        mEtCustCode.setText(MainActivity.sharePrefSettings.getString(AppConstantsFlags.KEY_CC, ""));
        //Log.d("sk2",MainActivity.sharePrefSettings.getString(AppConstantsFlags.KEY_CC, "") );

        mEtInvoiceNo.setText(MainActivity.sharePrefSettings.getString(AppConstantsFlags.KEY_IN, ""));
        //Log.d("sk2",MainActivity.sharePrefSettings.getString(AppConstantsFlags.KEY_IN,"") );

        mEtBranchCode.setText(MainActivity.sharePrefSettings.getString(AppConstantsFlags.KEY_BC, ""));
        //Log.d("sk2",MainActivity.sharePrefSettings.getString(AppConstantsFlags.KEY_BC,"") );

        mEtEmail.setText(MainActivity.sharePrefSettings.getString(AppConstantsFlags.KEY_EMAIL, ""));
        //Log.d("sk2",MainActivity.sharePrefSettings.getString(AppConstantsFlags.KEY_EMAIL, "") );

        mEtPhone.setText(MainActivity.sharePrefSettings.getString(AppConstantsFlags.KEY_CONTACT_NO, ""));
        //Log.d("sk2",MainActivity.sharePrefSettings.getString(AppConstantsFlags.KEY_CONTACT_NO, ""));

        mEtStaffId.setText(MainActivity.sharePrefSettings.getString(AppConstantsFlags.KEY_STAFF_ID, ""));
        //Log.d("sk2",MainActivity.sharePrefSettings.getString(AppConstantsFlags.KEY_STAFF_ID, ""));

        mEtPwd.setText(MainActivity.sharePrefSettings.getString(AppConstantsFlags.KEY_PASSWORD_STAFF, ""));
        //Log.d("sk2",MainActivity.sharePrefSettings.getString(AppConstantsFlags.KEY_PASSWORD_STAFF, ""));

        mLicense = new LicenseDataHolder();
        strAndroidId = Secure.getString(getActivity().getContentResolver(),
                Secure.ANDROID_ID);
        // mTvAndroidId.setText(strAndroidId);
        strMacAddress = getMACAddress();
        //Toast.makeText(getActivity(), ":::" + strMacAddress,
        //     Toast.LENGTH_SHORT).show();
        if (strMacAddress == null) {
            // mTvMac.setText(strMacAddress);
            Toast.makeText(getActivity(), "Ensure that wifi is switched on.",
                    Toast.LENGTH_SHORT).show();
        }

    }

    private String getMACAddress() {

		/*WifiManager wifiManager = (WifiManager) getActivity().getSystemService(
				Context.WIFI_SERVICE);
		WifiInfo wInfo = wifiManager.getConnectionInfo();
		String macAddress = wInfo.getMacAddress();
		return macAddress;*/
        try {
            WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext()
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo wInfo = wifiManager.getConnectionInfo();
            String macAddress = wInfo.getMacAddress();
            return macAddress;
        } catch (Exception e) {
            // TODO: handle exception
            return "";
        }
    }

    private boolean isEmailValid(String strEmail) {
        String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        if (strEmail != null) {
            return strEmail.matches(EMAIL_REGEX);
        } else {
            return false;
        }
    }

	/*private class ClearLicenseBtnClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			new DataLayer(getActivity()).clearSettings();
			// getActivity().finish();
		}
	}*/

    interface OnLicenseDialogFinishListener {
        void onLicenseDialogFinished(LicenseDataHolder license);
    }

    private class ExitLicenseBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            getDialog().dismiss();
            getActivity().finishAffinity();
            System.exit(0);
        }
    }

    private class RequestLicenseBtnClickListener implements
            View.OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            if (!mEtInvoiceNo.getText().toString().equals("") || !mEtBranchCode.getText().toString().equals("") ||
                    !mEtCustCode.getText().toString().equals("") || !mEtEmail.getText().toString().equals("")
                    || !mEtPhone.getText().toString().equals("") || !mEtStaffId.getText().toString().equals("")
                    && !mEtPwd.getText().toString().equals("")) {

                Editor ed = MainActivity.sharePrefSettings.edit();
                ed.putString(AppConstantsFlags.KEY_CC, mEtCustCode.getText().toString());
                //Log.d("sk2",mEtCustCode.getText().toString());

                ed.putString(AppConstantsFlags.KEY_IN, mEtInvoiceNo.getText().toString());
                //Log.d("sk2",mEtInvoiceNo.getText().toString());

                ed.putString(AppConstantsFlags.KEY_BC, mEtBranchCode.getText().toString());
                //Log.d("sk2",mEtBranchCode.getText().toString());

                ed.putString(AppConstantsFlags.KEY_EMAIL, mEtEmail.getText().toString());
                //Log.d("sk2",mEtEmail.getText().toString());

                ed.putString(AppConstantsFlags.KEY_CONTACT_NO, mEtPhone.getText().toString());
                //Log.d("sk2",mEtPhone.getText().toString());

                ed.putString(AppConstantsFlags.KEY_STAFF_ID, mEtStaffId.getText().toString());
                //Log.d("sk2",mEtPhone.getText().toString());

                ed.putString(AppConstantsFlags.KEY_PASSWORD_STAFF, mEtPwd.getText().toString());
                //Log.d("sk2",mEtPhone.getText().toString());

                ed.commit();
            }
            reqType = v.getTag().toString();
            //String strUrl = "http://192.168.1.112:82" + "/" + "License.aspx";
            String strUrl = "http://jeddah.from-ak.com:86" + "/"
                    + "License.aspx";
            if (v.getTag().toString().equals(FULL_LIC)
                    && (mEtInvoiceNo.getText().toString().equals("")
                    || mEtBranchCode.getText().toString().equals("") || mEtCustCode
                    .getText().toString().equals(""))) {
                mTvInfo.setTextColor(Color.RED);
                mTvInfo.setText("Please provide Invoice-No, Branch-code and Customer-code.");
                return;
            }
            if (!mEtStaffId.getText().toString().equals("")
                    && mEtPwd.getText().toString().equals("")) {
                mTvInfo.setTextColor(Color.RED);
                mTvInfo.setText("Password field cannot be left empty.");
                return;
            }
            if (v.getTag().toString().equals(TRIAL_LIC)
                    && mEtEmail.getText().toString().equals("")
                    && mEtPhone.getText().toString().equals("")) {
                mTvInfo.setTextColor(Color.RED);
                mTvInfo.setText("Email-address or phone-number is required for trial license.");
                return;
            } else if (v.getTag().toString().equals(TRIAL_LIC)
                    && !mEtEmail.getText().toString().equals("")
                    && !isEmailValid(mEtEmail.getText().toString())) {
                mTvInfo.setTextColor(Color.RED);
                mTvInfo.setText("Invalid email-address.");
                return;
            }
            new AsyncTask_RequestLicense().executeOnExecutor(
                    AsyncTask.THREAD_POOL_EXECUTOR, strUrl, "android_id",
                    strAndroidId, "mac", strMacAddress, "app_id", APP_ID,
                    "cust_code", mEtCustCode.getText().toString(),
                    "invoice_no", mEtInvoiceNo.getText().toString(),
                    "branch_code", mEtBranchCode.getText().toString(),
                    "staff_id", mEtStaffId.getText().toString(), "staff_pwd",
                    mEtPwd.getText().toString(), "request_type", v.getTag()
                            .toString(), "email",
                    mEtEmail.getText().toString(), "phone", mEtPhone.getText()
                            .toString());


        }
    }

	/*private boolean isPhoneValid(String strPhone) {
		return true;
	}*/

    /*
     * private View.OnClickListener mRequestLicenseBtnClickListener = new
     * View.OnClickListener() {
     *
     * @Override public void onClick(View v) {
     *
     * nirmal 23 jan 2015
     *
     * String strUrl = "http://" + "192.168.1.114" + "/" + "keygentest" + "/" +
     * "License.aspx" + "?android_id=" + strAndroidId + "&mac=" + strMacAddress;
     *
     * // nirmal 19 feb 2015 // String strUrl = "http://192.168.1.114" + "/" +
     * "keygentest" + "/" // + "License.aspx"; String strUrl =
     * "http://192.168.1.112:82" + "/" + "License.aspx"; new
     * AsyncTask_RequestLicense().execute(strUrl, "android_id", strAndroidId,
     * "mac", strMacAddress, "app_id", APP_ID, "invoice_no",
     * mEtInvoiceNo.getText().toString(), "branch_code",
     * mEtBranchCode.getText().toString(), "staff_id",
     * mEtStaffId.getText().toString(), "staff_pwd",
     * mEtPwd.getText().toString()); } };
     */

    private class AsyncTask_RequestLicense extends
            AsyncTask<String, String, LicenseDataHolder> {

        // final String strServerUrl = "";
        private static final String RECEIVED = "8";
        private static final String PENDING = "9";
        private static final String REJECTED = "10";
        private static final String APPROVED = "11";
        private static final String INVALID_REQUEST = "12";
        private static final String TRIAL_EXPIRED = "13";
        private static final String REJECTED_AMT_PAID = "14";

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(), "",
                    "sending...", true, false);
//					Log.i(LOG_TAG, "Pre executed: " + System.currentTimeMillis());
        }

        @Override
        protected LicenseDataHolder doInBackground(String... params) {
//					Log.i(LOG_TAG, "Do in background: " + System.currentTimeMillis());
            /*
             * nirmal 23 jan 2015
             *
             * URL url; InputStream inStream = null;
             */
            // BufferedReader bReader = null;
            String strLicense = "";
            //Sarvajeet 11-5
            String TrialDays = "";
            // String strMsg = "";
            LicenseDataHolder license = new LicenseDataHolder();
            // Log.d("qs", "url ="+params[0]);
            HttpClient httpClient;
            HttpPost httpPost;

            try {
                httpClient = new DefaultHttpClient();
                httpPost = new HttpPost(params[0]);
                List<NameValuePair> dataList = new ArrayList<NameValuePair>();
                for (int i = 1; i < params.length; i++) {
                    dataList.add(new BasicNameValuePair(params[i], params[++i]));
                }
                httpPost.setEntity(new UrlEncodedFormEntity(dataList, "UTF-8"));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpResponseEntity = httpResponse.getEntity();
                String str = EntityUtils.toString(httpResponseEntity, "UTF-8");
//						Log.d(LOG_TAG, "json received as " + str);
                // JSONObject json = new JSONObject(EntityUtils.toString(
                // httpResponseEntity, "UTF-8"));
                JSONObject json = new JSONObject(str);
//						Log.d(LOG_TAG, "json received as " + json.toString());

                //Approved- sarvajeet 11-5
                String strResponseCode = "";
                strResponseCode = json.getString("response");
                license.setResponseCode(strResponseCode);
                if (!strResponseCode.equals("")) {
                    if (strResponseCode.equalsIgnoreCase(APPROVED)) {
                        strLicense = json.getString("license");
                        //Sarvajeet 11-5
                        TrialDays = json.getString("TrialDays");
                        license.setTrialDays(TrialDays);
                        DateFormat dateFormat = new SimpleDateFormat(
                                "dd-MM-yyyy", Locale.US);
                        Date date = new Date();
                        date = dateFormat.parse(json.getString("startDate"));
                        Calendar cal = new GregorianCalendar();
                        cal.setTime(date);
                        license.setStartDate(cal);
                    }
                }

                // Log.d("qs",
                // "date from server : "+json.getString("startDate"));

                /*
                 * Log.d("qs", "saved date : "+cal.get(Calendar.DAY_OF_MONTH) +
                 * "-" + String.format(Locale.US, "%02d", 1 +
                 * cal.get(Calendar.MONTH)) + "-" + cal.get(Calendar.YEAR));
                 */
                // Log.d("qs",
                // "received start date from server : " + date.toString());
                license.setReferenceNo(json.getString("refNo"));
                license.setMessage(json.getString("msg"));
                license.setContainsError(false);
                license.setErrorMsg("");
            } catch (IllegalArgumentException e) {
                // strLicense = "Invalid URL";
                strLicense = "";
                TrialDays = "";
                license.setContainsError(true);
                license.setErrorMsg("Server not found");
                // strMsg = "Server not found";
            } catch (ArrayIndexOutOfBoundsException e) {
                // strLicense = "Insufficient parameters";
                strLicense = "";
                TrialDays = "";
                license.setContainsError(true);
                license.setErrorMsg("Insufficient data entered");
                // strMsg = "Insufficient data entered";
            } catch (UnsupportedEncodingException e) {
                // strLicense = "Unsupported encoding";
                strLicense = "";
                TrialDays = "";
                license.setContainsError(true);
                license.setErrorMsg("Internal encoding error");
                // strMsg = "Internal encoding error";
            } catch (ClientProtocolException e) {
                // strLicense = "Client protocol exception";
                strLicense = "";
                TrialDays = "";
                license.setContainsError(true);
                license.setErrorMsg("Connection error");
                // strMsg = "Connection error";
            } catch (IOException e) {
                // strLicense = "IO error";
                strLicense = "";
                TrialDays = "";
                license.setContainsError(true);
                license.setErrorMsg("Connection broken");
//						Log.d(LOG_TAG, "IO exception " + e.getMessage());
                // strMsg = "Connection broken";
            } catch (org.apache.http.ParseException e) {
                // strLicense = "Error in parsing response";
                strLicense = "";
                TrialDays = "";
                license.setContainsError(true);
                license.setErrorMsg("Corrupt data received");
                // strMsg = "Corrupt data received";
            } catch (java.text.ParseException e) {
                // strLicense = "Error in parsing response";
                strLicense = "";
                TrialDays = "";
                license.setContainsError(true);
                license.setErrorMsg("Corrupt date received");
                // strMsg = "Corrupt data received";
            } catch (JSONException e) {
                // strLicense = "Error in parsing json";
                strLicense = "";
                TrialDays = "";
                license.setContainsError(true);
                license.setErrorMsg("Improper data received");
                // strMsg = "Improper data received";
            } catch (NullPointerException e) {
                // strLicense = "Unknown error";
                strLicense = "";
                TrialDays = "";
                license.setContainsError(true);
                license.setErrorMsg("Internal error - nullpointer exception");
                // strMsg = "Some error occurred";
            } catch (IllegalStateException e) {
                // strLicense = "Unknown error";
                strLicense = "";
                TrialDays = "";
                license.setContainsError(true);
                license.setErrorMsg("Internal error - data already consumed");
                // strMsg = "Some error occurred";
            } catch (Exception e) {
                // strLicense = "Unknown error";
                strLicense = "";
                TrialDays = "";
                license.setContainsError(true);
                license.setErrorMsg("Some error occurred");
//						Log.d(LOG_TAG, "unknown exception : " + e.getClass());
                // strMsg = "Some error occurred";
            }
            license.setLicenseKey(strLicense);
            // license.setMessage(strMsg);
//					Log.i(LOG_TAG, "Done in background: " + System.currentTimeMillis());
            return license;
        }

        @Override
        protected void onPostExecute(LicenseDataHolder license) {
            // TODO Auto-generated method stub
//					Log.i(LOG_TAG, "Post execute: " + System.currentTimeMillis());
            super.onPostExecute(license);
//					Log.i(LOG_TAG,
//							"Post super-on-post-execute: " + System.currentTimeMillis());

            progressDialog.dismiss();
            if (!license.ContainsError()) {
                mLicense = license;
                String strMsg = "";
                if (!license.getResponseCode().equals("")) {
                    if (license.getResponseCode().equalsIgnoreCase(APPROVED)) {
                        // strMsg = "Request approved, license received";
                        // strMsg = license.getMessage();
                        mTvInfo.setTextColor(Color.rgb(15, 159, 48));
                        strMsg = "Your request has been approved, press Save to continue.";
                    } else if (license.getResponseCode().equalsIgnoreCase(
                            PENDING)) {
                        // strMsg = "Request is pending";
                        // strMsg = license.getMessage();
                        mTvInfo.setTextColor(Color.rgb(15, 159, 48));
                        strMsg = "Your request is pending approval.";
                    } else if (license.getResponseCode().equalsIgnoreCase(
                            REJECTED)) {
                        mTvInfo.setTextColor(Color.RED);
                        strMsg = "Request rejected, please contact company salesperson and quote the reference number: "
                                + license.getReferenceNo()
                                + "."
                                + (license.getMessage().equals("") ? ""
                                : "\n Reason: " + license.getMessage());
                    } else if (license.getResponseCode().equalsIgnoreCase(
                            REJECTED_AMT_PAID)) {
                        // strMsg = "Request received";
                        // strMsg = license.getMessage();
                        mTvInfo.setTextColor(Color.RED);
                        strMsg = "Your request is rejected because your invoice is not fully paid. Please go for trial version.";
                    } else if (license.getResponseCode().equalsIgnoreCase(
                            RECEIVED)) {
                        // save Preferences of CustomerCode, InvNo, BranchCode
                        Editor ed = MainActivity.sharePrefSettings.edit();
                        if (reqType.equals(TRIAL_LIC)) {
                            ed.putString(AppConstantsFlags.KEY_EMAIL, mEtEmail.getText().toString());
                            //Log.d("sk2",mEtEmail.getText().toString());

                            ed.putString(AppConstantsFlags.KEY_CONTACT_NO, mEtPhone.getText().toString());
                            //Log.d("sk2",mEtPhone.getText().toString());

                            ed.commit();
                        } else {
                            ed.putString(AppConstantsFlags.KEY_CC, mEtCustCode.getText().toString());
                            //Log.d("sk2",mEtCustCode.getText().toString());

                            ed.putString(AppConstantsFlags.KEY_IN, mEtInvoiceNo.getText().toString());
                            //Log.d("sk2",mEtInvoiceNo.getText().toString());

                            ed.putString(AppConstantsFlags.KEY_BC, mEtBranchCode.getText().toString());
                            //Log.d("sk2",mEtBranchCode.getText().toString());

                            ed.putString(AppConstantsFlags.KEY_EMAIL, mEtEmail.getText().toString());
                            //Log.d("sk2",mEtEmail.getText().toString());

                            ed.putString(AppConstantsFlags.KEY_CONTACT_NO, mEtPhone.getText().toString());
                            //Log.d("sk2",mEtPhone.getText().toString());

                            ed.commit();
                        }
                        // strMsg = "Request received";
                        // strMsg = license.getMessage();
                        mTvInfo.setTextColor(Color.rgb(15, 159, 48));
                        strMsg = "Request sent successfully.";
                    } else if (license.getResponseCode().equalsIgnoreCase(
                            INVALID_REQUEST)) {
                        // strMsg = "Request received";
                        // strMsg = license.getMessage();
                        mTvInfo.setTextColor(Color.RED);
                        strMsg = "Request failed, please re-check the data you have provided.";
                    } else if (license.getResponseCode().equalsIgnoreCase(
                            TRIAL_EXPIRED)) {
                        // strMsg = license.getMessage();
                        mTvInfo.setTextColor(Color.RED);
                        strMsg = "Your trial-license has expired, you may contact the company salesperson and quote the reference number: "
                                + license.getReferenceNo();
                    }
                }
                // mTvInfo.setText(strMsg + "\n" + license.getMessage());
                mTvInfo.setText(strMsg);
            } else {
                mTvInfo.setTextColor(Color.RED);
                mTvInfo.setText(license.getErrorMsg());
            }
            // mTvInfo.setText("response received from server");
//					Log.i(LOG_TAG, "Post executed: " + System.currentTimeMillis());
        }

    }

}
