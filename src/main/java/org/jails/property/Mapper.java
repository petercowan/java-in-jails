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

public class Mapper {
	protected static Logger logger = LoggerFactory.getLogger(Mapper.class);

	protected PropertyHandler propertyHandler;
	protected PropertyParser propertyParser;

	static {
		ConverterUtil.getInstance();
	}

	private Mapper(){}

	/**
	 *
	 * @param propertyParser
	 */
	public Mapper(PropertyParser propertyParser) {
		this.propertyParser = propertyParser;
	}

	/**
	 *
	 * @param propertyParser
	 * @param propertyHandler
	 */
	public Mapper(PropertyParser propertyParser, PropertyHandler propertyHandler) {
		this.propertyHandler = propertyHandler;
		this.propertyParser = propertyParser;
	}

	/**
	 *
	 * @param classType
	 * @param propertiesMap
	 * @param <T>
	 * @return
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
	 * @param propertiesMap
	 */
	public void toExistingObject(Object object, Map<String, String[]> propertiesMap) {

		for (String rawProperty : propertiesMap.keySet()) {
			String[] valuesArray = propertiesMap.get(rawProperty);

			setValues(object, rawProperty, valuesArray);
		}
	}

	/**
	 *
	 * @param classType
	 * @param paramMap
	 * @param <T>
	 * @return
	 */
	public <T> List<T> toList(Class<T> classType, Map<String, String[]> paramMap) {
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

		_toExistingList(objects, multiMap);

		return objects;
	}

	/**
	 *
	 * @param objects
	 * @param paramMap
	 */
	public void toExistingList(List<?> objects, Map<String, String[]> paramMap) {
		PropertiesMultiMap multiMap = PropertiesMultiMap.getMultiMap(paramMap);

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
	 *
	 * @param object
	 * @param rawProperty
	 * @param valArray
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
	 *
	 * @param object
	 * @param rawProperty
	 * @param value
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
					logger.info("Setting " + propertyName + " value to |" + value + "|");
					if (value != null) logger.info("valueClass: " + value.getClass());
					BeanUtils.setProperty(object, propertyName, value);
				} catch (Exception e) {
					logger.warn(e.getMessage());
				}
			}
		}
	}

	protected void handleMemberObject(Object object, Object memberObject, String rootPropertyName, String nestedProperty,
									  String[] valArray, PropertyParser propertyParser) {
		logger.info("memberObject: " + memberObject);

		//memberObject is null, so attempt find an instance, using the PropertyHandler
		if (memberObject == null && propertyHandler != null) {
			memberObject = propertyHandler.handleNullNestedProperty(object, rootPropertyName, nestedProperty, valArray, propertyParser);
		}

		if (memberObject != null) {
			logger.info("memberObject Class: " + memberObject.getClass());
			_setValues(memberObject, nestedProperty, valArray);
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
		return toMap(object, null);
	}

	/**
	 *
	 * @param objects
	 * @return
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
			Method[] methods = bean.getClass().getMethods();

			Map<Method, String> getters = new HashMap<Method, String>();
			for (int i = 0; i < methods.length; i++) {
				Method method = methods[i];
				if (method.getParameterTypes() == null || method.getParameterTypes().length == 0
						&& !method.getReturnType().equals(Void.TYPE)) {
					if ((method.getName().startsWith("get") || method.getName().startsWith("is"))
							&& !"getClass".equals(method.getName())) {
						String property = (method.getName().startsWith("get"))
								? Strings.initLowercase(method.getName().substring(3))
								: Strings.initLowercase(method.getName().substring(2));
						getters.put(method, property);
					}
				}
			}

			for (Method method : getters.keySet()) {
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
							paramMap.put(prefix + getters.get(method) + index, new String[]{ConverterUtil.getInstance().convert(value)});
						} else {
							Map<String, String[]> nestedMap = _toMap(value, prefix + getters.get(method) + index, cache);
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
	 *
	 * @param object
	 * @param rawProperty
	 * @return
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
