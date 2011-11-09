package org.jails.form.constructor;

import org.jails.form.taglib.RadioButtonTag;
import org.jails.form.taglib.RepeaterTag;
import org.jails.form.taglib.SimpleFormTag;

import javax.servlet.ServletRequest;

public class RadioButtonConstructor
extends TagInputConstructor<RadioButtonTag> {
	public RadioButtonConstructor(RadioButtonTag tag, SimpleFormTag formTag, RepeaterTag repeatTag, ServletRequest request) {
		super(tag, formTag, repeatTag, request);
	}

	@Override
	public String getInputHtml() {
		return "<input" + getTypeAttr("radio") +
				getFieldNameAttr() +
				getInputIdAttr() +
				getClientValidationAttr() +
				getValueAttr(getFieldValue(0)) +
				getAttribute("tabIndex", tag.getTabIndex()) + " />";
	}
}
