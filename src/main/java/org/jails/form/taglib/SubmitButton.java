package org.jails.form.taglib;

import org.jails.form.constructor.SubmitConstructor;
import org.jails.form.constructor.TagInputConstructor;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

public class SubmitButton
		extends FormInputTagSupport {

	@Override
	protected TagInputConstructor getInputConstructor(SimpleFormTag formTag, RepeaterTag repeatTag, ServletRequest request) throws JspTagException {
		return new SubmitConstructor(this, formTag, repeatTag, request);
	}

	@Override
	public int doStartTag() throws JspException {
		super.doStartTag();
		ServletRequest request = pageContext.getRequest();
		getInputConstructor(formTag, repeatTag, request);
		return EVAL_PAGE;
	}
}
