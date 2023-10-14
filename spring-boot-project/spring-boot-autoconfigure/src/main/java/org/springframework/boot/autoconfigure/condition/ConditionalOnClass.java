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

package org.springframework.boot.autoconfigure.condition;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * {@link Conditional @Conditional} that only matches when the specified classes are on
 * the classpath.
 * <p>
 * A {@link #value()} can be safely specified on {@code @Configuration} classes as the
 * annotation metadata is parsed by using ASM before the class is loaded. Extra care is
 * required when placed on {@code @Bean} methods, consider isolating the condition in a
 * separate {@code Configuration} class, in particular if the return type of the method
 * matches the {@link #value target of the condition}.
 * 1) 由于注解是由 ASM 解析，所以可以使用 value 来指定具体的 class，即使这个类不在当前路径下.
 * 2) 但是这个注解如果要用在 @Bean 方法上，并且 @Bean 方法返回的类型和注解的类型相同时，在 Conditional 条件应用之前，
 * JVM 会加载对应的类，这会导致失败如果当前类路径下没有指定的类，介于此，需要将该 @Bean 方法上的 Conditional 条件独立
 * 拆分出一个新的 Configuration.
 * https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.developing-auto-configuration.condition-annotations.class-conditions
 *
 * @author Phillip Webb
 * @since 1.0.0
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnClassCondition.class)
public @interface ConditionalOnClass {

	/**
	 * The classes that must be present. Since this annotation is parsed by loading class
	 * bytecode, it is safe to specify classes here that may ultimately not be on the
	 * classpath, only if this annotation is directly on the affected component and
	 * <b>not</b> if this annotation is used as a composed, meta-annotation. In order to
	 * use this annotation as a meta-annotation, only use the {@link #name} attribute.
	 * @return the classes that must be present
	 */
	Class<?>[] value() default {};

	/**
	 * The classes names that must be present.
	 * @return the class names that must be present.
	 */
	String[] name() default {};

}
