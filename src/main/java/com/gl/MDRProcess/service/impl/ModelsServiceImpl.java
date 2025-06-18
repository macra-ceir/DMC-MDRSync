package com.gl.MDRProcess.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gl.MDRProcess.model.app.DeviceBrand;
import com.gl.MDRProcess.model.app.DeviceModel;
import com.gl.MDRProcess.repo.app.DeviceModelRepository;

@Service
public class ModelsServiceImpl {
	
	private static final Logger logger = LogManager.getLogger(ModelsServiceImpl.class);
	
	@Autowired
	BrandServiceImpl brandService;
	
	@Autowired
	DeviceModelRepository modelRepository;
	
	@PersistenceContext
	private EntityManager modelEntityManager;
	
	
	public void updateNewModels(Set<String> brandNames, HashMap<String, String> modelMap) {
		HashMap<String, Integer> brandNameIdMap = null;
		List<DeviceModel> modelList = new ArrayList<DeviceModel>();
		try {
			brandNameIdMap = brandService.getBrandNameIdMap(brandNames);
			for(String modelName: modelMap.keySet()) {
				if(Objects.isNull(modelRepository.findByBrandNameIdAndModelName(brandNameIdMap.get(modelMap.get(modelName)),
						modelName)))
					modelList.add(new DeviceModel(modelMap.get(modelName),
							brandNameIdMap.get(modelMap.get(modelName)), modelName));
			}
			if(modelList.size() > 0)
				modelRepository.saveAll(modelList);
		}catch(Exception ex) {
			logger.error("Error during updating new models in the model table.");
			logger.error(ex.getMessage(), ex);
		}
	}
	
	public void deleteOldModels() {
		List<DeviceModel> oldModels = null;
		try {
			oldModels = modelRepository.getOldModels();
			logger.info("["+oldModels.size()+"] old models going to be deleted.");
			if(oldModels.size() > 0) {
				logger.info("Find models no longer used and going to delete it.");
				modelRepository.deleteAll(oldModels);
			}
				
		}catch(Exception ex) {
			logger.error("Error during deleting old models from the model table.");
			logger.error(ex.getMessage(), ex);
		}
	}
	
	public DeviceModel getModelDetails(String brandName, String modelName) {
		return modelRepository.findByBrandNameAndModelName(brandName, modelName);
	}
	
	public DeviceModel saveModel(String brandName, Integer brandNameId, String modelName) {
		return modelRepository.save(new DeviceModel(brandName, brandNameId, modelName));
	}
	
	@Transactional
	public void saveModelInBulk(List<DeviceModel> deviceModels) {
//		modelRepository.saveAll(deviceModels);
		String sql = "insert into dev_model_name(brand_name, brand_name_id, model_name, created_on) values(?, ?, ?, CURRENT_TIMESTAMP)";
		try {
		    for( DeviceModel deviceModel: deviceModels) {
		    	modelEntityManager.persist(deviceModel);
		    }
	    	modelEntityManager.flush();
	    	modelEntityManager.clear();
		} catch (Exception ex) {
		    logger.error(ex.getMessage(), ex);
		} finally {
			modelEntityManager.clear();
//			modelEntityManager.close();
		}
	}

}
