package com.orbits.queuingsystem

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast

class DbConfigurationDialog : DialogFragment() {
    ///// manjusha 20/dec/2019
    //    Spinner spinTokenLength, spinShowHideAdCounter;
    //    String tokenLength_txt, showAdCounter_txt;
    //    CheckBox prefixCheck, logoCheck, scrollCheck;
    var spinShowHideAdCounter: Spinner? = null
    var showAdCounter_txt: String? = null
    var showTopText: String? = null
    var showMiddleText: String? = null
    var logoCheck: CheckBox? = null
    var scrollCheck: CheckBox? = null
    var checkRestart: CheckBox? = null
    var checkShowSName: CheckBox? = null
    var checkShowBranchName: CheckBox? = null
    var checkboxAll: CheckBox? = null
    var checkShowTime: CheckBox? = null
    var spinTop: Spinner? = null
    var spinMiddle: Spinner? = null
    var spinSelectCounter: Spinner? = null


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val linf = LayoutInflater.from(activity)

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        val inflator = linf.inflate(R.layout.dialog_dbconfig, null)
        val serverNmTxt = inflator.findViewById<View>(R.id.editServerName) as EditText
        val counterNoTxt = inflator.findViewById<View>(R.id.editCounterNo) as EditText
        val queuePortTxt = inflator.findViewById<View>(R.id.editQueueTCP_Port) as EditText
        // final CheckBox checkBox = (CheckBox) inflator.findViewById(R.id.chkAndroid);
        checkRestart = inflator.findViewById<View>(R.id.chkRestart) as CheckBox
        val editTimeToRestart = inflator.findViewById<View>(R.id.editTimeToRestart) as EditText

        //prefixCheck = (CheckBox) inflator.findViewById(R.id.prefixCheck);
        logoCheck = inflator.findViewById<View>(R.id.chkLogo) as CheckBox
        checkShowBranchName = inflator.findViewById<View>(R.id.chkBranchName) as CheckBox
        checkShowSName = inflator.findViewById<View>(R.id.chkServiceName) as CheckBox
        checkboxAll = inflator.findViewById<View>(R.id.checkboxAll) as CheckBox
        checkShowTime = inflator.findViewById<View>(R.id.chkShowTimeCheckbox) as CheckBox
        scrollCheck = inflator.findViewById<View>(R.id.chkScroll) as CheckBox
        spinShowHideAdCounter = inflator.findViewById<View>(R.id.spinnerAdCounter) as Spinner
        spinSelectCounter = inflator.findViewById<View>(R.id.spinnerCounterSelection) as Spinner




        spinTop = inflator.findViewById<View>(R.id.spinner_top) as Spinner
        spinMiddle = inflator.findViewById<View>(R.id.spinner_middle) as Spinner

        val options: MutableList<String> = ArrayList()
        options.add("Show Advertisement")
        options.add("Show Counter")
        options.add("Show Nothing")

        val topOptions: MutableList<String> = ArrayList()
        topOptions.add("Service Name")
        topOptions.add("Branch Name")
        topOptions.add("User Name")
        topOptions.add("None")

