package org.jails.validation.client.activejdbc;

import org.jails.util.Strings;
import org.javalite.activejdbc.Registry;
import org.javalite.activejdbc.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ActiveJDBCValidatorUtil {
	private static Logger logger = LoggerFactory.getLogger(ActiveJDBCValidatorUtil.class);

	public static List<Validator> getValidators(Class<?> classType) {
		Registry registry = Registry.instance();
		try {
			Method getValidators = registry.getClass().getDeclaredMethod("getValidators");
			getValidators.setAccessible(true);
			return (List<Validator>) getValidators.invoke(registry, classType);
		} catch (Exception e) {
			logger.warn(Strings.getStackTrace(e));
		}
		return new ArrayList<Validator>();
	}

	public static List<Validator> getValidators(Class<?> classType, String propertyName) {
		List<Validator> classValidators = getValidators(classType);
		List<Validator> validators = new ArrayList<Validator>();
		for (Validator validator : classValidators) {
			try {
				Field field = validator.getClass().getField(propertyName);
				String attributeName = (String) field.get(validator);
				if (attributeName.equals(propertyName)) validators.add(validator);
			} catch (Exception e) {
				logger.warn(Strings.getStackTrace(e));
			}
		}
		return validators;
	}

	public static Validator getValidators(Class<?> classType, String propertyName,
												Class<? extends Validator> validator) {
		List<Validator> validators = getValidators(classType, propertyName);
		for (Validator v : validators) {
			if (v.getClass().equals(validator)) return v;
		}
		return null;
	}

}
