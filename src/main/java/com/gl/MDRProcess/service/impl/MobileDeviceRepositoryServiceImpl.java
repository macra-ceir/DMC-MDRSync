package com.gl.MDRProcess.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.gl.MDRProcess.configuration.PropertiesReader;
import com.gl.MDRProcess.model.app.AlertMessages;
import com.gl.MDRProcess.model.app.DeviceBrand;
import com.gl.MDRProcess.model.app.DeviceModel;
import com.gl.MDRProcess.model.app.GSMATacDetails;
import com.gl.MDRProcess.model.app.MobileDeviceRepository;
import com.gl.MDRProcess.model.audit.ModulesAuditTrail;
import com.gl.MDRProcess.model.oam.MobileDeviceRepositoryHistory;
import com.gl.MDRProcess.model.view.BrandView;
import com.gl.MDRProcess.model.view.ModelBrandView;
import com.gl.MDRProcess.repo.app.AlertRepository;
//import com.gl.MDRProcess.repo.app.GSMATacDetailsRepository;
import com.gl.MDRProcess.repo.app.MobileDeviceRepoRepository;
import com.gl.MDRProcess.repo.audit.ModulesAuditTrailRepository;
import com.gl.MDRProcess.repo.oam.MobileDeviceRepoHistoryRepository;
import com.gl.MDRProcess.util.Utility;
import com.gl.MDRProcess.model.app.SystemConfigListDb;
import com.gl.MDRProcess.repo.app.SystemConfigListRepository;

@Service
public class MobileDeviceRepositoryServiceImpl {
	
	private static final Logger logger = LogManager.getLogger(MobileDeviceRepositoryServiceImpl.class);
	
	@Autowired
	MobileDeviceRepoRepository mdrRepository;
	
	@Autowired
	MobileDeviceRepoHistoryRepository mdrHistoryRepository;
	
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
	AlertService alertService;
	
	@Autowired
	@PersistenceContext//(unitName = "appEntityManagerFactory")
	private EntityManager deviceEntityManager;
	
//	@Autowired
////    @Qualifier("oamEntityManagerFactory")
//	@PersistenceContext(unitName = "oamEntityManagerFactory")
//    private EntityManager deviceHisEntityManager;
	
