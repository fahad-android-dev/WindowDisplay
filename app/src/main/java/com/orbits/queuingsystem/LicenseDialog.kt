package com.orbits.queuingsystem

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.wifi.WifiManager
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings.Secure
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import org.apache.http.NameValuePair
import org.apache.http.ParseException
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.HttpClient
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale

//import android.util.Log;
class LicenseDialog : DialogFragment() {
    private var mLicenseDialogListener: OnLicenseDialogFinishListener? = null

    // private EditText mEtLicense;
    private var mBtnSaveLicense: Button? = null
    private var mBtnRequestFullLicense: Button? = null
    private var mBtnRequestTrialLicense: Button? = null
    private var mBtnExitLicense: Button? = null // , mBtnClearLicense;

    //	static final String LOG_TAG = "qs";
    // private TextView mTvAndroidId, mTvMac;
    private var mEtCustCode: EditText? = null
    private var mEtInvoiceNo: EditText? = null
    private var mEtBranchCode: EditText? = null
    private var mEtStaffId: EditText? = null
    private var mEtPwd: EditText? = null
    private var mEtEmail: EditText? = null
    private var mEtPhone: EditText? = null
    private var strAndroidId = ""
    private var strMacAddress: String? = ""
    private var mLicense: LicenseDataHolder? = null
    private var mTvInfo: TextView? = null
    private var mTvCustCodeLbl: TextView? = null
    private var mTvInvoiceNumberLbl: TextView? = null
    private var mTvBranchCodeLbl: TextView? = null
    private var mTvEmailLbl: TextView? = null
    private var mTvPhoneLbl: TextView? = null
    private var reqType = TRIAL_LIC
    private val mSaveBtnClickListener = View.OnClickListener {
        // TODO Auto-generated method stub
        val activity: Context? = activity
        if (activity != null) {
            try {
                mLicenseDialogListener = activity as OnLicenseDialogFinishListener?
                dialog.dismiss()
                // mLicenseDialogListener.onLicenseDialogFinished(mEtLicense
                // .getText().toString());
//					Log.d(LOG_TAG, "license = " + mLicense.getLicenseKey());
                mLicenseDialogListener!!.onLicenseDialogFinished(mLicense)
            } catch (e: Exception) {
                // TODO: handle exception
//					Log.d(LOG_TAG, "error while saving to database");
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // TODO Auto-generated method stub
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val view = inflater
            .inflate(R.layout.dialog_license_entry, null, false)
        builder.setView(view)

        // builder.setCustomTitle(inflater.inflate(R.layout.license_dialog_title,
        // null));
        // mTvAndroidId = (TextView) view.findViewById(R.id.tv_android_id);
        // mTvMac = (TextView) view.findViewById(R.id.tv_mac);
        // mEtLicense = (EditText) view.findViewById(R.id.et_license_key);
        mTvCustCodeLbl = view.findViewById<View>(R.id.tv_cust_code) as TextView
        mTvCustCodeLbl!!.text = Html
            .fromHtml("Customer Code <sup><small>*</small></sup>")
        mTvInvoiceNumberLbl = view.findViewById<View>(R.id.tv_inv_num) as TextView
        mTvInvoiceNumberLbl!!.text = Html
            .fromHtml("Invoice Number  <sup><small>*</small></sup>")
        mTvBranchCodeLbl = view.findViewById<View>(R.id.tv_branch_code) as TextView
        mTvBranchCodeLbl!!.text = Html
            .fromHtml("Branch Code  <sup><small>*</small></sup>")
        mTvEmailLbl = view.findViewById<View>(R.id.tv_email) as TextView
        mTvEmailLbl?.text = Html.fromHtml("Email  <sup><small>#</small></sup>")
        mTvPhoneLbl = view.findViewById<View>(R.id.tv_phone) as TextView
        mTvPhoneLbl!!.text = Html
            .fromHtml("Contact No.  <sup><small>#</small></sup>")
        mEtCustCode = view.findViewById<View>(R.id.et_cust_code) as EditText
        mEtInvoiceNo = view.findViewById<View>(R.id.et_inv_num) as EditText
        mEtBranchCode = view.findViewById<View>(R.id.et_branch_code) as EditText
        mEtEmail = view.findViewById<View>(R.id.et_email) as EditText
        mEtPhone = view.findViewById<View>(R.id.et_phone) as EditText
        mEtStaffId = view.findViewById<View>(R.id.et_staff_id) as EditText
        mEtPwd = view.findViewById<View>(R.id.et_staff_pwd) as EditText
        mTvInfo = view.findViewById<View>(R.id.tvInfo) as TextView
        mBtnSaveLicense = view.findViewById<View>(R.id.btn_save_license) as Button
        mBtnExitLicense = view.findViewById<View>(R.id.btn_exit_license) as Button
        mBtnRequestFullLicense = view
            .findViewById<View>(R.id.btn_get_full_license) as Button
        // mBtnRequestFullLicense.setTag("full");
        // mBtnRequestFullLicense.setTag("2");
        mBtnRequestFullLicense!!.tag = FULL_LIC

        // mBtnRequestFullLicense.setOnClickListener(mRequestLicenseBtnClickListener);
        mBtnRequestTrialLicense = view
            .findViewById<View>(R.id.btn_get_trial_license) as Button
        // mBtnRequestTrialLicense.setTag("trial");
        // mBtnRequestTrialLicense.setTag("3");
        mBtnRequestTrialLicense!!.tag = TRIAL_LIC
        // getDialog().setTitle("License");
        // getDialog().setTitle("License Authentication");
        // mBtnClearLicense = (Button)
        // view.findViewById(R.id.btn_clear_license);
        mBtnRequestFullLicense?.setOnClickListener(RequestLicenseBtnClickListener())
        mBtnRequestTrialLicense?.setOnClickListener(RequestLicenseBtnClickListener())
        mBtnSaveLicense!!.setOnClickListener(mSaveBtnClickListener)
        mBtnExitLicense!!.setOnClickListener(ExitLicenseBtnClickListener())

        // mBtnClearLicense.setOnClickListener(new
        // ClearLicenseBtnClickListener());
        // return super.onCreateDialog(savedInstanceState);
        val dialog: Dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
        //AlertDialog dialog = builder.create();
        return dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        // TODO Auto-generated method stub
        super.onStart()

        /*
         * String strDeviceId = Secure.getString(getActivity()
         * .getContentResolver(), Secure.ANDROID_ID);
         */
        mEtCustCode!!.setText(
            MainActivity.Companion.sharePrefSettings!!.getString(
                AppConstantsFlags.KEY_CC,
                ""
            )
        )

        //Log.d("sk2",MainActivity.sharePrefSettings.getString(AppConstantsFlags.KEY_CC, "") );
        mEtInvoiceNo!!.setText(
            MainActivity.Companion.sharePrefSettings!!.getString(
                AppConstantsFlags.KEY_IN,
                ""
            )
        )

        //Log.d("sk2",MainActivity.sharePrefSettings.getString(AppConstantsFlags.KEY_IN,"") );
        mEtBranchCode!!.setText(
            MainActivity.Companion.sharePrefSettings!!.getString(
                AppConstantsFlags.KEY_BC,
                ""
            )
        )

        //Log.d("sk2",MainActivity.sharePrefSettings.getString(AppConstantsFlags.KEY_BC,"") );
        mEtEmail!!.setText(
            MainActivity.Companion.sharePrefSettings!!.getString(
                AppConstantsFlags.KEY_EMAIL,
                ""
            )
        )

        //Log.d("sk2",MainActivity.sharePrefSettings.getString(AppConstantsFlags.KEY_EMAIL, "") );
        mEtPhone!!.setText(
            MainActivity.Companion.sharePrefSettings!!.getString(
                AppConstantsFlags.KEY_CONTACT_NO,
                ""
            )
        )

        //Log.d("sk2",MainActivity.sharePrefSettings.getString(AppConstantsFlags.KEY_CONTACT_NO, ""));
        mEtStaffId!!.setText(
            MainActivity.Companion.sharePrefSettings!!.getString(
                AppConstantsFlags.KEY_STAFF_ID,
                ""
            )
        )

        //Log.d("sk2",MainActivity.sharePrefSettings.getString(AppConstantsFlags.KEY_STAFF_ID, ""));
        mEtPwd!!.setText(
            MainActivity.Companion.sharePrefSettings!!.getString(
                AppConstantsFlags.KEY_PASSWORD_STAFF,
                ""
            )
        )

        //Log.d("sk2",MainActivity.sharePrefSettings.getString(AppConstantsFlags.KEY_PASSWORD_STAFF, ""));
        mLicense = LicenseDataHolder()
        strAndroidId = Secure.getString(
            activity.contentResolver,
            Secure.ANDROID_ID
        )
        // mTvAndroidId.setText(strAndroidId);
        strMacAddress = mACAddress
        //Toast.makeText(getActivity(), ":::" + strMacAddress,
        //     Toast.LENGTH_SHORT).show();
        if (strMacAddress == null) {
            // mTvMac.setText(strMacAddress);
            Toast.makeText(
                activity, "Ensure that wifi is switched on.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private val mACAddress: String
        get() {
            /*WifiManager wifiManager = (WifiManager) getActivity().getSystemService(
				Context.WIFI_SERVICE);
		WifiInfo wInfo = wifiManager.getConnectionInfo();
		String macAddress = wInfo.getMacAddress();
		return macAddress;*/

            try {
                val wifiManager = activity.applicationContext
                    .getSystemService(Context.WIFI_SERVICE) as WifiManager
                val wInfo = wifiManager.connectionInfo
                val macAddress = wInfo.macAddress
                return macAddress
            } catch (e: Exception) {
                // TODO: handle exception
                return ""
            }
        }

    private fun isEmailValid(strEmail: String?): Boolean {
        val EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$"
        return strEmail?.matches(EMAIL_REGEX.toRegex()) ?: false
    }

    /*private class ClearLicenseBtnClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			new DataLayer(getActivity()).clearSettings();
			// getActivity().finish();
		}
	}*/
    internal interface OnLicenseDialogFinishListener {
        fun onLicenseDialogFinished(license: LicenseDataHolder?)
    }

    private inner class ExitLicenseBtnClickListener : View.OnClickListener {
        override fun onClick(v: View) {
            // TODO Auto-generated method stub
            dialog.dismiss()
            activity.finishAffinity()
            System.exit(0)
        }
    }

    private inner class RequestLicenseBtnClickListener : View.OnClickListener {
        override fun onClick(v: View) {
            // TODO Auto-generated method stub

            if (mEtInvoiceNo!!.text.toString() != "" || mEtBranchCode!!.text.toString() != "" ||
                mEtCustCode!!.text.toString() != "" || mEtEmail!!.text.toString() != ""
                || mEtPhone!!.text.toString() != "" || (mEtStaffId!!.text.toString() != ""
                        && mEtPwd!!.text.toString() != "")
            ) {
                val ed: SharedPreferences.Editor = MainActivity.Companion.sharePrefSettings!!.edit()
                ed.putString(AppConstantsFlags.KEY_CC, mEtCustCode!!.text.toString())

                //Log.d("sk2",mEtCustCode.getText().toString());
                ed.putString(AppConstantsFlags.KEY_IN, mEtInvoiceNo!!.text.toString())

                //Log.d("sk2",mEtInvoiceNo.getText().toString());
                ed.putString(AppConstantsFlags.KEY_BC, mEtBranchCode!!.text.toString())

                //Log.d("sk2",mEtBranchCode.getText().toString());
                ed.putString(AppConstantsFlags.KEY_EMAIL, mEtEmail!!.text.toString())

                //Log.d("sk2",mEtEmail.getText().toString());
                ed.putString(AppConstantsFlags.KEY_CONTACT_NO, mEtPhone!!.text.toString())

                //Log.d("sk2",mEtPhone.getText().toString());
                ed.putString(AppConstantsFlags.KEY_STAFF_ID, mEtStaffId!!.text.toString())

                //Log.d("sk2",mEtPhone.getText().toString());
                ed.putString(AppConstantsFlags.KEY_PASSWORD_STAFF, mEtPwd!!.text.toString())

                //Log.d("sk2",mEtPhone.getText().toString());
                ed.commit()
            }
            reqType = v.tag.toString()
            //String strUrl = "http://192.168.1.112:82" + "/" + "License.aspx";
            val strUrl = ("http://jeddah.from-ak.com:86" + "/"
                    + "License.aspx")
            if (v.tag.toString() == FULL_LIC && (mEtInvoiceNo!!.text.toString() == "" || mEtBranchCode!!.text.toString() == "" || mEtCustCode!!
                    .getText().toString() == "")
            ) {
                mTvInfo!!.setTextColor(Color.RED)
                mTvInfo!!.text = "Please provide Invoice-No, Branch-code and Customer-code."
                return
            }
            if (mEtStaffId!!.text.toString() != "" && mEtPwd!!.text.toString() == "") {
                mTvInfo!!.setTextColor(Color.RED)
                mTvInfo!!.text = "Password field cannot be left empty."
                return
            }
            if (v.tag.toString() == TRIAL_LIC && mEtEmail!!.text.toString() == "" && mEtPhone!!.text.toString() == "") {
                mTvInfo!!.setTextColor(Color.RED)
                mTvInfo!!.text = "Email-address or phone-number is required for trial license."
                return
            } else if (v.tag.toString() == TRIAL_LIC && mEtEmail!!.text.toString() != ""
                && !isEmailValid(mEtEmail!!.text.toString())
            ) {
                mTvInfo!!.setTextColor(Color.RED)
                mTvInfo!!.text = "Invalid email-address."
                return
            }
            AsyncTask_RequestLicense().executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR, strUrl, "android_id",
                strAndroidId, "mac", strMacAddress, "app_id", APP_ID,
                "cust_code", mEtCustCode!!.text.toString(),
                "invoice_no", mEtInvoiceNo!!.text.toString(),
                "branch_code", mEtBranchCode!!.text.toString(),
                "staff_id", mEtStaffId!!.text.toString(), "staff_pwd",
                mEtPwd!!.text.toString(), "request_type", v.tag
                    .toString(), "email",
                mEtEmail!!.text.toString(), "phone", mEtPhone!!.text
                    .toString()
            )
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
    private inner class AsyncTask_RequestLicense :
        AsyncTask<String?, String?, LicenseDataHolder>() {
        private var progressDialog: ProgressDialog? = null

        override fun onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute()
            progressDialog = ProgressDialog.show(
                activity, "",
                "sending...", true, false
            )
            //					Log.i(LOG_TAG, "Pre executed: " + System.currentTimeMillis());
        }

        override fun doInBackground(vararg params: String?): LicenseDataHolder? {
//					Log.i(LOG_TAG, "Do in background: " + System.currentTimeMillis());
            /*
             * nirmal 23 jan 2015
             *
             * URL url; InputStream inStream = null;
             */
            // BufferedReader bReader = null;
            var strLicense = ""
            //Sarvajeet 11-5
            var TrialDays = ""
            // String strMsg = "";
            val license = LicenseDataHolder()
            // Log.d("qs", "url ="+params[0]);
            val httpClient: HttpClient
            val httpPost: HttpPost

            try {
                httpClient = DefaultHttpClient()
                httpPost = HttpPost(params[0])
                val dataList: MutableList<NameValuePair> = ArrayList()
                var i = 1
                while (i < params.size) {
                    dataList.add(BasicNameValuePair(params[i], params[++i]))
                    i++
                }
                httpPost.entity = UrlEncodedFormEntity(dataList, "UTF-8")
                val httpResponse = httpClient.execute(httpPost)
                val httpResponseEntity = httpResponse.entity
                val str = EntityUtils.toString(httpResponseEntity, "UTF-8")
                //						Log.d(LOG_TAG, "json received as " + str);
                // JSONObject json = new JSONObject(EntityUtils.toString(
                // httpResponseEntity, "UTF-8"));
                val json = JSONObject(str)

                //						Log.d(LOG_TAG, "json received as " + json.toString());

                //Approved- sarvajeet 11-5
                var strResponseCode = ""
                strResponseCode = json.getString("response")
                license.responseCode = strResponseCode
                if (strResponseCode != "") {
                    if (strResponseCode.equals(Companion.APPROVED, ignoreCase = true)) {
                        strLicense = json.getString("license")
                        //Sarvajeet 11-5
                        TrialDays = json.getString("TrialDays")
                        license.trialDays = TrialDays
                        val dateFormat: DateFormat = SimpleDateFormat(
                            "dd-MM-yyyy", Locale.US
                        )
                        var date: Date? = Date()
                        date = dateFormat.parse(json.getString("startDate"))
                        val cal: Calendar = GregorianCalendar()
                        cal.time = date
                        license.startDate = cal
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
                license.referenceNo = json.getString("refNo")
                license.message = json.getString("msg")
                license.setContainsError(false)
                license.errorMsg = ""
            } catch (e: IllegalArgumentException) {
                // strLicense = "Invalid URL";
                strLicense = ""
                TrialDays = ""
                license.setContainsError(true)
                license.errorMsg = "Server not found"
                // strMsg = "Server not found";
            } catch (e: ArrayIndexOutOfBoundsException) {
                // strLicense = "Insufficient parameters";
                strLicense = ""
                TrialDays = ""
                license.setContainsError(true)
                license.errorMsg = "Insufficient data entered"
                // strMsg = "Insufficient data entered";
            } catch (e: UnsupportedEncodingException) {
                // strLicense = "Unsupported encoding";
                strLicense = ""
                TrialDays = ""
                license.setContainsError(true)
                license.errorMsg = "Internal encoding error"
                // strMsg = "Internal encoding error";
            } catch (e: ClientProtocolException) {
                // strLicense = "Client protocol exception";
                strLicense = ""
                TrialDays = ""
                license.setContainsError(true)
                license.errorMsg = "Connection error"
                // strMsg = "Connection error";
            } catch (e: IOException) {
                // strLicense = "IO error";
                strLicense = ""
                TrialDays = ""
                license.setContainsError(true)
                license.errorMsg = "Connection broken"
                //						Log.d(LOG_TAG, "IO exception " + e.getMessage());
                // strMsg = "Connection broken";
            } catch (e: ParseException) {
                // strLicense = "Error in parsing response";
                strLicense = ""
                TrialDays = ""
                license.setContainsError(true)
                license.errorMsg = "Corrupt data received"
                // strMsg = "Corrupt data received";
            } catch (e: java.text.ParseException) {
                // strLicense = "Error in parsing response";
                strLicense = ""
                TrialDays = ""
                license.setContainsError(true)
                license.errorMsg = "Corrupt date received"
                // strMsg = "Corrupt data received";
            } catch (e: JSONException) {
                // strLicense = "Error in parsing json";
                strLicense = ""
                TrialDays = ""
                license.setContainsError(true)
                license.errorMsg = "Improper data received"
                // strMsg = "Improper data received";
            } catch (e: NullPointerException) {
                // strLicense = "Unknown error";
                strLicense = ""
                TrialDays = ""
                license.setContainsError(true)
                license.errorMsg = "Internal error - nullpointer exception"
                // strMsg = "Some error occurred";
            } catch (e: IllegalStateException) {
                // strLicense = "Unknown error";
                strLicense = ""
                TrialDays = ""
                license.setContainsError(true)
                license.errorMsg = "Internal error - data already consumed"
                // strMsg = "Some error occurred";
            } catch (e: Exception) {
                // strLicense = "Unknown error";
                strLicense = ""
                TrialDays = ""
                license.setContainsError(true)
                license.errorMsg = "Some error occurred"
                //						Log.d(LOG_TAG, "unknown exception : " + e.getClass());
                // strMsg = "Some error occurred";
            }
            license.licenseKey = strLicense
            // license.setMessage(strMsg);
//					Log.i(LOG_TAG, "Done in background: " + System.currentTimeMillis());
            return license
        }

        override fun onPostExecute(license: LicenseDataHolder) {
            // TODO Auto-generated method stub
//					Log.i(LOG_TAG, "Post execute: " + System.currentTimeMillis());
            super.onPostExecute(license)

            //					Log.i(LOG_TAG,
//							"Post super-on-post-execute: " + System.currentTimeMillis());
            progressDialog!!.dismiss()
            if (!license.ContainsError()) {
                mLicense = license
                var strMsg = ""
                if (license.responseCode != "") {
                    if (license.responseCode.equals(Companion.APPROVED, ignoreCase = true)) {
                        // strMsg = "Request approved, license received";
                        // strMsg = license.getMessage();
                        mTvInfo!!.setTextColor(Color.rgb(15, 159, 48))
                        strMsg = "Your request has been approved, press Save to continue."
                    } else if (license.responseCode.equals(
                            Companion.PENDING, ignoreCase = true
                        )
                    ) {
                        // strMsg = "Request is pending";
                        // strMsg = license.getMessage();
                        mTvInfo!!.setTextColor(Color.rgb(15, 159, 48))
                        strMsg = "Your request is pending approval."
                    } else if (license.responseCode.equals(
                            Companion.REJECTED, ignoreCase = true
                        )
                    ) {
                        mTvInfo!!.setTextColor(Color.RED)
                        strMsg =
                            ("Request rejected, please contact company salesperson and quote the reference number: "
                                    + license.referenceNo
                                    + "."
                                    + (if (license.message == "") ""
                            else """
 Reason: ${license.message}"""))
                    } else if (license.responseCode.equals(
                            Companion.REJECTED_AMT_PAID, ignoreCase = true
                        )
                    ) {
                        // strMsg = "Request received";
                        // strMsg = license.getMessage();
                        mTvInfo!!.setTextColor(Color.RED)
                        strMsg =
                            "Your request is rejected because your invoice is not fully paid. Please go for trial version."
                    } else if (license.responseCode.equals(
                            Companion.RECEIVED, ignoreCase = true
                        )
                    ) {
                        // save Preferences of CustomerCode, InvNo, BranchCode
                        val ed: SharedPreferences.Editor =
                            MainActivity.Companion.sharePrefSettings!!.edit()
                        if (reqType == TRIAL_LIC) {
                            ed.putString(AppConstantsFlags.KEY_EMAIL, mEtEmail!!.text.toString())

                            //Log.d("sk2",mEtEmail.getText().toString());
                            ed.putString(
                                AppConstantsFlags.KEY_CONTACT_NO,
                                mEtPhone!!.text.toString()
                            )

                            //Log.d("sk2",mEtPhone.getText().toString());
                            ed.commit()
                        } else {
                            ed.putString(AppConstantsFlags.KEY_CC, mEtCustCode!!.text.toString())

                            //Log.d("sk2",mEtCustCode.getText().toString());
                            ed.putString(AppConstantsFlags.KEY_IN, mEtInvoiceNo!!.text.toString())

                            //Log.d("sk2",mEtInvoiceNo.getText().toString());
                            ed.putString(AppConstantsFlags.KEY_BC, mEtBranchCode!!.text.toString())

                            //Log.d("sk2",mEtBranchCode.getText().toString());
                            ed.putString(AppConstantsFlags.KEY_EMAIL, mEtEmail!!.text.toString())

                            //Log.d("sk2",mEtEmail.getText().toString());
                            ed.putString(
                                AppConstantsFlags.KEY_CONTACT_NO,
                                mEtPhone!!.text.toString()
                            )

                            //Log.d("sk2",mEtPhone.getText().toString());
                            ed.commit()
                        }
                        // strMsg = "Request received";
                        // strMsg = license.getMessage();
                        mTvInfo!!.setTextColor(Color.rgb(15, 159, 48))
                        strMsg = "Request sent successfully."
                    } else if (license.responseCode.equals(
                            Companion.INVALID_REQUEST, ignoreCase = true
                        )
                    ) {
                        // strMsg = "Request received";
                        // strMsg = license.getMessage();
                        mTvInfo!!.setTextColor(Color.RED)
                        strMsg = "Request failed, please re-check the data you have provided."
                    } else if (license.responseCode.equals(
                            Companion.TRIAL_EXPIRED, ignoreCase = true
                        )
                    ) {
                        // strMsg = license.getMessage();
                        mTvInfo!!.setTextColor(Color.RED)
                        strMsg =
                            ("Your trial-license has expired, you may contact the company salesperson and quote the reference number: "
                                    + license.referenceNo)
                    }
                }
                // mTvInfo.setText(strMsg + "\n" + license.getMessage());
                mTvInfo!!.text = strMsg
            } else {
                mTvInfo?.setTextColor(Color.RED)
                mTvInfo?.text = license.errorMsg
            }
            // mTvInfo.setText("response received from server");
//					Log.i(LOG_TAG, "Post executed: " + System.currentTimeMillis());
        }
    }

    companion object {
        private const val APP_ID = "queue_window_display"
        private const val FULL_LIC = "2"
        private const val TRIAL_LIC = "3"


        // final String strServerUrl = "";
        private const val RECEIVED = "8"
        private const val PENDING = "9"
        private const val REJECTED = "10"
        private const val APPROVED = "11"
        private const val INVALID_REQUEST = "12"
        private const val TRIAL_EXPIRED = "13"
        private const val REJECTED_AMT_PAID = "14"
    }
}
