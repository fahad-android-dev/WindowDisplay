package com.orbits.queuingsystem

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.orbits.queuingsystem.ProtocolDataHolder.PROTOCOL_TYPE
import org.json.JSONException
import org.json.JSONObject
import kotlin.math.max
import kotlin.math.pow

//import android.util.Log;

internal class CommonLogic {
    var context: Context? = null
    fun extractTokenNumber(strProtocol: String): ProtocolDataHolder {
        /*
         * nirmal -- extract token number from protocol-string
         */
        // Log.d("awd", "extracting token number");
//		Log.d("awd", "protocol = " + strProtocol);

        ////// manjusha 20/dec/2019////
//      String tokenLength = MainActivity.sharePrefSettings
//               .getString("tokenLength", "");
//        String prefixCheck = MainActivity.sharePrefSettings
//                .getString("CheckPrefix", "");

        val protocolData = ProtocolDataHolder()
        // protocolData.setLogin(2);
        protocolData.protocolType = PROTOCOL_TYPE.TOKEN_NUMBER

        try {
            //Log.d("awd", "exception = " + strProtocol);
            /*
             * nirmal 29 oct 2014 -- dont check destination address, as data for
             * others wont be received here.
             *
             * String destAddr = ""; // destAddr =
             * Integer.parseInt(strProtocol.substring(1, 3)); destAddr =
             * strProtocol.substring(1, 3); // Log.d("awd",
             * "destination address = " + destAddr); if
             * (!destAddr.equals(MainActivity.sharePrefSettings.getString(
             * "CounterNo", "1"))) { // if destination address is not our
             * counter number then ignore. protocolData.setToBeIgnored(true);
             * return protocolData; }
             */

            val commandCode1 = 0
            var commandCode2 = 0

            commandCode2 = strProtocol.codePointAt(6)
            //Log.d("commandCode2::", "" + commandCode2);
            ////// manjusha 27/dec/2019////
            if (commandCode2 == 59) {
                val ed: SharedPreferences.Editor = MainActivity.Companion.sharePrefSettings!!.edit()
                ed.putString("tokenLength", strProtocol[19].toString())
                ed.putString("prefix", strProtocol[18].toString())
                ed.commit()
            }
            if (commandCode2 != 42) {
                // if commandCode2 is not 42 then ignore.
                //Log.d("outer data logout::", "" + commandCode2);
                if (commandCode2 == 38) {
                    //Log.d("data logout::", "" + commandCode2);
                    if (strProtocol.substring(13, 15) == "99") {
                        protocolData.isLogin = 1 //log out//
                    } else if (strProtocol.substring(13, 15) == "01") {
                        protocolData.isLogin = 2 //login//
                    }
                }
                protocolData.isToBeIgnored = true
                return protocolData
            }
            protocolData.isLogin = 3

            // nirmal 28 oct 2014 -- ring the bell if statusByte = 128
            var statusByte = 0
            statusByte = strProtocol.codePointAt(7)
            //Toast.makeText(getActivity(), ":::" + mRingBell, Toast.LENGTH_SHORT).show();
            if (statusByte == 128) {
                protocolData.ringBell = true
            } else {
                protocolData.ringBell = false
            }

            // if (statusByte == 8364) {
            // protocolData.setRingBell(true);
            // } else {
            // protocolData.setRingBell(false);
            // }

            // Log.d("awd", "statusByte = " + statusByte);
            // Log.d("awd",
            // "statusByte character = " + strProtocol.substring(7, 8));
            // Log.d("awd", "statusByte character = " + new String(new
            // byte[]{(byte) 128},"UTF-16"));
            // protocolData.setRingBell(true);
            /*
             * nirmal 28 oct 2014 -- no need for vip indication in
             * android-window-display, hence commented the code to check for
             * vip. commandCode1 = strProtocol.codePointAt(5); if (commandCode1
             * == 1) { protocolData.setVip(true); } else {
             * protocolData.setVip(false); }
             */
            var dataLength = 0
            dataLength = strProtocol.codePointAt(8)
            // Log.d("awd", "dataLength = "+dataLength);
            // Log.d("awd", "dataLength = "+strProtocol.charAt(8));
            var strData = ""
            //Log.d("awd", "strProtocol:: = " + strProtocol);
            strData = strProtocol.substring(9, 9 + dataLength)

            // strData = strProtocol.substring(9+dataLength-5, 9 + dataLength);
            // if (strData.length() == 5) {
            // // if length is 5 then take last three characters
            // protocolData.setTokenNumber(Integer.parseInt(strData
            // .substring(2)));
            // } else {
            // // else take 2nd 3rd and 4th characters
            // protocolData.setTokenNumber(Integer.parseInt(strData.substring(
            // 2, 5)));
            // }
            // nirmal -- last 3 positions contain the token-number.
            tokenLength = MainActivity.Companion.sharePrefSettings
                ?.getString("tokenLength", "")
            prefixCheck = MainActivity.Companion.sharePrefSettings
                ?.getString("prefix", "")
            //Log.d("awd", "str data:: = " + strData);
            ////// manjusha 20/dec/2019
//            if (prefixCheck.equals("true")) {
//                protocolData.setTokenNumber((strData.substring(strData.length() - (Integer.parseInt(tokenLength) + 3))).trim());
//            } else {
//                protocolData.setTokenNumber((strData.substring(strData.length() - (Integer.parseInt(tokenLength)))).trim());
//            }
            ////// manjusha 30/dec/2019
            protocolData.tokenNumber =
                strData.substring(strData.length - (tokenLength!!.toInt() + prefixCheck!!.toInt()))
                    .trim { it <= ' ' }
            // Log.d("awd", "after str data:: = " + (strData.substring(strData.length() - (Integer.parseInt(tokenLength) + Integer.parseInt(prefixCheck)))).trim());
            protocolData.isToBeIgnored = false
            // Log.d("awd",
            // "extracted token number -- "
            // + protocolData.getTokenNumber());
        } catch (e: Exception) {
            // TODO: handle exception
            // Log.d("awd",
            // "error while extracting token number -- " + e.getMessage());
            protocolData.isToBeIgnored = true
            protocolData.errorMessage = e.message
            e.printStackTrace()
        }
        return protocolData
    }

