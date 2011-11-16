package org.jails.property.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadObjectHandler extends AbstractNullObjectHandler {
	private static Logger logger = LoggerFactory.getLogger(LoadObjectHandler.class);

	protected ObjectLoader loader;

	public LoadObjectHandler(ObjectLoader loader) {
		this.loader = loader;
	}

	public Object getObject(Class classType, String nestedProperty, String[] valArray) {
		try {
			Object obj = loader.load(classType, nestedProperty, valArray[0]);
//			logger.info("Setting " + nestedProperty + " to " + valArray[0] + " on " + classType.getSimpleName());
//			BeanUtils.setProperty(obj, nestedProperty, valArray[0]);
			return obj;
		} catch (Exception e) {
			logger.warn(e.getMessage());
			return null;
		}
	}
}
