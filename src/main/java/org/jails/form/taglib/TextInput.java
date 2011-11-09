package org.jails.form.taglib;

import org.jails.form.constructor.TagInputConstructor;
import org.jails.form.constructor.TextConstructor;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspTagException;

public class TextInput
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
		return new TextConstructor(this, request);
	}
}
