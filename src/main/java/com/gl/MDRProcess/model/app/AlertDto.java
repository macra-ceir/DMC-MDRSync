package com.gl.MDRProcess.model.app;



public class AlertDto {

    private String alertId;

    private String alertMessage;

    private String alertProcess;

    private String userId;
    private String serverName;
    private String featureName;
    private String txnId;

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAlertProcess() {
        return alertProcess;
    }

    public void setAlertProcess(String alertProcess) {
        this.alertProcess = alertProcess;
    }

    public String getAlertMessage() {
        return alertMessage;
    }

    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }

    public String getAlertId() {
        return alertId;
    }

    public void setAlertId(String alertId) {
        this.alertId = alertId;
    }

    @Override
    public String toString() {
        return "AlertDto{" +
                "alertId='" + alertId + '\'' +
                ", alertMessage='" + alertMessage + '\'' +
                ", alertProcess='" + alertProcess + '\'' +
                ", userId='" + userId + '\'' +
                ", serverName='" + serverName + '\'' +
                ", featureName='" + featureName + '\'' +
                ", txnId='" + txnId + '\'' +
                '}';
    }
}
