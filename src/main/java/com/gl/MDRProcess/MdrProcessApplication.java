package com.gl.MDRProcess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.gl.MDRProcess.controller.MobileDeviceRepositoryController;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@SpringBootApplication(scanBasePackages= {"com.gl.MDRProcess"})
@EnableEncryptableProperties
public class MdrProcessApplication {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(MdrProcessApplication.class, args);
		MobileDeviceRepositoryController mobileDeviceRepositoryController =
				applicationContext.getBean(MobileDeviceRepositoryController.class);
		mobileDeviceRepositoryController.process();
	}

}
