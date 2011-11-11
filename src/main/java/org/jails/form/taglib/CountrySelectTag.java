package org.jails.form.taglib;

import org.jails.form.constructor.BodyTagInputConstructor;
import org.jails.form.constructor.SelectConstructor;
import org.jails.util.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspTagException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CountrySelectTag
		extends SelectTag {

	private static Logger logger = LoggerFactory.getLogger(CountrySelectTag.class);

	@Override
	protected BodyTagInputConstructor getBodyInputConstructor(SimpleFormTag formTag, RepeaterTag repeatTag, ServletRequest request) throws JspTagException {
		SelectConstructor constructor = new SelectConstructor(this, formTag, repeatTag, request);
		setPrompt("-- Choose a Country --");

		List<Country> countries = Country.getCountries();
		Map<String, String> options = new LinkedHashMap<String, String>();
		for (Country c : countries) {
			options.put(c.getIsoCode(), c.getName());
		}

		constructor.setOptions(options);
		return constructor;
	}
}

