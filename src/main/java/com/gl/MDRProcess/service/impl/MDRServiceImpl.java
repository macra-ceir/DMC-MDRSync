package com.gl.MDRProcess.service.impl;

import com.gl.MDRProcess.configuration.PropertiesReader;
import com.gl.MDRProcess.model.app.AlertMessages;
import com.gl.MDRProcess.model.app.GSMATacDetails;
import com.gl.MDRProcess.model.app.RuleDb;
import com.gl.MDRProcess.model.app.SystemConfigListDb;
import com.gl.MDRProcess.model.audit.ModulesAuditTrail;
import com.gl.MDRProcess.model.view.ModelBrandView;
import com.gl.MDRProcess.repo.app.AlertRepository;
import com.gl.MDRProcess.repo.app.MobileDeviceRepoRepository;
import com.gl.MDRProcess.repo.app.RuleRepository;
import com.gl.MDRProcess.repo.app.SystemConfigListRepository;
import com.gl.MDRProcess.repo.audit.ModulesAuditTrailRepository;
import com.gl.MDRProcess.util.Utility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class MDRServiceImpl {

    private static final Logger logger = LogManager.getLogger(MDRServiceImpl.class);

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
    AlertRepository alertRepository;

    @Autowired
    RuleRepository ruleRepository;

    @Autowired
    AlertService alertService;

    @Autowired
    ModuleAuditTrailService moduleAuditTrailService;

    @Autowired
    @PersistenceContext//(unitName = "appEntityManagerFactory")
    private EntityManager deviceEntityManager;

    boolean failFalg = false;

    public void insertMDR(LocalDate localDate) {
        int insertionCount = 0;
        Pageable pageable = null;
        int batchSize = propertiesReader.batchSize;
        Integer userId = propertiesReader.userId;
        long startTime = 0;
        long totalTime = 0;
        int isTypeApprovedValues = 0;
        String moduleName = propertiesReader.moduleName;
        String feature = propertiesReader.feature;
        String serverName = null;
        AlertMessages alertMessages = null;
        List<GSMATacDetails> tacDetails = null;
        List<SystemConfigListDb> testIMEIList = null;
        List<Object[]> newDeviceDetails = null;
        List<String> deviceIds = null;

        if (!moduleAuditTrailService.previousDependentModuleExecuted(localDate, propertiesReader.getDependentFeatureName())) {
            logger.info("localDate Received in previousDependentModule {}" ,localDate);
            logger.info("Process:{} will not execute as already Dependent Module:{} Not Executed for the day {}", propertiesReader.getFeatureName(), propertiesReader.getDependentFeatureName(), localDate);
            return;
        }
        if (!moduleAuditTrailService.runProcess(localDate, propertiesReader.getFeatureName())) {
            logger.info("localDate  Received in runProcess{}" ,localDate);
            logger.info("Process:{} will not execute it may already Running or Completed for the day {}", propertiesReader.getFeatureName(), localDate);
            return;
        }

        try {
            serverName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        ModulesAuditTrail modulesAuditTrail = new ModulesAuditTrail((int) 0, 201,
                "Initial", moduleName, feature, "Sync", 0, serverName);

        try {
            logger.info("Recovery Mobile Device Repository History update process is started.");
            long logTime = System.currentTimeMillis();
            newDeviceDetails = this.retrySavedDevices(deviceIds);
            if (newDeviceDetails.size() > 0) {
                this.saveHistoryData(newDeviceDetails);
            }
            if (failFalg) {
                newDeviceDetails = null;
                logger.error("Mobile Device Repository Database Sync Utility Failed, error while saving MDR history");
                System.exit(0);
            }
            logger.info("End Recovery History saved in the database, total time:{" + String.valueOf(System.currentTimeMillis() - logTime) + "}");
        } catch (Exception e) {
            // TODO: handle exception
            newDeviceDetails = null;
            logger.error("Exception Mobile Device Repository Database Sync Utility Failed, error while saving MDR history");
        }
        try {
            logger.info("Mobile Device Repository update process is started.");
            //serverName = InetAddress.getLocalHost().getHostName();
            pageable = PageRequest.of(0, batchSize);
            startTime = System.currentTimeMillis();
            modulesAuditTrail = modulesAuditTrailRepository.save(modulesAuditTrail);
            testIMEIList = systemConfigListRepository.findByTag("TEST_IMEI_SERIES");

            RuleDb isApproved = null;

            try {

                isApproved = ruleRepository.findByName("TYPE_APPROVED");

                logger.info("Going to get new records for rule table default values false : " + isApproved.toString() + " and isTypeApprovedValues : " + isTypeApprovedValues);
                if (isApproved != null) {

                    if (isApproved.getState().equalsIgnoreCase("Enabled")) {
                        isTypeApprovedValues = 1;
                    } else {
                        isTypeApprovedValues = 0;
                    }

                    logger.info("Going to get new records for rule table default values false : " + isApproved.getState() + " and isTypeApprovedValues : " + isTypeApprovedValues);
                }
            } catch (Exception e) {

                // TODO: handle exception
                logger.info("Exception Going to get new records for rule table default values false : " + isApproved.getState() + " and isTypeApprovedValues : " + isTypeApprovedValues);
                e.printStackTrace();
                alertMessages = alertRepository.findByAlertIdIn("alert1102");
                logger.error("Going to Raise alert alert1102 ,description of alert is={}", alertMessages.getDescription());
                logger.error("Alert alert1102 is raised");
                raiseAnAlert("alert1102", null, "GSMA to Mobile Device Repository Sync", 0);
                System.exit(0);
            }

            if (Objects.isNull(testIMEIList) || testIMEIList.isEmpty()) {
                alertMessages = alertRepository.findByAlertIdIn("alert1101");
                logger.error("Going to Raise alert alert1101 ,description of alert is={}", alertMessages.getDescription());
                logger.error("Alert alert1101 is raised");
                raiseAnAlert("alert1101", null, "GSMA to Mobile Device Repository Sync", 0);
                modulesAuditTrail.setStatusCode(501);
                modulesAuditTrail.setStatus("Failed");
                modulesAuditTrail.setInfo(alertMessages.getDescription());
                modulesAuditTrailRepository.save(modulesAuditTrail);
                System.exit(0);
            }
            logger.info("Going to get new records for GSMA table.");
            long logTime = System.currentTimeMillis();
            tacDetails = this.getGSMATacs(batchSize);
            if (failFalg) {

                alertMessages = alertRepository.findByAlertIdIn("alert1103");
                logger.error("Going to Raise alert alert1103 ,description of alert is={}", alertMessages.getDescription());
                logger.error("Alert alert1103 is raised");
                raiseAnAlert("alert1103", null, "GSMA to Mobile Device Repository Sync", 0);

                modulesAuditTrail.setStatusCode(501);
                modulesAuditTrail.setStatus("Failed");
                modulesAuditTrail.setInfo(alertMessages.getDescription());
                modulesAuditTrailRepository.save(modulesAuditTrail);
                System.exit(0);

            }
            logger.info("Records reading done, total time:{" + String.valueOf(System.currentTimeMillis() - logTime) + "}");
            while (tacDetails.size() > 0) {
                logger.info("Record processing started.");
                logTime = System.currentTimeMillis();
                deviceIds = processDeviceDataAndSave(tacDetails, userId, testIMEIList, isTypeApprovedValues);
                if (failFalg) {
                    modulesAuditTrail.setStatusCode(501);
                    modulesAuditTrail.setStatus("Failed");
                    modulesAuditTrail.setInfo(alertMessages.getDescription());
                    modulesAuditTrailRepository.save(modulesAuditTrail);

                    alertMessages = alertRepository.findByAlertIdIn("alert1104");
                    logger.error("Going to Raise alert alert1104 ,description of alert is={}", alertMessages.getDescription());
                    logger.error("Alert alert1104 is raised");

                    raiseAnAlert("alert1104", null, "GSMA to Mobile Device Repository Sync", 0);
                    System.exit(0);
                }
                logger.info("Record processing ended. Total time:{" + String.valueOf(System.currentTimeMillis() - logTime) + "}");
                newDeviceDetails = this.getSavedDevices(deviceIds);
                deviceIds = null;
                logger.info("History processing started");
                logTime = System.currentTimeMillis();
                this.saveHistoryData(newDeviceDetails);
                if (failFalg) {
                    logger.error("Alert alert1105 is raised");
                    modulesAuditTrail.setStatusCode(501);
                    modulesAuditTrail.setStatus("Failed");
                    modulesAuditTrail.setInfo(alertMessages.getDescription());
                    modulesAuditTrailRepository.save(modulesAuditTrail);
                    alertMessages = alertRepository.findByAlertIdIn("alert1105");
                    raiseAnAlert("alert1105", null, "GSMA to Mobile Device Repository Sync", 0);
                    logger.error("Going to Raise alert alert1105 ,description of alert is={}", alertMessages.getDescription());
                    logger.error("Alert alert1105 is raised");
                    System.exit(0);
                }
                logger.info("History saved in the database, total time:{" + String.valueOf(System.currentTimeMillis() - logTime) + "}");
                insertionCount += tacDetails.size();
                logger.info("Going to get new records for GSMA table.");
                logTime = System.currentTimeMillis();
                tacDetails = this.getGSMATacs(batchSize);
                if (failFalg) {
                    logger.info("Again, new records for the GSMA table. Then, an error prevents saving new MDRs.");
                    modulesAuditTrail.setStatusCode(501);
                    modulesAuditTrail.setStatus("Failed");
                    modulesAuditTrail.setInfo(alertMessages.getDescription());
                    modulesAuditTrailRepository.save(modulesAuditTrail);

                    alertMessages = alertRepository.findByAlertIdIn("alert1103");
                    logger.error("Going to Raise alert alert1103 ,description of alert is={}", alertMessages.getDescription());
                    logger.error("Alert alert1103 is raised");
                    raiseAnAlert("alert1103", null, "GSMA to Mobile Device Repository Sync", 0);

                    System.exit(0);
                    // break;
                }
                logger.info("Records reading done, total time:{" + String.valueOf(System.currentTimeMillis() - logTime) + "}");
            }
            totalTime = System.currentTimeMillis() - startTime;
            modulesAuditTrail.setExecutionTime((int) totalTime);
            modulesAuditTrail.setStatusCode(200);
            modulesAuditTrail.setStatus("Success");
            modulesAuditTrail.setCount(insertionCount);
            modulesAuditTrailRepository.save(modulesAuditTrail);
            logger.info("Mobile Device Repository entry completed.");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            modulesAuditTrail.setStatusCode(501);
            modulesAuditTrail.setStatus("Failed");
            modulesAuditTrail.setInfo("Processing failed during DB Update");
            modulesAuditTrailRepository.save(modulesAuditTrail);
            logger.error("Mobile Device Repository insertion failed=" + e.getMessage());

            alertMessages = alertRepository.findByAlertIdIn("alert1106");
            logger.error("Going to Raise alert alert1106 ,description of alert is={}", alertMessages.getDescription());
            raiseAnAlert("alert1106", null, "GSMA to Mobile Device Repository Sync", 0);
            logger.error("Alert alert1106 is raised");
            System.exit(0);
        }
        try {
            this.deleteOldModels();
            this.deleteOldBrands();
            logger.info("Going to save all new brand and models.");
            long logTime = System.currentTimeMillis();
            try {
                this.saveAllNewBrandAndModel();
                logger.info("All new brand and models saved, total time:{{}}", String.valueOf(System.currentTimeMillis() - logTime));
            } catch (Exception ex) {
                logger.error("Failed to insert Brand and Model={}", ex.getMessage());
                logger.error(ex.getMessage(), ex);
                alertMessages = alertRepository.findByAlertIdIn("alert1108");
                logger.error("Going to Raise alert alert1108 ,description of alert is=" + alertMessages.getDescription());
                raiseAnAlert("alert1108", null, "GSMA to Mobile Device Repository Sync", 0);
                logger.error("Alert alert1108 is raised");
            }

        } catch (Exception ex) {
            logger.error("Model table and brand table update failed=" + ex.getMessage());
            logger.error(ex.getMessage(), ex);
            alertMessages = alertRepository.findByAlertIdIn("alert1107");
            logger.error("Going to Raise alert alert1107 ,description of alert is=" + alertMessages.getDescription());
            raiseAnAlert("alert1107", null, "GSMA to Mobile Device Repository Sync", 0);
            logger.error("Alert alert1107 is raised");
        }
        logger.info("Mobile Device Repository update process is ended.");
    }

    public List<GSMATacDetails> getGSMATacs(int batchLimit) {
        List<GSMATacDetails> tacDetails = null;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String query = "select tac, g.model_name, g.brand_name, g.allocation_date, network_technology, bluetooth,"
                + "nfc, wlan, g.device_type, g.imei_quantity, g.manufacturer, g.marketing_name, g.oem, operating_system,"
                + "g.removable_uicc, g.removable_euicc, g.nonremovable_uicc, g.nonremovable_euicc, g.sim_slot, g.network_specific_identifier "
                + "from gsma_tac_detail as g left join mobile_device_repository as m on tac=device_id where "
                + "device_id IS NULL and g.device_type in "
                + "(" + propertiesReader.filterDeviceTypes.stream().map(name -> "'" + name + "'").collect(Collectors.joining(",")) + ")"
                + " limit " + batchLimit;
        try {
            conn = this.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            tacDetails = new ArrayList<GSMATacDetails>();
            while (rs.next()) {
                tacDetails.add(new GSMATacDetails(
                        rs.getString("tac"),
                        rs.getString("model_name"),
                        rs.getString("brand_name"),
                        rs.getTimestamp("allocation_date").toLocalDateTime(),
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
        } catch (Exception ex) {
            logger.error("Error druing saving new MDRs: " + ex.getMessage());
            logger.error(ex.getMessage(), ex);
            failFalg = true;
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
                if (rs != null)
                    rs.close();
                if (conn != null)
                    conn.close();
            } catch (Exception e) {
            }
        }
        return tacDetails;
    }

    private List<String> processDeviceDataAndSave(List<GSMATacDetails> tacDetails, Integer userId,
                                                  List<SystemConfigListDb> testIMEIList, int isTypeApprovedValues) {
        List<String> deviceIds = new ArrayList<String>();
        String brandName = null;
        String modelName = null;
        String mdrQuery = "INSERT INTO mobile_device_repository(device_id, is_test_imei, brand_name, model_name, allocation_date,"
                + "band_detail, comms_bluetooth, comms_nfc, comms_wlan, device_type, imei_quantity, manufacturer, marketing_name,"
                + "network_technology_gsm, network_technology_evdo, network_technology_lte, network_technology_5g, oem, os,"
                + "removable_uicc, removable_euicc, nonremovable_uicc, nonremovable_euicc, sim_slot, device_state, user_id,"
                + "created_on, modified_on, network_specific_identifier,is_type_approved,trc_approved_status,trc_type_approved_by,trc_approval_date)"
                + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstat = null;
        try {
            conn = this.getConnection();
            pstat = conn.prepareStatement(mdrQuery);
            for (GSMATacDetails tacDetail : tacDetails) {
                brandName = tacDetail.getBrandName().trim();
                if (brandName == "" || brandName == "null")
                    brandName = "Not Known";
                modelName = tacDetail.getModelName().trim();
                if (modelName == "" || modelName == "null")
                    modelName = "Not Known";

                deviceIds.add(tacDetail.getTac());
                pstat.setString(1, tacDetail.getTac());
                pstat.setInt(2, 0);
                for (SystemConfigListDb series : testIMEIList) {
                    if (tacDetail.getTac().startsWith(series.getInterpretation())) {
                        pstat.setInt(2, 1);
                        break;
                    }
                }
                pstat.setString(3, brandName);
                pstat.setString(4, modelName);
                pstat.setObject(5, null);
                if (Objects.nonNull(tacDetail.getAllocationDate()) && !tacDetail.getAllocationDate().equals("null"))
                    pstat.setObject(5, tacDetail.getAllocationDate());
                pstat.setString(6, "");
                if (Objects.nonNull(tacDetail.getNetworkTechnology()) && !tacDetail.getNetworkTechnology().equals("null"))
                    pstat.setString(6, tacDetail.getNetworkTechnology());

                pstat.setString(7, "");
                if (Objects.nonNull(tacDetail.getBluetooth()) && !tacDetail.getBluetooth().equals("null"))
                    pstat.setString(7, tacDetail.getBluetooth());

                if (tacDetail.getNfc().trim().equalsIgnoreCase("yes"))
                    pstat.setInt(8, 1);
                else
                    pstat.setInt(8, 0);

                pstat.setString(9, "");
                if (Objects.nonNull(tacDetail.getWlan()) && !tacDetail.getWlan().equals("null"))
                    pstat.setString(9, tacDetail.getWlan());

                pstat.setString(10, "");
                if (Objects.nonNull(tacDetail.getDeviceType()) && !tacDetail.getDeviceType().equals("null"))
                    pstat.setString(10, tacDetail.getDeviceType());

                pstat.setInt(11, 0);
                if (Objects.nonNull(tacDetail.getImeiQuantity()))
                    pstat.setInt(11, tacDetail.getImeiQuantity());

                pstat.setString(12, "");
                if (Objects.nonNull(tacDetail.getManufacturer()) && !tacDetail.getManufacturer().equals("null"))
                    pstat.setString(12, tacDetail.getManufacturer());

                pstat.setString(13, "");
                if (Objects.nonNull(tacDetail.getMarketingName()) && !tacDetail.getMarketingName().equals("null"))
                    pstat.setString(13, tacDetail.getMarketingName());

                if (tacDetail.getNetworkTechnology().trim().toLowerCase().contains("2g")) {
                    pstat.setInt(14, 1);
                } else {
                    pstat.setInt(14, 0);
                }

                if (tacDetail.getNetworkTechnology().trim().toLowerCase().contains("3g")) {
                    pstat.setInt(15, 1);
                } else {
                    pstat.setInt(15, 0);
                }

                if (tacDetail.getNetworkTechnology().trim().toLowerCase().contains("4g")
                        || tacDetail.getNetworkTechnology().trim().toLowerCase().contains("lte")) {
                    pstat.setInt(16, 1);
                } else {
                    pstat.setInt(16, 0);
                }

                if (tacDetail.getNetworkTechnology().trim().toLowerCase().contains("5g")) {
                    pstat.setInt(17, 1);
                } else {
                    pstat.setInt(17, 0);
                }

                pstat.setString(18, "");
                if (Objects.nonNull(tacDetail.getOem()) && !tacDetail.getOem().equals("null"))
                    pstat.setString(18, tacDetail.getOem());

                pstat.setString(19, "");
                if (Objects.nonNull(tacDetail.getOperatingSystem()) && !tacDetail.getOperatingSystem().equals("null"))
                    pstat.setString(19, tacDetail.getOperatingSystem());

                pstat.setInt(20, 0);
                if (Objects.nonNull(tacDetail.getRemovableUICC()))
                    pstat.setInt(20, tacDetail.getRemovableUICC());

                pstat.setInt(21, 0);
                if (Objects.nonNull(tacDetail.getRemovableEUICC()))
                    pstat.setInt(21, tacDetail.getRemovableEUICC());

                pstat.setInt(22, 0);
                if (Objects.nonNull(tacDetail.getNonRemovableUICC()))
                    pstat.setInt(22, tacDetail.getNonRemovableUICC());

                pstat.setInt(23, 0);
                if (Objects.nonNull(tacDetail.getNonRemovableEUICC()))
                    pstat.setInt(23, tacDetail.getNonRemovableEUICC());

                pstat.setInt(24, 0);
                if (Objects.nonNull(tacDetail.getSimSlot()))
                    pstat.setInt(24, tacDetail.getSimSlot());
                pstat.setInt(25, 0);
                pstat.setInt(26, userId);
                pstat.setObject(27, LocalDateTime.now());
                pstat.setObject(28, LocalDateTime.now());
                pstat.setInt(29, 0);
                if (Objects.nonNull(tacDetail.getNetworkSpecificIdentifier()))
                    pstat.setInt(29, tacDetail.getNetworkSpecificIdentifier());

                if (isTypeApprovedValues == 1) {
                    pstat.setInt(30, 1);
                    pstat.setString(31, "");
                    pstat.setString(32, "");
                    pstat.setObject(33, null);

                } else {
                    pstat.setInt(30, 0);
                    pstat.setString(31, "Approved");
                    pstat.setString(32, "System");
                    pstat.setObject(33, LocalDateTime.now());
                }

                pstat.addBatch();
            }
            pstat.executeBatch();
        } catch (Exception ex) {
            logger.error("Error druing saving new MDRs: " + ex.getMessage());
            logger.error(ex.getMessage(), ex);
            failFalg = true;
        } finally {
            try {
                if (pstat != null)
                    pstat.close();
                if (conn != null)
                    conn.close();
            } catch (Exception e) {
            }
        }
        return deviceIds;
    }

    private void saveHistoryData(List<Object[]> deviceDetails) {
        String mdrQuery = "INSERT INTO oam.mobile_device_repository_his(device_id, is_test_imei, brand_name, model_name,"
                + "allocation_date, band_detail, comms_bluetooth, comms_nfc, comms_wlan, device_type, imei_quantity, manufacturer,"
                + "marketing_name, network_technology_gsm, network_technology_evdo, network_technology_lte, network_technology_5g,"
                + "oem, os, removable_uicc, removable_euicc, nonremovable_uicc, nonremovable_euicc, sim_slot, device_state,"
                + "user_id, created_on, modified_on, updated_on, mdr_id, network_specific_identifier,is_type_approved,trc_approved_status,trc_type_approved_by,trc_approval_date,manufacturer_country)"
                + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
        Connection conn = null;
        PreparedStatement pstat = null;
        try {
            conn = this.getConnection();
            pstat = conn.prepareStatement(mdrQuery);
            for (Object[] deviceDetail : deviceDetails) {
                pstat.setString(1, (String) deviceDetail[0]);
                pstat.setInt(2, (int) deviceDetail[1]);
                pstat.setString(3, (String) deviceDetail[2]);
                pstat.setString(4, (String) deviceDetail[3]);
                pstat.setObject(5, deviceDetail[4]);
                pstat.setString(6, (String) deviceDetail[5]);
                pstat.setString(7, (String) deviceDetail[6]);
                pstat.setInt(8, (int) deviceDetail[7]);
                pstat.setString(9, (String) deviceDetail[8]);
                pstat.setString(10, (String) deviceDetail[9]);
                pstat.setInt(11, (int) deviceDetail[10]);
                pstat.setString(12, (String) deviceDetail[11]);
                pstat.setString(13, (String) deviceDetail[12]);
                pstat.setInt(14, (int) deviceDetail[13]);
                pstat.setInt(15, (int) deviceDetail[14]);
                pstat.setInt(16, (int) deviceDetail[15]);
                pstat.setInt(17, (int) deviceDetail[16]);
                pstat.setString(18, (String) deviceDetail[17]);
                pstat.setString(19, (String) deviceDetail[18]);
                pstat.setInt(20, (int) deviceDetail[19]);
                pstat.setInt(21, (int) deviceDetail[20]);
                pstat.setInt(22, (int) deviceDetail[21]);
                pstat.setInt(23, (int) deviceDetail[22]);
                pstat.setInt(24, (int) deviceDetail[23]);
                pstat.setInt(25, (int) deviceDetail[24]);
                pstat.setInt(26, (int) deviceDetail[25]);
                pstat.setObject(27, deviceDetail[26]);
                pstat.setObject(28, deviceDetail[27]);
                pstat.setObject(29, LocalDateTime.now());
                pstat.setInt(30, (int) deviceDetail[28]);
                pstat.setInt(31, (int) deviceDetail[29]);
                pstat.setInt(32, (int) deviceDetail[30]);
                pstat.setString(33, (String) deviceDetail[31]);
                pstat.setString(34, (String) deviceDetail[32]);
                pstat.setObject(35, deviceDetail[33]);
                pstat.setString(36, (String) deviceDetail[34]);

                pstat.addBatch();
            }
            pstat.executeBatch();
        } catch (Exception ex) {
            logger.error("Error druing saving history of new MDRs: " + ex.getMessage());
            logger.error(ex.getMessage(), ex);
            failFalg = true;
        } finally {
            try {
                if (pstat != null)
                    pstat.close();
                if (conn != null)
                    conn.close();
            } catch (Exception e) {
            }
        }
    }

    private List<Object[]> getSavedDevices(List<String> deviceIds) {
        List<Object[]> deviceDetails = new ArrayList<Object[]>();
        Object[] data = null;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String query = "select device_id, is_test_imei, brand_name, model_name, allocation_date,"
                + "band_detail, comms_bluetooth, comms_nfc, comms_wlan, device_type, imei_quantity, manufacturer, marketing_name,"
                + "network_technology_gsm, network_technology_evdo, network_technology_lte, network_technology_5g, oem, os,"
                + "removable_uicc, removable_euicc, nonremovable_uicc, nonremovable_euicc, sim_slot, device_state, user_id,"
                + "created_on, modified_on, id, network_specific_identifier, is_type_approved,trc_approved_status,trc_type_approved_by,trc_approval_date,manufacturer_country from mobile_device_repository where "
                + "device_id in(" + deviceIds.stream().collect(Collectors.joining("','", "'", "'")) + ")";
        try {
            conn = this.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                data = new Object[rs.getMetaData().getColumnCount()];
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    data[i - 1] = rs.getObject(i);
                }
                deviceDetails.add(data);
                data = null;
            }
        } catch (Exception ex) {
            logger.error("Error druing saving new MDRs: " + ex.getMessage());
            logger.error(ex.getMessage(), ex);
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
                if (rs != null)
                    rs.close();
                if (conn != null)
                    conn.close();
            } catch (Exception e) {
            }
        }
        return deviceDetails;
    }

    private List<Object[]> retrySavedDevices(List<String> deviceIds) {
        List<Object[]> deviceDetails = new ArrayList<Object[]>();
        Object[] data = null;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        String query = "select m.device_id, m.is_test_imei, m.brand_name, m.model_name, m.allocation_date,"
                + "m.band_detail, m.comms_bluetooth, m.comms_nfc, m.comms_wlan, m.device_type, m.imei_quantity, m.manufacturer, m.marketing_name,"
                + "m.network_technology_gsm, m.network_technology_evdo, m.network_technology_lte, m.network_technology_5g, m.oem, m.os,"
                + "m.removable_uicc, m.removable_euicc, m.nonremovable_uicc, m.nonremovable_euicc, m.sim_slot, m.device_state, m.user_id,"
                + "m.created_on, m.modified_on, m.id, m.network_specific_identifier, m.is_type_approved, m.trc_approved_status, m.trc_type_approved_by,"
                + "m.trc_approval_date, m.manufacturer_country from mobile_device_repository m LEFT JOIN oam.mobile_device_repository_his h ON m.device_id=h.device_id where "
                + " h.device_id IS NULL";
        try {
            conn = this.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                data = new Object[rs.getMetaData().getColumnCount()];
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    data[i - 1] = rs.getObject(i);
                }
                deviceDetails.add(data);
                data = null;
            }
        } catch (Exception ex) {
            logger.error("Error druing retry saving new MDRs: " + ex.getMessage());
            logger.error(ex.getMessage(), ex);
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
                if (rs != null)
                    rs.close();
                if (conn != null)
                    conn.close();
            } catch (Exception e) {
            }
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
            for (String brand : newBrands) {
                pstat.setString(1, brand);
                pstat.setObject(2, LocalDateTime.now());
                pstat.addBatch();
                if (i == batchSize) {
                    pstat.executeBatch();
                    pstat.clearBatch();
                    i = 0;
                }
                i++;
            }
            pstat.executeBatch();
            if (pstat != null)
                pstat.close();
            if (conn != null)
                conn.close();
            logger.info("[" + newBrands.size() + "] new brands saved.");

            brandsMap = this.getAllBrands();
            conn = this.getConnection();
            pstat = conn.prepareStatement(modelQuery);
            i = 0;
            for (ModelBrandView view : newBrandsModels) {
                if (!view.getModelName().equals("")) {
                    pstat.setString(1, view.getBrandName());
                    pstat.setInt(2, brandsMap.get(view.getBrandName().toLowerCase()));
                    pstat.setString(3, view.getModelName());
                    pstat.setObject(4, LocalDateTime.now());
                    pstat.addBatch();
                    if (i == batchSize) {
                        pstat.executeBatch();
                        pstat.clearBatch();
                        i = 0;
                    }
                    i++;
                }
            }
            pstat.executeBatch();
            logger.info("[" + newBrandsModels.size() + "] new models saved.");
            logger.info("New brand and model update ends.");
        } catch (Exception ex) {
            logger.error("New model and brand update failed=" + ex.getMessage());
            logger.error(ex.getMessage(), ex);
        } finally {
            try {
                if (pstat != null)
                    pstat.close();
                if (conn != null)
                    conn.close();
            } catch (Exception e) {
            }
        }
    }

    public List<ModelBrandView> getNewBrandAndModels() {
        List<ModelBrandView> newBrandAndModels = new ArrayList<ModelBrandView>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String query = "SELECT distinct concat(mdr.model_name, ' ', mdr.brand_name) temp, mdr.model_name, mdr.brand_name "
                + "from mobile_device_repository as mdr LEFT JOIN dev_model_name as m ON mdr.brand_name = m.brand_name AND "
                + "mdr.model_name = m.model_name WHERE m.brand_name IS NULL AND m.model_name IS NULL and mdr.brand_name !=''";
        try {
            conn = this.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                newBrandAndModels.add(new ModelBrandView(rs.getString("model_name"), rs.getString("brand_name")));
            }
        } catch (Exception ex) {
            logger.error("Error druing saving new MDRs: " + ex.getMessage());
            logger.error(ex.getMessage(), ex);
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
                if (rs != null)
                    rs.close();
                if (conn != null)
                    conn.close();
            } catch (Exception e) {
            }
        }
        return newBrandAndModels;
    }

    public List<String> getNewBrands() {
        List<String> newBrands = new ArrayList<String>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String query = "SELECT distinct mdr.brand_name from mobile_device_repository as mdr LEFT JOIN dev_brand_name as m "
                + "ON mdr.brand_name = m.brand_name WHERE m.brand_name IS NULL AND "
                + "mdr.brand_name !=''";
        try {
            conn = this.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                newBrands.add(rs.getString("brand_name"));
            }
        } catch (Exception ex) {
            logger.error("Error druing saving new MDRs: " + ex.getMessage());
            logger.error(ex.getMessage(), ex);
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
                if (rs != null)
                    rs.close();
                if (conn != null)
                    conn.close();
            } catch (Exception e) {
            }
        }
        return newBrands;
    }

    public Map<String, Integer> getAllBrands() {
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
        } catch (Exception ex) {
            logger.error("Error druing saving new MDRs: " + ex.getMessage());
            logger.error(ex.getMessage(), ex);
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
                if (rs != null)
                    rs.close();
                if (conn != null)
                    conn.close();
            } catch (Exception e) {
            }
        }
        return brands;
    }

    public void deleteOldBrands() {
        Connection conn = null;
        Statement stmt = null;
        String query = "delete from dev_brand_name as brand where not exists(select mdr.brand_name "
                + "from mobile_device_repository as mdr where mdr.brand_name=brand.brand_name)";

        try {
            conn = this.getConnection();
            stmt = conn.createStatement();
            int deletedRowsCount = stmt.executeUpdate(query);
            logger.info("[" + deletedRowsCount + "] old brands got deleted.");
        } catch (Exception ex) {
            logger.error("Error druing saving new MDRs: " + ex.getMessage());
            logger.error(ex.getMessage(), ex);
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (Exception e) {
            }
        }
    }

    public void deleteOldModels() {
        Connection conn = null;
        Statement stmt = null;
        String query = "delete from dev_model_name as model where not exists(select mdr.model_name "
                + "from mobile_device_repository as mdr where mdr.model_name=model.model_name)";

        try {
            conn = this.getConnection();
            stmt = conn.createStatement();
            int deletedRowsCount = stmt.executeUpdate(query);
            logger.info("[" + deletedRowsCount + "] old models got deleted.");
        } catch (Exception ex) {
            logger.error("Error druing saving new MDRs: " + ex.getMessage());
            logger.error(ex.getMessage(), ex);
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (Exception e) {
            }
        }
    }


    public void raiseAnAlert(String alertCode, String alertMessage, String alertProcess, int userId) {
        alertService.raiseAnAlert(null, alertCode, alertMessage, alertProcess, userId);
		/*Process p = null;
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
        }*/
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
