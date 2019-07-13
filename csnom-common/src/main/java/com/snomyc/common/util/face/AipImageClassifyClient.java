package com.snomyc.common.util.face;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONObject;

import com.baidu.aip.client.BaseClient;
import com.baidu.aip.error.AipError;
import com.baidu.aip.http.AipRequest;
import com.baidu.aip.util.Base64Util;
import com.baidu.aip.util.Util;

/** 
 * @author  ZhangCaibao 
 * @version 2018年5月15日 上午11:52:59 
 * 类说明 
 */
public class AipImageClassifyClient extends BaseClient{

	public AipImageClassifyClient(String appId, String apiKey, String secretKey)
	  {
	    super(appId, apiKey, secretKey);
	  }
	 public JSONObject advancedGeneral(byte[] image, HashMap<String, String> options)
	  {
	    AipRequest request = new AipRequest();
	    preOperation(request);

	    String base64Content = Base64Util.encode(image);
	    request.addBody("image", base64Content);
	    if (options != null) {
	      request.addBody(options);
	    }
	    request.setUri("https://aip.baidubce.com/rest/2.0/image-classify/v2/advanced_general");
	    postOperation(request);
	    return requestServer(request);
	  }

	  public JSONObject advancedGeneral(String image, HashMap<String, String> options)
	  {
	    try
	    {
	      byte[] data = Util.readFileByBytes(image);
	      return advancedGeneral(data, options);
	    } catch (IOException e) {
	      e.printStackTrace();
	    }return AipError.IMAGE_READ_ERROR.toJsonResult();
	  }
}
