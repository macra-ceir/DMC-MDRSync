package com.gl.MDRProcess.repo.oam;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.gl.MDRProcess.model.oam.MobileDeviceRepositoryHistory;

@Repository
public interface MobileDeviceRepoHistoryRepository extends 
JpaRepository<MobileDeviceRepositoryHistory, Long>, JpaSpecificationExecutor<MobileDeviceRepositoryHistory>{
	
	public MobileDeviceRepositoryHistory save(MobileDeviceRepositoryHistory mobileDeviceRepositoryHistory);
	public List<MobileDeviceRepositoryHistory> getByDeviceId(Long deviceId);
//	public List<MobileDeviceRepositoryHistory> saveAll(List<MobileDeviceRepositoryHistory> histories);
}
