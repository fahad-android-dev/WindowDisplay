package com.orbits.queuingsystem;

//import android.util.Log;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.orbits.queuingsystem.ProtocolDataHolder.PROTOCOL_TYPE;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

class CommonLogic {
    private static final CommonLogic ourInstance = new CommonLogic();

    public static CommonLogic getInstance() {
        return ourInstance;
    }

    static String myType, mytoken, mywaiting, myID, mywaitTym;
    static String tokenLength, prefixCheck;
    private static String bufferData = "";
    private static String currDate, oldDate;
    Context context;
    private static long previousTime = 0;
    private static long currentTime = 0;

    public ProtocolDataHolder extractTokenNumber(String strProtocol) {
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
        ProtocolDataHolder protocolData = new ProtocolDataHolder();
        // protocolData.setLogin(2);
        protocolData.setProtocolType(PROTOCOL_TYPE.TOKEN_NUMBER);

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
            int commandCode1 = 0;
            int commandCode2 = 0;

            commandCode2 = strProtocol.codePointAt(6);
            //Log.d("commandCode2::", "" + commandCode2);
            ////// manjusha 27/dec/2019////
            if (commandCode2 == 59) {
                SharedPreferences.Editor ed = MainActivity.sharePrefSettings.edit();
                ed.putString("tokenLength", String.valueOf(strProtocol.charAt(19)));
                ed.putString("prefix", String.valueOf(strProtocol.charAt(18)));
                ed.commit();
            }
            if (commandCode2 != 42) {
                // if commandCode2 is not 42 then ignore.
                //Log.d("outer data logout::", "" + commandCode2);
                if (commandCode2 == 38) {
                    //Log.d("data logout::", "" + commandCode2);
                    if (strProtocol.substring(13, 15).equals("99")) {
                        protocolData.setLogin(1);//log out//
                    } else if (strProtocol.substring(13, 15).equals("01")) {
                        protocolData.setLogin(2);//login//
                    }
                }
                protocolData.setToBeIgnored(true);
                return protocolData;

            }
            protocolData.setLogin(3);

            // nirmal 28 oct 2014 -- ring the bell if statusByte = 128
            int statusByte = 0;
            statusByte = strProtocol.codePointAt(7);
            //Toast.makeText(getActivity(), ":::" + mRingBell, Toast.LENGTH_SHORT).show();
            if (statusByte == 128) {
                protocolData.setRingBell(true);
            } else {
                protocolData.setRingBell(false);
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
            int dataLength = 0;
            dataLength = strProtocol.codePointAt(8);
            // Log.d("awd", "dataLength = "+dataLength);
            // Log.d("awd", "dataLength = "+strProtocol.charAt(8));
            String strData = "";
            //Log.d("awd", "strProtocol:: = " + strProtocol);
            strData = strProtocol.substring(9, 9 + dataLength);
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

            tokenLength = MainActivity.sharePrefSettings
                    .getString("tokenLength", "");
            prefixCheck = MainActivity.sharePrefSettings
                    .getString("prefix", "");
            //Log.d("awd", "str data:: = " + strData);
            ////// manjusha 20/dec/2019
//            if (prefixCheck.equals("true")) {
//                protocolData.setTokenNumber((strData.substring(strData.length() - (Integer.parseInt(tokenLength) + 3))).trim());
//            } else {
//                protocolData.setTokenNumber((strData.substring(strData.length() - (Integer.parseInt(tokenLength)))).trim());
//            }
            ////// manjusha 30/dec/2019
            protocolData.setTokenNumber((strData.substring(strData.length() - (Integer.parseInt(tokenLength) + Integer.parseInt(prefixCheck)))).trim());
            // Log.d("awd", "after str data:: = " + (strData.substring(strData.length() - (Integer.parseInt(tokenLength) + Integer.parseInt(prefixCheck)))).trim());
            protocolData.setToBeIgnored(false);
            // Log.d("awd",
            // "extracted token number -- "
            // + protocolData.getTokenNumber());
        } catch (Exception e) {
            // TODO: handle exception
            // Log.d("awd",
            // "error while extracting token number -- " + e.getMessage());
            protocolData.setToBeIgnored(true);
            protocolData.setErrorMessage(e.getMessage());
            e.printStackTrace();

        }
        return protocolData;
    }

