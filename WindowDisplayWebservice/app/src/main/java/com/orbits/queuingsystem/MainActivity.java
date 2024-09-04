package com.orbits.queuingsystem;

import static android.widget.Toast.LENGTH_SHORT;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.orbits.queuingsystem.LicenseDialog.OnLicenseDialogFinishListener;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class MainActivity extends Activity implements
        OnLicenseDialogFinishListener {
    static final String LOG_TAG = "qs";
    //Shums 18/09/2018
    //++++====++++
    static final int TIME_OUT = 5000;
    static final int MSG_DISMISS_DIALOG = 0;
    public static String sdcardPath = Environment.getExternalStorageDirectory()
            .toString();
    // private TextView mTokenTouchView;
    public static HashMap<String, String> digitMap = new HashMap<String, String>();
    public static SharedPreferences sharePrefSettings;
    public static String serverName;
    // public static String tcp_ip_port;
    public static String dbName;
    public static int QUEUE_SERVER_PORT;
    int count = 0;
    // public CustomKeyboard mCustomKeyboard;
    public LinearLayout advLayout;
    // public RelativeLayout kbdLayout;
    public RelativeLayout vidLayout;
    public VideoView videoPlayer;
    // private int mTokenColor, mTokenBlinkColor;
    // StringBuilder enText,arText;
    public String message;
    // public static dbConnection con;
    LinearLayout ll_mainLayout, ll_tokenLayout, ll_logo_adv, ll_right,
            ll_top_scroll, ll_bottom_scroll;
    ScrollTextViewArabic tv_scrollTxtArabic;
    ScrollTextView tv_scrollTxtEng;
    TimerTask timerTask;
    Timer timer;
    String strTimeToRestart;
    Calendar calRunningTime;
    private int COUNTER_NO;
    private byte mConnectionCheckCounter;
    private DataLayer mDataLayer;
    private boolean ISTRIAL = true;
    private AlertDialog mAlertDialog;
    File logFile1;
    FileWriter writer;
    //static FileWriter writer;
    // static File logFile1;
    //Shums 18/09/2018
    //++++====++++
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_DISMISS_DIALOG:
                    if (mAlertDialog != null && mAlertDialog.isShowing()) {
                        mAlertDialog.dismiss();
                    }
                    break;

                default:
                    break;
            }
        }
    };
    private CountDownTimer lastRunDateUpdateTimer = new CountDownTimer(60000,
            10000) {

        @Override
        public void onTick(long millisUntilFinished) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onFinish() {
            // TODO Auto-generated method stub
            mDataLayer.updateLastRunDate();
            resetLastRunDateUpdateTimer();
            if (!isLicenseValid(false)) {
                showLicenseDialog();
            }
        }
    };

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

    //++++====++++
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        calRunningTime = Calendar.getInstance();
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermissions()) {

            } else {
                //Toast.makeText(this, "already granted::", LENGTH_SHORT).show();
            }
        }

        File logFile = new File(Environment.getExternalStorageDirectory()
                + "/QueueWindowDisplay");
        if (!logFile.exists()) {
            logFile.mkdir();
        }
        logFile1 = new File(logFile, "WindowDisplayLog.txt");

        mDataLayer = new DataLayer(getApplicationContext());
        // nirmal 07 july 2014 -- load layout file according to preferences
        sharePrefSettings = getSharedPreferences("QueuingSystemPreff", 0);

        AppConstantsFlags.SHOW_LOGO = Boolean.parseBoolean(MainActivity.sharePrefSettings.getString(AppConstantsFlags.key_pref_show_logo, ""));
        AppConstantsFlags.SHOW_SCROLL = Boolean.parseBoolean(MainActivity.sharePrefSettings.getString(AppConstantsFlags.key_pref_show_scroll, ""));
        AppConstantsFlags.SHOW_SERVICE_NAME = Boolean.parseBoolean(MainActivity.sharePrefSettings.getString(AppConstantsFlags.key_pref_service_name, ""));
        AppConstantsFlags.SHOW_BRANCH_NAME = Boolean.parseBoolean(MainActivity.sharePrefSettings.getString(AppConstantsFlags.key_pref_branch_name, ""));
        AppConstantsFlags.SHOW_USER_NAME = Boolean.parseBoolean(MainActivity.sharePrefSettings.getString(AppConstantsFlags.key_pref_user_name, ""));
        AppConstantsFlags.SHOW_TIME = Boolean.parseBoolean(MainActivity.sharePrefSettings.getString(AppConstantsFlags.key_pref_show_time, ""));
        if (AppConstantsFlags.SHOW_ADVERTIZEMENT != "") {
            AppConstantsFlags.SHOW_ADVERTIZEMENT = MainActivity.sharePrefSettings.getString("show_ad_counter", "");
        } else if (AppConstantsFlags.SHOW_SCROLL && AppConstantsFlags.SHOW_LOGO) {
            setContentView(R.layout.activity_main_ad_without_logo_scroll);
        }
        else {
            AppConstantsFlags.SHOW_ADVERTIZEMENT = MainActivity.sharePrefSettings.getString("show_ad_counter", "Show Advertisement");
        }

        if ((AppConstantsFlags.SHOW_ADVERTIZEMENT.equals("Show Nothing"))
                && !AppConstantsFlags.SHOW_SCROLL) {
            setContentView(R.layout.activity_main_full_screen);
        } else if (!(AppConstantsFlags.SHOW_ADVERTIZEMENT.equals("Show Nothing"))
                && AppConstantsFlags.SHOW_LOGO && AppConstantsFlags.SHOW_BRANCH_NAME && AppConstantsFlags.SHOW_SERVICE_NAME) {
            setContentView(R.layout.layout_main_service_branch_name);
        } else if (!(AppConstantsFlags.SHOW_ADVERTIZEMENT.equals("Show Nothing"))
                && AppConstantsFlags.SHOW_LOGO
                && !AppConstantsFlags.SHOW_SCROLL) {
            setContentView(R.layout.activity_main_ad_without_scroll);

        } else if ((AppConstantsFlags.SHOW_ADVERTIZEMENT.equals("Show Nothing")) && (AppConstantsFlags.SHOW_SCROLL)) {
            setContentView(R.layout.activity_main_scroll_without_ad);
        } else if (!(AppConstantsFlags.SHOW_ADVERTIZEMENT.equals("Show Nothing"))
                && AppConstantsFlags.SHOW_SCROLL
                && !AppConstantsFlags.SHOW_LOGO) {
            setContentView(R.layout.activity_main_ad_scroll_without_logo);
        } else if (!(AppConstantsFlags.SHOW_ADVERTIZEMENT.equals("Show Nothing"))
                && !AppConstantsFlags.SHOW_SCROLL
                && !AppConstantsFlags.SHOW_LOGO) {
            setContentView(R.layout.activity_main_ad_without_logo_scroll);
        }

        else {
            setContentView(R.layout.activity_main);
        }
        boolean restart = Boolean.parseBoolean(MainActivity.sharePrefSettings.getString(AppConstantsFlags.key_pref_restart, ""));
        strTimeToRestart = MainActivity.sharePrefSettings.getString(AppConstantsFlags.key_pref_time_to_restart, "");
        if (restart) {
            startTimer();
        } else {
            if (timer != null) {
                timer.purge();
                timer.cancel();
            }

            if (timerTask != null) {
                timerTask.cancel();
            }
        }

        /*if (!AppConstantsFlags.SHOW_ADVERTIZEMENT
                && !AppConstantsFlags.SHOW_SCROLL) {
            setContentView(R.layout.activity_main_full_screen);
        } else if (AppConstantsFlags.SHOW_ADVERTIZEMENT
                && AppConstantsFlags.SHOW_LOGO
                && !AppConstantsFlags.SHOW_SCROLL) {
            setContentView(R.layout.activity_main_ad_without_scroll);
        } else if (!AppConstantsFlags.SHOW_ADVERTIZEMENT
                && AppConstantsFlags.SHOW_SCROLL) {
            setContentView(R.layout.activity_main_scroll_without_ad);
        } else if (AppConstantsFlags.SHOW_ADVERTIZEMENT
                && AppConstantsFlags.SHOW_SCROLL
                && !AppConstantsFlags.SHOW_LOGO) {
            setContentView(R.layout.activity_main_ad_scroll_without_logo);
        } else if (AppConstantsFlags.SHOW_ADVERTIZEMENT
                && !AppConstantsFlags.SHOW_SCROLL
                && !AppConstantsFlags.SHOW_LOGO) {
            setContentView(R.layout.activity_main_ad_without_logo_scroll);
        } else {
           setContentView(R.layout.activity_main);
        }*/
        // mTokenTouchView = (TextView) findViewById(R.id.txtTokenNo);
        // nirmal 07 july 2014 -- hide the action-bar
        final ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        // TokenActivity.mTokenColor = Color.RED;
        // TokenActivity.mTokenBlinkColor = Color.BLUE;
        WifiManager wifiManager;
        // nirmal 18 june 2014 -- use global strings to store english and arabic
        // scroll-texts
        // enText=new StringBuilder();
        // arText=new StringBuilder();

        // nirmal 07 july 2014
        // sharePrefSettings = getSharedPreferences("QueuingSystemPreff", 0);
        serverName = sharePrefSettings.getString("Server_name", "");

        // tcp_ip_port = sharePrefSettings.getString("TCPIP_port", "");

        /*
         * // nirmal 17 june 2014 -- set the flags, whether to display ads and
         * // scroll-text or not App_Constants_Flags.SHOW_ADVERTIZEMENT =
         * sharePrefSettings.getBoolean( App_Constants_Flags.key_pref_show_ad,
         * true); App_Constants_Flags.SHOW_TOP_SCROLL =
         * sharePrefSettings.getBoolean(
         * App_Constants_Flags.key_pref_show_top_scroll, true);
         * App_Constants_Flags.SHOW_BOTTOM_SCROLL =
         * sharePrefSettings.getBoolean(
         * App_Constants_Flags.key_pref_show_bottom_scroll, true);
         */
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }

        /*
         * nirmal 29 oct 2014 -- no need for disabling strict-mode because
         * network operation is now performed by async-task.
         *
         * if (android.os.Build.VERSION.SDK_INT > 9) { StrictMode.ThreadPolicy
         * policy = new StrictMode.ThreadPolicy.Builder() .permitAll().build();
         * StrictMode.setThreadPolicy(policy); }
         */
        createDirIfNotExists();
        checkLicense();
        // nirmal 17 june 2014 -- initialize the reference to
        // top-parent-linear-layout of main-activity
        ll_mainLayout = (LinearLayout) findViewById(R.id.ll_main_activity_top_parent);
        ll_right = (LinearLayout) findViewById(R.id.rightLayout);
        ll_tokenLayout = (LinearLayout) findViewById(R.id.ll_fragment_token_container);

        // nirmal 07 july 2014 -- initialize references only if those components
        // are loaded
        if (AppConstantsFlags.SHOW_SCROLL) {
            tv_scrollTxtArabic = (ScrollTextViewArabic) findViewById(R.id.scrolltext_Ar);
            tv_scrollTxtEng = (ScrollTextView) findViewById(R.id.scrolltext);
            ll_top_scroll = (LinearLayout) findViewById(R.id.ll_main_top_scroll);
            ll_bottom_scroll = (LinearLayout) findViewById(R.id.ll_main_bottom_scroll);
        }
        if (AppConstantsFlags.SHOW_ADVERTIZEMENT.equals("Show Advertisement")) {
            ll_logo_adv = (LinearLayout) findViewById(R.id.ll_container_logo_ads);
        }


    }

    public void checkLicense() {
        mDataLayer.updateLastRunDate();
        if (!isLicenseValid(true)) {
            // if (getPref11("dialog", this) == null) {
            showLicenseDialog();
            // }
        } else {
            // mDataLayer.updateLastRunDate();
            lastRunDateUpdateTimer.start();
            if (ISTRIAL) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(
                        MainActivity.this);
                alertBuilder
                        .setMessage("Do you wish to purchase full license?");
                alertBuilder.setPositiveButton("Purchase full-license",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                showLicenseDialog();
                            }
                        });
                alertBuilder.setNegativeButton("Continue trial",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                dialog.dismiss();
                            }
                        });
                alertBuilder.setCancelable(false);
                //Shums 18/09/2018
                //++++====++++
                //alertBuilder.show();
                mAlertDialog = alertBuilder.create();
                mAlertDialog.show();
                mHandler.sendEmptyMessageDelayed(MSG_DISMISS_DIALOG, TIME_OUT);
                //++++====++++
            }
        }
    }

    //++++====++++
    @Override
    protected void onStart() {
        super.onStart();

        try {
            if (AppConstantsFlags.SHOW_SCROLL) {
                readTextFile();
            }
            readExcelFile();
            // nirmal 07 july 2014 -- initialize ad and video layouts only if ad
            // is to be displayed
            // if (AppConstantsFlags.SHOW_ADVERTIZEMENT) {
            advLayout = (LinearLayout) findViewById(R.id.leftLayout);
            // kbdLayout = (RelativeLayout)
            // findViewById(R.id.keyboardlayout);
            vidLayout = (RelativeLayout) findViewById(R.id.videoLayout);
            videoPlayer = (VideoView) findViewById(R.id.videoPlayer);
            // }

        } catch (Exception e) {
            // TODO: handle exception
        }
        // con = new dbConnection();
        // TokenActivity.dbFlag = con.setConnection();
    }

    public void startTimer() {
        timer = new Timer();
        initializeTimerTask();
        timer.schedule(timerTask, 1000, 1000); //
    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        calRunningTime.add(Calendar.SECOND, 1);
                        //   Log.e("time::", "" + strTimeToRestart);

                        if ((calRunningTime.get(Calendar.HOUR_OF_DAY) == Integer.parseInt(strTimeToRestart) && calRunningTime.get(Calendar.MINUTE) == 1)) {
                            // if ((calRunningTime.get(Calendar.HOUR_OF_DAY) == Integer.parseInt(strTimeToRestart)
//                        if (calRunningTime.get(Calendar.SECOND) == 0) {
//                            count++;
//                            Toast.makeText(getApplicationContext(), "" + count, Toast.LENGTH_LONG).show();
//                        }
//
//                        if (count == 3) {
                            finishAffinity();
//                        }
                        }
                    }
                });

            }
        };
    }

    public void logMessage(String msg) {
        try {
            writer = new FileWriter(logFile1, true);
            writer.append(msg + "\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @SuppressLint("RestrictedApi")
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //super.onCreateOptionsMenu(menu);
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.main, menu);
        getMenuInflater().inflate(R.menu.main, menu);
        if (menu instanceof MenuBuilder) {
            ((MenuBuilder) menu).setOptionalIconsVisible(true);
        }
//        if (sharePrefSettings.getBoolean(AppConstantsFlags.key_pref_show_counter, false)) {
//            menu.getItem(3).setTitle("Hide Counter");
//        } else {
//            menu.getItem(3).setTitle("Show Counter");
//        }
        return true;

    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            DialogFragment newFragment = DbConfigurationDialog.newInstance();
            newFragment.show(getFragmentManager(), "dialog");
            newFragment.setCancelable(false);
            return true;
        }

        return false;
    }

