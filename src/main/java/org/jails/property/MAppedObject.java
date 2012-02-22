package org.jails.property;

import org.jails.property.parser.PropertyParser;
import org.jails.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MappedObject {
    private static Logger logger = LoggerFactory.getLogger(MappedObject.class);

    private Object object;
    private String property;
    private String propertyName;
    private Integer propertyIndex;
    private String rootProperty;
    private String nestedProperty;
    private PropertiesWrapper propertiesMap;
    private String propertyValueKey;
    private String propertyStem;

    private PropertyUtils propertyUtils;
    private PropertyParser propertyParser;

    public MappedObject(Object object, String property, PropertiesWrapper propertiesMap,
                        String propertyStem, PropertyParser propertyParser, PropertyUtils propertyUtils) {
        this.object = object;
        this.property = property;
        this.propertiesMap = propertiesMap;
        this.propertyStem = propertyStem;
        this.propertyUtils = propertyUtils;
        this.propertyParser = propertyParser;

        //use this to retrieve value from propertiesMap
        propertyValueKey = propertyStem + "." + property;

        rootProperty = propertyParser.getRootProperty(property);
        nestedProperty = propertyParser.getNestedProperty(property);

        //strip index from property, if it exists
        propertyName = propertyParser.getPropertyName(rootProperty);

        if (propertyParser.isIndexed(property)) propertyIndex = propertyParser.getPropertyIndex(property);
    }

    public MappedObject getNestedMappedObject(Object object) {
        return new MappedObject(object, getNestedProperty(),
                getPropertiesMap(),
                getNestedPropertyStem(),
                propertyParser, propertyUtils);

    }

    public Object getObject() {
        return object;
    }

    public String getProperty() {
        return property;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Integer getPropertyIndex() {
        return propertyIndex;
    }

    public boolean hasPropertyIndex() {
        return propertyIndex != null;
    }

    public String getRootProperty() {
        return rootProperty;
    }

    public String getNestedProperty() {
        return nestedProperty;
    }

    public boolean hasNestedProperty() {
        return nestedProperty != null;
    }

    public PropertiesWrapper getPropertiesMap() {
        return propertiesMap;
    }

    public String getPropertyValueKey() {
        return propertyValueKey;
    }

    public String getPropertyStem() {
        return propertyStem;
    }

    public String getNestedPropertyStem() {
        return propertyStem + "." + rootProperty;
    }

    public String[] getPropertyMapValues()  {
        return propertiesMap.get(propertyValueKey);
    }

    public String getPropertyMapValue()  {
        return propertiesMap.getValue(propertyValueKey);
    }

    public String derivePropertyMapValue() throws PropertyException {
        logger.info("propertyValueKey: " + getPropertyValueKey());
        String value = propertiesMap.getValue(propertyValueKey);
        logger.info("Object's " + propertyName + " value: " + propertyUtils.getProperty(object, propertyName));
        logger.info("Setting " + propertyName + " value to |" + value + "|");

        if (value == null) value = propertiesMap.getSelectOtherValue(propertyValueKey);
        if (value == null)  value = propertiesMap.composeDate(propertyValueKey);

        return value;
    }

    public String cleanPropertyMapValue() throws PropertyException {
        String value = derivePropertyMapValue();
        //process value based on underlying Object type
        Class<?> returnType = propertyUtils
                .getPropertyType(getObject().getClass(), getPropertyName());
        logger.info("returnType: " + returnType);
        if (ReflectionUtil.isInteger(returnType)) {
            logger.info("Cleaning int value");
            value = Strings.cleanInt(value);
        } else if (ReflectionUtil.isDecimal(returnType)) {
            logger.info("Cleaning decimal value");
            value = Strings.cleanDecimal(value);
        }
        return value;
    }
}
