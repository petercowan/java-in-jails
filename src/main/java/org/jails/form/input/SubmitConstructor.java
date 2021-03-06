package org.jails.form.input;

import javax.servlet.ServletRequest;

public class SubmitConstructor
		extends  TagInputConstructor<SubmitButtonInput> {
	public SubmitConstructor(SubmitButtonInput tag, FormElement formTag, Repeater repeatTag, ServletRequest request) {
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
