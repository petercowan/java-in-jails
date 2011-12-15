package org.jails.form.input;

public interface SelectInput extends InputElement {
	public String getMultiple();

	public void setMultiple(String multiple);

	public String getPrompt();

	public void setPrompt(String prompt);

	public String getOther();

	public void setOther(String other);

}
