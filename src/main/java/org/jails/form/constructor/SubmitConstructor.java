package org.jails.form.constructor;

import org.jails.form.taglib.SubmitButton;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspTagException;

public class SubmitConstructor extends  TagInputConstructor<SubmitButton> {

	public SubmitConstructor(SubmitButton tag, ServletRequest request) throws JspTagException {
		super(tag, request);
		formTag.setSubmitButton(getInputHtml());
	}

	@Override
	public String getInputHtml() {
		return "<input" + getTypeAttr("submit") +
				getAttribute("name", "submit") +
				getInputIdAttr() +
				getClientValidationAttr() +
				getValueAttr(tag.getLabel()) + " />";
	}

}
