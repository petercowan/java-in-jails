package org.jails.form.taglib;

import org.jails.form.constructor.SubmitConstructor;
import org.jails.form.constructor.TagInputConstructor;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

public class SubmitButton
		extends FormInputTag {

	@Override
	protected TagInputConstructor getInputConstructor(ServletRequest request) throws JspTagException {
		return new SubmitConstructor(this, request);
	}

	@Override
	public int doStartTag() throws JspException {
		ServletRequest request = pageContext.getRequest();
		getInputConstructor(request);
		return EVAL_PAGE;
	}
}
