package org.jails.property;

public interface PropertyUtils<T> {
	public Object getProperty(T object, String propertyName) throws PropertyException;

	public Object getIndexedProperty(T object, String propertyName, int propertyIndex) throws PropertyException;

	public void setProperty(T object, String propertyName, Object value) throws PropertyException;

	public Class<?> getPropertyType(Class<? extends T> classType, String propertyName);

}