        val topAdapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, topOptions)
        topAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinTop?.adapter = topAdapter

        

        spinTop?.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                showTopText = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case when nothing is selected
            }
        }

        val showTopCounter: String =
            MainActivity.sharePrefSettings?.getString("top_text", "")!!
        val userName: String = MainActivity.sharePrefSettings?.getString(
            AppConstantsFlags.key_pref_user_name,
            ""
        )!!
        val branchName: String = MainActivity.sharePrefSettings?.getString(
            AppConstantsFlags.key_pref_branch_name,
            ""
        )!!
        val serviceName: String = MainActivity.sharePrefSettings?.getString(
            AppConstantsFlags.key_pref_service_name,
            ""
        )!!

        println("here is showTopCounter $showTopCounter")
        println("here is userName 111 $userName")
        println("here is branchName 111 $branchName")
        println("here is serviceName 111 $serviceName")

        for (i in topOptions.indices) {
            if (showTopCounter == topOptions[i]) {
                spinTop?.setSelection(i)
            }
        }

        /*val middleOptions: MutableList<String> = ArrayList()
        middleOptions.add("Service Name")
        middleOptions.add("Branch Name")
        middleOptions.add("User Name")
        middleOptions.add("None")
*/
        val newMiddleOptions: MutableList<String> = ArrayList()
        newMiddleOptions.add("Top")
        newMiddleOptions.add("Middle")

        val middleAdapter =
            ArrayAdapter(activity, android.R.layout.simple_spinner_item, newMiddleOptions)
        middleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinMiddle?.adapter = middleAdapter

        spinMiddle?.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                showMiddleText = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case when nothing is selected
            }
        }
        val showMiddleCounter: String =
            MainActivity.sharePrefSettings?.getString("middle_text", "")!!

        println("here is showMiddleCounter $showMiddleCounter")

        for (i in newMiddleOptions.indices) {
            if (showMiddleCounter == newMiddleOptions[i]) {
                spinMiddle?.setSelection(i)
            }
        }

        // Creating adapter for spinner
        val adCounterAdapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, options)
        // Drop down layout style - list view with radio button
        adCounterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // attaching data adapter to spinner
        spinShowHideAdCounter?.adapter = adCounterAdapter
        spinShowHideAdCounter?.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                showAdCounter_txt = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        val showAdCounter: String =
            MainActivity.sharePrefSettings?.getString("show_ad_counter", "")!!

        for (i in options.indices) {
            if (showAdCounter == options[i]) {
                spinShowHideAdCounter?.setSelection(i)
            }
        }
        checkRestart?.setOnCheckedChangeListener { compoundButton, b -> //   Toast.makeText(getActivity(), "" + b, Toast.LENGTH_SHORT).show();
            if (!b) {
                editTimeToRestart.isEnabled = false
            } else {
                editTimeToRestart.isEnabled = true
            }
        }


        val selectCounterOptions: MutableList<String> = ArrayList()
        selectCounterOptions.addAll(MainActivity.viewModel.dataList.map { it.id } as ArrayList<String>)

        println("here is options list  ::::: $options")

        val selectCounterAdapter =
            ArrayAdapter(activity, android.R.layout.simple_spinner_item, selectCounterOptions)
        selectCounterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinSelectCounter?.adapter = selectCounterAdapter

        var webId = ""
        var serviceId = ""
        var name = ""

        spinSelectCounter?.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                if (MainActivity.viewModel.dataList.isNotEmpty()){
                    webId = MainActivity.viewModel.dataList[position].id ?: ""
                    serviceId = MainActivity.viewModel.dataList[position].serviceId ?: ""
                    name = MainActivity.viewModel.dataList[position].counterType ?: ""
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case when nothing is selected
            }
        }


        //// manjusha 25/10/2018 change number of token digits////
        ////manjusha commented on 20/dec/2019///
//        spinTokenLength = (Spinner) inflator.findViewById(R.id.spinnerTokenLength);
//        List<String> categories = new ArrayList<String>();
//        categories.add("3");
//        categories.add("4");
//        // Creating adapter for spinner
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
//        // Drop down layout style - list view with radio button
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        // attaching data adapter to spinner
//        spinTokenLength.setAdapter(dataAdapter);
//        spinTokenLength.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                tokenLength_txt = parent.getItemAtPosition(position).toString();
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//        String tokenLength = MainActivity.sharePrefSettings.getString("tokenLength", "");
//
//        for (int i = 0; i < categories.size(); i++) {
//            if (tokenLength.equals(categories.get(i))) {
//                spinTokenLength.setSelection(i);
//            }
//        }
        //// close /////
        val serverName: String =
            MainActivity.sharePrefSettings?.getString("Server_name", "")!!
        /*Arham Momin 26/06/2024*/
        val QueuePort: String =
            MainActivity.sharePrefSettings?.getString("QueueTCP_port", "1024")!!
        val CounterNo: String =
            MainActivity.sharePrefSettings?.getString("CounterNo", "1")!!
        val strTimeToRestart: String = MainActivity.sharePrefSettings?.getString(
            AppConstantsFlags.key_pref_time_to_restart,
            ""
        )!!

        //checkBox.setChecked(Boolean.parseBoolean(MainActivity.sharePrefSettings.getString("CheckAndroid", "")));
        checkRestart?.isChecked = MainActivity.sharePrefSettings?.getString(
            AppConstantsFlags.key_pref_restart,
            ""
        ).toBoolean()
        checkShowSName?.isChecked = MainActivity.sharePrefSettings?.getString(
            AppConstantsFlags.key_pref_service_name,
            ""
        ).toBoolean()
        checkShowBranchName?.isChecked = MainActivity.sharePrefSettings?.getString(
            AppConstantsFlags.key_pref_branch_name,
            ""
        ).toBoolean()
        checkShowTime?.isChecked = MainActivity.sharePrefSettings?.getString(
            AppConstantsFlags.key_pref_show_time,
            ""
        ).toBoolean()
        checkboxAll?.isChecked = MainActivity.sharePrefSettings?.getString(
            AppConstantsFlags.key_pref_show_time,
            ""
        ).toBoolean()
        logoCheck?.isChecked = MainActivity.sharePrefSettings?.getString(
            AppConstantsFlags.key_pref_show_logo,
            ""
        ).toBoolean()
        scrollCheck?.isChecked = MainActivity.sharePrefSettings?.getString(
            AppConstantsFlags.key_pref_show_scroll,
            ""
        ).toBoolean()

        if (!MainActivity.sharePrefSettings?.getString(
                AppConstantsFlags.key_pref_restart,
                ""
            ).toBoolean()
        ) {
            editTimeToRestart.isEnabled = false
        } else {
            editTimeToRestart.isEnabled = true
        }
        serverNmTxt.setText(serverName)
        counterNoTxt.setText(CounterNo)
        queuePortTxt.setText(QueuePort)
        editTimeToRestart.setText(strTimeToRestart)

        val builder = AlertDialog.Builder(activity)


        // Update visibility of settings based on the new condition
