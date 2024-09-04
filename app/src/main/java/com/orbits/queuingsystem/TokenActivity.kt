package com.orbits.queuingsystem

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.ValueAnimator
import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.example.elcapi.jnielc
import com.google.gson.JsonObject
import com.orbits.queuingsystem.MainActivity.Companion.viewModel
import com.orbits.queuingsystem.helper.TransactionDataModel
import com.orbits.queuingsystem.helper.interfaces.ConnectionListener
import com.orbits.queuingsystem.helper.interfaces.TokenListener
import com.orbits.queuingsystem.webservice.BranchWebService.getBranchName
import com.orbits.queuingsystem.webservice.WebService.getTokenNumber
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException

class TokenActivity : Fragment(), TokenListener , ConnectionListener {
    var advView: ImageView? = null
    var a: Int = 0

    // public static boolean callInSec = false;
    //  private static long currDate;
    //  private static long oldDate;
    // private static long lastSocketDataTime;
    // public int ENGLISH_FONT = 0;
    var FEMALE_VOICE: Int = 0
    var En_Ar_VOICE: Int = 0
    var SPEAK_TOKEN_No: Int = 0
    var PLAY_VOICE_FILES: Int = 0
    var mediaFile: File? = null // universal media file path
    var mediaFile_En: File? = null // media file path 1 in both voices
    var mediaFile_Ar: File? = null // media file path 2 in both voices
    var mPlayer: MediaPlayer? = null
    var playAr: Boolean = false
    var ArEn_Font: Boolean = false
    var tknView: View? = null
    var tokenLength: String? = null
    var lastLedColor: Int = 0
    var fb: Int = 0
    var arabicFont: String? = null
    var femaleVoice: String? = null
    var enAr_Voice: String? = null
    var speakToken: String? = null
    var playVoices: String? = null
    var windowDisplay: String? = null
    var usernameDisplay: String? = null
    var image_FileNames: Array<String?>? = null
    var image_FilePaths: Array<String?>? = null
    var video_FileNames: Array<String?>? = null
    var video_FilePaths: Array<String?>? = null
    var serviceDisplay: String? = null
    var branchDisplay: String? = null

    // Touch listener on Token
    var tokenOnTouchListener: OnTouchListener = OnTouchListener { v, event ->
        if (event.action == MotionEvent.ACTION_UP) {
            val actionBar = activity.actionBar
            // nirmal 07 july 2014 -- hide the action-bar
            if (actionBar != null) {
                if (actionBar.isShowing) {
                    actionBar.hide()
                } else {
                    actionBar.show()
                }
            }
        }
        true
    }
    var utils: Utils? = null

    // int isLoggedIn;
    private var tokenTouchView: TextView? = null
    private var txtConnectivityView: TextView? = null
    private var txtTime: TextView? = null
    var tvServiceName: TextView? = null
    var tvBranchName: TextView? = null
    var txtUserName: TextView? = null
    var layoutBranchName: RelativeLayout? = null
    private val mArrayList = ArrayList<String>()

    // private Handler tokenHandler;
    // public static int tokenHandlerDelay;
    // public Socket client;
    // public static boolean dbFlag;
    //  private int COUNTER_NO;
    private var mTokenLayoutContainer: LinearLayout? = null

    // private boolean isReadTaskBusy = false;
    private var getTokenJsonResponse: String? = null
    private var getBranchJsonResponse: String? = null
    private var stopReadTask = false
    private var mHandler_readServerData: Handler? = null

    // private Handler mHandlerSendBeat = null;
    //private static Socket sock = null;
    //  private InputStream inStream = null;
    // private static PrintWriter dataOutStream = null;
    // private static String mProtocolIdentifySelf = null;
    // private static String mProtocolSendBeat = null;
    // private boolean mRingBell = true;///manju 29/12/2021
    // private byte mConnectionCheckCounter;
    private var mAudioFileExtenstion = ".wav"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (AppConstantsFlags.SHOW_SERVICE_NAME_TOP || AppConstantsFlags.SHOW_SERVICE_NAME
        ) {
            tknView =
                inflater.inflate(R.layout.activity_token_service_branch_name, container, false)
            tvServiceName = tknView?.findViewById(R.id.txtServiceName)
            tvBranchName = tknView?.findViewById(R.id.txtBranchName)
            advView = tknView?.findViewById(R.id.imgBg)
            layoutBranchName = tknView?.findViewById(R.id.layoutBranchName)
        } else {
            tknView = inflater.inflate(R.layout.activity_token, container, false)
        }
        MainActivity.viewModel.setTokenListener(this)
        MainActivity.viewModel.setConnectionListener(this)
        tokenTouchView = tknView?.findViewById<View>(R.id.txtTokenNo) as TextView

