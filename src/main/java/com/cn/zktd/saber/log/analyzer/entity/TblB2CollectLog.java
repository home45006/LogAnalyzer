package com.cn.zktd.saber.log.analyzer.entity;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "tbl_b2_collect_log")
// 默认的缓存策略.
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TblB2CollectLog implements Serializable {
	private static final long serialVersionUID = 2387266807912058861L;

	@Id
	@GenericGenerator(name = "idGenerator", strategy = "uuid")
	@GeneratedValue(generator = "idGenerator")
	private String id;

	@Column
	private String runTime;

	@Column
	private String protocol;

	@Column
	private String sendReceive;

	@Column
	private String sessionId;

	@Column
	private String roomId;

	@Column
	private String userName;

	@Column
	private String seq;

	@Column
	private String knxAddr;

	@Column
	private String value;


	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
