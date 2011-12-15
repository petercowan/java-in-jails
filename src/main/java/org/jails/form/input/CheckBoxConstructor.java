package org.jails.form.input;

import org.jails.util.Strings;

import javax.servlet.ServletRequest;

public class CheckBoxConstructor extends TagInputConstructor<CheckboxInput> {
	public CheckBoxConstructor(CheckboxInput tag, FormElement formTag, Repeater repeatTag, ServletRequest request) {
		super(tag, formTag, repeatTag, request);
	}

	@Override
	public String getInputHtml() {
		StringBuffer inputHtml = new StringBuffer("<input" + getTypeAttr("checkbox") +
				getFieldNameAttr() +
				getInputIdAttr() +
				getClientValidationAttr() +
				getValueAttr(tag.getValue()));

		String value = getFieldValue(0);
		if ((value != null && value.equals(tag.getValue()))
				|| (value == null && Strings.getBoolean(tag.getChecked()))) inputHtml.append(" CHECKED");
		inputHtml.append(" />");
		return inputHtml.toString();
	}
}
