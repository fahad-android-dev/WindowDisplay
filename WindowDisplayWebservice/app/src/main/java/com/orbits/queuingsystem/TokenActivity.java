package com.orbits.queuingsystem;

import static com.orbits.queuingsystem.MainActivity.digitMap;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elcapi.jnielc;
import com.orbits.queuingsystem.webservice.BranchWebService;
import com.orbits.queuingsystem.webservice.WebService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class TokenActivity extends Fragment {
    private static final int seek_red = 0xa1;
    private static final int seek_green = 0xa2;
    private static final int seek_blue = 0xa3;
    private static final int led_red = 2;
    private static final int led_blue = 3;
    private static final int led_green = 4;
    public static String token;
    public static String prevToken = "";
    public static int NO_OF_DIGIT;
    public static int ARABIC_FONT = 0;
    public static int WINDOW_DISPLAY = 0;
    public static int currentSongIndex = 0;
    //   public static int QUEUE_SERVER_PORT;
    // private int mTokenColor, mTokenBlinkColor;
    static int mTokenColor, mTokenBlinkColor;
    public static boolean connected = true;
    // public static boolean callInSec = false;
    //  private static long currDate;
    //  private static long oldDate;
    // private static long lastSocketDataTime;
    // public int ENGLISH_FONT = 0;
    public int FEMALE_VOICE = 0;
    public int En_Ar_VOICE = 0;
    public int SPEAK_TOKEN_No = 0;
    public int PLAY_VOICE_FILES = 0;
    public File mediaFile; // universal media file path
    public File mediaFile_En; // media file path 1 in both voices
    public File mediaFile_Ar; // media file path 2 in both voices
    public MediaPlayer mPlayer;
    public boolean playAr;
    public boolean ArEn_Font;
    View tknView;
    String tokenLength;
    int lastLedColor;
    int fb;
    String arabicFont;
    String femaleVoice;
    String enAr_Voice;
    String speakToken;
    String playVoices;
    String windowDisplay;
    // Touch listener on Token
    OnTouchListener tokenOnTouchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if (event.getAction() == MotionEvent.ACTION_UP) {
                final ActionBar actionBar = getActivity().getActionBar();
                // nirmal 07 july 2014 -- hide the action-bar
                if (actionBar != null) {
                    if (actionBar.isShowing()) {
                        actionBar.hide();
                    } else {
                        actionBar.show();
                    }
                }
            }

            return true;
        }
    };
    Utils utils;
    // int isLoggedIn;
    private TextView tokenTouchView, txtConnectivityView, txtTime;
    TextView tvServiceName, tvBranchName;
    RelativeLayout layoutBranchName;
    private ArrayList<String> mArrayList = new ArrayList<String>();
    // private Handler tokenHandler;
    // public static int tokenHandlerDelay;
    // public Socket client;
    // public static boolean dbFlag;
    //  private int COUNTER_NO;
    private LinearLayout mTokenLayoutContainer;
    // private boolean isReadTaskBusy = false;
    private String getTokenJsonResponse, getBranchJsonResponse;
    private boolean stopReadTask = false;
    private Handler mHandler_readServerData = null;
    // private Handler mHandlerSendBeat = null;
    //private static Socket sock = null;
    //  private InputStream inStream = null;
    // private static PrintWriter dataOutStream = null;
    // private static String mProtocolIdentifySelf = null;
    // private static String mProtocolSendBeat = null;
    // private boolean mRingBell = true;///manju 29/12/2021
    // private byte mConnectionCheckCounter;
    private String mAudioFileExtenstion = ".wav";

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (AppConstantsFlags.SHOW_BRANCH_NAME && AppConstantsFlags.SHOW_SERVICE_NAME) {
            tknView = inflater.inflate(R.layout.activity_token_service_branch_name, container, false);
            tvServiceName = tknView.findViewById(R.id.txtServiceName);
            tvBranchName = tknView.findViewById(R.id.txtBranchName);
            layoutBranchName = tknView.findViewById(R.id.layoutBranchName);

        } else {
            tknView = inflater.inflate(R.layout.activity_token, container, false);
        }
        tokenTouchView = (TextView) tknView.findViewById(R.id.txtTokenNo);

        txtConnectivityView = (TextView) tknView.findViewById(R.id.txtconnectivity);
        txtTime = (TextView) tknView.findViewById(R.id.txtTime);
        utils = new Utils();
        tokenTouchView.setPaintFlags(tokenTouchView.getPaintFlags()
                | Paint.FAKE_BOLD_TEXT_FLAG);
        tokenTouchView.setOnTouchListener(tokenOnTouchListener);
        try {
            tokenLength = MainActivity.sharePrefSettings
                    .getString("tokenLength", "3");
            NO_OF_DIGIT = Integer.parseInt(tokenLength);
            if (tokenLength.equals("4")) {
                tokenTouchView.setText("0000");
            } else {
                tokenTouchView.setText("000");
            }
            Typeface fnt = Typeface.createFromAsset(getActivity().getAssets(),
                    "ARIAL.TTF");
            tokenTouchView.setTypeface(fnt);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mPlayer = new MediaPlayer();
        mTokenLayoutContainer = (LinearLayout) tknView
                .findViewById(R.id.rightLayout);

        ///setMediaPath();   ///manjusha 08/01/2019

        return tknView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
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
        mHandler_readServerData = new Handler();
        //   mHandlerSendBeat = new Handler();
        // startBeatTime();
        //checkSocketConnectionTimer();
        mTokenColor = Color.RED;
        mTokenBlinkColor = Color.BLUE;
    }

    @Override
    public void onStart() {
        super.onStart();

        try {

            arabicFont = digitMap.get("Arabic_Font");
            femaleVoice = digitMap.get("Female_Voice");
            enAr_Voice = digitMap.get("English + Arabic Voice");
            speakToken = digitMap.get("Token Voice");
            playVoices = digitMap.get("Play Voice files");
            windowDisplay = digitMap.get("Window Display");

            if (arabicFont == null
                    || femaleVoice == null || enAr_Voice == null
                    || speakToken == null || playVoices == null
                    || windowDisplay == null) {
                // totalDigits = "3";
                arabicFont = "1";
                femaleVoice = "1";
                enAr_Voice = "0";
                speakToken = "0";
                playVoices = "0";
                windowDisplay = "0";
            }

            FEMALE_VOICE = Integer.parseInt(femaleVoice);
            ARABIC_FONT = Integer.parseInt(arabicFont);
            En_Ar_VOICE = Integer.parseInt(enAr_Voice);
            SPEAK_TOKEN_No = Integer.parseInt(speakToken);
            PLAY_VOICE_FILES = Integer.parseInt(playVoices);
            WINDOW_DISPLAY = Integer.parseInt(windowDisplay);

            //// set here due to issue in en_ar voice conversion/// manjusha 08/01/2019
            setMediaPath();

            stopReadTask = false;
            start_handler_readServerData();
            mAudioFileExtenstion = digitMap
                    .get(AppConstantsFlags.KEY_DIGIT_MAP_AUDIO_FILE_EXTENSION);

            //totalDigits = MainActivity.digitMap.get("Token_Digits");

            // nirmal 1 oct 2014 -- set different text-sizes according to
            // settings
            if (ARABIC_FONT == 1) {
                if (!(AppConstantsFlags.SHOW_ADVERTIZEMENT.equals("Show Nothing"))) {
                    tokenTouchView
                            .setTextSize(Integer.parseInt(digitMap
                                    .get(AppConstantsFlags.KEY_DIGIT_MAP_ARB_FONT_HALF_SCREEN)));
                } else {
                    tokenTouchView
                            .setTextSize(Integer.parseInt(digitMap
                                    .get(AppConstantsFlags.KEY_DIGIT_MAP_ARB_FONT_FULL_SCREEN)));
                }
            } else {
                if (!(AppConstantsFlags.SHOW_ADVERTIZEMENT.equals("Show Nothing"))) {
                    tokenTouchView
                            .setTextSize(Integer.parseInt(digitMap
                                    .get(AppConstantsFlags.KEY_DIGIT_MAP_ENG_FONT_HALF_SCREEN)));
                } else {
                    tokenTouchView
                            .setTextSize(Integer.parseInt(digitMap
                                    .get(AppConstantsFlags.KEY_DIGIT_MAP_ENG_FONT_FULL_SCREEN)));
                }
            }

            try {
                // nirmal 08 oct 2014 -- set padding for token-text-view
                tokenTouchView
                        .setPadding(
                                0,
                                Integer.parseInt(digitMap
                                        .get(AppConstantsFlags.KEY_DIGIT_MAP_TOKEN_PADDING_TOP)),
                                0,
                                Integer.parseInt(digitMap
                                        .get(AppConstantsFlags.KEY_DIGIT_MAP_TOKEN_PADDING_BOTTOM)));
            } catch (Exception e) {
                Toast.makeText(getActivity(),
                        "Some error occurred, Please check padding values.",
                        Toast.LENGTH_LONG).show();
            }
            boolean USE_BG_IMG = false;
            try {
                // nirmal 07 oct 2014 -- set backgroud color as mentioned in
                // excel file
                /*USE_BG_IMG = true;
                mTokenLayoutContainer.setBackgroundColor(Color.BLACK);*/
                // int bgColor = Color.parseColor("#0c543c");
                mTokenLayoutContainer.setBackgroundColor(Color.parseColor(digitMap
                        .get(AppConstantsFlags.KEY_DIGIT_MAP_BG_COLOR)));
            } catch (Exception e) {
                // nirmal 07 oct 2014 -- use background image if invalid color
                // is entered
                USE_BG_IMG = true;
                mTokenLayoutContainer.setBackgroundColor(Color.BLACK);
            }
            if (USE_BG_IMG) {
                try {
                    // nirmal 07 oct 2014 -- set backgroud image
                    File bgImgFile = new File(
                            Environment.getExternalStorageDirectory()
                                    + "/Queue_Config Files/bgimg.png");
                    if (bgImgFile.exists()) {
                        mTokenLayoutContainer.setBackground(Drawable
                                .createFromPath(bgImgFile.getAbsolutePath()));
                    }
                } catch (Exception e) {
                    Toast.makeText(
                            getActivity(),
                            "Some error occurred, Please check bgimg.png file.",
                            Toast.LENGTH_LONG).show();
                }
            }
            try {
                // nirmal 07 oct 2014 -- set token color as mentioned in
                // excel file
                mTokenColor = Color.parseColor(digitMap
                        .get(AppConstantsFlags.KEY_DIGIT_MAP_TOKEN_COLOR));
            } catch (Exception e) {
                mTokenColor = Color.RED;
                Toast.makeText(getActivity(),
                        "Some error occurred, Please check token color value.",
                        Toast.LENGTH_LONG).show();
            }
            try {
                // nirmal 07 oct 2014 -- set token blink color as mentioned in
                // excel file
                mTokenBlinkColor = Color
                        .parseColor(digitMap
                                .get(AppConstantsFlags.KEY_DIGIT_MAP_TOKEN_BLINK_COLOR));
            } catch (Exception e) {
                mTokenBlinkColor = Color.BLUE;
                Toast.makeText(
                        getActivity(),
                        "Some error occurred, Please check token blink color value.",
                        Toast.LENGTH_LONG).show();
            }

        } catch (NullPointerException e) {
            // Log.e("awd :: ", "Null pointer exception");
        } catch (Exception e) {
            // Log.e("awd :: ", "exception");
        }
        if (AppConstantsFlags.SHOW_BRANCH_NAME && AppConstantsFlags.SHOW_SERVICE_NAME) {
            setupUI();
        }
        if (AppConstantsFlags.SHOW_TIME) {
            txtTime.setVisibility(View.VISIBLE);
        } else {
            txtTime.setVisibility(View.GONE);
        }
    }

    private void setupUI() {

        new GetBranchName().execute();
        try {
            String serviceTextSize = digitMap.get(AppConstantsFlags.KEY_DIGIT_MAP_SERVICE_NAME_FONT_SIZE);
            if (serviceTextSize != null) {
                tvServiceName.setTextSize(Integer.parseInt(serviceTextSize));
                tvServiceName.setTextColor(Color.parseColor(digitMap
                        .get(AppConstantsFlags.KEY_DIGIT_MAP_SERVICE_NAME_TEXT_COLOR)));
            }
            String branchTextSize = digitMap.get(AppConstantsFlags.KEY_DIGIT_MAP_BRANCH_NAME_FONT_SIZE);
            if (branchTextSize != null) {
                tvBranchName.setTextSize(Integer.parseInt(branchTextSize));
                tvBranchName.setTextColor(Color.parseColor(digitMap
                        .get(AppConstantsFlags.KEY_DIGIT_MAP_BRANCH_NAME_TEXT_COLOR)));
                layoutBranchName.setBackgroundColor(Color.parseColor(digitMap
                        .get(AppConstantsFlags.KEY_DIGIT_MAP_BRANCH_NAME_BG_COLOR)));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

    }

//    public void showLogoutLogIn() {
//        try {
//            if (isLoggedIn == 2) {
//                //// make textsize half of font size given in excel /////
//                if (!(AppConstantsFlags.SHOW_ADVERTIZEMENT.equals("Show Nothing"))) {
//                    int halfTextSize = Integer.parseInt(MainActivity.digitMap.get(AppConstantsFlags.KEY_DIGIT_MAP_ENG_FONT_HALF_SCREEN));
//                    int finalhalfLength = halfTextSize / 2;
//                    tokenTouchView.setTextSize(Math.round(finalhalfLength));
//                } else {
//                    int fullTextSize = Integer.parseInt(MainActivity.digitMap.get(AppConstantsFlags.KEY_DIGIT_MAP_ENG_FONT_FULL_SCREEN));
//                    int finalLength = fullTextSize / 2;
//                    tokenTouchView.setTextSize(Math.round(finalLength));
//                }
//                tokenTouchView.setText("Free");   //// when counter is free and logged in////
//                ledOnGreen(100);
//            } else if (isLoggedIn == 1) {
//                //// make textsize half of font size given in excel /////
//                if (!(AppConstantsFlags.SHOW_ADVERTIZEMENT.equals("Show Nothing"))) {
//                    int halfTextSize = Integer.parseInt(MainActivity.digitMap.get(AppConstantsFlags.KEY_DIGIT_MAP_ENG_FONT_HALF_SCREEN));
//                    int finalhalfLength = halfTextSize / 2;
//                    tokenTouchView.setTextSize(Math.round(finalhalfLength));
//                } else {
//                    int fullTextSize = Integer.parseInt(MainActivity.digitMap.get(AppConstantsFlags.KEY_DIGIT_MAP_ENG_FONT_FULL_SCREEN));
//                    int finalLength = fullTextSize / 2;
//                    tokenTouchView.setTextSize(Math.round(finalLength));
//                }
//                tokenTouchView.setText("Closed");//// when counter is closed or logged out////
//                ledOnRed(100);
//            } else if (isLoggedIn == 3) {  /// when token is running///
//                ledOnBlue(100);
//                //Log.d("blue color:::token", "" + isLoggedIn);
//            }
//            lastLedColor = isLoggedIn;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void playTokenVoices(String strToken, int second, String time, String serviceNameEng, String serviceNameAr) {
        try {
            if (ARABIC_FONT == 1) {
                tvServiceName.setText("" + serviceNameAr);
            } else {
                tvServiceName.setText("" + serviceNameEng);
            }
            txtTime.setText("" + time);////2/7/2022
            String timeTextSize = digitMap.get(AppConstantsFlags.KEY_DIGIT_MAP_TIME_FONT_SIZE);
            if (timeTextSize != null) {
                txtTime.setTextSize(Integer.parseInt(timeTextSize));
            }
            txtTime.setTextColor(Color.parseColor(digitMap.get(AppConstantsFlags.KEY_DIGIT_MAP_TIME_TEXT_COLOR)));
            if (strToken.equalsIgnoreCase("Free")) {
                //// make textsize half of font size given in excel /////
                if (!(AppConstantsFlags.SHOW_ADVERTIZEMENT.equals("Show Nothing"))) {
                    int halfTextSize = Integer.parseInt(digitMap.get(AppConstantsFlags.KEY_DIGIT_MAP_ENG_FONT_HALF_SCREEN));
                    int finalhalfLength = halfTextSize / 2;
                    tokenTouchView
                            .setTextSize(Math.round(finalhalfLength));
                } else {
                    int fullTextSize = Integer.parseInt(digitMap.get(AppConstantsFlags.KEY_DIGIT_MAP_ENG_FONT_FULL_SCREEN));
                    int finalLength = fullTextSize / 2;
                    tokenTouchView.setTextSize(Math.round(finalLength));
                }
                tokenTouchView.setText("Free");
                ledOnGreen(100);
                lastLedColor = 2;
                // Log.d("000:::free", strToken);
            } else if (strToken.equalsIgnoreCase("Closed")) {
                if (!(AppConstantsFlags.SHOW_ADVERTIZEMENT.equals("Show Nothing"))) {
                    int halfTextSize = Integer.parseInt(digitMap.get(AppConstantsFlags.KEY_DIGIT_MAP_ENG_FONT_HALF_SCREEN));
                    int finalhalfLength = halfTextSize / 2;
                    tokenTouchView.setTextSize(Math.round(finalhalfLength));
                } else {
                    int fullTextSize = Integer.parseInt(digitMap.get(AppConstantsFlags.KEY_DIGIT_MAP_ENG_FONT_FULL_SCREEN));
                    int finalLength = fullTextSize / 2;
                    tokenTouchView.setTextSize(Math.round(finalLength));
                }
                tokenTouchView.setText("Closed");//// when counter is closed or logged out////
                ledOnRed(100);
            } else {
                if (ARABIC_FONT == 1) {
                    if (!(AppConstantsFlags.SHOW_ADVERTIZEMENT.equals("Show Nothing"))) {
                        tokenTouchView
                                .setTextSize(Integer.parseInt(digitMap
                                        .get(AppConstantsFlags.KEY_DIGIT_MAP_ARB_FONT_HALF_SCREEN)));
                    } else {
                        tokenTouchView
                                .setTextSize(Integer.parseInt(digitMap
                                        .get(AppConstantsFlags.KEY_DIGIT_MAP_ARB_FONT_FULL_SCREEN)));
                    }
                } else {
                    if (!(AppConstantsFlags.SHOW_ADVERTIZEMENT.equals("Show Nothing"))) {
                        tokenTouchView
                                .setTextSize(Integer.parseInt(digitMap
                                        .get(AppConstantsFlags.KEY_DIGIT_MAP_ENG_FONT_HALF_SCREEN)));
                    } else {
                        tokenTouchView
                                .setTextSize(Integer.parseInt(digitMap
                                        .get(AppConstantsFlags.KEY_DIGIT_MAP_ENG_FONT_FULL_SCREEN)));
                    }
                }
                //// manjusha 30/dec/2019////
                // int prefixLength = Integer.parseInt(MainActivity.sharePrefSettings
                //     .getString("prefix", ""));
                // if (prefixLength != 0) {
                //   token = Integer.parseInt(strToken.substring(strToken.length() - prefixLength));
                // } else {
                token = strToken;
                // }


                String formattedToken = "";
                if (ARABIC_FONT != 0) {
                    for (int i = 0; i < strToken.length(); i++) {
                        int s = (int) strToken.charAt(i);
                        if (s >= 48 && s <= 57) {
                            formattedToken += CommonLogic.englishToArabicNumber(Integer.parseInt(strToken.substring(i, i + 1)), 1);
                        } else {
                            formattedToken += strToken.charAt(i);
                        }
                    }
                } else {
                    formattedToken = strToken;
                }
                //// manjusha 30/dec/2019////
                tokenTouchView.setText(formattedToken);


                // Log.e("response::", "" + count);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Typeface fnt = Typeface.createFromAsset(getActivity().getAssets(),
        // "ARIAL.TTF");
        // tokenTouchView.setTypeface(fnt);
        // nirmal 11 june 2014 -- if not playing both voices, then call the
        // method voiceConversion
        // else call the method enArVoiceConv
        if (!strToken.equalsIgnoreCase(prevToken) && second < 3 && !(strToken.equalsIgnoreCase("Closed")) && !(strToken.equalsIgnoreCase("Free"))) {
            tokenAnimation();
            try {
                if (En_Ar_VOICE == 0) {
                    voiceConversion();
                } else {
                    enArVoiceConv(true);
                }
                // voiceConversion();
                currentSongIndex = 0;
                playVoice(currentSongIndex);

            } catch (Exception e) {
                e.printStackTrace();
            }
            prevToken = strToken;
        } else if (second >= 3) {
            prevToken = "";
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        // Typeface fnt = Typeface.createFromAsset(getActivity().getAssets(),
        // "ARIAL.TTF");
        // tokenTouchView.setTypeface(fnt);
        try {
            stopReadTask = false;
            start_handler_readServerData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*
         * nirmal 28 oct 2014 -- token number is set in the asyncTask in
         * mainActivity, hence no need to set here.
         *
         * if (MainActivity.con.isConnectionClosed()) { if
         * (MainActivity.con.setConnection()) { tokenTouchView.setText("---"); }
         * else { tokenTouchView.setText("NA"); } }
         */
    }

    // set Voice path
    public void setMediaPath() {
        String MEDIA_PATH = "";
        String MEDIA_PATH_Both = "";
        String femaleArabic, femaleEnglish, maleArabic, maleEnglish;

        // Toast.makeText(getActivity(), "::" + typeFolder, Toast.LENGTH_SHORT).show();
        try {

            femaleArabic = "/Queue_Voice Files/Arabic Voice/Female Voice";
            femaleEnglish = "/Queue_Voice Files/English Voice/Female Voice";
            maleArabic = "/Queue_Voice Files/Arabic Voice/Male Voice";
            maleEnglish = "/Queue_Voice Files/English Voice/Male Voice";
            if (En_Ar_VOICE == 0) {
                if (FEMALE_VOICE != 0 && ARABIC_FONT != 0) { // Female Arabic
                    // voice
                    MEDIA_PATH = new String(femaleArabic);
                } else if (FEMALE_VOICE == 0 && ARABIC_FONT != 0) { // Male
                    // Arabic
                    // voice
                    MEDIA_PATH = new String(maleArabic);
                } else if (FEMALE_VOICE != 0 && ARABIC_FONT == 0) { // Female
                    // English
                    // voice
                    MEDIA_PATH = new String(femaleEnglish);
                } else if (FEMALE_VOICE == 0 && ARABIC_FONT == 0) { // Male
                    // English
                    // voice
                    MEDIA_PATH = new String(maleEnglish);
                }
                ///// manjusha 08/01/2019
                // if (MEDIA_PATH == null) {
                //  mediaFile = new File(MainActivity.sdcardPath, MEDIA_PATH + "/" + typeFolder);
                // } else {
                mediaFile = new File(MainActivity.sdcardPath, MEDIA_PATH);
                // }
            } else {
                if (FEMALE_VOICE != 0 && ARABIC_FONT != 0) { // Both Ar_En
                    // Female
                    // voice
                    ///// manjusha 08/01/2019
                    MEDIA_PATH = new String(femaleArabic);
                    MEDIA_PATH_Both = new String(femaleEnglish);
                    ArEn_Font = true;
                } else if (FEMALE_VOICE == 0 && ARABIC_FONT != 0) { // Both
                    // Ar_En
                    // Male
                    // voice
                    MEDIA_PATH = new String(maleArabic);
                    MEDIA_PATH_Both = new String(maleEnglish);
                    ArEn_Font = true;
                } else if (FEMALE_VOICE != 0 && ARABIC_FONT == 0) { // Both
                    // En_Ar
                    // Female
                    // voice
                    MEDIA_PATH = new String(femaleEnglish);
                    MEDIA_PATH_Both = new String(femaleArabic);
                    ArEn_Font = false;
                } else if (FEMALE_VOICE == 0 && ARABIC_FONT == 0) { // Both
                    // En_Ar
                    // Male
                    // voice
                    MEDIA_PATH = new String(maleEnglish);
                    MEDIA_PATH_Both = new String(maleArabic);
                    ArEn_Font = false;
                }
                mediaFile_En = new File(MainActivity.sdcardPath, MEDIA_PATH);
                mediaFile_Ar = new File(MainActivity.sdcardPath,
                        MEDIA_PATH_Both);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Conversion of token into voice.
    public ArrayList<String> voiceConversion() {
        try {
            mArrayList.clear();
            // nirmal 28 oct 2014 -- check whether to play ring-bell
            // if (mRingBell) {
            //// manjusha 08/01/2019 check if file is available and if not there then take from corresponding folder
            //mArrayList.add(mediaFile.getPath() + "/DinDong" + mAudioFileExtenstion);
            //   mArrayList.add(getAudioFile("/DinDong"));
            //  }
            ///manju 29/12/2021 always play if play voice is 1 in xls file
            if (PLAY_VOICE_FILES != 0) {
                mArrayList.add(getAudioFile("/DinDong"));

                int vToken = Integer.parseInt(getToken());
                int hundred = vToken / 100;
                int tenth_2 = vToken % 100;

                if (SPEAK_TOKEN_No != 0) {
                    mArrayList.add(getAudioFile("/NUMBER"));
                }
                if (hundred != 0) {
                    switch (hundred) {
                        case 1:
                            if (ARABIC_FONT != 0) {
                                mArrayList.add(getAudioFile("/N100"));
                            } else {
                                mArrayList.add(getAudioFile("/N01"));
                                mArrayList.add(getAudioFile("/N100"));
                            }
                            break;
                        case 2:
                            mArrayList.add(getAudioFile("/N200"));
                            break;
                        case 3:
                            mArrayList.add(getAudioFile("/N300"));
                            break;
                        case 4:
                            mArrayList.add(getAudioFile("/N400"));
                            break;
                        case 5:
                            mArrayList.add(getAudioFile("/N500"));
                            break;
                        case 6:
                            mArrayList.add(getAudioFile("/N600"));
                            break;
                        case 7:
                            mArrayList.add(getAudioFile("/N700"));
                            break;
                        case 8:
                            mArrayList.add(getAudioFile("/N800"));
                            break;
                        case 9:
                            mArrayList.add(getAudioFile("/N900"));
                            break;
                    }
                    if (tenth_2 != 0) {
                        if (ARABIC_FONT != 0) {
                            arabicVoice(tenth_2);
                        } else {
                            tenthVoice(tenth_2);
                        }
                    }
                } else {
                    tenthVoice(tenth_2);
                }
            }
            return mArrayList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getAudioFile(String fileName) {
        String path = "";
        String typeFolder;
        try {
            typeFolder = mAudioFileExtenstion.substring(1);
            File f1 = new File(mediaFile.getPath() + fileName + mAudioFileExtenstion);
            if (f1.exists()) {
                path = mediaFile.getPath() + fileName + mAudioFileExtenstion;
            } else {
                path = mediaFile.getPath() + "/" + typeFolder + fileName + mAudioFileExtenstion;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    private String getArabicAudioFile(String fileName) {
        String path = "";
        String typeFolder;
        try {
            mediaFile = mediaFile_Ar;
            typeFolder = mAudioFileExtenstion.substring(1);
            File f1 = new File(mediaFile.getPath() + fileName + mAudioFileExtenstion);
            if (f1.exists()) {
                path = mediaFile.getPath() + fileName + mAudioFileExtenstion;
            } else {
                path = mediaFile.getPath() + "/" + typeFolder + fileName + mAudioFileExtenstion;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }


    // Conversion of token into both english & arabic voice.
    public ArrayList<String> enArVoiceConv(boolean flg) {
        try {
            int vToken = Integer.parseInt(getToken());
            int hundred = vToken / 100;
            int tenth_2 = vToken % 100;
            if (flg == true) {
                mArrayList.clear();
            }
            //playAr = true;
            // nirmal 11 june 2014 -- first convert english voice
            mediaFile = mediaFile_En;
            voiceConversion();
            // nirmal 28 oct 2014 -- check whether to play ring-bell
            //// manjusha 08/01/2019///
//            if (mRingBell) {
//                // nirmal 05 nov 2014 -- play voice files only if ringbell flag is
//                // true
//                //// manjusha 08/01/2019
//                mArrayList.add(mediaFile.getPath() + "/DinDong" + mAudioFileExtenstion);
//
//                if (PLAY_VOICE_FILES != 0) {
//
//                    if (SPEAK_TOKEN_No != 0) {
//                        mArrayList.add(mediaFile.getPath() + "/NUMBER" + mAudioFileExtenstion);
//                    }
//                    if (hundred != 0) {
//                        switch (hundred) {
//                            case 1:
//                                if (playAr) {
//                                    mArrayList.add(mediaFile.getPath() + "/N100" + mAudioFileExtenstion);
//                                } else {
//                                    mArrayList.add(mediaFile.getPath() + "/N01" + mAudioFileExtenstion);
//                                    mArrayList.add(mediaFile.getPath() + "/N100" + mAudioFileExtenstion);
//                                }
//                                break;
//                            case 2:
//                                mArrayList.add(mediaFile.getPath() + "/N200" + mAudioFileExtenstion);
//                                break;
//                            case 3:
//                                mArrayList.add(mediaFile.getPath() + "/N300" + mAudioFileExtenstion);
//                                break;
//                            case 4:
//                                mArrayList.add(mediaFile.getPath() + "/N400" + mAudioFileExtenstion);
//                                break;
//                            case 5:
//                                mArrayList.add(mediaFile.getPath() + "/N500" + mAudioFileExtenstion);
//                                break;
//                            case 6:
//                                mArrayList.add(mediaFile.getPath() + "/N600" + mAudioFileExtenstion);
//                                break;
//                            case 7:
//                                mArrayList.add(mediaFile.getPath() + "/N700" + mAudioFileExtenstion);
//                                break;
//                            case 8:
//                                mArrayList.add(mediaFile.getPath() + "/N800" + mAudioFileExtenstion);
//                                break;
//                            case 9:
//                                mArrayList.add(mediaFile.getPath() + "/N900" + mAudioFileExtenstion);
//                                break;
//                        }
//                        if (tenth_2 != 0) {
//                            if (playAr) {
//                                arabicVoice(tenth_2);
//                            } else {
//                                tenthVoice(tenth_2);
//                            }
//                        }
//                    } else {
//                        tenthVoice(tenth_2);
//                    }
//                }
//
//            }
            //close // manjusha 08/01/2019

            // nirmal 11 june 2014 --repeat the code for arabic voice
            // this is a temporary solution, code needs to be optimized

            // nirmal 11 june 2014 -- convert to arabic voice
            mediaFile = mediaFile_Ar;
            //  if (mRingBell) {///manju 29/12/2021 always play if play voice is 1 in xls file
            // nirmal 05 nov 2014 -- play voice files only if ringbell flag is
            // true
            if (PLAY_VOICE_FILES != 0) {

                if (SPEAK_TOKEN_No != 0) {
                    mArrayList.add(getArabicAudioFile("/NUMBER"));
                }
                if (hundred != 0) {
                    switch (hundred) {
                        case 1:
                            if (ARABIC_FONT != 0) {
                                mArrayList.add(getArabicAudioFile("/N100"));
                            } else {
                                mArrayList.add(getArabicAudioFile("/N01"));
                                mArrayList.add(getArabicAudioFile("/N100"));
                            }
                            break;
                        case 2:
                            mArrayList.add(getArabicAudioFile("/N200"));
                            break;
                        case 3:
                            mArrayList.add(getArabicAudioFile("/N300"));
                            break;
                        case 4:
                            mArrayList.add(getArabicAudioFile("/N400"));
                            break;
                        case 5:
                            mArrayList.add(getArabicAudioFile("/N500"));
                            break;
                        case 6:
                            mArrayList.add(getArabicAudioFile("/N600"));
                            break;
                        case 7:
                            mArrayList.add(getArabicAudioFile("/N700"));
                            break;
                        case 8:
                            mArrayList.add(getArabicAudioFile("/N800"));
                            break;
                        case 9:
                            mArrayList.add(getArabicAudioFile("/N900"));
                            break;
                    }
                    if (tenth_2 != 0) {
                        if (ARABIC_FONT != 0) {
                            arabicVoice(tenth_2);
                        } else {
                            tenthVoice(tenth_2);
                        }
                    }
                } else {
                    tenthVoice(tenth_2);
                }
            }
            // }
            return mArrayList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Arabic voice-greater than hundred
    public void arabicVoice(int num) {
        try {
            String s0 = String.valueOf(num);
            if (num < 10) {
                s0 = "NN0" + s0;
            } else {
                s0 = "NN" + s0;
            }
            //String s0_path = mediaFile.getPath() + "/" + s0 + mAudioFileExtenstion; ///manjusha 08/01/2019
            String s0_path = getAudioFile("/" + s0);
            mArrayList.add(s0_path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // English + arabic voice
    public void tenthVoice(int num) {
        try {
            String s0 = String.valueOf(num);
            if (num < 10) {
                s0 = "N0" + s0;
            } else {
                s0 = "N" + s0;
            }
            //String s0_path = mediaFile.getPath() + "/" + s0 + mAudioFileExtenstion; ///manjusha 08/01/2019
            String s0_path = getAudioFile("/" + s0);
            mArrayList.add(s0_path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Play voice of token.
    public void playVoice(int index) {
        if (currentSongIndex < mArrayList.size()) {
            try {
                mPlayer.reset();
                mPlayer.setDataSource(mArrayList.get(index));
                mPlayer.prepare();

                // nirmal 07 oct 2014 -- next line is not required since
                // animation is used instead
                // tokenTouchView.setTextColor(Color.BLUE);
                mPlayer.setOnCompletionListener(new OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer player) {
                        player.stop();
                        // play next audio file
                        if (currentSongIndex < (mArrayList.size() - 1)) {
                            playVoice(currentSongIndex + 1);
                            currentSongIndex = currentSongIndex + 1;
                        } else {
                            // nirmal 07 oct 2014 -- next line is not required
                            // since animation is used instead
                            // tokenTouchView.setTextColor(Color.RED);
                        }
                    }
                });
                // nirmal 11 june 2014 -- created onpreparedListener
                mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        // TODO Auto-generated method stub
                        //  if (mRingBell) {
                        // nirmal 05 nov 2014 -- play voice only if ringBell
                        // flag is true
                        mPlayer.start();
                        // }
                    }
                });

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception exc) {
            }
        }
    }

    // token blinking animation
    private void tokenAnimation() {
        try {
            // Log.d(":::token call", "calling token animation");
            /*
             * //Animation anim = new AlphaAnimation(0.0f, 1.0f);
             *
             * anim.setDuration(600); // manage the time of the blink with this //
             * parameter //anim.setStartOffset(20); // nirmal 18 june 2014 -- repeat
             * // blink 3 times anim.setRepeatCount(3);
             * //tokenTouchView.startAnimation(anim);
             */
            // nirmal 18 june 2014 -- used value animator
            // ValueAnimator anim = ValueAnimator.ofInt(Color.RED, Color.BLUE);
            // nirmal 07 oct 2014 -- token color and token blink color are loaded
            // from the excel file
            ValueAnimator anim = ValueAnimator.ofInt(mTokenColor, mTokenBlinkColor);
            // value animator gives intermediate colors between RED and BLUE
            // hence didnot use those interpolated values in the listener
            anim.setDuration(600);
            anim.setStartDelay(100);
            anim.setRepeatCount(4);
//        lastLedColor=
            anim.addListener(new Animator.AnimatorListener() {
                // boolean RED_TEXT = true;
                // nirmal 07 oct 2014 -- renamed RED_TEXT to TOKEN_ORIGINAL_COLOR
                boolean TOKEN_ORIGINAL_COLOR = true;

                @Override
                public void onAnimationStart(Animator animation) {
                    // tokenTouchView.setTextColor(Color.BLUE);
                    // RED_TEXT = false;
                    tokenTouchView.setTextColor(mTokenBlinkColor);
                    ledOnGreen(100);
                    // RED_TEXT = false;
                    TOKEN_ORIGINAL_COLOR = false;
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    // int col=(Integer)
                    // ((ValueAnimator)animation).getAnimatedValue();
                    // if (RED_TEXT) {
                    // tokenTouchView.setTextColor(Color.BLUE);
                    // RED_TEXT = false;
                    // } else {
                    // tokenTouchView.setTextColor(Color.RED);
                    // RED_TEXT = true;
                    // }
                    // nirmal 07 oct 2014 -- used TOKEN_ORIGINAL_COLOR instead of
                    // RED_TEXT
                    if (TOKEN_ORIGINAL_COLOR) {
                        tokenTouchView.setTextColor(mTokenBlinkColor);
                        ledOnGreen(100);
                        TOKEN_ORIGINAL_COLOR = false;
                    } else {
                        tokenTouchView.setTextColor(mTokenColor);
                        ledOnBlue(100);
                        TOKEN_ORIGINAL_COLOR = true;

                    }

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    // tokenTouchView.setTextColor(Color.RED);
                    // nirmal 07 oct 2014
                    tokenTouchView.setTextColor(mTokenColor);
                    TOKEN_ORIGINAL_COLOR = true;
                    switch (lastLedColor) {
                        case 1:
                            ledOnRed(100);
                            break;
                        case 2:
                            ledOnGreen(100);
                            break;
                        default:
                            ledOnBlue(100);
                            break;
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    // TODO Auto-generated method stub
                    tokenTouchView.setTextColor(mTokenColor);
                    ledOnBlue(100);
                    TOKEN_ORIGINAL_COLOR = true;

                }
            });
            anim.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // get token number
    public String getToken() {
        return token;
    }

    // set token number
    public void setToken(String value) {
        token = value;
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        mPlayer.stop();
        super.onStop();
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        stopReadTask = true;
    }

    public void ledOnRed(int red_bright) {
        try {
            // jnielc.ledmix(fb);
            jnielc.seekstart();
            jnielc.ledseek(seek_red, red_bright);
            jnielc.seekstop();
            Intent intent = new Intent("android.intent.action.ledctl");
            intent.putExtra("led", led_red);
            intent.putExtra("ledbrightness", red_bright);
            getActivity().sendBroadcast(intent);
            Log.d("count", "LED red On");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // SystemProperteisProxy.set("persist.demo.ledswitch", "1");
        //Toast.makeText(getActivity(), "LED red On !!!!!", Toast.LENGTH_SHORT).show();

    }

    public void ledOnGreen(int green_bright) {
        try {
            // jnielc.ledmix(fb);
            jnielc.seekstart();
            jnielc.ledseek(seek_green, green_bright);
            jnielc.seekstop();
            Intent intent = new Intent("android.intent.action.ledctl");
            intent.putExtra("led", led_green);
            intent.putExtra("ledbrightness", green_bright);
            getActivity().sendBroadcast(intent);
            Log.d("count", "LED green On");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // SystemProperteisProxy.set("persist.demo.ledswitch", "1");
        //Toast.makeText(getActivity(), "LED green On !!!!!", Toast.LENGTH_SHORT).show();

    }

    public void ledOnBlue(int blue_bright) {
        try {
            // jnielc.ledmix(fb);
            jnielc.seekstart();
            jnielc.ledseek(seek_blue, blue_bright);
            jnielc.seekstop();
            Intent intent = new Intent("android.intent.action.ledctl");
            intent.putExtra("led", led_blue);
            intent.putExtra("ledbrightness", blue_bright);
            getActivity().sendBroadcast(intent);
            Log.d("count", "LED blue On");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void startBeatTime() {
//
//        Timer t = new Timer();
//        t.scheduleAtFixedRate(new TimerTask() {
//
//            @Override
//            public void run() {
//                callInSec = true;
//            }
//
//        }, 0, 50000);
//
//
//    }

//    public void checkSocketConnectionTimer() {
//        Timer t = new Timer();
//        t.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                Date date = new Date();
//                long maxDuration = 1000 * 60 * 2;
//                long duration = date.getTime() - lastSocketDataTime;
//                if (duration >= maxDuration) {
//                    MainActivity.logMessage("its been two minutes no data coming::" + date.toString());
//                    try {
//                        if (sock != null) {
//                            sock.close();
//                            sock = null;
//                        }
//                        start_handler_readServerData();
//                    } catch (Exception e) {
//                        // TODO Auto-generated catch block
//                        // e.printStackTrace();
//                    }
//                }
//            }
//
//        }, 0, 1000 * 60);
//
//
//    }


    private void start_handler_readServerData() {
        // nirmal 27 oct 2014 -- this method will keep calling itself
        // the socket will be closed when the flag stopReadTask is true
        mHandler_readServerData.postDelayed(new Runnable() {

            @Override
            public void run() {
                //  if (!isReadTaskBusy) {
                readDataFromServer();
                // }

                if (!stopReadTask) {
                    start_handler_readServerData();
                }
            }
        }, AppConstantsFlags.SOCKET_HANDLER_DELAY);
    }

    private void readDataFromServer() {
        // nirmal 27 oct 2014 -- start the asyncTask to read data from server
        new AsyncTask_ReadServerData(getActivity()).execute();
    }


    class AsyncTask_ReadServerData extends AsyncTask<String, Void, String> {
        Activity callerActivityContext;

        public AsyncTask_ReadServerData(Activity callerActivityContext) {
            super();
            this.callerActivityContext = callerActivityContext;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                getTokenJsonResponse = WebService.getTokenNumber(MainActivity.sharePrefSettings.getString(
                        "CounterNo", ""), "getTokenDataForCounter");
            } catch (Exception e) {
            }
            return getTokenJsonResponse;

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            if (connected) {
                txtConnectivityView.setBackgroundColor(Color.GREEN);
            } else {
                txtConnectivityView.setBackgroundColor(Color.RED);
            }
            try {

                JSONObject jsonObject = new JSONObject(result);
                String token = jsonObject.getString("tno");
                String time = jsonObject.getString("dispServerTime");
                String serviceNameEng = jsonObject.getString("ServiceName_En");
                String serviceNameAr = jsonObject.getString("ServiceName_Ar");
                int second = Integer.parseInt(jsonObject.getString("sec"));
                JSONObject innerJsonObject = jsonObject.getJSONObject("returnStatus");
                boolean success = innerJsonObject.getBoolean("success");
                String reason = innerJsonObject.getString("reason");

                if (success) {
                    playTokenVoices(token, second, time, serviceNameEng, serviceNameAr);
                } else {
                    txtConnectivityView.setBackgroundColor(Color.parseColor("#FFA500"));
                }
            } catch (Exception e) {
                connected = false;
                e.printStackTrace();
            }
        }

    }


    public class GetBranchName extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                getBranchJsonResponse = BranchWebService.getBranchName("HO", "GetBranchDetail");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return getBranchJsonResponse;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject innerJsonObject = jsonObject.getJSONObject("returnStatus");
                boolean success = innerJsonObject.getBoolean("success");
                String reason = innerJsonObject.getString("reason");
                if (success) {
                    if (ARABIC_FONT == 1)
                        tvBranchName.setText("" + jsonObject.getString("NameAr"));
                    else
                        tvBranchName.setText("" + jsonObject.getString("NameEN"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}
