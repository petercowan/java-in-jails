package org.jails.form.input;

import org.jails.form.SimpleForm;

public interface FormTag {
	public void setName(String name);

	public String getName();

	public void setAction(String action);

	public void setMethod(String method);

	public void setStyle(String style);

	public void setErrorMessage(String errorMessage);

	public boolean isStacked();

	public SimpleForm getSimpleForm();

	public void addElement(String fieldName, int index);

	public void addLabel(String inputName, String label);
}
