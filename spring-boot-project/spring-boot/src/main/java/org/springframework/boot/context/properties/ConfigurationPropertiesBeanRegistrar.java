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
import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.ConfigurationPropertiesBean.BindMethod;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.annotation.MergedAnnotations.SearchStrategy;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Delegate used by {@link EnableConfigurationPropertiesRegistrar} and
 * {@link ConfigurationPropertiesScanRegistrar} to register a bean definition for a
 * {@link ConfigurationProperties @ConfigurationProperties} class.
 *
 * @author Madhura Bhave
 * @author Phillip Webb
 */
final class ConfigurationPropertiesBeanRegistrar {

	private final BeanDefinitionRegistry registry;

	private final BeanFactory beanFactory;

	ConfigurationPropertiesBeanRegistrar(BeanDefinitionRegistry registry) {
		this.registry = registry;
		this.beanFactory = (BeanFactory) this.registry;
	}

	void register(Class<?> type) {
		// 获取 @ConfigurationProperties 注解
		MergedAnnotation<ConfigurationProperties> annotation = MergedAnnotations
				.from(type, SearchStrategy.TYPE_HIERARCHY).get(ConfigurationProperties.class);
		// 注册
		register(type, annotation);
	}

	void register(Class<?> type, MergedAnnotation<ConfigurationProperties> annotation) {
		// name = prefix + className
		String name = getName(type, annotation);
		if (!containsBeanDefinition(name)) {
			registerBeanDefinition(name, type, annotation);
		}
	}

	private String getName(Class<?> type, MergedAnnotation<ConfigurationProperties> annotation) {
		String prefix = annotation.isPresent() ? annotation.getString("prefix") : "";
		return (StringUtils.hasText(prefix) ? prefix + "-" + type.getName() : type.getName());
	}

	private boolean containsBeanDefinition(String name) {
		return containsBeanDefinition(this.beanFactory, name);
	}

	private boolean containsBeanDefinition(BeanFactory beanFactory, String name) {
		if (beanFactory instanceof ListableBeanFactory
				&& ((ListableBeanFactory) beanFactory).containsBeanDefinition(name)) {
			return true;
		}
		if (beanFactory instanceof HierarchicalBeanFactory) {
			return containsBeanDefinition(((HierarchicalBeanFactory) beanFactory).getParentBeanFactory(), name);
		}
		return false;
	}

	private void registerBeanDefinition(String beanName, Class<?> type,
			MergedAnnotation<ConfigurationProperties> annotation) {
		Assert.state(annotation.isPresent(), () -> "No " + ConfigurationProperties.class.getSimpleName()
				+ " annotation found on  '" + type.getName() + "'.");
		this.registry.registerBeanDefinition(beanName, createBeanDefinition(beanName, type));
	}

	/**
	 * 1) 如果是使用构造器绑定属性，则通过 instanceSupplier 来创建 Bean，即从配置文件中读取属性，则反射调用构造器生成实例，
	 * 最后在绑定到 PropertySource 中，这种方式实例化的 ConfigurationProperty 在
	 * 2) 如果是使用其他方式绑定属性，则是通过常规的方式实例化 Bean，然后在 ConfigurationPropertiesBindingPostProcessor
	 * 这个后置处理器中将其绑定到 PropertySource 中。
	 * @see ConfigurationPropertiesBindingPostProcessor#postProcessBeforeInitialization
	 */
	private BeanDefinition createBeanDefinition(String beanName, Class<?> type) {
		// 判断绑定类型，当满足下面任意一个条件时，则是 VALUE_OBJECT 类型的绑定方式：
		// 1) 构造器上有 @ConstructorBinding 注解时
		// 2) 类上有 @ConstructorBinding 注解且仅有一个带参构造器
		if (BindMethod.forType(type) == BindMethod.VALUE_OBJECT) {
			// 这个 BeanDefinition 的构造方法里使用了 Spring 5.0 的新增的一种实例化 Bean 的方法，它给
			// org.springframework.beans.factory.support.AbstractBeanDefinition#instanceSupplier
			// 设置了创建 Bean 的方式， Spring 实例化 Bean 时优先使用这个字段创建 Bean 的方式。
			return new ConfigurationPropertiesValueObjectBeanDefinition(this.beanFactory, beanName, type);
		}
		GenericBeanDefinition definition = new GenericBeanDefinition();
		definition.setBeanClass(type);
		return definition;
	}

}
