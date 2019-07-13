package com.snomyc.service.sys;

import com.snomyc.common.base.service.BaseService;
import com.snomyc.service.sys.bean.SysConfig;

public interface SysConfigService extends BaseService<SysConfig, String> {

	public String findParamValByCode(String code);
}
