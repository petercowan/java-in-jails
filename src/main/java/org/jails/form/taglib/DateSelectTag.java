package org.jails.form.taglib;

import org.jails.form.FormInput;
import org.jails.form.constructor.DateSelectConstructor;
import org.jails.form.constructor.TagInputConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspTagException;

public class DateSelectTag
		extends FormInputTagSupport
		implements FormInput
{
	private static Logger logger = LoggerFactory.getLogger(DateSelectTag.class);

	@Override
	protected TagInputConstructor getInputConstructor(SimpleFormTag formTag, RepeaterTag repeatTag, ServletRequest request) throws JspTagException {
		return new DateSelectConstructor(this, formTag, repeatTag, request);
	}
}
