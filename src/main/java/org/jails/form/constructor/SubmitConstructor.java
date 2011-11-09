package org.jails.form.constructor;

import org.jails.form.taglib.RepeaterTag;
import org.jails.form.taglib.SimpleFormTag;
import org.jails.form.taglib.SubmitButton;

import javax.servlet.ServletRequest;

public class SubmitConstructor extends  TagInputConstructor<SubmitButton> {
	public SubmitConstructor(SubmitButton tag, SimpleFormTag formTag, RepeaterTag repeatTag, ServletRequest request) {
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
