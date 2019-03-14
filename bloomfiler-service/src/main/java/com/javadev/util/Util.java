package com.javadev.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

/**
* @Description: 读取配置文件信息
* @author ll-t150
* @date 2017年12月13日 上午9:51:24 
* @version V2.0
 */
public class Util {
    
    private static Properties props = new Properties();
    private static InputStream inputStream = null;
    private static String confPath = null;
    
    static {
        confPath = System.getProperty("conf");
        try {
            if(!StringUtils.isEmpty(confPath)){
                inputStream = new FileInputStream(confPath.concat("/bigdata-config.properties"));
            }else{
                ClassLoader cl = Util.class.getClassLoader();
                inputStream = cl.getResourceAsStream("bigdata-config.properties");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            props.load(inputStream);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    
      public static String getPropString(String key) {
            String rs = null;
            if (props != null) {
                rs = props.getProperty(key);
            }
            return rs;
        }

        public static int getPropInt(String key) {
            int rs = 0;
            if (props != null) {
                rs = Integer.parseInt(props.getProperty(key));
            }
            return rs;
        }

}
