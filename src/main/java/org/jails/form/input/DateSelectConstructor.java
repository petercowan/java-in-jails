package org.jails.form.input;

import org.jails.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateSelectConstructor
		extends TagInputConstructor<InputElement> {
	private static Logger logger = LoggerFactory.getLogger(DateSelectConstructor.class);
	private Calendar calendar;

	public DateSelectConstructor(InputElement tag, FormElement formTag, Repeater repeatTag, ServletRequest request) {
		super(tag, formTag, repeatTag, request);
	}

	@Override
	protected void initFieldValues(ServletRequest request) {
		super.initFieldValues(request);
		if (fieldValues.length > 0) {
			try {
				Date date = new SimpleDateFormat(tag.getFormat()).parse(fieldValues[0]);
				calendar = Calendar.getInstance();
				calendar.setTime(date);
			} catch (ParseException e) {
				logger.warn(e.getMessage());
			}
		}

	}

	@Override
	public String getInputHtml() {
		StringBuffer dateSelect = new StringBuffer();
		logger.debug("Creating DateSelectList");
		String day = (calendar == null) ? "" : calendar.get(Calendar.DATE) + "";
		String month = (calendar == null) ? "" : calendar.get(Calendar.MONTH) + "";
		String year = (calendar == null) ? "" : calendar.get(Calendar.YEAR) + "";

		dateSelect.append(getMonthSelectList(tag.getName() + "_date_month", month));
		dateSelect.append(getDaySelectList(tag.getName() + "_date_day", day));
		dateSelect.append(getYearSelectList(tag.getName() + "_date_year", year));

		return dateSelect.toString();
	}

	private String getSelectList(String name, String value, List<?> optionValues, List<?> optionNames) {
		StringBuffer formElementHtml = new StringBuffer();

		 formElementHtml.append("<select name=\"").append(name).append("\">");

		 String optionValue;
		 String optionName;
		 String selectedValue = (Strings.isEmpty(value)) ? "" : value;
		 for (int i = 0; i < optionValues.size(); i++) {
			 optionValue = optionValues.get(i).toString();
			 optionName = optionNames.get(i).toString();

			 formElementHtml.append("<option value=\"").append(optionValue).append("\"");
			 if (optionValue.equals(selectedValue)) {
				 formElementHtml.append(" SELECTED");
			 }
			 formElementHtml.append("> ").append(optionName).append("\n");
		 }
		 formElementHtml.append("</select>");

		 return formElementHtml.toString();
	}

	public String getDaySelectList(String name, String value) {
		List<String> dayValues = new ArrayList<String>();
		List<String> dayNames = new ArrayList<String>();

		dayValues.add("");
		dayNames.add("Day");
		for (int i = 1; i <= 31; i++) {
			dayValues.add(i + "");
			dayNames.add(i + "");
		}
		logger.debug("Creating Day Select List");
		return getSelectList(name, value, dayValues, dayNames);
	}

	public String getMonthSelectList(String name, String value) {
		List<String> monthValues = new ArrayList<String>();
		List<String> monthNames = new ArrayList<String>();

		monthValues.add("");
		for (int i = 1; i <= 12; i++) {
			monthValues.add(i + "");
		}

		monthNames.add("Month");
		monthNames.add("Jan");
		monthNames.add("Feb");
		monthNames.add("Mar");
		monthNames.add("Apr");
		monthNames.add("May");
		monthNames.add("Jun");
		monthNames.add("Jul");
		monthNames.add("Aug");
		monthNames.add("Sep");
		monthNames.add("Oct");
		monthNames.add("Nov");
		monthNames.add("Dec");

		logger.debug("Creating Month Select List");
		return getSelectList(name, value, monthValues, monthNames);
	}

	public String getYearSelectList(String name, String value) {

		List<String> yearValues = new ArrayList<String>();
		List<String> yearNames = new ArrayList<String>();

		yearValues.add("");
		yearNames.add("Year");

		Date now = new Date();
		Calendar nowCal = Calendar.getInstance();
		now.setTime(now.getTime());

		int thisYear = nowCal.get(Calendar.YEAR);
		int start = thisYear;
		int end = start + 1;

		for (int i = start; i <= end; i++) {
			yearValues.add(i + "");
			yearNames.add(i + "");
		}
		logger.debug("Creating Year Select List");
		return getSelectList(name, value, yearValues, yearNames);
	}
}


