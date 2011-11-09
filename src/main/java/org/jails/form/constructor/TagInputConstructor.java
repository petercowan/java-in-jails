package org.jails.form.constructor;

import org.jails.form.taglib.FormInput;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspTagException;

public abstract class TagInputConstructor<T extends FormInput> extends InputConstructor<T> {

	public TagInputConstructor(T tag, ServletRequest request) throws JspTagException {
		super(tag, request);
	}

	public abstract String getInputHtml();

	@Override
	public String wrapInputHtml(FormInput tag, String inputTagHtml) {
		return (inputTagHtml == null)
				? super.wrapInputHtml(tag, getInputHtml())
				: super.wrapInputHtml(tag, getInputHtml() + inputTagHtml);
	}

	public String wrapInputHtml(FormInput tag) {
		return super.wrapInputHtml(tag, getInputHtml());
	}

}
