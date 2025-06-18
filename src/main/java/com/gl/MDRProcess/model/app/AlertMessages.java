package com.gl.MDRProcess.model.app;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "cfg_feature_alert")
public class AlertMessages {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="alert_id", length=20, columnDefinition="varchar(20) DEFAULT ''", unique=true)
	private String alertId = "";
	
	@Column(name="description", length=250, columnDefinition="varchar(250) DEFAULT ''", unique=true)
	private String description = "";
	
	@Column(name="feature", length=250, columnDefinition="varchar(250) DEFAULT ''", unique=true)
	private String feature = "";
	
	@CreationTimestamp
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	@Column(name="created_on", columnDefinition="timestamp DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime createdOn = LocalDateTime.now();
	
	@CreationTimestamp
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	@Column(name="modified_on", columnDefinition="timestamp DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime modifiedOn = LocalDateTime.now();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAlertId() {
		return alertId;
	}

	public void setAlertId(String alertId) {
		this.alertId = alertId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
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

	@Override
	public String toString() {
		return "AlertMessages [id=" + id + ", alertId=" + alertId + ", description=" + description + ", feature="
				+ feature + ", createdOn=" + createdOn + ", modifiedOn=" + modifiedOn + "]";
	}
	
	
}
