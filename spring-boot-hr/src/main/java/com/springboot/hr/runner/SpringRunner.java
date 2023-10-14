package com.springboot.hr.runner;

import com.springboot.hr.bean.ProfileBean;
import com.springboot.hr.bean.PropertySourceBean;
import com.springboot.hr.properties.JavaBeanPropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * ApplicationRunner 和 CommandLineRunner 都会在 Spring 容器初始化完毕后执行.
 * 这两个接口是调试源码很好的选择，无需写 Controller
 */
@Component
public class SpringRunner implements ApplicationRunner {


	private ProfileBean profileBean;
	private PropertySourceBean propertySourceBean;
	private JavaBeanPropertiesService javaBeanPropertiesService;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		System.out.println("application runner run.......");
		System.out.println(propertySourceBean.getName());
		System.out.println(profileBean.getName());
		javaBeanPropertiesService.execute();
	}

	@Autowired
	public void setPropertySourceBean(PropertySourceBean propertySourceBean) {
		this.propertySourceBean = propertySourceBean;
	}

	@Autowired
	public void setProfileBean(ProfileBean profileBean) {
		this.profileBean = profileBean;
	}

	@Autowired
	public void setJavaBeanPropertiesService(JavaBeanPropertiesService javaBeanPropertiesService) {
		this.javaBeanPropertiesService = javaBeanPropertiesService;
	}
}
