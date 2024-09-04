package com.orbits.queuingsystem;

class AppConstantsFlags {
    static final String key_pref_show_ad = "SHOW_ADS";
    static final String key_pref_show_scroll = "SHOW_SCROLL";
    static final String key_pref_show_logo = "SHOW_LOGO";
    static final String key_pref_restart = "RESTART_APP";
    static final String key_pref_time_to_restart = "TIME_TO_RESTART";
    static final String key_pref_show_counter = "SHOW_COUNTER";
    static final String KEY_DIGIT_MAP_ARB_FONT_HALF_SCREEN = "Arabic HalfScr FontSize";
    static final String KEY_DIGIT_MAP_ENG_FONT_HALF_SCREEN = "English HalfScr FontSize";
    static final String KEY_DIGIT_MAP_ARB_FONT_FULL_SCREEN = "Arabic FullScr FontSize";
    static final String KEY_DIGIT_MAP_ENG_FONT_FULL_SCREEN = "English FullScr FontSize";
    static final String KEY_DIGIT_MAP_BG_COLOR = "Background Color";
    static final String KEY_DIGIT_MAP_TOKEN_COLOR = "Token Color";
    static final String KEY_DIGIT_MAP_TOKEN_BLINK_COLOR = "Token Blink Color";
    static final String KEY_DIGIT_MAP_TOKEN_PADDING_TOP = "Token Padding Top";
    static final String KEY_DIGIT_MAP_TOKEN_PADDING_BOTTOM = "Token Padding Bottom";
    static final String KEY_DIGIT_MAP_COUNTER_PADDING_BOTTOM = "Counter Padding Bottom";
    static final String KEY_DIGIT_MAP_COUNTER_PADDING_TOP = "Counter Padding Top";
    static final String KEY_DIGIT_MAP_COUNTER_COLOR = "Counter Color";
    static final String KEY_DIGIT_MAP_COUNTER_BG_COLOR = "Counter Background Color";
    static final String KEY_DIGIT_MAP_ENG_COUNTER_FONT_FULL_SCREEN = "Counter English FullScr FontSize";
    static final String KEY_DIGIT_MAP_ENG_COUNTER_FONT_HALF_SCREEN = "Counter English HalfScr FontSize";
    static final String KEY_DIGIT_MAP_ARB_COUNTER_FONT_HALF_SCREEN = "Counter Arabic HalfScr FontSize";
    static final String KEY_DIGIT_MAP_ARB_COUNTER_FONT_FULL_SCREEN = "Counter Arabic FullScr FontSize";
    static final String KEY_DIGIT_MAP_SERVICE_NAME_TEXT_COLOR = "Service Text Color";
    static final String KEY_DIGIT_MAP_SERVICE_NAME_FONT_SIZE = "Service FontSize";
    static final String KEY_DIGIT_MAP_BRANCH_NAME_TEXT_COLOR = "Branch Text Color";
    static final String KEY_DIGIT_MAP_BRANCH_NAME_BG_COLOR = "Branch Background Color";
    static final String KEY_DIGIT_MAP_BRANCH_NAME_FONT_SIZE = "Branch FontSize";
    static final String KEY_DIGIT_MAP_TIME_FONT_SIZE = "Time FontSize";
    static final String KEY_DIGIT_MAP_TIME_TEXT_COLOR = "Time Text Color";



    static final int SOCKET_HANDLER_DELAY = 1000;
    static final int SOCKET_BUFFER_SIZE = 512;
    static final int SOCKET_TIMEOUT_INTERVAL = 3000;
    static final int DEVICE_TYPE_ANDROID_WINDOW_DISPLAY = 3;
    static final int COMMAND_CODE_IDENTIFY_SELF = 58;
    static final int STATUS_BYTE = 128;
    static final String KEY_DIGIT_MAP_AUDIO_FILE_EXTENSION = "Audio-File extension";
    static final String KEY_CC = "Customer_Code";
    static final String KEY_IN = "Invoice_No";
    static final String KEY_BC = "Branch_Code";
    static final String KEY_EMAIL = "Email_Id";
    static final String KEY_CONTACT_NO = "Phone_No";
    static final String KEY_STAFF_ID = "STAFF_ID";

    //SharedPref
    static final String KEY_PASSWORD_STAFF = "PASSWORD_STAFF";
    public static String key_pref_service_name = "SERVICE_NAME";
    public static String key_pref_branch_name = "BRANCH_NAME";
    public static String key_pref_user_name = "USER_NAME";
    public static String key_pref_show_time= "TIME";
    // nirmal 17 june 2014 -- common class to declare global constants and flags
    static String SHOW_ADVERTIZEMENT = "";
    static boolean SHOW_SCROLL = true;
    static boolean SHOW_LOGO = true;
    static boolean SHOW_SERVICE_NAME = true;
    static boolean SHOW_BRANCH_NAME = true;
    static boolean SHOW_TIME = true;
    static boolean ANDROID_SERVER;
    static boolean SEND_PROTOCOL_TO_SERVER = false;
    static boolean READ_PROTOCOL_FROM_SERVER = false;
}
