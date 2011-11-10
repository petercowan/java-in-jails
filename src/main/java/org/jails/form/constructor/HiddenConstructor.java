package org.jails.form.constructor;

import org.jails.form.input.FormTag;
import org.jails.form.input.FormTag;
import org.jails.form.input.HiddenInput;
import org.jails.form.input.HiddenInput;
import org.jails.form.input.Repeater;
import org.jails.form.input.Repeater;

import javax.servlet.ServletRequest;

public class HiddenConstructor extends TagInputConstructor<HiddenInput>{

	public HiddenConstructor(HiddenInput tag, FormTag formTag, Repeater repeatTag, ServletRequest request) {
		super(tag, formTag, repeatTag, request);
	}

	@Override
	public String getInputHtml() {
		return "<input" + getTypeAttr("hidden") +
				getFieldNameAttr() +
				getInputIdAttr() +
				getClientValidationAttr() +
				getValueAttr(getFieldValue(0));
	}
}

