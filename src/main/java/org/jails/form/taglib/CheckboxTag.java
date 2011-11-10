package org.jails.form.taglib;

import org.jails.form.input.CheckboxInput;
import org.jails.form.constructor.CheckBoxConstructor;
import org.jails.form.constructor.TagInputConstructor;
import org.jails.form.input.CheckboxInput;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspTagException;

public class CheckboxTag
		extends FormInputTagSupport
		implements CheckboxInput {

	private String checked;

	public void setChecked(String checked) {
		this.checked = checked;
	}

	public String getChecked() {
		return checked;
	}

	@Override
	protected TagInputConstructor getInputConstructor(SimpleFormTag formTag, RepeaterTag repeatTag, ServletRequest request) throws JspTagException {
		return new CheckBoxConstructor(this, formTag, repeatTag, request);
	}

}
