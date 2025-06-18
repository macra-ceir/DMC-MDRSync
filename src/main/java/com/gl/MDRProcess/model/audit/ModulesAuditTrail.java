package com.gl.MDRProcess.model.audit;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "modules_audit_trail")
public class ModulesAuditTrail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@CreationTimestamp
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	@Column(name="created_on", columnDefinition="timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime createdOn = LocalDateTime.now();

	@UpdateTimestamp
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	@Column(name="modified_on", columnDefinition="timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime modifiedOn = LocalDateTime.now();
	
	@Column(name="execution_time", length=3, columnDefinition="int NOT NULL DEFAULT '0'")
	private Integer executionTime = 0;
	
	@Column(name="status_code", length=3, columnDefinition="int NOT NULL DEFAULT '0'")
	private Integer statusCode;
	
	@Column(name="status", length=10, columnDefinition="varchar(50) NOT NULL DEFAULT ''")
	@ColumnDefault(value = "''")
	private String status = "";
	
	@Column(name="error_message", length=255, columnDefinition="varchar(255) NOT NULL DEFAULT ''")
	@ColumnDefault(value = "''")
	private String errorMessage = "";
	
	@Column(name="module_name", length=50, columnDefinition="varchar(150) NOT NULL DEFAULT ''")
	@ColumnDefault(value = "''")
	private String moduleName = "";
	
	@Column(name="feature_name", length=50, columnDefinition="varchar(50) NOT NULL DEFAULT ''")
	@ColumnDefault(value = "''")
	private String feature = "";
	
	@Column(name="action", length=20, columnDefinition="varchar(20) NOT NULL DEFAULT ''")
	@ColumnDefault(value = "''")
	private String action = "";
	
	@Column(name="count", length=8, columnDefinition="int NOT NULL DEFAULT '0'")
	private Integer count = 0;
	
	@Column(name="info", length=255, columnDefinition="varchar(250) NOT NULL DEFAULT ''")
	@ColumnDefault(value = "''")
	private String info = "";
	
	@Column(name="server_name", length=30, columnDefinition="varchar(30) NOT NULL DEFAULT ''")
	private String serverName = "NA";
	
	public ModulesAuditTrail() {}
	
	public ModulesAuditTrail(Integer executionTime, Integer statusCode, String status,
			String moduleName, String feature, String action, Integer count, String serverName) {
		this.executionTime = executionTime;
		this.statusCode    = statusCode;
		this.status        = status;
		this.moduleName    = moduleName;
		this.feature       = feature;
		this.action        = action;
		this.count         = count;
		this.serverName    = serverName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	public LocalDateTime getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(LocalDateTime modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public Integer getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(Integer executionTime) {
		this.executionTime = executionTime;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

}
