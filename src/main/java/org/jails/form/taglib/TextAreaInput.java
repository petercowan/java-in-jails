package org.jails.form.taglib;

import org.jails.form.constructor.TagInputConstructor;
import org.jails.form.constructor.TextAreaConstructor;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspTagException;

public class TextAreaInput
		extends FormInputTag {

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
	protected TagInputConstructor getInputConstructor(ServletRequest request) throws JspTagException {
		return new TextAreaConstructor(this, request);
	}
}
