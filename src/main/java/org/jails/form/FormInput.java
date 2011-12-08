package org.jails.form;

import java.util.Map;

/**
 * FormInput models the most basic interface for an HTML form input tag
 */
public interface FormInput {
	public void setName(String name);

	public void setLabel(String label);

	public void setDefaultValue(String defaultValue);

	public void setCssClass(String cssClass);

	public void setFormat(String format);

	public void setAttributes(Map<String,String> attributes);

	public String getLabel();

	public String getName();

	public String getDefaultValue();

	public String getCssClass();

	public String getFormat();

	public Map<String,String> getAttributes();
}
