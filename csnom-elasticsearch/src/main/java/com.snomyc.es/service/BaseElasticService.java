package com.snomyc.es.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.snomyc.es.utils.ElasticUtil;
import com.ssj.es.entiy.ElasticEntity;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.IdsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.metrics.ParsedCardinality;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

/**
 * Elasticsearch 的公共Base类,已实现增删改查，子类中可直接继承使用
 * @author Admin
 */
@Component
public class BaseElasticService {
	
	protected  Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RestHighLevelClient restHighLevelClient;

    /**
     * @See
     * @param idxName   索引名称
     * @param idxSQL    索引描述
     * @return void
     * @throws
     * @since
     */
    public void createIndex(String idxName,String idxSQL){
        try {
            if (!this.indexExist(idxName)) {
                log.error(" idxName={} 已经存在,idxSql={}",idxName,idxSQL);
                return;
            }
            CreateIndexRequest request = new CreateIndexRequest(idxName);
            buildSetting(request);
            request.mapping(idxSQL, XContentType.JSON);
//            request.settings() 手工指定Setting
            CreateIndexResponse res = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
            if (!res.isAcknowledged()) {
                throw new RuntimeException("初始化失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /** 判断某个index是否存在
     * @See
     * @param idxName index名
     * @return boolean
     * @throws
     * @since
     */
    public boolean indexExist(String idxName) throws Exception {
        GetIndexRequest request = new GetIndexRequest(idxName);
        request.local(false);
        request.humanReadable(true);
        request.includeDefaults(false);
        request.indicesOptions(IndicesOptions.lenientExpandOpen());
        return restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
    }

    /** 判断某个index是否存在
     * @See
     * @param idxName index名
     * @return boolean
     * @throws
     * @since
     */
    public boolean isExistsIndex(String idxName) throws Exception {
        return restHighLevelClient.indices().exists(new GetIndexRequest(idxName),RequestOptions.DEFAULT);
    }

    /** 设置分片
     * @See
     * @param request
     * @return void
     * @throws
     * @since
     */
    public void buildSetting(CreateIndexRequest request){
        request.settings(Settings.builder().put("index.number_of_shards",3)
                .put("index.number_of_replicas",2));
    }
    /**
     * @See
     * @param idxName index
     * @param entity    对象
     * @param openRetry
     * @return void
     * @throws
     * @since
     */
    public void insertOrUpdateOne(String idxName, ElasticEntity entity, boolean openRetry) {
        IndexRequest request = new IndexRequest(idxName);
        log.error("Data : id={},entity={}",entity.getId(),JSON.toJSONString(entity.getData()));
        request.id(entity.getId());
//        request.source(entity.getData(), XContentType.JSON);
        request.source(JSON.toJSONString(entity.getData()), XContentType.JSON);
        boolean result = false;
        int retryCount = 0;
        try {
            do {
                try {
                    restHighLevelClient.index(request, RequestOptions.DEFAULT);
                    result = true;
                } catch (IOException e) {
                    if (!openRetry || retryCount >= BaseSearchService.MAX_RETRIES) {
                        throw e;
                    }
                    retryCount ++;
                }
            } while (openRetry && !result && retryCount <= BaseSearchService.MAX_RETRIES);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @See
     * @param idxName index
     * @param entity    对象
     * @param openRetry
     * @return void
     * @throws
     * @since
     */
    public void insertOrUpdateOneImmediate(String idxName, ElasticEntity entity, boolean openRetry) {
        IndexRequest request = new IndexRequest(idxName);
        log.debug("Data : id={},entity={}",entity.getId(),JSON.toJSONString(entity.getData()));
        request.id(entity.getId());
        request.source(JSON.toJSONString(entity.getData()), XContentType.JSON);
        request.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        boolean result = false;
        int retryCount = 0;
        try {
            do {
                try {
                    restHighLevelClient.index(request, RequestOptions.DEFAULT);
                    result = true;
                } catch (IOException e) {
                    if (!openRetry || retryCount >= BaseSearchService.MAX_RETRIES) {
                        throw e;
                    }
                    retryCount ++;
                }
            } while (openRetry && !result && retryCount <= BaseSearchService.MAX_RETRIES);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> void insertBatchES(String idxName,List<T> list) throws Exception{
        BulkRequest request = new BulkRequest();


        for (int i = 0,lengh=list.size(); i < lengh; i++) {

            Map<String, Object> map=  JSON.parseObject(JSON.toJSONString(list.get(i)), new TypeReference<Map<String, Object>>() {});

         String id = UUID.randomUUID().toString();
         if(map.containsKey("id")){
           id =  StringUtils.isNotBlank(map.get("id").toString()) ? map.get("id").toString():id;
         }
            IndexRequest indexRequest =  new IndexRequest(idxName).id(id);
            indexRequest.source(map);
            request.add(indexRequest);
        }

/*
        for (int i = 0,lengh=list.size(); i < lengh; i++) {
            request.add(new IndexRequest(idxName).id(worker.nextId()).source(JSON.toJSONString(list.get(i)), XContentType.JSON));
        }*/

        try {
            restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 更新es
     * @param indexName
     * @param queryBuilder
     * @param script
     */
    public void updteByQuery (String indexName, QueryBuilder queryBuilder, Script script) {
        UpdateByQueryRequest request = new UpdateByQueryRequest(indexName);
        request.setQuery(queryBuilder);
        request.setScript(script);
        request.setBatchSize(10000);
        request.setRefresh(true);
        try {
            restHighLevelClient.updateByQuery(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @See
     * @param idxName index
     * @param entity    对象
     * @return void
     * @throws
     * @since
     */
    public void deleteOne(String idxName, ElasticEntity entity) {
        DeleteRequest request = new DeleteRequest(idxName);
        request.id(entity.getId());
        try {
            restHighLevelClient.delete(request,RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** 批量插入数据
     * @See
     * @param idxName index
     * @param list 带插入列表
     * @return void
     * @throws
     * @since
     */
    public void insertBatch(String idxName, List<ElasticEntity> list) {
        BulkRequest request = new BulkRequest();
        list.forEach(item -> request.add(new IndexRequest(idxName).id(item.getId())
                .source(JSON.toJSONString(item.getData()), XContentType.JSON)));
        try {
            restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** 批量插入数据
     * @See
     * @param idxName index
     * @param list 带插入列表
     * @return void
     * @throws
     * @since
     */
    public void insertBatchTrueObj(String idxName, List<ElasticEntity> list) {
        BulkRequest request = new BulkRequest();
        list.forEach(item -> request.add(new IndexRequest(idxName).id(item.getId())
                .source(item.getData(), XContentType.JSON)));
        try {
            restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** 批量删除
     * @See
     * @param idxName index
     * @param idList    待删除列表
     * @return void
     * @throws
     * @since
     */
    public <T> void deleteBatch(String idxName, Collection<T> idList) {
        BulkRequest request = new BulkRequest();
        idList.forEach(item -> request.add(new DeleteRequest(idxName, item.toString())));
        try {
            restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @See
     * @param idxName index
     * @param builder   查询参数
     * @param c 结果类对象
     * @return java.util.List<T>
     * @throws
     * @since
     */
    public <T> List<T> search(String idxName, SearchSourceBuilder builder, Class<T> c) {
        long startTime = System.currentTimeMillis();
        SearchRequest request = new SearchRequest(idxName);
        request.source(builder);
        try {
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            long endTime = System.currentTimeMillis();
            log.error("es返回之后="+(endTime - startTime));
            SearchHit[] hits = response.getHits().getHits();
            List<T> res = new ArrayList<>(hits.length);
            for (SearchHit hit : hits) {
                res.add(JSON.parseObject(hit.getSourceAsString(), c));
            }
            long endTimeend = System.currentTimeMillis();
            log.error("组装时间="+(endTimeend - endTime));
            log.error("组装返回返回之后="+(endTimeend - startTime));
            return res;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    
    /**
     * @See
     * @param idxName index
     * @param builder   查询参数
     * @param c 结果类对象
     * @return map
     * @throws
     * @since
     */
    public <T> Page<T> searchPage(String idxName, SearchSourceBuilder builder, Class<T> c, Pageable pageable) {
        long startTime = System.currentTimeMillis();
        Page<T> page=new PageImpl<T>(new ArrayList<T>());
        if(pageable==null) {
        	pageable= new PageRequest(1, 500);//默认为第一页，一共500条。
        }
        try {
        	long total=count(idxName, builder);
        	if(total>0) {
	        	SearchRequest request = new SearchRequest(idxName);
	        	builder.from(pageable.getPageNumber());
	        	builder.size(pageable.getPageSize());
	            request.source(builder);
	            
	            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
                long endTime = System.currentTimeMillis();
                log.error("es返回之后="+(endTime - startTime));
	            SearchHit[] hits = response.getHits().getHits();
	            List<T> data = new ArrayList<>(hits.length);
	            for (SearchHit hit : hits) {
	                data.add(JSON.parseObject(hit.getSourceAsString(), c));
	            }
	            page=new PageImpl<T>(data,pageable,total);

                long endTimeend = System.currentTimeMillis();
                log.error("组装时间="+(endTimeend - endTime));
                log.error("组装返回返回之后="+(endTimeend - startTime));
        	}

            return page;
         } catch (Exception e) {
             throw new RuntimeException(e);
         }
    }

    


    /**
     * @See
     * @param idxName index
     * @param builder   查询参数
     * @param c 结果类对象
     * @return map
     * @throws
     * @since
     */
    public <T> Map<String, Object> searchDataAndCount(String idxName, SearchSourceBuilder builder, Class<T> c) {
        SearchRequest request = new SearchRequest(idxName);
        request.source(builder);
        try {
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            SearchHit[] hits = response.getHits().getHits();
            List<T> data = new ArrayList<>(hits.length);
            Map<String, Object> res  = new HashMap<>();
            for (SearchHit hit : hits) {
                data.add(JSON.parseObject(hit.getSourceAsString(), c));
            }
            res.put("data", data);
            res.put("total", response.getHits().getTotalHits().value);
            return res;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @See
     * @param idxName index
     * @param builder   查询参数
     * @param c 结果类对象
     * @return map
     * @throws
     * @since
     */
    public <T> Map<String, Object> searchDataAndCount(String idxName, SearchSourceBuilder builder, Class<T> c, String aggName) {
        SearchRequest request = new SearchRequest(idxName);
        request.source(builder);
        try {
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            SearchHit[] hits = response.getHits().getHits();
            List<T> data = new ArrayList<>(hits.length);
            Map<String, Object> res  = new HashMap<>();
            for (SearchHit hit : hits) {
                data.add(JSON.parseObject(hit.getSourceAsString(), c));
            }
            res.put("data", data);
            res.put("total", ((ParsedCardinality)response.getAggregations().get(aggName)).getValue());
            return res;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @See
     * @param idxName index
     * @param builder   查询参数
     * @return map
     * @throws
     * @since
     */
    public SearchResponse searchResponse(String idxName, SearchSourceBuilder builder) {
        SearchRequest request = new SearchRequest(idxName);
        request.source(builder);
        try {
            return restHighLevelClient.search(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @See
     * @param idxName index
     * @param builder   查询参数
     * @return Long
     * @throws
     * @since
     */
    public Long count(String idxName, SearchSourceBuilder builder) {
    	SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(builder.query());
        
        CountRequest countRequest = new CountRequest(idxName);
    	countRequest.source(sourceBuilder);
        try {
        	 CountResponse countResponse = restHighLevelClient.count(countRequest, RequestOptions.DEFAULT);
        	 return countResponse.getCount();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }    
    }
    

    /** 删除index
     * @See
     * @param idxName
     * @return void
     * @throws
     * @since
     */
    public void deleteIndex(String idxName) {
        try {
            if (!this.indexExist(idxName)) {
                log.error(" idxName={} 已经存在",idxName);
                return;
            }
            restHighLevelClient.indices().delete(new DeleteIndexRequest(idxName), RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @See
     * @param idxName
     * @param builder
     * @return void
     * @throws
     * @since
     */
    public void deleteByQuery(String idxName, QueryBuilder builder) {

        DeleteByQueryRequest request = new DeleteByQueryRequest(idxName);
        request.setQuery(builder);
        //设置批量操作数量,最大为10000
        request.setBatchSize(10000);
        request.setConflicts("proceed");
        try {
            restHighLevelClient.deleteByQuery(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @See
     * @param idxName
     * @return void
     * @throws
     * @since
     */
    public <T> T getById(String idxName, String id, Class<T> c) {
        try {
        	IdsQueryBuilder idsQueryBuilder=QueryBuilders.idsQuery();
    		idsQueryBuilder.addIds(id);
    		QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                    .must(idsQueryBuilder);
            SearchSourceBuilder builder = ElasticUtil.initSearchSourceBuilder(queryBuilder);
            SearchRequest request = new SearchRequest(idxName);
            request.source(builder);
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            SearchHit[] hits = response.getHits().getHits();
            if(hits.length>0) {
            	return JSON.parseObject(hits[0].getSourceAsString(), c);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    protected boolean bulk(BulkRequest bulkRequest) {
        try {
            restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("批量插入或更新练习册题目失败");
            return false;
        }

        return true;
    }


}
