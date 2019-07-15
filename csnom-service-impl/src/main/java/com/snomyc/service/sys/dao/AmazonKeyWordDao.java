package com.snomyc.service.sys.dao;

import com.snomyc.service.sys.bean.AmazonKeyWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AmazonKeyWordDao extends JpaRepository<AmazonKeyWord, String> {

	public List<AmazonKeyWord> findByKeyWordRoot(String keyWordRoot);
	
	public int countByKeyWordRootAndKeyWordSecond(String keyWordRoot, String keyWordSecond);
	
}