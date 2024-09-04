package com.orbits.queuingsystem

import NetworkMonitor
import android.Manifest
import android.app.AlertDialog
import android.app.DialogFragment
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.provider.Settings.Secure
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.orbits.queuingsystem.LicenseDialog.OnLicenseDialogFinishListener
import com.orbits.queuingsystem.helper.Dialogs
import com.orbits.queuingsystem.helper.MainViewModel
import com.orbits.queuingsystem.helper.NetworkChecker
import com.orbits.queuingsystem.helper.NetworkListener
import com.orbits.queuingsystem.helper.interfaces.AlertDialogInterface
import jxl.Workbook
import jxl.read.biff.BiffException
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Cell
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Locale
import java.util.Timer
import java.util.TimerTask

class MainActivity : AppCompatActivity(), OnLicenseDialogFinishListener, NetworkListener {
    var count: Int = 0

    // public CustomKeyboard mCustomKeyboard;
    var advLayout: LinearLayout? = null

    // public RelativeLayout kbdLayout;
    var vidLayout: RelativeLayout? = null
    var videoPlayer: VideoView? = null
    private var isContinued = false
    private lateinit var networkMonitor: NetworkMonitor
    private val networkChecker = NetworkChecker(this)

    // private int mTokenColor, mTokenBlinkColor;
    // StringBuilder enText,arText;
    var message: String? = null
    private var isAppStarted = false


    // public static dbConnection con;
    var ll_mainLayout: LinearLayout? = null
    var ll_tokenLayout: LinearLayout? = null
    var ll_logo_adv: LinearLayout? = null
    var ll_right: LinearLayout? = null
    var ll_top_scroll: LinearLayout? = null
    var ll_bottom_scroll: LinearLayout? = null
    var tv_scrollTxtArabic: ScrollTextViewArabic? = null
    var tv_scrollTxtEng: ScrollTextView? = null
    var timerTask: TimerTask? = null
    var timer: Timer? = null
    var strTimeToRestart: String? = null
    var calRunningTime: Calendar? = null
    private val COUNTER_NO = 0
    private val mConnectionCheckCounter: Byte = 0
    private var mDataLayer: DataLayer? = null
    private var ISTRIAL = true
    private var mAlertDialog: AlertDialog? = null
    var logFile1: File? = null
    var writer: FileWriter? = null

    //static FileWriter writer;
    // static File logFile1;
    //Shums 18/09/2018
    //++++====++++
    private val mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_DISMISS_DIALOG -> if (mAlertDialog != null && mAlertDialog!!.isShowing) {
                    mAlertDialog!!.dismiss()
                }

