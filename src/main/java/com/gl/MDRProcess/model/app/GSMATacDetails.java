package com.gl.MDRProcess.model.app;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

//@Entity
//@Table(name = "gsma_tac_detail")
public class GSMATacDetails {
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Integer id;
	
	@CreationTimestamp
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	@Column(name="created_on", columnDefinition="timestamp DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime createdOn;

	@UpdateTimestamp
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	@Column(name="modified_on", columnDefinition="timestamp DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime modifiedOn;
	
	@Column(name="tac", length=8, unique=true, columnDefinition="varchar(8) DEFAULT ''")
	private String tac;
	
	@Column(name="manufacturer", length=50, columnDefinition="varchar(50) DEFAULT ''")
	private String manufacturer;

	@Column(name="model_name", length=50, columnDefinition="varchar(50) DEFAULT ''")
	private String modelName;
	
	@Column(name="marketing_name", length=50, columnDefinition="varchar(50) DEFAULT ''")
	private String marketingName;

	@Column(name="brand_name", length=50, columnDefinition="varchar(50) DEFAULT ''")
	private String brandName;
	
	@Column(name="allocation_date", columnDefinition="timestamp DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime allocationDate;
	
	@Column(name="organisation_id", length=25, columnDefinition="varchar(25) DEFAULT ''")
	private String organizationId;

	@Column(name="device_type", length=50, columnDefinition="varchar(50) DEFAULT ''")
	private String deviceType;
	
	@Column(name="bluetooth", length=3, columnDefinition="varchar(3) DEFAULT ''")
	private String bluetooth;
	
	@Column(name="nfc", length=3, columnDefinition="varchar(3) DEFAULT ''")
	private String nfc;
	
	@Column(name="wlan", length=3, columnDefinition="varchar(3) DEFAULT ''")
	private String wlan;
	
	@Column(name="removable_uicc", columnDefinition="int DEFAULT '0'")
	private Integer removableUICC;
	
	@Column(name="removable_euicc", columnDefinition="int DEFAULT '0'")
	private Integer removableEUICC;
	
	@Column(name="nonremovable_uicc", columnDefinition="int DEFAULT '0'")
	private Integer nonRemovableUICC;
	
	@Column(name="nonremovable_euicc", columnDefinition="int DEFAULT '0'")
	private Integer nonRemovableEUICC;
	
	@Column(name="sim_slot", columnDefinition="int DEFAULT '0'")
	private Integer simSlot;
	
	@Column(name="imei_quantity", columnDefinition="int DEFAULT '0'")
	private Integer imeiQuantity;
	
	@Column(name="operating_system", length=50, columnDefinition="varchar(50) DEFAULT ''")
	private String operatingSystem;
	
	@Column(name="oem", length=100, columnDefinition="varchar(100) DEFAULT ''")
	private String oem;
	
	@Column(name="action", length=10, columnDefinition="varchar(10) DEFAULT ''")
	private String action;
	
	@Column(name="network_technology", length=30, columnDefinition="varchar(30) DEFAULT ''")
	private String networkTechnology;
	
	@Column(name="network_specific_identifier", columnDefinition="int DEFAULT '0'")
	private Integer networkSpecificIdentifier;
	
	public GSMATacDetails() {}
	
	public GSMATacDetails(String tac, String modelName, String brandName, LocalDateTime allocationDate, 
			String networkTechnology, String bluetooth, String nfc, String wlan, String deviceType, Integer imeiQuantity,
			String manufacturer, String marketingName, String oem, String operatingSystem, Integer removableUICC,
			Integer removableEUICC, Integer nonRemovableUICC, Integer nonRemovableEUICC, Integer simSlot, Integer networkSpecificIdentifier) {
		this.tac = tac;
		this.modelName = modelName;
		this.brandName = brandName;
		this.allocationDate = allocationDate;
		this.networkTechnology = networkTechnology;
		this.bluetooth = bluetooth;
		this.nfc = nfc;
		this.wlan = wlan;
		this.deviceType = deviceType;
		this.imeiQuantity = imeiQuantity;
		this.manufacturer = manufacturer;
		this.marketingName = marketingName;
		this.oem = oem;
		this.operatingSystem = operatingSystem;
		this.removableUICC = removableUICC;
		this.removableEUICC = removableEUICC;
		this.nonRemovableUICC = nonRemovableUICC;
		this.nonRemovableEUICC = nonRemovableEUICC;
		this.simSlot = simSlot;
		this.networkSpecificIdentifier = networkSpecificIdentifier;
	}

//	public Integer getId() {
//		return id;
//	}
//
//	public void setId(Integer id) {
//		this.id = id;
//	}

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

	public String getTac() {
		return tac;
	}

	public void setTac(String tac) {
		this.tac = tac;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getMarketingName() {
		return marketingName;
	}

	public void setMarketingName(String marketingName) {
		this.marketingName = marketingName;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public LocalDateTime getAllocationDate() {
		return allocationDate;
	}

	public void setAllocationDate(LocalDateTime allocationDate) {
		this.allocationDate = allocationDate;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getBluetooth() {
		return bluetooth;
	}

	public void setBluetooth(String bluetooth) {
		this.bluetooth = bluetooth;
	}

	public String getNfc() {
		return nfc;
	}

	public void setNfc(String nfc) {
		this.nfc = nfc;
	}

	public String getWlan() {
		return wlan;
	}

	public void setWlan(String wlan) {
		this.wlan = wlan;
	}

	public Integer getRemovableUICC() {
		return removableUICC;
	}

	public void setRemovableUICC(Integer removableUICC) {
		this.removableUICC = removableUICC;
	}

	public Integer getRemovableEUICC() {
		return removableEUICC;
	}

	public void setRemovableEUICC(Integer removableEUICC) {
		this.removableEUICC = removableEUICC;
	}

	public Integer getNonRemovableUICC() {
		return nonRemovableUICC;
	}

	public void setNonRemovableUICC(Integer nonRemovableUICC) {
		this.nonRemovableUICC = nonRemovableUICC;
	}

	public Integer getNonRemovableEUICC() {
		return nonRemovableEUICC;
	}

	public void setNonRemovableEUICC(Integer nonRemovableEUICC) {
		this.nonRemovableEUICC = nonRemovableEUICC;
	}

	public Integer getSimSlot() {
		return simSlot;
	}

	public void setSimSlot(Integer simSlot) {
		this.simSlot = simSlot;
	}

	public Integer getImeiQuantity() {
		return imeiQuantity;
	}

	public void setImeiQuantity(Integer imeiQuantity) {
		this.imeiQuantity = imeiQuantity;
	}

	public String getOperatingSystem() {
		return operatingSystem;
	}

	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	public String getOem() {
		return oem;
	}

	public void setOem(String oem) {
		this.oem = oem;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getNetworkTechnology() {
		return networkTechnology;
	}

	public void setNetworkTechnology(String networkTechnology) {
		this.networkTechnology = networkTechnology;
	}

	public Integer getNetworkSpecificIdentifier() {
		return networkSpecificIdentifier;
	}

	public void setNetworkSpecificIdentifier(Integer networkSpecificIdentifier) {
		this.networkSpecificIdentifier = networkSpecificIdentifier;
	}

//	@Override
//	public String toString() {
//		return "GSMATacDetails [id=" + id + ", createdOn=" + createdOn + ", modifiedOn=" + modifiedOn + ", tac=" + tac
//				+ ", manufacturer=" + manufacturer + ", modelName=" + modelName + ", marketingName=" + marketingName
//				+ ", brandName=" + brandName + ", allocationDate=" + allocationDate + ", organizationId="
//				+ organizationId + ", deviceType=" + deviceType + ", bluetooth=" + bluetooth + ", nfc=" + nfc
//				+ ", wlan=" + wlan + ", removableUICC=" + removableUICC + ", removableEUICC=" + removableEUICC
//				+ ", nonRemovableUICC=" + nonRemovableUICC + ", nonRemovableEUICC=" + nonRemovableEUICC + ", simSlot="
//				+ simSlot + ", imeiQuantity=" + imeiQuantity + ", operatingSystem=" + operatingSystem + ", oem=" + oem
//				+ ", action=" + action + ", networkTechnology=" + networkTechnology + "]";
//	}

}
