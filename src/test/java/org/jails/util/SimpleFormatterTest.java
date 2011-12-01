package org.jails.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class SimpleFormatterTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public SimpleFormatterTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(SimpleFormatterTest.class);
	}

	public void testDateFormat() {
		SimpleFormatter simpleFormat = new SimpleFormatter();
		String dateString = simpleFormat.format("Sun Oct 11 11:11:11 PST 2012", "@MM-dd-yyyy");
		System.out.println("dateString: " + dateString);
		assertEquals("dateFormat", dateString, "10-11-2012");
	}

	public void testNumberFormat() {
		SimpleFormatter simpleFormat = new SimpleFormatter();

		String intString = simpleFormat.format("1234.34", "1");
		System.out.println("intString: " + intString);
		assertEquals("intString", intString, "1234");

		String moneyIntString = simpleFormat.format("1234", "$1000");
		System.out.println("moneyIntString: " + moneyIntString);
		assertEquals("moneyIntString", moneyIntString, "$1234");

		String niceIntString = simpleFormat.format("1234", "1,000");
		System.out.println("niceIntString: " + niceIntString);
		assertEquals("niceIntString", niceIntString, "1,234");

		String paddedIntString = simpleFormat.format("12", "001");
		System.out.println("paddedIntString: " + paddedIntString);
		assertEquals("paddedIntString", paddedIntString, "012");

		String niceMoneyIntString = simpleFormat.format("1234", "$1,000");
		System.out.println("niceMoneyIntString: " + niceMoneyIntString);
		assertEquals("niceMoneyIntString", niceMoneyIntString, "$1,234");

		String paddedMoneyIntString = simpleFormat.format("1234", "$000001");
		System.out.println("paddedMoneyIntString: " + paddedMoneyIntString);
		assertEquals("paddedMoneyIntString", paddedMoneyIntString, "$001234");

		String nicePaddedIntString = simpleFormat.format("1234", "001,000");
		System.out.println("nicePaddedIntString: " + nicePaddedIntString);
		assertEquals("nicePaddedIntString", nicePaddedIntString, "001,234");

		String nicePaddedMoneyIntString = simpleFormat.format("1234", "$001,000");
		System.out.println("nicePaddedMoneyIntString: " + nicePaddedMoneyIntString);
		assertEquals("nicePaddedMoneyIntString", nicePaddedMoneyIntString, "$001,234");

		String floatString = simpleFormat.format("1234.567", "1.00");
		System.out.println("floatString: " + floatString);
		assertEquals("floatString", floatString, "1234.57");

		String moneyFloatString = simpleFormat.format("1234.567", "$1000.0");
		System.out.println("moneyFloatString: " + moneyFloatString);
		assertEquals("moneyFloatString", moneyFloatString, "$1234.6");

		String niceFloatString = simpleFormat.format("1234.567", "1,000.00");
		System.out.println("niceFloatString: " + niceFloatString);
		assertEquals("niceFloatString", niceFloatString, "1,234.57");

		String paddedFloatString = simpleFormat.format("1234.567", "00001.000");
		System.out.println("paddedFloatString: " + paddedFloatString);
		assertEquals("paddedFloatString", paddedFloatString, "01234.567");

		String niceMoneyFloatString = simpleFormat.format("1234.567", "$1,000.00");
		System.out.println("niceMoneyFloatString: " + niceMoneyFloatString);
		assertEquals("niceMoneyFloatString", niceMoneyFloatString, "$1,234.57");

		String paddedMoneyFloatString = simpleFormat.format("1234.567", "$001000.00");
		System.out.println("paddedMoneyFloatString: " + paddedMoneyFloatString);
		assertEquals("paddedMoneyFloatString", paddedMoneyFloatString, "$001234.57");

		String nicePaddedFloatString = simpleFormat.format("1234.567", "001,000.00");
		System.out.println("nicePaddedFloatString: " + nicePaddedFloatString);
		assertEquals("nicePaddedFloatString", nicePaddedFloatString, "001,234.57");

		String nicePaddedMoneyFloatString = simpleFormat.format("1234.567", "$001,000.00");
		System.out.println("nicePaddedMoneyFloatString: " + nicePaddedMoneyFloatString);
		assertEquals("nicePaddedMoneyFloatString", nicePaddedMoneyFloatString, "$001,234.57");
	}
}

