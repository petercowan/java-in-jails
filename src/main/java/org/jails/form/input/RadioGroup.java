package org.jails.form.input;

import java.util.Map;

public interface RadioGroup {
    public String getName();

    public void setName(String name);

    public String getLabel();

    public void setLabel(String name);

    public String getDisplayLabel();

    public void setDisplayLabel(String displayLabel);

    public String getLabelMarker();

    public void setLabelMarker(String labelMarker);

    public Map<RadioButtonInput, Integer> getButtons();

    public void setButtons(Map<RadioButtonInput, Integer> buttons);

    public void addButton(RadioButtonInput button);

    public String getStyle();

    public void setStyle(String style);

    public boolean isStacked();
}
