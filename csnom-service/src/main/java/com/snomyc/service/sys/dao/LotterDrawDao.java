package com.snomyc.service.sys.dao;

import com.snomyc.service.sys.bean.LotterDraw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LotterDrawDao extends JpaRepository<LotterDraw, String> {
}