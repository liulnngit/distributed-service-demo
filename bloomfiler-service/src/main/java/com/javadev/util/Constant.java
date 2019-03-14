package com.javadev.util;

/**
* @Description: 获取配置文件的一些列信息
* @author ll-t150
* @date 2017年12月13日 上午10:01:49 
* @version V2.0
 */
public class Constant {
    
  
    //ElasticSearch配置
    public static String CLUSTER=Util.getPropString("cluster.name");
    public static String ADDRESS=Util.getPropString("elasticsearch.address");
    public static String ADDRESS1=Util.getPropString("elasticsearch.address1");
    public static String ADDRESS2=Util.getPropString("elasticsearch.address2");
    public static String PORT=Util.getPropString("elasticsearch.port");
    
    public static int PER_SIZE = Util.getPropInt("per.search.count");
	
    public static final String T_WARNING_ORDER = "t_warning_order";
    
}
