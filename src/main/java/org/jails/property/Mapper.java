package org.jails.property;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.jails.property.handler.PropertyHandler;
import org.jails.property.parser.PropertyParser;
import org.jails.property.parser.SimplePropertyParser;
import org.jails.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Mapper {
	private static Logger logger = LoggerFactory.getLogger(Mapper.class);

	private PropertyHandler propertyHandler;
	private PropertyParser propertyParser;

	static {
		MapBeanUtil.getInstance().initializeConverters();
	}

	public Mapper() {
		this.propertyParser = new SimplePropertyParser();
	}

	public Mapper(PropertyParser propertyParser) {
		this.propertyParser = propertyParser;
	}

	public Mapper(PropertyParser propertyParser, PropertyHandler propertyHandler) {
		this.propertyHandler = propertyHandler;
		this.propertyParser = propertyParser;
	}

	public <T> T toObject(Map<String, String[]> propertiesMap, Class<T> classType) {
		try {
			T object = classType.newInstance();
			toExistingObject(propertiesMap, object);
			return object;
		} catch (Exception e) {
			logger.warn(e.getMessage());
			throw new IllegalArgumentException("Class must have a public constructor with no args to use this method");
		}
	}

	/**
	 * Populate the a bean using its setters and  a map of string values. Map
	 * keys correspond to bean property names. Map keys may have nested properties
	 * and/or ordered indexes appended to them:
	 * <p/>
	 * Charity bean: user
	 * <p/>
	 * Property name -> setterMethod
	 * <p/>
	 * charity.name -> charity.setName(charity.name);
	 * charity.name.1 -> charity.setName(charity.name.1);
	 * charity.address.zip -> if (charity.getAddress() != null) charity.getAddress().setZip(charity.address.zip);
	 * <p/>
	 * Order indexes are used for populating multiple object in a list. Nested properties are used
	 * to populate fields of a member Object. If a nested properties member object is null, then
	 * we attempt to load that object from the database:
	 * <p/>
	 * charity.address.id -> if (charity.getAddress() == null) charity.setAddress(load(Address.class, charity.address.id));
	 * <p/>
	 * This works for fields other than just "id":
	 * charity.address.zip -> if (charity.getAddress() == null) charity.setAddress(load(Address.class, charity.address.zip));
	 * so it is recommended to use nested properties very carefully
	 *
	 * @param propertiesMap
	 * @param object
	 */
	public void toExistingObject(Map<String, String[]> propertiesMap, Object object) {

		for (String rawProperty : propertiesMap.keySet()) {
			String[] valuesArray = propertiesMap.get(rawProperty);

			setProperty(rawProperty, object, valuesArray);
		}
	}

	public <T> List<T> toList(Map<String, String[]> paramMap, Class<T> classType) {
		PropertiesMultiMap multiMap = PropertiesMultiMap.getMultiMap(paramMap);

		List<T> objects = new ArrayList<T>(multiMap.size());

		try {
			T object = classType.newInstance();
			for (int i = 0; i < multiMap.size(); i++) {
				objects.add(object);
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
			throw new IllegalArgumentException("Class must have a public constructor with no args to use this method");
		}

		setIndexedBeanProperties(multiMap, objects);

		return objects;
	}

	public void toExistingList(Map<String, String[]> paramMap, List<?> objects) {
		PropertiesMultiMap multiMap = PropertiesMultiMap.getMultiMap(paramMap);

		setIndexedBeanProperties(multiMap, objects);
	}

	protected void setIndexedBeanProperties(PropertiesMultiMap multiMap, List<?> objects) {
		for (Integer propertyIndex : multiMap.keySet()) {
			Map<String, String[]> indexedMap = multiMap.get(propertyIndex);

			if (objects.size() > propertyIndex) {
				Object object = objects.get(propertyIndex);
				logger.info("Got object from list: " + object.getClass());
				toExistingObject(indexedMap, object);
			}
		}
	}

	public void setProperty(String rawProperty, Object object, String[] valArray) {
		if (rawProperty == null)
			throw new IllegalArgumentException("rawProperty must not be null");
		if (object == null)
			throw new IllegalArgumentException("object must not be null");
		if (valArray == null)
			throw new IllegalArgumentException("valArray must not be null");

		logger.info("rawProperty: " + rawProperty + " of Class: "
				+ object.getClass() + " with values: " + valArray.length);

		//separate the type and property names from the param: type.propertyName -> type, propertyName
		String type = propertyParser.getType(rawProperty);
		String propertyName = propertyParser.getPropertyName(rawProperty);

		if (propertyName != null) {
			//this property contains nested properties
			if (propertyParser.hasNestedProperty(propertyName)) {
				String nestedProperty = propertyParser.getNestedProperty(propertyName);
				String rootPropertyName = propertyParser.getRootProperty(propertyName);

				if (propertyHandler.acceptsNestedProperties(object, rootPropertyName)) {
					try {
						Integer propertyIndex = propertyParser.getPropertyIndex(rootPropertyName);
						logger.info("loading rootPropertyName " + rootPropertyName + " of Class: " + object.getClass()
										+ ". index: " + propertyIndex);
						Object memberObject;

						if (propertyIndex != null) {
							memberObject = PropertyUtils.getIndexedProperty(object, rootPropertyName, propertyIndex);
						} else {
							memberObject = PropertyUtils.getProperty(object, rootPropertyName);
						}
						logger.info("memberObject: " + memberObject);

						//memberObject is null, so attempt find an instance, using the PropertyHandler
						if (memberObject == null && propertyHandler != null) {
							propertyHandler.handleNullNestedProperty(object, rootPropertyName, nestedProperty, valArray, propertyParser);
						} else {
							logger.info("memberObject Class: " + memberObject.getClass());
							setProperty(nestedProperty, memberObject, valArray);
						}
					} catch (Exception e) {
						logger.warn(e.getMessage());
					}
				} else {
					logger.info("ignoring nested property " + nestedProperty);
				}
			} else {
				try {
					String value = (valArray != null && !Strings.isEmpty(valArray[0])) ? valArray[0] : null;
					logger.info("Setting " + propertyName + " value to |" + value + "|");
					if (value != null) logger.info("valueClass: " + value.getClass());
					BeanUtils.setProperty(object, propertyName, value);
				} catch (Exception e) {
					logger.warn(e.getMessage());
				}
			}
		}
	}

	//To map
	/**
	 * Populate the a bean using its setters and  a map of string values. Map
	 * keys correspond to bean property names. Map keys may have nested properties
	 * and/or ordered indexes appended to them:
	 * <p/>
	 * Charity bean: user
	 * <p/>
	 * Property name -> setterMethod
	 * <p/>
	 * charity.name -> charity.setName(charity.name);
	 * charity.name.1 -> charity.setName(charity.name.1);
	 * charity.address.zip -> if (charity.getAddress() != null) charity.getAddress().setZip(charity.address.zip);
	 * <p/>
	 * Order indexes are used for populating multiple object in a list. Nested properties are used
	 * to populate fields of a member Object. If a nested properties member object is null, then
	 * we attempt to load that object from the database:
	 * <p/>
	 * charity.address.id -> if (charity.getAddress() == null) charity.setAddress(load(Address.class, charity.address.id));
	 * <p/>
	 * This works for fields other than just "id":
	 * charity.address.zip -> if (charity.getAddress() == null) charity.setAddress(load(Address.class, charity.address.zip));
	 * so it is recommended to use nested properties very carefully
	 *
	 * @param object
	 */
	public Map<String, String[]> toMap(Object object) {
		try {
			return BeanUtils.describe(object);
		} catch (Exception e) {
			logger.warn(e.getMessage());
			return Collections.EMPTY_MAP;
		}
	}

	public String[] toValues(String rawProperty, Object object) {
		if (rawProperty == null) {
			logger.info("ignoring rawProperty: " + rawProperty);
			return new String[]{};
		}

		//separate the type and property names from the param: type.propertyName -> type, propertyName
		String type = propertyParser.getType(rawProperty);
		String property = propertyParser.getPropertyName(rawProperty);

		if (property == null || property.startsWith("_")) {
			logger.info("ignoring property: " + property);
			return new String[]{};
		}
		String propertyValue;

		//this property contains nested properties
		if (propertyParser.hasNestedProperty(property)) {
			String nestedProperty = propertyParser.getNestedProperty(property);
			property = propertyParser.getRootProperty(property);

			Object memberObject = null;
			try {
				memberObject = PropertyUtils.getProperty(object, property);
				logger.info("memberObject: " + memberObject);
			} catch (Exception e) {
				logger.warn(e.getMessage());
			}

			//memberObject is null, so attempt find an instance, using the NullNestedPropertyHandler
			if (memberObject != null) {
				return new String[]{};
			} else {
				logger.info("memberObject Class: " + memberObject.getClass());
				return toValues(nestedProperty, memberObject);
			}
		} else {
			try {
				return new String[]{BeanUtils.getProperty(object, property)};
			} catch (Exception e) {
				logger.warn(e.getMessage());
				return new String[]{};
			}
		}
	}

	public String toValue(String rawProperty, Object object) {
		String[] propertyValues = toValues(rawProperty, object);
		return (propertyValues.length > 0) ? propertyValues[0] : null;
	}

	public Class getType(String rawProperty, Object object) {
		//separate the type and property names from the param: type.propertyName -> type, propertyName
		String type = propertyParser.getType(rawProperty);
		String property = propertyParser.getPropertyName(rawProperty);

		String propertyValue;

		//this property contains nested properties
		if (propertyParser.hasNestedProperty(property)) {
			String nestedProperty = propertyParser.getNestedProperty(property);
			property = propertyParser.getRootProperty(property);

			Class propertyType = getPropertyType(property, object.getClass());
			return getPropertyType(nestedProperty, propertyType);
		} else {
			return getPropertyType(property, object.getClass());
		}
	}

	private Class getPropertyType(String property, Class classType) {
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
