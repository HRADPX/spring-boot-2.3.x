package com.springboot.hr.bean;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author huangran <huangran@kuaishou.com>
 * created on 2022-04-26
 */
public class PropertySourceBean {

	@Value("${name}")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
