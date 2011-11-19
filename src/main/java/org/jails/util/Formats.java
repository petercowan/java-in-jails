package org.jails.util;

import java.text.NumberFormat;

public class Formats {
	public static NumberFormat AMOUNT = NumberFormat.getInstance();

	static
	{
		AMOUNT.setMaximumFractionDigits(2);
		AMOUNT.setMinimumFractionDigits(2);
	}

	public static NumberFormat DOLLAR = NumberFormat.getCurrencyInstance();

	static
	{
		DOLLAR.setMaximumFractionDigits(2);
		DOLLAR.setMinimumFractionDigits(2);
	}

	public static NumberFormat DECIMAL = NumberFormat.getInstance();

	static
	{
		DECIMAL.setMaximumFractionDigits(2);
		DECIMAL.setMinimumFractionDigits(2);
		DECIMAL.setGroupingUsed(false);
	}

	public static NumberFormat DIGITS = NumberFormat.getIntegerInstance();

	static
	{
		DIGITS.setGroupingUsed(false);
	}

	public static NumberFormat INT = NumberFormat.getInstance();

	static
	{
		INT.setMaximumFractionDigits(0);
	}

	private Formats() {}

}

