package com.ssafy.a604.config;

import java.io.InputStream;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

public class YmlConfigLoader {

    private static final String DB_URL;
    private static final String DB_USER;
    private static final String DB_PASSWORD;

    static {
        Yaml yaml = new Yaml();
        try (InputStream in = YmlConfigLoader.class.getClassLoader().getResourceAsStream("application.yml")) {
            if (in == null) {
                throw new IllegalArgumentException("application.yml 파일을 찾을 수 없습니다.");
            }
            Map<String, Object> yamlMap = yaml.load(in);
            Map<String, Object> datasourceConfig = (Map<String, Object>) yamlMap.get("datasource");

            DB_URL = (String) datasourceConfig.get("url");
            DB_USER = (String) datasourceConfig.get("username");
            DB_PASSWORD = (String) datasourceConfig.get("password");

        } catch (Exception e) {
            throw new RuntimeException("YML 파일 로드 중 오류 발생", e);
        }
    }

    public static String getDbUrl() {
        return DB_URL;
    }

    public static String getDbUser() {
        return DB_USER;
    }

    public static String getDbPassword() {
        return DB_PASSWORD;
    }
}