                else -> {}
            }
        }
    }
    private val lastRunDateUpdateTimer: CountDownTimer = object : CountDownTimer(
        60000,
        10000
    ) {
        override fun onTick(millisUntilFinished: Long) {
            // TODO Auto-generated method stub
        }

        override fun onFinish() {
            // TODO Auto-generated method stub
            mDataLayer!!.updateLastRunDate()
            resetLastRunDateUpdateTimer()
            if (!isLicenseValid(false)) {
                showLicenseDialog()
            }
        }
    }

    //++++====++++
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        networkChecker.setNetworkListener(this)


        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        requestWindowFeature(Window.FEATURE_ACTION_BAR)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        calRunningTime = Calendar.getInstance()
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermissions()) {
            } else {
                //Toast.makeText(this, "already granted::", LENGTH_SHORT).show();
            }
        }

        val logFile = File(
            Environment.getExternalStorageDirectory()
                .toString() + "/QueueWindowDisplay"
        )
        if (!logFile.exists()) {
            logFile.mkdir()
        }
        logFile1 = File(logFile, "WindowDisplayLog.txt")

        mDataLayer = DataLayer(applicationContext)
        // nirmal 07 july 2014 -- load layout file according to preferences
        sharePrefSettings = getSharedPreferences("QueuingSystemPreff", 0)

        AppConstantsFlags.SHOW_LOGO =
            sharePrefSettings?.getString(AppConstantsFlags.key_pref_show_logo, "").toBoolean()
        AppConstantsFlags.SHOW_SCROLL =
            sharePrefSettings?.getString(AppConstantsFlags.key_pref_show_scroll, "").toBoolean()
        AppConstantsFlags.SHOW_SERVICE_NAME =
            sharePrefSettings?.getString(AppConstantsFlags.key_pref_service_name, "").toBoolean()
        AppConstantsFlags.SHOW_BRANCH_NAME =
            sharePrefSettings?.getString(AppConstantsFlags.key_pref_branch_name, "").toBoolean()
        AppConstantsFlags.SHOW_USER_NAME =
            sharePrefSettings?.getString(AppConstantsFlags.key_pref_user_name, "").toBoolean()
        AppConstantsFlags.SHOW_SERVICE_NAME_TOP =
            sharePrefSettings?.getString(AppConstantsFlags.key_pref_service_top, "").toBoolean()
        AppConstantsFlags.SHOW_BRANCH_NAME_TOP =
            sharePrefSettings?.getString(AppConstantsFlags.key_pref_branch_top, "").toBoolean()
        AppConstantsFlags.SHOW_NONE_TOP =
            sharePrefSettings?.getString(AppConstantsFlags.key_pref_none_top, "").toBoolean()
        AppConstantsFlags.SHOW_NONE =
            sharePrefSettings?.getString(AppConstantsFlags.key_pref_none, "").toBoolean()
        AppConstantsFlags.SHOW_USER_NAME_TOP =
            sharePrefSettings?.getString(AppConstantsFlags.key_pref_user_top, "").toBoolean()
        AppConstantsFlags.SHOW_TIME =
            sharePrefSettings?.getString(AppConstantsFlags.key_pref_show_time, "").toBoolean()
        if (AppConstantsFlags.SHOW_ADVERTIZEMENT !== "") {
            println("here is condition 11111111")
            AppConstantsFlags.SHOW_ADVERTIZEMENT =
                sharePrefSettings?.getString("show_ad_counter", "")!!
        } else if (AppConstantsFlags.SHOW_SCROLL && AppConstantsFlags.SHOW_LOGO) {
            println("here is condition 222222222")
            setContentView(R.layout.activity_main_ad_without_logo_scroll)
        } else {
            println("here is condition 3333333")
            AppConstantsFlags.SHOW_ADVERTIZEMENT =
                sharePrefSettings?.getString("show_ad_counter", "Show Advertisement")!!
        }

        if ((AppConstantsFlags.SHOW_ADVERTIZEMENT == "Show Nothing")
            && !AppConstantsFlags.SHOW_SCROLL
        ) {
            println("here is condition 4444444")
            setContentView(R.layout.activity_main_full_screen)
        } else if (AppConstantsFlags.SHOW_ADVERTIZEMENT != "Show Nothing"
            && AppConstantsFlags.SHOW_LOGO && AppConstantsFlags.SHOW_BRANCH_NAME && AppConstantsFlags.SHOW_SERVICE_NAME && AppConstantsFlags.SHOW_USER_NAME
        ) {
            println("here is condition 555555555")
            setContentView(R.layout.layout_main_service_branch_name)
        } else if (AppConstantsFlags.SHOW_ADVERTIZEMENT != "Show Nothing"
            && AppConstantsFlags.SHOW_LOGO
            && !AppConstantsFlags.SHOW_SCROLL
        ) {
            println("here is condition 66666666666")
            setContentView(R.layout.activity_main_ad_without_scroll)
        } else if ((AppConstantsFlags.SHOW_ADVERTIZEMENT == "Show Nothing") && (AppConstantsFlags.SHOW_SCROLL)) {
            println("here is condition 7777777777")
            setContentView(R.layout.activity_main_scroll_without_ad)
        } else if (AppConstantsFlags.SHOW_ADVERTIZEMENT != "Show Nothing"
            && AppConstantsFlags.SHOW_SCROLL
            && !AppConstantsFlags.SHOW_LOGO
        ) {
            println("here is condition 8888888")
            setContentView(R.layout.activity_main_ad_scroll_without_logo)
        } else if (AppConstantsFlags.SHOW_ADVERTIZEMENT != "Show Nothing"
            && !AppConstantsFlags.SHOW_SCROLL
            && !AppConstantsFlags.SHOW_LOGO
        ) {
            println("here is condition 999999999")
            setContentView(R.layout.activity_main_ad_without_logo_scroll)
        } else {
            println("here is condition 000000000")
            setContentView(R.layout.activity_main)
        }
        val restart =
            sharePrefSettings?.getString(AppConstantsFlags.key_pref_restart, "").toBoolean()
        strTimeToRestart =
            sharePrefSettings?.getString(AppConstantsFlags.key_pref_time_to_restart, "")
        if (restart) {
            startTimer()
        } else {
            if (timer != null) {
                timer!!.purge()
                timer!!.cancel()
            }

            if (timerTask != null) {
                timerTask!!.cancel()
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
        val actionBar = actionBar
        actionBar?.hide()

        // nirmal 18 june 2014 -- use global strings to store english and arabic
        // scroll-texts
        // enText=new StringBuilder();
        // arText=new StringBuilder();

        // nirmal 07 july 2014
        // sharePrefSettings = getSharedPreferences("QueuingSystemPreff", 0);
        serverName = sharePrefSettings?.getString("Server_name", "")

        val port = sharePrefSettings?.getString("QueueTCP_port", "")

        networkMonitor = NetworkMonitor(this) {
            if (serverName?.isNotEmpty() == true){
                viewModel.connectWebSocket(serverName ?: "",port ?: "")
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            networkMonitor.registerNetworkCallback()
        }


        // tcp_ip_port = sharePrefSettings?.getString("TCPIP_port", "");

        /*
         * // nirmal 17 june 2014 -- set the flags, whether to display ads and
         * // scroll-text or not App_Constants_Flags.SHOW_ADVERTIZEMENT =
         * sharePrefSettings?.getBoolean( App_Constants_Flags.key_pref_show_ad,
         * true); App_Constants_Flags.SHOW_TOP_SCROLL =
         * sharePrefSettings?.getBoolean(
         * App_Constants_Flags.key_pref_show_top_scroll, true);
         * App_Constants_Flags.SHOW_BOTTOM_SCROLL =
         * sharePrefSettings?.getBoolean(
         * App_Constants_Flags.key_pref_show_bottom_scroll, true);
         */
        // TokenActivity.mTokenColor = Color.RED;
        // TokenActivity.mTokenBlinkColor = Color.BLUE;
        val wifiManager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager

        if (!wifiManager.isWifiEnabled) {
            wifiManager.setWifiEnabled(true)
        }

        /*
         * nirmal 29 oct 2014 -- no need for disabling strict-mode because
         * network operation is now performed by async-task.
         *
         * if (android.os.Build.VERSION.SDK_INT > 9) { StrictMode.ThreadPolicy
         * policy = new StrictMode.ThreadPolicy.Builder() .permitAll().build();
         * StrictMode.setThreadPolicy(policy); }
         */
        createDirIfNotExists()
        checkLicense()
        // nirmal 17 june 2014 -- initialize the reference to
        // top-parent-linear-layout of main-activity
        ll_mainLayout = findViewById<View>(R.id.ll_main_activity_top_parent) as LinearLayout
        ll_right = findViewById<View>(R.id.rightLayout) as LinearLayout?
        ll_tokenLayout = findViewById<View>(R.id.ll_fragment_token_container) as LinearLayout

        // nirmal 07 july 2014 -- initialize references only if those components
        // are loaded
        if (AppConstantsFlags.SHOW_SCROLL) {
            tv_scrollTxtArabic = findViewById<View>(R.id.scrolltext_Ar) as ScrollTextViewArabic
            tv_scrollTxtEng = findViewById<View>(R.id.scrolltext) as ScrollTextView
            ll_top_scroll = findViewById<View>(R.id.ll_main_top_scroll) as LinearLayout
            ll_bottom_scroll = findViewById<View>(R.id.ll_main_bottom_scroll) as LinearLayout
        }
        if (AppConstantsFlags.SHOW_ADVERTIZEMENT == "Show Advertisement") {
            ll_logo_adv = findViewById<View>(R.id.ll_container_logo_ads) as LinearLayout
        }

        ll_mainLayout?.setOnLongClickListener { v ->
            val newFragment: DialogFragment = DbConfigurationDialog.newInstance()
            newFragment.show(fragmentManager, "dialog")
            newFragment.isCancelable = false
            true
        }
    }

    private fun showCounterDialog(){
        if (!serverName.isNullOrEmpty()){
            Dialogs.showSelectCounterDialog(
                activity = this@MainActivity,
                dataList = viewModel.dataList,
                alertDialogInterface = object : AlertDialogInterface {
                    override fun onCounterSelected(
                        counterId: String,
                        counterType: String,
                        serviceId: String
                    ) {
                        mainCounter = counterId
                        viewModel.sendConnectionMessage(
                            counterType,
                            counterId,
                            serviceId
                        )

                       sharePrefSettings?.edit()?.putString("counterId", mainCounter)?.commit()
                       sharePrefSettings?.edit()?.putString("counterType", counterType)?.commit()
                       sharePrefSettings?.edit()?.putString("serviceId", serviceId)?.commit()
                        isAppStarted = true
                        networkChecker.start()



                        /*handler(1000){
                            val model = viewModel.dataModel?.getAsJsonObject("transaction")
                            viewModel.sendNewMessage(
                                transaction = TransactionDataModel(
                                    id = model?.get("id")?.asString ?: "",
                                    counterId = model?.get("counterId")?.asString ?: "",
                                    serviceId = model?.get("serviceId")?.asString ?: "",
                                    entityID = model?.get("entityID")?.asString ?: "",
                                    serviceType = model?.get("serviceAssign")?.asString ?: "",
                                    token = model?.get("token")?.asString ?: "",
                                    ticketToken = null,
                                    keypadToken = model?.get("keypadToken")?.asString ?: "",
                                    issueTime = model?.get("issueTime")?.asString ?: "",
                                    status = "4" // Completed status
                                ),
                                counterId = mainCounter
                            )
                        }*/
                    }
                }

            )
        }
    }

    fun checkLicense() {
        mDataLayer!!.updateLastRunDate()
        if (!isLicenseValid(true)) {
            // if (getPref11("dialog", this) == null) {
            showLicenseDialog()
            // }
        } else {
            // mDataLayer.updateLastRunDate();
            lastRunDateUpdateTimer.start()
            if (ISTRIAL) {
                val alertBuilder = AlertDialog.Builder(
                    this@MainActivity
                )
                alertBuilder
                    .setMessage("Do you wish to purchase full license?")
                alertBuilder.setPositiveButton(
                    "Purchase full-license"
                ) { dialog, which -> // TODO Auto-generated method stub
                    showLicenseDialog()
                }
                alertBuilder.setNegativeButton(
                    "Continue trial"
                ) { dialog, which -> // TODO Auto-generated method stub
                    dialog.dismiss()
                    /*if (!sharePrefSettings?.getString("counterId", "")?.isNullOrEmpty()!!){
                    handler(400){
                        viewModel.sendConnectionMessage(
                            sharePrefSettings?.getString("counterType", "") ?: "",
                            sharePrefSettings?.getString("counterId", "") ?: "",
                            sharePrefSettings?.getString("serviceId", "") ?: ""
                        )
                    }
                        networkChecker.start()
                }else {
                        showCounterDialog()
                        isContinued = true
                    }*/
                }
                alertBuilder.setCancelable(false)

                mAlertDialog = alertBuilder.create()
                mAlertDialog?.show()
                mHandler.sendEmptyMessageDelayed(MSG_DISMISS_DIALOG, TIME_OUT.toLong())
                handler(TIME_OUT.toLong()){
                    if (!isContinued){
                        if (!sharePrefSettings?.getString("counterId", "")?.isNullOrEmpty()!!){
                            handler(400){
                                viewModel.sendConnectionMessage(
                                    sharePrefSettings?.getString("counterType", "") ?: "",
                                    sharePrefSettings?.getString("counterId", "") ?: "",
                                    sharePrefSettings?.getString("serviceId", "") ?: ""
                                )
                            }
                            isAppStarted = true
                            networkChecker.start()
                        }else {
                            showCounterDialog()
                        }

                    }

                }
            }
        }
    }

    //++++====++++
    override fun onStart() {
        super.onStart()

        try {
            if (AppConstantsFlags.SHOW_SCROLL) {
                readTextFile()
            }
            readExcelFile()

            advLayout = findViewById<View>(R.id.leftLayout) as LinearLayout

            vidLayout = findViewById<View>(R.id.videoLayout) as RelativeLayout
            videoPlayer = findViewById<View>(R.id.videoPlayer) as VideoView

        } catch (e: Exception) {
        }
    }

    fun startTimer() {
        timer = Timer()
        initializeTimerTask()
        timer!!.schedule(timerTask, 1000, 1000) //
    }

    fun initializeTimerTask() {
        timerTask = object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    calRunningTime!!.add(Calendar.SECOND, 1)
                    if ((calRunningTime!![Calendar.HOUR_OF_DAY] == strTimeToRestart!!.toInt() && calRunningTime!![Calendar.MINUTE] == 1)) {
                        finishAffinity()
                    }
                }
            }
        }
    }

    fun logMessage(msg: String) {
        try {
            writer = FileWriter(logFile1, true)
            writer!!.append(msg + "\n")
            writer!!.flush()
            writer!!.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    // create new directory if not exist.
    private fun createDirIfNotExists() {
        if (isExternalStorageAvailable) {
            val arFemaleVoiceDir = File(
                Environment.getExternalStorageDirectory()
                    .toString() + "/Queue_Voice Files/Arabic Voice/Female Voice"
            )
            val arMaleVoiceDir = File(
                Environment.getExternalStorageDirectory()
                    .toString() + "/Queue_Voice Files/Arabic Voice/Male Voice"
            )
            val enFemaleVoiceDir = File(
                Environment.getExternalStorageDirectory()
                    .toString() + "/Queue_Voice Files/English Voice/Female Voice"
            )
            val enMaleVoiceDir = File(
                Environment.getExternalStorageDirectory()
                    .toString() + "/Queue_Voice Files/English Voice/Male Voice"
            )
            val fileImageDir = File(
                Environment.getExternalStorageDirectory()
                    .toString() + "/Queue_Config Files/Advertise images"
            )
            val fileTokenDir = File(
                Environment.getExternalStorageDirectory()
                    .toString() + "/Queue_Config Files/Token images"
            )
            val fileVideoDir = File(
                Environment.getExternalStorageDirectory()
                    .toString() + "/Queue_Config Files/Advertise videos"
            )
            val setDigitfile = File(
                Environment.getExternalStorageDirectory()
                    .toString() + "/Queue_Config Files/SetDigit.xls"
            )
            val englishTextfile = File(
                Environment.getExternalStorageDirectory()
                    .toString() + "/Queue_Config Files/Scroll_text.txt"
            )
            val arabicTextfile = File(
                Environment.getExternalStorageDirectory()
                    .toString() + "/Queue_Config Files/Arabic Scroll.txt"
            )
            try {
                if (!arFemaleVoiceDir.exists()) {
                    if (!arFemaleVoiceDir.mkdirs()) { // directory is created;
                        Log.e(
                            "TravellerLog :: ",
                            "Problem creating Arabic female folder"
                        )
                    }
                }
                if (!arMaleVoiceDir.exists()) {
                    if (!arMaleVoiceDir.mkdirs()) { // directory is created;
                        Log.e(
                            "TravellerLog :: ",
                            "Problem creating Arabic male folder"
                        )
                    }
                }
                if (!enFemaleVoiceDir.exists()) {
                    if (!enFemaleVoiceDir.mkdirs()) { // directory is created;
                        Log.e(
                            "TravellerLog :: ",
                            "Problem creating English female folder"
                        )
                    }
                }
                if (!enMaleVoiceDir.exists()) {
                    if (!enMaleVoiceDir.mkdirs()) { // directory is created;
                        Log.e(
                            "TravellerLog :: ",
                            "Problem creating English male folder"
                        )
                    }
                }
                if (!fileImageDir.exists()) {
                    fileImageDir.mkdirs()
                }
                if (!fileTokenDir.exists()) {
                    fileTokenDir.mkdirs()
                }
                if (!fileVideoDir.exists()) {
                    fileVideoDir.mkdirs()
                }
                if (!englishTextfile.exists()) {
                    val writer = FileWriter(englishTextfile)
                    writer.append("Put your scrolling text in ScrollText.txt file in sdcard. Have a great day!!!")
                    writer.flush()
                    writer.close()
                    Toast.makeText(
                        this, "ScrollText file created.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if (!arabicTextfile.exists()) {
                    val writer = FileWriter(arabicTextfile)
                    writer.append("من المعروف أن النتيجة النهائية لسوء خدمة العملاء وعدم رضائهم هو فقدان الأرباح.")
                    writer.flush()
                    writer.close()
                    Toast.makeText(
                        this, "Arabic scrolling text file created.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if (!setDigitfile.exists()) {
                    createExcelFile(setDigitfile.absolutePath)
                }
            } catch (e: IOException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
                Log.e("TravellerLog :: ", "Problem creating file folder")
            }
        }
    }

    // create new Excel File if not exist
    private fun createExcelFile(directoryPath: String) {
        val workbook = HSSFWorkbook()
        val firstSheet = workbook.createSheet("Sheet 0")

        // 2 row
        val row0 = firstSheet.createRow(0)
        val cell_00 = row0.createCell(0)
        val cell_01 = row0.createCell(1)
        cell_01.cellType = Cell.CELL_TYPE_NUMERIC
        cell_00.setCellValue("Arabic_Font")
        cell_01.setCellValue(1.0)

        // 3 row
        val row2 = firstSheet.createRow(2)
        val cell_20 = row2.createCell(0)
        val cell_21 = row2.createCell(1)
        cell_21.cellType = Cell.CELL_TYPE_NUMERIC
        cell_20.setCellValue("Female_Voice")
        cell_21.setCellValue(1.0)

        // 4 row
        val row4 = firstSheet.createRow(4)
        val cell_40 = row4.createCell(0)
        val cell_41 = row4.createCell(1)
        cell_41.cellType = Cell.CELL_TYPE_NUMERIC
        cell_40.setCellValue("English + Arabic Voice")
        cell_41.setCellValue(1.0)

        // 5 row
        val row6 = firstSheet.createRow(6)
        val cell_60 = row6.createCell(0)
        val cell_61 = row6.createCell(1)
        val cell_62 = row6.createCell(2)
        cell_61.cellType = Cell.CELL_TYPE_NUMERIC
        cell_60.setCellValue("Advertise Delay")
        cell_61.setCellValue(20.0)
        cell_62.setCellValue("sec")

        // 6 row
        val row8 = firstSheet.createRow(8)
        val cell_80 = row8.createCell(0)
        val cell_81 = row8.createCell(1)
        cell_81.cellType = Cell.CELL_TYPE_NUMERIC
        cell_80.setCellValue("Token Voice")
        cell_81.setCellValue(0.0)

        // 7 row
        val row10 = firstSheet.createRow(10)
        val cell_100 = row10.createCell(0)
        val cell_101 = row10.createCell(1)
        val cell_102 = row10.createCell(2)
        cell_101.cellType = Cell.CELL_TYPE_NUMERIC
        cell_100.setCellValue("Video volume")
        cell_101.setCellValue(50.0)
        cell_102.setCellValue("0 to 100")

        // 8 row
        val row12 = firstSheet.createRow(12)
        val cell_120 = row12.createCell(0)
        val cell_121 = row12.createCell(1)
        cell_121.cellType = Cell.CELL_TYPE_NUMERIC
        cell_120.setCellValue("Play Voice files")
        cell_121.setCellValue(0.0)

        // 9 row
        val row14 = firstSheet.createRow(14)
        val cell_140 = row14.createCell(0)
        val cell_141 = row14.createCell(1)
        val cell_142 = row14.createCell(2)
        cell_141.cellType = Cell.CELL_TYPE_NUMERIC
        cell_140.setCellValue("Window Display")
        cell_141.setCellValue(0.0)
        cell_142.setCellValue("0 -> Window  1 -> Keypad")

        // nirmal 01 oct 2014 -- added field for english-token font-size
        // 10th row
        val row16 = firstSheet.createRow(16)
        val cell_160 = row16.createCell(0)
        val cell_161 = row16.createCell(1)
        val cell_162 = row16.createCell(2)
        cell_161.cellType = Cell.CELL_TYPE_NUMERIC
        cell_160.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_ENG_FONT_FULL_SCREEN)
        cell_161.setCellValue(400.0)
        cell_162.setCellValue("Fullscreen english token")

        // nirmal 01 oct 2014 -- added field for arabic-token font-size
        // 11th row
        val row18 = firstSheet.createRow(18)
        val cell_180 = row18.createCell(0)
        val cell_181 = row18.createCell(1)
        val cell_182 = row18.createCell(2)
        cell_181.cellType = Cell.CELL_TYPE_NUMERIC
        cell_180.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_ARB_FONT_FULL_SCREEN)
        cell_181.setCellValue(650.0)
        cell_182.setCellValue("Fullscreen arabic token")

        // nirmal 01 oct 2014 -- added field for english-token font-size
        // (half-screen)
        // 12th row
        val row20 = firstSheet.createRow(20)
        val cell_200 = row20.createCell(0)
        val cell_201 = row20.createCell(1)
        val cell_202 = row20.createCell(2)
        cell_201.cellType = Cell.CELL_TYPE_NUMERIC
        cell_200.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_ENG_FONT_HALF_SCREEN)
        cell_201.setCellValue(150.0)
        cell_202.setCellValue("Half screen english token")

        // nirmal 01 oct 2014 -- added field for arabic-token font-size
        // (half-screen)
        // 13th row
        val row22 = firstSheet.createRow(22)
        val cell_220 = row22.createCell(0)
        val cell_221 = row22.createCell(1)
        val cell_222 = row22.createCell(2)
        cell_221.cellType = Cell.CELL_TYPE_NUMERIC
        cell_220.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_ARB_FONT_HALF_SCREEN)
        cell_221.setCellValue(180.0)
        cell_222.setCellValue("Half screen arabic token")

        // nirmal 07 oct 2014 -- added field for background-color
        val row24 = firstSheet.createRow(24)
        val cell_240 = row24.createCell(0)
        val cell_241 = row24.createCell(1)
        val cell_242 = row24.createCell(2)
        cell_240.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_BG_COLOR)
        cell_241.setCellValue("black")
        cell_242.setCellValue("#RRGGBB or leave it empty to use image")


        // nirmal 07 oct 2014 -- added field for token text color
        val row26 = firstSheet.createRow(26)
        val cell_260 = row26.createCell(0)
        val cell_261 = row26.createCell(1)
        val cell_262 = row26.createCell(2)
        cell_260.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_TOKEN_COLOR)
        cell_261.setCellValue("red")
        cell_262.setCellValue("#RRGGBB")

        // nirmal 07 oct 2014 -- added field for token text blink color
        val row28 = firstSheet.createRow(28)
        val cell_280 = row28.createCell(0)
        val cell_281 = row28.createCell(1)
        val cell_282 = row28.createCell(2)
        cell_280.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_TOKEN_BLINK_COLOR)
        cell_281.setCellValue("blue")
        cell_282.setCellValue("#RRGGBB")

        // nirmal 08 oct 2014 -- added field for token-text-view top-padding
        val row30 = firstSheet.createRow(30)
        val cell_300 = row30.createCell(0)
        val cell_301 = row30.createCell(1)
        cell_300.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_TOKEN_PADDING_TOP)
        cell_301.setCellValue("60")

        // nirmal 08 oct 2014 -- added field for token-text-view bottom-padding
        val row32 = firstSheet.createRow(32)
        val cell_320 = row32.createCell(0)
        val cell_321 = row32.createCell(1)
        cell_320.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_TOKEN_PADDING_BOTTOM)
        cell_321.setCellValue("0")

        val row34 = firstSheet.createRow(34)
        val cell_340 = row34.createCell(0)
        val cell_341 = row34.createCell(1)
        val cell_342 = row34.createCell(2)
        cell_340.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_AUDIO_FILE_EXTENSION)
        cell_341.setCellValue(".wav")
        cell_342.setCellValue(".wav/.mp3/etc")

        val row36 = firstSheet.createRow(36)
        val cell_360 = row36.createCell(0)
        val cell_361 = row36.createCell(1)
        val cell_362 = row36.createCell(2)
        cell_361.cellType = Cell.CELL_TYPE_NUMERIC
        cell_360.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_ENG_COUNTER_FONT_HALF_SCREEN)
        cell_361.setCellValue(150.0)
        cell_362.setCellValue("Half screen english counter ")

        val row38 = firstSheet.createRow(38)
        val cell_380 = row38.createCell(0)
        val cell_381 = row38.createCell(1)
        val cell_382 = row38.createCell(2)
        cell_381.cellType = Cell.CELL_TYPE_NUMERIC
        cell_380.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_ARB_COUNTER_FONT_HALF_SCREEN)
        cell_381.setCellValue(180.0)
        cell_382.setCellValue("Half screen arabic counter")

        val row40 = firstSheet.createRow(40)
        val cell_400 = row40.createCell(0)
        val cell_401 = row40.createCell(1)
        val cell_402 = row40.createCell(2)
        cell_400.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_COUNTER_BG_COLOR)
        cell_401.setCellValue("black")
        cell_402.setCellValue("#RRGGBB or leave it empty to use image")

        val row42 = firstSheet.createRow(42)
        val cell_420 = row42.createCell(0)
        val cell_421 = row42.createCell(1)
        val cell_422 = row42.createCell(2)
        cell_420.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_COUNTER_COLOR)
        cell_421.setCellValue("red")
        cell_422.setCellValue("#RRGGBB")

        val row44 = firstSheet.createRow(44)
        val cell_440 = row44.createCell(0)
        val cell_441 = row44.createCell(1)
        cell_440.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_COUNTER_PADDING_TOP)
        cell_441.setCellValue("60")

        // nirmal 08 oct 2014 -- added field for token-text-view bottom-padding
        val row46 = firstSheet.createRow(46)
        val cell_460 = row46.createCell(0)
        val cell_461 = row46.createCell(1)
        cell_460.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_COUNTER_PADDING_BOTTOM)
        cell_461.setCellValue("0")

        val row47 = firstSheet.createRow(47)
        val cell_470 = row47.createCell(0)
        val cell_471 = row47.createCell(1)
        cell_470.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_SERVICE_NAME_TEXT_COLOR)
        cell_471.setCellValue("#ffffff")

        val row48 = firstSheet.createRow(48)
        val cell_480 = row48.createCell(0)
        val cell_481 = row48.createCell(1)
        cell_480.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_SERVICE_NAME_FONT_SIZE)
        cell_481.setCellValue("20")

        val row49 = firstSheet.createRow(49)
        val cell_490 = row49.createCell(0)
        val cell_491 = row49.createCell(1)
        cell_490.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_BRANCH_NAME_TEXT_COLOR)
        cell_491.setCellValue("green")

        val row50 = firstSheet.createRow(50)
        val cell_500 = row50.createCell(0)
        val cell_501 = row50.createCell(1)
        cell_500.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_BRANCH_NAME_FONT_SIZE)
        cell_501.setCellValue("20")

        val row51 = firstSheet.createRow(51)
        val cell_510 = row51.createCell(0)
        val cell_511 = row51.createCell(1)
        cell_510.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_BRANCH_NAME_BG_COLOR)
        cell_511.setCellValue("#fffafa")

        val row52 = firstSheet.createRow(52)
        val cell_520 = row52.createCell(0)
        val cell_521 = row52.createCell(1)
        cell_520.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_TIME_FONT_SIZE)
        cell_521.setCellValue("15")

        val row53 = firstSheet.createRow(53)
        val cell_530 = row53.createCell(0)
        val cell_531 = row53.createCell(1)
        cell_530.setCellValue(AppConstantsFlags.KEY_DIGIT_MAP_TIME_TEXT_COLOR)
        cell_531.setCellValue("#BCE4F4")

        var fos: FileOutputStream? = null
        try {
            val file = File(directoryPath)
            fos = FileOutputStream(file)
            workbook.write(fos)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (fos != null) {
                try {
                    fos.flush()
                    fos.close()
                } catch (e2: Exception) {
                    e2.printStackTrace()
                }
            }
            Toast.makeText(
                this, "SetDigit.xls file created.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.webSocketClient?.disconnect()
        if (timer != null) {
            timer!!.purge()
            timer!!.cancel()
        }
        if (timerTask != null) {
            timerTask!!.cancel()
        }
    }

    val isExternalStorageReadOnly: Boolean
        get() {
            val extStorageState = Environment.getExternalStorageState()
            if (Environment.MEDIA_MOUNTED_READ_ONLY == extStorageState) {
                return true
            }
            return false
        }

    val isExternalStorageAvailable: Boolean
        get() {
            val extStorageState = Environment.getExternalStorageState()
            if (Environment.MEDIA_MOUNTED == extStorageState) {
                return true
            }
            return false
        }

    // Read Text file from sdcard
    private fun readTextFile() {
        try {
            var br: BufferedReader

            val englishFile = File(
                sdcardPath,
                "/Queue_Config Files/Scroll_text.txt"
            )
            val enText = StringBuilder()
            var enLine: String?
            br = BufferedReader(FileReader(englishFile))
            while ((br.readLine().also { enLine = it }) != null) {
                enText.append(enLine)
            }
            br.close()

            tv_scrollTxtEng!!.text = enText
            tv_scrollTxtEng!!.startScroll()

            val arabicFile = File(
                sdcardPath,
                "/Queue_Config Files/Arabic Scroll.txt"
            )

            val arText = StringBuilder()
            var arLine: String?

            br = BufferedReader(FileReader(arabicFile))
            while ((br.readLine().also { arLine = it }) != null) {
                arText.append(arLine)
            }
            br.close()

            tv_scrollTxtArabic!!.text = arText
            tv_scrollTxtArabic!!.startScroll_Arabic()
        } catch (e: IOException) {
            Toast.makeText(
                baseContext, "Scroll_text.txt file not found.",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
        }
    }

    // Read excel file from sdcard.
    private fun readExcelFile() {
        if (!isExternalStorageAvailable || isExternalStorageReadOnly) {
            Log.e(null, "Storage not available or read only")
            return
        }
        val w: Workbook
        try {
            val inputWorkbook = File(
                sdcardPath
                        + "/Queue_Config Files/SetDigit.xls"
            )
            w = Workbook.getWorkbook(inputWorkbook)
            // Get the first sheet
            val sheet = w.getSheet(0)
            // Loop over first 10 column and lines
            val rows = sheet.rows
            var i = 0
            for (j in 0 until rows) {
                val cell1 = sheet.getCell(i, j)
                val cell2 = sheet.getCell(i + 1, j)
                val key = cell1.contents
                val value = cell2.contents
                digitMap[key] = value
                i = 0
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(
                baseContext, "SetDigit.xls file not found.",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: BiffException) {
            Toast.makeText(
                baseContext, "SetDigit.xls file not found.",
                Toast.LENGTH_SHORT
            ).show()
            e.printStackTrace()
        } catch (e: Exception) {
            Toast.makeText(
                baseContext, "SetDigit.xls file not found.",
                Toast.LENGTH_SHORT
            ).show()
            e.printStackTrace()
        }
    }

    override fun onStop() {
        super.onStop()
        try {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            if (AppConstantsFlags.SHOW_ADVERTIZEMENT !== "") {
                if (AppConstantsFlags.SHOW_ADVERTIZEMENT == "Show Advertisement") {
                    videoPlayer!!.stopPlayback()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    // Back button pressed.
    override fun onBackPressed() {
        try {
            val frgMgr = fragmentManager
            val tknFrg = frgMgr
                .findFragmentById(R.id.fragment_token) as TokenActivity
            if (AppConstantsFlags.SHOW_ADVERTIZEMENT !== "") {
                if (TokenActivity.Companion.WINDOW_DISPLAY != 0 && AppConstantsFlags.SHOW_ADVERTIZEMENT == "Show Advertisement") {
                    videoPlayer!!.stopPlayback()
                    tknFrg.mPlayer!!.stop()
                    this.finish()
                } else {
                    this.finish()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun checkPermissions(): Boolean {
        val permission1 =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val permission2 =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE)
        val permission3 =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
        val permission4 =
            ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE)
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        if (permission1 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (permission2 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_WIFI_STATE)
        }
        if (permission3 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_NETWORK_STATE)
        }
        if (permission4 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CHANGE_WIFI_STATE)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionsNeeded.toTypedArray<String>(),
                100
            )
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == 100) {
            if (grantResults.size > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
            }
            return
        }
    }

    override fun onPause() {
        // TODO Auto-generated method stub
        super.onPause()
    }

    override fun onResume() {
        // TODO Auto-generated method stub
        super.onResume()
    }

    private fun resetLastRunDateUpdateTimer() {
        lastRunDateUpdateTimer.cancel()
        lastRunDateUpdateTimer.start()
    }

    private fun showLicenseDialog() {
        val licenseDialog = LicenseDialog()
        licenseDialog.isCancelable = false
        licenseDialog.show(fragmentManager, "LicenseDialog")
    }

    private val mACAddress: String
        get() {
            try {
                val wifiManager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
                val wInfo = wifiManager.connectionInfo
                val macAddress = wInfo.macAddress
                return macAddress ?: ""
            } catch (e: Exception) {
                // TODO Auto-generated catch block
                return ""
            }
        }

    private fun isLicenseValid(showToastMsg: Boolean): Boolean {
        try {
            var deviceId = Secure.getString(
                this.contentResolver,
                Secure.ANDROID_ID
            )
            deviceId += mACAddress
            val appSalt = byteArrayOf(34, 93, 43, 56, 40, 23, 99, 14)
            val fullTypeSalt = byteArrayOf(74, 8, 69, 34, 40, 15)
            val trialTypeSalt = byteArrayOf(63, 59, 54, 37, 19, 81)
            var storedLicense = ""
            val license = mDataLayer?.licenseKey
            storedLicense = license?.licenseKey ?: ""

            val TrialDays = license?.trialDays
            Log.d("Trial Days............", TrialDays!!)
            var calLicense: Calendar? = GregorianCalendar()
            calLicense = license.startDate

            val strLicenseStartDate = (String.format(
                Locale.US, "%02d",
                calLicense!![Calendar.DAY_OF_MONTH]
            ) + "-"
                    + String.format(
                Locale.US, "%02d",
                1 + calLicense!![Calendar.MONTH]
            ) + "-"
                    + calLicense!![Calendar.YEAR])
            calLicense = truncateTimePart(calLicense)
            var calLastRunDate: Calendar? = GregorianCalendar()
            calLastRunDate = license.lastRunDate

            storedLicense = storedLicense ?: ""

            Log.d(
                "License 1", getHash(
                    String(trialTypeSalt, charset("UTF-8")) + deviceId
                            + String(appSalt, charset("UTF-8")) + strLicenseStartDate
                            + TrialDays
                )
            )
            Log.d("Lice 2", storedLicense)


            if (getHash(
                    String(fullTypeSalt, charset("UTF-8")) + deviceId
                            + String(appSalt, charset("UTF-8")) + strLicenseStartDate
                            + "000"
                ) == storedLicense
            ) {
                if (showToastMsg) {
                    Toast.makeText(
                        applicationContext, "Full License",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                ISTRIAL = false
                return true
            } else if (getHash(
                    String(trialTypeSalt, charset("UTF-8")) + deviceId
                            + String(appSalt, charset("UTF-8")) + strLicenseStartDate
                            + TrialDays
                ) == storedLicense
            ) {
                if (showToastMsg) {
                    Toast.makeText(
                        applicationContext, "Trial License",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                val calNow: Calendar = GregorianCalendar(Locale.US)

                var calLicenseExpiry: Calendar
                calLicenseExpiry = calLicense.clone() as GregorianCalendar

                calLicenseExpiry.add(Calendar.DATE, TrialDays!!.toInt())
                calLicenseExpiry = truncateTimePart(calLicenseExpiry)

                if ((calNow.after(calLicense) || calNow == calLicense)
                    && calNow.before(calLicenseExpiry)
                ) {

                    if (calNow.after(calLastRunDate)) {
                        ISTRIAL = true
                        if (showToastMsg) {
                            Toast.makeText(
                                applicationContext,
                                "Trial license expires on :"
                                        + String.format(
                                    Locale.US,
                                    "%02d",
                                    calLicenseExpiry[Calendar.DAY_OF_MONTH]
                                ) + "-"
                                        + calLicenseExpiry.getDisplayName(
                                    Calendar.MONTH,
                                    Calendar.SHORT, Locale.US
                                )
                                        + "-"
                                        + calLicenseExpiry[Calendar.YEAR],
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        return true
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Detected system-date backward-change !!!",
                            Toast.LENGTH_SHORT
                        ).show()
                        return false
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Trial License expired !!!", Toast.LENGTH_SHORT
                    )
                        .show()
                    return false
                }
            } else {
                Toast.makeText(
                    applicationContext, "Invalid License !!!",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
        } catch (e: UnsupportedEncodingException) {
            // TODO: handle exception
        } catch (e: Exception) {
            // TODO: handle exception
        }
        return false
    }

    private fun truncateTimePart(cal: Calendar?): Calendar {
        cal!![Calendar.HOUR_OF_DAY] = 0
        cal[Calendar.MINUTE] = 0
        cal[Calendar.SECOND] = 0
        cal[Calendar.MILLISECOND] = 0
        return cal.clone() as GregorianCalendar
    }

    private fun getHash(str: String): String {
        var strHash = ""
        try {
            val msgDigest = MessageDigest.getInstance("SHA-256")
            // msgDigest.update(str.getBytes());
            msgDigest.update(str.toByteArray(charset("UTF-8")))
            val hashBytes = msgDigest.digest()
            val hexString = StringBuffer()
            for (i in hashBytes.indices) {
                val hex = Integer.toHexString(0xff and hashBytes[i].toInt())
                if (hex.length == 1) hexString.append('0')
                hexString.append(hex)
            }
            strHash = hexString.toString()
        } catch (e: NoSuchAlgorithmException) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            strHash = ""
        } catch (e: UnsupportedEncodingException) {
            strHash = ""
        } catch (e: Exception) {
            strHash = ""
        }
        return strHash
    }

    fun handler(delay: Long, block: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed({
            block()
        }, delay)
    }


    override fun onLicenseDialogFinished(license: LicenseDataHolder?) {
        mDataLayer!!.saveLicenseKey(license)
        if (!isLicenseValid(true)) {
            showLicenseDialog()
        }
    }

    companion object {
        const val LOG_TAG: String = "qs"

        const val TIME_OUT: Int = 5000
        const val MSG_DISMISS_DIALOG: Int = 0
        var sdcardPath: String = Environment.getExternalStorageDirectory()
            .toString()

        // private TextView mTokenTouchView;
        var digitMap: HashMap<String?, String> = HashMap()
        var sharePrefSettings: SharedPreferences? = null
        var serverName: String? = null
        var mainCounter = ""
        lateinit var viewModel: MainViewModel

    }

    override fun onSuccess() {
        println("here is connected")
        serverName = sharePrefSettings?.getString("Server_name", "")

        val port = sharePrefSettings?.getString("QueueTCP_port", "")

        if (!isAppStarted){
            if (serverName?.isNotEmpty() == true){
                viewModel.connectWebSocket(serverName ?: "",port ?: "")
                handler(500){
                    viewModel.sendReConnectionMessage(
                        sharePrefSettings?.getString("counterType", "") ?: "",
                        sharePrefSettings?.getString("counterId", "") ?: "",
                        sharePrefSettings?.getString("serviceId", "") ?: ""
                    )
                }
            }
        }
        isAppStarted = false

    }

    override fun onFailure() {

    }
}
