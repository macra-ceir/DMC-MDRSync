package com.gl.MDRProcess.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.gl.MDRProcess.configuration.PropertiesReader;
import com.gl.MDRProcess.model.app.AlertMessages;
import com.gl.MDRProcess.model.app.GSMATacDetails;
import com.gl.MDRProcess.model.audit.ModulesAuditTrail;
import com.gl.MDRProcess.model.view.ModelBrandView;
import com.gl.MDRProcess.repo.app.AlertRepository;
//import com.gl.MDRProcess.repo.app.GSMATacDetailsRepository;
import com.gl.MDRProcess.repo.app.MobileDeviceRepoRepository;
import com.gl.MDRProcess.repo.audit.ModulesAuditTrailRepository;
import com.gl.MDRProcess.util.Utility;
import com.gl.MDRProcess.model.app.SystemConfigListDb;
import com.gl.MDRProcess.model.app.SystemParamDb;
import com.gl.MDRProcess.repo.app.SystemConfigListRepository;
import com.gl.MDRProcess.repo.app.SystemParamRepository;

@Service
@Transactional
public class MDRServiceImpl {
	
	private static final Logger logger = LogManager.getLogger(MDRServiceImpl.class);
	
//	@Autowired
//	GSMATacDetailsRepository gsmaTacDetailsRepository;
	
	@Autowired
	MobileDeviceRepoRepository mdrRepository;
	
	@Autowired
	ModulesAuditTrailRepository modulesAuditTrailRepository;
	
	@Autowired
	PropertiesReader propertiesReader;
	
	@Autowired
	ModelsServiceImpl modelService;
	
	@Autowired
	BrandServiceImpl brandService;
	
	@Autowired
	Utility utility;
	
	@Autowired
	SystemConfigListRepository systemConfigListRepository;
	
	@Autowired
	SystemParamRepository systemParamRepository;
	
	
	@Autowired
	AlertRepository alertRepository;
	
	@Autowired
	@PersistenceContext//(unitName = "appEntityManagerFactory")
	private EntityManager deviceEntityManager;
	
	boolean failFalg = false;

