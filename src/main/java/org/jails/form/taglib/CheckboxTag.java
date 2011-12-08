package org.jails.form.taglib;

import org.jails.form.CheckboxInput;
import org.jails.form.constructor.CheckBoxConstructor;
import org.jails.form.constructor.TagInputConstructor;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspTagException;

public class CheckboxTag
		extends FormInputTagSupport
		implements CheckboxInput {

	private String value;
	private String checked;
	private String tabIndex;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	public String getChecked() {
		return checked;
	}

	public void setTabIndex(String tabIndex) {
		this.tabIndex = tabIndex;
	}

	public String getTabIndex() {
		return tabIndex;
	}

	@Override
	protected TagInputConstructor getInputConstructor(SimpleFormTag formTag, RepeaterTag repeatTag, ServletRequest request) throws JspTagException {
		return new CheckBoxConstructor(this, formTag, repeatTag, request);
	}

}
