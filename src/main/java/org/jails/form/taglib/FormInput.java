package org.jails.form.taglib;

import javax.servlet.jsp.tagext.Tag;

/**
 * FormInput models the most basic interface for an HTML form input tag
 */
public interface FormInput extends Tag {
	public void setName(String name);

	public void setLabel(String label);

	public void setDefaultValue(String defaultValue);

	public void setCssClass(String cssClass);

	public String getLabel();

	public String getName();

	public String getDefaultValue();

	public String getCssClass();
}
