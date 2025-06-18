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
@Table(name = "dev_model_name")
public class DeviceModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="brand_name", length=50, columnDefinition="varchar(50) DEFAULT ''")
	private String brandName = "";
	
	@Column(name="brand_name_id", columnDefinition="int DEFAULT '0'")
	private Integer brandNameId = 0;
	
	@Column(name="model_name", length=50, columnDefinition="varchar(50) DEFAULT ''")
	private String modelName = "";
	
	@CreationTimestamp
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	@Column(name="created_on", columnDefinition="timestamp DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime createdOn = LocalDateTime.now();
	
	public DeviceModel() {}
	
	public DeviceModel(String brandName, Integer brandNameId, String modelName) {
		this.brandName = brandName;
		this.brandNameId = brandNameId;
		this.modelName   = modelName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public Integer getBrandNameId() {
		return brandNameId;
	}

	public void setBrandNameId(Integer brandNameId) {
		this.brandNameId = brandNameId;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	@Override
	public String toString() {
		return "DeviceModel [id=" + id + ", brandName=" + brandName + ", brandNameId=" + brandNameId + ", modelName="
				+ modelName + ", createdOn=" + createdOn + "]";
	}
	
}
