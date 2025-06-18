package com.gl.MDRProcess.service.impl;


import com.gl.MDRProcess.configuration.PropertiesReader;
import com.gl.MDRProcess.model.app.AlertDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;



@Service
public class AlertService{
    private final Logger logger = LogManager.getLogger(this.getClass());
    @Autowired
    PropertiesReader appConfig;

    private RestTemplate restTemplate = null;
    public void raiseAnAlert(final String txnId, final String alertId, final String alertMessage, final String alertProcess,
                             final int userId) {

            AlertDto alertDto = new AlertDto();
            alertDto.setAlertId(alertId);
            alertDto.setUserId(String.valueOf(userId));
            alertDto.setAlertMessage(alertMessage);
            alertDto.setAlertProcess(alertProcess);
            //alertDto.setServerName(appConfig.getSourceServerName());
            alertDto.setFeatureName("MDR Sync");
            alertDto.setTxnId(txnId);

            long start = System.currentTimeMillis();
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<AlertDto> request = new HttpEntity<AlertDto>(alertDto, headers);

                SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
                clientHttpRequestFactory.setConnectTimeout(1000);
                clientHttpRequestFactory.setReadTimeout(1000);
                restTemplate = new RestTemplate(clientHttpRequestFactory);
                logger.info(String.valueOf(request.getBody()));
                logger.info("appConfig.alertUrl {}", appConfig.alertUrl);

                ResponseEntity<String> responseEntity = restTemplate.postForEntity(appConfig.alertUrl, request, String.class);
                logger.info("Alert Sent Request:{}, TimeTaken:{} Response:{}", alertDto, responseEntity, (System.currentTimeMillis() - start));
            } catch (org.springframework.web.client.ResourceAccessException resourceAccessException) {
                logger.error("Error while Sending Alert resourceAccessException:{} Request:{}", resourceAccessException.getMessage(), alertDto, resourceAccessException);
            } catch (Exception e) {
                logger.error("Error while Sending Alert Error:{} Request:{}", e.getMessage(), alertDto, e);
            }

    }
}
