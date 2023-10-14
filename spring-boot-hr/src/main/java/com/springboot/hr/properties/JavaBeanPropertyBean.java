package com.springboot.hr.properties;

import com.springboot.hr.Application;

/**
 * @author huangran <huangran@kuaishou.com>
 * created on 2022-04-27
 *
 * 通过 {@link org.springframework.context.annotation.Bean} 方法绑定, 配置文件: application.yml
 * @see Application#javaBeanPropertyBean()
 */
public class JavaBeanPropertyBean {

	private String name;
	private String url;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
