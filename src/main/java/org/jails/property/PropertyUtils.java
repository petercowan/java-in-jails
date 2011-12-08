package org.jails.property;

public interface PropertyUtils {
	public Object getProperty(Object object, String propertyName) throws PropertyException;

	public Object getIndexedProperty(Object object, String propertyName, int propertyIndex) throws PropertyException;

	public void setProperty(Object object, String propertyName, Object value) throws PropertyException;

	public Class<?> getPropertyType(Class<?> classType, String propertyName);

}
