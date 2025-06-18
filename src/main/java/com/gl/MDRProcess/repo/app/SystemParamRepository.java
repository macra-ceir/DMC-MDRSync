package com.gl.MDRProcess.repo.app;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.gl.MDRProcess.model.app.SystemParamDb;


@Repository
public interface SystemParamRepository extends CrudRepository<SystemParamDb, Long>, 
JpaRepository<SystemParamDb, Long>, JpaSpecificationExecutor<SystemParamDb>{
	
	public SystemParamDb findByTag(String tag);
	
	
//	@Query("SELECT DISTINCT a.value FROM app.sys_param a where a.tag='type_approved_enable_flag'")
//	public String findTagValue();
	
}
