package org.jails.property;

import org.apache.commons.beanutils.PropertyUtils;
import org.jails.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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

	public static Map<String, Method> getGetterMethods(Class classType) {
		Map<String, Method> getters = new HashMap<String, Method>();

		Method[] methods = classType.getMethods();
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			if (method.getParameterTypes() == null || method.getParameterTypes().length == 0
					&& !method.getReturnType().equals(Void.TYPE)) {
				if ((method.getName().startsWith("get") || method.getName().startsWith("is"))
						&& !"getClass".equals(method.getName())) {
					String property = (method.getName().startsWith("get"))
							? Strings.initLowercase(method.getName().substring(3))
							: Strings.initLowercase(method.getName().substring(2));
					getters.put(property, method);
				}
			}
		}
		return getters;
	}

	public static Map<String, Method> getSetterMethods(Class classType) {
		Map<String, Method> setters = new HashMap<String, Method>();

		Method[] methods = classType.getMethods();
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			if (method.getParameterTypes() != null && method.getParameterTypes().length == 1
					&& method.getReturnType().equals(Void.TYPE)) {
				if (method.getName().startsWith("set")) {
					String property = Strings.initLowercase(method.getName().substring(3));
					setters.put(property, method);
				}
			}
		}
		return setters;
	}

	public static Class getGetterMethodReturnType(Class classType, String propertyName) {
		try {
			Method getter = classType.getMethod("get" + Strings.initCaps(propertyName), null);
			return (getter == null) ? Void.TYPE : getter.getReturnType();
		} catch (NoSuchMethodException e) {
			logger.warn(e.getMessage());
			return Void.TYPE;
		}
	}

	public static boolean isDecimal(Class<?> type) {
		logger.info("isDecimal type: " + type.getName());
		boolean isDecimal = type.equals(Float.class) || type.equals(float.class)
				|| type.equals(Double.class) || type.equals(double.class)
				|| type.equals(BigDecimal.class)
				//perhaps a custom subclass??
				|| Collection.class.isAssignableFrom(Number.class);
		return isDecimal;
	}

	public static boolean isInteger(Class<?> type) {
		logger.info("isInteger type: " + type.getName());
		boolean isInteger = type.equals(Integer.class) || type.equals(int.class)
				|| type.equals(Short.class) || type.equals(short.class)
				|| type.equals(Long.class) || type.equals(long.class)
				|| type.equals(Byte.class) || type.equals(byte.class)
				|| type.equals(BigInteger.class);
		return isInteger;
	}


	public static Class getPropertyType(Class classType, String property) {
		try {
			Class propertyType = null;
			PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(classType);
			if (pds != null) {
				PropertyDescriptor descriptor = null;
				for (int i = 0; i < pds.length && descriptor != null; i++) {
					if (property.equals(pds[i].getName())) {
						descriptor = pds[i];
					}
				}
				propertyType = descriptor.getPropertyType();
			}
			logger.info("Property: " + property + ". Class: " + propertyType);
			return propertyType;
		} catch (Exception e) {
			logger.warn(e.getMessage());
			return null;
		}
	}
}
