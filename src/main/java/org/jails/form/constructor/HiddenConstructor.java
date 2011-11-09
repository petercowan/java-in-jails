package org.jails.form.constructor;

import org.jails.form.taglib.HiddenTag;
import org.jails.form.taglib.RepeaterTag;
import org.jails.form.taglib.SimpleFormTag;

import javax.servlet.ServletRequest;

public class HiddenConstructor extends TagInputConstructor<HiddenTag>{

	public HiddenConstructor(HiddenTag tag, SimpleFormTag formTag, RepeaterTag repeatTag, ServletRequest request) {
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

