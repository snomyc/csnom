package com.snomyc.api.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.snomyc.common.base.domain.ResponseConstant;
import com.snomyc.common.base.domain.ResponseEntity;
import com.snomyc.common.util.face.FaceReq;
import com.snomyc.service.sys.LotterDrawService;
import com.snomyc.service.sys.bean.LotterDraw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
@Api(value = "公共接口", tags = "公共接口")
@RestController
@RequestMapping("/api/common")
public class CommonApiController {
	
	@Autowired
	private LotterDrawService lotterDrawService;

	@ApiOperation(value = "识别图片信息",httpMethod = "POST")  
	@RequestMapping(value = "/discernPicture", method = RequestMethod.POST)
	public ResponseEntity discernPicture(@ApiParam(required = true, name = "fileName", value = "图片存放 服务器绝对地址") @RequestParam(name = "fileName",required = true) String fileName) {
		ResponseEntity responseEntity = new ResponseEntity();
		try {
			org.json.JSONObject words = FaceReq.basicAccurateGeneral(fileName, new HashMap<String, String>());
			Map<String,Object> data = new HashMap<String,Object>();
			data.put("words", words);
			responseEntity.success(data,"成功!");
		} catch (Exception e) {
			responseEntity.failure(ResponseConstant.CODE_500, "接口调用异常");
		}
		return responseEntity;
	}
	
	
	@ApiOperation(value = "返回抽奖结果名单",httpMethod = "POST")  
	@RequestMapping(value = "/lotteryDraw", method = RequestMethod.POST)
	public ResponseEntity lotteryDraw() {
		ResponseEntity responseEntity = new ResponseEntity();
		try {
//			for (int i =1; i<=100; i++) {
//				LotterDraw lotterDraw = new LotterDraw();
//				lotterDraw.setNum(i);
//				lotterDraw.setUserName("中奖用户"+i);
//				lotterDraw.setMobile("188******"+i);
//				lotterDrawService.save(lotterDraw);
//			}
			
			List<LotterDraw> list = lotterDrawService.findAll();
			//String data = "[{\"id\":\"1\",\"name\":\"张三\",\"tel\":\"152****1253\"},{\"id\":\"2\",\"name\":\"李四\",\"tel\":\"152****1253\"},{\"id\":\"3\",\"name\":\"王大麻子\",\"tel\":\"152****1253\"},{\"id\":\"4\",\"name\":\"阿拉\",\"tel\":\"152****1253\"},{\"id\":\"5\",\"name\":\"活动时间\",\"tel\":\"152****1253\"},{\"id\":\"6\",\"name\":\"分阿萨德\",\"tel\":\"152****1253\"},{\"id\":\"7\",\"name\":\"按时啊\",\"tel\":\"152****1253\"},{\"id\":\"8\",\"name\":\"富商大贾\",\"tel\":\"152****1253\"},{\"id\":\"9\",\"name\":\"分公司\",\"tel\":\"152****1253\"},{\"id\":\"10\",\"name\":\"个双方各\",\"tel\":\"152****1253\"},{\"id\":\"11\",\"name\":\"张是梵蒂冈三\",\"tel\":\"152****1253\"},{\"id\":\"12\",\"name\":\"双方各\",\"tel\":\"152****1253\"},{\"id\":\"13\",\"name\":\"是是梵蒂冈\",\"tel\":\"152****1253\"},{\"id\":\"14\",\"name\":\"对方感受到\",\"tel\":\"152****1253\"},{\"id\":\"15\",\"name\":\"的是干啥的\",\"tel\":\"152****1253\"},{\"id\":\"16\",\"name\":\"十多个发送到个\",\"tel\":\"152****1253\"},{\"id\":\"17\",\"name\":\"的双方各\",\"tel\":\"152****1253\"},{\"id\":\"18\",\"name\":\"换个\",\"tel\":\"152****1253\"},{\"id\":\"19\",\"name\":\"就梵蒂冈和\",\"tel\":\"152****1253\"},{\"id\":\"20\",\"name\":\"东方红\",\"tel\":\"152****1253\"},{\"id\":\"21\",\"name\":\"张和豆腐干三\",\"tel\":\"152****1253\"},{\"id\":\"22\",\"name\":\"大概\",\"tel\":\"152****1253\"},{\"id\":\"23\",\"name\":\"烦得很\",\"tel\":\"152****1253\"},{\"id\":\"24\",\"name\":\"梵蒂冈\",\"tel\":\"152****1253\"},{\"id\":\"25\",\"name\":\"电饭锅和\",\"tel\":\"152****1253\"},{\"id\":\"26\",\"name\":\"个\",\"tel\":\"152****1253\"},{\"id\":\"27\",\"name\":\"订个婚\",\"tel\":\"152****1253\"},{\"id\":\"28\",\"name\":\"东方红\",\"tel\":\"152****1253\"},{\"id\":\"29\",\"name\":\"电饭锅和\",\"tel\":\"152****1253\"},{\"id\":\"30\",\"name\":\"好\",\"tel\":\"152****1253\"},{\"id\":\"31\",\"name\":\"好的\",\"tel\":\"152****1253\"},{\"id\":\"32\",\"name\":\"额特\",\"tel\":\"152****1253\"},{\"id\":\"33\",\"name\":\"热水\",\"tel\":\"152****1253\"},{\"id\":\"34\",\"name\":\"刚刚\",\"tel\":\"152****1253\"},{\"id\":\"35\",\"name\":\"房间号\",\"tel\":\"152****1253\"},{\"id\":\"36\",\"name\":\"发个说的\",\"tel\":\"152****1253\"},{\"id\":\"37\",\"name\":\"风格的\",\"tel\":\"152****1253\"},{\"id\":\"38\",\"name\":\"是大法官\",\"tel\":\"152****1253\"},{\"id\":\"39\",\"name\":\"是否\",\"tel\":\"152****1253\"},{\"id\":\"40\",\"name\":\"十多个发送到个\",\"tel\":\"152****1253\"},{\"id\":\"41\",\"name\":\"刚刚\",\"tel\":\"152****1253\"},{\"id\":\"42\",\"name\":\"萨芬的\",\"tel\":\"152****1253\"},{\"id\":\"43\",\"name\":\"张三\",\"tel\":\"152****1253\"},{\"id\":\"44\",\"name\":\"发个\",\"tel\":\"152****1253\"},{\"id\":\"45\",\"name\":\"萨芬的\",\"tel\":\"152****1253\"},{\"id\":\"46\",\"name\":\"双方各\",\"tel\":\"152****1253\"},{\"id\":\"47\",\"name\":\"是大法官\",\"tel\":\"152****1253\"},{\"id\":\"48\",\"name\":\"萨芬的\",\"tel\":\"152****1253\"},{\"id\":\"49\",\"name\":\"是大法官\",\"tel\":\"152****1253\"},{\"id\":\"50\",\"name\":\"是爱的说法\",\"tel\":\"152****1253\"},{\"id\":\"51\",\"name\":\"阿斯蒂芬\",\"tel\":\"152****1253\"},{\"id\":\"52\",\"name\":\"第三帝国\",\"tel\":\"152****1253\"},{\"id\":\"53\",\"name\":\"是大法官\",\"tel\":\"152****1253\"},{\"id\":\"54\",\"name\":\"是大法官\",\"tel\":\"152****1253\"},{\"id\":\"55\",\"name\":\"张爱的方式\",\"tel\":\"152****1253\"},{\"id\":\"56\",\"name\":\"个发多少\",\"tel\":\"152****1253\"},{\"id\":\"57\",\"name\":\"大概\",\"tel\":\"152****1253\"},{\"id\":\"58\",\"name\":\"电饭锅和\",\"tel\":\"152****1253\"},{\"id\":\"59\",\"name\":\"订个婚\",\"tel\":\"152****1253\"},{\"id\":\"60\",\"name\":\"改好\",\"tel\":\"152****1253\"},{\"id\":\"61\",\"name\":\"电饭锅和\",\"tel\":\"152****1253\"},{\"id\":\"62\",\"name\":\"订个婚\",\"tel\":\"152****1253\"},{\"id\":\"63\",\"name\":\"大概\",\"tel\":\"152****1253\"},{\"id\":\"64\",\"name\":\"改好\",\"tel\":\"152****1253\"},{\"id\":\"65\",\"name\":\"电饭锅和\",\"tel\":\"152****1253\"},{\"id\":\"66\",\"name\":\"大地飞歌\",\"tel\":\"152****1253\"},{\"id\":\"67\",\"name\":\"好\",\"tel\":\"152****1253\"},{\"id\":\"68\",\"name\":\"萨芬的\",\"tel\":\"152****1253\"},{\"id\":\"69\",\"name\":\"发个\",\"tel\":\"152****1253\"},{\"id\":\"70\",\"name\":\"是梵蒂冈\",\"tel\":\"152****1253\"},{\"id\":\"71\",\"name\":\"撒旦法\",\"tel\":\"152****1253\"},{\"id\":\"72\",\"name\":\"是梵蒂冈\",\"tel\":\"152****1253\"}]";
			responseEntity.success(list, "成功");
		} catch (Exception e) {
			responseEntity.failure(ResponseConstant.CODE_500, "接口调用异常");
		}
		return responseEntity;
	}
}