	public void insertMDR() {
		int insertionCount = 0;
		Pageable pageable  = null;
		int batchSize   = propertiesReader.batchSize;
		Integer userId = propertiesReader.userId;
		long startTime = 0;
		long totalTime = 0;
//		Integer userType = propertiesReader.userType;
		String moduleName = propertiesReader.moduleName;
		String feature    = propertiesReader.feature;
		String serverName = null;
		//SystemConfigListDb isTypeApproved = null;
		int isTypeApprovedValues = 0;
		AlertMessages alertMessages = null;
		List<GSMATacDetails> tacDetails = null;
		List<SystemConfigListDb> testIMEIList = null;
		List<Object[]> newDeviceDetails = null;
		List<String> deviceIds = null;
		
		try {
			serverName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ModulesAuditTrail modulesAuditTrail = new ModulesAuditTrail((int)0, 201, 
				"Initial", moduleName, feature, "Sync", 0, serverName);
		try {
			logger.info("Mobile Device Repository update process is started.");
			
			pageable = PageRequest.of(0, batchSize);
			startTime = System.currentTimeMillis();
			modulesAuditTrail = modulesAuditTrailRepository.save(modulesAuditTrail);
			testIMEIList = systemConfigListRepository.findByTag("TEST_IMEI_SERIES");
			
			SystemParamDb isApproved=null ;
			
			try {
				
				 isApproved = systemParamRepository.findByTag("type_approved_enable_flag");
				 
				 logger.info("Going to get new records for sys_param table default values false : "+isApproved.toString()+" and isTypeApprovedValues : "+isTypeApprovedValues);
				if(isApproved!=null) {
					
					if(isApproved.getValue().equalsIgnoreCase("true")) {
						isTypeApprovedValues=0;
					}else {
						isTypeApprovedValues=1;
					}
					
					logger.info("Going to get new records for sys_param table default values false : "+isApproved.getValue()+" and isTypeApprovedValues : "+isTypeApprovedValues);
				}
			} catch (Exception e) {
				// TODO: handle exception
				logger.info("Exception Going to get new records for sys_param table default values false : "+isApproved.getValue()+" and isTypeApprovedValues : "+isTypeApprovedValues);
				e.printStackTrace();
			}
			
			if (Objects.isNull(testIMEIList) || testIMEIList.isEmpty()) {
				alertMessages = alertRepository.findByAlertIdIn("alert1500");
				logger.error("Going to Raise alert alert1500 ,description of alert is="+alertMessages.getDescription());
				logger.error("Alert alert1500 is raised");
				modulesAuditTrail.setStatusCode(501);
				modulesAuditTrail.setStatus("Failed");
				modulesAuditTrail.setInfo(alertMessages.getDescription());
				modulesAuditTrailRepository.save(modulesAuditTrail);
				System.exit(0);
			}
			logger.info("Going to get new records for GSMA table.");
			long logTime = System.currentTimeMillis();
			tacDetails = this.getGSMATacs(batchSize);
			
			if(failFalg) {
				modulesAuditTrail.setStatusCode(501);
				modulesAuditTrail.setStatus("Failed");
				try {
					modulesAuditTrail.setInfo(alertMessages.getDescription());
				} catch (Exception e) {
					// TODO: handle exception
					modulesAuditTrail.setInfo("");
				}
				modulesAuditTrailRepository.save(modulesAuditTrail);
				System.exit(0);
				alertMessages = alertRepository.findByAlertIdIn("alert1500");
				logger.error("Going to Raise alert alert1500 ,description of alert is="+alertMessages.getDescription());
				logger.error("Alert alert1500 is raised");
				raiseAnAlert("alert1500","Mobile Device Repository Database Sync Utility Failed, error while reading TACserror while saving devices.", "Sync", 0);
				
			}
			logger.info("Records reading done, total time:{"+String.valueOf(System.currentTimeMillis()-logTime)+"}");
			while(tacDetails.size() > 0) {
				logger.info("Record processing started.");
				logTime = System.currentTimeMillis();
				deviceIds = processDeviceDataAndSave(tacDetails, userId, testIMEIList,isTypeApprovedValues);
				if(failFalg) {
					modulesAuditTrail.setStatusCode(501);
					modulesAuditTrail.setStatus("Failed");
					//modulesAuditTrail.setInfo(alertMessages.getDescription());
					try {
						modulesAuditTrail.setInfo(alertMessages.getDescription());
					} catch (Exception e) {
						// TODO: handle exception
						modulesAuditTrail.setInfo("");
					}
					alertMessages = alertRepository.findByAlertIdIn("alert1500");
					modulesAuditTrailRepository.save(modulesAuditTrail);
					logger.error("Going to Raise alert alert1500 ,description of alert is="+alertMessages.getDescription());
					logger.error("Alert alert1500 is raised");
					raiseAnAlert("alert1500","Mobile Device Repository Database Sync Utility Failed, error while saving devices.", "Sync", 0);
					System.exit(0);
				}
				logger.info("Record processing ended. Total time:{"+String.valueOf(System.currentTimeMillis()-logTime)+"}");
				newDeviceDetails = this.getSavedDevices(deviceIds);
				deviceIds = null;
				logger.info("History processing started");
				logTime = System.currentTimeMillis();
				this.saveHistoryData(newDeviceDetails);
				if(failFalg) {
					logger.error("Alert alert1500 is raised");
					
					modulesAuditTrail.setStatusCode(501);
					modulesAuditTrail.setStatus("Failed");
					try {
						modulesAuditTrail.setInfo(alertMessages.getDescription());
					} catch (Exception e) {
						// TODO: handle exception
						modulesAuditTrail.setInfo("");
					}
					//modulesAuditTrail.setInfo(alertMessages.getDescription());
					modulesAuditTrailRepository.save(modulesAuditTrail);
					alertMessages = alertRepository.findByAlertIdIn("alert1500");
					raiseAnAlert("alert1500","Mobile Device Repository Database Sync Utility Failed, error while saving MDR history.", "Sync", 0);
					logger.error("Going to Raise alert alert1500 ,description of alert is="+alertMessages.getDescription());
					System.exit(0);
				}
				logger.info("History saved in the database, total time:{"+String.valueOf(System.currentTimeMillis()-logTime)+"}");
				insertionCount += tacDetails.size();
				logger.info("Going to get new records for GSMA table.");
				logTime = System.currentTimeMillis();
				tacDetails = this.getGSMATacs(batchSize);
				logger.info("Records reading done, total time:{"+String.valueOf(System.currentTimeMillis()-logTime)+"}");
//				break;//Need to comment after test
			}
			totalTime = System.currentTimeMillis() - startTime;
			modulesAuditTrail.setExecutionTime((int)totalTime);
			modulesAuditTrail.setStatusCode(200);
			modulesAuditTrail.setStatus("Success");
			modulesAuditTrail.setCount(insertionCount);
			modulesAuditTrailRepository.save(modulesAuditTrail);
			logger.info("Mobile Device Repository entry completed.");
		}catch(Exception e) {
			logger.error(e.getMessage(), e);
			modulesAuditTrail.setStatusCode(501);
			modulesAuditTrail.setStatus("Failed");
			modulesAuditTrail.setInfo("Processing failed during DB Update");
			modulesAuditTrailRepository.save(modulesAuditTrail);
			logger.error("Mobile Device Repository insertion failed="+e.getMessage());
			
			alertMessages = alertRepository.findByAlertIdIn("alert1500");
			logger.error("Going to Raise alert alert1500 ,description of alert is="+alertMessages.getDescription());
			raiseAnAlert("alert1500","Mobile Device Repository Database Sync Utility Failed,"+e.getMessage(), "Sync", 0);
			logger.error("Alert alert1500 is raised");
			System.exit(0);
		}
		try {
			this.deleteOldModels();
			this.deleteOldBrands();
			logger.info("Going to save all new brand and models.");
			long logTime = System.currentTimeMillis();
			this.saveAllNewBrandAndModel();
			logger.info("All new brand and models saved, total time:{"+String.valueOf(System.currentTimeMillis()-logTime)+"}");
		}catch(Exception ex) {
			logger.error("Model table and brand table update failed="+ex.getMessage());
			logger.error(ex.getMessage(), ex);
			alertMessages =alertRepository.findByAlertIdIn("alert1500");
			logger.error("Going to Raise alert alert1500 ,description of alert is="+alertMessages.getDescription());
			raiseAnAlert("alert1500","Mobile Device Repository Database Sync Utility Failed,"+ex.getMessage(), "Sync", 0);
			logger.error("Alert alert1500 is raised");
		}
		logger.info("Mobile Device Repository update process is ended.");
	}
	
	public List<GSMATacDetails> getGSMATacs(int batchLimit){
		List<GSMATacDetails> tacDetails = null;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = "select gsma_tac_details.tac, gsma_tac_details.model_name, gsma_tac_details.brand_name, gsma_tac_details.allocation_date, network_technology, bluetooth,nfc,"
				+ " wlan, gsma_tac_details.device_type, gsma_tac_details.imei_quantity, gsma_tac_details.manufacturer, gsma_tac_details.marketing_name, gsma_tac_details.oem, operating_system,"
				+ " gsma_tac_details.removable_uicc, gsma_tac_details.removable_euicc, gsma_tac_details.nonremovable_uicc, gsma_tac_details.nonremovable_euicc, gsma_tac_details.sim_slot, gsma_tac_details.network_specific_identifier"
				+ " from app.gsma_tac_details  left join app.mobile_device_repository  on gsma_tac_details.tac=mobile_device_repository.device_id where "
				+ " mobile_device_repository.device_id IS NULL and gsma_tac_details.device_type in "
				+ "("+propertiesReader.filterDeviceTypes.stream().map(name -> "'" + name + "'").collect(Collectors.joining(","))+")"
						+ " FETCH NEXT "+batchLimit+" ROWS ONLY ";
//		String query = "select tac, g.model_name, g.brand_name, g.allocation_date, network_technology, bluetooth,"
//				+ "nfc, wlan, g.device_type, g.imei_quantity, g.manufacturer, g.marketing_name, g.oem, operating_system,"
//				+ "g.removable_uicc, g.removable_euicc, g.nonremovable_uicc, g.nonremovable_euicc, g.sim_slot, g.network_specific_identifier "
//				+ "from gsma_tac_details as g left join mobile_device_repository as m on tac=device_id where "
//				+ "device_id IS NULL and g.device_type in "
//				+ "("+propertiesReader.filterDeviceTypes.stream().map(name -> "'" + name + "'").collect(Collectors.joining(","))+")"
//						+ " FETCH FIRST "+batchLimit+" ROWS ONLY ";
		try {
			logger.info("Final Query get Data from GSMATacs in MDRs: "+query);
			
			conn = this.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			tacDetails = new ArrayList<GSMATacDetails>();
			while (rs.next()) {
				tacDetails.add(new GSMATacDetails(
						rs.getString("tac"),
						rs.getString("model_name"),
						rs.getString("brand_name"),
						rs.getTimestamp("allocation_date"),
						rs.getString("network_technology"),
						rs.getString("bluetooth"),
						rs.getString("nfc"),
						rs.getString("wlan"),
						rs.getString("device_type"),
						rs.getInt("imei_quantity"),
						rs.getString("manufacturer"),
						rs.getString("marketing_name"),
						rs.getString("oem"),
						rs.getString("operating_system"),
						rs.getInt("removable_uicc"),
						rs.getInt("removable_euicc"),
						rs.getInt("nonremovable_uicc"),
						rs.getInt("nonremovable_euicc"),
						rs.getInt("sim_slot"),
						rs.getInt("network_specific_identifier")
						));
            }
		}catch(Exception ex) {
			logger.error("Error druing saving new MDRs: "+ex.getMessage());
			logger.error(ex.getMessage(), ex);
			failFalg = true;
		}finally {
			try {
				if(stmt != null)
					stmt.close();
				if(rs != null)
					rs.close();
				if(conn != null)
					conn.close();
			} catch(Exception e) {}
		}
		return tacDetails;
	}
	
	private List<String> processDeviceDataAndSave(List<GSMATacDetails> tacDetails, Integer userId, 
			List<SystemConfigListDb> testIMEIList , int isTypeApprovedValues){
		List<String> deviceIds = new ArrayList<String>();
		String brandName = null;
		String modelName = null;
		String mdrQuery  = "INSERT INTO mobile_device_repository(device_id, is_test_imei, brand_name, model_name, allocation_date,"
				+ "band_detail, comms_bluetooth, comms_nfc, comms_wlan, device_type, imei_quantity, manufacturer, marketing_name,"
				+ "network_technology_gsm, network_technology_evdo, network_technology_lte, network_technology_5g, oem, os,"
				+ "removable_uicc, removable_euicc, nonremovable_uicc, nonremovable_euicc, sim_slot, device_state, user_id,"
				+ "created_on, modified_on, network_specific_identifier,is_type_approved)"
				+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
		Connection conn = null;
		PreparedStatement pstat = null;
		try {
			conn = this.getConnection();
			pstat = conn.prepareStatement(mdrQuery);
			for(GSMATacDetails tacDetail: tacDetails) {
				try {
					brandName = tacDetail.getBrandName().trim();
				} catch (Exception e) {
					// TODO: handle exception
					brandName="";
				}
				
				if(brandName == "" || brandName == "null")
					brandName = "Not Known";
				modelName = tacDetail.getModelName().trim();
				if(modelName == "" || modelName == "null")
					modelName = "Not Known";
				
				deviceIds.add(tacDetail.getTac());
				pstat.setString(1, tacDetail.getTac());
				
				pstat.setInt(2, 0);
				for(SystemConfigListDb series: testIMEIList) {
	            	if(tacDetail.getTac().startsWith(series.getInterpretation())) {
	            		pstat.setInt(2, 1);
	            		break;
	            	}
	            }
				pstat.setString(3, brandName);
				pstat.setString(4, modelName);
				pstat.setObject(5, null);
				if(Objects.nonNull(tacDetail.getAllocationDate()) && !tacDetail.getAllocationDate().equals("null"))
					pstat.setObject(5, tacDetail.getAllocationDate());
				pstat.setString(6, "");
				if(Objects.nonNull(tacDetail.getNetworkTechnology()) && !tacDetail.getNetworkTechnology().equals("null"))
					pstat.setString(6, tacDetail.getNetworkTechnology());
				
				pstat.setString(7, "");
				if(Objects.nonNull(tacDetail.getBluetooth()) && !tacDetail.getBluetooth().equals("null"))
					pstat.setString(7, tacDetail.getBluetooth());
				
				if(tacDetail.getNfc().trim().equalsIgnoreCase("yes"))
					pstat.setInt(8, 1);
				else
					pstat.setInt(8, 0);
				
				pstat.setString(9, "");
				if(Objects.nonNull(tacDetail.getWlan()) && !tacDetail.getWlan().equals("null"))
					pstat.setString(9, tacDetail.getWlan());
				
				pstat.setString(10, "");
				if(Objects.nonNull(tacDetail.getDeviceType()) && !tacDetail.getDeviceType().equals("null"))
					pstat.setString(10, tacDetail.getDeviceType());
				
				pstat.setInt(11, 0);
				if(Objects.nonNull(tacDetail.getImeiQuantity()))
					pstat.setInt(11, tacDetail.getImeiQuantity());
				
				pstat.setString(12, "");
				if(Objects.nonNull(tacDetail.getManufacturer()) && !tacDetail.getManufacturer().equals("null"))
					pstat.setString(12, tacDetail.getManufacturer());
				
				pstat.setString(13, "");
				if(Objects.nonNull(tacDetail.getMarketingName()) && !tacDetail.getMarketingName().equals("null"))
					pstat.setString(13, tacDetail.getMarketingName());
				
				try {
				
					if(tacDetail.getNetworkTechnology().trim().toLowerCase().contains("2g")) {
						pstat.setInt(14, 1);
					}else {
						pstat.setInt(14, 0);
					}
				} catch (Exception e) {
					// TODO: handle exception
					pstat.setInt(14, 0);
				}
				
				try {
					if(tacDetail.getNetworkTechnology().trim().toLowerCase().contains("3g")) {
						pstat.setInt(15, 1);
					}else {
						pstat.setInt(15, 0);
					}
				} catch (Exception e) {
					// TODO: handle exception
					pstat.setInt(15, 0);
				}
				try {
					if(tacDetail.getNetworkTechnology().trim().toLowerCase().contains("4g") 
							|| tacDetail.getNetworkTechnology().trim().toLowerCase().contains("lte")) {
						pstat.setInt(16, 1);
					}else {
						pstat.setInt(16, 0);
					}
				} catch (Exception e) {
					// TODO: handle exception
					pstat.setInt(16, 0);
				}
				
				try {
					if(tacDetail.getNetworkTechnology().trim().toLowerCase().contains("5g")) {
						pstat.setInt(17, 1);
					}else {
						pstat.setInt(17, 0);
					}
					
				} catch (Exception e) {
					// TODO: handle exception
					pstat.setInt(17, 0);
				}
				
				
				
				pstat.setString(18, "");
				if(Objects.nonNull(tacDetail.getOem()) && !tacDetail.getOem().equals("null"))
					pstat.setString(18, tacDetail.getOem());
				
				pstat.setString(19, "");
				if(Objects.nonNull(tacDetail.getOperatingSystem()) && !tacDetail.getOperatingSystem().equals("null"))
					pstat.setString(19, tacDetail.getOperatingSystem());
				
				pstat.setInt(20, 0);
				if(Objects.nonNull(tacDetail.getRemovableUICC()))
					pstat.setInt(20, tacDetail.getRemovableUICC());
				
				pstat.setInt(21, 0);
				if(Objects.nonNull(tacDetail.getRemovableEUICC()))
					pstat.setInt(21, tacDetail.getRemovableEUICC());
				
				pstat.setInt(22, 0);
				if(Objects.nonNull(tacDetail.getNonRemovableUICC()))
					pstat.setInt(22, tacDetail.getNonRemovableUICC());
				
				pstat.setInt(23, 0);
				if(Objects.nonNull(tacDetail.getNonRemovableEUICC()))
					pstat.setInt(23, tacDetail.getNonRemovableEUICC());
				
				pstat.setInt(24, 0);
				if(Objects.nonNull(tacDetail.getSimSlot()))
					pstat.setInt(24, tacDetail.getSimSlot());
				pstat.setInt(25, 0);
				pstat.setInt(26, userId);
				java.util.Date utilDate = new java.util.Date();
				pstat.setDate(27, new java.sql.Date(utilDate.getTime()));
				pstat.setDate(28, new java.sql.Date(utilDate.getTime()));
				pstat.setInt(29, 0);
				if(Objects.nonNull(tacDetail.getNetworkSpecificIdentifier()))
					pstat.setInt(29, tacDetail.getNetworkSpecificIdentifier());
				
				pstat.setInt(30, isTypeApprovedValues);
//				if(isTypeApprovedValues==0)
//					pstat.setInt(30, 1);
				
				pstat.addBatch();
			}
			pstat.executeBatch();
		} catch(Exception ex) {
			logger.error("Error druing saving new MDRs: "+ex.getMessage());
			logger.error(ex.getMessage(), ex);
			failFalg = true;
		}finally {
			try {
				if(pstat != null)
					pstat.close();
				if(conn != null)
					conn.close();
			} catch(Exception e) {}
		}
		return deviceIds;
	}
	
	private void saveHistoryData(List<Object[]> deviceDetails){
		String mdrQuery  = "INSERT INTO oam.mobile_device_repository_his(device_id, is_test_imei, brand_name, model_name,"
				+ "allocation_date, band_detail, comms_bluetooth, comms_nfc, comms_wlan, device_type, imei_quantity, manufacturer,"
				+ "marketing_name, network_technology_gsm, network_technology_evdo, network_technology_lte, network_technology_5g,"
				+ "oem, os, removable_uicc, removable_euicc, nonremovable_uicc, nonremovable_euicc, sim_slot, device_state,"
				+ "user_id, created_on, modified_on, updated_on, mdr_id, network_specific_identifier,is_type_approved)"
				+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
		Connection conn = null;
		PreparedStatement pstat = null;
		try {
			conn = this.getConnection();
			pstat = conn.prepareStatement(mdrQuery);
			for(Object[] deviceDetail: deviceDetails) {
				pstat.setString(1, (String)deviceDetail[0]);
				pstat.setBigDecimal(2,(BigDecimal) deviceDetail[1]);
				pstat.setString(3, (String)deviceDetail[2]);
				pstat.setString(4, (String)deviceDetail[3]);
				pstat.setObject(5, deviceDetail[4]);
				pstat.setString(6, (String)deviceDetail[5]);
				pstat.setString(7, (String)deviceDetail[6]);
				pstat.setBigDecimal(8, (BigDecimal)deviceDetail[7]);
				pstat.setString(9, (String)deviceDetail[8]);	
				pstat.setString(10, (String)deviceDetail[9]);
				pstat.setBigDecimal(11, (BigDecimal)deviceDetail[10]);
				pstat.setString(12, (String)deviceDetail[11]);
				pstat.setString(13, (String)deviceDetail[12]);
				pstat.setBigDecimal(14, (BigDecimal)deviceDetail[13]);
				pstat.setBigDecimal(15, (BigDecimal)deviceDetail[14]);
				pstat.setBigDecimal(16, (BigDecimal)deviceDetail[15]);
				pstat.setBigDecimal(17, (BigDecimal)deviceDetail[16]);
				pstat.setString(18, (String)deviceDetail[17]);
				pstat.setString(19, (String)deviceDetail[18]);			
				pstat.setBigDecimal(20,(BigDecimal)deviceDetail[19]);
				pstat.setBigDecimal(21, (BigDecimal)deviceDetail[20]);
				pstat.setBigDecimal(22, (BigDecimal)deviceDetail[21]);
				pstat.setBigDecimal(23, (BigDecimal)deviceDetail[22]);
				pstat.setBigDecimal(24, (BigDecimal)deviceDetail[23]);
				pstat.setBigDecimal(25, (BigDecimal)deviceDetail[24]);
				pstat.setBigDecimal(26, (BigDecimal)deviceDetail[25]);
				pstat.setObject(27, deviceDetail[26]);
				pstat.setObject(28, deviceDetail[27]);
				java.util.Date utilDate = new java.util.Date();
				pstat.setDate(29, new java.sql.Date(utilDate.getTime()));
				//pstat.setObject(29, LocalDateTime.now());
				pstat.setBigDecimal(30, (BigDecimal)deviceDetail[28]);
				pstat.setBigDecimal(31, (BigDecimal)deviceDetail[29]);
				try {
					int IS_TYPE_APPROVED= (int)deviceDetail[30];
					pstat.setInt(32, IS_TYPE_APPROVED);
				} catch (Exception e) {
					// TODO: handle exception
					try {
						pstat.setBigDecimal(32, (BigDecimal)deviceDetail[30]);
					} catch (Exception e2) {
						// TODO: handle exception
						pstat.setInt(32, 0);
					}
					
				}
				
				pstat.addBatch();
			}
			pstat.executeBatch();
		} catch(Exception ex) {
			logger.error("Error druing saving history of new MDRs: "+ex.getMessage());
			logger.error(ex.getMessage(), ex);
			failFalg = true;
		}finally {
			try {
				if(pstat != null)
					pstat.close();
				if(conn != null)
					conn.close();
			} catch(Exception e) {}
		}
	}
	
	private List<Object[]> getSavedDevices(List<String> deviceIds){
		List<Object[]> deviceDetails = new ArrayList<Object[]>();
		Object[] data = null;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = "select device_id, is_test_imei, brand_name, model_name, allocation_date,"
				+ "band_detail, comms_bluetooth, comms_nfc, comms_wlan, device_type, imei_quantity, manufacturer, marketing_name,"
				+ "network_technology_gsm, network_technology_evdo, network_technology_lte, network_technology_5g, oem, os,"
				+ "removable_uicc, removable_euicc, nonremovable_uicc, nonremovable_euicc, sim_slot, device_state, user_id,"
				+ "created_on, modified_on, id, network_specific_identifier,is_type_approved from mobile_device_repository where "
				+ "device_id in("+deviceIds.stream().collect(Collectors.joining("','", "'", "'"))+")";
		try {
			conn = this.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				data = new Object[rs.getMetaData().getColumnCount()];
				for( int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					data[i-1] = rs.getObject(i);
		        }
				deviceDetails.add(data);
				data = null;
            }
		}catch(Exception ex) {
			logger.error("Error druing saving new MDRs: "+ex.getMessage());
			logger.error(ex.getMessage(), ex);
		}finally {
			try {
				if(stmt != null)
					stmt.close();
				if(rs != null)
					rs.close();
				if(conn != null)
					conn.close();
			} catch(Exception e) {}
		}
		return deviceDetails;
	}
	
