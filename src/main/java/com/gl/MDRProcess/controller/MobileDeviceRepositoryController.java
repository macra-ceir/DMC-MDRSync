package com.gl.MDRProcess.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gl.MDRProcess.service.impl.MDRServiceImpl;
import com.gl.MDRProcess.service.impl.MobileDeviceRepositoryServiceImpl;

@Component
public class MobileDeviceRepositoryController {
	private static final Logger logger = LogManager.getLogger(MobileDeviceRepositoryController.class);
	
//	@Autowired
//	MobileDeviceRepositoryServiceImpl service;
	
	@Autowired
	MDRServiceImpl service;
	
	public void process() {
		logger.info("Mobile Device Repository Table update started.");
		service.insertMDR();
		logger.info("Mobile Device Repository Table update ended.");
	}
}
