package org.jails.form.constructor;

import org.jails.form.taglib.CheckboxInput;
import org.jails.util.StringUtil;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspTagException;

public class CheckBoxConstructor extends TagInputConstructor<CheckboxInput> {
	public CheckBoxConstructor(CheckboxInput tag, ServletRequest request) throws JspTagException {
		super(tag, request);
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
