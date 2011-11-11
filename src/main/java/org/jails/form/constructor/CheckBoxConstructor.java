package org.jails.form.constructor;

import org.jails.form.input.CheckboxInput;
import org.jails.form.input.FormTag;
import org.jails.form.input.Repeater;
import org.jails.util.Strings;

import javax.servlet.ServletRequest;

public class CheckBoxConstructor extends TagInputConstructor<CheckboxInput> {
	public CheckBoxConstructor(CheckboxInput tag, FormTag formTag, Repeater repeatTag, ServletRequest request) {
		super(tag, formTag, repeatTag, request);
	}

	@Override
	public String getInputHtml() {
		StringBuffer inputHtml = new StringBuffer("<input" + getTypeAttr("text") +
				getFieldNameAttr() +
				getInputIdAttr() +
				getClientValidationAttr() +
				getValueAttr(getFieldValue(0)));

		if (Strings.getBoolean(tag.getChecked())) inputHtml.append(" CHECKED");
		inputHtml.append(" />");
		return inputHtml.toString();
	}
}
