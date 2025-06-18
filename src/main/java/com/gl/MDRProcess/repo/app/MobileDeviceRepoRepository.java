package com.gl.MDRProcess.repo.app;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gl.MDRProcess.model.app.DeviceModel;
import com.gl.MDRProcess.model.app.DeviceBrand;
import com.gl.MDRProcess.model.view.ModelBrandView;
import com.gl.MDRProcess.model.view.BrandView;
import com.gl.MDRProcess.model.app.MobileDeviceRepository;

@Repository
public interface MobileDeviceRepoRepository extends 
JpaRepository<MobileDeviceRepository, Long>, JpaSpecificationExecutor<MobileDeviceRepository>{
	
	public MobileDeviceRepository save(MobileDeviceRepository mobileDeviceRepository);
	public MobileDeviceRepository getByDeviceId(Long deviceId);
	
	@Query(value="select mdr from MobileDeviceRepository as mdr left join DeviceModel as m on mdr.brandName=m.brandName and"
			+ " mdr.modelName=m.modelName where m.brandName IS NULL and m.modelName IS NULL")
	public List<MobileDeviceRepository> getNewBrandAndModels(Pageable pageable);
	
	@Query(value="SELECT DISTINCT new com.gl.MDRProcess.model.view.ModelBrandView(mdr.modelName, mdr.brandName) FROM "
			+ "MobileDeviceRepository AS mdr LEFT JOIN DeviceModel AS m ON mdr.brandName = m.brandName AND"
			+ " mdr.modelName = m.modelName WHERE m.brandName IS NULL AND m.modelName IS NULL and mdr.brandName !=''")
	public List<ModelBrandView> getNewBrandAndModels();
	
	@Query(value="SELECT DISTINCT mdr.brandName FROM MobileDeviceRepository AS mdr LEFT JOIN DeviceBrand AS m ON "
			+ "mdr.brandName=m.brandName WHERE m.brandName IS NULL and mdr.brandName !=''")
	public List<String> getNewBrands();
	
	@Query(value="SELECT DISTINCT new com.gl.MDRProcess.model.view.BrandView( m.brandName, m.id) FROM MobileDeviceRepository"
			+ " AS mdr JOIN DeviceBrand AS m ON mdr.brandName=m.brandName")
	public List<BrandView> getExistingBrandList();
}
