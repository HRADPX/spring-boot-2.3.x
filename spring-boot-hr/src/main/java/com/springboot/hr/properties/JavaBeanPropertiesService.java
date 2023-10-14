package com.springboot.hr.properties;

import org.springframework.stereotype.Service;

/**
 * @author huangran <huangran@kuaishou.com>
 * created on 2022-04-27
 * {@link org.springframework.boot.context.properties.ConfigurationProperties} 注解测试
 * @see JavaBeanProperties 构造器绑定
 * @see JavaBeanPropertiesBySet set 方法绑定
 * @see JavaBeanPropertyBean  Factory Method 绑定
 * @see com.springboot.hr.Application
 * 配置文件: application.yml
 */
@Service
public class JavaBeanPropertiesService {

	private final JavaBeanProperties javaBeanProperties;
	private final JavaBeanPropertyBean javaBeanPropertyBean;
	private final JavaBeanPropertiesBySet javaBeanPropertiesBySet;

	public JavaBeanPropertiesService(JavaBeanProperties javaBeanProperties,
									 JavaBeanPropertyBean javaBeanPropertyBean,
									 JavaBeanPropertiesBySet javaBeanPropertiesBySet) {
		this.javaBeanProperties = javaBeanProperties;
		this.javaBeanPropertyBean = javaBeanPropertyBean;
		this.javaBeanPropertiesBySet = javaBeanPropertiesBySet;
	}

	public void execute() {
		System.out.println("-------------Properties By Constructor---------------");
		System.out.println(javaBeanProperties.isEnable());
		System.out.println(javaBeanProperties.getSecurity().getUsername());
		System.out.println(javaBeanProperties.getSecurity().getPassword());
		System.out.println(javaBeanProperties.getSecurity().getRoles());
		System.out.println("-------------Properties By @Bean---------------");
		System.out.println(javaBeanPropertyBean.getName());
		System.out.println(javaBeanPropertyBean.getUrl());
		System.out.println("-------------Properties By Setter---------------");
		System.out.println(javaBeanPropertiesBySet.isEnable());
		System.out.println(javaBeanPropertiesBySet.getSecurity().getUsername());
		System.out.println(javaBeanPropertiesBySet.getSecurity().getPassword());
		System.out.println(javaBeanPropertiesBySet.getSecurity().getRoles());
	}
}
