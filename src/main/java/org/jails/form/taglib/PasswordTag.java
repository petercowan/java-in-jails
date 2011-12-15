package org.jails.form.taglib;

import org.jails.form.input.PasswordConstructor;
import org.jails.form.input.PasswordInput;
import org.jails.form.input.TagInputConstructor;

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
	protected TagInputConstructor getInputConstructor(FormTag formTag, RepeaterTag repeatTag, ServletRequest request) throws JspTagException {
		return new PasswordConstructor(this, formTag, repeatTag, request);
	}
}
