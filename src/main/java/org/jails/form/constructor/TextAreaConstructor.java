package org.jails.form.constructor;

import org.jails.form.taglib.RepeaterTag;
import org.jails.form.taglib.SimpleFormTag;
import org.jails.form.taglib.TextAreaTag;

import javax.servlet.ServletRequest;

public class TextAreaConstructor extends TagInputConstructor<TextAreaTag> {
	public TextAreaConstructor(TextAreaTag tag, SimpleFormTag formTag, RepeaterTag repeatTag, ServletRequest request) {
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


