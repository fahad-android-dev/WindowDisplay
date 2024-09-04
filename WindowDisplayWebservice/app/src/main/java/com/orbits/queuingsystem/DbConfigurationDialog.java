package com.orbits.queuingsystem;

import static com.orbits.queuingsystem.MainActivity.sharePrefSettings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DbConfigurationDialog extends DialogFragment {
    ///// manjusha 20/dec/2019
//    Spinner spinTokenLength, spinShowHideAdCounter;
//    String tokenLength_txt, showAdCounter_txt;
//    CheckBox prefixCheck, logoCheck, scrollCheck;

    Spinner spinShowHideAdCounter;
    String showAdCounter_txt;
    CheckBox logoCheck, scrollCheck, checkRestart;
    CheckBox checkShowSName,checkShowBranchName,checkShowTime,checkShowuserName;

    static DbConfigurationDialog newInstance() {
        return new DbConfigurationDialog();
    }

    public static void putPref11(String key, String value, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, String.valueOf(value));
        editor.commit();
    }

    public static String getPref11(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater linf = LayoutInflater.from(getActivity());
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        final View inflator = linf.inflate(R.layout.dialog_dbconfig, null);
        final EditText serverNmTxt = (EditText) inflator.findViewById(R.id.editServerName);
        final EditText counterNoTxt = (EditText) inflator.findViewById(R.id.editCounterNo);
        final EditText queuePortTxt = (EditText) inflator.findViewById(R.id.editQueueTCP_Port);
        // final CheckBox checkBox = (CheckBox) inflator.findViewById(R.id.chkAndroid);
        checkRestart = (CheckBox) inflator.findViewById(R.id.chkRestart);
        final EditText editTimeToRestart = (EditText) inflator.findViewById(R.id.editTimeToRestart);

        //prefixCheck = (CheckBox) inflator.findViewById(R.id.prefixCheck);

        logoCheck = (CheckBox) inflator.findViewById(R.id.chkLogo);
        checkShowBranchName = (CheckBox) inflator.findViewById(R.id.chkBranchName);
        checkShowSName = (CheckBox) inflator.findViewById(R.id.chkServiceName);
        checkShowTime = (CheckBox) inflator.findViewById(R.id.chkShowTime);
        checkShowuserName = (CheckBox) inflator.findViewById(R.id.chkuserName);
        scrollCheck = (CheckBox) inflator.findViewById(R.id.chkScroll);
        spinShowHideAdCounter = (Spinner) inflator.findViewById(R.id.spinnerAdCounter);
        List<String> options = new ArrayList<String>();
        options.add("Show Advertisement");
        options.add("Show Counter");
        options.add("Show Nothing");
        // Creating adapter for spinner
        ArrayAdapter<String> adCounterAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, options);
        // Drop down layout style - list view with radio button
        adCounterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinShowHideAdCounter.setAdapter(adCounterAdapter);
        spinShowHideAdCounter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showAdCounter_txt = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String showAdCounter = sharePrefSettings.getString("show_ad_counter", "");

        for (int i = 0; i < options.size(); i++) {
            if (showAdCounter.equals(options.get(i))) {
                spinShowHideAdCounter.setSelection(i);
            }
        }
        checkRestart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //   Toast.makeText(getActivity(), "" + b, Toast.LENGTH_SHORT).show();
                if (!b) {
                    editTimeToRestart.setEnabled(false);
                } else {
                    editTimeToRestart.setEnabled(true);
                }
            }
        });

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
        String serverName = sharePrefSettings.getString("Server_name", "q.goidpoint.com");
        /*Arham Momin 26/06/2024*/
        String QueuePort = sharePrefSettings.getString("QueueTCP_port", "1024");
        String CounterNo = sharePrefSettings.getString("CounterNo", "1");
        String strTimeToRestart = sharePrefSettings.getString(AppConstantsFlags.key_pref_time_to_restart, "");

        //checkBox.setChecked(Boolean.parseBoolean(MainActivity.sharePrefSettings.getString("CheckAndroid", "")));

        checkRestart.setChecked(Boolean.parseBoolean(sharePrefSettings.getString(AppConstantsFlags.key_pref_restart, "")));
        checkShowSName.setChecked(Boolean.parseBoolean(sharePrefSettings.getString(AppConstantsFlags.key_pref_service_name, "")));
        checkShowBranchName.setChecked(Boolean.parseBoolean(sharePrefSettings.getString(AppConstantsFlags.key_pref_branch_name, "")));
        checkShowuserName.setChecked(Boolean.parseBoolean(sharePrefSettings.getString(AppConstantsFlags.key_pref_user_name, "")));
        checkShowTime.setChecked(Boolean.parseBoolean(sharePrefSettings.getString(AppConstantsFlags.key_pref_show_time, "")));
        logoCheck.setChecked(Boolean.parseBoolean(sharePrefSettings.getString(AppConstantsFlags.key_pref_show_logo, "")));
        scrollCheck.setChecked(Boolean.parseBoolean(sharePrefSettings.getString(AppConstantsFlags.key_pref_show_scroll, "")));

        if (!Boolean.parseBoolean(sharePrefSettings.getString(AppConstantsFlags.key_pref_restart, ""))) {
            editTimeToRestart.setEnabled(false);
        } else {
            editTimeToRestart.setEnabled(true);
        }
        serverNmTxt.setText(serverName);
        counterNoTxt.setText(CounterNo);
        queuePortTxt.setText(QueuePort);
        editTimeToRestart.setText(strTimeToRestart);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


