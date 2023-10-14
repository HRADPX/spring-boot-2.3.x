/*
 * Copyright 2012-2020 the original author or authors.
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

package org.springframework.boot.context.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.LivenessState;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.ErrorHandler;

/**
 * {@link SpringApplicationRunListener} to publish {@link SpringApplicationEvent}s.
 * <p>
 * Uses an internal {@link ApplicationEventMulticaster} for the events that are fired
 * before the context is actually refreshed.
 *
 * @author Phillip Webb
 * @author Stephane Nicoll
 * @author Andy Wilkinson
 * @author Artsiom Yudovin
 * @author Brian Clozel
 * @since 1.0.0
 * 	该监听器用于监听 {@link SpringApplication#run(String...)} 事件，即 SpringBoot 容器启动，它内部维护了一个事件广播(多播)器
 * 	{@link EventPublishingRunListener#initialMulticaster }，SpringBoot 会将所有 classpath 下 spring.factories 配置文件
 * 	中的所有配置的 ApplicationListener 添加到这个事件多播器中，在 SpringBoot启动的各个阶段，如启动开始阶段 {@link #starting()}、
 * 	容器环境就绪阶段 {@link #environmentPrepared(ConfigurableEnvironment)}，发布对应的事件，订阅该事件类型的事件监听器就会执行
 * 	相应的逻辑， SpringBoot 中很多组件的初始化都是依赖这种事件发布订阅机制完成的.
 *
 * SpringBoot 启动常用的事件以及时间发布时间:
 * 	(1) 应用启动事件 {@link ApplicationStartingEvent }，应用刚启动，完成监听器和初始化器的读取.
 * 		事件发布时间: {@link SpringApplication#run(String...)} --> listeners.starting()
 * 	(2) 容器环境就绪事件 {@link ApplicationEnvironmentPreparedEvent } 应用环境的创建.
 * 		事件发布时间: {@link SpringApplication#run(String...)}  --> prepareEnvironment()
 * 	-------------------------------- ApplicationContext 实例化 --------------------------------------
 * 	(3) 应用上下文完成初始化事件 {@link ApplicationContextInitializedEvent}，即所有的 ApplicationContextInitializer
 * 	都完成回调，并且在 BeanDefinition 注册进容器前.
 * 		事件发布时间: {@link SpringApplication#run(String...)} --> prepareContext --> listeners.contextPrepared(context);
 * 	(4) 应用就绪事件 {@link ApplicationPreparedEvent}，ApplicationContext 准备就绪（将配置文件中的监听器添加到 ApplicationContext 中）
 * 	但是未刷新，容器环境已经可用，BeanDefinition 尚未注册.
 * 		事件发布时间: {@link SpringApplication#run(String...)} --> prepareContext --> listeners.contextLoaded(context);
 * 	(5) 应用完成启动事件 {@link ApplicationStartedEvent }，ApplicationContext 已完成刷新，即 refresh 方法完成调用，但是
 * 	{@link org.springframework.boot.ApplicationRunner }和 {@link org.springframework.boot.CommandLineRunner }
 * 	还没有执行.
 * 		事件发布时间: {@link SpringApplication#run(String...)}  --> listeners.started(context);
 * 	(6) 应用就绪事件 {@link ApplicationReadyEvent}， 应用就绪，可以接受 request 请求了.
 * 		事件发布时间:	{@link SpringApplication#run(String...)} listeners.running(context);
 *  (7) 应用启动失败事件: {@link ApplicationFailedEvent}, 应用启动发生异常，可以发生在任何阶段.
 *  	事件发布时间: 应用启动的任何阶段失败.
 */
public class EventPublishingRunListener implements SpringApplicationRunListener, Ordered {

	private final SpringApplication application;

	private final String[] args;

	private final SimpleApplicationEventMulticaster initialMulticaster;

	public EventPublishingRunListener(SpringApplication application, String[] args) {
		this.application = application;
		this.args = args;
		// 实例化事件广播器
		this.initialMulticaster = new SimpleApplicationEventMulticaster();
		// 将 SpringApplication 中的监听器添加到这个事件广播器中，用于后续发布事件时回调订阅事件的监听器中的接口方法
		for (ApplicationListener<?> listener : application.getListeners()) {
			this.initialMulticaster.addApplicationListener(listener);
		}
	}

	@Override
	public int getOrder() {
		return 0;
	}

	@Override
	public void starting() {
		this.initialMulticaster.multicastEvent(new ApplicationStartingEvent(this.application, this.args));
	}

	@Override
	public void environmentPrepared(ConfigurableEnvironment environment) {
		this.initialMulticaster
				.multicastEvent(new ApplicationEnvironmentPreparedEvent(this.application, this.args, environment));
	}

	@Override
	public void contextPrepared(ConfigurableApplicationContext context) {
		this.initialMulticaster
				.multicastEvent(new ApplicationContextInitializedEvent(this.application, this.args, context));
	}

	/**
	 * 将 spring.factories 配置文件中的监听器添加到 ApplicationContext 中
	 */
	@Override
	public void contextLoaded(ConfigurableApplicationContext context) {
		for (ApplicationListener<?> listener : this.application.getListeners()) {
			if (listener instanceof ApplicationContextAware) {
				((ApplicationContextAware) listener).setApplicationContext(context);
			}
			context.addApplicationListener(listener);
		}
		this.initialMulticaster.multicastEvent(new ApplicationPreparedEvent(this.application, this.args, context));
	}

	@Override
	public void started(ConfigurableApplicationContext context) {
		context.publishEvent(new ApplicationStartedEvent(this.application, this.args, context));
		AvailabilityChangeEvent.publish(context, LivenessState.CORRECT);
	}

	@Override
	public void running(ConfigurableApplicationContext context) {
		context.publishEvent(new ApplicationReadyEvent(this.application, this.args, context));
		AvailabilityChangeEvent.publish(context, ReadinessState.ACCEPTING_TRAFFIC);
	}

	@Override
	public void failed(ConfigurableApplicationContext context, Throwable exception) {
		ApplicationFailedEvent event = new ApplicationFailedEvent(this.application, this.args, context, exception);
		if (context != null && context.isActive()) {
			// Listeners have been registered to the application context so we should
			// use it at this point if we can
			context.publishEvent(event);
		}
		else {
			// An inactive context may not have a multicaster so we use our multicaster to
			// call all of the context's listeners instead
			if (context instanceof AbstractApplicationContext) {
				for (ApplicationListener<?> listener : ((AbstractApplicationContext) context)
						.getApplicationListeners()) {
					this.initialMulticaster.addApplicationListener(listener);
				}
			}
			this.initialMulticaster.setErrorHandler(new LoggingErrorHandler());
			this.initialMulticaster.multicastEvent(event);
		}
	}

	private static class LoggingErrorHandler implements ErrorHandler {

		private static final Log logger = LogFactory.getLog(EventPublishingRunListener.class);

		@Override
		public void handleError(Throwable throwable) {
			logger.warn("Error calling ApplicationEventListener", throwable);
		}

	}

}