// ... (your code to show/hide settings based on showSettings)


//		builder.setTitle("DATABASE CONFIGURATION");
        val title = TextView(activity)
        title.text = "CONNECTIVITY SETTINGS"
        title.setPadding(10, 10, 10, 10)
        title.gravity = Gravity.CENTER
        title.setTextColor(Color.parseColor("#17B0DE"))
        title.textSize = 25f
        title.setTypeface(null, Typeface.BOLD)
        //		builder.setTitle("CONNECTIVITY SETTINGS");
        builder.setCustomTitle(title)

        builder.setView(inflator) // Add action buttons
            .setPositiveButton(R.string.submit) { dialog, id ->
                if (serverNmTxt.text.toString() !== "") {
                    MainActivity.sharePrefSettings?.edit()
                        ?.putString("Server_name", serverNmTxt.text.toString())?.commit()
                }
                if (queuePortTxt.text.toString() !== "") {
                    // System.out.println(queuePortTxt.getText().toString());
                    MainActivity.sharePrefSettings?.edit()
                        ?.putString("QueueTCP_port", queuePortTxt.text.toString())?.commit()
                }
                if (counterNoTxt.text.toString() !== "") {
                    MainActivity.sharePrefSettings?.edit()
                        ?.putString("CounterNo", counterNoTxt.text.toString())?.commit()
                }
                if (editTimeToRestart.text.toString() !== "") {
                    // System.out.println(queuePortTxt.getText().toString());
                    MainActivity.sharePrefSettings?.edit()?.putString(
                        AppConstantsFlags.key_pref_time_to_restart,
                        editTimeToRestart.text.toString()
                    )?.commit()
                }

                /*if (checkBox.isChecked()) {
                                MainActivity.sharePrefSettings.edit().putString("CheckAndroid", "true").commit();
                                AppConstantsFlags.ANDROID_SERVER = true;
                            } else {
                                MainActivity.sharePrefSettings.edit().putString("CheckAndroid", "false").commit();
                                AppConstantsFlags.ANDROID_SERVER = false;
                            }*/
                if (checkRestart?.isChecked == true) {
                    println("here is checked 1")
                    MainActivity.sharePrefSettings?.edit()
                        ?.putString(AppConstantsFlags.key_pref_restart, "true")?.commit()
                } else {
                    println("here is checked 2")
                    MainActivity.sharePrefSettings?.edit()
                        ?.putString(AppConstantsFlags.key_pref_restart, "false")?.commit()
                }


                /* if (checkShowSName.isChecked()) {
                                System.out.println("here is checked 3");
                                MainActivity.sharePrefSettings.edit().putString(AppConstantsFlags.key_pref_service_name, "true").commit();
                            } else {
                                System.out.println("here is checked 4");
                                MainActivity.sharePrefSettings.edit().putString(AppConstantsFlags.key_pref_service_name, "false").commit();
                            }
                            if (checkShowBranchName.isChecked()) {
                                System.out.println("here is checked 5");
                                MainActivity.sharePrefSettings.edit().putString(AppConstantsFlags.key_pref_branch_name, "true").commit();
                            } else {
                                System.out.println("here is checked 6");
                                MainActivity.sharePrefSettings.edit().putString(AppConstantsFlags.key_pref_branch_name, "false").commit();
                            }
                            if (checkShowTime.isChecked()) {
                                System.out.println("here is checked 7");
                                MainActivity.sharePrefSettings.edit().putString(AppConstantsFlags.key_pref_show_time, "true").commit();
                            }else {
                                System.out.println("here is checked 8");
                                MainActivity.sharePrefSettings.edit().putString(AppConstantsFlags.key_pref_show_time, "false").commit();
                            }*/
                /* if (checkboxAll.isChecked()) {
                                System.out.println("here is checked 9");
                                MainActivity.sharePrefSettings.edit().putString(AppConstantsFlags.key_pref_service_name, "true").commit();
                                MainActivity.sharePrefSettings.edit().putString(AppConstantsFlags.key_pref_branch_name, "true").commit();
                                MainActivity.sharePrefSettings.edit().putString(AppConstantsFlags.key_pref_show_time, "true").commit();
                            }else {
                                MainActivity.sharePrefSettings.edit().putString(AppConstantsFlags.key_pref_service_name, "false").commit();
                                MainActivity.sharePrefSettings.edit().putString(AppConstantsFlags.key_pref_branch_name, "false").commit();
                                MainActivity.sharePrefSettings.edit().putString(AppConstantsFlags.key_pref_show_time, "false").commit();
                            }*/
                if (checkShowTime?.isChecked == true) {
                    println("here is checked 9")
                    MainActivity.sharePrefSettings?.edit()
                        ?.putString(AppConstantsFlags.key_pref_show_time, "true")?.commit()
                } else {
                    MainActivity.sharePrefSettings?.edit()
                        ?.putString(AppConstantsFlags.key_pref_show_time, "false")?.commit()
                }


                //                        if (checkShowSName.isChecked()) {
//                            sharePrefSettings.edit().putString(AppConstantsFlags.key_pref_service_name, "true").commit();
//                        } else if (checkShowBranchName.isChecked()) {
//                            sharePrefSettings.edit().putString(AppConstantsFlags.key_pref_branch_name, "true").commit();
//                        } else if (checkShowTime.isChecked()) {
//                            sharePrefSettings.edit().putString(AppConstantsFlags.key_pref_show_time, "true").commit();
//                        } else {
//                            // Handle the case where no checkbox is checked, if needed
//                        }
//                        sharePrefSettings.edit().commit();
                var showBranch = false
                var showBranchTop = false
                var showService = false
                var showServiceTop = false
                var showUserName = false
                var showUserNameTop = false
                var showNone = false
                var showNoneTop = false

                if (showMiddleText == "Top") {
                    showServiceTop = true
                }
                if (showMiddleText == "Middle") {
                    showService = true
                }
                /*if (showMiddleText == "User Name") {
                    showUserName = true
                }
                if (showTopText == "User Name") {
                    showUserNameTop = true
                }
                if (showTopText == "Service Name") {
                    showServiceTop = true
                }
                if (showTopText == "Branch Name") {
                    showBranchTop = true
                }
                if (showTopText == "None") {
                    showNoneTop = true
                }

                if (showMiddleText == "None") {
                    showNone = true
                }*/


                /*if (showBranch) {
                    MainActivity.sharePrefSettings?.edit()
                        ?.putString(AppConstantsFlags.key_pref_branch_name, "true")?.commit()
                } else {
                    MainActivity.sharePrefSettings?.edit()
                        ?.putString(AppConstantsFlags.key_pref_branch_name, "false")?.commit()
                }
                if (showUserName) {
                    MainActivity.sharePrefSettings?.edit()
                        ?.putString(AppConstantsFlags.key_pref_user_name, "true")?.commit()
                } else {
                    MainActivity.sharePrefSettings?.edit()
                        ?.putString(AppConstantsFlags.key_pref_user_name, "false")?.commit()
                }*/
                if (showService) {
                    MainActivity.sharePrefSettings?.edit()
                        ?.putString(AppConstantsFlags.key_pref_service_name, "true")?.commit()
                } else {
                    MainActivity.sharePrefSettings?.edit()
                        ?.putString(AppConstantsFlags.key_pref_service_name, "false")?.commit()
                }




               /* if (showUserNameTop) {
                    MainActivity.sharePrefSettings?.edit()
                        ?.putString(AppConstantsFlags.key_pref_user_top, "true")?.commit()
                } else {
                    MainActivity.sharePrefSettings?.edit()
                        ?.putString(AppConstantsFlags.key_pref_user_top, "false")?.commit()
                }
                if (showBranchTop) {
                    MainActivity.sharePrefSettings?.edit()
                        ?.putString(AppConstantsFlags.key_pref_branch_top, "true")?.commit()
                } else {
                    MainActivity.sharePrefSettings?.edit()
                        ?.putString(AppConstantsFlags.key_pref_branch_top, "false")?.commit()
                }*/
                if (showServiceTop) {
                    MainActivity.sharePrefSettings?.edit()
                        ?.putString(AppConstantsFlags.key_pref_service_top, "true")?.commit()
                } else {
                    MainActivity.sharePrefSettings?.edit()
                        ?.putString(AppConstantsFlags.key_pref_service_top, "false")?.commit()
                }
               /* if (showNoneTop) {
                    MainActivity.sharePrefSettings?.edit()
                        ?.putString(AppConstantsFlags.key_pref_none_top, "true")?.commit()
                } else {
                    MainActivity.sharePrefSettings?.edit()
                        ?.putString(AppConstantsFlags.key_pref_none_top, "false")?.commit()
                }

                if (showNone) {
                    MainActivity.sharePrefSettings?.edit()
                        ?.putString(AppConstantsFlags.key_pref_none, "true")?.commit()
                } else {
                    MainActivity.sharePrefSettings?.edit()
                        ?.putString(AppConstantsFlags.key_pref_none, "false")?.commit()
                }*/





               /* if (showNone) {
                    MainActivity.sharePrefSettings?.edit()
                        ?.putString(AppConstantsFlags.key_pref_service_name, "false")?.commit()
                    MainActivity.sharePrefSettings?.edit()
                        ?.putString(AppConstantsFlags.key_pref_user_name, "false")?.commit()
                    MainActivity.sharePrefSettings?.edit()
                        ?.putString(AppConstantsFlags.key_pref_branch_name, "false")?.commit()
                }*/


                /* Arham Momin*/
                var showSettings = false
                if (showAdCounter_txt == "Show Nothing") {
                    showSettings = true
                } else if (showAdCounter_txt == "Show Advertisement" &&
                    logoCheck?.isChecked == true && scrollCheck?.isChecked == true
                ) {
                    // Show settings only if both "Show Logo" and "Show Scroll" are checked
                    showSettings = true
                }
                if (logoCheck?.isChecked == true) {
                    MainActivity.sharePrefSettings?.edit()
                        ?.putString(AppConstantsFlags.key_pref_show_logo, "true")?.commit()
                } else {
                    MainActivity.sharePrefSettings?.edit()
                        ?.putString(AppConstantsFlags.key_pref_show_logo, "false")?.commit()
                }
                if (scrollCheck?.isChecked == true) {
                    MainActivity.sharePrefSettings?.edit()
                        ?.putString(AppConstantsFlags.key_pref_show_scroll, "true")?.commit()
                } else {
                    MainActivity.sharePrefSettings?.edit()
                        ?.putString(AppConstantsFlags.key_pref_show_scroll, "false")?.commit()
                }

                MainActivity.sharePrefSettings?.edit()?.putString("counterId", webId)?.commit()
                MainActivity.sharePrefSettings?.edit()?.putString("serviceId", serviceId)?.commit()
                MainActivity.sharePrefSettings?.edit()?.putString("counterType",name)?.commit()

                ///MainActivity.sharePrefSettings.edit().putString("tokenLength", tokenLength_txt).commit();
                MainActivity.sharePrefSettings?.edit()
                    ?.putString("show_ad_counter", showAdCounter_txt)?.commit()
                MainActivity.sharePrefSettings?.edit()?.putString("top_text", showTopText)
                    ?.commit()
                MainActivity.sharePrefSettings?.edit()
                    ?.putString("middle_text", showMiddleText)?.commit()
                //nirmal 01 dec 2014 -- check values entered in counterNo and Queue TCP port, before saving them
                //TokenActivity.tokenHandlerDelay = 500;
                dialog.cancel()
                activity.finishAffinity()
                System.exit(0)
            }
            .setNegativeButton(R.string.cancel) { dialog, id -> dialog.cancel() }
        return builder.create()
    }


    fun isNumeric(str: String): Boolean {
        try {
            str.toInt()
        } catch (e: NumberFormatException) {
            Toast.makeText(activity, "Calibration Value should be Numeric.", Toast.LENGTH_LONG)
                .show()
            return false
        }
        return true
    }

    companion object {
        fun newInstance(): DbConfigurationDialog {
            return DbConfigurationDialog()
        }

        fun putPref11(key: String?, value: String, context: Context?) {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = prefs.edit()
            editor.putString(key, value.toString())
            editor.commit()
        }

        fun getPref11(key: String?, context: Context?): String? {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getString(key, null)
        }
    }
}
