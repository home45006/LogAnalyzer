package com.cn.zktd.saber.log.analyzer.service.impl;

import com.cn.zktd.saber.log.analyzer.dao.B2LogDao;
import com.cn.zktd.saber.log.analyzer.entity.TblB2CollectLog;
import com.cn.zktd.saber.log.analyzer.service.B2LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("b2LogService")
@Transactional
public class B2LogServiceImpl extends EntityServiceImpl<TblB2CollectLog, Long, B2LogDao> implements B2LogService {

	@Override
	@Autowired
	public void setEntityDao(B2LogDao entityDao) {
		this.entityDao = entityDao;
	}

}
