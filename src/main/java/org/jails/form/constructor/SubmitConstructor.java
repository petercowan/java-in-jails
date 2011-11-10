package org.jails.form.constructor;

import org.jails.form.input.FormTag;
import org.jails.form.input.FormTag;
import org.jails.form.input.Repeater;
import org.jails.form.input.Repeater;
import org.jails.form.input.SubmitButtonInput;
import org.jails.form.input.SubmitButtonInput;

import javax.servlet.ServletRequest;

public class SubmitConstructor
		extends  TagInputConstructor<SubmitButtonInput> {
	public SubmitConstructor(SubmitButtonInput tag, FormTag formTag, Repeater repeatTag, ServletRequest request) {
		super(tag, formTag, repeatTag, request);
	}

	@Override
	public String getInputHtml() {
		return "<input" + getTypeAttr("submit") +
				getAttribute("name", "submit") +
				getInputIdAttr() +
				getClientValidationAttr() +
				getValueAttr(tag.getLabel()) + " />";
	}

}
