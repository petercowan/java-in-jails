package org.jails.form;

import java.util.Map;

public interface RadioGroup {
    public String getName();

    public void setName(String name);

    public String getLabel();

    public void setLabel(String name);

    public String getLabelMarker();

    public void setLabelMarker(String labelMarker);

    public Map<String, RadioButtonInput> getButtons();

    public void setButtons(Map<String, RadioButtonInput> buttons);

    public void addButtons(RadioButtonInput button);

    public String getStyle();

    public void setStyle(String style);

    public boolean isStacked();
}
