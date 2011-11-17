package org.jails.property;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.jails.property.handler.PropertyHandler;
import org.jails.property.parser.PropertyParser;
import org.jails.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;

/**
 * <pre>
 * <code>Mapper</code> converts a Map of Strings to an Object, and vice versa using the
 * Object's getter/setter methods. It is intended to be used with the request
 * parameters accessed via calling <code>HttpServletRequest.getParameterMap</code>, which
 * is why the Map is of type <code>Map<String,String[]></code>. Converting between the two is
 * as simple as this:
 *
 * <code>
 *  MyObject myObject = mapper.toObject(MyObject.class, request.getParameterMap());
 * 	Map<String, String[]> propertiesMap = mapper.toMap(myObject);
 * </code>
 *
 * The property parsing is handled by the PropertyParser member Object, however a typical format is:
 *
 * <code>
 *  MyObject myObject = //get MyObject...;
 * 	String property = "myObject.myProperty.myNestedProperty"
 * 	String propertyValue = mapper.getValue(myObject, property);
 * 	// converts myObject.getMyProperty().getMyNestedProperty() to a String
 * </code>
 *
 * see {@link SimpleMapper} for an example implementation)
 *
 * Mapper places no limits on the level of nested properties to be resolved. The default
 * behavior for handling nested properties is if the initial property is null, ignore the
 * nestedProperty, failing silently:
 *
 * <code>
 *  MyObject myObject = new MyObject();
 * 	String property = "myObject.myProperty.myNestedProperty"
 * 	mapper.setValue(myObject, property,
 * 				request.getParameterValues(property));
 * //myObject.getMyProperty() is null, so no value is set.
 * </code>
 *
 * Default behavior is overridden by creating Mapper with a custom PropertyHandler.
 * see {@link org.jails.property.handler.SimplePropertyHandler} for the recommended implementation
 *
 * Conversion of property values from Objects/primitives to String is handled by ConverterUtil.
 *
 * </pre>
 *
 * @see SimpleMapper
 * @see org.jails.property.handler.SimplePropertyHandler
 * @see ConverterUtil
 */
public class Mapper {
	private static Logger logger = LoggerFactory.getLogger(Mapper.class);

	protected PropertyHandler propertyHandler;
	protected PropertyParser propertyParser;

	static {
		ConverterUtil.getInstance();
	}

	private Mapper(){}

	/**
	 * Create a new Mapper with the given PropertyParser
	 *
	 * @param propertyParser
	 */
	public Mapper(PropertyParser propertyParser) {
		this.propertyParser = propertyParser;
	}

	/**
	 * Create a new Mapper with the given PropertyParser and PropertyHandler
	 *
	 * @param propertyParser
	 * @param propertyHandler
	 */
	public Mapper(PropertyParser propertyParser, PropertyHandler propertyHandler) {
		this.propertyHandler = propertyHandler;
		this.propertyParser = propertyParser;
	}

	/**
	 * Creates a new instance of classType, and sets the values of its fields by calling
	 * its setters methods based on the names of the keys of the propertiesMap, using
	 * the corresponding values.
	 *
	 * @param classType Class of Object to create instance of
	 * @param propertiesMap properties to map to Object
	 * @param <T> same type as classType
	 * @return instance of type T
	 */
	public <T> T toObject(Class<T> classType, Map<String, String[]> propertiesMap) {
		try {
			T object = classType.newInstance();
			toExistingObject(object, propertiesMap);
			return object;
		} catch (Exception e) {
			logger.warn(e.getMessage());
			throw new IllegalArgumentException("Class must have a public constructor with no args to use this method");
		}
	}

	/**
	 * Updates an existing instance of an Object, setting the values of its fields by calling
	 * its setters methods based on the names of the keys of the propertiesMap, using
	 * the corresponding values.
	 *
	 * @param object Object to update
	 * @param propertiesMap properties to map to Object
	 */
	public void toExistingObject(Object object, Map<String, String[]> propertiesMap) {

		for (String rawProperty : propertiesMap.keySet()) {
			String[] valuesArray = propertiesMap.get(rawProperty);

			setValues(object, rawProperty, valuesArray);
		}
	}

	/**
	 * @see Mapper#toObject(Class, java.util.Map)
	 *
	 * @param classType Class of Object to create List of instances
	 * @param propertiesMap properties to map to Object
	 * @param <T> same type as classType
	 * @return List of Objects of of type T
	 */
	public <T> List<T> toList(Class<T> classType, Map<String, String[]> propertiesMap) {
		PropertiesMultiMap multiMap = PropertiesMultiMap.getMultiMap(propertiesMap);

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

		_toExistingList(objects, multiMap);

		return objects;
	}

