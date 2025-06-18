package com.gl.MDRProcess.model.app;


import java.time.LocalDateTime;

public class ModuleAuditTrail {

    private LocalDateTime createdOn;
    private Integer statusCode;
    private String featureName;
    private String moduleName;
    private Integer count;
    private Long timeTaken;

    public Long getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(Long timeTaken) {
        this.timeTaken = timeTaken;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    @Override
    public String toString() {
        return "ModuleAuditTrail{" +
                "createdOn=" + createdOn +
                ", statusCode=" + statusCode +
                ", featureName='" + featureName + '\'' +
                ", moduleName='" + moduleName + '\'' +
                ", count=" + count +
                ", timeTaken=" + timeTaken +
                '}';
    }
}
