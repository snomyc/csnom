package com.snomyc.service.sys;

import com.snomyc.bean.SysConfig;
import com.snomyc.common.base.service.BaseService;

public interface SysConfigService extends BaseService<SysConfig, String> {

	public String findParamValByCode(String code);
}
