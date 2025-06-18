package com.gl.MDRProcess;

import com.gl.MDRProcess.controller.MobileDeviceRepositoryController;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@SpringBootApplication(scanBasePackages = {"com.gl.MDRProcess"})
@EnableEncryptableProperties
public class MdrProcessApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(MdrProcessApplication.class, args);
        MobileDeviceRepositoryController mobileDeviceRepositoryController =
                applicationContext.getBean(MobileDeviceRepositoryController.class);
        LocalDate date = LocalDate.parse(args[0], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        mobileDeviceRepositoryController.process(date);
    }

}
