package com.snomyc.es.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ssj.es.entiy.EsResModel;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.RequestLine;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedTopHits;
import org.elasticsearch.search.aggregations.metrics.TopHitsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;


/**
 *  
 */
@Component
public class BaseSearchService {
	
	protected  Logger log = LoggerFactory.getLogger(this.getClass());

	public static final int MAX_RETRIES = 5;
	
	@Autowired
	RestHighLevelClient restHighLevelClient;

	public   EsResModel findEsIndexPage(String indexName, LinkedHashMap<String,Object> params) {
		return findEsIndexPage( indexName, params, null,null,null);
	}
	
	public   EsResModel findEsIndexPage(String indexName, LinkedHashMap<String,Object> params,Integer page,Integer pageSize) {
		return findEsIndexPage( indexName, params,null,page,pageSize);
	}
		
	/**
	 * DSL格式查询数据
	 * @param indexName
	 * @param params
	 * @param orders
	 * @return
	 */
    public  EsResModel findEsIndexPage(String indexName, 
    		LinkedHashMap<String,Object> params,LinkedHashMap<String,Object> orders,
    		Integer page,Integer pageSize) {
    	EsResModel model=new EsResModel();
    	Set<String> ksysSet=new HashSet<String>();
    	List<String> wildcards=new ArrayList<String>();
	    if(params!=null){
	    	Set<String> keys= params.keySet();  
            for (String key : keys) {
            	if(params.get(key)!=null){
            		wildcards.add("{\"match_phrase\": { \""+key+"\": { \"query\":\""+params.get(key).toString()+"\"} }}");
            		ksysSet.add(params.get(key).toString());
            	}
			}
	    }
	    List<String> ordercards=new ArrayList<String>();
	    if(orders!=null){
	    	Set<String> keys= orders.keySet();  
            for (String key : keys) {
            	if(orders.get(key)!=null){
            		ordercards.add("{\""+key+"\": { \"order\": \""+orders.get(key).toString()+"\" }}");
            	}
			}
	    } 
    	try {
    	     StringBuffer bb=new StringBuffer();
    	     bb.append(" { ");
	    	     bb.append(" \"query\": { ");
	    	     	bb.append(" \"bool\" : ");
	    	     		bb.append(" { ");
	    	     		bb.append("  \"must\" :");
	    	     			bb.append(" [ ");
	    	     			 	if(wildcards.size()>0){
	    	     			 		bb.append( StringUtils.join(wildcards, ",") );
	    	     			 	}
	    	     			bb.append( " ]" );

	    	     		bb.append( " } " );
	    	     bb.append( " } " );
	    	     if(page!=null && pageSize!=null){
	    	    	 bb.append( " , " );
	    	    	 bb.append( " \"from\": "+((page-1)*pageSize)+"," );
	    	    	 bb.append( " \"size\": "+pageSize+"" );
	    	     }
    	     if(ordercards!=null && ordercards.size()>=1){
    	    	 bb.append( "  ,\"sort\": [  "+StringUtils.join(ordercards, ",")+"]" );
    	     }
	    	 bb.append( " }");
    		 System.out.println(bb.toString());
			 String indexHtml=this.sendGetRequest("/"+indexName+"/_search",bb.toString());
			 System.out.println(indexHtml);
			 JSONObject reqObj=(JSONObject)JSONObject.parse(indexHtml);
			 reqObj=(JSONObject)reqObj.get("hits");
			 model.setTotal(reqObj.getJSONObject("total").getInteger("value"));
			 model.setList(JSONArray.parseArray(reqObj.get("hits").toString()));
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return model;
    }
    


	/**
	 * 批量插入数据至es
	 *
	 * @param indexName es索引名称
	 * @param dataMapList 插入的数据
	 * @param openRetry 是否开启重试，最多重试MAX_RETRIES次
	 */
	public void insertBatch(String indexName, List<Map<String, Object>> dataMapList, boolean openRetry) {
		boolean result = false;
		int retryCount = 0;

		try {
			BulkRequest bulkRequest = new BulkRequest();
			dataMapList.forEach(dataMap -> {
				IndexRequest indexRequest = new IndexRequest(indexName).id(String.valueOf(dataMap.get("id")));
				indexRequest.source(dataMap);
				bulkRequest.add(indexRequest);
			});
			bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
			do {
				try {
					restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
					result = true;
				} catch (IOException e) {
					if (!openRetry || retryCount >= MAX_RETRIES) {
						throw e;
					}
					retryCount ++;
				}
			} while (openRetry && !result && retryCount <= MAX_RETRIES);
		} catch (Exception e) {
			//dataMapList.forEach(dataMap -> redisUtil.lpush(Constant.ES_SAVE_ERROR_QUESTIONS, dataMap.getOrDefault("question_id", "").toString()));
			log.error("es批量插入数据异常", e);
			log.error("indexName=[{}]", indexName);
		}
	}

	/**
	 * 批量同步数据至es，存在则更新，否则插入
	 *
	 * @return 是否操作成功
	 */
	public boolean updateOrInsertBatch(String indexName, List<Map<String, Object>> dataMapList) {
		try {
			BulkRequest bulkRequest = new BulkRequest();
			dataMapList.forEach(dataMap -> bulkRequest.add(new UpdateRequest(indexName, String.valueOf(dataMap.get("id"))).doc(dataMap).upsert(dataMap)));
			bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
			restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
			return true;
		} catch (Exception e) {
			log.error("es批量同步数据异常", e);
			log.error("indexName=[{}]", indexName);
		}

		return false;
	}

	/**
	 * 根据es查询query删除数据
	 * @param indexName 索引名称
	 * @param queryMap 查询query
	 */
	public  void deleteByQuery(String indexName, Map<String, Object> queryMap){
    	try {
			Map<String, Object> postMap = Collections.singletonMap("query", queryMap);
			String postJson = JSON.toJSONString(postMap);
    		log.info("----delete--->{}", postJson);
			this.sendPostRequest("/" + indexName + "/_delete_by_query", postJson);
		} catch (Exception e) {
    		log.error("es删除数据异常", e);
    		log.error("indexName=[{}], queryMap=[{}]", indexName, queryMap);
		}
    }

	public  EsResModel findPage(String indexName, Map<String, Object> queryMap, List<Map<String, Object>> sortList, List<String> selectColumnList,
									  Map<String, Object> pageMap, Map<String, Object> aggregationMap) {

		EsResModel esResModel = new EsResModel();
		try {
			//post参数
			Map<String, Object> postMap = new HashMap<>();

			//查询条件
			if (queryMap == null || queryMap.size() < 1) {
				postMap.put("query", Collections.singletonMap("match_all", new Object()));
			} else {
				postMap.put("query", queryMap);
			}

			//指定需要查询的列
			if (CollectionUtils.isNotEmpty(selectColumnList)) {
				postMap.put("_source", selectColumnList);
			}

			//自定义排序
			if (CollectionUtils.isNotEmpty(sortList)) {
				postMap.put("sort", sortList);
			}

			//自定义分页
			if (pageMap != null && pageMap.size() > 0) {
				pageMap.forEach(postMap::put);
			}

			//聚合
			if (aggregationMap != null) {
				postMap.put("aggs", aggregationMap);
			}

			String uri = "/" + indexName + "/_search";
			String responseStr = this.sendPostRequest(uri, JSON.toJSONString(postMap));
			JSONObject responseObj = (JSONObject) JSON.parse(responseStr);
			JSONObject hitsObj = responseObj.getJSONObject("hits");
			esResModel.setTotal(hitsObj.getJSONObject("total").getInteger("value"));
			esResModel.setList(hitsObj.getJSONArray("hits"));
			esResModel.setAggregations(responseObj.getJSONObject("aggregations"));
		} catch (Exception e) {
			log.error("es查询异常", e);
			log.error("参数：indexName=[{}], paramMap={}, sortList={}, selectColumnList={}, pageMap={}, aggregationMap={}",
					indexName, queryMap, sortList, selectColumnList, pageMap, aggregationMap);
		}

		return esResModel;
	}

	
	/**
	 * 根据id批量删除
	 * @param indexName 索引名
	 * @param idList id列表
	 */
	public  void deleteByIdList(String indexName, List<String> idList) {
		try {
			BulkRequest bulkRequest = new BulkRequest();
			idList.forEach(id -> bulkRequest.add(new DeleteRequest(indexName, id)));
			bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
			restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
		} catch (Exception e) {
			log.error("es批量删除数据异常", e);
			log.error("indexName=[{}], idList={}", indexName, idList);
		}
	}

	/**
	 * 需要清空缓存的所以名称，缺省则清空所有索引缓存
	 * @param indexName 索引名称
	 */
	public  void clearCache(String indexName) {
		try {
			String uri = "_cache/clear";
			if (StringUtils.isNotBlank(indexName)) {
				uri = "/" + indexName + "/" + uri;
			}
			this.sendPostRequest(uri, "{}");
		} catch (Exception e) {
			log.error("indexName=[{}]", indexName);
			log.error("es清空缓存异常", e);
		}
	}

	public  SearchResponse search(SearchRequest searchRequest, RequestOptions requestOptions) {
		try {
			SearchResponse searchResponse = restHighLevelClient.search(searchRequest, requestOptions == null ? RequestOptions.DEFAULT : requestOptions);
			if (searchResponse.status().getStatus() == 200) {
				return searchResponse;
			}
		} catch (Exception e) {
			log.error("es搜索异常", e);
			log.error("searchRequest={}，requestOptions={}", searchRequest, requestOptions);
		}
		return null;
	}

	public List<Map<String, Object>> findByHowDetailKdIdFromEs(List<String> howDetailIdList,
															  Set<String> existQuestionIdSet, String[] includeColumns,
															  int difficulty, int needSum, int smallQuestionSum){

		List<Map<String, Object>> resultMapList = new LinkedList<>();
		SearchRequest searchRequest = new SearchRequest();
		searchRequest.indices("kmt_exercise_question_know");
		searchRequest.source(new SearchSourceBuilder());
		BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
		searchRequest.source().query(boolQueryBuilder);
		searchRequest.source().size(0);

		//题目发布状态
		BoolQueryBuilder dealStatusFilterBuilder = new BoolQueryBuilder();
		boolQueryBuilder.must(dealStatusFilterBuilder);
		dealStatusFilterBuilder.should(new RangeQueryBuilder("deal_status").gte(4).lte(5));
		dealStatusFilterBuilder.should(new RangeQueryBuilder("deal_status").gt(7));

		//题目难度
		boolQueryBuilder.must(new QueryStringQueryBuilder(String.valueOf(difficulty)).field("difficulty_level"));

		//知识点过滤
		BoolQueryBuilder knowledgeFilterBuilder = new BoolQueryBuilder();
		boolQueryBuilder.must(knowledgeFilterBuilder);
		howDetailIdList.forEach(it -> knowledgeFilterBuilder.should(new QueryStringQueryBuilder(it).field("how_details_id.keyword")));

		//排除题目
		existQuestionIdSet.forEach(it -> boolQueryBuilder.mustNot(new QueryStringQueryBuilder(it).field("question_id.keyword")));

		//聚合分组
		TermsAggregationBuilder groupByParentIdOrQuestionId = AggregationBuilders.terms("group_by_parent_id_or_question_id")
				.script(new Script("if(doc['parent_id.keyword'].value.length() > 0){return doc['parent_id.keyword'].value} else{return doc['question_id.keyword'].value}"))
				.order(BucketOrder.aggregation("randomSort", true))
				.size(needSum);
		searchRequest.source().aggregation(groupByParentIdOrQuestionId);

		AvgAggregationBuilder randomSort = AggregationBuilders.avg("randomSort")
				.script(new Script("Math.random()"));
		groupByParentIdOrQuestionId.subAggregation(randomSort);

		TermsAggregationBuilder groupByQuestionId = AggregationBuilders.terms("group_by_question_id");
		groupByQuestionId.script(new Script("doc['num'].value + '_' + doc['question_id.keyword'].value"));
		groupByQuestionId.order(BucketOrder.key(true));
		groupByQuestionId.size(smallQuestionSum);
		groupByParentIdOrQuestionId.subAggregation(groupByQuestionId);

		TopHitsAggregationBuilder topHitsAggregationBuilder = AggregationBuilders.topHits("top_tag_hits")
				.fetchSource(includeColumns, ArrayUtils.EMPTY_STRING_ARRAY)
				.size(1);
		groupByQuestionId.subAggregation(topHitsAggregationBuilder);
		log.debug("-------->{}", searchRequest.source().toString());
		SearchResponse response = this.search(searchRequest, null);

		if (response != null) {
			ParsedStringTerms parsedStringTerms = response.getAggregations().get("group_by_parent_id_or_question_id");
			for (Terms.Bucket groupByParentIdOrQuestionIdBucket : parsedStringTerms.getBuckets()) {
				ParsedStringTerms groupByQuestionIdParsedStringTerms = groupByParentIdOrQuestionIdBucket.getAggregations().get("group_by_question_id");
				for (Terms.Bucket groupByQuestionIdParsedStringTermsBucket : groupByQuestionIdParsedStringTerms.getBuckets()) {
					ParsedTopHits parsedTopHits = groupByQuestionIdParsedStringTermsBucket.getAggregations().get("top_tag_hits");
					for (SearchHit hit : parsedTopHits.getHits().getHits()) {
						resultMapList.add(hit.getSourceAsMap());
					}
				}
			}
		}

		return resultMapList;
	}
	
	
	
	//同步发送请求
	public  String sendGetRequest(String uri, String json) throws Exception {
		 // 创建一个请求组
		 Request request = new Request("GET", uri);
		 // 为请求添加一些参数
		 request.addParameter("pretty", "true");
		 // 还可以将其设置为一个字符串，该字符串将默认为application/json的ContentType。
		 if(StringUtils.isNotEmpty(json)){
			request.setJsonEntity(json);
		 }
		 Response response = restHighLevelClient.getLowLevelClient().performRequest(request);
		// 有关已执行请求的信息
		 RequestLine requestLine = response.getRequestLine();
		 //返回响应的主机
		 HttpHost host = response.getHost();
		 //响应状态行，您可以从中检索状态代码
		 int statusCode = response.getStatusLine().getStatusCode();
		//响应头
		 Header[] headers = response.getHeaders();
		 String responseBody = EntityUtils.toString(response.getEntity());
		 log.info("Uri:"+requestLine.getUri()+"   status:"+statusCode);
		 return responseBody;
	}
	
	
	
	//同步发送请求
	public  String sendPostRequest(String uri, String json) throws Exception {
		 // 创建一个请求组
		 Request request = new Request("POST", uri);
		 // 为请求添加一些参数
		 request.addParameter("pretty", "true");
		 // 还可以将其设置为一个字符串，该字符串将默认为application/json的ContentType。
		 if(StringUtils.isNotEmpty(json)){
			request.setJsonEntity(json);
		 }
		 Response response = restHighLevelClient.getLowLevelClient().performRequest(request);
		// 有关已执行请求的信息
		 RequestLine requestLine = response.getRequestLine();
		 //返回响应的主机
		 HttpHost host = response.getHost();
		 //响应状态行，您可以从中检索状态代码
		 int statusCode = response.getStatusLine().getStatusCode();
		//响应头
		 Header[] headers = response.getHeaders();
		 String responseBody = EntityUtils.toString(response.getEntity());
		 log.info("Uri:"+requestLine.getUri()+"   status:"+statusCode);
		 return responseBody;
	}
		
		
	
	
	
	
	public  void main(String[] args) {
		
//		deleteEsIndexById("kmt_exercise_questions", "4644a9ec-86a1-34e1-bb5e-bdbad2ff8157");
		
//		List<Map<String, Object>> dataMapList = new LinkedList<>();

//		String dataStr = "{\"num\":291,\"handle_user_id\":\"87684f70-b4bb-4b35-b084-60c08a4e4cd2\",\"source\":\"\",\"answer_image\":\"math_renjiao_grade21_qkwtbkslxdbcsj_answer/f8729a85-f987-41f4-ba45-7d023ac4e611.jpg\",\"small_question_name\":\"5\",\"precision_percent\":\"0.997\",\"how_detail_kd_sort_id\":\"a97bf85d-cbd4-11e8-94c4-00163e04df3f\",\"publish_time\":\"2019-09-06T16:22:47.000Z\",\"id\":\"0d72722f-c58b-11e9-991c-00163e022481\",\"unit_id\":\"be17ff29-c31a-11e9-991c-00163e022481\",\"book_course_id\":\"be2a86bb-c31a-11e9-991c-00163e022481\",\"deal_status\":4,\"image\":\"/ai/results/math_renjiao_grade21_qkwtbkslxdbcsj_question/d0445b9f-8790-4a3c-982e-2195615daccd.jpg\",\"how_details_id\":\"6fdc3d47-7ed2-463a-92d0-156a2f02d722\",\"update_type\":1,\"answer_json\":\"[{\\\"index\\\":0,\\\"list\\\":[{\\\"style\\\":{\\\"height\\\":16,\\\"width\\\":16},\\\"type\\\":1,\\\"value\\\":\\\"4\\\"}],\\\"selectedType\\\":0}]\",\"create_time\":\"2019-08-23T17:47:42.000Z\",\"question\":\"<img src='https://img.sharingschool.com/ai/results/math_renjiao_grade21_qkwtbkslxdbcsj_question/d0445b9f-8790-4a3c-982e-2195615daccd.jpg'/>\",\"big_question_index\":\"一\",\"how_details_kd_id\":\"97269b8e-9726-4ab4-8220-55dd758cc9a8\",\"book_id\":\"659958c2-84bc-3cd3-b164-ed8f3f7a6b10\",\"auditing_user_id\":\"5cfea228-53a2-42d0-95a5-bea2972ade51\",\"question_id\":\"d0445b9f-8790-4a3c-982e-2195615daccd\",\"version\":2,\"question_text\":\"5.小英有两件上衣和两条裤子,如果她想搭配一套衣服,有()种不同的<br/>搭配方法。\",\"question_json\":\"[]\",\"analysis_submit_type\":0,\"parent_id\":\"1fb2fc11-9140-4849-849b-6e1380d21d60\",\"page_index\":19,\"option_answer\":\"<img src='https://img.sharingschool.com/ai/results/math_renjiao_grade21_qkwtbkslxdbcsj_answer/f8729a85-f987-41f4-ba45-7d023ac4e611.jpg'/>\",\"big_name\":\"\",\"question_type\":-1,\"real_exam_type\":\"\",\"difficulty_level\":-1,\"course_name\":\"\",\"training_skill\":\"\",\"analysis_json\":\"\",\"concise_id\":\"\",\"analysis_auditer_id\":\"\",\"how_detail_sort_id\":\"a27cdc62-2408-11e8-a093-e0db55b9888d\",\"option_answer_text\":\"4\"}";
//		Map map = JSON.parseObject(dataStr, Map.class);
//		dataMapList.add(map);

//		dataStr = "{\"option_json\":null,\"unit_id\":\"54e24914-3732-11e9-a55b-00163e04df3f\",\"small_question_index\":null,\"old_answer_json\":null,\"big_name\":\"\",\"question_json\":null,\"auditing_user_id\":null,\"type\":\"kmt_exercise_question_know\",\"remark\":null,\"question_text\":\"十、习作。(任选一题写一篇习作)(30分)<br/>1.在学习和生活中,你一定遇见过一些美好的事情,如:观赏过<br/>美丽的风景,阅读过精彩的文章或书籍,实现过美好的愿望,<br/>等等。请你以《美好的<br/>》为题目,写一篇作文。<br/>围绕题目把自己的见闻和感受写清楚,不少于400字,标点符<br/>号使用正确,字迹工整,卷面整洁<br/>2.写作业几乎是同学们天天必做的事,因而,可能会有许许多多<br/>和作业相关的事情发生。请你以“作业”为话题写一篇记叙<br/>文。题目自拟。<br/>要求:①情感真实,内容具体,语句通顺;<br/>②书写工整,标点符号使用正确。400字左右\",\"small_name\":\"\",\"publish_time\":null,\"book_id\":\"d92aae76-6273-38f5-8afa-1cc903d8e36f\",\"how_details_kd_id\":\"1bb197ab-3362-45f9-b7a8-94a1e990b0cd\",\"image\":\"语文-人教版-四下-黄冈达标卷题目/451bb756-a55a-305a-b9f5-9aca87b6c4ec.jpg\",\"question\":\"<img src='https://t.sharingschool.com/upload/ai/results/语文-人教版-四下-黄冈达标卷题目/451bb756-a55a-305a-b9f5-9aca87b6c4ec.jpg'/>\",\"handle_user_id\":null,\"option_answer_text\":\"\",\"update_type\":0,\"book_course_id\":\"54ea9c0e-3732-11e9-a55b-00163e04df3f\",\"deal_status\":4,\"small_question_name\":\"0\",\"big_question_index\":\"十\",\"source\":\"\",\"id\":\"95843602-db3a-44f7-a2e7-34b5a06d05de\",\"option_content\":\"\",\"big_question_index_sort\":0,\"question_id\":\"451bb756-a55a-305a-b9f5-9aca87b6c4ec\",\"option_answer\":\"\",\"how_details_id\":\"4e5b1316-51f5-4a09-95ad-057ac308f5ee\",\"answer_image\":\"\",\"num\":33,\"create_time\":\"2019-02-28T01:58:39.000Z\",\"parent_id\":\"\",\"precision_percent\":\"0.996\",\"answer_json\":null,\"page_index\":2}";
//		map = JSON.parseObject(dataStr, Map.class);

//		dataMapList.add(map);
//		BaseSearchService.updateOrInsertBatch("kmt_exercise_question_know", dataMapList);
//		BaseSearchService.clearCache("kmt_exercise_question_know");
//		int version = 2;
//		List<String> list = new LinkedList<>();
//		list.add("f7bb7d0d-c886-425f-8c9a-3ca0776036a4aaaa");
//		SearchRequest searchRequest = new SearchRequest();
//		searchRequest.indices("kmt_exercise_question_know");
//		searchRequest.source(new SearchSourceBuilder());
//		BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
//		searchRequest.source().query(boolQueryBuilder);
//		searchRequest.source().size(0);
//		boolQueryBuilder.must(new TermQueryBuilder("version", version));
//		list.forEach(it -> boolQueryBuilder.must(new QueryStringQueryBuilder(it).field("how_details_id.keyword")));
//		search(searchRequest, null);

//		SearchRequest searchRequest = new SearchRequest();
//		searchRequest.indices("kmt_question_know");
//		searchRequest.source(new SearchSourceBuilder());
//		BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
//		searchRequest.source().query(boolQueryBuilder);
//		searchRequest.source().size(0);
//		BoolQueryBuilder knowledgeFilterBuilder = new BoolQueryBuilder();
//		boolQueryBuilder.must(knowledgeFilterBuilder);
//		List<String> wrongQuestionHowDetailKdIdList = new ArrayList<>();
//		wrongQuestionHowDetailKdIdList.add("2cceeb7e-7843-11e8-bd6d-00163e0833db");
//		wrongQuestionHowDetailKdIdList.add("65e04f8d-f70c-430f-8064-4df6885788c7");
//		wrongQuestionHowDetailKdIdList.forEach(it -> knowledgeFilterBuilder.should(new QueryStringQueryBuilder(it).field("how_details_kd_id.keyword")));
//		TopHitsAggregationBuilder topHitsAggregationBuilder = AggregationBuilders.topHits("top_tag_hits")
//				.sort(SortBuilders.scriptSort(new Script("Math.random()"), ScriptSortBuilder.ScriptSortType.NUMBER).order(SortOrder.ASC))
//				.fetchSource(new String[]{"question_id", "parent_id", "question", "option_content"}, ArrayUtils.EMPTY_STRING_ARRAY)
//				.size(1);
//		TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("group_by_how_details_kd_id")
//				.field("how_details_id.keyword")
//				.subAggregation(topHitsAggregationBuilder);
//		searchRequest.source().aggregation(termsAggregationBuilder);
//		System.out.println("------------->" + searchRequest);
//		SearchResponse response = BaseSearchService.search(searchRequest, null);
	}

	
	
}
