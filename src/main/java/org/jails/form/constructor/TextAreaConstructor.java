package org.jails.form.constructor;

import org.jails.form.FormTag;
import org.jails.form.Repeater;
import org.jails.form.TextareaInput;

import javax.servlet.ServletRequest;

public class TextAreaConstructor extends TagInputConstructor<TextareaInput> {
	public TextAreaConstructor(TextareaInput tag, FormTag formTag, Repeater repeatTag, ServletRequest request) {
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
				getValueAttr(getFieldValue(0)) +
				"<textarea/>";
	}
}


