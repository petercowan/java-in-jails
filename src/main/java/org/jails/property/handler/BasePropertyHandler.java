package org.jails.property.handler;

import org.jails.property.AcceptsNestedAttributes;
import org.jails.property.IgnoreNestedAttributes;
import org.jails.property.ReflectionUtil;
import org.jails.property.parser.PropertyParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasePropertyHandler<T>
		implements PropertyHandler<T> {

	private static Logger logger = LoggerFactory.getLogger(BasePropertyHandler.class);

	protected NullNestedPropertyHandler<T> nullNestedPropertyHandler;

	public BasePropertyHandler(NullNestedPropertyHandler<T> nullNestedPropertyHandler) {
		this.nullNestedPropertyHandler = nullNestedPropertyHandler;
	}

	public <T> boolean acceptsNestedProperties(T object, String propertyName) {
			boolean classAcceptsNestedAttributes = ReflectionUtil
					.getClassAnnotation(AcceptsNestedAttributes.class, object.getClass()) != null;
			boolean propertyAcceptsNestedAttributes = ReflectionUtil
					.getFieldAnnotation(AcceptsNestedAttributes.class, object.getClass(), propertyName) != null;
			boolean propertyRejectsNestedAttributes = ReflectionUtil
					.getFieldAnnotation(IgnoreNestedAttributes.class, object.getClass(), propertyName) != null;

			logger.debug("classAcceptsNestedAttributes?" + classAcceptsNestedAttributes);
			logger.debug("propertyAcceptsNestedAttributes?" + propertyAcceptsNestedAttributes);
			logger.debug("propertyRejectsNestedAttributes?" + propertyRejectsNestedAttributes);
			return (classAcceptsNestedAttributes && !propertyRejectsNestedAttributes)
					|| propertyAcceptsNestedAttributes;
		}

		public void handleNullNestedProperty(T object, String property, String nestedProperty, String[] valArray, PropertyParser propertyParser) {
			nullNestedPropertyHandler.handleProperty(object, property, nestedProperty, valArray, propertyParser);
		}
	}
