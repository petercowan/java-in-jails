package org.jails.form.constructor;

import org.jails.form.FormTag;
import org.jails.form.Repeater;
import org.jails.form.TextInput;

import javax.servlet.ServletRequest;

public class DateConstructor extends TagInputConstructor<TextInput> {
	public DateConstructor(TextInput tag, FormTag formTag, Repeater repeatTag, ServletRequest request) {
		super(tag, formTag, repeatTag, request);
	}

	@Override
	public String getInputHtml() {
		String dateTag = "<input" + getTypeAttr("text") +
				getFieldNameAttr() + "_date_picker" +
				getInputIdAttr() + "_date_picker" +
				getValueAttr("") +
				getAttribute("size", tag.getSize()) + " />";

		String hiddenTag = "<input" + getTypeAttr("hidden") +
				getFieldNameAttr() +
				getInputIdAttr() +
				getValueAttr(getFieldValue(0)) + " />";

		return dateTag + hiddenTag;
	}
}

