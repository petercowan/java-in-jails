package org.jails.form.taglib;

import org.jails.form.input.DateSelectConstructor;
import org.jails.form.input.InputElement;
import org.jails.form.input.TagInputConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspTagException;

public class DateSelectTag
		extends FormInputTagSupport
		implements InputElement
{
	private static Logger logger = LoggerFactory.getLogger(DateSelectTag.class);

	@Override
	protected TagInputConstructor getInputConstructor(FormTag formTag, RepeaterTag repeatTag, ServletRequest request) throws JspTagException {
		return new DateSelectConstructor(this, formTag, repeatTag, request);
	}
}
