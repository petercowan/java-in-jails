package org.jails.property;

import org.apache.commons.beanutils.BeanUtils;

public class CommonsPropertyUtils implements PropertyUtils {
	public Object getProperty(Object object, String propertyName) throws PropertyException {
		try {
			return org.apache.commons.beanutils.PropertyUtils.getProperty(object, propertyName);
		} catch (Exception e) {
			throw new PropertyException(e);
		}
	}

	public Object getIndexedProperty(Object object, String propertyName, int propertyIndex) throws PropertyException {
		try {
			return org.apache.commons.beanutils.PropertyUtils.getIndexedProperty(object, propertyName, propertyIndex);
		} catch (Exception e) {
			throw new PropertyException(e);
		}
	}

	public void setProperty(Object object, String propertyName, Object value) throws PropertyException {
		try {
			BeanUtils.setProperty(object, propertyName, value);
		} catch (Exception e) {
			throw new PropertyException(e);
		}
	}

	public Class<?> getPropertyType(Class<?> classType, String propertyName) {
		//todo - check field type instead of getter
		return ReflectionUtil.getGetterMethodReturnType(classType, propertyName);
	}
}
