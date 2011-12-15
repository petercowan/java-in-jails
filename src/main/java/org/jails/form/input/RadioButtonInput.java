package org.jails.form.input;

import org.jails.form.taglib.RadioGroupTag;

public interface RadioButtonInput extends InputElement {
	public String getValue();

	public void setValue(String value);

	public void setChecked(String checked);

	public String getChecked();

	public void setTabIndex(String tabIndex);

	public String getTabIndex();

    public RadioGroupTag getRadioGroupTag();
}
