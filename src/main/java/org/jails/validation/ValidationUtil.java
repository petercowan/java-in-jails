package org.jails.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
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
				logger.info("Error property " + propertyName);
				List<String> fieldsErrors = errorFieldsMap.get(propertyName);
				if (fieldsErrors == null) {
					fieldsErrors = new ArrayList<String>();
					errorFieldsMap.put(propertyName, fieldsErrors);
				}
				String errorMessage = c.getMessage();
				logger.info("Error messageTemplate: " + c.getMessageTemplate());

				logger.info("Error message " + errorMessage);
				errorFieldsMap.get(propertyName).add(errorMessage);
			}
		}
		return errorFieldsMap;
	}

	public static String replaceTokens(String tokenString, Map<String,? extends Object> attributeNames) {
		String content = tokenString;
		for (String attributeName : attributeNames.keySet()) {
			String attributeRegex = "\\$\\{" + attributeName + "\\}";
			String formFieldRegex = "\\$\\{form\\." + attributeName + "\\.id\\}";
			Pattern attributePattern = Pattern.compile(attributeRegex);
			Pattern formFieldPattern = Pattern.compile(formFieldRegex);
			Matcher formFieldMatcher = formFieldPattern.matcher(tokenString);
			Matcher attributeMatcher = attributePattern.matcher(tokenString);
			if (formFieldMatcher.find()) {
				logger.info("Matched " + formFieldRegex);
				Object attributeValue = attributeNames.get(attributeName);
				logger.info("attributeValue " + attributeValue);
				if (attributeValue != null)
					content = content.replaceFirst(formFieldRegex, attributeValue.toString() + ".id");
			} else if (attributeMatcher.find()) {
				logger.info("Matched " + attributeRegex);
				Object attributeValue = attributeNames.get(attributeName);
				logger.info("attributeValue " + attributeValue);
				if (attributeValue != null)
					content = content.replaceFirst(attributeRegex, attributeValue.toString());
			}
		}
		return content;
	}
}
