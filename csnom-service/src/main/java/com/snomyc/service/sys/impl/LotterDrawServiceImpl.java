package com.snomyc.service.sys.impl;

import com.snomyc.common.base.service.BaseServiceImpl;
import com.snomyc.service.sys.LotterDrawService;
import com.snomyc.service.sys.bean.LotterDraw;
import com.snomyc.service.sys.dao.LotterDrawDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

@Service
public class LotterDrawServiceImpl extends BaseServiceImpl<LotterDraw, String> implements LotterDrawService {

    @Autowired
    private LotterDrawDao lotterDrawDao;

	@Override
	public PagingAndSortingRepository<LotterDraw, String> getDao() {
		return lotterDrawDao;
	}

}