    fun processProtocolString(strProtocol: String): ProtocolDataHolder {
        /*
         * extract data from protocol string
         */

        var protocolData = ProtocolDataHolder()
        try {
            // if (strProtocol.substring(0, 1).equalsIgnoreCase("@")) {
            // protocolData = extractTokenNumber(strProtocol);
            // }

            /*---------------------28/11/2017---------------------------*/

            bufferData += strProtocol

            Log.d("awd", "protocol = " + bufferData)
            val checkstatus: String = MainActivity.Companion.sharePrefSettings
                ?.getString("CheckAndroid", "") ?: ""
            val checkCounter: String = MainActivity.Companion.sharePrefSettings
                ?.getString("CounterNo", "") ?: ""

            /*--------------------------------------------*/
            if (bufferData.indexOf('{') >= 0) {
                bufferData = bufferData.substring(bufferData.indexOf('{'))
                val chars = bufferData.toCharArray()

                for (i in chars.indices) {
                    println("buffer data char:::::::::::: ::: " + chars[i])
                    bufferData += chars[i]
                }
            }


            /*------------------------------------------*/
            if (checkstatus == "false") {
                ///manju 16 dec 2021///due to stuck
//                Date date = new Date();
//                currentTime = date.getTime();
//                if (bufferData.indexOf("MWT")>=0) {
//                    previousTime = date.getTime();
//                } else {
//                    long duration = currentTime - previousTime;
//                    //long maxDuration = ((duration / 1000) / 60) * 1;
//                    long maxDuration = 1000 * 60;
//                    TokenActivity.connected = false;
//                    if (duration >= maxDuration) {
//                        MainActivity.logMessage("not getting MWT going for reconnect::" + new Date());
//                        MainActivity.logMessage("data after 1 min::" + bufferData);
//                        TokenActivity.reconnectToSocket();
//                        previousTime = date.getTime();
//                    }
//                }

                while (bufferData.indexOf('@') >= 0) {
                    bufferData = bufferData.substring(bufferData.indexOf('@'))

                    if (bufferData.indexOf(13.toChar()) >= 0) {
                        if (bufferData.startsWith("@,")) {
                            protocolData.isToBeIgnored = true
                            protocolData.errorMessage = "junk protocol received"
                        } else if (bufferData.startsWith("@")) {
//
                            protocolData =
                                extractTokenNumber(bufferData.substring(0, (bufferData.length - 1)))
                            // protocolData = extractTokenNumber(bufferData.substring(0, bufferData.indexOf((char) 13)));
                            //Log.d("awd", "@inner protocol = " + bufferData);
                        }
                        bufferData = bufferData.substring(bufferData.indexOf(13.toChar()) + 1)
                    } else {
                        break
                    }
                }
            } else if (checkstatus == "true") {
                val json_string = strProtocol
                val isJsonString = json_string.indexOf("{")
                println("Json Array123 ::: $json_string")
                if (!json_string.isEmpty() && json_string != null) {
                    try {
                        if (isJsonString == 0) {
                            println("Json Array123 ::: $json_string")
                            val root = JSONObject(json_string)
                            val parseData = root.getJSONArray("DataToKeypad")
                            for (i in 0 until parseData.length()) {
                                val cc = parseData.getJSONObject(i)

                                myType = cc.getString("Action")
                                mytoken = cc.getString("CurrentToken")
                                mywaiting = cc.getString("Waiting")
                                myID = cc.getString("MaxWait")
                            }
                        }
                    } catch (e: JSONException) {
                        // sock = null;
                        //Toast.makeText(getApplicationContext(), "Msg => " + line, Toast.LENGTH_SHORT).show();
                        e.printStackTrace()
                    } catch (e: Exception) {
                        //sock = null;
                        e.printStackTrace()
                    }
                }
            }


            /*-------------------------------------------------------------*/
        } catch (e: Exception) {
            // TODO: handle exception
            // Log.d("awd",
            // "error while processing protocol string -- "
            // + e.getMessage());
            protocolData.isToBeIgnored = true
            protocolData.errorMessage = e.message
        }
        return protocolData
    }

