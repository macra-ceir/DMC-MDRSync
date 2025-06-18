package com.gl.MDRProcess.repo.app;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gl.MDRProcess.model.app.DeviceModel;
import com.gl.MDRProcess.model.app.MobileDeviceRepository;

@Repository
public interface DeviceModelRepository extends JpaRepository<DeviceModel, Integer>{
	
	public DeviceModel save(DeviceModel deviceModel);
	
	public DeviceModel findByBrandNameIdAndModelName(Integer brandNameId, String modelName);
	
	public DeviceModel findByBrandNameAndModelName(String brandName, String modelName);
	
	@Query(value="select model from DeviceModel as model where not exists(select mdr.modelName"
			+ " from MobileDeviceRepository as mdr where mdr.modelName=model.modelName)")
	public List<DeviceModel> getOldModels();

}
