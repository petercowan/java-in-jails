package org.jails.property.handler;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.jails.property.parser.PropertyParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractNullNestedPropertyHandler implements NullNestedPropertyHandler {
	private static Logger logger = LoggerFactory.getLogger(AbstractNullNestedPropertyHandler.class);

	protected AbstractNullNestedPropertyHandler() {}

	public Object handleProperty(Object object, String property, String nestedProperty, String[] valArray, PropertyParser propertyParser) {
		try {
			logger.info("getting class type of property " + property + " from Class: " + object.getClass());
			Class memberType = PropertyUtils.getPropertyType(object, property);
			logger.info("classType of " + property + ": " + memberType.getSimpleName());

			String nestedPropertyName = propertyParser.getPropertyName(nestedProperty);

			logger.info("Attempting to load new " + memberType);
			Object memberObject = getObject(memberType, nestedPropertyName, valArray);

			//now set memberObject in parent object
			if (memberObject != null) {
				String setterName = "set" + property.substring(0, 1).toUpperCase() + property.substring(1);
				logger.info(setterName + " : " + memberObject);
				MethodUtils.invokeExactMethod(object, setterName, memberObject);
			}
			return memberObject;
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
		return null;
	}

	protected abstract Object getObject(Class classType, String nestedProperty, String[] valArray);
}
