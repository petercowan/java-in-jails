package org.jails.property.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * NewInstanceNullNestedPropertyHandler creates a new instance of the class of the given
 * Type, and populates the nestedProeprty with the given value(s)
 */
public class NewInstanceNullNestedPropertyHandler extends AbstractNullNestedPropertyHandler {
	private static Logger logger = LoggerFactory.getLogger(NewInstanceNullNestedPropertyHandler.class);

	public Object getObject(Class classType, String nestedProperty, String[] valArray) {
		try {
			Object obj = classType.newInstance();
//			logger.info("Setting " + nestedProperty + " to " + valArray[0] + " on " + classType.getSimpleName());
//			BeanUtils.setProperty(obj, nestedProperty, valArray[0]);
			return obj;
		} catch (Exception e) {
			logger.warn(e.getMessage());
			return null;
		}
	}
}
