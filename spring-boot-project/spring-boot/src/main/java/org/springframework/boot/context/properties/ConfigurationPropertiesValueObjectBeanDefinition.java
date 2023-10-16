/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.context.properties;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;

/**
 * {@link BeanDefinition} that is used for registering
 * {@link ConfigurationProperties @ConfigurationProperties} value object beans that are
 * bound at creation time.
 *
 * @author Stephane Nicoll
 * @author Madhura Bhave
 * @author Phillip Webb
 */
final class ConfigurationPropertiesValueObjectBeanDefinition extends GenericBeanDefinition {

	private final BeanFactory beanFactory;

	private final String beanName;

	ConfigurationPropertiesValueObjectBeanDefinition(BeanFactory beanFactory, String beanName, Class<?> beanClass) {
		this.beanFactory = beanFactory;
		this.beanName = beanName;
		setBeanClass(beanClass);
		// Spring 5.0 新增的一种实例化 Bean 的一种方式，如果该属性不为空，则使用这个 supplier 实例化 Bean，
		// 不会走通常的构造器推断的方式来实例化 Bean，但是同样会走后置处理器和生命周期回调，可以看作是一种特殊的 FactoryMethod
		setInstanceSupplier(this::createBean);
	}

	/**
	 * {@link org.springframework.boot.context.properties.ConfigurationPropertiesBean.BindMethod#VALUE_OBJECT} 绑定
	 * 类型的 ConfigurationProperties 的 JavaBean 实例化方式
	 */
	private Object createBean() {
		ConfigurationPropertiesBean bean = ConfigurationPropertiesBean.forValueObject(getBeanClass(), this.beanName);
		ConfigurationPropertiesBinder binder = ConfigurationPropertiesBinder.get(this.beanFactory);
		try {
			// 如果是通过构造器注入，那么表示仅有一个构造器，在后续的然后从配置文件中读取 JavaBean 中的属性，
			// 再使用这个唯一的构造器通过反射生成实例，即实例化时，所有的属性都已经注入
			return binder.bindOrCreate(bean);
		}
		catch (Exception ex) {
			throw new ConfigurationPropertiesBindException(bean, ex);
		}
	}

}
