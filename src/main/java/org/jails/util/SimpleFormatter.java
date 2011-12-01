package org.jails.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <code>SimpleFormatter</code> formats Strings based on simple format patterns passed to the format method.
 * Currently only dates and numbers are supportted.
 *
 * Date formats use the same formatting pattern as SimpleDateFormat, however you must precede the pattern
 * with an '@' to indicate to SimpleFormatter that the value to be formatted is a date.
 *
 * ex: @MM-dd-yyyy
 *
 * Number formats use flexible, custom pattern that allows yuo to specify number of fraction digits,
 * currency, groupings, and front padding. You basically just provide a number that looks like what you
 * want yours to look like:
 *
 * 100
 * 100.00
 * $100.00
 * $1,000.00
 * 001.00
 * 000,100.00
 *
 * The rules are as follows:
 *
 *  1. Begin with a '$' to indicate currency
 *  2. Add a '.' and trailing digits to indicate how many fraction digits you want
 *  3. Lead with one or more '0's and the total number of digits to the left of the (optional) decimal will indicate the
 *  minimum integer digits
 *  4. Add a ',' anywhere after the first character to indicate the use of digit groups.
 */
public class SimpleFormatter {
	private static Logger logger = LoggerFactory.getLogger(SimpleFormatter.class);
	private String defaultDatePattern = "EEE MMM dd hh:mm:ss zzz yyyy";

	public SimpleFormatter() {}

	public SimpleFormatter(String defaultDatePattern) {
		this.defaultDatePattern = defaultDatePattern;
	}

	public String format(String value, String formatString) {
		if (formatString.matches("\\$?[0-9],?[0-9,.]*")) {
			logger.info("formatting number");
			boolean isDecimal = formatString.contains(".");
			logger.info("isDecimal? " + isDecimal);
			boolean isCurrency = formatString.startsWith("$");
			logger.info("isCurrency? " + isCurrency);
			boolean useGrouping = formatString.contains(",");
			logger.info("useGrouping? " + useGrouping);
			boolean useLeadingZeros = Strings.cleanDecimal(formatString).indexOf("0") == 0;
			logger.info("useLeadingZeros? " + useLeadingZeros);

			NumberFormat numberFormat;

			if (isCurrency) numberFormat = NumberFormat.getCurrencyInstance();
			else numberFormat = NumberFormat.getIntegerInstance();

			if (useGrouping) numberFormat.setGroupingUsed(true);
			else numberFormat.setGroupingUsed(false);

			int fractionDigits = 0;
			if (isDecimal) {
				String decimal = getFraction(formatString);
				fractionDigits = decimal.getBytes().length;
				logger.info("setFractionDigits? " + fractionDigits);
				numberFormat.setMaximumFractionDigits(fractionDigits);
				numberFormat.setMinimumFractionDigits(fractionDigits);
			} else {
				numberFormat.setParseIntegerOnly(true);
			}

			if (useLeadingZeros) {
				String integer = getInt(formatString);
				int intDigits = integer.getBytes().length;
				logger.info("setMinimumIntegerDigits? " + intDigits);
				numberFormat.setMinimumIntegerDigits(intDigits);
			}

			//format number
			float floatValue;
			try {
				logger.info("before: " + value);
				logger.info("clean: " + Strings.cleanDecimal(value));
				floatValue = Float.parseFloat(Strings.cleanDecimal(value));
				logger.info("after: " + floatValue);

				String formattedValue = numberFormat.format(floatValue);
				logger.info("formatted: " + formattedValue);
				if (!isDecimal && formattedValue.contains(".")) {
					formattedValue = formattedValue.substring(0, formattedValue.indexOf("."));
					logger.info("trimmed decimal: " + formattedValue);
				} else if (isDecimal && isCurrency) {
					formattedValue = formattedValue.substring(0, formattedValue.lastIndexOf("."))
							+ "." + getFraction(formattedValue).substring(0, fractionDigits);
					logger.info("shorten decimal: " + formattedValue);
				}
				return formattedValue;
			} catch (NumberFormatException e) {
				logger.warn(e.getMessage());
				return value;
			}

		} else if (formatString.startsWith("@")) {
			//format date
			String datePattern = formatString.substring(1);
			try {
				Date date = new SimpleDateFormat(defaultDatePattern).parse(value);
				String dateString = new SimpleDateFormat(datePattern).format(date);
				return dateString;
			} catch (ParseException e) {
				logger.warn(e.getMessage());
			}
		} else {
			logger.info("not formatting value");
		}
		return value;
	}

	private String getInt(String decimal) {
		String integer = (decimal.indexOf(".") > 0)
				? Strings.cleanDecimal(decimal.substring(0, decimal.lastIndexOf(".")))
				: Strings.cleanInt(decimal);

		return integer;
	}

	private String getFraction(String decimal) {
		String integer = (decimal.indexOf(".") > 0)
				? decimal.substring(decimal.lastIndexOf(".") + 1)
				: "";
		return integer;
	}

}
