package org.jails.form.taglib;

import org.jails.form.PasswordInput;
import org.jails.form.constructor.PasswordConstructor;
import org.jails.form.constructor.TagInputConstructor;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspTagException;

public class PasswordTag
		extends FormInputTagSupport
		implements PasswordInput {

	private String size = "25";

	public void setSize(String size) {
		this.size = size;
	}

	public String getSize() {
		return size;
	}

	@Override
	protected TagInputConstructor getInputConstructor(SimpleFormTag formTag, RepeaterTag repeatTag, ServletRequest request) throws JspTagException {
		return new PasswordConstructor(this, formTag, repeatTag, request);
	}
}
