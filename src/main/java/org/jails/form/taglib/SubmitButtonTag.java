package org.jails.form.taglib;

import org.jails.form.input.SubmitButtonInput;
import org.jails.form.input.SubmitConstructor;
import org.jails.form.input.TagInputConstructor;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

public class SubmitButtonTag
		extends FormInputTagSupport implements SubmitButtonInput {

	public SubmitButtonTag() {
		name = "submit";
	}

	@Override
	protected TagInputConstructor getInputConstructor(FormTag formTag, RepeaterTag repeatTag, ServletRequest request) throws JspTagException {
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
