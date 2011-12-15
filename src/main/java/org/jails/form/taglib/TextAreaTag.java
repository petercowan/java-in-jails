package org.jails.form.taglib;

import org.jails.form.input.TagInputConstructor;
import org.jails.form.input.TextAreaConstructor;
import org.jails.form.input.TextareaInput;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspTagException;

public class TextAreaTag
		extends FormInputTagSupport
		implements TextareaInput {

	protected String rows;
	protected String cols;

	public void setRows(String rows) {
		this.rows = rows;
	}

	public void setCols(String cols) {
		this.cols = cols;
	}

	public String getRows() {
		return rows;
	}

	public String getCols() {
		return cols;
	}

	@Override
	protected TagInputConstructor getInputConstructor(FormTag formTag, RepeaterTag repeatTag, ServletRequest request) throws JspTagException {
		return new TextAreaConstructor(this, formTag, repeatTag, request);
	}
}
