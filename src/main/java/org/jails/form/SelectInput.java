package org.jails.form;

public interface SelectInput extends FormInput {
	public String getMultiple();

	public void setMultiple(String multiple);

	public String getPrompt();

	public void setPrompt(String prompt);

	public String getOther();

	public void setOther(String other);

}
