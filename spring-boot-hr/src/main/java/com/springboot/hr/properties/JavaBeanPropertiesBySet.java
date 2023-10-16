package com.springboot.hr.properties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author huangran <huangran@kuaishou.com>
 * created on 2022-05-09
 *
 * 通过 set 方法绑定属性，构造器绑定 {@link JavaBeanProperties}, 配置文件: application.yml
 * @see com.springboot.hr.Application
 */
@ConfigurationProperties(prefix = "spring.boot.service.setter")
public class JavaBeanPropertiesBySet {

	private boolean enable;

	private JavaBeanProperties.DataInfo security;

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public JavaBeanProperties.DataInfo getSecurity() {
		return security;
	}

	public void setSecurity(JavaBeanProperties.DataInfo security) {
		this.security = security;
	}

	public static class DataInfo {
		private String username;

		private String password;

		private List<String> roles = new ArrayList<>(Collections.singletonList("USER"));

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public List<String> getRoles() {
			return roles;
		}

		public void setRoles(List<String> roles) {
			this.roles = roles;
		}
	}
}
