package com.orbits.queuingsystem


internal class ProtocolDataHolder {
    var isLogin: Int = 0

    // private boolean vip;
    var tokenLength: String? = null
    var prefixLength: String? = null

    /*
    * nirmal -- this class will hold the data extracted from the
    * protocol-string
    */
    var isToBeIgnored: Boolean
    var errorMessage: String?

    // private int totalPeopleWaiting;
    // private ArrayList<String> serviceNameList;
    // private HashMap<String, Integer> serviceMap;
    var tokenNumber: String? = null

    /*
     * public int getTotalPeopleWaiting() { return totalPeopleWaiting; }
     */
    /*
        * public void setTotalPeopleWaiting(int totalPeopleWaiting) {
        * this.totalPeopleWaiting = totalPeopleWaiting; }
        */
    /*
        * public ArrayList<String> getServiceNameList() { return serviceNameList; }
        */
    /*
        * public void setServiceNameList(ArrayList<String> serviceNameList) {
        * this.serviceNameList = serviceNameList; }
        */
    /*
        * public HashMap<String, Integer> getServiceMap() { return serviceMap; }
        */
    /*
        * public void setServiceMap(HashMap<String, Integer> serviceMap) {
        * this.serviceMap = serviceMap; }
        */
    var ringBell: Boolean = false
    var protocolType: PROTOCOL_TYPE? = null

    constructor(toBeIgnored: Boolean, strErrorMessage: String?) : super() {
        this.isToBeIgnored = toBeIgnored
        this.errorMessage = strErrorMessage
    }

    constructor() {
        this.isToBeIgnored = false
        this.errorMessage = ""
    }


    internal enum class PROTOCOL_TYPE {
        TOTAL_WAITING_PEOPLE, ALL_SERVICES, TOKEN_NUMBER
    } /*
     * public boolean isVip() { return vip; }
     */
    /*
     * public void setVip(boolean vip) { this.vip = vip; }
     */
}