	/**
	 * @see Mapper#toExistingObject(Object, java.util.Map)
	 * @see Mapper#toList(Class, java.util.Map)
	 *
	 * @param objects List<?> of Objects to update
	 * @param propertiesMap properties to map to Object
	 */
	public void toExistingList(List<?> objects, Map<String, String[]> propertiesMap) {
		PropertiesMultiMap multiMap = PropertiesMultiMap.getMultiMap(propertiesMap);

		_toExistingList(objects, multiMap);
	}

	protected void _toExistingList(List<?> objects, PropertiesMultiMap multiMap) {
		for (Integer propertyIndex : multiMap.keySet()) {
			Map<String, String[]> indexedMap = multiMap.get(propertyIndex);

			if (objects.size() > propertyIndex) {
				Object object = objects.get(propertyIndex);
				logger.info("Got object from list: " + object.getClass());
				toExistingObject(object, indexedMap);
			}
		}
	}

	/**
	 * Sets the value, or values, of the corresponding Object property to
	 * the rawPoperty String provided, and the values.
	 *
	 * <code>
	 * 		mapper.setValue(myObject, "myObject.myProperty", new String[]{"myValue"});
 	 * 		//calls myObject.setMyProperty(ConverterUtils.convert("myValue"));
	 * </code>
	 *
	 * @param object Object to update
	 * @param rawProperty field to update
	 * @param valArray String values to be converted and set in field
	 */
	public void setValues(Object object, String rawProperty, String[] valArray) {
		if (rawProperty == null)
			throw new IllegalArgumentException("rawProperty must not be null");
		if (object == null)
			throw new IllegalArgumentException("object must not be null");
		if (valArray == null)
			throw new IllegalArgumentException("valArray must not be null");

		logger.info("rawProperty: " + rawProperty);

		//separate the type and property names from the param: type.propertyName -> type, propertyName
		String type = propertyParser.getRootProperty(rawProperty);
		rawProperty = propertyParser.getNestedProperty(rawProperty);
		logger.info("type: " + type);
		logger.info("property: " + rawProperty);

		_setValues(object, rawProperty, valArray);

	}

	/**
	 * Convenience method for setValues
	 * @see Mapper#setValues(Object, String, String[])
	 *
	 * @param object Object to update
	 * @param rawProperty field to update
	 * @param value String value to be converted and set in field
	 */
	public void setValue(Object object, String rawProperty, String value) {
		setValues(object, rawProperty, new String[]{value});
	}

