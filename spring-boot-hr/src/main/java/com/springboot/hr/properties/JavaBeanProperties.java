package com.springboot.hr.properties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * @author huangran <huangran@kuaishou.com>
 * created on 2022-04-27
 * JavaBeanProperty 属性注入
 * 	注解 @ConstructorBinding 可以根据构造器给该 JavaBean 注入属性，属性的值从配置文件中读取
 * 	（本例是 classpath: application.yml）必须配合 @EnableConfigurationProperties 注解
 * 	或者 @ConfigurationPropertiesScan 使用，并且注入的不能是 Spring 的 Bean.
 * 	Note: 如果 JavaBean 有多个构造器，则直接在要注入的构造器上加 @ConstructorBinding
 * @see com.springboot.hr.Application
 *
 * 本例通过构造器绑定绑定属性，set 方法绑定见  {@link JavaBeanPropertiesBySet}, 配置文件: application.yml
 */
@ConstructorBinding
@ConfigurationProperties(prefix = "spring.boot.service")
public class JavaBeanProperties {

	private boolean enable;

	private DataInfo security;

	public JavaBeanProperties(boolean enable, @DefaultValue DataInfo security) {
		this.enable = enable;
		this.security = security;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public DataInfo getSecurity() {
		return security;
	}

	public void setSecurity(DataInfo security) {
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
