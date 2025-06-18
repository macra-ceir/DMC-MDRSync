package com.gl.MDRProcess.service.impl;


import com.gl.MDRProcess.configuration.PropertiesReader;
import com.gl.MDRProcess.constant.AuditQueriesConstant;
import com.gl.MDRProcess.constant.DBType;
import com.gl.MDRProcess.model.app.ModuleAuditTrail;
import com.gl.MDRProcess.util.DateFormatterConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ModuleAuditTrailService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    QueryExecutorService queryExecutorService;

    @Autowired
    PropertiesReader appConfig;



    public Boolean runProcess(LocalDate localDate, String moduleName) {
        LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.of(0, 0, 0));
        Boolean startProcess = true;
        String query = appConfig.getDbType() == DBType.MYSQL ? AuditQueriesConstant.SELECT_MODULE_AUDIT_TRAIL_MYSQL : AuditQueriesConstant.SELECT_MODULE_AUDIT_TRAIL_ORACLE;
        query = query.replaceAll(AuditQueriesConstant.FEATURE_NAME, moduleName);
        query = query.replaceAll(AuditQueriesConstant.PARAM_START_RANGE, localDateTime.format(DateFormatterConstants.simpleDateFormat));
        query = query.replaceAll(AuditQueriesConstant.PARAM_END_RANGE, localDateTime.plusDays(1).format(DateFormatterConstants.simpleDateFormat));
        log.info("Running Query:{} ", query);
        try {
            List<ModuleAuditTrail> trails = jdbcTemplate.query(query, new RowMapper<ModuleAuditTrail>() {
                @Override
                public ModuleAuditTrail mapRow(ResultSet rs, int rowNum) throws SQLException {
                    ModuleAuditTrail moduleAuditTrail = new ModuleAuditTrail();
                    moduleAuditTrail.setCreatedOn(rs.getTimestamp("created_on").toLocalDateTime());
                    moduleAuditTrail.setStatusCode(rs.getInt("status_code"));
                    moduleAuditTrail.setFeatureName(rs.getString("feature_name"));
                    log.info("ModuleAudiTrail for today's {}", moduleAuditTrail);
                    return moduleAuditTrail;
                }
            });
            long count = trails.stream().filter(trail -> (trail.getStatusCode().intValue() == 200)).count();
            startProcess = (count == 0);
        } catch (Exception e) {
            log.error("Error:{} while running query:[{}]", e.getMessage(), query);
        }

        return startProcess;
    }

    public Boolean previousDependentModuleExecuted(LocalDate localDate, String moduleName) {
        LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.of(0, 0, 0));
        Boolean startProcess = false;
        String query = appConfig.getDbType() == DBType.MYSQL ? AuditQueriesConstant.SELECT_MODULE_AUDIT_TRAIL_MYSQL : AuditQueriesConstant.SELECT_MODULE_AUDIT_TRAIL_ORACLE;
        query = query.replaceAll(AuditQueriesConstant.FEATURE_NAME, moduleName);
        query = query.replaceAll(AuditQueriesConstant.PARAM_START_RANGE, localDateTime.format(DateFormatterConstants.simpleDateFormat));
        query = query.replaceAll(AuditQueriesConstant.PARAM_END_RANGE, localDateTime.plusDays(1).format(DateFormatterConstants.simpleDateFormat));
        log.info("Running Query:{} ", query);
        try {
            List<ModuleAuditTrail> trails = jdbcTemplate.query(query, new RowMapper<ModuleAuditTrail>() {
                @Override
                public ModuleAuditTrail mapRow(ResultSet rs, int rowNum) throws SQLException {
                    ModuleAuditTrail moduleAuditTrail = new ModuleAuditTrail();
                    moduleAuditTrail.setCreatedOn(rs.getTimestamp("created_on").toLocalDateTime());
                    moduleAuditTrail.setStatusCode(rs.getInt("status_code"));
                    moduleAuditTrail.setFeatureName(rs.getString("feature_name"));
                    log.info("ModuleAudiTrail for today's {}", moduleAuditTrail);
                    return moduleAuditTrail;
                }
            });
            long count = trails.stream().filter(trail -> (trail.getStatusCode().intValue() == 200)).count();
            startProcess = (count > 0);
        } catch (Exception e) {
            log.error("Error:{} while running query:[{}]", e.getMessage(), query);
        }

        return startProcess;
    }

    public void createAudit(ModuleAuditTrail moduleAuditTrail) {
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            queryExecutorService.execute(AuditQueriesConstant.INSERT_MODULE_AUDIT_TRAIL.replaceAll(AuditQueriesConstant.PARAM_HOSTNAME, localhost.getHostName()).replaceAll(AuditQueriesConstant.MODULE_NAME, moduleAuditTrail.getModuleName())
                    .replaceAll(AuditQueriesConstant.FEATURE_NAME, moduleAuditTrail.getFeatureName())
                    .replaceAll(AuditQueriesConstant.PARAM_CREATED_ON, moduleAuditTrail.getCreatedOn().format(DateFormatterConstants.simpleDateFormat)));
        } catch (UnknownHostException ex) {
            log.info("Error to get Hostname moduleAuditTrail:{} Error:{}", moduleAuditTrail, ex.getMessage());
        }
    }

    public void updateAudit(ModuleAuditTrail moduleAuditTrail) {
        String updateAuditQuery = appConfig.getDbType() == DBType.MYSQL ? AuditQueriesConstant.UPDATE_MODULE_AUDIT_TRAIL_MYSQL : AuditQueriesConstant.UPDATE_MODULE_AUDIT_TRAIL_ORACLE;
        updateAuditQuery = updateAuditQuery.replaceAll(AuditQueriesConstant.PARAM_TIME_TAKEN, String.valueOf(moduleAuditTrail.getTimeTaken()))
                .replaceAll(AuditQueriesConstant.FEATURE_NAME, moduleAuditTrail.getFeatureName())
                .replaceAll(AuditQueriesConstant.PARAM_SUCCESS_COUNT, String.valueOf(moduleAuditTrail.getCount()))
                .replaceAll(AuditQueriesConstant.PARAM_STATUS_CODE, String.valueOf(moduleAuditTrail.getStatusCode()));
        queryExecutorService.execute(updateAuditQuery);
    }
}
