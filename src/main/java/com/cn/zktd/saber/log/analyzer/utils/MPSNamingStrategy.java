package com.cn.zktd.saber.log.analyzer.utils;

import org.hibernate.cfg.ImprovedNamingStrategy;

public class MPSNamingStrategy extends ImprovedNamingStrategy {
	private static final long serialVersionUID = 1978036015779975728L;

	@Override 
	public String tableName(String tableName) { 
		return tableName; 
	} 
}
