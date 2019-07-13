package com.snomyc.service.sys.dao;

import com.snomyc.service.sys.bean.SysConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SysConfigDao extends JpaRepository<SysConfig, String> {

	@Query(value="select param_val from sys_config where param_code = ?1", nativeQuery = true)
	public String findParamValByCode(String code);
}