	@Transactional
	public void insertMDR() {
		int insertionCount = 0;
		Pageable pageable  = null;
		int batchSize   = propertiesReader.batchSize;
		Integer userId = propertiesReader.userId;
		long startTime = 0;
		long totalTime = 0;
		Integer userType = propertiesReader.userType;
		String moduleName = propertiesReader.moduleName;
		String feature    = propertiesReader.feature;
		String serverName = null;
		AlertMessages alertMessages = null;
		List<GSMATacDetails> tacDetails = null;
		List<SystemConfigListDb> testIMEIList = null;
		List<MobileDeviceRepository> mobileDeviceRepositories = null;
		List<MobileDeviceRepositoryHistory> mobileDeviceRepositoryHistories = null;
		ModulesAuditTrail modulesAuditTrail = new ModulesAuditTrail((int)0, 201, 
				"Initial", moduleName, feature, "Sync", 0, serverName);
		try {
			logger.info("Mobile Device Repository update process is started.");
			serverName = InetAddress.getLocalHost().getHostName();
			pageable = PageRequest.of(0, batchSize);
			startTime = System.currentTimeMillis();
//			modulesAuditTrail = new ModulesAuditTrail((int)0, 201, 
//					"Initial", moduleName, feature, "Sync", 0, serverName);
			modulesAuditTrail = modulesAuditTrailRepository.save(modulesAuditTrail);
			testIMEIList = systemConfigListRepository.findByTag("TEST_IMEI_SERIES");
			if (Objects.isNull(testIMEIList) || testIMEIList.isEmpty()) {
				alertMessages = alertRepository.findByAlertIdIn("alert1500");
				logger.error("Going to Raise alert alert1500 ,description of alert is="+alertMessages.getDescription());
//				raiseAnAlert("alert1500",alertMessages.getDescription(), "Sync", 0);
				logger.error("Alert alert1500 is raised");
				modulesAuditTrail.setStatusCode(501);
				modulesAuditTrail.setStatus("Failed");
				modulesAuditTrail.setInfo(alertMessages.getDescription());
				modulesAuditTrailRepository.save(modulesAuditTrail);
				System.exit(0);
			}
			logger.info("Going to get new records for GSMA table.");
			long logTime = System.currentTimeMillis();
//			tacDetails = gsmaTacDetailsRepository.findAll(pageable).getContent();
			logger.info("Records reading done, total time:{"+String.valueOf(System.currentTimeMillis()-logTime)+"}");
			while(tacDetails.size() > 0) {
				logger.info("Record processing started.");
				logTime = System.currentTimeMillis();
				mobileDeviceRepositories = processDeviceData(tacDetails, userId, testIMEIList);
				logger.info("Record processing ended. Total time:{"+String.valueOf(System.currentTimeMillis()-logTime)+"}");logger.info("History processing started");
				logTime = System.currentTimeMillis();
				mobileDeviceRepositoryHistories = new ArrayList<MobileDeviceRepositoryHistory>();
				for(MobileDeviceRepository mdr: mobileDeviceRepositories) {
					deviceEntityManager.persist(mdr);
					mobileDeviceRepositoryHistories.add(new MobileDeviceRepositoryHistory(mdr));
//					deviceHisEntityManager.persist(new MobileDeviceRepositoryHistory(mdr));
				}
				deviceEntityManager.flush();
//				deviceEntityManager.getTransaction().commit();
				deviceEntityManager.clear();
//				deviceHisEntityManager.flush();
//				deviceHisEntityManager.clear();
//				mobileDeviceRepositoryHistories = new ArrayList<MobileDeviceRepositoryHistory>();
//				for(MobileDeviceRepository mdr: mobileDeviceRepositories) {
////					mobileDeviceRepositoryHistories.add(new MobileDeviceRepositoryHistory(mdr));
//					deviceHisEntityManager.persist(new MobileDeviceRepositoryHistory(mdr));
//				}
//				deviceHisEntityManager.flush();
//				deviceHisEntityManager.clear();
				mdrHistoryRepository.saveAll(mobileDeviceRepositoryHistories);
				logger.info("History saved in the database, total time:{"+String.valueOf(System.currentTimeMillis()-logTime)+"}");
				insertionCount += tacDetails.size();
				logger.info("Going to get new records for GSMA table.");
				logTime = System.currentTimeMillis();
//				tacDetails = gsmaTacDetailsRepository.findAll(pageable).getContent();
				logger.info("Records reading done, total time:{"+String.valueOf(System.currentTimeMillis()-logTime)+"}");
				break;//Need to comment after test
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
		} finally {
			deviceEntityManager.clear();
//			deviceEntityManager.close();
//			deviceHisEntityManager.clear();
//			deviceHisEntityManager.close();
		}
		try {
			modelService.deleteOldModels();
			brandService.deleteOldBrands();
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
	
	private List<MobileDeviceRepository> processDeviceData(List<GSMATacDetails> tacDetails, Integer userId, 
			List<SystemConfigListDb> testIMEIList){
		List<MobileDeviceRepository> mobileDeviceRepositories = new ArrayList<MobileDeviceRepository>();
		MobileDeviceRepository mobileDeviceRepository = null;
		String brandName = null;
		String modelName = null;
		for(GSMATacDetails tacDetail: tacDetails) {
			brandName = tacDetail.getBrandName().trim();
			if(brandName == "" || brandName == "null")
				brandName = "Not Known";
			modelName = tacDetail.getModelName().trim();
			if(modelName == "" || modelName == "null")
				modelName = "Not Known";
			mobileDeviceRepository = new MobileDeviceRepository();
			
			mobileDeviceRepository.setDeviceId(tacDetail.getTac());
			
			for(SystemConfigListDb series: testIMEIList) {
            	if(tacDetail.getTac().startsWith(series.getInterpretation())) {
            		mobileDeviceRepository.setIsTestImei(1);
            		break;
            	}
            }
			
			if(Objects.nonNull(tacDetail.getAllocationDate()) && !tacDetail.getAllocationDate().equals("null"))
				mobileDeviceRepository.setAllocationDate(tacDetail.getAllocationDate());
			
			if(Objects.nonNull(tacDetail.getNetworkTechnology()) && !tacDetail.getNetworkTechnology().equals("null"))
				mobileDeviceRepository.setBandDetail(tacDetail.getNetworkTechnology());
			
			if(Objects.nonNull(brandName) && !brandName.equals("null"))
				mobileDeviceRepository.setBrandName(brandName);
			
			if(Objects.nonNull(tacDetail.getBluetooth()) && !tacDetail.getBluetooth().equals("null"))
				mobileDeviceRepository.setCommsBluetooth(tacDetail.getBluetooth());
			
			if(tacDetail.getNfc().trim().equalsIgnoreCase("yes"))
				mobileDeviceRepository.setCommsNFC(1);
			else
				mobileDeviceRepository.setCommsNFC(0);
			
			if(Objects.nonNull(tacDetail.getWlan()) && !tacDetail.getWlan().equals("null"))
				mobileDeviceRepository.setCommsWLAN(tacDetail.getWlan());
			
			if(Objects.nonNull(tacDetail.getDeviceType()) && !tacDetail.getDeviceType().equals("null"))
				mobileDeviceRepository.setDeviceType(tacDetail.getDeviceType());
			
			if(Objects.nonNull(tacDetail.getImeiQuantity()))
				mobileDeviceRepository.setImeiQuantity(tacDetail.getImeiQuantity());
			
			if(Objects.nonNull(tacDetail.getManufacturer()) && !tacDetail.getManufacturer().equals("null"))
				mobileDeviceRepository.setManufacturer(tacDetail.getManufacturer());
			
			if(Objects.nonNull(tacDetail.getMarketingName()) && !tacDetail.getMarketingName().equals("null"))
				mobileDeviceRepository.setMarketingName(tacDetail.getMarketingName());
			
			if(Objects.nonNull(modelName) && !modelName.equals("null"))
				mobileDeviceRepository.setModelName(modelName);
			
			if(tacDetail.getNetworkTechnology().trim().toLowerCase().contains("2g")) {
				mobileDeviceRepository.setNetworkTechnologyGSM(1);
//				mobileDeviceRepository.setNetwork2GBands(1);
			}else {
				mobileDeviceRepository.setNetworkTechnologyGSM(0);
			}
			
			if(tacDetail.getNetworkTechnology().trim().toLowerCase().contains("3g")) {
				mobileDeviceRepository.setNetworkTechnologyEVDO(1);
//				mobileDeviceRepository.setNetwork2GBands(1);
			}else {
				mobileDeviceRepository.setNetworkTechnologyEVDO(0);
			}
			
			if(tacDetail.getNetworkTechnology().trim().toLowerCase().contains("4g") 
					|| tacDetail.getNetworkTechnology().trim().toLowerCase().contains("lte")) {
				mobileDeviceRepository.setNetworkTechnologyLTE(1);
			}else {
				mobileDeviceRepository.setNetworkTechnologyLTE(0);
			}
			
			if(tacDetail.getNetworkTechnology().trim().toLowerCase().contains("5g")) {
				mobileDeviceRepository.setNetworkTechnology5G(1);
			}else {
				mobileDeviceRepository.setNetworkTechnology5G(0);
			}
			
			if(Objects.nonNull(tacDetail.getOem()) && !tacDetail.getOem().equals("null"))
				mobileDeviceRepository.setOem(tacDetail.getOem());
			
			if(Objects.nonNull(tacDetail.getOperatingSystem()) && !tacDetail.getOperatingSystem().equals("null"))
				mobileDeviceRepository.setOs(tacDetail.getOperatingSystem());
			
			if(Objects.nonNull(tacDetail.getRemovableUICC()))
				mobileDeviceRepository.setRemovableUICC(tacDetail.getRemovableUICC());
			
			if(Objects.nonNull(tacDetail.getRemovableEUICC()))
				mobileDeviceRepository.setRemovableEUICC(tacDetail.getRemovableEUICC());
			
			if(Objects.nonNull(tacDetail.getNonRemovableUICC()))
				mobileDeviceRepository.setNonremovableUICC(tacDetail.getNonRemovableUICC());
			
			if(Objects.nonNull(tacDetail.getNonRemovableEUICC()))
				mobileDeviceRepository.setNonremovableEUICC(tacDetail.getNonRemovableEUICC());
			
			if(Objects.nonNull(tacDetail.getSimSlot()))
				mobileDeviceRepository.setSimSlot(tacDetail.getSimSlot());
			mobileDeviceRepository.setDeviceState(0);
			mobileDeviceRepository.setUserId(userId);
			
			mobileDeviceRepositories.add(mobileDeviceRepository);
		}
		return mobileDeviceRepositories;
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void saveAllNewBrandAndModel() {
		Map<String, Integer> brandsMap = null;
		List<ModelBrandView> newBrandsModels = null;
		List<BrandView> existingBrands = null;
		List<DeviceBrand> brandsToSave = new ArrayList<DeviceBrand>();
		List<DeviceModel> modelsToSave = new ArrayList<DeviceModel>();
		List<String> newBrands = null;
		try {
			logger.info("Starting to update new brands and models in brand and model tables.");
			logger.info("Started reading all new brand and models.");
			newBrandsModels = mdrRepository.getNewBrandAndModels();
			logger.info("Completed reading all new brand and models.");
			logger.info("Started reading all new brands.");
			newBrands = mdrRepository.getNewBrands();
			logger.info("Completed reading all new brands.");
			for(String brand: newBrands) {
//				brandsToSave.add(new DeviceBrand(brand));
				deviceEntityManager.persist(new DeviceBrand(brand));
			}
			deviceEntityManager.flush();
//			deviceEntityManager.getTransaction().commit();
			deviceEntityManager.clear();
//			brandService.saveBrands(brandsToSave);
			logger.info("["+brandsToSave.size()+"] new brands saved.");
			existingBrands  = brandService.getAllBrands();
			brandsMap = existingBrands.parallelStream()
	                .collect(Collectors.toMap(BrandView::getBrandName, BrandView::getBrandId, (existingValue, newValue) -> newValue));
			for(ModelBrandView view: newBrandsModels) {
				if( !view.getModelName().equals("") )
//					modelsToSave.add(new DeviceModel(view.getBrandName(), brandsMap.get(view.getBrandName()),
//							view.getModelName()));
				deviceEntityManager.persist(new DeviceModel(view.getBrandName(), brandsMap.get(view.getBrandName()),
							view.getModelName()));
			}
			deviceEntityManager.flush();
//			deviceEntityManager.getTransaction().commit();
			deviceEntityManager.clear();
//			modelService.saveModelInBulk(modelsToSave);
			logger.info("["+modelsToSave.size()+"] new models saved.");
			logger.info("New brand and model update ends.");
		}catch(Exception ex) {
			logger.error("New model and brand update failed="+ex.getMessage());
			logger.error(ex.getMessage(), ex);
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
}
