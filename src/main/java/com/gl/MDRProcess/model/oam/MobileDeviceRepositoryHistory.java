package com.gl.MDRProcess.model.oam;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gl.MDRProcess.model.app.MobileDeviceRepository;

@Entity
@Table(name = "mobile_device_repository_his")
public class MobileDeviceRepositoryHistory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="updated_on", columnDefinition="timestamp DEFAULT CURRENT_TIMESTAMP")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updatedOn;
	
	@NotNull
	private Integer mdrId;
	
	@Column(name="allocation_date", columnDefinition="timestamp DEFAULT NULL")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime allocationDate;
	
	@Column(name="announce_date", columnDefinition="timestamp DEFAULT NULL")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime announceDate;
	
	@Column(name="band_detail", length=50, columnDefinition="varchar(50) DEFAULT ''")
	private String bandDetail = "";

	@Column(name="battery_capacity", length=5, columnDefinition="int DEFAULT '0'")
	private Integer batteryCapacity = 0;

	@Column(name="battery_charging", length=100, columnDefinition="varchar(100) DEFAULT ''")
	private String batteryCharging = "";
	
	@Column(name="battery_type", length=50, columnDefinition="varchar(50) DEFAULT ''")
	private String batteryType = "";

	@Column(name="body_dimension", length=50, columnDefinition="varchar(50) DEFAULT ''")
	private String bodyDimension = "";

	@Column(name="body_weight", length=20, columnDefinition="varchar(20) DEFAULT ''")
	private String bodyWeight = "";

	@Column(name="brand_name", length=50, columnDefinition="varchar(50) DEFAULT ''")
	private String brandName = "";

	@Column(name="color", length=100, columnDefinition="varchar(100) DEFAULT ''")
	private String colors = "";

	@Column(name="comms_bluetooth", length=100, columnDefinition="varchar(100) DEFAULT ''")
	private String commsBluetooth = "";

	@Column(name="comms_gps", length=100, columnDefinition="varchar(100) DEFAULT ''")
	private String commsGPS = "";
	
	@Column(name="comms_nfc", length=1, columnDefinition="int DEFAULT '0'")
	private Integer commsNFC = 0;

	@Column(name="comms_radio", length=1, columnDefinition="int DEFAULT '0'")
	private Integer commsRadio = 0;

	@Column(name="comms_usb", length=50, columnDefinition="varchar(50) DEFAULT ''")
	private String commsUSB = "";

	@Column(name="comms_wlan", length=100, columnDefinition="varchar(100) DEFAULT ''")
	private String commsWLAN = "";
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	@Column(name="created_on", columnDefinition="timestamp DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime createdOn;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	@Column(name="modified_on", columnDefinition="timestamp DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime modifiedOn;
	
	@Column(name="device_id", length=8, columnDefinition="varchar(8) DEFAULT '0'")
	private String deviceId = "0";
	
	@Column(name="marketing_name", length=100, columnDefinition="varchar(100) DEFAULT ''")
	private String marketingName = "";
	
	@Column(length=100, columnDefinition="varchar(100) DEFAULT ''")
	private String manufacturer = "";
	
	@Column(name="manufacturing_location", length=100, columnDefinition="varchar(100) DEFAULT ''")
	private String manufacturingLocation = "";
	
	@Column(name="model_name", length=50, columnDefinition="varchar(50) DEFAULT ''")
	private String modelName = "";
	
	@Column(name="oem", length=100, columnDefinition="varchar(100) DEFAULT ''")
	private String oem = "";
	
	@Column(name="organization_id", length=25, columnDefinition="varchar(25) DEFAULT ''")
	private String organizationId = "";
	
	@Column(name="device_type", length=50, columnDefinition="varchar(50) DEFAULT ''")
	private String deviceType = "";
	
	@Column(name="imei_quantity", length=1, columnDefinition="int DEFAULT '0'")
	private Integer imeiQuantity = 0;
	
	@Column(name="sim_slot", length=1, columnDefinition="int DEFAULT '0'")
	private Integer simSlot = 0;
	
	@Column(name="esim_support", length=1, columnDefinition="int DEFAULT '0'")
	private Integer esimSupport = 0; // Configuration table entry 'Yes', 'No'
	
	@Column(name="softsim_support", length=1, columnDefinition="int DEFAULT '0'")
	private Integer softsimSupport = 0; // Configuration table entry 'Yes', 'No'
	
	@Column(name="sim_type", length=15, columnDefinition="varchar(15) DEFAULT ''")
	private String simType = "";
	
	@Column(name="os", length=50, columnDefinition="varchar(50) DEFAULT ''")
	private String os = "";

	@Column(name="os_current_version", length=50, columnDefinition="varchar(50) DEFAULT ''")
	private String osCurrentVersion = "";
	
	@Column(name="os_base_version", length=50, columnDefinition="varchar(50) DEFAULT ''")
	private String osBaseVersion = "";
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Column(name="launch_date", columnDefinition="timestamp DEFAULT NULL")
	private LocalDateTime launchDate;
	
	@Column(name="device_status", length=20, columnDefinition="varchar(20) DEFAULT ''")
	private String deviceStatus = "";
	
	@Column(name="discontinue_date", columnDefinition="timestamp DEFAULT NULL")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime discontinueDate;
	
	@Column(name="network_technology_gsm", length=1, columnDefinition="int DEFAULT '0'") //2G
	private Integer networkTechnologyGSM = 0; // Configuration table entry 'Yes', 'No'
	
	@Column(name="network_technology_cdma", length=1, columnDefinition="int DEFAULT '0'")
	private Integer networkTechnologyCDMA = 0; // Configuration table entry 'Yes', 'No'
	
	@Column(name="network_technology_evdo", length=1, columnDefinition="int DEFAULT '0'")
	private Integer networkTechnologyEVDO = 0; // Configuration table entry 'Yes', 'No'
	
	@Column(name="network_technology_lte", length=1, columnDefinition="int DEFAULT '0'")
	private Integer networkTechnologyLTE = 0; // Configuration table entry 'Yes', 'No'
	
	@Column(name="network_technology_5g", length=1, columnDefinition="int DEFAULT '0'")
	private Integer networkTechnology5G = 0; // Configuration table entry 'Yes', 'No'
	
	@Column(name="network_technology_6g", length=1, columnDefinition="int DEFAULT '0'")
	private Integer networkTechnology6G = 0; // Configuration table entry 'Yes', 'No'
	
	@Column(name="network_technology_7g", length=1, columnDefinition="int DEFAULT '0'")
	private Integer networkTechnology7G = 0; // Configuration table entry 'Yes', 'No'
	
	@Column(name="network_2g_band", length=100, columnDefinition="varchar(100) DEFAULT ''")
	private String network2GBand = "";
	
	@Column(name="network_3g_band", length=100, columnDefinition="varchar(100) DEFAULT ''")
	private String network3GBand = "";
	
	@Column(name="network_4g_band", length=100, columnDefinition="varchar(100) DEFAULT ''")
	private String network4GBand = "";
	
	@Column(name="network_5g_band", length=100, columnDefinition="varchar(100) DEFAULT ''")
	private String network5GBand = "";
	
	@Column(name="network_6g_band", length=100, columnDefinition="varchar(100) DEFAULT ''")
	private String network6GBand = "";
	
	@Column(name="network_7g_band", length=100, columnDefinition="varchar(100) DEFAULT ''")
	private String network7GBand = "";
	
	@Column(name="network_speed", length=100, columnDefinition="varchar(100) DEFAULT ''")
	private String networkSpeed = "";
	
	@Column(name="display_type", length=50, columnDefinition="varchar(50) DEFAULT ''")
	private String displayType = "";
	
	@Column(name="display_size", length=50, columnDefinition="varchar(50) DEFAULT ''")
	private String displaySize = "";
	
	@Column(name="display_resolution", length=50, columnDefinition="varchar(50) DEFAULT ''")
	private String displayResolution = "";
	
	@Column(name="display_protection", length=50, columnDefinition="varchar(50) DEFAULT ''")
	private String displayProtection = "";
	
	@Column(name="platform_chipset", length=100, columnDefinition="varchar(100) DEFAULT ''")
	private String platformChipset = "";
	
	@Column(name="platform_cpu", length=100, columnDefinition="varchar(100) DEFAULT ''")
	private String platformCPU = "";
	
	@Column(name="platform_gpu", length=50, columnDefinition="varchar(50) DEFAULT ''")
	private String platformGPU = "";
	
	@Column(name="memory_card_slot", length=1, columnDefinition="int DEFAULT '0'")
	private Integer memoryCardSlot = 0; // Changed from String to integer. Configuration table entry 'Yes', 'No'
	
	@Column(name="memory_internal", length=5, columnDefinition="int DEFAULT '0'")
	private Integer memoryInternal = 0;
	
	@Column(name="ram", length=20, columnDefinition="varchar(20) DEFAULT ''")
	private String ram = "0";
	
	@Column(name="main_camera_type", length=1, columnDefinition="int DEFAULT '0'")
	private Integer mainCameraType = 0; // Changed from String to integer. Configuration table entry 'Single Camera', 'Dual Camera', 'Triple Camera'
	
	@Column(name="main_camera_spec", length=50, columnDefinition="varchar(50) DEFAULT ''")
	private String mainCameraSpec = ""; // Changed from int to varchar as per example
	
	@Column(name="main_camera_feature", length=50, columnDefinition="varchar(50) DEFAULT ''")
	private String mainCameraFeature = ""; // Changed from int to varchar as per example
	
	@Column(name="main_camera_video", length=50, columnDefinition="varchar(50) DEFAULT ''")
	private String mainCameraVideo = "";
	
	@Column(name="selfie_camera_type", length=1, columnDefinition="int DEFAULT '0'")
	private Integer selfieCameraType = 0; // Changed from String to integer. Configuration table entry 'Single Camera', 'Dual Camera', 'Triple Camera'
	
	@Column(name="selfie_camera_spec", length=50, columnDefinition="varchar(50) DEFAULT ''")
	private String selfieCameraSpec = ""; // Changed from int to varchar as per example
	
	@Column(name="selfie_camera_feature", length=50, columnDefinition="varchar(50) DEFAULT ''")
	private String selfieCameraFeature = ""; // Changed from int to varchar as per example
	
	@Column(name="selfie_camera_video", length=50, columnDefinition="varchar(50) DEFAULT ''")
	private String selfieCameraVideo = "";
	
	@Column(name="sound_loudspeaker", length=1, columnDefinition="int DEFAULT '0'")
	private Integer soundLoudspeaker = 0; // Configuration table entry 'Yes', 'No'
	
	@Column(name="sound_3_5mm_jack", length=1, columnDefinition="int DEFAULT '0'")
	private Integer sound35mmJack = 0; // Configuration table entry 'Yes', 'No'
	
	@Column(name="sensor", length=50, columnDefinition="varchar(50) DEFAULT ''")
	private String sensor = "";
	
	@Column(name="removable_uicc", length=2, columnDefinition="int DEFAULT '0'")
	private Integer removableUICC = 0;
	
	@Column(name="removable_euicc", length=2, columnDefinition="int DEFAULT '0'")
	private Integer removableEUICC = 0;
	
	@Column(name="nonremovable_uicc", length=2, columnDefinition="int DEFAULT '0'")
	private Integer nonremovableUICC = 0;
	
	@Column(name="nonremovable_euicc", length=2, columnDefinition="int DEFAULT '0'")
	private Integer nonremovableEUICC = 0;
	
	@Column(name="launch_price_asian_market", columnDefinition="double DEFAULT '0'")
	private Double launchPriceAsianMarket = 0.0;
	
	@Column(name="launch_price_us_market", columnDefinition="double DEFAULT '0'")
	private Double launchPriceUSMarket = 0.0;
	
	@Column(name="launch_price_europe_market", columnDefinition="double DEFAULT '0'")
	private Double launchPriceEuropeMarket = 0.0;
	
	@Column(name="launch_price_international_market", columnDefinition="double DEFAULT '0'")
	private Double launchPriceInternationalMarket = 0.0;
	
	@Column(name="launch_price_cambodia_market", columnDefinition="double DEFAULT '0'")
	private Double launchPriceCambodiaMarket = 0.0;
	
	@Column(name="custom_price_of_device", columnDefinition="double DEFAULT '0'")
	private Double customPriceOfDevice = 0.0;
	
	@Column(name="source_of_cambodia_market_price", length=100, columnDefinition="varchar(100) DEFAULT ''")
	private String sourceOfCambodiaMarketPrice = "";
	
	@Column(name="reported_global_issue", length=255, columnDefinition="varchar(255) DEFAULT ''") // Need to inform about field length change
	private String reportedGlobalIssue = "";
	
	@Column(name="reported_local_issue", length=255, columnDefinition="varchar(255) DEFAULT ''") // Need to inform about field length change
	private String reportedLocalIssue = "";
	
	@Column(name="device_state", length=3, columnDefinition="int DEFAULT '0'")
	private Integer deviceState = 0;
	
	@Column(name="user_id", columnDefinition="int DEFAULT '0'")
	private Integer userId;
	
	@Column(name="remark", length=1000, columnDefinition="varchar(1000) DEFAULT ''")
	private String remark = "";
	
	@Column(name="network_specific_identifier", columnDefinition="int DEFAULT '0'")
	private Integer networkSpecificIdentifier;
	
	@Transient
	private long featureId;
	
	@Transient
	private String userDisplayName  = "NA";
	
	@Transient
	private String publicIp = "NA";
	
	@Transient
	private String browser = "NA";
	
	@Transient
	private String stateInterp = "NA";
	
	@Transient
	private String esimSupportInterp = "NA";
	
	@Transient
	private String softsimSupportInterp = "NA";
	
	@Transient
	private String networkTechnologyGSMInterp = "NA";
	
	@Transient
	private String networkTechnologyCDMAInterp = "NA";
	
	@Transient
	private String networkTechnologyEVDOInterp = "NA";
	
	@Transient
	private String networkTechnologyLTEInterp = "NA";
	
	@Transient
	private String networkTechnology5GInterp = "NA";
	
	@Transient
	private String networkTechnology6GInterp = "NA";
	
	@Transient
	private String networkTechnology7GInterp = "NA";
	
	@Transient
	private String memoryCardSlotInterp = "NA";
	
	@Transient
	private String mainCameraTypeInterp = "NA";
	
	@Transient
	private String selfieCameraTypeInterp = "NA";
	
	@Transient
	private String soundLoudspeakerInterp = "NA";
	
	@Transient
	private String sound35mmJackInterp = "NA";
	
	@Transient
	private String commsNFCInterp = "NA";
	
	@Transient
	private String commsRadioInterp = "NA";
	
	public MobileDeviceRepositoryHistory() {}
	
	public MobileDeviceRepositoryHistory(MobileDeviceRepository mdr) {
		this.updatedOn               = LocalDateTime.now();
		this.mdrId                   = mdr.getId();
		this.createdOn               = mdr.getCreatedOn();
		this.modifiedOn              = mdr.getModifiedOn();
		this.deviceId                = mdr.getDeviceId();
		this.marketingName           = mdr.getMarketingName();
		this.manufacturer            = mdr.getManufacturer();
		this.manufacturingLocation   = mdr.getManufacturingLocation();
		this.modelName               = mdr.getModelName();
		this.brandName               = mdr.getBrandName();
		this.oem                     = mdr.getOem();
		this.organizationId          = mdr.getOrganizationId();
		this.deviceType              = mdr.getDeviceType();
		this.allocationDate          = mdr.getAllocationDate();
		this.imeiQuantity            = mdr.getImeiQuantity();
		this.simSlot                 = mdr.getSimSlot();
		this.esimSupport             = mdr.getEsimSupport();
		this.softsimSupport          = mdr.getSoftsimSupport();
		this.simType                 = mdr.getSimType();
		this.os                      = mdr.getOs();
		this.osCurrentVersion        = mdr.getOsCurrentVersion();
		this.osBaseVersion           = mdr.getOsBaseVersion();
		this.announceDate            = mdr.getAnnounceDate();
		this.launchDate              = mdr.getLaunchDate();
		this.deviceStatus            = mdr.getDeviceStatus();
		this.discontinueDate         = mdr.getDiscontinueDate();
		this.networkTechnologyGSM    = mdr.getNetworkTechnologyGSM();
		this.networkTechnologyCDMA   = mdr.getNetworkTechnologyCDMA();
		this.networkTechnologyEVDO   = mdr.getNetworkTechnologyEVDO();
		this.networkTechnologyLTE    = mdr.getNetworkTechnologyLTE();
		this.networkTechnology5G     = mdr.getNetworkTechnology5G();
		this.networkTechnology6G     = mdr.getNetworkTechnology6G();
		this.networkTechnology7G     = mdr.getNetworkTechnology7G();
		this.network2GBand           = mdr.getNetwork2GBand();
		this.network3GBand           = mdr.getNetwork3GBand();
		this.network4GBand           = mdr.getNetwork4GBand();
		this.network5GBand           = mdr.getNetwork5GBand();
		this.network6GBand           = mdr.getNetwork6GBand();
		this.network7GBand           = mdr.getNetwork7GBand();
		this.networkSpeed            = mdr.getNetworkSpeed();
		this.bodyDimension           = mdr.getBodyDimension();
		this.bodyWeight              = mdr.getBodyWeight();
		this.displayType             = mdr.getDisplayType();
		this.displaySize             = mdr.getDisplaySize();
		this.displayResolution       = mdr.getDisplayResolution();
		this.displayProtection       = mdr.getDisplayProtection();
		this.platformChipset         = mdr.getPlatformChipset();
		this.platformCPU             = mdr.getPlatformCPU();
		this.platformGPU             = mdr.getPlatformGPU();
		this.memoryCardSlot          = mdr.getMemoryCardSlot();
		this.memoryInternal          = mdr.getMemoryInternal();
		this.ram                     = mdr.getRam();
		this.mainCameraType          = mdr.getMainCameraType();
		this.mainCameraSpec          = mdr.getMainCameraSpec();
		this.mainCameraFeature       = mdr.getMainCameraFeature();
		this.mainCameraVideo         = mdr.getMainCameraVideo();
		this.selfieCameraType        = mdr.getSelfieCameraType();
		this.selfieCameraSpec        = mdr.getSelfieCameraSpec();
		this.selfieCameraFeature     = mdr.getSelfieCameraFeature();
		this.selfieCameraVideo       = mdr.getSelfieCameraVideo();
		this.soundLoudspeaker        = mdr.getSoundLoudspeaker();
		this.sound35mmJack           = mdr.getSound35mmJack();
		this.commsWLAN               = mdr.getCommsWLAN();
		this.commsBluetooth          = mdr.getCommsBluetooth();
		this.commsGPS                = mdr.getCommsGPS();
		this.commsNFC                = mdr.getCommsNFC();
		this.commsRadio              = mdr.getCommsRadio();
		this.commsUSB                = mdr.getCommsUSB();
		this.sensor                  = mdr.getSensor();
		this.batteryType             = mdr.getBatteryType();
		this.batteryCapacity         = mdr.getBatteryCapacity();
		this.batteryCharging         = mdr.getBatteryCharging();
		this.colors                  = mdr.getColors();
		this.removableUICC           = mdr.getRemovableUICC();
		this.removableEUICC          = mdr.getRemovableEUICC();
		this.nonremovableUICC        = mdr.getNonremovableUICC();
		this.nonremovableEUICC       = mdr.getNonremovableEUICC();
		this.bandDetail              = mdr.getBandDetail();
		this.launchPriceAsianMarket  = mdr.getLaunchPriceAsianMarket();
		this.launchPriceUSMarket     = mdr.getLaunchPriceUSMarket();
		this.launchPriceEuropeMarket = mdr.getLaunchPriceEuropeMarket();
		this.launchPriceInternationalMarket = mdr.getLaunchPriceInternationalMarket();
		this.launchPriceCambodiaMarket      = mdr.getLaunchPriceCambodiaMarket();
		this.customPriceOfDevice            = mdr.getCustomPriceOfDevice();
		this.sourceOfCambodiaMarketPrice    = mdr.getSourceOfCambodiaMarketPrice();
		this.reportedGlobalIssue            = mdr.getReportedGlobalIssue();
		this.reportedLocalIssue             = mdr.getReportedLocalIssue();
		this.deviceState                    = mdr.getDeviceState();
		this.userId                         = mdr.getUserId();
		this.remark                         = mdr.getRemark();
		this.networkSpecificIdentifier      = mdr.getNetworkSpecificIdentifier();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDateTime getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(LocalDateTime updatedOn) {
		this.updatedOn = updatedOn;
	}

	public Integer getMdrId() {
		return mdrId;
	}

	public void setMdrId(Integer mdrId) {
		this.mdrId = mdrId;
	}

	public LocalDateTime getAllocationDate() {
		return allocationDate;
	}

	public void setAllocationDate(LocalDateTime allocationDate) {
		this.allocationDate = allocationDate;
	}

	public LocalDateTime getAnnounceDate() {
		return announceDate;
	}

	public void setAnnounceDate(LocalDateTime announceDate) {
		this.announceDate = announceDate;
	}

	public String getBandDetail() {
		return bandDetail;
	}

	public void setBandDetail(String bandDetail) {
		this.bandDetail = bandDetail;
	}

	public Integer getBatteryCapacity() {
		return batteryCapacity;
	}

	public void setBatteryCapacity(Integer batteryCapacity) {
		this.batteryCapacity = batteryCapacity;
	}

	public String getBatteryCharging() {
		return batteryCharging;
	}

	public void setBatteryCharging(String batteryCharging) {
		this.batteryCharging = batteryCharging;
	}

	public String getBatteryType() {
		return batteryType;
	}

	public void setBatteryType(String batteryType) {
		this.batteryType = batteryType;
	}

	public String getBodyDimension() {
		return bodyDimension;
	}

	public void setBodyDimension(String bodyDimension) {
		this.bodyDimension = bodyDimension;
	}

	public String getBodyWeight() {
		return bodyWeight;
	}

	public void setBodyWeight(String bodyWeight) {
		this.bodyWeight = bodyWeight;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getColors() {
		return colors;
	}

	public void setColors(String colors) {
		this.colors = colors;
	}

	public String getCommsBluetooth() {
		return commsBluetooth;
	}

	public void setCommsBluetooth(String commsBluetooth) {
		this.commsBluetooth = commsBluetooth;
	}

	public String getCommsGPS() {
		return commsGPS;
	}

	public void setCommsGPS(String commsGPS) {
		this.commsGPS = commsGPS;
	}

	public Integer getCommsNFC() {
		return commsNFC;
	}

	public void setCommsNFC(Integer commsNFC) {
		this.commsNFC = commsNFC;
	}

	public Integer getCommsRadio() {
		return commsRadio;
	}

	public void setCommsRadio(Integer commsRadio) {
		this.commsRadio = commsRadio;
	}

	public String getCommsUSB() {
		return commsUSB;
	}

	public void setCommsUSB(String commsUSB) {
		this.commsUSB = commsUSB;
	}

	public String getCommsWLAN() {
		return commsWLAN;
	}

	public void setCommsWLAN(String commsWLAN) {
		this.commsWLAN = commsWLAN;
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

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getMarketingName() {
		return marketingName;
	}

	public void setMarketingName(String marketingName) {
		this.marketingName = marketingName;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getManufacturingLocation() {
		return manufacturingLocation;
	}

	public void setManufacturingLocation(String manufacturingLocation) {
		this.manufacturingLocation = manufacturingLocation;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getOem() {
		return oem;
	}

	public void setOem(String oem) {
		this.oem = oem;
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

	public Integer getImeiQuantity() {
		return imeiQuantity;
	}

	public void setImeiQuantity(Integer imeiQuantity) {
		this.imeiQuantity = imeiQuantity;
	}

	public Integer getSimSlot() {
		return simSlot;
	}

	public void setSimSlot(Integer simSlot) {
		this.simSlot = simSlot;
	}

	public Integer getEsimSupport() {
		return esimSupport;
	}

	public void setEsimSupport(Integer esimSupport) {
		this.esimSupport = esimSupport;
	}

	public Integer getSoftsimSupport() {
		return softsimSupport;
	}

	public void setSoftsimSupport(Integer softsimSupport) {
		this.softsimSupport = softsimSupport;
	}

	public String getSimType() {
		return simType;
	}

	public void setSimType(String simType) {
		this.simType = simType;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getOsCurrentVersion() {
		return osCurrentVersion;
	}

	public void setOsCurrentVersion(String osCurrentVersion) {
		this.osCurrentVersion = osCurrentVersion;
	}

	public String getOsBaseVersion() {
		return osBaseVersion;
	}

	public void setOsBaseVersion(String osBaseVersion) {
		this.osBaseVersion = osBaseVersion;
	}

	public LocalDateTime getLaunchDate() {
		return launchDate;
	}

	public void setLaunchDate(LocalDateTime launchDate) {
		this.launchDate = launchDate;
	}

	public String getDeviceStatus() {
		return deviceStatus;
	}

	public void setDeviceStatus(String deviceStatus) {
		this.deviceStatus = deviceStatus;
	}

	public LocalDateTime getDiscontinueDate() {
		return discontinueDate;
	}

	public void setDiscontinueDate(LocalDateTime discontinueDate) {
		this.discontinueDate = discontinueDate;
	}

	public Integer getNetworkTechnologyGSM() {
		return networkTechnologyGSM;
	}

	public void setNetworkTechnologyGSM(Integer networkTechnologyGSM) {
		this.networkTechnologyGSM = networkTechnologyGSM;
	}

	public Integer getNetworkTechnologyCDMA() {
		return networkTechnologyCDMA;
	}

	public void setNetworkTechnologyCDMA(Integer networkTechnologyCDMA) {
		this.networkTechnologyCDMA = networkTechnologyCDMA;
	}

	public Integer getNetworkTechnologyEVDO() {
		return networkTechnologyEVDO;
	}

	public void setNetworkTechnologyEVDO(Integer networkTechnologyEVDO) {
		this.networkTechnologyEVDO = networkTechnologyEVDO;
	}

	public Integer getNetworkTechnologyLTE() {
		return networkTechnologyLTE;
	}

	public void setNetworkTechnologyLTE(Integer networkTechnologyLTE) {
		this.networkTechnologyLTE = networkTechnologyLTE;
	}

	public Integer getNetworkTechnology5G() {
		return networkTechnology5G;
	}

	public void setNetworkTechnology5G(Integer networkTechnology5G) {
		this.networkTechnology5G = networkTechnology5G;
	}

	public Integer getNetworkTechnology6G() {
		return networkTechnology6G;
	}

	public void setNetworkTechnology6G(Integer networkTechnology6G) {
		this.networkTechnology6G = networkTechnology6G;
	}

	public Integer getNetworkTechnology7G() {
		return networkTechnology7G;
	}

	public void setNetworkTechnology7G(Integer networkTechnology7G) {
		this.networkTechnology7G = networkTechnology7G;
	}

	public String getNetwork2GBand() {
		return network2GBand;
	}

	public void setNetwork2GBand(String network2gBand) {
		network2GBand = network2gBand;
	}

	public String getNetwork3GBand() {
		return network3GBand;
	}

	public void setNetwork3GBand(String network3gBand) {
		network3GBand = network3gBand;
	}

	public String getNetwork4GBand() {
		return network4GBand;
	}

	public void setNetwork4GBand(String network4gBand) {
		network4GBand = network4gBand;
	}

	public String getNetwork5GBand() {
		return network5GBand;
	}

	public void setNetwork5GBand(String network5gBand) {
		network5GBand = network5gBand;
	}

	public String getNetwork6GBand() {
		return network6GBand;
	}

	public void setNetwork6GBand(String network6gBand) {
		network6GBand = network6gBand;
	}

	public String getNetwork7GBand() {
		return network7GBand;
	}

	public void setNetwork7GBand(String network7gBand) {
		network7GBand = network7gBand;
	}

	public String getNetworkSpeed() {
		return networkSpeed;
	}

	public void setNetworkSpeed(String networkSpeed) {
		this.networkSpeed = networkSpeed;
	}

	public String getDisplayType() {
		return displayType;
	}

	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}

	public String getDisplaySize() {
		return displaySize;
	}

	public void setDisplaySize(String displaySize) {
		this.displaySize = displaySize;
	}

	public String getDisplayResolution() {
		return displayResolution;
	}

	public void setDisplayResolution(String displayResolution) {
		this.displayResolution = displayResolution;
	}

	public String getDisplayProtection() {
		return displayProtection;
	}

	public void setDisplayProtection(String displayProtection) {
		this.displayProtection = displayProtection;
	}

	public String getPlatformChipset() {
		return platformChipset;
	}

	public void setPlatformChipset(String platformChipset) {
		this.platformChipset = platformChipset;
	}

	public String getPlatformCPU() {
		return platformCPU;
	}

	public void setPlatformCPU(String platformCPU) {
		this.platformCPU = platformCPU;
	}

	public String getPlatformGPU() {
		return platformGPU;
	}

	public void setPlatformGPU(String platformGPU) {
		this.platformGPU = platformGPU;
	}

	public Integer getMemoryCardSlot() {
		return memoryCardSlot;
	}

	public void setMemoryCardSlot(Integer memoryCardSlot) {
		this.memoryCardSlot = memoryCardSlot;
	}

	public Integer getMemoryInternal() {
		return memoryInternal;
	}

	public void setMemoryInternal(Integer memoryInternal) {
		this.memoryInternal = memoryInternal;
	}

	public String getRam() {
		return ram;
	}

	public void setRam(String ram) {
		this.ram = ram;
	}

	public Integer getMainCameraType() {
		return mainCameraType;
	}

	public void setMainCameraType(Integer mainCameraType) {
		this.mainCameraType = mainCameraType;
	}

	public String getMainCameraSpec() {
		return mainCameraSpec;
	}

	public void setMainCameraSpec(String mainCameraSpec) {
		this.mainCameraSpec = mainCameraSpec;
	}

	public String getMainCameraFeature() {
		return mainCameraFeature;
	}

	public void setMainCameraFeature(String mainCameraFeature) {
		this.mainCameraFeature = mainCameraFeature;
	}

	public String getMainCameraVideo() {
		return mainCameraVideo;
	}

	public void setMainCameraVideo(String mainCameraVideo) {
		this.mainCameraVideo = mainCameraVideo;
	}

	public Integer getSelfieCameraType() {
		return selfieCameraType;
	}

	public void setSelfieCameraType(Integer selfieCameraType) {
		this.selfieCameraType = selfieCameraType;
	}

	public String getSelfieCameraSpec() {
		return selfieCameraSpec;
	}

	public void setSelfieCameraSpec(String selfieCameraSpec) {
		this.selfieCameraSpec = selfieCameraSpec;
	}

	public String getSelfieCameraFeature() {
		return selfieCameraFeature;
	}

	public void setSelfieCameraFeature(String selfieCameraFeature) {
		this.selfieCameraFeature = selfieCameraFeature;
	}

	public String getSelfieCameraVideo() {
		return selfieCameraVideo;
	}

	public void setSelfieCameraVideo(String selfieCameraVideo) {
		this.selfieCameraVideo = selfieCameraVideo;
	}

	public Integer getSoundLoudspeaker() {
		return soundLoudspeaker;
	}

	public void setSoundLoudspeaker(Integer soundLoudspeaker) {
		this.soundLoudspeaker = soundLoudspeaker;
	}

	public Integer getSound35mmJack() {
		return sound35mmJack;
	}

	public void setSound35mmJack(Integer sound35mmJack) {
		this.sound35mmJack = sound35mmJack;
	}

	public String getSensor() {
		return sensor;
	}

	public void setSensor(String sensor) {
		this.sensor = sensor;
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

	public Integer getNonremovableUICC() {
		return nonremovableUICC;
	}

	public void setNonremovableUICC(Integer nonremovableUICC) {
		this.nonremovableUICC = nonremovableUICC;
	}

	public Integer getNonremovableEUICC() {
		return nonremovableEUICC;
	}

	public void setNonremovableEUICC(Integer nonremovableEUICC) {
		this.nonremovableEUICC = nonremovableEUICC;
	}

	public Double getLaunchPriceAsianMarket() {
		return launchPriceAsianMarket;
	}

	public void setLaunchPriceAsianMarket(Double launchPriceAsianMarket) {
		this.launchPriceAsianMarket = launchPriceAsianMarket;
	}

	public Double getLaunchPriceUSMarket() {
		return launchPriceUSMarket;
	}

	public void setLaunchPriceUSMarket(Double launchPriceUSMarket) {
		this.launchPriceUSMarket = launchPriceUSMarket;
	}

	public Double getLaunchPriceEuropeMarket() {
		return launchPriceEuropeMarket;
	}

	public void setLaunchPriceEuropeMarket(Double launchPriceEuropeMarket) {
		this.launchPriceEuropeMarket = launchPriceEuropeMarket;
	}

	public Double getLaunchPriceInternationalMarket() {
		return launchPriceInternationalMarket;
	}

	public void setLaunchPriceInternationalMarket(Double launchPriceInternationalMarket) {
		this.launchPriceInternationalMarket = launchPriceInternationalMarket;
	}

	public Double getLaunchPriceCambodiaMarket() {
		return launchPriceCambodiaMarket;
	}

	public void setLaunchPriceCambodiaMarket(Double launchPriceCambodiaMarket) {
		this.launchPriceCambodiaMarket = launchPriceCambodiaMarket;
	}

	public Double getCustomPriceOfDevice() {
		return customPriceOfDevice;
	}

	public void setCustomPriceOfDevice(Double customPriceOfDevice) {
		this.customPriceOfDevice = customPriceOfDevice;
	}

	public String getSourceOfCambodiaMarketPrice() {
		return sourceOfCambodiaMarketPrice;
	}

	public void setSourceOfCambodiaMarketPrice(String sourceOfCambodiaMarketPrice) {
		this.sourceOfCambodiaMarketPrice = sourceOfCambodiaMarketPrice;
	}

	public String getReportedGlobalIssue() {
		return reportedGlobalIssue;
	}

	public void setReportedGlobalIssue(String reportedGlobalIssue) {
		this.reportedGlobalIssue = reportedGlobalIssue;
	}

	public String getReportedLocalIssue() {
		return reportedLocalIssue;
	}

	public void setReportedLocalIssue(String reportedLocalIssue) {
		this.reportedLocalIssue = reportedLocalIssue;
	}

	public Integer getDeviceState() {
		return deviceState;
	}

	public void setDeviceState(Integer deviceState) {
		this.deviceState = deviceState;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public long getFeatureId() {
		return featureId;
	}

	public void setFeatureId(long featureId) {
		this.featureId = featureId;
	}

	public String getUserDisplayName() {
		return userDisplayName;
	}

	public void setUserDisplayName(String userDisplayName) {
		this.userDisplayName = userDisplayName;
	}

	public String getPublicIp() {
		return publicIp;
	}

	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String getStateInterp() {
		return stateInterp;
	}

	public void setStateInterp(String stateInterp) {
		this.stateInterp = stateInterp;
	}

	public String getEsimSupportInterp() {
		return esimSupportInterp;
	}

	public void setEsimSupportInterp(String esimSupportInterp) {
		this.esimSupportInterp = esimSupportInterp;
	}

	public String getSoftsimSupportInterp() {
		return softsimSupportInterp;
	}

	public void setSoftsimSupportInterp(String softsimSupportInterp) {
		this.softsimSupportInterp = softsimSupportInterp;
	}

	public String getNetworkTechnologyGSMInterp() {
		return networkTechnologyGSMInterp;
	}

	public void setNetworkTechnologyGSMInterp(String networkTechnologyGSMInterp) {
		this.networkTechnologyGSMInterp = networkTechnologyGSMInterp;
	}

	public String getNetworkTechnologyCDMAInterp() {
		return networkTechnologyCDMAInterp;
	}

	public void setNetworkTechnologyCDMAInterp(String networkTechnologyCDMAInterp) {
		this.networkTechnologyCDMAInterp = networkTechnologyCDMAInterp;
	}

	public String getNetworkTechnologyEVDOInterp() {
		return networkTechnologyEVDOInterp;
	}

	public void setNetworkTechnologyEVDOInterp(String networkTechnologyEVDOInterp) {
		this.networkTechnologyEVDOInterp = networkTechnologyEVDOInterp;
	}

	public String getNetworkTechnologyLTEInterp() {
		return networkTechnologyLTEInterp;
	}

	public void setNetworkTechnologyLTEInterp(String networkTechnologyLTEInterp) {
		this.networkTechnologyLTEInterp = networkTechnologyLTEInterp;
	}

	public String getNetworkTechnology5GInterp() {
		return networkTechnology5GInterp;
	}

	public void setNetworkTechnology5GInterp(String networkTechnology5GInterp) {
		this.networkTechnology5GInterp = networkTechnology5GInterp;
	}

	public String getNetworkTechnology6GInterp() {
		return networkTechnology6GInterp;
	}

	public void setNetworkTechnology6GInterp(String networkTechnology6GInterp) {
		this.networkTechnology6GInterp = networkTechnology6GInterp;
	}

	public String getNetworkTechnology7GInterp() {
		return networkTechnology7GInterp;
	}

	public void setNetworkTechnology7GInterp(String networkTechnology7GInterp) {
		this.networkTechnology7GInterp = networkTechnology7GInterp;
	}

	public String getMemoryCardSlotInterp() {
		return memoryCardSlotInterp;
	}

	public void setMemoryCardSlotInterp(String memoryCardSlotInterp) {
		this.memoryCardSlotInterp = memoryCardSlotInterp;
	}

	public String getMainCameraTypeInterp() {
		return mainCameraTypeInterp;
	}

	public void setMainCameraTypeInterp(String mainCameraTypeInterp) {
		this.mainCameraTypeInterp = mainCameraTypeInterp;
	}

	public String getSelfieCameraTypeInterp() {
		return selfieCameraTypeInterp;
	}

	public void setSelfieCameraTypeInterp(String selfieCameraTypeInterp) {
		this.selfieCameraTypeInterp = selfieCameraTypeInterp;
	}

	public String getSoundLoudspeakerInterp() {
		return soundLoudspeakerInterp;
	}

	public void setSoundLoudspeakerInterp(String soundLoudspeakerInterp) {
		this.soundLoudspeakerInterp = soundLoudspeakerInterp;
	}

	public String getSound35mmJackInterp() {
		return sound35mmJackInterp;
	}

	public void setSound35mmJackInterp(String sound35mmJackInterp) {
		this.sound35mmJackInterp = sound35mmJackInterp;
	}

	public String getCommsNFCInterp() {
		return commsNFCInterp;
	}

	public void setCommsNFCInterp(String commsNFCInterp) {
		this.commsNFCInterp = commsNFCInterp;
	}

	public String getCommsRadioInterp() {
		return commsRadioInterp;
	}

	public void setCommsRadioInterp(String commsRadioInterp) {
		this.commsRadioInterp = commsRadioInterp;
	}

	public Integer getNetworkSpecificIdentifier() {
		return networkSpecificIdentifier;
	}

	public void setNetworkSpecificIdentifier(Integer networkSpecificIdentifier) {
		this.networkSpecificIdentifier = networkSpecificIdentifier;
	}

}