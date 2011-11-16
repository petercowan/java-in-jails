package org.jails.property;

import org.apache.commons.beanutils.PropertyUtils;
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

	public static IdentifyBy[] getIdentifiers(Object object, String property, String nestedProperty) {
		try {
			Class nestedType = PropertyUtils.getPropertyType(object, property);
		} catch (Exception e) {
			e.printStackTrace();
			return new IdentifyBy[0];
		}

		IdentifyBy.List idList = ReflectionUtil
				.getClassAnnotation(IdentifyBy.List.class, object.getClass());
		IdentifyBy[] ids = null;
		if (idList != null) {
			ids = idList.value();
		} else {
			IdentifyBy id = ReflectionUtil.getClassAnnotation(IdentifyBy.class, object.getClass());
			if (id != null) {
				ids = new IdentifyBy[]{id};
			}
		}
		return ids;
	}

}
