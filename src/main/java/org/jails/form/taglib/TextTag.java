package org.jails.form.taglib;

import org.jails.form.constructor.TagInputConstructor;
import org.jails.form.constructor.TextConstructor;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspTagException;

public class TextTag
		extends FormInputTagSupport {

	private String size = "25";

	public void setSize(String size) {
		this.size = size;
	}

	public String getSize() {
		return size;
	}

	@Override
	protected TagInputConstructor getInputConstructor(SimpleFormTag formTag, RepeaterTag repeatTag, ServletRequest request) throws JspTagException {
		return new TextConstructor(this, formTag, repeatTag, request);
	}
}
