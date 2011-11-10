package org.jails.form.constructor;

import org.jails.form.input.FormTag;
import org.jails.form.input.RadioButtonInput;
import org.jails.form.input.Repeater;

import javax.servlet.ServletRequest;

public class RadioButtonConstructor
extends TagInputConstructor<RadioButtonInput> {
	public RadioButtonConstructor(RadioButtonInput tag, FormTag formTag, Repeater repeatTag, ServletRequest request) {
		super(tag, formTag, repeatTag, request);
	}

	@Override
	public String getInputHtml() {
		return "<input" + getTypeAttr("radio") +
				getFieldNameAttr() +
				getInputIdAttr() +
				getClientValidationAttr() +
				getValueAttr(getFieldValue(0)) +
				getAttribute("tabIndex", tag.getTabIndex()) + " />";
	}
}
