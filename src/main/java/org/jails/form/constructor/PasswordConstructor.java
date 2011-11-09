package org.jails.form.constructor;

import org.jails.form.taglib.PasswordInput;
import org.jails.form.taglib.RepeaterTag;
import org.jails.form.taglib.SimpleFormTag;

import javax.servlet.ServletRequest;

public class PasswordConstructor extends TagInputConstructor<PasswordInput> {
	public PasswordConstructor(PasswordInput tag, SimpleFormTag formTag, RepeaterTag repeatTag, ServletRequest request) {
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

