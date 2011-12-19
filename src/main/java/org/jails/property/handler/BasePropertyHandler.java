package org.jails.property.handler;

import org.jails.property.AcceptsNestedAttributes;
import org.jails.property.IgnoreNestedAttributes;
import org.jails.property.ReflectionUtil;
import org.jails.property.parser.PropertyParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BasePropertyHandler
        implements PropertyHandler {

    private static Logger logger = LoggerFactory.getLogger(BasePropertyHandler.class);

    public boolean acceptsNestedProperties(Object object, String propertyName) {
        boolean classAcceptsNestedAttributes = ReflectionUtil
                .getClassAnnotation(AcceptsNestedAttributes.class, object.getClass()) != null;
        boolean propertyAcceptsNestedAttributes = ReflectionUtil
                .getFieldAnnotation(AcceptsNestedAttributes.class, object.getClass(), propertyName) != null;
        boolean propertyRejectsNestedAttributes = ReflectionUtil
                .getFieldAnnotation(IgnoreNestedAttributes.class, object.getClass(), propertyName) != null;

        logger.debug("classAcceptsNestedAttributes?" + classAcceptsNestedAttributes);
        logger.debug("propertyAcceptsNestedAttributes?" + propertyAcceptsNestedAttributes);
        logger.debug("propertyRejectsNestedAttributes?" + propertyRejectsNestedAttributes);
        boolean acceptsNestedProperties = (classAcceptsNestedAttributes && !propertyRejectsNestedAttributes)
                || propertyAcceptsNestedAttributes;

        logger.debug("acceptsNestedProperties? " + acceptsNestedProperties);
        return acceptsNestedProperties;
    }

    public abstract Object handleNullNestedProperty(Object object, String property,
                                                    String nestedProperty, String[] valArray,
                                                    PropertyParser propertyParser);
}
