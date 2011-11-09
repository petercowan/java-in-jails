package org.jails.form.taglib;

import org.jails.form.constructor.PasswordConstructor;
import org.jails.form.constructor.TagInputConstructor;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspTagException;

public class PasswordInput
		extends FormInputTag {

	private String size = "25";

	public void setSize(String size) {
		this.size = size;
	}

	public String getSize() {
		return size;
	}

	@Override
	protected TagInputConstructor getInputConstructor(ServletRequest request) throws JspTagException {
		return new PasswordConstructor(this, request);
	}
}
