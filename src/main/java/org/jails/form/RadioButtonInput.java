package org.jails.form;

public interface RadioButtonInput extends FormInput {
	public String getValue();

	public void setValue(String value);

	public void setChecked(String checked);

	public String getChecked();

	public void setTabIndex(String tabIndex);

	public String getTabIndex();

}
