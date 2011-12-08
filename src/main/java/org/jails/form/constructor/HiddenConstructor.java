package org.jails.form.constructor;

import org.jails.form.FormInput;
import org.jails.form.FormTag;
import org.jails.form.HiddenInput;
import org.jails.form.Repeater;

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
				getValueAttr(getFieldValue(0)) + " />";
	}

	public String wrapInputHtml(FormInput tag) {
		return getInputHtml();
	}

}

