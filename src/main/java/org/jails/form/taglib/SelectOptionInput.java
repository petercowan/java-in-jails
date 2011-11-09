package org.jails.form.taglib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class SelectOptionInput
		extends TagSupport {
	private static Logger logger = LoggerFactory.getLogger(SelectOptionInput.class);

	private String value;
	private String label;

	public void setValue(String value) {
		this.value = value;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int doStartTag()
			throws JspException {
		SelectInput selectInput = (SelectInput) TagSupport.findAncestorWithClass(this, SelectInput.class);
		if (selectInput == null) {
			throw new JspTagException("A SelectOptionInput tag must be nested within a SelectInput.");
		}

		ServletRequest request = pageContext.getRequest();
		JspWriter out = pageContext.getOut();

		try {

			String[] fieldValues = selectInput.getBodyInputConstructor(request).getFieldValues();
			String selectedValue = (fieldValues.length > 0 && fieldValues[0] != null) ? fieldValues[0] : "";

			StringBuffer optionHtml = new StringBuffer();
			optionHtml.append("<option value=\"" + value + "\"");
			if (value.equals(selectedValue)) optionHtml.append(" SELECTED");
			optionHtml.append("> " + label + "</option>");

			out.print(optionHtml);

		} catch (Exception ex) {
			logger.error(ex.toString());
			throw new JspTagException(ex.getMessage());
		}
		return EVAL_PAGE;
	}

}
