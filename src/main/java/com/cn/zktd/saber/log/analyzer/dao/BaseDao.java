package com.cn.zktd.saber.log.analyzer.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;

@NoRepositoryBean
public interface BaseDao<T, pk extends Serializable> extends PagingAndSortingRepository<T, Serializable>,
        JpaSpecificationExecutor<T> {

}
