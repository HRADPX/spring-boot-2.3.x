package com.springboot.hr.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author huangran <huangran@kuaishou.com>
 * created on 2022-04-26
 */
@Service
public class BootService {

	@Value("${name}")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
