package org.jails.form.taglib;

import org.jails.form.constructor.BodyTagInputConstructor;
import org.jails.form.constructor.SelectConstructor;
import org.jails.form.input.SelectInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspTagException;

public class SelectTag
		extends FormInputBodyTagSupport implements SelectInput {

	private static Logger logger = LoggerFactory.getLogger(SelectTag.class);

	private String multiple;
	private String prompt;

	public String getMultiple() {
		return multiple;
	}

	public void setMultiple(String multiple) {
		this.multiple = multiple;
	}

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	@Override
	protected BodyTagInputConstructor getBodyInputConstructor(SimpleFormTag formTag, RepeaterTag repeatTag, ServletRequest request) throws JspTagException {
		return new SelectConstructor(this, formTag, repeatTag, request);
	}
}