	private void saveAllNewBrandAndModel() {
		int i = 0;
		int batchSize = 100;
		Map<String, Integer> brandsMap = null;
		List<ModelBrandView> newBrandsModels = null;
//		List<BrandView> existingBrands = null;
		List<String> newBrands = null;
		Connection conn = null;
		PreparedStatement pstat = null;
		String brandQuery = "INSERT INTO dev_brand_name(brand_name, created_on) VALUES(?, ?)";
		String modelQuery = "INSERT INTO dev_model_name(brand_name, brand_name_id, model_name, created_on) VALUES(?, ?, ?, ?)";
		try {
			logger.info("Starting to update new brands and models in brand and model tables.");
			logger.info("Started reading all new brand and models.");
			newBrandsModels = this.getNewBrandAndModels();
			logger.info("Completed reading all new brand and models.");
			logger.info("Started reading all new brands.");
			newBrands = this.getNewBrands();
			logger.info("Completed reading all new brands.");
			conn = this.getConnection();
			pstat = conn.prepareStatement(brandQuery);
			for(String brand: newBrands) {
				pstat.setString(1, brand);
				pstat.setObject(2, LocalDateTime.now());
				pstat.addBatch();
				if( i == batchSize ) {
					pstat.executeBatch();
					pstat.clearBatch();
					i = 0;
				}
				i++;
			}
			pstat.executeBatch();
			if( pstat != null )
				pstat.close();
			if( conn != null )
				conn.close();
			logger.info("["+newBrands.size()+"] new brands saved.");
//			existingBrands  = brandService.getAllBrands();
//			brandsMap = existingBrands.parallelStream()
//	                .collect(Collectors.toMap(BrandView::getBrandName, BrandView::getBrandId, (existingValue, newValue) -> newValue));
			brandsMap = this.getAllBrands();
//			logger.info("Brands Map:{"+brandsMap.toString()+"}");
			conn = this.getConnection();
			pstat = conn.prepareStatement(modelQuery);
			i = 0;
			for(ModelBrandView view: newBrandsModels) {
//				logger.info("Model:{"+view.getModelName()+"} and Brand:{"+view.getBrandName()+"}");
				if( !view.getModelName().equals("") ) {
					pstat.setString(1, view.getBrandName());
					pstat.setInt(2, brandsMap.get(view.getBrandName().toLowerCase()));
					pstat.setString(3, view.getModelName());
					pstat.setObject(4, LocalDateTime.now());
					pstat.addBatch();
					if( i == batchSize ) {
						pstat.executeBatch();
						pstat.clearBatch();
						i = 0;
					}
					i++;
				}
			}
			pstat.executeBatch();
			logger.info("["+newBrandsModels.size()+"] new models saved.");
			logger.info("New brand and model update ends.");
		}catch(Exception ex) {
			logger.error("New model and brand update failed="+ex.getMessage());
			logger.error(ex.getMessage(), ex);
		}finally {
			try {
				if(pstat != null)
					pstat.close();
				if(conn != null)
					conn.close();
			} catch(Exception e) {}
		}
	}
	
