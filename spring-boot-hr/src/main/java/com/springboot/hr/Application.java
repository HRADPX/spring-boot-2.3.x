package com.springboot.hr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.springboot.hr.bean.ProfileBean;
import com.springboot.hr.bean.PropertySourceBean;
import com.springboot.hr.properties.JavaBeanProperties;
import com.springboot.hr.properties.JavaBeanPropertiesBySet;
import com.springboot.hr.properties.JavaBeanPropertyBean;

@SpringBootApplication
@EnableConfigurationProperties(value = {JavaBeanProperties.class, JavaBeanPropertiesBySet.class})
@PropertySource("classpath:spring.properties")
public class Application {

	@Autowired
	private Environment environment;

	@Bean
	public PropertySourceBean propertySourceBean() {

		PropertySourceBean propertyBean = new PropertySourceBean();
		System.out.println("before:" + propertyBean.getName());
		propertyBean.setName(environment.getProperty("property.source.bean.name"));
		System.out.println("after:" + propertyBean.getName());
		return propertyBean;
	}

	@Bean
	@Profile("prod")
	public ProfileBean profileProdBean() {
		return new ProfileBean();
	}

	@Bean
	@Profile("dev")
	public ProfileBean profileDevBean() {
		return new ProfileBean();
	}

	/**
	 * 在 @Bean 注解的方法上使用 @ConfigurationProperties
	 */
	@Bean
	@ConfigurationProperties(prefix = "bean")
	public JavaBeanPropertyBean javaBeanPropertyBean() {
		return new JavaBeanPropertyBean();
	}


//	@Bean
//	public DataSource dataSource(){
//		DriverManagerDataSource dataSource = new DriverManagerDataSource();
//		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
//		dataSource.setUrl("jdbc:mysql://localhost:3306/info");
//		dataSource.setUsername("root");
//		dataSource.setPassword("1234");
//		return dataSource;
//	}

	public static void main(String[] args){
		SpringApplication.run(Application.class, args);
	}

}