    public ProtocolDataHolder processProtocolString(String strProtocol) {
        /*
         * extract data from protocol string
         */

        ProtocolDataHolder protocolData = new ProtocolDataHolder();
        try {

            // if (strProtocol.substring(0, 1).equalsIgnoreCase("@")) {
            // protocolData = extractTokenNumber(strProtocol);
            // }

            /*---------------------28/11/2017---------------------------*/
            bufferData += strProtocol;

            Log.d("awd", "protocol = " + bufferData);
            String checkstatus = MainActivity.sharePrefSettings
                    .getString("CheckAndroid", "");
            String checkCounter = MainActivity.sharePrefSettings
                    .getString("CounterNo", "");

            /*--------------------------------------------*/

            if (bufferData.indexOf('{') >= 0) {
                bufferData = bufferData.substring(bufferData.indexOf('{'));
                char[] chars = bufferData.toCharArray();

                for (int i = 0; i < chars.length; i++) {
                    System.out.println("buffer data char:::::::::::: ::: " + chars[i]);
                    bufferData += chars[i];

                }
            }




            /*------------------------------------------*/


            if (checkstatus.equals("false")) {
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
                    bufferData = bufferData.substring(bufferData.indexOf('@'));

                    if (bufferData.indexOf((char) 13) >= 0) {

                        if (bufferData.startsWith("@,")) {
                            protocolData.setToBeIgnored(true);
                            protocolData.setErrorMessage("junk protocol received");
                        } else if (bufferData.startsWith("@")) {
//
                            protocolData = extractTokenNumber(bufferData.substring(0, (bufferData.length() - 1)));
                            // protocolData = extractTokenNumber(bufferData.substring(0, bufferData.indexOf((char) 13)));
                             //Log.d("awd", "@inner protocol = " + bufferData);
                        }
                        bufferData = bufferData.substring(bufferData.indexOf((char) 13) + 1);
                    } else {
                        break;
                    }
                }
            } else if (checkstatus.equals("true")) {
                String json_string = strProtocol;
                int isJsonString = json_string.indexOf("{");
                System.out.println("Json Array123 ::: " + json_string);
                if (!json_string.isEmpty() && !json_string.equals(null)) {
                    try {
                        if (isJsonString == 0) {
                            System.out.println("Json Array123 ::: " + json_string);
                            JSONObject root = new JSONObject(json_string);
                            JSONArray parseData = root.getJSONArray("DataToKeypad");
                            for (int i = 0; i < parseData.length(); i++) {
                                JSONObject cc = parseData.getJSONObject(i);

                                myType = cc.getString("Action");
                                mytoken = cc.getString("CurrentToken");
                                mywaiting = cc.getString("Waiting");
                                myID = cc.getString("MaxWait");
                            }
                        }

                    } catch (JSONException e) {
                        // sock = null;
                        //Toast.makeText(getApplicationContext(), "Msg => " + line, Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    } catch (Exception e) {
                        //sock = null;
                        e.printStackTrace();
                    }
                }

            }

            /*-------------------------------------------------------------*/


        } catch (Exception e) {
            // TODO: handle exception
            // Log.d("awd",
            // "error while processing protocol string -- "
            // + e.getMessage());
            protocolData.setToBeIgnored(true);
            protocolData.setErrorMessage(e.getMessage());
        }
        return protocolData;
    }

    static String englishToArabicNumber(int engNumber, int minDigit) {
        String arabic = "";
        int len1 = String.valueOf(engNumber).length();
        len1 = Math.max(len1, minDigit);
        int x = 0;
        for (x = len1; x > 0; x--) {
            int num1 = 0;
            if (x - 1 > 0) {
                num1 = (engNumber / (int) Math.pow(10, (x - 1))) % 10;
            } else {
                num1 = (engNumber % 10);
            }
            switch (num1) {
                case 0:
                    arabic += "\u0660";
                    break;
                case 1:
                    arabic += "\u0661";
                    break;
                case 2:
                    arabic += "\u0662";
                    break;
                case 3:
                    arabic += "\u0663";
                    break;
                case 4:
                    arabic += "\u0664";
                    break;
                case 5:
                    arabic += "\u0665";
                    break;
                case 6:
                    arabic += "\u0666";
                    break;
                case 7:
                    arabic += "\u0667";
                    break;
                case 8:
                    arabic += "\u0668";
                    break;
                case 9:
                    arabic += "\u0669";
                    break;
            }
        }
        return arabic;
    }

    static String englishDigits(int token, int minDigit) {
        String english = "";
        int len1 = String.valueOf(token).length();
        len1 = Math.max(len1, minDigit);
        int x = 0;
        for (x = len1; x > 0; x--) {
            int num1 = 0;
            if (x - 1 > 0) {
                num1 = (token / (int) Math.pow(10, (x - 1))) % 10;
            } else {
                num1 = (token % 10);
            }
            switch (num1) {
                case 0:
                    english += "0";
                    break;
                case 1:
                    english += "1";
                    break;
                case 2:
                    english += "2";
                    break;
                case 3:
                    english += "3";
                    break;
                case 4:
                    english += "4";
                    break;
                case 5:
                    english += "5";
                    break;
                case 6:
                    english += "6";
                    break;
                case 7:
                    english += "7";
                    break;
                case 8:
                    english += "8";
                    break;
                case 9:
                    english += "9";
                    break;
            }
        }
        return english;
    }

}
