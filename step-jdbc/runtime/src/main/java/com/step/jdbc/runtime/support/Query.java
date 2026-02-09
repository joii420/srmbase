/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package com.step.jdbc.runtime.support;


import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 分页工具
 *
 * @author Chill
 */
@Accessors(chain = true)
public class Query {

	/**
	 * 当前页
	 */
	private Integer current;

	/**
	 * 每页的数量
	 */
	private Integer size;

	/**
	 * 正排序规则
	 */
	private String ascs;

	/**
	 * 倒排序规则
	 */
	private String descs;

	public Integer getCurrent() {
		return current;
	}

	public void setCurrent(Integer current) {
		this.current = current;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getAscs() {
		return ascs;
	}

	public void setAscs(String ascs) {
		this.ascs = ascs;
	}

	public String getDescs() {
		return descs;
	}

	public void setDescs(String descs) {
		this.descs = descs;
	}
}
