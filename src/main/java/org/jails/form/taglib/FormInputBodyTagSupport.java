package org.jails.form.taglib;

import org.jails.form.FormInput;
import org.jails.form.FormTag;
import org.jails.form.constructor.BodyTagInputConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.Map;

/**
 * Adds the FormInput interface to the BodyTagSupport class to create CustomTags
 * that output the skeleton of a form input tag. Subclasses must override
 * getBodyInputConstructor() to do the rest.
 */
public abstract class FormInputBodyTagSupport
		extends BodyTagSupport
		implements FormInput {
	private static Logger logger = LoggerFactory.getLogger(FormInputBodyTagSupport.class);

	protected String label;
    protected String displayLabel;
    protected String labelMarker;
	protected String name;
	protected String defaultValue;
	protected String cssClass;
	protected String format;
    protected String style;
	protected Map<String,String> attributes;
	protected SimpleFormTag formTag;
	protected RepeaterTag repeatTag;

    public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

    public String getDisplayLabel() {
        return displayLabel;
    }

    public void setDisplayLabel(String displayLabel) {
        this.displayLabel = displayLabel;
    }

    public String getLabelMarker() {
        return labelMarker;
    }

    public void setLabelMarker(String labelMarker) {
        this.labelMarker = labelMarker;
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

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public boolean isStacked() {
        return FormTag.STACKED.equals(style);
    }

    public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	public int doStartTag() throws JspException {
		formTag = (SimpleFormTag) TagSupport.findAncestorWithClass(this, SimpleFormTag.class);
		if (formTag == null) {
			if (formTag == null) {
				throw new JspTagException("A FormInput tag must be nested within a FormTag.");
			}
		}
		repeatTag = (RepeaterTag) TagSupport.findAncestorWithClass(this, RepeaterTag.class);

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
		BodyTagInputConstructor constructor = getBodyInputConstructor(formTag, repeatTag, request);
		if (constructor != null) logger.info(constructor.getClass().toString());
		String bodyContentString = (bodyContent == null) ? "" : bodyContent.getString();
		return constructor.wrapInputHtml(bodyContentString);
	}

	protected abstract BodyTagInputConstructor getBodyInputConstructor(SimpleFormTag formTag, RepeaterTag repeatTag, ServletRequest request)
			throws JspTagException;
}
