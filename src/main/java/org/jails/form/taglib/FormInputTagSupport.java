package org.jails.form.taglib;

import org.jails.form.constructor.TagInputConstructor;
import org.jails.form.input.FormInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.Map;

/**
 * Adds the FormInput interface to the TagSupport class to create CustomTags
 * that output the skeleton of a form input tag. Subclasses must override
 * getInputConstructor() to do the rest.
 */
public abstract class FormInputTagSupport
		extends TagSupport
		implements FormInput {
	private static Logger logger = LoggerFactory.getLogger(FormInputTagSupport.class);

	protected String label;
	protected String name;
	protected String defaultValue;
	protected String cssClass;
	protected Map<String,String> attributes;
	protected SimpleFormTag formTag;
	protected RepeaterTag repeatTag;

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

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	public int doStartTag()
			throws JspException {
		formTag = (SimpleFormTag) TagSupport.findAncestorWithClass(this, SimpleFormTag.class);
		if (formTag == null) {
			if (formTag == null) {
				throw new JspTagException("A FormInput tag must be nested within a FormTag.");
			}
		}
		repeatTag = (RepeaterTag) TagSupport.findAncestorWithClass(this, RepeaterTag.class);

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
		TagInputConstructor constructor = getInputConstructor(formTag, repeatTag, request);

		return constructor.wrapInputHtml(this);

	}

	protected abstract TagInputConstructor getInputConstructor(SimpleFormTag formTag, RepeaterTag repeatTag, ServletRequest request) throws JspTagException;
}

