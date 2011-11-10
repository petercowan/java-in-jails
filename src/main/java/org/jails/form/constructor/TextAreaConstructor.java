package org.jails.form.constructor;

import org.jails.form.TextareaInput;
import org.jails.form.taglib.RepeaterTag;
import org.jails.form.taglib.SimpleFormTag;

import javax.servlet.ServletRequest;

public class TextAreaConstructor extends TagInputConstructor<TextareaInput> {
	public TextAreaConstructor(TextareaInput tag, SimpleFormTag formTag, RepeaterTag repeatTag, ServletRequest request) {
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


