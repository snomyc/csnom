package com.snomyc.service.sys.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.snomyc.common.base.service.BaseServiceImpl;
import com.snomyc.service.sys.LotterDrawService;
import com.snomyc.service.sys.bean.LotterDraw;
import com.snomyc.service.sys.dao.LotterDrawDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;

@Service(version="1.0")
public class LotterDrawServiceImpl extends BaseServiceImpl<LotterDraw, String> implements LotterDrawService {

    @Autowired
    private LotterDrawDao lotterDrawDao;

	@Override
	public PagingAndSortingRepository<LotterDraw, String> getDao() {
		return lotterDrawDao;
	}

}


