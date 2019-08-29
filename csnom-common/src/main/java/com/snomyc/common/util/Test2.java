package com.snomyc.common.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import com.alibaba.fastjson.JSON;
public class Test2 {

	public static void main(String[] args) throws Exception {
//		List<Map<String,Object>> userList = new ArrayList<Map<String,Object>>();
//		Map<String,Object> map = new HashMap<String,Object>();
//		map.put("id", 1);
//		map.put("name", 1);
//		
//		userList.add(map);
//		Map<String,Object> map1 = new HashMap<String,Object>();
//		map1.put("id", 2);
//		map1.put("name", 2);
//		userList.add(map1);
//        Map<Object, Object> userInfoMap = userList.stream().collect(Collectors.toMap(n -> n.get("id"),n -> n));
//        System.out.println(userInfoMap);
		
		//String url = "https://t.sharingschool.com/wxapi/user/getLoginType";
		//String url = "https://t.questyes.com/wxapi/user/getLoginType";
//	    RestTemplate rest = new RestTemplate();
//	    MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
//	    param.add("validateTime", "{\"validateTime\":\"2018-05-30\"}");
//	    String string = rest.postForObject(url, param, String.class);
//	    System.out.println(string);
	    
	    
      //String url = "http://localhost:8080/sharingschool/wxapi/user/appUserlogin";
	  //String url = "https://t.questyes.com/wxapi/user/getLoginType";
//	  String url = "https://t.sharingschool.com/wxapi/user/getLoginType";
//      Map<String, Object> map = new HashMap<String, Object>();
//	  map.put("validateTime", "2018-05-30");
//      HttpHeaders headers = new HttpHeaders();
//      headers.setContentType(MediaType.valueOf("application/json;UTF-8"));
//      HttpEntity<String> strEntity = new HttpEntity<String>(JSON.toJSONString(map),headers);
//      RestTemplate restTemplate = new RestTemplate();
//      
//      String result = restTemplate.postForObject(url,strEntity,String.class);
//      System.out.println(result);
      
      String url = "http://localhost:8080/api/amazon/exportUrl";
      System.out.println(url.substring(0, url.indexOf("/",7)));

      String apiUrl = "/api/common/lotteryDraw";
      boolean flag = !StringUtils.startsWith(apiUrl, "/api/");
      System.out.println(flag);

      System.out.println((double) 920552/(1000*60));
	}
}
