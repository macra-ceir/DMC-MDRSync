package com.gl.MDRProcess.controller;

import com.gl.MDRProcess.service.impl.MDRServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class MobileDeviceRepositoryController {
    private static final Logger logger = LogManager.getLogger(MobileDeviceRepositoryController.class);

    @Autowired
    MDRServiceImpl service;

    public void process(LocalDate localDate) {
        logger.info("Mobile Device Repository Table update started.");
        service.insertMDR(localDate);
        logger.info("Mobile Device Repository Table update ended.");
    }
}
