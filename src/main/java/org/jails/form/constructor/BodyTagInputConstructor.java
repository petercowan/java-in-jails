package org.jails.form.constructor;

import org.jails.form.taglib.FormInput;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspTagException;

public abstract class BodyTagInputConstructor<T extends FormInput> extends InputConstructor<T> {

	public BodyTagInputConstructor(T tag, ServletRequest request) throws JspTagException {
		super(tag, request);
	}

	public abstract String getOpeningHtml();

	public abstract String getClosingHtml();

	@Override
	public String wrapInputHtml(FormInput tag, String inputTagHtml) {
		return super.wrapInputHtml(tag, getOpeningHtml()
										+ inputTagHtml
										+ getClosingHtml());
	}

}
