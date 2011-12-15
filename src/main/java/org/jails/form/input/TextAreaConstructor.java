package org.jails.form.input;

import javax.servlet.ServletRequest;

public class TextAreaConstructor extends TagInputConstructor<TextareaInput> {
	public TextAreaConstructor(TextareaInput tag, FormElement formTag, Repeater repeatTag, ServletRequest request) {
		super(tag, formTag, repeatTag, request);
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
				getFieldValue(0) +
				"</textarea>";
	}
}