        txtConnectivityView = tknView?.findViewById<View>(R.id.txtconnectivity) as TextView
        txtTime = tknView?.findViewById<View>(R.id.txtTime) as TextView
        utils = Utils()
        tokenTouchView!!.paintFlags = (tokenTouchView!!.paintFlags
                or Paint.FAKE_BOLD_TEXT_FLAG)
        tokenTouchView!!.setOnTouchListener(tokenOnTouchListener)
        try {
            tokenLength = MainActivity.Companion.sharePrefSettings
                ?.getString("tokenLength", "3")
            NO_OF_DIGIT = tokenLength!!.toInt()
            if (tokenLength == "4") {
                tokenTouchView!!.text = "0000"
            } else {
                tokenTouchView!!.text = "000"
            }
            val fnt = Typeface.createFromAsset(
                activity.assets,
                "ARIAL.TTF"
            )
            tokenTouchView!!.setTypeface(fnt)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mPlayer = MediaPlayer()
        mTokenLayoutContainer = tknView!!
            .findViewById<View>(R.id.rightLayout) as LinearLayout

        ///setMediaPath();   ///manjusha 08/01/2019
        return tknView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState)
        //        mConnectionCheckCounter = 0;
//        try {
//
//            QUEUE_SERVER_PORT = Integer.parseInt(MainActivity.sharePrefSettings
//                    .getString("QueueTCP_port", "1020"));
//            COUNTER_NO = Integer.parseInt(MainActivity.sharePrefSettings
//                    .getString("CounterNo", "1"));
//            // nirmal 03 nov 2014 -- 4digitCounterNumber
//            mProtocolIdentifySelf = "@"
//                    // String.valueOf(Character.toChars(64))
//                    // + String.valueOf(Character.toChars(48))
//                    // + String.valueOf(Character.toChars(48))
//                    + String.format(Locale.US, "%04d", COUNTER_NO)
//                    // + String.valueOf(COUNTER_NO / 1000)
//                    // + String.valueOf((COUNTER_NO % 1000) / 100)
//                    // + String.valueOf(COUNTER_NO / 10)
//                    // + String.valueOf(COUNTER_NO % 10)
//                    + String.valueOf(Character
//                    .toChars(AppConstantsFlags.DEVICE_TYPE_ANDROID_WINDOW_DISPLAY))
//                    + String.valueOf(Character
//                    .toChars(AppConstantsFlags.COMMAND_CODE_IDENTIFY_SELF))
//                    + String.valueOf(Character
//                    .toChars(AppConstantsFlags.STATUS_BYTE))
//                    + String.valueOf(Character.toChars(5))
//                    + String.valueOf(Character.toChars(48))
//                    + String.valueOf(Character.toChars(48))
//                    + String.valueOf(Character.toChars(48))
//                    + String.valueOf(Character.toChars(48))
//                    + String.valueOf(Character.toChars(48))
//                    + String.valueOf(Character.toChars(10))
//                    + String.valueOf(Character.toChars(13));
//
//
//            ///manju 16/dec/2021
//            mProtocolSendBeat = "@"
//                    // String.valueOf(Character.toChars(64))
//                    // + String.valueOf(Character.toChars(48))
//                    // + String.valueOf(Character.toChars(48))
//                    + String.format(Locale.US, "%04d", COUNTER_NO)
//                    // + String.valueOf(COUNTER_NO / 1000)
//                    // + String.valueOf((COUNTER_NO % 1000) / 100)
//                    // + String.valueOf(COUNTER_NO / 10)
//                    // + String.valueOf(COUNTER_NO % 10)
//                    + String.valueOf(Character
//                    .toChars(AppConstantsFlags.DEVICE_TYPE_ANDROID_WINDOW_DISPLAY))
//                    + String.valueOf(Character.toChars(73))
//                    + String.valueOf(Character
//                    .toChars(AppConstantsFlags.STATUS_BYTE))
//                    + String.valueOf(Character.toChars(5))
//                    + String.valueOf(Character.toChars(48))
//                    + String.valueOf(Character.toChars(48))
//                    + String.valueOf(Character.toChars(48))
//                    + String.valueOf(Character.toChars(48))
//                    + String.valueOf(Character.toChars(48))
//                    + String.valueOf(Character.toChars(10))
//                    + String.valueOf(Character.toChars(13));
//        } catch (Exception e) {
//            // TODO: handle exception
//            // Toast.makeText(
//            // getActivity(),
//            // "error while creating identifier protocol  "
//            // + e.getMessage(), Toast.LENGTH_LONG).();
//        }
//
//        String checkstatus = MainActivity.sharePrefSettings
//                .getString("CheckAndroid", "");
////        Toast.makeText(getActivity(),
////                "CheckAndroid" + checkstatus,
////                Toast.LENGTH_LONG).show();
        mHandler_readServerData = Handler()
        //   mHandlerSendBeat = new Handler();
        // startBeatTime();
        //checkSocketConnectionTimer();
        mTokenColor = Color.RED
        mTokenBlinkColor = Color.BLUE
    }

    override fun onStart() {
        super.onStart()

        try {
            arabicFont = MainActivity.Companion.digitMap.get("Arabic_Font")
            femaleVoice = MainActivity.Companion.digitMap.get("Female_Voice")
            enAr_Voice = MainActivity.Companion.digitMap.get("English + Arabic Voice")
            speakToken = MainActivity.Companion.digitMap.get("Token Voice")
            playVoices = MainActivity.Companion.digitMap.get("Play Voice files")
            windowDisplay = MainActivity.Companion.digitMap.get("Window Display")

            if (arabicFont == null || femaleVoice == null || enAr_Voice == null || speakToken == null || playVoices == null || windowDisplay == null) {
                // totalDigits = "3";
                arabicFont = "1"
                femaleVoice = "1"
                enAr_Voice = "0"
                speakToken = "0"
                playVoices = "0"
                windowDisplay = "0"
            }

            FEMALE_VOICE = femaleVoice!!.toInt()
            ARABIC_FONT = arabicFont!!.toInt()
            En_Ar_VOICE = enAr_Voice!!.toInt()
            SPEAK_TOKEN_No = speakToken!!.toInt()
            PLAY_VOICE_FILES = playVoices!!.toInt()
            WINDOW_DISPLAY = windowDisplay!!.toInt()

            //// set here due to issue in en_ar voice conversion/// manjusha 08/01/2019
            setMediaPath()

            stopReadTask = false
           // start_handler_readServerData()
            mAudioFileExtenstion = MainActivity.Companion.digitMap
                .get(AppConstantsFlags.KEY_DIGIT_MAP_AUDIO_FILE_EXTENSION)!!

            //totalDigits = MainActivity.digitMap.get("Token_Digits");

            // nirmal 1 oct 2014 -- set different text-sizes according to
            // settings
            if (ARABIC_FONT == 1) {
                if (AppConstantsFlags.SHOW_ADVERTIZEMENT != "Show Nothing") {
                    tokenTouchView
                        ?.setTextSize(
                            MainActivity.Companion.digitMap
                                .get(AppConstantsFlags.KEY_DIGIT_MAP_ARB_FONT_HALF_SCREEN)!!.toInt()
                                .toFloat()
                        )
                } else {
                    tokenTouchView
                        ?.setTextSize(
                            MainActivity.Companion.digitMap
                                .get(AppConstantsFlags.KEY_DIGIT_MAP_ARB_FONT_FULL_SCREEN)!!.toInt()
                                .toFloat()
                        )
                }
            } else {
                if (AppConstantsFlags.SHOW_ADVERTIZEMENT != "Show Nothing") {
                    tokenTouchView
                        ?.setTextSize(
                            MainActivity.Companion.digitMap
                                .get(AppConstantsFlags.KEY_DIGIT_MAP_ENG_FONT_HALF_SCREEN)!!.toInt()
                                .toFloat()
                        )
                } else {
                    tokenTouchView
                        ?.setTextSize(
                            MainActivity.Companion.digitMap
                                .get(AppConstantsFlags.KEY_DIGIT_MAP_ENG_FONT_FULL_SCREEN)!!.toInt()
                                .toFloat()
                        )
                }
            }

            try {
                // nirmal 08 oct 2014 -- set padding for token-text-view
                tokenTouchView
                    ?.setPadding(
                        0,
                        MainActivity.Companion.digitMap
                            .get(AppConstantsFlags.KEY_DIGIT_MAP_TOKEN_PADDING_TOP)!!.toInt(),
                        0,
                        MainActivity.Companion.digitMap
                            .get(AppConstantsFlags.KEY_DIGIT_MAP_TOKEN_PADDING_BOTTOM)!!.toInt()
                    )
            } catch (e: Exception) {
                Toast.makeText(
                    activity,
                    "Some error occurred, Please check padding values.",
                    Toast.LENGTH_LONG
                ).show()
            }
            var USE_BG_IMG = false
            try {
                // nirmal 07 oct 2014 -- set backgroud color as mentioned in
                // excel file
                /*USE_BG_IMG = true;
                mTokenLayoutContainer.setBackgroundColor(Color.BLACK);*/
                // int bgColor = Color.parseColor("#0c543c");
                mTokenLayoutContainer!!.setBackgroundColor(
                    Color.parseColor(
                        MainActivity.Companion.digitMap.get(
                            AppConstantsFlags.KEY_DIGIT_MAP_BG_COLOR
                        )
                    )
                )
            } catch (e: Exception) {
                // nirmal 07 oct 2014 -- use background image if invalid color
                // is entered
                USE_BG_IMG = true
                mTokenLayoutContainer!!.setBackgroundColor(Color.BLACK)
            }
            if (USE_BG_IMG) {
                try {
                    // nirmal 07 oct 2014 -- set backgroud image
                    val bgImgFile = File(
                        Environment.getExternalStorageDirectory()
                            .toString() + "/Queue_Config Files/bgimg.png"
                    )
                    if (bgImgFile.exists()) {
                        mTokenLayoutContainer!!.background =
                            Drawable.createFromPath(bgImgFile.absolutePath)
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        activity,
                        "Some error occurred, Please check bgimg.png file.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            try {
                // nirmal 07 oct 2014 -- set token color as mentioned in
                // excel file
                mTokenColor = Color.parseColor(
                    MainActivity.Companion.digitMap
                        .get(AppConstantsFlags.KEY_DIGIT_MAP_TOKEN_COLOR)
                )
            } catch (e: Exception) {
                mTokenColor = Color.RED
                Toast.makeText(
                    activity,
                    "Some error occurred, Please check token color value.",
                    Toast.LENGTH_LONG
                ).show()
            }
            try {
                // nirmal 07 oct 2014 -- set token blink color as mentioned in
                // excel file
                mTokenBlinkColor = Color
                    .parseColor(
                        MainActivity.Companion.digitMap
                            .get(AppConstantsFlags.KEY_DIGIT_MAP_TOKEN_BLINK_COLOR)
                    )
            } catch (e: Exception) {
                mTokenBlinkColor = Color.BLUE
                Toast.makeText(
                    activity,
                    "Some error occurred, Please check token blink color value.",
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: NullPointerException) {
            // Log.e("awd :: ", "Null pointer exception");
        } catch (e: Exception) {
            // Log.e("awd :: ", "exception");
        }
        if (AppConstantsFlags.SHOW_TIME) {
            txtTime!!.visibility = View.VISIBLE
        } else {
            txtTime!!.visibility = View.GONE
        }
    }

    override fun onTokenReceived(jsonObject: JsonObject?) {
        handler(1000) {
            println("here is transaction added")
            val model = jsonObject?.getAsJsonObject("transaction")
            /*handler(500){
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
                    counterId = MainActivity.mainCounter
                )
            }*/
            if (jsonObject?.has("reconnected") == false){
                setupUI(model)
            }
        }
    }

    fun handler(delay: Long, block: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed({
            block()
        }, delay)
    }

    private fun setupUI(model:JsonObject ?= null) {
        println("here is branch starting 5555")
      //  GetBranchName().execute()
        try {
            val serviceTextSize: String =
                MainActivity.digitMap.get(AppConstantsFlags.KEY_DIGIT_MAP_SERVICE_NAME_FONT_SIZE) ?: ""
            if (serviceTextSize != null) {
                tvServiceName?.textSize = serviceTextSize.toInt().toFloat()
                tvServiceName?.setTextColor(
                    Color.parseColor(
                        MainActivity.digitMap
                            .get(AppConstantsFlags.KEY_DIGIT_MAP_SERVICE_NAME_TEXT_COLOR)
                    )
                )
            }
            val branchTextSize: String =
                MainActivity.digitMap.get(AppConstantsFlags.KEY_DIGIT_MAP_BRANCH_NAME_FONT_SIZE) ?: ""
            if (branchTextSize != null) {
                tvBranchName?.textSize = branchTextSize.toInt().toFloat()
                tvBranchName?.setTextColor(
                    Color.parseColor(
                        MainActivity.digitMap.get(
                            AppConstantsFlags.KEY_DIGIT_MAP_BRANCH_NAME_TEXT_COLOR
                        )
                    )
                )
                layoutBranchName?.setBackgroundColor(
                    Color.parseColor(
                        MainActivity.digitMap.get(
                            AppConstantsFlags.KEY_DIGIT_MAP_BRANCH_NAME_BG_COLOR
                        )
                    )
                )
            }

            val isService: Boolean = MainActivity.Companion.sharePrefSettings!!.getString(
                AppConstantsFlags.key_pref_service_name,
                ""
            ).toBoolean()
            val isServiceTop: Boolean = MainActivity.sharePrefSettings!!.getString(
                AppConstantsFlags.key_pref_service_top,
                ""
            ).toBoolean()
            if (isService){
                tvServiceName?.visibility = View.VISIBLE
                tvServiceName?.text = model?.get("serviceAssign")?.asString
                tvBranchName?.text = ""
            }else if (isServiceTop){
                tvBranchName?.text = model?.get("serviceAssign")?.asString
                tvServiceName?.visibility = View.GONE
                tvServiceName?.text = ""
            }
            tokenTouchView?.text = model?.get("keypadToken")?.asString
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
    }

    private fun playTokenVoices(
        strToken: String,
        second: Int,
        time: String,
        serviceNameEng: String,
        serviceNameAr: String,
        username: String
    ) {
        try {
            val isUserName: Boolean = MainActivity.Companion.sharePrefSettings!!.getString(
                AppConstantsFlags.key_pref_user_name,
                ""
            ).toBoolean()
            val isBranch: Boolean = MainActivity.Companion.sharePrefSettings!!.getString(
                AppConstantsFlags.key_pref_branch_name,
                ""
            ).toBoolean()
            val isService: Boolean = MainActivity.Companion.sharePrefSettings!!.getString(
                AppConstantsFlags.key_pref_service_name,
                ""
            ).toBoolean()

            if (isUserName) {
                tvServiceName!!.text = "" + username
            } else if (isBranch) {
             //   GetBranchName().execute()
                tvServiceName!!.text = "" + branchDisplay
            } else if (isService) {
                if (ARABIC_FONT == 1) {
                    tvServiceName!!.text = "" + serviceNameAr
                } else {
                    tvServiceName!!.text = "" + serviceNameEng
                }
            } else {
                tvServiceName!!.text = ""
            }
            txtTime!!.text = "" + time ////2/7/2022
            val timeTextSize: String =
                MainActivity.Companion.digitMap.get(AppConstantsFlags.KEY_DIGIT_MAP_TIME_FONT_SIZE) ?: ""
            if (timeTextSize != null) {
                txtTime!!.textSize = timeTextSize.toInt().toFloat()
            }
            txtTime!!.setTextColor(
                Color.parseColor(
                    MainActivity.Companion.digitMap.get(
                        AppConstantsFlags.KEY_DIGIT_MAP_TIME_TEXT_COLOR
                    )
                )
            )
            if (strToken.equals("Free", ignoreCase = true)) {
                //// make textsize half of font size given in excel /////
                if (AppConstantsFlags.SHOW_ADVERTIZEMENT != "Show Nothing") {
                    val halfTextSize: Int =
                        MainActivity.Companion.digitMap.get(AppConstantsFlags.KEY_DIGIT_MAP_ENG_FONT_HALF_SCREEN)!!
                            .toInt()
                    val finalhalfLength = halfTextSize / 2
                    tokenTouchView
                        ?.setTextSize(Math.round(finalhalfLength.toFloat()).toFloat())
                } else {
                    val fullTextSize: Int =
                        MainActivity.Companion.digitMap.get(AppConstantsFlags.KEY_DIGIT_MAP_ENG_FONT_FULL_SCREEN)!!
                            .toInt()
                    val finalLength = fullTextSize / 2
                    tokenTouchView!!.textSize = Math.round(finalLength.toFloat()).toFloat()
                }
                tokenTouchView!!.text = "Free"
                ledOnGreen(100)
                lastLedColor = 2
                // Log.d("000:::free", strToken);
            } else if (strToken.equals("Closed", ignoreCase = true)) {
                if (AppConstantsFlags.SHOW_ADVERTIZEMENT != "Show Nothing") {
                    val halfTextSize: Int =
                        MainActivity.digitMap[AppConstantsFlags.KEY_DIGIT_MAP_ENG_FONT_HALF_SCREEN]?.toInt() ?: 0
                    val finalhalfLength = halfTextSize / 2
                    tokenTouchView!!.textSize = Math.round(finalhalfLength.toFloat()).toFloat()
                } else {
                    val fullTextSize: Int =
                        MainActivity.digitMap[AppConstantsFlags.KEY_DIGIT_MAP_ENG_FONT_FULL_SCREEN]?.toInt() ?: 0
                    val finalLength = fullTextSize / 2
                    tokenTouchView!!.textSize = Math.round(finalLength.toFloat()).toFloat()
                }
                tokenTouchView!!.text = "Closed" //// when counter is closed or logged out////
                ledOnRed(100)
            } else {
                if (ARABIC_FONT == 1) {
                    if (AppConstantsFlags.SHOW_ADVERTIZEMENT != "Show Nothing") {
                        tokenTouchView
                            ?.setTextSize(
                                MainActivity.Companion.digitMap
                                    .get(AppConstantsFlags.KEY_DIGIT_MAP_ARB_FONT_HALF_SCREEN)!!
                                    .toInt().toFloat()
                            )
                    } else {
                        tokenTouchView
                            ?.setTextSize(
                                MainActivity.Companion.digitMap
                                    .get(AppConstantsFlags.KEY_DIGIT_MAP_ARB_FONT_FULL_SCREEN)!!
                                    .toInt().toFloat()
                            )
                    }
                } else {
                    if (AppConstantsFlags.SHOW_ADVERTIZEMENT != "Show Nothing") {
                        tokenTouchView
                            ?.setTextSize(
                                MainActivity.Companion.digitMap
                                    .get(AppConstantsFlags.KEY_DIGIT_MAP_ENG_FONT_HALF_SCREEN)!!
                                    .toInt().toFloat()
                            )
                    } else {
                        tokenTouchView
                            ?.setTextSize(
                                MainActivity.Companion.digitMap
                                    .get(AppConstantsFlags.KEY_DIGIT_MAP_ENG_FONT_FULL_SCREEN)!!
                                    .toInt().toFloat()
                            )
                    }
                }
                //// manjusha 30/dec/2019////
                // int prefixLength = Integer.parseInt(MainActivity.sharePrefSettings
                //     .getString("prefix", ""));
                // if (prefixLength != 0) {
                //   token = Integer.parseInt(strToken.substring(strToken.length() - prefixLength));
                // } else {
                Companion.token = strToken


                // }
                var formattedToken = ""
                if (ARABIC_FONT != 0) {
                    for (i in 0 until strToken.length) {
                        val s = strToken[i].code
                        if (s >= 48 && s <= 57) {
                            formattedToken += CommonLogic.Companion.englishToArabicNumber(
                                strToken.substring(
                                    i,
                                    i + 1
                                ).toInt(), 1
                            )
                        } else {
                            formattedToken += strToken[i]
                        }
                    }
                } else {
                    formattedToken = strToken
                }
                //// manjusha 30/dec/2019////
                tokenTouchView!!.text = formattedToken


                // Log.e("response::", "" + count);
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (!strToken.equals(
                prevToken,
                ignoreCase = true
            ) && second < 3 && !(strToken.equals("Closed", ignoreCase = true)) && !(strToken.equals(
                "Free",
                ignoreCase = true
            ))
        ) {
            tokenAnimation()
            try {
                if (En_Ar_VOICE == 0) {
                    voiceConversion()
                } else {
                    enArVoiceConv(true)
                }
                // voiceConversion();
                currentSongIndex = 0
                playVoice(currentSongIndex)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            prevToken = strToken
        } else if (second >= 3) {
            prevToken = ""
        }
    }

    private fun updateUI() {
        try {
            if (image_FilePaths != null || video_FilePaths != null) {
                val bitmapImg: Bitmap

                //ImageView imageView;
                val img = advView!!.findViewById<ImageView>(R.id.imgBg)
                if (image_FilePaths!!.size == 1) {
//                bitmapImg = BitmapFactory.decodeFile(image_FilePaths[a]);
//                imageView.setImageBitmap(bitmapImg);
                    img.background = Drawable.createFromPath(image_FilePaths!![a])
                } else if (a < image_FilePaths!!.size) {
                    bitmapImg = BitmapFactory.decodeFile(image_FilePaths!![a])
                    //                imageView.setImageBitmap(bitmapImg);
//                imageView.startAnimation(myFadeInAnimation);
                    img.background = Drawable.createFromPath(image_FilePaths!![a])
                    img.startAnimation(AdvertiseActivity.Companion.myFadeInAnimation)
                    a++
                } else {
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()

        try {
            stopReadTask = false
          //  start_handler_readServerData()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    // set Voice path
    fun setMediaPath() {
        var MEDIA_PATH = ""
        var MEDIA_PATH_Both = ""
        val femaleArabic: String
        val femaleEnglish: String
        val maleArabic: String
        val maleEnglish: String

        try {
            femaleArabic = "/Queue_Voice Files/Arabic Voice/Female Voice"
            femaleEnglish = "/Queue_Voice Files/English Voice/Female Voice"
            maleArabic = "/Queue_Voice Files/Arabic Voice/Male Voice"
            maleEnglish = "/Queue_Voice Files/English Voice/Male Voice"
            if (En_Ar_VOICE == 0) {
                if (FEMALE_VOICE != 0 && ARABIC_FONT != 0) { // Female Arabic
                    // voice
                    MEDIA_PATH = java.lang.String(femaleArabic) as String
                } else if (FEMALE_VOICE == 0 && ARABIC_FONT != 0) { // Male
                    // Arabic
                    // voice
                    MEDIA_PATH = java.lang.String(maleArabic) as String
                } else if (FEMALE_VOICE != 0 && ARABIC_FONT == 0) { // Female
                    // English
                    // voice
                    MEDIA_PATH = java.lang.String(femaleEnglish) as String
                } else if (FEMALE_VOICE == 0 && ARABIC_FONT == 0) { // Male
                    // English
                    // voice
                    MEDIA_PATH = java.lang.String(maleEnglish) as String
                }

                mediaFile = File(MainActivity.Companion.sdcardPath, MEDIA_PATH)
                // }
            } else {
                if (FEMALE_VOICE != 0 && ARABIC_FONT != 0) { // Both Ar_En
                    // Female
                    // voice
                    MEDIA_PATH = java.lang.String(femaleArabic) as String
                    MEDIA_PATH_Both = java.lang.String(femaleEnglish) as String
                    ArEn_Font = true
                } else if (FEMALE_VOICE == 0 && ARABIC_FONT != 0) { // Both
                    // Ar_En
                    // Male
                    // voice
                    MEDIA_PATH = java.lang.String(maleArabic) as String
                    MEDIA_PATH_Both = java.lang.String(maleEnglish) as String
                    ArEn_Font = true
                } else if (FEMALE_VOICE != 0 && ARABIC_FONT == 0) { // Both
                    // En_Ar
                    // Female
                    // voice
                    MEDIA_PATH = java.lang.String(femaleEnglish) as String
                    MEDIA_PATH_Both = java.lang.String(femaleArabic) as String
                    ArEn_Font = false
                } else if (FEMALE_VOICE == 0 && ARABIC_FONT == 0) { // Both
                    // En_Ar
                    // Male
                    // voice
                    MEDIA_PATH = java.lang.String(maleEnglish) as String
                    MEDIA_PATH_Both = java.lang.String(maleArabic) as String
                    ArEn_Font = false
                }
                mediaFile_En = File(MainActivity.sdcardPath, MEDIA_PATH)
                mediaFile_Ar = File(
                    MainActivity.sdcardPath,
                    MEDIA_PATH_Both
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Conversion of token into voice.
    fun voiceConversion(): ArrayList<String>? {
        try {
            mArrayList.clear()

            if (PLAY_VOICE_FILES != 0) {
                mArrayList.add(getAudioFile("/DinDong"))

                val vToken = this.token!!.toInt()
                val hundred = vToken / 100
                val tenth_2 = vToken % 100

                if (SPEAK_TOKEN_No != 0) {
                    mArrayList.add(getAudioFile("/NUMBER"))
                }
                if (hundred != 0) {
                    when (hundred) {
                        1 -> if (ARABIC_FONT != 0) {
                            mArrayList.add(getAudioFile("/N100"))
                        } else {
                            mArrayList.add(getAudioFile("/N01"))
                            mArrayList.add(getAudioFile("/N100"))
                        }

                        2 -> mArrayList.add(getAudioFile("/N200"))
                        3 -> mArrayList.add(getAudioFile("/N300"))
                        4 -> mArrayList.add(getAudioFile("/N400"))
                        5 -> mArrayList.add(getAudioFile("/N500"))
                        6 -> mArrayList.add(getAudioFile("/N600"))
                        7 -> mArrayList.add(getAudioFile("/N700"))
                        8 -> mArrayList.add(getAudioFile("/N800"))
                        9 -> mArrayList.add(getAudioFile("/N900"))
                    }
                    if (tenth_2 != 0) {
                        if (ARABIC_FONT != 0) {
                            arabicVoice(tenth_2)
                        } else {
                            tenthVoice(tenth_2)
                        }
                    }
                } else {
                    tenthVoice(tenth_2)
                }
            }
            return mArrayList
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun getAudioFile(fileName: String): String {
        var path = ""
        val typeFolder: String
        try {
            typeFolder = mAudioFileExtenstion.substring(1)
            val f1 = File(mediaFile!!.path + fileName + mAudioFileExtenstion)
            path = if (f1.exists()) {
                mediaFile!!.path + fileName + mAudioFileExtenstion
            } else {
                mediaFile!!.path + "/" + typeFolder + fileName + mAudioFileExtenstion
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return path
    }

    private fun getArabicAudioFile(fileName: String): String {
        var path = ""
        val typeFolder: String
        try {
            mediaFile = mediaFile_Ar
            typeFolder = mAudioFileExtenstion.substring(1)
            val f1 = File(mediaFile!!.path + fileName + mAudioFileExtenstion)
            path = if (f1.exists()) {
                mediaFile!!.path + fileName + mAudioFileExtenstion
            } else {
                mediaFile!!.path + "/" + typeFolder + fileName + mAudioFileExtenstion
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return path
    }


    // Conversion of token into both english & arabic voice.
    fun enArVoiceConv(flg: Boolean): ArrayList<String>? {
        try {
            val vToken = this.token!!.toInt()
            val hundred = vToken / 100
            val tenth_2 = vToken % 100
            if (flg == true) {
                mArrayList.clear()
            }
            //playAr = true;
            mediaFile = mediaFile_En
            voiceConversion()
            mediaFile = mediaFile_Ar

            if (PLAY_VOICE_FILES != 0) {
                if (SPEAK_TOKEN_No != 0) {
                    mArrayList.add(getArabicAudioFile("/NUMBER"))
                }
                if (hundred != 0) {
                    when (hundred) {
                        1 -> if (ARABIC_FONT != 0) {
                            mArrayList.add(getArabicAudioFile("/N100"))
                        } else {
                            mArrayList.add(getArabicAudioFile("/N01"))
                            mArrayList.add(getArabicAudioFile("/N100"))
                        }

                        2 -> mArrayList.add(getArabicAudioFile("/N200"))
                        3 -> mArrayList.add(getArabicAudioFile("/N300"))
                        4 -> mArrayList.add(getArabicAudioFile("/N400"))
                        5 -> mArrayList.add(getArabicAudioFile("/N500"))
                        6 -> mArrayList.add(getArabicAudioFile("/N600"))
                        7 -> mArrayList.add(getArabicAudioFile("/N700"))
                        8 -> mArrayList.add(getArabicAudioFile("/N800"))
                        9 -> mArrayList.add(getArabicAudioFile("/N900"))
                    }
                    if (tenth_2 != 0) {
                        if (ARABIC_FONT != 0) {
                            arabicVoice(tenth_2)
                        } else {
                            tenthVoice(tenth_2)
                        }
                    }
                } else {
                    tenthVoice(tenth_2)
                }
            }
            // }
            return mArrayList
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    // Arabic voice-greater than hundred
    fun arabicVoice(num: Int) {
        try {
            var s0 = num.toString()
            s0 = if (num < 10) {
                "NN0$s0"
            } else {
                "NN$s0"
            }
            //String s0_path = mediaFile.getPath() + "/" + s0 + mAudioFileExtenstion;
            val s0_path = getAudioFile("/$s0")
            mArrayList.add(s0_path)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // English + arabic voice
    fun tenthVoice(num: Int) {
        try {
            var s0 = num.toString()
            s0 = if (num < 10) {
                "N0$s0"
            } else {
                "N$s0"
            }
            //String s0_path = mediaFile.getPath() + "/" + s0 + mAudioFileExtenstion;
            val s0_path = getAudioFile("/$s0")
            mArrayList.add(s0_path)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Play voice of token.
    fun playVoice(index: Int) {
        if (currentSongIndex < mArrayList.size) {
            try {
                mPlayer!!.reset()
                mPlayer!!.setDataSource(mArrayList[index])
                mPlayer!!.prepare()

                mPlayer!!.setOnCompletionListener { player ->
                    player.stop()
                    // play next audio file
                    if (currentSongIndex < (mArrayList.size - 1)) {
                        playVoice(currentSongIndex + 1)
                        currentSongIndex = currentSongIndex + 1
                    } else {

                    }
                }
                // nirmal 11 june 2014 -- created onpreparedListener
                mPlayer!!.setOnPreparedListener {
                    // TODO Auto-generated method stub
                    //  if (mRingBell) {
                    mPlayer!!.start()
                    // }
                }
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (exc: Exception) {
            }
        }
    }

    // token blinking animation
    private fun tokenAnimation() {
        try {

            val anim = ValueAnimator.ofInt(mTokenColor, mTokenBlinkColor)

            anim.setDuration(600)
            anim.startDelay = 100
            anim.repeatCount = 4
            //        lastLedColor=
            anim.addListener(object : AnimatorListener {

                var TOKEN_ORIGINAL_COLOR: Boolean = true

                override fun onAnimationStart(animation: Animator) {
                    // tokenTouchView.setTextColor(Color.BLUE);
                    // RED_TEXT = false;
                    tokenTouchView!!.setTextColor(mTokenBlinkColor)
                    ledOnGreen(100)
                    // RED_TEXT = false;
                    TOKEN_ORIGINAL_COLOR = false
                }

                override fun onAnimationRepeat(animation: Animator) {

                    if (TOKEN_ORIGINAL_COLOR) {
                        tokenTouchView!!.setTextColor(mTokenBlinkColor)
                        ledOnGreen(100)
                        TOKEN_ORIGINAL_COLOR = false
                    } else {
                        tokenTouchView!!.setTextColor(mTokenColor)
                        ledOnBlue(100)
                        TOKEN_ORIGINAL_COLOR = true
                    }
                }

                override fun onAnimationEnd(animation: Animator) {
                    // tokenTouchView.setTextColor(Color.RED);
                    // nirmal 07 oct 2014
                    tokenTouchView!!.setTextColor(mTokenColor)
                    TOKEN_ORIGINAL_COLOR = true
                    when (lastLedColor) {
                        1 -> ledOnRed(100)
                        2 -> ledOnGreen(100)
                        else -> ledOnBlue(100)
                    }
                }

                override fun onAnimationCancel(animation: Animator) {
                    // TODO Auto-generated method stub
                    tokenTouchView!!.setTextColor(mTokenColor)
                    ledOnBlue(100)
                    TOKEN_ORIGINAL_COLOR = true
                }
            })
            anim.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    var token: String?
        // get token number
        get() = Companion.token
        // set token number
        set(value) {
            Companion.token = value
        }

    override fun onStop() {
        // TODO Auto-generated method stub
        mPlayer!!.stop()
        super.onStop()
    }

    override fun onPause() {
        // TODO Auto-generated method stub
        super.onPause()
        stopReadTask = true
    }

    fun ledOnRed(red_bright: Int) {
        try {
            // jnielc.ledmix(fb);
            jnielc.seekstart()
            jnielc.ledseek(seek_red, red_bright)
            jnielc.seekstop()
            val intent = Intent("android.intent.action.ledctl")
            intent.putExtra("led", led_red)
            intent.putExtra("ledbrightness", red_bright)
            activity.sendBroadcast(intent)
            Log.d("count", "LED red On")
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    fun ledOnGreen(green_bright: Int) {
        try {
            // jnielc.ledmix(fb);
            jnielc.seekstart()
            jnielc.ledseek(seek_green, green_bright)
            jnielc.seekstop()
            val intent = Intent("android.intent.action.ledctl")
            intent.putExtra("led", led_green)
            intent.putExtra("ledbrightness", green_bright)
            activity.sendBroadcast(intent)
            Log.d("count", "LED green On")
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    fun ledOnBlue(blue_bright: Int) {
        try {
            // jnielc.ledmix(fb);
            jnielc.seekstart()
            jnielc.ledseek(seek_blue, blue_bright)
            jnielc.seekstop()
            val intent = Intent("android.intent.action.ledctl")
            intent.putExtra("led", led_blue)
            intent.putExtra("ledbrightness", blue_bright)
            activity.sendBroadcast(intent)
            Log.d("count", "LED blue On")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun startHandlerReadServerData() {

        mHandler_readServerData!!.postDelayed({ //  if (!isReadTaskBusy) {
            readDataFromServer()

            // }
            if (!stopReadTask) {
                startHandlerReadServerData()
            }
            if (AppConstantsFlags.SHOW_SERVICE_NAME_TOP || AppConstantsFlags.SHOW_SERVICE_NAME
            ) {
                /*readAdvImageFile();
                        updateUI();*/
                setupUI()
            }
        }, AppConstantsFlags.SOCKET_HANDLER_DELAY.toLong())
    }

    private fun readDataFromServer() {
        AsyncTaskReadServerData(activity).execute()
    }

    fun readAdvImageFile() {
        try {
            val listFile: Array<File>
            image_FileNames = null
            image_FilePaths = null

            val file = File(
                Environment.getExternalStorageDirectory()
                    .toString() + "/Queue_Config Files/Token images"
            )
            if (file.isDirectory) {
                listFile = file.listFiles()
                // List file path for Images
                image_FilePaths = arrayOfNulls(listFile.size)
                // List file path for Image file names
                image_FileNames = arrayOfNulls(listFile.size)

                for (i in listFile.indices) {
                    // Get the path
                    image_FilePaths!![i] = listFile[i].absolutePath
                    // Get the name
                    image_FileNames!![i] = listFile[i].name
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    internal inner class AsyncTaskReadServerData(var callerActivityContext: Activity) :
        AsyncTask<String?, Void?, String?>() {
            override fun doInBackground(vararg params: String?): String? {
            try {
                getTokenJsonResponse = getTokenNumber(
                    MainActivity.Companion.sharePrefSettings!!.getString(
                        "CounterNo", ""
                    ), "getTokenDataForCounter"
                )
                println("here is getTokenJsonResponse:::::::::::: ::: $getTokenJsonResponse")
            } catch (e: Exception) {
            }
            return getTokenJsonResponse
        }

        override fun onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute()
        }

        override fun onPostExecute(result: String?) {
            if (connected) {
                txtConnectivityView!!.setBackgroundColor(Color.GREEN)
            } else {
                txtConnectivityView!!.setBackgroundColor(Color.RED)
            }
            try {
                val jsonObject = JSONObject(result)
                val token = jsonObject.getString("tno")
                val time = jsonObject.getString("dispServerTime")
                val serviceNameEng = jsonObject.getString("ServiceName_En")
                val serviceNameAr = jsonObject.getString("ServiceName_Ar")
                val username = jsonObject.getString("UserName")
                val second = jsonObject.getString("sec").toInt()
                val innerJsonObject = jsonObject.getJSONObject("returnStatus")
                val success = innerJsonObject.getBoolean("success")
                val reason = innerJsonObject.getString("reason")

                if (success) {
                    usernameDisplay = username
                    serviceDisplay = serviceNameEng
                    println("here is usernameDisplay 111 $usernameDisplay")
                    playTokenVoices(token, second, time, serviceNameEng, serviceNameAr, username)
                } else {
                    txtConnectivityView!!.setBackgroundColor(Color.parseColor("#FFA500"))
                }
            } catch (e: Exception) {
                connected = false
                e.printStackTrace()
            }
        }
    }

    inner class GetBranchName : AsyncTask<String?, Void?, String?>() {
       override fun doInBackground(vararg params: String?): String? {
            try {
                getBranchJsonResponse = getBranchName("HO", "GetBranchDetail")
                println("here is getBranchJsonResponse:::::::::::: ::: $getBranchJsonResponse")
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return getBranchJsonResponse
        }

        override fun onPostExecute(result: String?) {
            try {
                val jsonObject = JSONObject(result)
                val innerJsonObject = jsonObject.getJSONObject("returnStatus")
                val success = innerJsonObject.getBoolean("success")
                val reason = innerJsonObject.getString("reason")
                if (success) {
                    branchDisplay = jsonObject.getString("NameEN")
                    val isUserName: Boolean = MainActivity.Companion.sharePrefSettings!!.getString(
                        AppConstantsFlags.key_pref_user_top,
                        ""
                    ).toBoolean()
                    val isService: Boolean = MainActivity.Companion.sharePrefSettings!!.getString(
                        AppConstantsFlags.key_pref_service_top,
                        ""
                    ).toBoolean()
                    val isBranch: Boolean = MainActivity.Companion.sharePrefSettings!!.getString(
                        AppConstantsFlags.key_pref_branch_top,
                        ""
                    ).toBoolean()

                    println("here is isUserName $isUserName")
                    println("here is isService $isService")
                    if (isUserName) {
                        println("here is usernameDisplay $usernameDisplay")
                        tvBranchName!!.text = usernameDisplay
                    } else if (isService) {
                        tvBranchName!!.text = serviceDisplay
                    } else if (isBranch) {
                        if (ARABIC_FONT == 1) tvBranchName!!.text =
                            "" + jsonObject.getString("NameAr")
                        else tvBranchName!!.text = "" + jsonObject.getString("NameEN")
                    } else {
                        tvBranchName!!.text = ""
                    }
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        private const val seek_red = 0xa1
        private const val seek_green = 0xa2
        private const val seek_blue = 0xa3
        private const val led_red = 2
        private const val led_blue = 3
        private const val led_green = 4
        var token: String? = null
        var prevToken: String = ""
        var NO_OF_DIGIT: Int = 0
        var ARABIC_FONT: Int = 0
        var WINDOW_DISPLAY: Int = 0
        var currentSongIndex: Int = 0

        //   public static int QUEUE_SERVER_PORT;
        // private int mTokenColor, mTokenBlinkColor;
        var mTokenColor: Int = 0
        var mTokenBlinkColor: Int = 0
        var connected: Boolean = true
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

    override fun onConnectionBroken(message: String) {
        txtConnectivityView!!.setBackgroundColor(Color.RED)
    }

    override fun onConnectionRestarted() {
        txtConnectivityView!!.setBackgroundColor(Color.GREEN)
    }
}
