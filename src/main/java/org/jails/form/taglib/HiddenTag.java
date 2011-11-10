package org.jails.form.taglib;

import org.jails.form.HiddenInput;
import org.jails.form.constructor.HiddenConstructor;
import org.jails.form.constructor.TagInputConstructor;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspTagException;

public class HiddenTag
		extends FormInputTagSupport
		implements HiddenInput {

	@Override
	protected TagInputConstructor getInputConstructor(SimpleFormTag formTag, RepeaterTag repeatTag, ServletRequest request) throws JspTagException {
		return new HiddenConstructor(this, formTag, repeatTag, request);
	}


}

