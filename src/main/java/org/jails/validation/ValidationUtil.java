package org.jails.validation;

import org.jails.util.Strings;
import org.jails.validation.constraint.FieldMatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtil {
	private static Logger logger = LoggerFactory.getLogger(ValidationUtil.class);

	public static <T> Map<String, List<String>> getErrorFieldsMap(Set<ConstraintViolation<T>> constraintViolations) {

		Map<String, List<String>> errorFieldsMap = new HashMap<String, List<String>>();
		if (constraintViolations != null && constraintViolations.size() > 0) {
			logger.info("Found " + constraintViolations.size() + " constraint violations");
			for (ConstraintViolation<T> c : constraintViolations) {
				String propertyName = c.getPropertyPath().toString();
				if (Strings.isEmpty(propertyName)) {
					 handleClassConstraint(c, errorFieldsMap);
				} else {
					logger.info("Error property " + propertyName);
					getFieldErrors(propertyName, errorFieldsMap);
					String errorMessage = c.getMessage();
					errorFieldsMap.get(propertyName).add(errorMessage);
				}
			}
		}
		return errorFieldsMap;
	}

	private static List<String> getFieldErrors(String propertyName, Map<String, List<String>> errorFieldsMap) {
		List<String> fieldsErrors = errorFieldsMap.get(propertyName);
		if (fieldsErrors == null) {
			fieldsErrors = new ArrayList<String>();
			errorFieldsMap.put(propertyName, fieldsErrors);
		}
		return fieldsErrors;
	}

	private static <T> void handleClassConstraint(ConstraintViolation<T> c, Map<String, List<String>> errorFieldsMap) {
		Annotation a = c.getConstraintDescriptor().getAnnotation();
		if (a instanceof FieldMatch) {
			FieldMatch fieldMatch = (FieldMatch) a;
			logger.warn("FieldMatch Error!!! " + fieldMatch.field() + ", " + fieldMatch.matchField());

			getFieldErrors(fieldMatch.field(), errorFieldsMap);
			getFieldErrors(fieldMatch.matchField(), errorFieldsMap);
			String errorMessage = c.getMessage();
			errorFieldsMap.get(fieldMatch.field()).add(errorMessage);
			errorFieldsMap.get(fieldMatch.matchField()).add("");
		}
	}

	public static String replaceTokens(String tokenString, Map<String, ? extends Object> attributeNames) {
		String content = tokenString;
		for (String attributeName : attributeNames.keySet()) {
			String attributeRegex = "\\$\\{" + attributeName + "\\}";
			String formFieldRegex = "\\$\\{form\\." + attributeName + "\\.id\\}";
			Pattern attributePattern = Pattern.compile(attributeRegex);
			Pattern formFieldPattern = Pattern.compile(formFieldRegex);
			Matcher formFieldMatcher = formFieldPattern.matcher(tokenString);
			Matcher attributeMatcher = attributePattern.matcher(tokenString);
			if (formFieldMatcher.find()) {
				logger.debug("Matched " + formFieldRegex);
				Object attributeValue = attributeNames.get(attributeName);
				logger.info("attributeValue " + attributeValue);
				if (attributeValue != null)
					content = content.replaceFirst(formFieldRegex, attributeValue.toString() + ".id");
			} else if (attributeMatcher.find()) {
				logger.debug("Matched " + attributeRegex);
				Object attributeValue = attributeNames.get(attributeName);
				logger.debug("attributeValue " + attributeValue);
				if (attributeValue != null)
					content = content.replaceFirst(attributeRegex, attributeValue.toString());
			}
		}
		return content;
	}
}
