package com.javadev.util;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;  
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

public class ElasticSearchUtil {
    
    private static Log log = LogFactory.getLog(ElasticSearchUtil.class);

    private static TransportClient client;
    public static final int PER_SERACH_COUNT = Constant.PER_SIZE;

    public ElasticSearchUtil(){
        Settings settings = Settings.settingsBuilder()
                                    .put("cluster.name", Constant.CLUSTER)
                                    //启动嗅探功能,通过一个指定任意节点,(不必是主节点)查找出集群下所有 ES 节点
                                    .put("client.transport.sniff", true)
                                    .build();
        try {
            client = TransportClient.builder().settings(settings).build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(Constant.ADDRESS),Integer.valueOf(Constant.PORT)))
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(Constant.ADDRESS1),Integer.valueOf(Constant.PORT)))
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(Constant.ADDRESS2),Integer.valueOf(Constant.PORT)))
                    ;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
    
    /**'
     * @Description: 查询ES索引的全部数据
     * @author ll-t150
     * @date 2018年4月27日 下午4:33:39
      */
     public <T> List<T> selectESAll(String index, String type,Class<T> tclass) {
         SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index).setTypes(type);
         searchRequestBuilder.setSearchType(SearchType.QUERY_THEN_FETCH).setSize(100000000);
         SearchResponse searchResponse = searchRequestBuilder.get();
         SearchHits hits = searchResponse.getHits();
         SearchHit[] searchHits = hits.hits();
         List<T> searchList = processSearchHits(searchHits, tclass);
         return searchList;
     }
     
     //提供分批加载代码逻辑
     public BloomFilter<String> loadESDataToBloom(String table){
         long startTime = System.currentTimeMillis();
         SearchRequestBuilder searchRequestBuilder = client.prepareSearch(table).setTypes(table);
         SearchResponse searchResponse = searchRequestBuilder.get();
         SearchHits hits = searchResponse.getHits();
         long count = hits.getTotalHits();
         BloomFilter<String> bf = BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8),count,0.01);
         /*int sum = 0;
         while (sum < count) {
             SearchResponse response = searchRequestBuilder.setFrom(sum).setSize(PER_SERACH_COUNT).execute().actionGet();
             sum += response.getHits().hits().length;
             log.info("total size :" + count + ", current size:" + sum);
             SearchHit[] searchHits = response.getHits().hits();
             if (null != searchHits && searchHits.length != 0) {
                 for (SearchHit s : searchHits) {
                     Map<String, Object> map = s.getSource();
                     String orderId = String.valueOf(map.get("orderId")); 
                     bf.put(orderId);
                 }
             }
         }*/
         //初始化
         int num = 0;
         num =(int)count/PER_SERACH_COUNT;
         int remain = (int)count%PER_SERACH_COUNT;
         int cpuNum = Runtime.getRuntime().availableProcessors();
         log.info("cpu processor:"+cpuNum+",cut into pieces:"+num);
         ExecutorService threadPool = Executors.newFixedThreadPool(cpuNum);
         if(num>0&&(remain>0)){
             CountDownLatch cdl = new CountDownLatch(num);
             for (int i = 0; i <=num; i++) {
                threadPool.execute(new Operater(cdl,searchRequestBuilder, bf, count, i));
                cdl.countDown();
            }
             log.info("Bloom Filter init warn order size"+count+" completely, cost time:"
                     +(System.currentTimeMillis()-startTime)+"ms");
         }else{
             SearchResponse response = searchRequestBuilder.setFrom(0).setSize(PER_SERACH_COUNT).execute().actionGet();
             log.info("total size :" + count );
             SearchHit[] searchHits = response.getHits().hits();
             if (null != searchHits && searchHits.length != 0) {
                 for (SearchHit s : searchHits) {
                     Map<String, Object> map = s.getSource();
                     String orderId = String.valueOf(map.get("orderId")); 
                     bf.put(orderId);
                 }
             } 
         }
         
        return bf;
     }
     
     class Operater implements Runnable{
        private CountDownLatch cdl; 
        private SearchRequestBuilder searchRequestBuilder;
        private BloomFilter<String> bf;
        private long total;
        private int range;

        public Operater(CountDownLatch cdl,SearchRequestBuilder searchRequestBuilder, BloomFilter<String> bf, long total, int range) {
            this.cdl = cdl;
            this.searchRequestBuilder = searchRequestBuilder;
            this.bf = bf;
            this.total = total;
            this.range = range;
        }

        @Override
        public void run() {
            try {
                cdl.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SearchResponse response = searchRequestBuilder.setFrom(range*PER_SERACH_COUNT).setSize(PER_SERACH_COUNT).execute().actionGet();
            log.info("total size :" + total + ", from size:" + range*PER_SERACH_COUNT);
            SearchHit[] searchHits = response.getHits().hits();
            if (null != searchHits && searchHits.length != 0) {
                for (SearchHit s : searchHits) {
                    Map<String, Object> map = s.getSource();
                    String orderId = String.valueOf(map.get("orderId")); 
                    bf.put(orderId);
                }
            } 
        }
         
     }
     
     
     private static <T> List<T> processSearchHits(SearchHit[] searchHits, Class<T> tclass) {
         List<T> searchList = new ArrayList<>();
         try {
             if (null != searchHits && searchHits.length != 0) {
                 for (SearchHit s : searchHits) {
                     Map<String, Object> map = s.getSource();
                     map.put("id", s.getId());
                     T obj = JSONObject.parseObject(JSONObject.toJSONString(map),tclass);
                     searchList.add(obj);
                 }
             }
         } catch (Exception e) {
             log.error(e.getMessage(), e);
         }
         return searchList;
     }
     
     public static void main(String[] args) throws ParseException {
            Settings settings = Settings.settingsBuilder()
                    .put("cluster.name", "cspes")
                    .put("client.transport.sniff", true)
                    .build();
    
            try {
                client = TransportClient.builder().settings(settings).build().addTransportAddress(
                        new InetSocketTransportAddress(InetAddress.getByName("192.168.46.163"),
                                Integer.valueOf("9300")));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
    
        }
    

}