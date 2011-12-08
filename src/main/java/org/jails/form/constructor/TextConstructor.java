package org.jails.form.constructor;

import org.jails.form.FormTag;
import org.jails.form.Repeater;
import org.jails.form.TextInput;

import javax.servlet.ServletRequest;

public class TextConstructor extends TagInputConstructor<TextInput> {
	public TextConstructor(TextInput tag, FormTag formTag, Repeater repeatTag, ServletRequest request) {
		super(tag, formTag, repeatTag, request);
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

