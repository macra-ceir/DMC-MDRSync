package com.gl.MDRProcess.constant;

public interface AuditQueriesConstant {
    String PARAM_START_RANGE = "<START_RANGE>";
    String PARAM_CREATED_ON = "<CREATED_ON>";

    String PARAM_END_RANGE = "<END_RANGE>";
    String PARAM_HOSTNAME = "<HOSTNAME>";
    String MODULE_NAME = "<MODULE_NAME>";

    String FEATURE_NAME = "<FEATURE_NAME>";

    String PARAM_STATUS_CODE = "<STATUS_CODE>";
    String PARAM_TIME_TAKEN = "<TIME_TAKEN>";

    String PARAM_SUCCESS_COUNT = "<SUCCESS_COUNT>";
    String INSERT_MODULE_AUDIT_TRAIL = "insert into aud.modules_audit_trail (status_code, status, error_message, module_name, feature_name, action, count,execution_time,info,server_name,created_on) values (201, 'Initial', 'NA', '" + MODULE_NAME + "', '" + FEATURE_NAME + "', 'update', 0,0,'starting " + FEATURE_NAME + "','" + PARAM_HOSTNAME + "','" + PARAM_CREATED_ON + "')";
    String UPDATE_MODULE_AUDIT_TRAIL_MYSQL = "update aud.modules_audit_trail set status_code=" + PARAM_STATUS_CODE + ", status='success', count='" + PARAM_SUCCESS_COUNT + "',  execution_time='" + PARAM_TIME_TAKEN + "' where  id = (select x.id from (select max(b.id) as id from aud.modules_audit_trail as b where b.status_code=201 and b.feature_name='" + FEATURE_NAME + "') as x)";
    String UPDATE_MODULE_AUDIT_TRAIL_ORACLE = "update aud.modules_audit_trail set status_code=" + PARAM_STATUS_CODE + ", status='success', count='" + PARAM_SUCCESS_COUNT + "',  execution_time='" + PARAM_TIME_TAKEN + "' where  id = (select  max(id) from aud.modules_audit_trail where status_code=201 and feature_name='" + FEATURE_NAME + "')";

    String SELECT_MODULE_AUDIT_TRAIL_MYSQL = "select  created_on, feature_name ,status_code from aud.modules_audit_trail where feature_name='" + FEATURE_NAME + "' and CREATED_ON >='" + PARAM_START_RANGE + "' and CREATED_ON <'" + PARAM_END_RANGE + "'";
    String SELECT_MODULE_AUDIT_TRAIL_ORACLE = "select  created_on, feature_name ,status_code from aud.modules_audit_trail where feature_name='" + FEATURE_NAME + "' and CREATED_ON >= to_date( '" + PARAM_START_RANGE + "', 'YYYY-MM-DD HH24:MI:SS' ) and CREATED_ON < to_date( '" + PARAM_END_RANGE + "', 'YYYY-MM-DD HH24:MI:SS' )";

}
