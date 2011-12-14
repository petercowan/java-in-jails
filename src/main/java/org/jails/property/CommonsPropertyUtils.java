package org.jails.property;

import org.apache.commons.beanutils.BeanUtils;
import org.jails.property.parser.PropertyParser;
import org.jails.property.parser.SimplePropertyParser;
import org.jails.util.Strings;
import org.jails.util.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class CommonsPropertyUtils implements PropertyUtils<Object> {
    private static Logger logger = LoggerFactory.getLogger(CommonsPropertyUtils.class);
    protected PropertyParser propertyParser;

    public CommonsPropertyUtils() {
        propertyParser = new SimplePropertyParser();
    }

    public CommonsPropertyUtils(PropertyParser propertyParser) {
        this.propertyParser = propertyParser;
    }

    public Object getProperty(Object object, String propertyName) throws PropertyException {
        try {
            return org.apache.commons.beanutils.PropertyUtils.getProperty(object, propertyName);
        } catch (Exception e) {
            throw new PropertyException(e);
        }
    }

    public Object getIndexedProperty(Object object, String propertyName, int propertyIndex) throws PropertyException {
        try {
            return org.apache.commons.beanutils.PropertyUtils.getIndexedProperty(object, propertyName, propertyIndex);
        } catch (Exception e) {
            throw new PropertyException(e);
        }
    }

    public void setProperty(Object object, String propertyName, Object value) throws PropertyException {
        try {
            BeanUtils.setProperty(object, propertyName, value);
        } catch (Exception e) {
            throw new PropertyException(e);
        }
    }

    public Class<?> getPropertyType(Class<?> classType, String property) {
        logger.warn("classType: " + classType + ", property: " + property);
        Class<?> type = getAccessorType(classType, property);
        if (type == null) type = getFieldType(classType, property);
        return type;
    }

    private Class<?> getFieldType(Class<?> classType, String property) {
        try {
            Field field = classType.getField(property);
            return field.getType();
        } catch (NoSuchFieldException e) {
            logger.warn(Strings.getStackTrace(e));
            return null;
        }
    }

    private Class<?> getAccessorType(Class<?> classType, String property) {
        try {
            Class propertyType = null;
            PropertyDescriptor[] pds = org.apache.commons.beanutils.PropertyUtils.getPropertyDescriptors(classType);
            if (pds != null) {
                logger.warn("looping through descriptors: " + pds.length);
                PropertyDescriptor descriptor;
                for (int i = 0; i < pds.length; i++) {
                    if (property.equals(pds[i].getName())) {
                        descriptor = pds[i];
                        propertyType = descriptor.getPropertyType();
                        break;
                    }
                }
            }
            logger.warn("Property: " + property + ". Class: " + propertyType);
            return propertyType;
        } catch (Exception e) {
            logger.warn(Strings.getStackTrace(e));
            return null;
        }
    }

    public Tuple<Class<?>, String> getNestedPropertyType(Class<? extends Object> classType, String property) {
        if (propertyParser.hasNestedProperty(property)) {
            Class nestedClass = classType;
            String nestedProperty = property;
            while (propertyParser.hasNestedProperty(nestedProperty)) {
                String rootParam = propertyParser.getRootProperty(nestedProperty);
                logger.debug("rootParam: " + rootParam);
                nestedClass = getPropertyType(nestedClass, rootParam);
                nestedProperty = propertyParser.getNestedProperty(nestedProperty);
                logger.debug("nestedClass: " + nestedClass + ", nestedProperty: " + nestedProperty);
            }
            return new Tuple<Class<?>, String>(nestedClass, nestedProperty);
        } else {
            return new Tuple<Class<?>, String>(classType, property);
        }
    }

    public Map<String, Object> getProperties(Object object) {
        Map<String, Object> propertyMap = getFields(object, object.getClass(), true);
        //getters take precedence in case of any name collisions
        propertyMap.putAll(getGetters(object));
        return propertyMap;
    }

    private Map<String, Object> getGetters(Object object) {
        Map<String, Object> result = new HashMap<String, Object>();
        BeanInfo info;
        try {
            info = Introspector.getBeanInfo(object.getClass());
            for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
                Method reader = pd.getReadMethod();
                if (reader != null) {
                    String name = pd.getName();
                    if (!"class".equals(name)) {
                        try {
                            Object value = reader.invoke(object);
                            result.put(name, value);
                        } catch (Exception e) {
                            //you can choose to do something here
                        }
                    }
                }
            }
        } catch (IntrospectionException e) {
            //you can choose to do something here
        } finally {
            return result;
        }
    }

    private Map<String, Object> getFields(Object object, Class<?> classType, boolean onlyPublic) {
        Map<String, Object> result = new HashMap<String, Object>();

        Class superClass = classType.getSuperclass();
        if (superClass != null) result.putAll(getFields(object, superClass, onlyPublic));

        //get public fields only
        Field[] fields = (onlyPublic) ? classType.getFields() : classType.getDeclaredFields();
        for (Field field : fields) {
            try {
                result.put(field.getName(), field.get(object));
            } catch (IllegalAccessException e) {
                //you can choose to do something here
            }
        }
        return result;
    }
}
