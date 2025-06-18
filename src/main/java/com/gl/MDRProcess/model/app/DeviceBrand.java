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
@Table(name = "dev_brand_name")
public class DeviceBrand {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="brand_name", length=50, columnDefinition="varchar(50) DEFAULT ''", unique=true)
	private String brandName = "";
	
	@CreationTimestamp
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	@Column(name="created_on", columnDefinition="timestamp DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime createdOn = LocalDateTime.now();
	
	public DeviceBrand() {}
	
	public DeviceBrand(String brandName) {
		this.brandName = brandName;
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

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	@Override
	public String toString() {
		return "DeviceBrand [id=" + id + ", brandName=" + brandName + ", createdOn=" + createdOn + "]";
	}
	
}
