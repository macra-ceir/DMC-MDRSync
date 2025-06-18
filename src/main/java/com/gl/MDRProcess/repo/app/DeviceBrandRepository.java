package com.gl.MDRProcess.repo.app;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gl.MDRProcess.model.app.DeviceBrand;
import com.gl.MDRProcess.model.app.MobileDeviceRepository;
import com.gl.MDRProcess.model.view.BrandView;

@Repository
public interface DeviceBrandRepository extends JpaRepository<DeviceBrand, Integer>{
	
	public DeviceBrand save(DeviceBrand deviceBrand);
	
	public List<DeviceBrand> findByBrandNameIn(Set<String> brandName);
	
	public DeviceBrand findByBrandName(String brandName);
	
	@Query(value="select brand from DeviceBrand brand where not exists(select mdr.brandName"
			+ " from MobileDeviceRepository mdr where mdr.brandName=brand.brandName)")
	public List<DeviceBrand> getOldBrands();
	
	@Query(value="SELECT new com.gl.MDRProcess.model.view.BrandView(m.brandName, m.id) FROM DeviceBrand AS m")
	public List<BrandView> getBrandList();

}
