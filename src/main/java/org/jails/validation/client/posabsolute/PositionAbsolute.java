package org.jails.validation.client.posabsolute;

public class PositionAbsolute {
	public static String REQUIRED = "required";
	public static String CREDIT_CARD = "creditCard";
	public static String EMAIL = "custom[email]";
	public static String MIN_SIZE = "minSize[${min}],maxSize[${max}]";
	public static String MAX_SIZE = "max[${max}]";
	public static String MIN_VALUE = "min[${value}]";
	public static String MAX_VALUE = "max[${value}]";
	public static String PAST = "past[now]";
	public static String FUTURE = "future[now]";
	public static String EQUALS = "equals[${field}]";
	public static String INTEGER = "custom[integer]";
	public static String NUMBER = "custom[number]";
	public static String URL = "custom[url]";

	public static String VALIDATION_FUNCTION = "validate";
	public static String INPUT_ATTRIBUTE = "class";

}
