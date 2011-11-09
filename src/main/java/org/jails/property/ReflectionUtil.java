package org.jails.property;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtil {
	private static Logger logger = LoggerFactory.getLogger(ReflectionUtil.class);

	public static <A extends Annotation> A getMethodAnnotation(Class<A> annotationType,
															   Class classType,
															   String propertyName,
															   Class[] parameterTypes) {
		Method method;
		try {
			method = classType.getMethod(propertyName, parameterTypes);
			logger.info("AnnotatedMethod: " + method.getName() + ": " + propertyName);
			Annotation a = method.getAnnotation(annotationType);
			logger.info("Annotation: " + a);
			return (A) a;
		} catch (NoSuchMethodException e) {
			logger.warn(e.getMessage());
		}
		return null;
	}

	public static <A extends Annotation> A getClassAnnotation(Class<A> annotationType, Class classType) {
		logger.info("AnnotatedClass: " + classType.getSimpleName());
		Annotation a = (A) classType.getAnnotation(annotationType);
		logger.info("Annotation: " + a);
		return (A) a;
	}

	public static <A extends Annotation> A getFieldAnnotation(Class<A> annotationType,
															  Class classType,
															  String fieldName) {
		Field field;
		try {
			field = classType.getDeclaredField(fieldName);
			logger.info("AnnotatedField: " + field.getName() + ": " + field);
			Annotation a = field.getAnnotation(annotationType);
			logger.info("Annotation: " + a);
			return (A) a;
		} catch (NoSuchFieldException e) {
			logger.warn(e.getMessage());
		}
		return null;
	}
}
