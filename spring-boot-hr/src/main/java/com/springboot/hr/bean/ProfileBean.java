package com.springboot.hr.bean;

import com.springboot.hr.Application;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author huangran <huangran@kuaishou.com>
 * created on 2022-04-27
 * @see Application#profileProdBean()
 * @see Application#profileDevBean()
 * Spring 提供 @Profile 注解，只有 spring.profile.active 属性的值和注解的值匹配时，
 * 才会加载对应的 Bean
 */
public class ProfileBean {

	@Value("${application.env}")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
