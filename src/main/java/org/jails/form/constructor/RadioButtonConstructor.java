package org.jails.form.constructor;

import org.jails.form.FormTag;
import org.jails.form.RadioButtonInput;
import org.jails.form.Repeater;
import org.jails.util.Strings;

import javax.servlet.ServletRequest;

public class RadioButtonConstructor
extends TagInputConstructor<RadioButtonInput> {
	public RadioButtonConstructor(RadioButtonInput tag, FormTag formTag, Repeater repeatTag, ServletRequest request) {
		super(tag, formTag, repeatTag, request);
	}

	@Override
	public String getInputHtml() {
		String value = getFieldValue(0);
		String checked = ((value != null && value.equals(tag.getValue()))
				|| (value == null && Strings.getBoolean(tag.getChecked()))) ? " CHECKED" : "";

		return "<input" + getTypeAttr("radio") +
				getFieldNameAttr() +
				getInputIdAttr() +
				getClientValidationAttr() +
				getValueAttr(tag.getValue()) +
				getAttribute("tabIndex", tag.getTabIndex()) + checked + " /> " + tag.getValue() ;
	}
}