//    public static void logMessage(String msg) {
//        try {
//            writer = new FileWriter(logFile1, true);
//            writer.append(msg + "\n");
//            writer.flush();
//            writer.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    // create new directory if not exist.
    private void createDirIfNotExists() {
        if (isExternalStorageAvailable()) {
            File arFemaleVoiceDir = new File(
                    Environment.getExternalStorageDirectory()
                            + "/Queue_Voice Files/Arabic Voice/Female Voice");
            File arMaleVoiceDir = new File(
                    Environment.getExternalStorageDirectory()
                            + "/Queue_Voice Files/Arabic Voice/Male Voice");
            File enFemaleVoiceDir = new File(
                    Environment.getExternalStorageDirectory()
                            + "/Queue_Voice Files/English Voice/Female Voice");
            File enMaleVoiceDir = new File(
                    Environment.getExternalStorageDirectory()
                            + "/Queue_Voice Files/English Voice/Male Voice");
            File fileImageDir = new File(
                    Environment.getExternalStorageDirectory()
                            + "/Queue_Config Files/Advertise images");
            File fileTokenDir = new File(
                    Environment.getExternalStorageDirectory()
                            + "/Queue_Config Files/Token images");
            File fileVideoDir = new File(
                    Environment.getExternalStorageDirectory()
                            + "/Queue_Config Files/Advertise videos");
            File setDigitfile = new File(
                    Environment.getExternalStorageDirectory()
                            + "/Queue_Config Files/SetDigit.xls");
            File englishTextfile = new File(
                    Environment.getExternalStorageDirectory()
                            + "/Queue_Config Files/Scroll_text.txt");
            File arabicTextfile = new File(
                    Environment.getExternalStorageDirectory()
                            + "/Queue_Config Files/Arabic Scroll.txt");
            try {
                if (!arFemaleVoiceDir.exists()) {
                    if (!arFemaleVoiceDir.mkdirs()) { // directory is created;
                        Log.e("TravellerLog :: ",
                                "Problem creating Arabic female folder");
                    }
                }
                if (!arMaleVoiceDir.exists()) {
                    if (!arMaleVoiceDir.mkdirs()) { // directory is created;
                        Log.e("TravellerLog :: ",
                                "Problem creating Arabic male folder");
                    }
                }
                if (!enFemaleVoiceDir.exists()) {
                    if (!enFemaleVoiceDir.mkdirs()) { // directory is created;
                        Log.e("TravellerLog :: ",
                                "Problem creating English female folder");
                    }
                }
                if (!enMaleVoiceDir.exists()) {
                    if (!enMaleVoiceDir.mkdirs()) { // directory is created;
                        Log.e("TravellerLog :: ",
                                "Problem creating English male folder");
                    }
                }
                if (!fileImageDir.exists()) {
                    fileImageDir.mkdirs();
                }
                if (!fileVideoDir.exists()) {
                    fileVideoDir.mkdirs();
                }
                if (!englishTextfile.exists()) {
                    FileWriter writer = new FileWriter(englishTextfile);
                    writer.append("Put your scrolling text in ScrollText.txt file in sdcard. Have a great day!!!");
                    writer.flush();
                    writer.close();
                    Toast.makeText(this, "ScrollText file created.",
                            LENGTH_SHORT).show();
                }
                if (!arabicTextfile.exists()) {
                    FileWriter writer = new FileWriter(arabicTextfile);
                    writer.append("من المعروف أن النتيجة النهائية لسوء خدمة العملاء وعدم رضائهم هو فقدان الأرباح.");
                    writer.flush();
                    writer.close();
                    Toast.makeText(this, "Arabic scrolling text file created.",
                            LENGTH_SHORT).show();
                }
                if (!setDigitfile.exists()) {
                    createExcelFile(setDigitfile.getAbsolutePath());
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.e("TravellerLog :: ", "Problem creating file folder");
            }
        }
    }

    // create new Excel File if not exist
    private void createExcelFile(String directoryPath) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet firstSheet = workbook.createSheet("Sheet 0");

        // 1 Row
//        HSSFRow row0 = firstSheet.createRow(0);
//        HSSFCell cell_00 = row0.createCell(0);
//        HSSFCell cell_01 = row0.createCell(1);
//        cell_01.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC);
//        cell_00.setCellValue("Token_Digits");
//        cell_01.setCellValue(3);

        // 2 row
        HSSFRow row0 = firstSheet.createRow(0);
        HSSFCell cell_00 = row0.createCell(0);
        HSSFCell cell_01 = row0.createCell(1);
        cell_01.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC);
        cell_00.setCellValue("Arabic_Font");
        cell_01.setCellValue(1);

        // 3 row
        HSSFRow row2 = firstSheet.createRow(2);
        HSSFCell cell_20 = row2.createCell(0);
        HSSFCell cell_21 = row2.createCell(1);
        cell_21.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC);
        cell_20.setCellValue("Female_Voice");
        cell_21.setCellValue(1);

        // 4 row
        HSSFRow row4 = firstSheet.createRow(4);
        HSSFCell cell_40 = row4.createCell(0);
        HSSFCell cell_41 = row4.createCell(1);
        cell_41.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC);
        cell_40.setCellValue("English + Arabic Voice");
        cell_41.setCellValue(1);

        // 5 row
        HSSFRow row6 = firstSheet.createRow(6);
        HSSFCell cell_60 = row6.createCell(0);
        HSSFCell cell_61 = row6.createCell(1);
        HSSFCell cell_62 = row6.createCell(2);
        cell_61.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC);
        cell_60.setCellValue("Advertise Delay");
        cell_61.setCellValue(20);
        cell_62.setCellValue("sec");

        // 6 row
        HSSFRow row8 = firstSheet.createRow(8);
        HSSFCell cell_80 = row8.createCell(0);
        HSSFCell cell_81 = row8.createCell(1);
        cell_81.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC);
        cell_80.setCellValue("Token Voice");
        cell_81.setCellValue(0);

        // 7 row
        HSSFRow row10 = firstSheet.createRow(10);
        HSSFCell cell_100 = row10.createCell(0);
        HSSFCell cell_101 = row10.createCell(1);
        HSSFCell cell_102 = row10.createCell(2);
        cell_101.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC);
        cell_100.setCellValue("Video volume");
        cell_101.setCellValue(50);
        cell_102.setCellValue("0 to 100");

        // 8 row
        HSSFRow row12 = firstSheet.createRow(12);
        HSSFCell cell_120 = row12.createCell(0);
        HSSFCell cell_121 = row12.createCell(1);
        cell_121.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC);
        cell_120.setCellValue("Play Voice files");
        cell_121.setCellValue(0);

        // 9 row
        HSSFRow row14 = firstSheet.createRow(14);
        HSSFCell cell_140 = row14.createCell(0);
        HSSFCell cell_141 = row14.createCell(1);
        HSSFCell cell_142 = row14.createCell(2);
        cell_141.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC);
        cell_140.setCellValue("Window Display");
        cell_141.setCellValue(0);
        cell_142.setCellValue("0 -> Window  1 -> Keypad");

        // nirmal 01 oct 2014 -- added field for english-token font-size
        // 10th row
        HSSFRow row16 = firstSheet.createRow(16);
        HSSFCell cell_160 = row16.createCell(0);
        HSSFCell cell_161 = row16.createCell(1);
        HSSFCell cell_162 = row16.createCell(2);
        cell_161.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC);
        cell_160.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_ENG_FONT_FULL_SCREEN);
        cell_161.setCellValue(400);
        cell_162.setCellValue("Fullscreen english token");

        // nirmal 01 oct 2014 -- added field for arabic-token font-size
        // 11th row
        HSSFRow row18 = firstSheet.createRow(18);
        HSSFCell cell_180 = row18.createCell(0);
        HSSFCell cell_181 = row18.createCell(1);
        HSSFCell cell_182 = row18.createCell(2);
        cell_181.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC);
        cell_180.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_ARB_FONT_FULL_SCREEN);
        cell_181.setCellValue(650);
        cell_182.setCellValue("Fullscreen arabic token");

        // nirmal 01 oct 2014 -- added field for english-token font-size
        // (half-screen)
        // 12th row
        HSSFRow row20 = firstSheet.createRow(20);
        HSSFCell cell_200 = row20.createCell(0);
        HSSFCell cell_201 = row20.createCell(1);
        HSSFCell cell_202 = row20.createCell(2);
        cell_201.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC);
        cell_200.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_ENG_FONT_HALF_SCREEN);
        cell_201.setCellValue(150);
        cell_202.setCellValue("Half screen english token");

        // nirmal 01 oct 2014 -- added field for arabic-token font-size
        // (half-screen)
        // 13th row
        HSSFRow row22 = firstSheet.createRow(22);
        HSSFCell cell_220 = row22.createCell(0);
        HSSFCell cell_221 = row22.createCell(1);
        HSSFCell cell_222 = row22.createCell(2);
        cell_221.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC);
        cell_220.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_ARB_FONT_HALF_SCREEN);
        cell_221.setCellValue(180);
        cell_222.setCellValue("Half screen arabic token");

        // nirmal 07 oct 2014 -- added field for background-color
        HSSFRow row24 = firstSheet.createRow(24);
        HSSFCell cell_240 = row24.createCell(0);
        HSSFCell cell_241 = row24.createCell(1);
        HSSFCell cell_242 = row24.createCell(2);
        cell_240.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_BG_COLOR);
        cell_241.setCellValue("black");
        cell_242.setCellValue("#RRGGBB or leave it empty to use image");


        // nirmal 07 oct 2014 -- added field for token text color
        HSSFRow row26 = firstSheet.createRow(26);
        HSSFCell cell_260 = row26.createCell(0);
        HSSFCell cell_261 = row26.createCell(1);
        HSSFCell cell_262 = row26.createCell(2);
        cell_260.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_TOKEN_COLOR);
        cell_261.setCellValue("red");
        cell_262.setCellValue("#RRGGBB");

        // nirmal 07 oct 2014 -- added field for token text blink color
        HSSFRow row28 = firstSheet.createRow(28);
        HSSFCell cell_280 = row28.createCell(0);
        HSSFCell cell_281 = row28.createCell(1);
        HSSFCell cell_282 = row28.createCell(2);
        cell_280.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_TOKEN_BLINK_COLOR);
        cell_281.setCellValue("blue");
        cell_282.setCellValue("#RRGGBB");

        // nirmal 08 oct 2014 -- added field for token-text-view top-padding
        HSSFRow row30 = firstSheet.createRow(30);
        HSSFCell cell_300 = row30.createCell(0);
        HSSFCell cell_301 = row30.createCell(1);
        cell_300.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_TOKEN_PADDING_TOP);
        cell_301.setCellValue("60");

        // nirmal 08 oct 2014 -- added field for token-text-view bottom-padding
        HSSFRow row32 = firstSheet.createRow(32);
        HSSFCell cell_320 = row32.createCell(0);
        HSSFCell cell_321 = row32.createCell(1);
        cell_320.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_TOKEN_PADDING_BOTTOM);
        cell_321.setCellValue("0");

        HSSFRow row34 = firstSheet.createRow(34);
        HSSFCell cell_340 = row34.createCell(0);
        HSSFCell cell_341 = row34.createCell(1);
        HSSFCell cell_342 = row34.createCell(2);
        cell_340.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_AUDIO_FILE_EXTENSION);
        cell_341.setCellValue(".wav");
        cell_342.setCellValue(".wav/.mp3/etc");

        HSSFRow row36 = firstSheet.createRow(36);
        HSSFCell cell_360 = row36.createCell(0);
        HSSFCell cell_361 = row36.createCell(1);
        HSSFCell cell_362 = row36.createCell(2);
        cell_361.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC);
        cell_360.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_ENG_COUNTER_FONT_HALF_SCREEN);
        cell_361.setCellValue(150);
        cell_362.setCellValue("Half screen english counter ");

        HSSFRow row38 = firstSheet.createRow(38);
        HSSFCell cell_380 = row38.createCell(0);
        HSSFCell cell_381 = row38.createCell(1);
        HSSFCell cell_382 = row38.createCell(2);
        cell_381.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC);
        cell_380.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_ARB_COUNTER_FONT_HALF_SCREEN);
        cell_381.setCellValue(180);
        cell_382.setCellValue("Half screen arabic counter");

        HSSFRow row40 = firstSheet.createRow(40);
        HSSFCell cell_400 = row40.createCell(0);
        HSSFCell cell_401 = row40.createCell(1);
        HSSFCell cell_402 = row40.createCell(2);
        cell_400.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_COUNTER_BG_COLOR);
        cell_401.setCellValue("black");
        cell_402.setCellValue("#RRGGBB or leave it empty to use image");

        HSSFRow row42 = firstSheet.createRow(42);
        HSSFCell cell_420 = row42.createCell(0);
        HSSFCell cell_421 = row42.createCell(1);
        HSSFCell cell_422 = row42.createCell(2);
        cell_420.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_COUNTER_COLOR);
        cell_421.setCellValue("red");
        cell_422.setCellValue("#RRGGBB");

        HSSFRow row44 = firstSheet.createRow(44);
        HSSFCell cell_440 = row44.createCell(0);
        HSSFCell cell_441 = row44.createCell(1);
        cell_440.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_COUNTER_PADDING_TOP);
        cell_441.setCellValue("60");

        // nirmal 08 oct 2014 -- added field for token-text-view bottom-padding
        HSSFRow row46 = firstSheet.createRow(46);
        HSSFCell cell_460 = row46.createCell(0);
        HSSFCell cell_461 = row46.createCell(1);
        cell_460.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_COUNTER_PADDING_BOTTOM);
        cell_461.setCellValue("0");

        HSSFRow row47 = firstSheet.createRow(47);
        HSSFCell cell_470 = row47.createCell(0);
        HSSFCell cell_471 = row47.createCell(1);
        cell_470.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_SERVICE_NAME_TEXT_COLOR);
        cell_471.setCellValue("#ffffff");

        HSSFRow row48 = firstSheet.createRow(48);
        HSSFCell cell_480 = row48.createCell(0);
        HSSFCell cell_481 = row48.createCell(1);
        cell_480.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_SERVICE_NAME_FONT_SIZE);
        cell_481.setCellValue("20");

        HSSFRow row49 = firstSheet.createRow(49);
        HSSFCell cell_490 = row49.createCell(0);
        HSSFCell cell_491 = row49.createCell(1);
        cell_490.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_BRANCH_NAME_TEXT_COLOR);
        cell_491.setCellValue("green");

        HSSFRow row50 = firstSheet.createRow(50);
        HSSFCell cell_500 = row50.createCell(0);
        HSSFCell cell_501= row50.createCell(1);
        cell_500.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_BRANCH_NAME_FONT_SIZE);
        cell_501.setCellValue("20");

        HSSFRow row51 = firstSheet.createRow(51);
        HSSFCell cell_510 = row51.createCell(0);
        HSSFCell cell_511 = row51.createCell(1);
        cell_510.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_BRANCH_NAME_BG_COLOR);
        cell_511.setCellValue("#fffafa");

        HSSFRow row52 = firstSheet.createRow(52);
        HSSFCell cell_520 = row52.createCell(0);
        HSSFCell cell_521 = row52.createCell(1);
        cell_520.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_TIME_FONT_SIZE);
        cell_521.setCellValue("15");

        HSSFRow row53 = firstSheet.createRow(53);
        HSSFCell cell_530 = row53.createCell(0);
        HSSFCell cell_531 = row53.createCell(1);
        cell_530.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_TIME_TEXT_COLOR);
        cell_531.setCellValue("#BCE4F4");

        FileOutputStream fos = null;
        try {
            File file = new File(directoryPath);
            fos = new FileOutputStream(file);
            workbook.write(fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            Toast.makeText(this, "SetDigit.xls file created.",
                    LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.purge();
            timer.cancel();

        }
        if (timerTask != null) {
            timerTask.cancel();

        }
    }

    public boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    // Read Text file from sdcard
    private void readTextFile() {
        try {
            BufferedReader br;

            File englishFile = new File(sdcardPath,
                    "/Queue_Config Files/Scroll_text.txt");
            StringBuilder enText = new StringBuilder();
            // enText = new StringBuilder();
            String enLine;
            br = new BufferedReader(new FileReader(englishFile));
            while ((enLine = br.readLine()) != null) {
                enText.append(enLine);
            }
            br.close();
            // scroll english text
            // ScrollTextView scrolltext = (ScrollTextView)
            // findViewById(R.id.scrolltext);
            // scrolltext.setSelected(true);

            tv_scrollTxtEng.setText(enText);
            tv_scrollTxtEng.startScroll();

            File arabicFile = new File(sdcardPath,
                    "/Queue_Config Files/Arabic Scroll.txt");

            StringBuilder arText = new StringBuilder();
            // arText = new StringBuilder();
            String arLine;

            br = new BufferedReader(new FileReader(arabicFile));
            while ((arLine = br.readLine()) != null) {
                arText.append(arLine);
            }
            br.close();
            // scroll arabic text
            // ScrollTextViewArabic scrolltext_Ar = (ScrollTextViewArabic)
            // findViewById(R.id.scrolltext_Ar);
            // scrolltext_Ar.setSelected(true);
            // tv_scrollTxtArabic.setSelected(true);
            tv_scrollTxtArabic.setText(arText);
            tv_scrollTxtArabic.startScroll_Arabic();

        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "Scroll_text.txt file not found.",
                    LENGTH_SHORT).show();
        } catch (Exception e) {
        }

    }

    // Read excel file from sdcard.
    private void readExcelFile() {
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.e(null, "Storage not available or read only");
            return;
        }
        Workbook w;
        try {
            File inputWorkbook = new File(sdcardPath
                    + "/Queue_Config Files/SetDigit.xls");
            w = Workbook.getWorkbook(inputWorkbook);
            // Get the first sheet
            Sheet sheet = w.getSheet(0);
            // Loop over first 10 column and lines
            int rows = sheet.getRows();
            int i = 0;
            for (int j = 0; j < rows; j++) {
                Cell cell1 = sheet.getCell(i, j);
                Cell cell2 = sheet.getCell(i + 1, j);
                String key = cell1.getContents();
                String value = cell2.getContents();
                digitMap.put(key, value);
                i = 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), "SetDigit.xls file not found.",
                    LENGTH_SHORT).show();
        } catch (BiffException e) {
            Toast.makeText(getBaseContext(), "SetDigit.xls file not found.",
                    LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "SetDigit.xls file not found.",
                    LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // FragmentManager frgMgr = getFragmentManager();
        // TokenActivity tknFrg = (TokenActivity) frgMgr
        // .findFragmentById(R.id.fragment_token);
        try {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            if (AppConstantsFlags.SHOW_ADVERTIZEMENT != "") {
                if (AppConstantsFlags.SHOW_ADVERTIZEMENT.equals("Show Advertisement")) {
                    videoPlayer.stopPlayback();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // tknFrg.mPlayer.stop();
        // con.closeConnection();
    }

    // Back button pressed.
    @Override
    public void onBackPressed() {
        // nirmal 07 july 2014 -- check whether advertize is present on the
        // screen
        try {
            FragmentManager frgMgr = getFragmentManager();
            TokenActivity tknFrg = (TokenActivity) frgMgr
                    .findFragmentById(R.id.fragment_token);
            if (AppConstantsFlags.SHOW_ADVERTIZEMENT != "") {
                if (TokenActivity.WINDOW_DISPLAY != 0
                        && AppConstantsFlags.SHOW_ADVERTIZEMENT.equals("Show Advertisement")) {
                    videoPlayer.stopPlayback();
                    tknFrg.mPlayer.stop();
                    this.finish();
                } else {
                    this.finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean checkPermissions() {
        int permission1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE);
        int permission3 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);
        int permission4 = ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permission1 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permission2 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_WIFI_STATE);
        }
        if (permission3 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_NETWORK_STATE);
        }
        if (permission4 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CHANGE_WIFI_STATE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //  Toast.makeText(this, "request granted::", LENGTH_SHORT).show();
//                File logFile = new File(Environment.getExternalStorageDirectory()
//                        + "/QueueWindowDisplay");
//                if (!logFile.exists()) {
//                    logFile.mkdir();
//                }
//                logFile1 = new File(logFile, "WindowDisplayLog.txt");

            }
            return;
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

    }

    private void resetLastRunDateUpdateTimer() {
        lastRunDateUpdateTimer.cancel();
        lastRunDateUpdateTimer.start();
    }

    private void showLicenseDialog() {
        LicenseDialog licenseDialog = new LicenseDialog();
        licenseDialog.setCancelable(false);
        // putPref11("dialog", "0", this);
        // licenseDialog.setStyle(DialogFragment.STYLE_NO_FRAME,
        // R.style.LicenseDialogStyle);
        licenseDialog.show(getFragmentManager(), "LicenseDialog");
    }

    private String getMACAddress() {
        try {
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wInfo = wifiManager.getConnectionInfo();
            String macAddress = wInfo.getMacAddress();
            return macAddress == null ? "" : macAddress;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return "";
        }
    }

    private boolean isLicenseValid(boolean showToastMsg) {

        try {

            String deviceId = Secure.getString(this.getContentResolver(),
                    Secure.ANDROID_ID);
            deviceId += getMACAddress();
            // final byte[] appSalt = { 53, 95, 81, 43, 97, 51, 31, 17 };
            // final byte[] fullTypeSalt = { 74, 8, 69, 34, 40, 15 };
            // final byte[] trialTypeSalt = { 63, 59, 54, 37, 19, 81 };
            // // String strId = deviceId + new String(appSalt);
            final byte[] appSalt = {34, 93, 43, 56, 40, 23, 99, 14};
            final byte[] fullTypeSalt = {74, 8, 69, 34, 40, 15};
            final byte[] trialTypeSalt = {63, 59, 54, 37, 19, 81};

            LicenseDataHolder license;
            String storedLicense = "";
            license = mDataLayer.getLicenseKey();
            storedLicense = license.getLicenseKey();

            //sarvajeet 11-5
            String TrialDays = license.getTrialDays();
            Log.d("Trial Days............", TrialDays);
            Calendar calLicense = new GregorianCalendar();
            calLicense = license.getStartDate();
            /*
             * nirmal 03 march 2015 -- used formatting for the day_of_month part
             * also, because the server sends the date in such a format
             *
             * String strLicenseStartDate =
             * calLicense.get(Calendar.DAY_OF_MONTH) + "-" +
             * String.format(Locale.US, "%02d", 1 +
             * calLicense.get(Calendar.MONTH)) + "-" +
             * calLicense.get(Calendar.YEAR);
             */
            String strLicenseStartDate = String.format(Locale.US, "%02d",
                    calLicense.get(Calendar.DAY_OF_MONTH))
                    + "-"
                    + String.format(Locale.US, "%02d",
                    1 + calLicense.get(Calendar.MONTH))
                    + "-"
                    + calLicense.get(Calendar.YEAR);
            calLicense = truncateTimePart(calLicense);
            Calendar calLastRunDate = new GregorianCalendar();
            calLastRunDate = license.getLastRunDate();
            // calLastRunDate = truncateTimePart(calLastRunDate);
            /*
             * Log.d(LicenseDialog.LOG_TAG, "start date =" +
             * String.format(Locale.US, "%02d",
             * calLicense.get(Calendar.DAY_OF_MONTH)) + "-" +
             * String.format(Locale.US, "%02d", 1 +
             * calLicense.get(Calendar.MONTH)) + "-" +
             * calLicense.get(Calendar.YEAR));
             */
//			Log.d(LOG_TAG,
//					"last run date ="
//							+ String.format(Locale.US, "%02d",
//									calLastRunDate.get(Calendar.DAY_OF_MONTH))
//							+ "-"
//							+ String.format(Locale.US, "%02d",
//									1 + calLastRunDate.get(Calendar.MONTH))
//							+ "-" + calLastRunDate.get(Calendar.YEAR));
            storedLicense = storedLicense == null ? "" : storedLicense;

            // Log.d(LicenseDialog.LOG_TAG, "start date = " +
            // strLicenseStartDate);
            /*
             * Log.d(LicenseDialog.LOG_TAG, "stored license = " +
             * storedLicense); Log.d(LicenseDialog.LOG_TAG,
             * "generated license = " + getHash(new String(trialTypeSalt,
             * "UTF-8") + deviceId + new String(appSalt, "UTF-8") +
             * strLicenseStartDate));
             */
            // Log.d(LicenseDialog.LOG_TAG, "generated license "
            // + getHash(new String(trialTypeSalt, "UTF-8") + deviceId
            // + new String(appSalt, "UTF-8")
            // + strLicenseStartDate));

            //Trial Extension- sarvajeet 11-5
            Log.d("License 1", getHash(
                    new String(trialTypeSalt, "UTF-8") + deviceId
                            + new String(appSalt, "UTF-8")
                            + strLicenseStartDate
                            + TrialDays));
            Log.d("Lice 2", storedLicense);


            if (getHash(
                    new String(fullTypeSalt, "UTF-8") + deviceId
                            + new String(appSalt, "UTF-8")
                            + strLicenseStartDate
                            + "000").equals(storedLicense)) {
                if (showToastMsg) {
                    Toast.makeText(getApplicationContext(), "Full License",
                            LENGTH_SHORT).show();
                }
                ISTRIAL = false;
                return true;
            } else if (getHash(
                    new String(trialTypeSalt, "UTF-8") + deviceId
                            + new String(appSalt, "UTF-8")
                            + strLicenseStartDate
                            + TrialDays).equals(storedLicense)) {
                if (showToastMsg) {
                    Toast.makeText(getApplicationContext(), "Trial License",
                            LENGTH_SHORT).show();
                }
                /*
                 * Log.d(LicenseDialog.LOG_TAG, "generated license " +
                 * getHash(new String(trialTypeSalt, "UTF-8") + deviceId + new
                 * String(appSalt, "UTF-8") + strLicenseStartDate));
                 */
                Calendar calNow = new GregorianCalendar(Locale.US);
                // calNow = truncateTimePart(calNow);
                // calNow.set(Calendar.DAY_OF_MONTH, 19);
                // calNow.set(Calendar.MONTH, Calendar.FEBRUARY);
                /* Calendar calLicense = license.getInstallDate(); */
                // calLicense.add(Calendar.MONTH, 1);
                Calendar calLicenseExpiry = new GregorianCalendar();
                calLicenseExpiry = (GregorianCalendar) calLicense.clone();
                // Log.d("qs",
                // "cal license = "
                // + calLicense.getTimeInMillis()
                // + " "
                // + calLicense.get(Calendar.DAY_OF_MONTH)
                // + "-"
                // + calLicense.getDisplayName(Calendar.MONTH,
                // Calendar.SHORT, Locale.US) + "-"
                // + calLicense.get(Calendar.YEAR));
                // Log.d("qs", "adding one month to start-date");

                /*
                 * nirmal 05 march 2015 -- add 30 days instead of 1 month
                 * calLicenseExpiry.add(Calendar.MONTH, 1);
                 */
                //Sarvajeet 11-5-15 Added no of days got from protocol for expiry
                calLicenseExpiry.add(Calendar.DATE, Integer.parseInt(TrialDays));
                calLicenseExpiry = truncateTimePart(calLicenseExpiry);

                // Log.d("qs",
                // "cal now = "
                // + calNow.getTimeInMillis()
                // + " "
                // + calNow.get(Calendar.DAY_OF_MONTH)
                // + "-"
                // + calNow.getDisplayName(Calendar.MONTH,
                // Calendar.SHORT, Locale.US) + "-"
                // + calNow.get(Calendar.YEAR));
                // Log.d("qs",
                // "cal license = "
                // + calLicense.getTimeInMillis()
                // + " "
                // + calLicense.get(Calendar.DAY_OF_MONTH)
                // + "-"
                // + calLicense.getDisplayName(Calendar.MONTH,
                // Calendar.SHORT, Locale.US) + "-"
                // + calLicense.get(Calendar.YEAR));
                // Log.d("qs",
                // "cal license expiry = "
                // + calLicenseExpiry.getTimeInMillis()
                // + " "
                // + calLicenseExpiry.get(Calendar.DAY_OF_MONTH)
                // + "-"
                // + calLicenseExpiry.getDisplayName(Calendar.MONTH,
                // Calendar.SHORT, Locale.US) + "-"
                // + calLicenseExpiry.get(Calendar.YEAR));

                if ((calNow.after(calLicense) || calNow.equals(calLicense))
                        && calNow.before(calLicenseExpiry)) {
                    // if current date is between license install date and
                    // expiry
                    // date, and current date is greater than or equal to
                    // last-run-date, then return true
                    // if (calLicense.compareTo(calNow) > -1) {
                    if (calNow.after(calLastRunDate)) {
                        ISTRIAL = true;
                        if (showToastMsg) {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Trial license expires on :"
                                            + String.format(
                                            Locale.US,
                                            "%02d",
                                            calLicenseExpiry
                                                    .get(Calendar.DAY_OF_MONTH))
                                            + "-"
                                            + calLicenseExpiry.getDisplayName(
                                            Calendar.MONTH,
                                            Calendar.SHORT, Locale.US)
                                            + "-"
                                            + calLicenseExpiry
                                            .get(Calendar.YEAR),
                                    LENGTH_SHORT).show();
                        }
                        return true;
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Detected system-date backward-change !!!",
                                LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                                    "Trial License expired !!!", LENGTH_SHORT)
                            .show();
                    //putPref11("dialog", null, this);
                    return false;
                }
            } else {
                Toast.makeText(getApplicationContext(), "Invalid License !!!",
                        LENGTH_SHORT).show();
                return false;
            }
        } catch (UnsupportedEncodingException e) {
            // TODO: handle exception
//			Log.d(LOG_TAG, "encoding not supported");
        } catch (Exception e) {
            // TODO: handle exception
        }
        return false;

    }

    private Calendar truncateTimePart(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (GregorianCalendar) cal.clone();
    }

    private String getHash(String str) {
        String strHash = "";
        try {
            MessageDigest msgDigest = MessageDigest.getInstance("SHA-256");
            // msgDigest.update(str.getBytes());
            msgDigest.update(str.getBytes("UTF-8"));
            byte[] hashBytes = msgDigest.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hashBytes.length; i++) {
                String hex = Integer.toHexString(0xff & hashBytes[i]);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            strHash = hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            strHash = "";
        } catch (UnsupportedEncodingException e) {
            strHash = "";
        } catch (Exception e) {
            strHash = "";
        }
        return strHash;
    }

    @Override
    public void onLicenseDialogFinished(LicenseDataHolder license) {
        mDataLayer.saveLicenseKey(license);
        if (!isLicenseValid(true)) {
            showLicenseDialog();
        }
    }
}
