package com.orbits.queuingsystem.webservice;

import com.orbits.queuingsystem.MainActivity;
import com.orbits.queuingsystem.TokenActivity;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class BranchWebService {
    private static String NAMESPACE = "http://tempuri.org/";
    private static String URL = "/wsBranchDetails.asmx";
    private static String SOAP_ACTION = "http://tempuri.org/";

    public static String getBranchName(String branchCode, String webMethName) {
        String serverName = MainActivity.sharePrefSettings.getString(
                "Server_name", "");
        String portNumber = MainActivity.sharePrefSettings
                .getString("QueueTCP_port", "1020");
        String resTxt;
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        request.addProperty("BranchCode", branchCode);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE("http://" + serverName + ":" + portNumber + URL, 20000);

        try {
            TokenActivity.connected = true;
            androidHttpTransport.call(SOAP_ACTION + webMethName, envelope);
            Object response = (Object) envelope.getResponse();
            resTxt = response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            TokenActivity.connected = false;
            resTxt = e.getMessage().toString();
        }
        return resTxt;
    }

}
