package org.jails.form.taglib;

import org.jails.form.input.SelectOptionInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class SelectOptionTag
		extends TagSupport
		implements SelectOptionInput {
	private static Logger logger = LoggerFactory.getLogger(SelectOptionTag.class);

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
		SelectTag selectInput = (SelectTag) TagSupport.findAncestorWithClass(this, SelectTag.class);
		if (selectInput == null) {
			throw new JspTagException("A SelectOptionInput tag must be nested within a SelectInput.");
		}
		FormTag formTag = (FormTag) TagSupport.findAncestorWithClass(this, FormTag.class);
		if (formTag == null) {
			if (formTag == null) {
				throw new JspTagException("A FormInput tag must be nested within a FormTag.");
			}
		}
		RepeaterTag repeatTag = (RepeaterTag) TagSupport.findAncestorWithClass(this, RepeaterTag.class);


		ServletRequest request = pageContext.getRequest();
		JspWriter out = pageContext.getOut();

		try {

            String optionValue = ((value == null) ? label : value);
            //todo - factor out display code
			String[] fieldValues = selectInput.getBodyInputConstructor(formTag, repeatTag, request).getFieldValues();

			StringBuffer optionHtml = new StringBuffer();
			optionHtml.append("<option value=\"" + optionValue + "\"");
			for (String fieldValue : fieldValues) {
				if (optionValue.equals(fieldValue)) optionHtml.append(" SELECTED");
				break;
			}
			optionHtml.append("> " + label + "</option>");

			out.print(optionHtml);

		} catch (Exception ex) {
			logger.error(ex.toString());
			throw new JspTagException(ex.getMessage());
		}
		return EVAL_PAGE;
	}

}
