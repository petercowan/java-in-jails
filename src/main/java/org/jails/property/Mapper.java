package org.jails.property;

import org.jails.property.handler.PropertyHandler;
import org.jails.property.parser.PropertyParser;
import org.jails.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	protected PropertyUtils propertyUtils;

	static {
		ConverterUtil.getInstance();
	}

	private Mapper() {
		propertyUtils = new CommonsPropertyUtils();
	}

	/**
	 * Create a new Mapper with the given PropertyParser
	 *
	 * @param propertyParser
	 */
	public Mapper(PropertyParser propertyParser) {
		this();
		this.propertyParser = propertyParser;
	}

	/**
	 * Create a new Mapper with the given PropertyParser and PropertyHandler
	 *
	 * @param propertyParser
	 * @param propertyHandler
	 */
	public Mapper(PropertyParser propertyParser, PropertyHandler propertyHandler) {
		this();
		this.propertyHandler = propertyHandler;
		this.propertyParser = propertyParser;
	}

	/**
	 * Creates a new instance of classType, and sets the values of its fields by calling
	 * its setters methods based on the names of the keys of the propertiesMap, using
	 * the corresponding values.
	 *
	 * @param classType	 Class of Object to create instance of
	 * @param propertiesMap properties to map to Object
	 * @param <T>           same type as classType
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
	 * @param object		Object to update
	 * @param propertiesMap properties to map to Object
	 */
	public void toExistingObject(Object object, Map<String, String[]> propertiesMap) {

		PropertiesWrapper propertiesWrapper = new PropertiesWrapper(propertiesMap, object.getClass());
		for (String rawProperty : propertiesMap.keySet()) {
			String[] valuesArray = propertiesMap.get(rawProperty);
			setValues(object, rawProperty, valuesArray, propertiesWrapper);
		}
	}

	/**
	 * @param classType	 Class of Object to create List of instances
	 * @param propertiesMap properties to map to Object
	 * @param <T>           same type as classType
	 * @return List of Objects of of type T
	 * @see Mapper#toObject(Class, java.util.Map)
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
	 * @param objects	   List<?> of Objects to update
	 * @param propertiesMap properties to map to Object
	 * @see Mapper#toExistingObject(Object, java.util.Map)
	 * @see Mapper#toList(Class, java.util.Map)
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
	 * <p/>
	 * <code>
	 * mapper.setValue(myObject, "myObject.myProperty", new String[]{"myValue"});
	 * //calls myObject.setMyProperty(ConverterUtils.convert("myValue"));
	 * </code>
	 *
	 * @param object			Object to update
	 * @param rawProperty	   field to update
	 * @param valArray		  String values to be converted and set in field
	 * @param propertiesWrapper
	 */
	protected void setValues(Object object, String rawProperty, String[] valArray, PropertiesWrapper propertiesWrapper) {
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

		_setValues(object, rawProperty, type, propertiesWrapper);
	}

	protected void _setValues(Object object, String property, String propertyStem, PropertiesWrapper propertiesMap) {
		logger.info("property: " + property + " of Class: " + object.getClass());

		//use this to retrieve value from propertiesMap
		String propertyValueKey = propertyStem + "." + property;

		//if this property is nested (eg "nested.property"), and the object allows nested properties to be
		//set, retrieve the memberObject and set the nested property there.
		if (propertyParser.hasNestedProperty(property)) {
			String nestedProperty = propertyParser.getNestedProperty(property);
			property = propertyParser.getRootProperty(property);

			//strip index from property, if it exists
			String propertyName = propertyParser.getPropertyName(property);

			if (propertyHandler.acceptsNestedProperties(object, propertyName)) {
				try {
					//propertyIndex refers to a properties position within a Collection or Array
					Integer propertyIndex = propertyParser.getPropertyIndex(property);
					logger.info("loading propertyName " + propertyName + " of Class: " + object.getClass() + ". index: " + propertyIndex);

					Object memberObject;
					if (propertyParser.isIndexed(propertyName)) {//&& propertyParser.getPropertyIndex(propertyName) != null) {
						memberObject = propertyUtils.getIndexedProperty(object, propertyName, propertyIndex);
					} else {
						memberObject = propertyUtils.getProperty(object, propertyName);
					}
					logger.info("memberObject: " + memberObject);

					//memberObject is null, so attempt retrieve an instance using the PropertyHandler
					if (memberObject == null && propertyHandler != null) {
						String[] valArray = propertiesMap.get(propertyValueKey);
						memberObject = propertyHandler.handleNullNestedProperty(object,
								propertyName, nestedProperty, valArray, propertyParser);
					}

					if (memberObject != null) {
						logger.info("memberObject Class: " + memberObject.getClass());
						_setValues(memberObject, nestedProperty, propertyStem + "." + property, propertiesMap);
					}
				} catch (Exception e) {
					logger.warn(e.getMessage());
				}
			} else {
				logger.info("ignoring nested property " + nestedProperty);
			}
		} else {
			//strip index from property, if it exists
			String propertyName = propertyParser.getPropertyName(property);

			try {
				logger.info("propertyValueKey: " + propertyValueKey);
				String value = propertiesMap.getValue(propertyValueKey);
				logger.info("Object's " + propertyName + " value: " + propertyUtils.getProperty(object, propertyName));
				logger.info("Setting " + propertyName + " value to |" + value + "|");

				if (value == null) value = propertiesMap.getSelectOtherValue(propertyValueKey);

				//process value based on underlying Object type
				Class<?> returnType = ReflectionUtil.getGetterMethodReturnType(object.getClass(), propertyName);
				logger.info("returnType: " + returnType);
				if (ReflectionUtil.isInteger(returnType)) {
					logger.info("Cleaning int value");
					value = Strings.cleanInt(value);
				} else if (ReflectionUtil.isDecimal(returnType)) {
					logger.info("Cleaning decimal value");
					value = Strings.cleanDecimal(value);
				} else if (Date.class.equals(returnType)) {
					if (value == null) {
						value = propertiesMap.composeDate(propertyValueKey);
					}
				}

				propertyUtils.setProperty(object, propertyName, value);
				logger.info(propertyName + " set to : " + propertyUtils.getProperty(object, propertyName));
			} catch (Exception e) {
				logger.warn(e.getMessage());
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
	 * @param objects List<?> of Objects
	 * @return Map<String, String[]> of corresponding properties and values
	 * @see Mapper#toMap(Object)
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
		return recursiveToMap(object, type + indexStr, cache);
	}

	protected static Map<String, String[]> recursiveToMap(Object object, String prefix, Set cache) {
		if (cache.contains(object)) return Collections.EMPTY_MAP;
		cache.add(object);
		prefix = (prefix != null) ? prefix + "." : "";

		Map<String, String[]> beanMap = new TreeMap<String, String[]>();

		Map<String, Object> properties = ReflectionUtil.getProperties(object);
		for (String property : properties.keySet()) {
			Object value = properties.get(property);
			try {
				if (value == null) {
					//ignore nulls
				} else if (Collection.class.isAssignableFrom(value.getClass())) {
					beanMap.putAll(convertAll((Collection) value, prefix + property, cache));
				} else if (value.getClass().isArray()) {
					beanMap.putAll(convertAll(Arrays.asList((Object[]) value), prefix + property, cache));
				} else if (Map.class.isAssignableFrom(value.getClass())) {
					beanMap.putAll(convertMap((Map) value, prefix + property, cache));
				} else {
					beanMap.putAll(convertObject(value, prefix + property, cache));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return beanMap;
	}

	protected static Map<String, String[]> convertAll(Collection<Object> values, String key, Set cache) {
		Map<String, String[]> valuesMap = new HashMap<String, String[]>();
		Object[] valArray = values.toArray();
		for (int i = 0; i < valArray.length; i++) {
			Object value = valArray[i];
			if (value != null) valuesMap.putAll(convertObject(value, key + "[" + i + "]", cache));
		}
		return valuesMap;
	}

	protected static Map<String, String[]> convertMap(Map<Object, Object> values, String key, Set cache) {
		Map<String, String[]> valuesMap = new HashMap<String, String[]>();
		for (Object thisKey : values.keySet()) {
			Object value = values.get(thisKey);
			if (value != null) valuesMap.putAll(convertObject(value, key + "[" + thisKey + "]", cache));
		}
		return valuesMap;
	}

	protected static Map<String, String[]> convertObject(Object value, String key, Set cache) {
		//if this type has a registered converted, then get the string and return
		if (ConverterUtil.getInstance().canConvert(value)) {
			String stringValue = ConverterUtil.getInstance().convert(value);
			Map<String, String[]> valueMap = new HashMap<String, String[]>();
			valueMap.put(key, new String[]{stringValue});
			return valueMap;
		} else {
			//otherwise, treat it as a nested bean that needs to be mapped itself
			return recursiveToMap(value, key, cache);
		}
	}

}