// Update visibility of settings based on the new condition
// ... (your code to show/hide settings based on showSettings)


//		builder.setTitle("DATABASE CONFIGURATION");
        TextView title = new TextView(getActivity());
        title.setText("CONNECTIVITY SETTINGS");
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.parseColor("#17B0DE"));
        title.setTextSize(25);
        title.setTypeface(null, Typeface.BOLD);
//		builder.setTitle("CONNECTIVITY SETTINGS");
        builder.setCustomTitle(title);

        builder.setView(inflator)
                // Add action buttons
                .setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (serverNmTxt.getText().toString() != "") {
                            sharePrefSettings.edit().putString("Server_name", serverNmTxt.getText().toString()).commit();
                        }
                        if (queuePortTxt.getText().toString() != "") {
                            // System.out.println(queuePortTxt.getText().toString());
                            sharePrefSettings.edit().putString("QueueTCP_port", queuePortTxt.getText().toString()).commit();
                        }
                        if (counterNoTxt.getText().toString() != "") {
                            sharePrefSettings.edit().putString("CounterNo", counterNoTxt.getText().toString()).commit();
                        }
                        if (editTimeToRestart.getText().toString() != "") {
                            // System.out.println(queuePortTxt.getText().toString());
                            sharePrefSettings.edit().putString(AppConstantsFlags.key_pref_time_to_restart, editTimeToRestart.getText().toString()).commit();
                        }
                        /*if (checkBox.isChecked()) {
                            MainActivity.sharePrefSettings.edit().putString("CheckAndroid", "true").commit();
                            AppConstantsFlags.ANDROID_SERVER = true;
                        } else {
                            MainActivity.sharePrefSettings.edit().putString("CheckAndroid", "false").commit();
                            AppConstantsFlags.ANDROID_SERVER = false;
                        }*/

                        if (checkRestart.isChecked()) {
                            sharePrefSettings.edit().putString(AppConstantsFlags.key_pref_restart, "true").commit();
                        } else {
                            sharePrefSettings.edit().putString(AppConstantsFlags.key_pref_restart, "false").commit();
                        }



                        if (checkShowSName.isChecked()) {
                            MainActivity.sharePrefSettings.edit().putString(AppConstantsFlags.key_pref_service_name, "true").commit();
                        } else {
                            MainActivity.sharePrefSettings.edit().putString(AppConstantsFlags.key_pref_service_name, "false").commit();
                        }
                        if (checkShowBranchName.isChecked()) {
                            MainActivity.sharePrefSettings.edit().putString(AppConstantsFlags.key_pref_branch_name, "true").commit();
                        } else {
                            MainActivity.sharePrefSettings.edit().putString(AppConstantsFlags.key_pref_branch_name, "false").commit();
                        if (checkShowuserName.isChecked()) {
                                MainActivity.sharePrefSettings.edit().putString(AppConstantsFlags.key_pref_user_name, "true").commit();
                        } else {
                            MainActivity.sharePrefSettings.edit().putString(AppConstantsFlags.key_pref_user_name, "false").commit();
                        }
                        if (checkShowTime.isChecked()) {
                            MainActivity.sharePrefSettings.edit().putString(AppConstantsFlags.key_pref_show_time, "true").commit();
                        } else {
                            MainActivity.sharePrefSettings.edit().putString(AppConstantsFlags.key_pref_show_time, "false").commit();
                        }

                        boolean showSettings = false;
                        if (showAdCounter_txt.equals("Show Nothing")) {
                            showSettings = true;
                        } else if (showAdCounter_txt.equals("Show Advertisement") &&
                                logoCheck.isChecked() && scrollCheck.isChecked()) {
                            // Show settings only if both "Show Logo" and "Show Scroll" are checked
                            showSettings = true;
                        }
                        if (logoCheck.isChecked()) {
                            sharePrefSettings.edit().putString(AppConstantsFlags.key_pref_show_logo, "true").commit();
                        } else {
                            sharePrefSettings.edit().putString(AppConstantsFlags.key_pref_show_logo, "false").commit();
                        }
                        if (scrollCheck.isChecked()) {
                            sharePrefSettings.edit().putString(AppConstantsFlags.key_pref_show_scroll, "true").commit();
                        } else {
                            sharePrefSettings.edit().putString(AppConstantsFlags.key_pref_show_scroll, "false").commit();
                        }

                        ///MainActivity.sharePrefSettings.edit().putString("tokenLength", tokenLength_txt).commit();
                        sharePrefSettings.edit().putString("show_ad_counter", showAdCounter_txt).commit();
                        //nirmal 01 dec 2014 -- check values entered in counterNo and Queue TCP port, before saving them
//				TokenActivity.tokenHandlerDelay = 500;
                        DbConfigurationDialog.this.getDialog().cancel();
                        getActivity().finishAffinity();
                        System.exit(0);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DbConfigurationDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }


    public boolean isNumeric(String str) {

        try {
            Integer.parseInt(str);
        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(), "Calibration Value should be Numeric.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

}
