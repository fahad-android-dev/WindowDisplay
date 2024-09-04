package com.orbits.queuingsystem.webservice

import com.orbits.queuingsystem.MainActivity
import com.orbits.queuingsystem.TokenActivity
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE

object BranchWebService {
    private const val NAMESPACE = "http://tempuri.org/"
    private const val URL = "/wsBranchDetails.asmx"
    private const val SOAP_ACTION = "http://tempuri.org/"

    @JvmStatic
    fun getBranchName(branchCode: String?, webMethName: String): String {
        val serverName = MainActivity.sharePrefSettings?.getString(
            "Server_name", ""
        )
        val portNumber = MainActivity.sharePrefSettings
            ?.getString("QueueTCP_port", "1020")
        var resTxt: String
        val request = SoapObject(NAMESPACE, webMethName)
        request.addProperty("BranchCode", branchCode)
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
            resTxt = e.message.toString()
        }
        return resTxt
    }
}
