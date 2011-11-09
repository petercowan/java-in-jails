package org.jails.property;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jails.util.StringUtil;
import org.jails.property.handler.PropertyHandler;
import org.jails.property.parser.SimplePropertyParser;
import org.jails.property.parser.PropertyParser;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MapToBean {
	private static Logger logger = LoggerFactory.getLogger(MapToBean.class);

	private PropertyHandler propertyHandler;
	private PropertyParser propertyParser;

	static {
		MapBeanUtil.getInstance().initializeConverters();
	}

	public MapToBean() {
		this.propertyParser = new SimplePropertyParser();
	}

	public MapToBean(PropertyParser propertyParser) {
		this.propertyParser = propertyParser;
	}

	public MapToBean(PropertyParser propertyParser, PropertyHandler propertyHandler) {
		this.propertyHandler = propertyHandler;
		this.propertyParser = propertyParser;
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
	public void setBeanProperties(Map<String, String[]> propertiesMap, Object object) {

		for (String rawProperty : propertiesMap.keySet()) {
			String[] valuesArray = propertiesMap.get(rawProperty);

			setBeanProperty(rawProperty, object, valuesArray);
		}
	}

	public void setIndexedBeanProperties(Map<String, String[]> paramMap, List<?> objects) {
		Map<Integer, Map<String, String[]>> paramMapMap = new LinkedHashMap<Integer, Map<String, String[]>>();
		int index = 0;
		for (String param : paramMap.keySet()) {
			Integer propertyIndex = propertyParser.getPropertyIndex(param);

			if (propertyIndex != null) {
				Map<String, String[]> indexedMap = paramMapMap.get(propertyIndex);
				if (indexedMap == null) {
					logger.info("adding index map: " + propertyIndex);
					indexedMap = new HashMap<String, String[]>();
					paramMapMap.put(propertyIndex, indexedMap);
				}
				indexedMap.put(propertyParser.getPropertyName(param), paramMap.get(param));
			}
		}

		for (Integer propertyIndex : paramMapMap.keySet()) {
			Map<String, String[]> indexedMap = paramMapMap.get(propertyIndex);

			if (objects.size() > propertyIndex) {
				Object object = objects.get(propertyIndex);
				logger.info("Got object from list: " + object.getClass());
				setBeanProperties(indexedMap, object);
			}
		}
	}

	public void setBeanProperty(String rawProperty, Object object, String[] valArray) {
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
						//todo - used propertyIndex to find member Objects within a collection
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
							setBeanProperty(nestedProperty, memberObject, valArray);
						}
					} catch (Exception e) {
						logger.warn(e.getMessage());
					}
				} else {
					logger.info("ignoring nested property " + nestedProperty);
				}
			} else {
				try {
					String value = (valArray != null && !StringUtil.isEmpty(valArray[0])) ? valArray[0] : null;
					logger.info("Setting " + propertyName + " value to |" + value + "|");
					if (value != null) logger.info("valueClass: " + value.getClass());
					BeanUtils.setProperty(object, propertyName, value);
				} catch (Exception e) {
					logger.warn(e.getMessage());
				}
			}
		}
	}
}
