package org.jails.property;

import org.jails.util.Strings;
import org.javalite.activejdbc.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class ActiveJDBCPropertyUtils implements PropertyUtils<Model> {
	private static Logger logger = LoggerFactory.getLogger(ActiveJDBCPropertyUtils.class);

	public Object getProperty(Model object, String propertyName) throws PropertyException {
		return object.get(propertyName);
	}

	public Object getIndexedProperty(Model object, String propertyName, int propertyIndex) throws PropertyException {
		try {
			Collection c = (Collection) object.get(propertyName);
			return c.toArray()[propertyIndex];
		} catch (Exception e) {
			logger.warn(Strings.getStackTrace(e));
		}
		return null;
	}

	public void setProperty(Model object, String propertyName, Object value) throws PropertyException {
		object.set(propertyName, value);
	}

	public Class<?> getPropertyType(Class<? extends Model> classType, String propertyName) {
		//todo - getType
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
