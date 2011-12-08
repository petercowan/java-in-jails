package org.jails.form;

import javax.servlet.ServletRequest;
import java.util.List;
import java.util.Map;

public interface FormTag {

	public void setName(String name);

	public String getName();

	public void setAction(String action);

	public String getAction();

	public void setMethod(String method);

	public String getMethod();

	public void setStyle(String style);

	public String getStyle();

	public boolean isStacked();

	public void setLegend(String label);

	public String getLegend();

	public void setErrorMessage(String errorMessage);

	public String getErrorMessage();

	public SimpleForm getSimpleForm();

	public String[] getInputValue(ServletRequest request, String elementName, Integer repeaterIndex);

	public void addElement(String fieldName, int index);

	public Map<Integer, List<String>> getElements();

	public void addLabel(String inputName, String label);

	public Map<String, String> getLabels();
}
