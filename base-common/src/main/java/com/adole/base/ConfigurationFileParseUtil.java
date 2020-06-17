package com.adole.base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: adole
 * @Date: 2020/5/23 13:28
 */
public class ConfigurationFileParseUtil {

    public static Map parseKeyValue(File file) {
        Map map = new HashMap();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                String[] split = line.split("=",2);
                String key = split[0].trim();
                String value = split[1].trim();
                map.put(key, value);
            }
        } catch (Exception e) {
            throw new ApplicationException("解析配置文件错误");
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }
}
