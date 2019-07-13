package com.snomyc.service.sys.impl;

import com.snomyc.common.base.service.BaseServiceImpl;
import com.snomyc.service.sys.SysConfigService;
import com.snomyc.service.sys.bean.SysConfig;
import com.snomyc.service.sys.dao.SysConfigDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
@Service
public class SysConfigServiceImpl extends BaseServiceImpl<SysConfig, String> implements SysConfigService {

    @Autowired
    private SysConfigDao sysConfigDao;
    
    @Override
	public PagingAndSortingRepository<SysConfig, String> getDao() {
		return sysConfigDao;
	}

	@Override
	public String findParamValByCode(String code) {
		return sysConfigDao.findParamValByCode(code);
	}

	
}


