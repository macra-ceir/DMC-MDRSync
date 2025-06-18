package com.gl.MDRProcess.repo.audit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.gl.MDRProcess.model.audit.ModulesAuditTrail;

@Repository
public interface ModulesAuditTrailRepository extends 
JpaRepository<ModulesAuditTrail, Long>, JpaSpecificationExecutor<ModulesAuditTrail>{
	
	public ModulesAuditTrail save(ModulesAuditTrail mobileDeviceRepositoryAudit);
}
