package com.gl.MDRProcess.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gl.MDRProcess.model.app.DeviceBrand;
import com.gl.MDRProcess.model.view.BrandView;
import com.gl.MDRProcess.repo.app.DeviceBrandRepository;

@Service
public class BrandServiceImpl {
	
	private static final Logger logger = LogManager.getLogger(BrandServiceImpl.class);
	
	@Autowired
	DeviceBrandRepository brandRepository;
	
	@PersistenceContext
	private EntityManager brandEntityManager;
	
	public List<DeviceBrand> getBrandDetailsListByBrandNames(Set<String> brandNames){
		List<DeviceBrand> brandList = brandRepository.findByBrandNameIn(brandNames);
		return brandList;
	}
	
	public HashMap<String, Integer> getBrandNameIdMap(Set<String> brandNames){
		HashMap<String, Integer> brandNameIdMap = new HashMap<String, Integer>();
		List<DeviceBrand> brandList = null;
		try {
			brandList = this.getBrandDetailsListByBrandNames(brandNames);
			for(DeviceBrand brand: brandList) {
				brandNameIdMap.put(brand.getBrandName(), brand.getId());
			}
		}catch(Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		return brandNameIdMap;
	}
	
	public void updateNewBrands(Set<String> brands) {
		List<DeviceBrand> existingBrands = null;
		List<DeviceBrand> brandList = new ArrayList<DeviceBrand>();
		try {
			existingBrands = this.getBrandDetailsListByBrandNames(brands);
			for(DeviceBrand brand: existingBrands)
				brands.remove(brand.getBrandName());
			for(String brand: brands) {
				brandList.add(new DeviceBrand(brand));
			}
			if(brandList.size() > 0)
				brandRepository.saveAll(brandList);
		}catch(Exception ex) {
			logger.error("Error during updating new brands in the brand table.");
			logger.error(ex.getMessage(), ex);
		}	
	}
	
	public void deleteOldBrands() {
		List<DeviceBrand> oldBrands = null;
		try {
			oldBrands = brandRepository.getOldBrands();
			logger.info("["+oldBrands.size()+"] old brands going to be deleted.");
			if(oldBrands.size() > 0) {
				logger.info("Find brands no longer in use and going to delete it.");
				brandRepository.deleteAll(oldBrands);
			}
				
		}catch(Exception ex) {
			logger.error("Error during deleting old brands from the brand table.");
			logger.error(ex.getMessage(), ex);
		}
	}
	
	public DeviceBrand getBrandDetails(String brandName) {
		return brandRepository.findByBrandName(brandName);
	}
	
	public DeviceBrand saveBrand(String brandName) {
		return brandRepository.save(new DeviceBrand(brandName));
	}
	
	@Transactional
	public void saveBrands(List<DeviceBrand> brands) {
		String sql = "insert into dev_brand_name(brand_name, created_on) values(?, CURRENT_TIMESTAMP)";
		try {
			for(DeviceBrand brand: brands) {
				brandEntityManager.persist(brand);
			}
			brandEntityManager.flush();
			brandEntityManager.clear();
		}catch (Exception ex) {
		    logger.error(ex.getMessage(), ex);
		} finally {
			brandEntityManager.clear();
		}
	}
	
	public List<BrandView> getAllBrands() {
		return brandRepository.getBrandList();
	}

}
