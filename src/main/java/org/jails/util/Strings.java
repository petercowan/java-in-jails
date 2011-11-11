package org.jails.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Strings {
	private static Logger logger = LoggerFactory.getLogger(Strings.class);

	public static boolean isEmpty(String in) {
		return  in != null && in.length() <= 0;
	}

	public static String toCamelCase(String word, String wordSeparator) {

		int index = word.indexOf(wordSeparator);
		while (index > 0) {
			word = word.substring(0, index) + Strings.initCaps(word.substring(index + 1));
			index = word.indexOf(wordSeparator);
		}
		return Strings.initLowercase(word);
	}

	public static String toCamelCase(String word) {
		return toCamelCase(word.replaceAll(" +", " ").trim(), " ");
	}

	/**
	 * Capitalize the first letter of the String
	 *
	 * @param str String to capitalize first letter
	 * @return init capitalized String
	 */
	public static String initCaps(String str) {
		if (str == null) return null;
		String upper = str.substring(0, 1).toUpperCase();
		if (str.length() > 1) {
			return upper + str.substring(1);
		} else {
			return upper;
		}
	}

	/**
	 * Capitalize the first letter of the String
	 *
	 * @param str String to capitalize first letter
	 * @return init capitalized String
	 */
	public static String initLowercase(String str) {
		if (str == null) return null;
		String lower = str.substring(0, 1).toLowerCase();
		if (str.length() > 1) {
			return lower + str.substring(1);
		} else {
			return lower;
		}
	}

	/**
	 * Replaces uppercase letters in a camel case word with
	 * a special character followed by the lowercase equivalent.
	 * <p/>
	 * "camelCaseWord", "_"
	 * becomes
	 * "camel_case_word"
	 *
	 * @param camelCaseWord
	 * @param wordSeparator
	 * @return
	 */
	public static String flattenCamelCase(String camelCaseWord, String wordSeparator) {
		String[] camelCaseWords = splitCamelCase(camelCaseWord);

		StringBuffer flatCamelCase = new StringBuffer();
		int length = camelCaseWords.length;
		for (int i = 0; i < length; i++) {
			String word = camelCaseWords[i];
			flatCamelCase.append(word);
			if (i != length - 1) flatCamelCase.append(wordSeparator);
		}
		return flatCamelCase.toString().toLowerCase();
	}


	/**
	 * Turns a camel case word into a list of words:
	 * <p/>
	 * "camelCaseWord"
	 * becomes
	 * {"camel", "Case", "Word"}
	 *
	 * @param camelCaseWord
	 * @return String[]
	 */
	public static String[] splitCamelCase(String camelCaseWord) {
		//split the String on uppercase letters into a String[]
		String[] camelCaseWords = camelCaseWord.split("(?=[A-Z])");

		logger.debug("split camel case word: " + camelCaseWord + " to array " + camelCaseWords.length);
		return camelCaseWords;
	}


	public static String join(String token, String[] strings) {
		StringBuffer sb = new StringBuffer();

		for (int x = 0; x < (strings.length - 1); x++) {
			sb.append(strings[x]);
			sb.append(token);
		}
		sb.append(strings[strings.length - 1]);

		return (sb.toString());
	}

	private String cleanNumber(String number, boolean allowDecimal) {
		//make sure they entered SOMETHING
		if (isEmpty(number)) {
			return "";
		}

		char[] charsIn = number.toCharArray();

		StringBuffer charsOut = new StringBuffer(number.length());

		for (char currentChar : charsIn) {
			if (Character.isDigit(currentChar) || (allowDecimal && currentChar == '.')) {
				charsOut.append(currentChar).append("");
			}
		}
		return charsOut.toString();

	}

	public String cleanInt(String intString) {
		return cleanNumber(intString, false);
	}

	public String cleanDecimal(String decimalString) {
		return cleanNumber(decimalString, true);
	}

	public static boolean isStrongPassword(String password)
	{
		Pattern p;
		Matcher m;

		//Check that it is at least 7 chars long
		if (password.length() < 7)
		{
			return false;
		}

		//Check that it contains some letters, not enforcing upper and lower case yet
		p = Pattern.compile(".??[a-zA-Z]");
		m = p.matcher(password);
		if (!m.find())
		{
			return false;
		}

		//Check that it contains at least one number
		p = Pattern.compile(".??[0-9]");
		m = p.matcher(password);
		if (!m.find())
		{
			return false;
		}

		//Check that it contains at least one special symbol
		p = Pattern.compile(".??[:,!,@,#,$,%,^,&,*,?,_,-,=,+,~]");
		m = p.matcher(password);
		if (!m.find())
		{
			return false;
		}

		//Check that there are no whitespace chars
		p = Pattern.compile(".??[\\s]");
		m = p.matcher(password);
		if (m.find())
		{
			return false;
		}

		//Check for any other symbols
		p = Pattern.compile(".??[(,),;,|,{,},<,>,\",/,'\\']");
		m = p.matcher(password);
		return !m.find();
	}

	/**
	 * Boolean values can be true/false case-insensitive, t/f, or 1/0.
	 *
	 * @param booleanValue The value to evaluate.
	 * @return The evaluation
	 */
	public static boolean getBoolean(String booleanValue) {
		return booleanValue != null &&
				(booleanValue.equals("1") || booleanValue.equalsIgnoreCase("t") || booleanValue.equalsIgnoreCase("true"));
	}

}
