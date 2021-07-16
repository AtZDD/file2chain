package com.jmtc.file2chain.utils;

import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Chris
 * @date 2021/6/7 13:50
 * @Email:gang.wu@nexgaming.com
 */
public class YamlUtils {
    public static Map<?, ?> loadYaml(String fileName) throws FileNotFoundException {
        //InputStream in = YamlUtils.class.getClassLoader().getResourceAsStream(fileName);
        InputStream in = new FileInputStream(new File(System.getProperty("user.dir") + "/config/" + fileName));
        return StringUtils.isNotEmpty(fileName) ? (LinkedHashMap<?, ?>) new Yaml().load(in) : null;
    }

    public static void dumpYaml(String fileName, Map<?, ?> map) throws IOException {
        if (StringUtils.isNotEmpty(fileName)) {
            FileWriter fileWriter = new FileWriter(YamlUtils.class.getResource(fileName).getFile());
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml yaml = new Yaml(options);
            yaml.dump(map, fileWriter);
        }
    }

    public static Object getProperty(Map<?, ?> map, Object qualifiedKey) {
        if (map != null && !map.isEmpty() && qualifiedKey != null) {
            String input = String.valueOf(qualifiedKey);
            if (!input.equals("")) {
                if (input.contains(".")) {
                    int index = input.indexOf(".");
                    String left = input.substring(0, index);
                    String right = input.substring(index + 1, input.length());
                    return getProperty((Map<?, ?>) map.get(left), right);
                } else if (map.containsKey(input)) {
                    return map.get(input);
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static void setProperty(Map<?, ?> map, Object qualifiedKey, Object value) {
        if (map != null && !map.isEmpty() && qualifiedKey != null) {
            String input = String.valueOf(qualifiedKey);
            if (!input.equals("")) {
                if (input.contains(".")) {
                    int index = input.indexOf(".");
                    String left = input.substring(0, index);
                    String right = input.substring(index + 1, input.length());
                    setProperty((Map<?, ?>) map.get(left), right, value);
                } else {
                    ((Map<Object, Object>) map).put(qualifiedKey, value);
                }
            }
        }
    }
}
