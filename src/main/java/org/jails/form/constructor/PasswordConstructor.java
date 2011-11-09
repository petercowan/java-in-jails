package org.jails.form.constructor;

import org.jails.form.taglib.PasswordInput;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspTagException;

public class PasswordConstructor extends TagInputConstructor<PasswordInput> {
	public PasswordConstructor(PasswordInput tag, ServletRequest request) throws JspTagException {
		super(tag, request);
	}

	@Override
	public String getInputHtml() {
		return "<input" + getTypeAttr("password") +
				getFieldNameAttr() +
				getInputIdAttr() +
				getClientValidationAttr() +
				getValueAttr(getFieldValue(0)) +
				getAttribute("size", tag.getSize()) + " />";
	}
}

