package org.jails.form.constructor;

import org.jails.form.taglib.SelectInput;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspTagException;
import java.util.Map;

public class SelectConstructor extends BodyTagInputConstructor<SelectInput> {
	public SelectConstructor(SelectInput tag, ServletRequest request) throws JspTagException {
		super(tag, request);
	}

	protected Map<String, String> options;

	public void setOptions(Map<String, String> options) {
		this.options = options;
	}

	@Override
	public String getOpeningHtml() {
		StringBuffer openingHtml = new StringBuffer();

		String fieldValue = getFieldValue(0);
		openingHtml.append("<select" +
				getFieldNameAttr() +
				getInputIdAttr() +
				getClientValidationAttr() + ">");
		if (options != null && options.size() > 0) {
			StringBuffer optionHtml = new StringBuffer();
			for (String optionValue : options.keySet()) {
				String optionLabel = options.get(optionValue);
				optionHtml.append("<option" + getAttribute("value", optionValue));
				if (optionValue.equals(fieldValue)) optionHtml.append(" SELECTED");
				optionHtml.append("> " + optionLabel + "</option>");
			}
			openingHtml.append(optionHtml);
		}
		return openingHtml.toString();
	}

	@Override
	public String getClosingHtml() {
		StringBuffer closingHtml = new StringBuffer();

		closingHtml.append("</select>");
		return closingHtml.toString();
	}
}

