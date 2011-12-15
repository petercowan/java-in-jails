package org.jails.form.input;

import java.util.Map;

/**
 * FormInput models the most basic interface for an HTML form input tag
 */
public interface InputElement {
    public String getName();

	public void setName(String name);

    public String getLabel();

	public void setLabel(String label);

    public String getDefaultValue();

	public void setDefaultValue(String defaultValue);

    public String getCssClass();

	public void setCssClass(String cssClass);

    public String getFormat();

	public void setFormat(String format);

    public Map<String,String> getAttributes();

	public void setAttributes(Map<String,String> attributes);

    public String getLabelMarker();

    public void setLabelMarker(String labelMarker);

    public String getDisplayLabel();

    public void setDisplayLabel(String displayLabel);

    public void setStyle(String style);

    public String getStyle();

    public boolean isStacked();
}