    companion object {
        val instance: CommonLogic = CommonLogic()

        var myType: String? = null
        var mytoken: String? = null
        var mywaiting: String? = null
        var myID: String? = null
        var mywaitTym: String? = null
        var tokenLength: String? = null
        var prefixCheck: String? = null
        private var bufferData = ""
        private val currDate: String? = null
        private val oldDate: String? = null
        private const val previousTime: Long = 0
        private const val currentTime: Long = 0

        fun englishToArabicNumber(engNumber: Int, minDigit: Int): String {
            var arabic = ""
            var len1 = engNumber.toString().length
            len1 = max(len1.toDouble(), minDigit.toDouble()).toInt()
            var x = 0
            x = len1
            while (x > 0) {
                var num1 = 0
                num1 = if (x - 1 > 0) {
                    engNumber / 10.0.pow((x - 1).toDouble()).toInt() % 10
                } else {
                    engNumber % 10
                }
                when (num1) {
                    0 -> arabic += "\u0660"
                    1 -> arabic += "\u0661"
                    2 -> arabic += "\u0662"
                    3 -> arabic += "\u0663"
                    4 -> arabic += "\u0664"
                    5 -> arabic += "\u0665"
                    6 -> arabic += "\u0666"
                    7 -> arabic += "\u0667"
                    8 -> arabic += "\u0668"
                    9 -> arabic += "\u0669"
                }
                x--
            }
            return arabic
        }

        fun englishDigits(token: Int, minDigit: Int): String {
            var english = ""
            var len1 = token.toString().length
            len1 = max(len1.toDouble(), minDigit.toDouble()).toInt()
            var x = 0
            x = len1
            while (x > 0) {
                var num1 = 0
                num1 = if (x - 1 > 0) {
                    token / 10.0.pow((x - 1).toDouble()).toInt() % 10
                } else {
                    token % 10
                }
                when (num1) {
                    0 -> english += "0"
                    1 -> english += "1"
                    2 -> english += "2"
                    3 -> english += "3"
                    4 -> english += "4"
                    5 -> english += "5"
                    6 -> english += "6"
                    7 -> english += "7"
                    8 -> english += "8"
                    9 -> english += "9"
                }
                x--
            }
            return english
        }
    }
}
