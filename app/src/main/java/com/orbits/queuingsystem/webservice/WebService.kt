package com.orbits.queuingsystem.webservice

import com.orbits.queuingsystem.MainActivity
import com.orbits.queuingsystem.TokenActivity
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE

object WebService {
    private const val NAMESPACE = "http://tempuri.org/"

    //Webservice URL - It is asmx file location hosted in the server in case of .Net
    private const val URL = "/wsEval.asmx"

    //SOAP Action URI again http://tempuri.org
    private const val SOAP_ACTION = "http://tempuri.org/"

    @JvmStatic
    fun getTokenNumber(counterId: String?, webMethName: String): String {
        val serverName = MainActivity.sharePrefSettings?.getString(
            "Server_name", ""
        )
        val portNumber = MainActivity.sharePrefSettings?.getString("QueueTCP_port", "1020")
        var resTxt: String
        val request = SoapObject(NAMESPACE, webMethName)
        request.addProperty("CounterId", counterId)
        val envelope = SoapSerializationEnvelope(
            SoapEnvelope.VER11
        )
        envelope.dotNet = true
        envelope.setOutputSoapObject(request)
        val androidHttpTransport =
            HttpTransportSE("http://" + serverName + ":" + portNumber + URL, 20000)

        try {
            TokenActivity.connected = true
            androidHttpTransport.call(SOAP_ACTION + webMethName, envelope)
            val response = envelope.response as Any
            resTxt = response.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            TokenActivity.connected = false
            //System.out.println(" Error occured in web service ================" + e);
            // resTxt = "Error occured";
            resTxt = e.message.toString()
        }
        return resTxt
    }
}
