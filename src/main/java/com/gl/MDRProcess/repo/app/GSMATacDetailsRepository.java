//package com.gl.MDRProcess.repo.app;
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import com.gl.MDRProcess.model.app.GSMATacDetails;
//import com.gl.MDRProcess.model.app.MobileDeviceRepository;
//
//@Repository
//public interface GSMATacDetailsRepository extends JpaRepository<GSMATacDetails, Long>, JpaSpecificationExecutor<GSMATacDetails>{
//	
//	@Query(value="select g from GSMATacDetails g left join MobileDeviceRepository m on g.tac = m.deviceId where "
//			+ "m.deviceId IS NULL")
//	public Page<GSMATacDetails> findAll(Pageable pageable);
//}
