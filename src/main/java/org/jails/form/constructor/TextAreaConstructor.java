package org.jails.form.constructor;

import org.jails.form.taglib.TextAreaInput;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspTagException;

public class TextAreaConstructor extends TagInputConstructor<TextAreaInput> {
	public TextAreaConstructor(TextAreaInput tag, ServletRequest request) throws JspTagException {
		super(tag, request);
	}

	@Override
	public String getInputHtml() {
		return "<textarea" +
				getFieldNameAttr() +
				getInputIdAttr() +
				getClientValidationAttr() +
				getAttribute("rows", tag.getRows()) +
				getAttribute("cols", tag.getCols()) +
				">" +
				getValueAttr(getFieldValue(0)) +
				"<textarea/>";
	}
}


