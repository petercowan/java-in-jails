package org.jails.form.taglib;

import org.jails.form.constructor.BodyTagInputConstructor;
import org.jails.form.constructor.SelectConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspTagException;

public class SelectTag
		extends FormInputBodyTagSupport {

	private static Logger logger = LoggerFactory.getLogger(SelectTag.class);

	private String multiple = "no";

	public String getMultiple() {
		return multiple;
	}

	public void setMultiple(String multiple) {
		this.multiple = multiple;
	}

	@Override
	protected BodyTagInputConstructor getBodyInputConstructor(SimpleFormTag formTag, RepeaterTag repeatTag, ServletRequest request) throws JspTagException {
		return new SelectConstructor(this, formTag, repeatTag, request);
	}
}

