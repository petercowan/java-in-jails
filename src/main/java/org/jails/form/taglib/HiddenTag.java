package org.jails.form.taglib;

import org.jails.form.input.HiddenConstructor;
import org.jails.form.input.HiddenInput;
import org.jails.form.input.TagInputConstructor;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspTagException;

public class HiddenTag
		extends FormInputTagSupport
		implements HiddenInput {

	@Override
	protected TagInputConstructor getInputConstructor(FormTag formTag, RepeaterTag repeatTag, ServletRequest request) throws JspTagException {
		return new HiddenConstructor(this, formTag, repeatTag, request);
	}


}

