package org.jails.form.constructor;

import org.jails.form.taglib.RepeaterTag;
import org.jails.form.taglib.SelectTag;
import org.jails.form.taglib.SimpleFormTag;

import javax.servlet.ServletRequest;
import java.util.Map;

public class SelectConstructor extends BodyTagInputConstructor<SelectTag> {
	public SelectConstructor(SelectTag tag, SimpleFormTag formTag, RepeaterTag repeatTag, ServletRequest request) {
		super(tag, formTag, repeatTag, request);
	}

	protected Map<String, String> options;

	public void setOptions(Map<String, String> options) {
		this.options = options;
	}

	@Override
	public String getOpeningHtml() {
		StringBuffer openingHtml = new StringBuffer();

		openingHtml.append("<select" +
				getFieldNameAttr() +
				getInputIdAttr() +
				getClientValidationAttr() +
				getAttribute("multiple", tag.getMultiple()) + ">");
		if (options != null && options.size() > 0) {
			StringBuffer optionHtml = new StringBuffer();
			for (String optionValue : options.keySet()) {
				String optionLabel = options.get(optionValue);
				optionHtml.append("<option" + getAttribute("value", optionValue));
				for (String fieldValue : fieldValues) {
					if (optionValue.equals(fieldValue)) {
						optionHtml.append(" SELECTED");
						break;
					}
				}
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

