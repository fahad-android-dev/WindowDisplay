package com.orbits.queuingsystem

internal object AppConstantsFlags {
    const val key_pref_show_ad: String = "SHOW_ADS"
    const val key_pref_show_scroll: String = "SHOW_SCROLL"
    const val key_pref_show_logo: String = "SHOW_LOGO"
    const val key_pref_restart: String = "RESTART_APP"
    const val key_pref_time_to_restart: String = "TIME_TO_RESTART"
    const val key_pref_show_counter: String = "SHOW_COUNTER"
    const val KEY_DIGIT_MAP_ARB_FONT_HALF_SCREEN: String = "Arabic HalfScr FontSize"
    const val KEY_DIGIT_MAP_ENG_FONT_HALF_SCREEN: String = "English HalfScr FontSize"
    const val KEY_DIGIT_MAP_ARB_FONT_FULL_SCREEN: String = "Arabic FullScr FontSize"
    const val KEY_DIGIT_MAP_ENG_FONT_FULL_SCREEN: String = "English FullScr FontSize"
    const val KEY_DIGIT_MAP_BG_COLOR: String = "Background Color"
    const val KEY_DIGIT_MAP_TOKEN_COLOR: String = "Token Color"
    const val KEY_DIGIT_MAP_TOKEN_BLINK_COLOR: String = "Token Blink Color"
    const val KEY_DIGIT_MAP_TOKEN_PADDING_TOP: String = "Token Padding Top"
    const val KEY_DIGIT_MAP_TOKEN_PADDING_BOTTOM: String = "Token Padding Bottom"
    const val KEY_DIGIT_MAP_COUNTER_PADDING_BOTTOM: String = "Counter Padding Bottom"
    const val KEY_DIGIT_MAP_COUNTER_PADDING_TOP: String = "Counter Padding Top"
    const val KEY_DIGIT_MAP_COUNTER_COLOR: String = "Counter Color"
    const val KEY_DIGIT_MAP_COUNTER_BG_COLOR: String = "Counter Background Color"
    const val KEY_DIGIT_MAP_ENG_COUNTER_FONT_FULL_SCREEN: String =
        "Counter English FullScr FontSize"
    const val KEY_DIGIT_MAP_ENG_COUNTER_FONT_HALF_SCREEN: String =
        "Counter English HalfScr FontSize"
    const val KEY_DIGIT_MAP_ARB_COUNTER_FONT_HALF_SCREEN: String = "Counter Arabic HalfScr FontSize"
    const val KEY_DIGIT_MAP_ARB_COUNTER_FONT_FULL_SCREEN: String = "Counter Arabic FullScr FontSize"
    const val KEY_DIGIT_MAP_SERVICE_NAME_TEXT_COLOR: String = "Service Text Color"
    const val KEY_DIGIT_MAP_SERVICE_NAME_FONT_SIZE: String = "Service FontSize"
    const val KEY_DIGIT_MAP_BRANCH_NAME_TEXT_COLOR: String = "Branch Text Color"
    const val KEY_DIGIT_MAP_BRANCH_NAME_BG_COLOR: String = "Branch Background Color"
    const val KEY_DIGIT_MAP_BRANCH_NAME_FONT_SIZE: String = "Branch FontSize"
    const val KEY_DIGIT_MAP_TIME_FONT_SIZE: String = "Time FontSize"
    const val KEY_DIGIT_MAP_TIME_TEXT_COLOR: String = "Time Text Color"


    const val SOCKET_HANDLER_DELAY: Int = 1000
    const val SOCKET_BUFFER_SIZE: Int = 512
    const val SOCKET_TIMEOUT_INTERVAL: Int = 3000
    const val DEVICE_TYPE_ANDROID_WINDOW_DISPLAY: Int = 3
    const val COMMAND_CODE_IDENTIFY_SELF: Int = 58
    const val STATUS_BYTE: Int = 128
    const val KEY_DIGIT_MAP_AUDIO_FILE_EXTENSION: String = "Audio-File extension"
    const val KEY_CC: String = "Customer_Code"
    const val KEY_IN: String = "Invoice_No"
    const val KEY_BC: String = "Branch_Code"
    const val KEY_EMAIL: String = "Email_Id"
    const val KEY_CONTACT_NO: String = "Phone_No"
    const val KEY_STAFF_ID: String = "STAFF_ID"

    //SharedPref
    const val KEY_PASSWORD_STAFF: String = "PASSWORD_STAFF"
    var key_pref_service_name: String = "SERVICE_NAME"
    var key_pref_service_top: String = "SERVICE_TOP"
    var key_pref_none_top: String = "NONE_TOP"
    var key_pref_user_top: String = "USER_TOP"
    var key_pref_branch_top: String = "BRANCH_TOP"
    var key_pref_branch_name: String = "BRANCH_NAME"
    var key_pref_none: String = "NONE"
    var key_pref_show_time: String = "TIME"
    var key_pref_user_name: String = "USER_NAME"

    // nirmal 17 june 2014 -- common class to declare global constants and flags
    var SHOW_ADVERTIZEMENT: String = ""
    var SHOW_SCROLL: Boolean = true
    var SHOW_LOGO: Boolean = true
    var SHOW_SERVICE_NAME: Boolean = true
    var SHOW_SERVICE_NAME_TOP: Boolean = true
    var SHOW_USER_NAME_TOP: Boolean = true
    var SHOW_BRANCH_NAME_TOP: Boolean = true
    var SHOW_NONE_TOP: Boolean = true
    var SHOW_NONE: Boolean = true
    var SHOW_BRANCH_NAME: Boolean = true
    var SHOW_USER_NAME: Boolean = true
    var SHOW_TIME: Boolean = true
    var ANDROID_SERVER: Boolean = false
    var SEND_PROTOCOL_TO_SERVER: Boolean = false
    var READ_PROTOCOL_FROM_SERVER: Boolean = false
}
