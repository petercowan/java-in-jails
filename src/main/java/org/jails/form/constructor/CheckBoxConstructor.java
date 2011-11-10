package org.jails.form.constructor;

import org.jails.form.CheckboxInput;
import org.jails.form.taglib.RepeaterTag;
import org.jails.form.taglib.SimpleFormTag;
import org.jails.util.StringUtil;

import javax.servlet.ServletRequest;

public class CheckBoxConstructor extends TagInputConstructor<CheckboxInput> {
	public CheckBoxConstructor(CheckboxInput tag, SimpleFormTag formTag, RepeaterTag repeatTag, ServletRequest request) {
		super(tag, formTag, repeatTag, request);
	}

	@Override
	public String getInputHtml() {
		StringBuffer inputHtml = new StringBuffer("<input" + getTypeAttr("text") +
				getFieldNameAttr() +
				getInputIdAttr() +
				getClientValidationAttr() +
				getValueAttr(getFieldValue(0)));

		if (StringUtil.getBoolean(tag.getChecked())) inputHtml.append(" CHECKED");
		inputHtml.append(" />");
		return inputHtml.toString();
	}
}
