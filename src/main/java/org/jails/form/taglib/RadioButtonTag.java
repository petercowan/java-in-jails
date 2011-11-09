package org.jails.form.taglib;

import org.jails.form.constructor.RadioButtonConstructor;
import org.jails.form.constructor.TagInputConstructor;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspTagException;

public class RadioButtonTag
		extends FormInputTagSupport {

	private String tabIndex;

	public void setTabIndex(String tabIndex) {
		this.tabIndex = tabIndex;
	}

	public String getTabIndex() {
		return tabIndex;
	}

	@Override
	protected TagInputConstructor getInputConstructor(SimpleFormTag formTag, RepeaterTag repeatTag, ServletRequest request) throws JspTagException {
		return new RadioButtonConstructor(this, formTag, repeatTag, request);
	}
}

