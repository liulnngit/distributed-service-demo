package com.javadev.impl;


import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.hash.BloomFilter;
import com.javadev.IBloomFilterService;
import com.javadev.util.Constant;
import com.javadev.util.ElasticSearchUtil;

/**
* @author ll-t150
* @date 2018年4月28日 下午5:00:40 
 */

public class BloomFilterServiceImpl implements IBloomFilterService{
    
    private static Log log = LogFactory.getLog(BloomFilterServiceImpl.class);
    
    public BloomFilter<String> bf;
    
    @PostConstruct
    public void initBloomFilter() {
        try {
            ElasticSearchUtil esUtil = new ElasticSearchUtil();
            //查询风险订单数据
            /*
             * 老版本是查询加载
             * List<WarningOrder> warnOrders = esUtil.selectESAll(EsTableConstant.T_WARNING_ORDER, EsTableConstant.T_WARNING_ORDER,WarningOrder.class);
            initSize = warnOrders.size();
            for (WarningOrder warningOrder : warnOrders) {
                bf.put(warningOrder.getOrderId());
            }*/
            //初始化布隆过滤器
            bf = esUtil.loadESDataToBloom(Constant.T_WARNING_ORDER);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Bloom Filter init failed!");
        }
        log.info("Bloom Filter init completely!");
    }   
    
    @Override
    public boolean contain(String term) {
        //log.info(bf+","+term);
        if(bf.mightContain(term)){
            log.info("bloom filter contains "+term);
            return true;
        }
        return false;
    }
    
    //由于布隆过滤器的写入是线程不安全的
    @Override
    public synchronized boolean put(String term) {
        return bf.put(term);
    }

}
