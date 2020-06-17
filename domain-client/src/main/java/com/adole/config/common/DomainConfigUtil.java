package com.adole.config.common;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.util.CollectionUtils;

import java.sql.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @Author: adole
 * @Date: 2020/1/1 16:43
 */
public class DomainConfigUtil {

    public Integer getAppId(String app, Map<String, String> connectParams) {
        Integer appId = Integer.valueOf(0);
        try {
            if (((String) connectParams.get("type")).equals(Constant.START_TYPE_DB.toString())) {
                if (isInteger(app)) {
                    appId = Integer.valueOf(Integer.parseInt(app));
                } else {
                    List list = query(connectParams, "SELECT app_id FROM t_app WHERE app_name='%s'", new Object[]{app});
                    if (CollectionUtils.isEmpty(list)) {
                        throw new RuntimeException("There is no app named '" + app + "' in table t_app");
                    }
                    appId = Integer.valueOf(Integer.parseInt(((Map) list.get(0)).get("app_id").toString()));
                }
            } else {
                getRestParams(connectParams, "", new Object[0]);
                appId = Integer.valueOf(0);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Query app(" + app + ") parameter fail, message:" + ex.getMessage());
        }
        return appId;
    }

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    public List<Map<String, Object>> getEnvProperties(Map<String, String> connectParams) {
        List envParasList;
        if (((String) connectParams.get("type")).equals(Constant.START_TYPE_DB.toString()))
            envParasList = query(connectParams, "SELECT para_name,para_value,is_encrypt FROM t_env_param WHERE status = 1", new Object[0]);
        else {
            envParasList = getRestParams(connectParams, "", new Object[0]);
        }
        return envParasList;
    }

    public List<Map<String, Object>> getModuleParams(Map<String, String> connectParams, Integer appId, String moduleCode) {
        return query(connectParams, "SELECT module_code,para_name,para_value,is_encrypt FROM t_module_param WHERE app_id=%s AND module_code='%s' AND status = 1 AND (broker_code is null OR broker_code ='')", new Object[]{appId, moduleCode});
    }

    public List<Map<String, Object>> getBrokerParams(Map<String, String> connectParams, Integer appId, String moduleCode, String brokerCode) {
        return query(connectParams, "SELECT module_code,para_name,para_value,is_encrypt as count FROM t_module_param WHERE app_id=%s AND module_code='%s' AND broker_code='%s' AND status = 1", new Object[]{appId, moduleCode, brokerCode});
    }

    public List<Map<String, Object>> getRestParams(Map<String, String> connectParams, String restUrl, Object[] params) {
        RestTemplateBuilder builder = new RestTemplateBuilder(new RestTemplateCustomizer[0]);
//        RestTemplate restTemplate = builder.build();
        return null;
    }

    public Connection getConnection(Map<String, String> connectParams)
            throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");

        String url = (String) connectParams.get("url");
        String username = (String) connectParams.get("username");
        String password = (String) connectParams.get("password");
        Connection conn = DriverManager.getConnection(url, username, password);
        return conn;
    }

    public List<Map<String, Object>> query(Map<String, String> connectParams, String sql, Object[] params) {
        List resultList = null;
        Connection conn = null;
        try {
            conn = getConnection(connectParams);
            Statement statement = conn.createStatement();

            ResultSet result = statement.executeQuery(String.format(sql, params));
            resultList = resultSetToList(result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    System.out.println("Ignore close errors");
                }
            }
        }
        return resultList;
    }

    public static List<Map<String, Object>> resultSetToList(ResultSet rs)
            throws SQLException {
        if (rs == null) {
            return Collections.EMPTY_LIST;
        }
        ResultSetMetaData columns = rs.getMetaData();

        int columnCount = columns.getColumnCount();
        List list = new ArrayList();
        Map rowData = new HashMap();
        while (rs.next()) {
            rowData = new HashMap(columnCount);
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(columns.getColumnName(i), rs.getObject(i));
            }
            list.add(rowData);
        }
        return list;
    }
}