	public List<ModelBrandView> getNewBrandAndModels(){
		List<ModelBrandView> newBrandAndModels = new ArrayList<ModelBrandView>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
//		String query = "SELECT distinct concat(mobile_device_repository.model_name, ' ', mobile_device_repository.brand_name) temp, mobile_device_repository.model_name, mobile_device_repository.brand_name "
//				+ "from mobile_device_repository LEFT JOIN dev_model_name ON mobile_device_repository.brand_name = dev_model_name.brand_name AND "
//				+ "mobile_device_repository.model_name = dev_model_name.model_name WHERE dev_model_name.brand_name IS NULL AND dev_model_name.model_name IS NULL and mobile_device_repository.brand_name !=''";
		String query = "SELECT distinct mobile_device_repository.model_name || ' ' || mobile_device_repository.brand_name temp, mobile_device_repository.model_name, mobile_device_repository.brand_name "
				+ "from mobile_device_repository LEFT JOIN dev_model_name ON mobile_device_repository.brand_name = dev_model_name.brand_name AND "
				+ "mobile_device_repository.model_name = dev_model_name.model_name WHERE dev_model_name.brand_name IS NULL AND dev_model_name.model_name IS NULL and mobile_device_repository.brand_name !=''";
		logger.info("newBrandAndModels Query ---> "+query);
		try {
			conn = this.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				newBrandAndModels.add(new ModelBrandView(rs.getString("model_name"), rs.getString("brand_name")));
            }
		}catch(Exception ex) {
			logger.error("Error druing saving new MDRs: "+ex.getMessage());
			logger.error(ex.getMessage(), ex);
		}finally {
			try {
				if(stmt != null)
					stmt.close();
				if(rs != null)
					rs.close();
				if(conn != null)
					conn.close();
			} catch(Exception e) {}
		}
		return newBrandAndModels;
	}
	
	public List<String> getNewBrands(){
		List<String> newBrands = new ArrayList<String>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = "SELECT distinct mobile_device_repository.brand_name from mobile_device_repository LEFT JOIN dev_brand_name "
				+ "ON mobile_device_repository.brand_name = dev_brand_name.brand_name WHERE dev_brand_name.brand_name IS NULL AND "
				+ "mobile_device_repository.brand_name !=''";
		try {
			conn = this.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				newBrands.add(rs.getString("brand_name"));
            }
		}catch(Exception ex) {
			logger.error("Error druing saving new MDRs: "+ex.getMessage());
			logger.error(ex.getMessage(), ex);
		}finally {
			try {
				if(stmt != null)
					stmt.close();
				if(rs != null)
					rs.close();
				if(conn != null)
					conn.close();
			} catch(Exception e) {}
		}
		return newBrands;
	}
	
	public Map<String, Integer> getAllBrands(){
		Map<String, Integer> brands = new HashMap<String, Integer>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = "SELECT id, brand_name from dev_brand_name";
		try {
			conn = this.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				brands.put(rs.getString("brand_name").toLowerCase(), rs.getInt("id"));
            }
		}catch(Exception ex) {
			logger.error("Error druing saving new MDRs: "+ex.getMessage());
			logger.error(ex.getMessage(), ex);
		}finally {
			try {
				if(stmt != null)
					stmt.close();
				if(rs != null)
					rs.close();
				if(conn != null)
					conn.close();
			} catch(Exception e) {}
		}
		return brands;
	}
	
	public void deleteOldBrands(){
		Connection conn = null;
		Statement stmt = null;
		String query = "delete from dev_brand_name where not exists(select mobile_device_repository.brand_name "
				+ "from mobile_device_repository where mobile_device_repository.brand_name=dev_brand_name.brand_name)";

		try {
			conn = this.getConnection();
			stmt = conn.createStatement();
			int deletedRowsCount =stmt.executeUpdate(query);
			logger.info("["+deletedRowsCount+"] old brands got deleted.");
		}catch(Exception ex) {
			logger.error("Error druing saving new MDRs: "+ex.getMessage());
			logger.error(ex.getMessage(), ex);
		}finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			} catch(Exception e) {}
		}
	}
	
	public void deleteOldModels(){
		Connection conn = null;
		Statement stmt = null;
		String query = "delete from dev_model_name where not exists(select mobile_device_repository.model_name "
				+ "from mobile_device_repository where mobile_device_repository.model_name=dev_model_name.model_name)";

		try {
			conn = this.getConnection();
			stmt = conn.createStatement();
			int deletedRowsCount =stmt.executeUpdate(query);
			logger.info("["+deletedRowsCount+"] old models got deleted.");
		}catch(Exception ex) {
			logger.error("Error druing saving new MDRs: "+ex.getMessage());
			logger.error(ex.getMessage(), ex);
		}finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			} catch(Exception e) {}
		}
	}
	
	
	public void raiseAnAlert(String alertCode, String alertMessage, String alertProcess, int userId) {
		Process p = null;
		String path = null;
		String line = null;
        String response = null;
		ProcessBuilder pb = null;
		BufferedReader reader = null;
        try {
            path = System.getenv("APP_HOME") + "alert/start.sh";
            pb = new ProcessBuilder(path, alertCode, alertMessage, alertProcess, String.valueOf(userId));
            p = pb.start();
            reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = reader.readLine()) != null) {
                response += line;
            }
            logger.info("Alert is generated :response " + response);
        } catch (Exception ex) {
            logger.error("Not able to execute Alert mgnt jar ", ex.getLocalizedMessage() + " ::: " + ex.getMessage());
        }
    }
	
	public Connection getConnection() {
		EntityManagerFactoryInfo info = (EntityManagerFactoryInfo) deviceEntityManager.getEntityManagerFactory();
	    try {
			return info.getDataSource().getConnection();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
}
