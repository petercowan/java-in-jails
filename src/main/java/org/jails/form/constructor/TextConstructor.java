package org.jails.form.constructor;

import org.jails.form.taglib.TextInput;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspTagException;

public class TextConstructor extends TagInputConstructor<TextInput> {
	public TextConstructor(TextInput tag, ServletRequest request) throws JspTagException {
		super(tag, request);
	}

	@Override
	public String getInputHtml() {
		return "<input" + getTypeAttr("text") +
				getFieldNameAttr() +
				getInputIdAttr() +
				getClientValidationAttr() +
				getValueAttr(getFieldValue(0)) +
				getAttribute("size", tag.getSize()) + " />";
	}
}

