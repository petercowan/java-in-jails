package org.jails.form.taglib;

import org.jails.form.input.TagInputConstructor;
import org.jails.form.input.TextConstructor;
import org.jails.form.input.TextInput;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspTagException;

public class TextTag
		extends FormInputTagSupport
		implements TextInput {

	private String size = "25";

	public void setSize(String size) {
		this.size = size;
	}

	public String getSize() {
		return size;
	}

	@Override
	protected TagInputConstructor getInputConstructor(FormTag formTag, RepeaterTag repeatTag, ServletRequest request) throws JspTagException {
		return new TextConstructor(this, formTag, repeatTag, request);
	}
}
