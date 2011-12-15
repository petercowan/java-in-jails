package org.jails.form.input;

import javax.servlet.ServletRequest;

public class TextConstructor extends TagInputConstructor<TextInput> {
	public TextConstructor(TextInput tag, FormElement formTag, Repeater repeatTag, ServletRequest request) {
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

