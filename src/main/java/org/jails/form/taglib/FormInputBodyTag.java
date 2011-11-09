package org.jails.form.taglib;

import org.jails.form.constructor.BodyTagInputConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Adds the FormInput interface to the BodyTagSupport class to create CustomTags
 * that output the skeleton of a form input tag. Subclasses must override
 * getBodyInputConstructor() to do the rest.
 */
public abstract class FormInputBodyTag
		extends BodyTagSupport
		implements FormInput {
	private static Logger logger = LoggerFactory.getLogger(FormInputBodyTag.class);

	protected String label;
	protected String name;
	protected String defaultValue;
	protected String cssClass;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public int doStartTag() throws JspException {
		return EVAL_BODY_TAG;
	}

	public int doEndTag() throws JspException {
		ServletRequest request = pageContext.getRequest();
		JspWriter out = pageContext.getOut();

		try {
			out.print(getHtml(request));
		} catch (Exception ex) {
			logger.error(ex.toString());
			throw new JspTagException(ex.getMessage());
		}
		return EVAL_PAGE;
	}

	public String getHtml(ServletRequest request) throws JspTagException {
		BodyTagInputConstructor constructor = getBodyInputConstructor(request);
		return constructor.wrapInputHtml(this, bodyContent.getString());
	}

	protected abstract BodyTagInputConstructor getBodyInputConstructor(ServletRequest request) throws JspTagException;
}
