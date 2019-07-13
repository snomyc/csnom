package com.snomyc.common.util.face;

import java.util.Arrays;
import java.util.HashMap;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.aip.face.AipFace;
import com.baidu.aip.nlp.AipNlp;
import com.baidu.aip.ocr.AipOcr;

public class FaceReq {
	  protected static Logger logger = LoggerFactory.getLogger(FaceReq.class);
	private final static String GroupFace="group1";
	
	private static AipFace face;
	
	private static AipOcr orc;
	
	private static AipNlp nlp;
	private static AipImageClassifyClient image;
	
	
	static{
		face=new AipFace("10221631", "lMkyQYcDoCBgbHw0YGnvFh3l", "MlaRSG5odlgHsgOMf7G7e7hoUp29Ymrv");//人脸识别
		//orc=new AipOcr("10582041", "LY2s8OFRsCXrPPMzIRHrNhq7", "BjLGICGFqQnxSaqfAoAtXggp4qd4OFiQ");//文字
		orc=new AipOcr("16270967", "2KDBhiY2lOKyd4QQr5DWQgZn", "HYbVewfajlac21FmZwbmkPSdytcIiqmm");//文字
		nlp=new AipNlp("10582315","j0438Yllt3qNGAgOtKDgYTBG","j0uQnzZsTtyAusrzlYrOEdF0G9gHwTKh");//词法分析
		image=new AipImageClassifyClient("11186720","iOjkzcxGuFL5linrnRifZOOS","LNplcaooZZ8hNNG5xaQzD87785c36vi3");
	}
	
	//添加用户到group_face中
	public static JSONObject addUser(String uid,String user_info,String img,HashMap<String, String> options){
		options.put("user_top_num", "1");
		JSONObject req=face.addUser(uid,user_info,  Arrays.asList(GroupFace),img,options);
		return req;
	}
	
	//人脸识别--group_face
	public static JSONObject identifyUser(String img,HashMap<String, Object> options){
		JSONObject req=face.identifyUser(Arrays.asList(GroupFace),img, options);
		return req;
	}
	
	//通用文字识别（高精度版）
	public static JSONObject basicAccurateGeneral(String img,HashMap<String, String> options){
		JSONObject req=orc.basicAccurateGeneral(img, options);
		return req;
	}
	
	//通用文字识别（高精度版）文件流
	public static JSONObject basicAccurateGeneral(byte[] imgData,HashMap<String, String> options){
		JSONObject req=orc.basicAccurateGeneral(imgData, options);
		return req;
	}
	
	public static JSONObject advancedGeneral(String img,HashMap<String, String> options){
		String savePath = "/www/sss/111.jpg";
		 img = savePath + img;
		 logger.info("通用图像分析——通用物体和场景识别"+img);
		JSONObject req=image.advancedGeneral(img, options);
		logger.info("通用图像分析——通用物体和场景识别返回"+req);
		return req;
	}

	//情感倾向分析
	/**
	 * {
     *  "sentiment":2,    //表示情感极性分类结果
     *   "confidence":0.40, //表示分类的置信度
     *   "positive_prob":0.73, //表示属于积极类别的概率
     *   "negative_prob":0.27  //表示属于消极类别的概率
     *  }
	 */
	public static JSONObject sentimentClassify(String text){
		JSONObject req=nlp.sentimentClassify(text);
		return req;
	}
	
	public static void main(String[] args) {
		String img = "E://123.PNG";
		long start = System.currentTimeMillis();
		org.json.JSONObject words = FaceReq.basicAccurateGeneral(img, new HashMap<String, String>());
		long end = System.currentTimeMillis();
		System.out.println("总耗时:"+(end-start));
		System.out.println(words);
	}
	

}
