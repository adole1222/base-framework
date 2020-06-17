package com.adole.config;

import ch.qos.logback.core.util.FileUtil;
import com.adole.base.ApplicationException;
import com.adole.base.ConfigurationFileParseUtil;
import com.adole.base.EncryptUtil;
import com.adole.config.common.DomainConfigUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.*;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ReadDbPropertiesPostProcessor implements EnvironmentPostProcessor, Ordered {
    private static final String PROPERTY_SOURCE_NAME = "application.properties";
    private static final String APP_MODULE_BROKER = "APP";
    private static final String DOMAIN_CONFIG_URL = "ADOLE_DOMAIN_URL";
    private static final String DOMAIN_CONFIG_ENCRYPT = "ADOLE_DOMAIN_ENC";
    private static final String APP_NAME = "APP_NAME";
    private static final String INSTANCE_NAME = "INSTANCE_NAME";
    private static final String LOG_CONFIG = "log.config";
    PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper("${", "}", ":", true);

    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        try {
            MutablePropertySources propertySources = environment.getPropertySources();
            Map envMap = getEnvInit();
            String instanceName = (String) envMap.get(INSTANCE_NAME);
            String app = (String) envMap.get(APP_MODULE_BROKER);
            String encryption = (String) envMap.get(DOMAIN_CONFIG_ENCRYPT);
            String domainUrl = (String) envMap.get(DOMAIN_CONFIG_URL);
            Integer startType = 0;
//            Integer startType = Constant.START_TYPE_DB;
            if ((StringUtils.isEmpty(domainUrl)) || (StringUtils.isEmpty(app)) || (StringUtils.isEmpty(instanceName))) {
                app = System.getProperty(APP_NAME);
                instanceName = System.getProperty(INSTANCE_NAME);
                encryption = System.getProperty(DOMAIN_CONFIG_ENCRYPT);
                domainUrl = System.getProperty(DOMAIN_CONFIG_URL);
            }

            if (StringUtils.isEmpty(app)) {
                throw new RuntimeException("You need to set APP_NAME in your environment property");
            }

            if (StringUtils.isEmpty(instanceName)) {
                throw new RuntimeException("You must set module instance name when you startup this application! Because we need to get properties from domainconfigdb database!Parameter type:APP=app/instance_name/startType");
            }

            String argParams = "";
            String argType = "";
            for (PropertySource item : propertySources) {
                if ((item instanceof SimpleCommandLinePropertySource)) {
                    String property = ((SimpleCommandLinePropertySource) item).getProperty("nonOptionArgs");
                    String[] nonOptionArgs = StringUtils.commaDelimitedListToStringArray(property);
                    for (String argItem : nonOptionArgs) {
                        if (argItem.toUpperCase().startsWith("APP=")) {
                            argParams = argItem;
                            System.out.println("You argParams is:" + argParams);
                            break;
                        }
                    }
                }
            }
            if (!StringUtils.isEmpty(argParams)) {
                String[] split = argParams.split("=");
                if ((split.length != 2) || (split[1].split("/").length < 2)) {
                    throw new RuntimeException("Parameter type error!Parameter type:APP=app/instance_name/startType");
                }
                String[] paramValue = split[1].split("/");
                app = paramValue[0];
                instanceName = paramValue[1];
//                if (paramValue.length >= 3) {
//                    startType = Integer.valueOf(Integer.parseInt(paramValue[3]));
//                }
            }

            if (StringUtils.isEmpty(encryption)) {
                encryption = "true";
            }

            if (StringUtils.isEmpty(domainUrl)) {
                throw new RuntimeException("Empty domain config database connection string ");
            }
            Boolean isEncryption = Boolean.valueOf(encryption);
            if (isEncryption.booleanValue()) {
                domainUrl = EncryptUtil.decode(domainUrl);
            }

            String[] split = domainUrl.split("\\|");
            if ((split == null) || (split.length != 3)) {
                throw new RuntimeException("Error domain config database connection string");
            }
            Properties newProperties = new Properties();
            System.out.println("访问的环境变量地址为: " + split[0]);
            Map connectParams = new HashMap();

            connectParams.put("type", String.valueOf(startType));
            connectParams.put("url", split[0]);
            connectParams.put("username", split[1]);
            connectParams.put("password", EncryptUtil.decodePwd(split[2]));
            DomainConfigUtil dataUtil = new DomainConfigUtil();
            Integer appId = dataUtil.getAppId(app, connectParams);
            List<Map> envParasList = dataUtil.getEnvProperties(connectParams);
            Properties envProperties = new Properties();
            envProperties.putAll(envMap);
            for (Map item : envParasList) {
                String paraName = item.get("para_name").toString().trim();
                String paraValue = (String) envMap.get("ENV_" + paraName.replace(".", "_"));
                if (!StringUtils.isEmpty(paraValue)) {
                    item.put("para_value", paraValue);
                }
                setEnv(envProperties, item);
            }
            String moduleCode = instanceName.split("_")[0];
            List<Map> moduleParasList = dataUtil.getModuleParams(connectParams, appId, moduleCode);
            if (CollectionUtils.isEmpty(moduleParasList)) {
                throw new RuntimeException("There is no params, module_code is " + moduleCode);
            }
            List moduleParasList2 = dataUtil.getBrokerParams(connectParams, appId, moduleCode, instanceName);
            moduleParasList.addAll(moduleParasList2);
            for (Map item : moduleParasList) {
                String paraName = item.get("para_name").toString().trim();
                String paraValue = (String) envMap.get(app + "_" + moduleCode + "_" + paraName.replace(".", "_"));
                if (!StringUtils.isEmpty(paraValue)) {
                    item.put("para_value", paraValue);
                }
                setParams(newProperties, envProperties, item);
            }
            propertySources.addFirst(new PropertiesPropertySource(PROPERTY_SOURCE_NAME, newProperties));
        } catch (Exception e) {
            System.out.println("Set property parameter error:" + e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public int getOrder() {
        return -1147483649;
    }

    /**
     * 获取配置的环境参数
     *
     * @return
     */
    private Map getEnvInit() {
        Map envMap = new HashMap();
        envMap.putAll(System.getenv());
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = resourcePatternResolver.getResources("classpath*:setenv.sh");
            for (Resource resource : resources) {
                Map o = ConfigurationFileParseUtil.parseKeyValue(resource.getFile());
                envMap.putAll(o);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApplicationException("获取环境参数配置错误");
        }
        return envMap;
    }

    private void setEnv(Properties envProperties, Map<String, Object> item) {
        String paraName = item.get("para_name").toString().trim();
        String paraValue = item.get("para_value").toString().trim();
        if ((!StringUtils.isEmpty(item.get("is_encrypt"))) && (Boolean.valueOf(item.get("is_encrypt").toString()).booleanValue())) {
            try {
                paraValue = EncryptUtil.decode(paraValue);
            } catch (Exception e) {
                throw new RuntimeException("Environment parameter encrypt decode fail, parameter name:" + paraName + ", parameter value:" + paraValue);
            }
        }
        envProperties.put(paraName, paraValue);
    }

    private void setParams(Properties newProperties, Properties envProperties, Map<String, Object> item) {
        String paraValue = "";
        if (!StringUtils.isEmpty(item.get("para_value"))) {
            String paraName = item.get("para_name").toString().trim();
            paraValue = item.get("para_value").toString().trim();
            if ((!StringUtils.isEmpty(item.get("is_encrypt"))) && (Boolean.valueOf(item.get("is_encrypt").toString()).booleanValue())) {
                try {
                    paraValue = EncryptUtil.decode(paraValue);
                } catch (Exception ex) {
                    throw new RuntimeException("Sys parameter Encrypt decode fail,module name :" + item.get("module_code") + ", parameter name:" + paraName + ", parameter value:" + paraValue);
                }
            }
            paraValue = this.helper.replacePlaceholders(paraValue, envProperties);
            newProperties.put(paraName, paraValue);
            if (paraName.startsWith("log.config"))
                System.setProperty(paraName, paraValue);
        }
    }
}