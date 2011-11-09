package org.jails.form.taglib;

import org.jails.form.constructor.BodyTagInputConstructor;
import org.jails.form.constructor.SelectConstructor;
import org.jails.util.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspTagException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StateSelectTag
		extends SelectTag {

	private static Logger logger = LoggerFactory.getLogger(StateSelectTag.class);

	private String country;

	public void setStates(String states) {
		this.country = states;
	}

	@Override
	protected BodyTagInputConstructor getBodyInputConstructor(SimpleFormTag formTag, RepeaterTag repeatTag, ServletRequest request) throws JspTagException {
		SelectConstructor constructor = new SelectConstructor(this, formTag, repeatTag, request);

		List<State> states = ("US".equalsIgnoreCase(country))
				? State.getUSStates()
				: State.getStates();
		Map<String, String> options = new LinkedHashMap<String, String>();
		for (State s : states) {
			options.put(s.getIsoCode(), s.getName());
		}

		constructor.setOptions(options);
		return constructor;
	}
}


