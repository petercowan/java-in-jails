package org.jails.form.taglib;

import org.jails.form.constructor.CheckBoxConstructor;
import org.jails.form.constructor.TagInputConstructor;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspTagException;

public class CheckboxInput
		extends FormInputTag {

	private String checked;

	public void setChecked(String checked) {
		this.checked = checked;
	}

	public String getChecked() {
		return checked;
	}

	@Override
	protected TagInputConstructor getInputConstructor(ServletRequest request) throws JspTagException {
		return new CheckBoxConstructor(this, request);
	}

}
