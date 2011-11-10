package org.jails.form.constructor;

import org.jails.form.input.FormTag;
import org.jails.form.input.FormTag;
import org.jails.form.input.PasswordInput;
import org.jails.form.input.PasswordInput;
import org.jails.form.input.Repeater;
import org.jails.form.input.Repeater;

import javax.servlet.ServletRequest;

public class PasswordConstructor extends TagInputConstructor<PasswordInput> {
	public PasswordConstructor(PasswordInput tag, FormTag formTag, Repeater repeatTag, ServletRequest request) {
		super(tag, formTag, repeatTag, request);
	}

	@Override
	public String getInputHtml() {
		return "<input" + getTypeAttr("password") +
				getFieldNameAttr() +
				getInputIdAttr() +
				getClientValidationAttr() +
				getValueAttr(getFieldValue(0)) +
				getAttribute("size", tag.getSize()) + " />";
	}
}