	protected void _setValues(Object object, String property, String[] valArray) {
		logger.info("property: " + property + " of Class: "
				+ object.getClass() + " with values: ");
		if (valArray.length > 1) {
			StringBuffer values = new StringBuffer();
			for (int i = 0; i < valArray.length; i++) {
				if (i > 0) values.append(",");
				values.append(valArray[i]);
			}
			logger.info(values.toString());
		} else {
			logger.info(valArray[0]);
		}

		//separate the type and property names from the param: type.propertyName -> type, propertyName
		String propertyName = propertyParser.getPropertyName(property);

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

						if (propertyParser.isIndexed(rootPropertyName)) {
							memberObject = PropertyUtils.getIndexedProperty(object, rootPropertyName, propertyIndex);
						} else {
							memberObject = PropertyUtils.getProperty(object, rootPropertyName);
						}
						logger.info("memberObject: " + memberObject);

						//memberObject is null, so attempt find an instance, using the PropertyHandler
						if (memberObject == null && propertyHandler != null) {
							memberObject = propertyHandler.handleNullNestedProperty(object, rootPropertyName, nestedProperty, valArray, propertyParser);
						}

						if (memberObject != null) {
							logger.info("memberObject Class: " + memberObject.getClass());
							_setValues(memberObject, nestedProperty, valArray);
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
					logger.info("Original " + propertyName + " value: " + BeanUtils.getProperty(object, propertyName));
					logger.info("Setting " + propertyName + " value to |" + value + "|");
					if (value != null) logger.info("valueClass: " + value.getClass());
					Class<?> returnType = ReflectionUtil.getGetterMethodReturnType(object.getClass(), propertyName);
					if (ReflectionUtil.isInteger(returnType)) {
						logger.info("Cleaning int value");
						value = Strings.cleanInt(value);
					} else if (ReflectionUtil.isDecimal(returnType)) {
						logger.info("Cleaning decimal value");
						value = Strings.cleanDecimal(value);
					}
					BeanUtils.setProperty(object, propertyName, value);
					logger.info(propertyName + " set to : " + BeanUtils.getProperty(object, propertyName));
				} catch (Exception e) {
					logger.warn(e.getMessage());
				}
			}
		}
	}

	//To map

	/**
	 * Create a Map based on an Object, and it's member Objects (recursively),
	 * getters as keys, and their values.
	 *
	 * @param object Object to convert to Map
	 * @return Map<String, String[]> of corresponding properties and values
	 */
	public Map<String, String[]> toMap(Object object) {
		return toMap(object, null);
	}

	/**
	 * @see Mapper#toMap(Object)
	 *
	 * @param objects List<?> of Objects
	 * @return Map<String, String[]> of corresponding properties and values
	 */
	public Map<String, String[]> toMap(List<?> objects) {
		Map<String, String[]> paramMap = new TreeMap<String, String[]>();
		for (int i = 0; i < objects.size(); i++) {
			paramMap.putAll(toMap(objects.get(i), i));
		}
		return paramMap;
	}

	protected Map<String, String[]> toMap(Object object, Integer index) {
		String type = Strings.initLowercase(object.getClass().getSimpleName());
		String indexStr = (index != null) ? "[" + index + "]" : "";
		Set cache = new HashSet();
		return _toMap(object, type + indexStr, cache);
	}

	protected Map<String, String[]> _toMap(Object bean, String type, Set cache) {
		if (cache.contains(bean)) return Collections.EMPTY_MAP;
		cache.add(bean);

		String prefix = (type != null) ? type + "." : "";

		Map<String, String[]> paramMap = new TreeMap<String, String[]>();
		try {

			Map<String, Method> getters = ReflectionUtil.getGetterMethods(bean.getClass());

			for (String fieldName : getters.keySet()) {
				Method method = getters.get(fieldName);
				try {
					Collection<Object> values = new ArrayList<Object>();
					boolean isCollection = true;

					if (Collection.class.isAssignableFrom(method.getReturnType())) {
						Collection c = (Collection) MethodUtils.invokeMethod(bean, method.getName(), null);
						if (c != null) values.addAll(c);

					} else if (method.getReturnType().isArray()) {
						Object[] array = (Object[]) MethodUtils.invokeMethod(bean, method.getName(), null);

						if (array != null) values.addAll(Arrays.asList(array));
					} else {
						isCollection = false;
						Object value = MethodUtils.invokeMethod(bean, method.getName(), null);
						values.add(value);
					}

					Object[] valArray = values.toArray();
					for (int i = 0; i < valArray.length; i++) {
						Object value = valArray[i];
						String index = (isCollection) ? "[" + i +"]" : "";

						if (value == null) {
							//ignore for now
							//logger.info("Null value for " + method.getName());
						} else if (ConverterUtil.getInstance().canConvert(value)) {
							paramMap.put(prefix + fieldName + index, new String[]{ConverterUtil.getInstance().convert(value)});
						} else {
							Map<String, String[]> nestedMap = _toMap(value, prefix + fieldName + index, cache);
							paramMap.putAll(nestedMap);
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return paramMap;
	}

	/**
	 * Returns a list of String values by calling the corresponding getter method
	 * in the given Object based on the propertyName provided.
	 *
	 * <code>
	 *     String[] propertyValues = mapper.getValue(myObject, "myObject.myProperty");
	 *     //calls ConverterUtils.convert(myObject.getMyProperty());
	 * </code>
	 *
	 * @param object
	 * @param rawProperty
	 * @return String[] of converted values
	 */
	public String[] getValues(Object object, String rawProperty) {
		if (rawProperty == null) {
			logger.info("ignoring rawProperty: " + rawProperty);
			return new String[]{};
		}

		//separate the type and property names from the param: type.propertyName -> type, propertyName
		String type = propertyParser.getRootProperty(rawProperty);
		rawProperty = propertyParser.getNestedProperty(rawProperty);

		return _getValues(object, rawProperty);
	}

	protected String[] _getValues(Object object, String rawProperty) {
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
				return getValues(memberObject, nestedProperty);
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

	/**
	 * Convenience method for Mapper.getValues()
	 * @see Mapper#getValues(Object, String)
	 *
	 * @param object
	 * @param rawProperty
	 * @return
	 */
	public String getValue(Object object, String rawProperty) {
		String[] propertyValues = getValues(object, rawProperty);
		return (propertyValues.length > 0) ? propertyValues[0] : null;
	}

	protected Class getType(Object object, String rawProperty) {
		//separate the type and property names from the param: type.propertyName -> type, propertyName
		String type = propertyParser.getRootProperty(rawProperty);
		rawProperty = propertyParser.getNestedProperty(rawProperty);

		return getType(object, rawProperty);
	}

	protected Class _getType(Class classType, String rawProperty) {
		String property = propertyParser.getPropertyName(rawProperty);

		if (propertyParser.hasNestedProperty(property)) {
			String nestedProperty = propertyParser.getNestedProperty(property);
			String rootProperty = propertyParser.getRootProperty(property);

			Class propertyType = getPropertyType(classType, rootProperty);
			return _getType(propertyType, nestedProperty);
		} else {
			return getPropertyType(classType, property);
		}
	}

	protected Class getPropertyType(Class classType, String property) {
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
