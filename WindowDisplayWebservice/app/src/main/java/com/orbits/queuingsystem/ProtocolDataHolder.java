package com.orbits.queuingsystem;


class ProtocolDataHolder {
    int isLogin;
    // private boolean vip;
    String tokenLength, prefixLength;
    /*
     * nirmal -- this class will hold the data extracted from the
     * protocol-string
     */
    private boolean toBeIgnored;
    private String errorMessage;
    // private int totalPeopleWaiting;
    // private ArrayList<String> serviceNameList;
    // private HashMap<String, Integer> serviceMap;
    private String tokenNumber;
    private boolean ringBell;
    private PROTOCOL_TYPE protocolType;

    public ProtocolDataHolder(boolean toBeIgnored, String strErrorMessage) {
        super();
        this.toBeIgnored = toBeIgnored;
        this.errorMessage = strErrorMessage;
    }

    public ProtocolDataHolder() {
        this.toBeIgnored = false;
        this.errorMessage = "";
    }

    public PROTOCOL_TYPE getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(PROTOCOL_TYPE protocolType) {
        this.protocolType = protocolType;
    }

    public boolean isToBeIgnored() {
        return toBeIgnored;
    }

    public void setToBeIgnored(boolean corrupted) {
        this.toBeIgnored = corrupted;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getTokenLength() {
        return tokenLength;
    }

    public void setTokenLength(String tokenLength) {
        this.tokenLength = tokenLength;
    }

    public String getPrefixLength() {
        return prefixLength;
    }

    public void setPrefixLength(String prefixLength) {
        this.prefixLength = prefixLength;
    }

    public void setErrorMessage(String strErrorMessage) {
        this.errorMessage = strErrorMessage;
    }

    public String getTokenNumber() {
        return tokenNumber;
    }

    public void setTokenNumber(String tokenNumber) {
        this.tokenNumber = tokenNumber;
    }

    public int isLogin() {
        return isLogin;
    }

    public void setLogin(int login) {
        isLogin = login;
    }

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


    boolean getRingBell() {
        return ringBell;
    }

    void setRingBell(boolean ringBell) {
        this.ringBell = ringBell;
    }

    enum PROTOCOL_TYPE {
        TOTAL_WAITING_PEOPLE, ALL_SERVICES, TOKEN_NUMBER
    }

    /*
     * public boolean isVip() { return vip; }
     */

    /*
     * public void setVip(boolean vip) { this.vip = vip; }
     */

}
