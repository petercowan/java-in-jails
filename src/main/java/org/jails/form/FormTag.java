package org.jails.form;

public interface FormTag {
	public void setName(String name);

	public void setAction(String action);

	public void setMethod(String method);

	public void setStyle(String style);

	public boolean isStacked();

	public SimpleForm getSimpleForm();

	public void addElement(String fieldName, int index);

	public void addLabel(String inputName, String label);
}
