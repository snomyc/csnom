package com.snomyc.service.api;

import com.alibaba.dubbo.config.annotation.Reference;
import com.snomyc.common.base.domain.ResponseConstant;
import com.snomyc.common.base.domain.ResponseEntity;
import com.snomyc.common.base.redis.JedisPoolUtil;
import com.snomyc.service.sys.UserService;
import com.snomyc.service.sys.bean.User;
import com.snomyc.service.sys.request.UserAddRequest;
import com.snomyc.service.sys.request.UserEditRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Api(value = "用户接口", tags = "用户接口")
@RestController
@RequestMapping("/api/user")
public class UserApiController {
	private Logger logger = LoggerFactory.getLogger("AmazonSystem");

	@Reference(version = "1.0" ,timeout = 15000)
	private UserService userService;

	@Autowired
	private JedisPool jedisPool;

	@Autowired
	private JedisPoolUtil jedisPoolUtil;

	@ApiOperation(value = "返回用户列表",httpMethod = "POST",notes = "app升级推送所有设备")
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public ResponseEntity list() {
		ResponseEntity responseEntity = new ResponseEntity();
		try {
			List<User> users = userService.findAll();
			List<String> userNames = new ArrayList<>();
			for (User user:users) {
				userNames.add(user.getUserName());
			}
			userService.updateAge(20,userNames);
			responseEntity.success(users, "调用成功");
		} catch (Exception e) {
			logger.error(e.getMessage());
			responseEntity.failure(ResponseConstant.CODE_500, "接口调用异常");
		}
		return responseEntity;
	}

	@ApiOperation(value = "查询用户",httpMethod = "POST",notes = "查询用户")
	@RequestMapping(value = "/selectOne", method = RequestMethod.POST)
	public ResponseEntity selectOne(@ApiParam(required = true, name = "userName", value = "用户名") @RequestParam(name = "userName",required = true) String userName) {
		ResponseEntity responseEntity = new ResponseEntity();
		try {
			List<Map<String,Object>> users = userService.findByUserName(userName);
			responseEntity.success(users, "调用成功");
		} catch (Exception e) {
			logger.error(e.getMessage());
			responseEntity.failure(ResponseConstant.CODE_500, "接口调用异常");
		}
		return responseEntity;
	}

	@ApiOperation(value = "添加用户",httpMethod = "POST")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity add(@RequestBody UserAddRequest request) {
		ResponseEntity responseEntity = new ResponseEntity();
		try {
			User user = new User();
			user.setUserName(request.getUserName());
			user.setPassword(request.getPassword());
			user.setAge(request.getAge());
			userService.save(user);
			responseEntity.success();
		} catch (Exception e) {
			responseEntity.failure(ResponseConstant.CODE_500, "接口调用异常");
		}
		return responseEntity;
	}

	@ApiOperation(value = "编辑用户",httpMethod = "POST")
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ResponseEntity edit(@RequestBody UserEditRequest request) {
		ResponseEntity responseEntity = new ResponseEntity();
		try {
            userService.testTranSactional(request);
			//userService.save(user);
			responseEntity.success();
		} catch (Exception e) {
			responseEntity.failure(ResponseConstant.CODE_500, "接口调用异常");
		}
		return responseEntity;
	}

	@ApiOperation(value = "删除用户",httpMethod = "POST")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity delete(@ApiParam(required = true, name = "id", value = "用户id") @RequestParam(name = "id",required = true) String id) {
		ResponseEntity responseEntity = new ResponseEntity();
		try {
			userService.delete(id);
			responseEntity.success();
		} catch (Exception e) {
			responseEntity.failure(ResponseConstant.CODE_500, "接口调用异常");
		}
		return responseEntity;
	}

	@ApiOperation(value = "测试缓存",httpMethod = "POST")
	@RequestMapping(value = "/redisKey", method = RequestMethod.POST)
	public ResponseEntity redisKey(@ApiParam(required = true, name = "key", value = "reids key") @RequestParam(name = "key",required = true) String key) {
		ResponseEntity responseEntity = new ResponseEntity();
		try {
			try (Jedis jedis = jedisPool.getResource()) {
	             jedis.setex(key, 3600, "wtf");
	        }
			jedisPoolUtil.setex(key+2, "wtf",3600);
			responseEntity.success();
		} catch (Exception e) {
			responseEntity.failure(ResponseConstant.CODE_500, "接口调用异常");
		}
		return responseEntity;
	}

}
