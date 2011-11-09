package org.jails.form.taglib;

import org.jails.form.SimpleBeanForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jails.validation.ValidationUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SimpleBeanFormTag
		extends SimpleFormTag<SimpleBeanForm> {

	private static Logger logger = LoggerFactory.getLogger(SimpleBeanFormTag.class);

	protected List<String> formElements = new ArrayList<String>();

	@Override
	protected void setFormFromRequest() {
		String simpleFormParam = "_" + name + "_form";
		logger.info("Getting SimpleForm: " + simpleFormParam);
		simpleForm = (SimpleBeanForm) pageContext.getRequest().getAttribute(simpleFormParam);
		logger.info("Loaded form " + simpleForm);
	}

	@Override
	public SimpleBeanForm getSimpleForm() {
		return simpleForm;
	}

	@Override
	public void addElement(String fieldName) {
		if (!formElements.contains(fieldName)) formElements.add(fieldName);
	}

	@Override
	public boolean fieldHasError(String fieldName) {
		return simpleForm.fieldHasError(fieldName);
	}

	@Override
	protected String getErrorMessage() {
		StringBuffer errorMessage = new StringBuffer();
		for (String fieldName : formElements) {
			if (simpleForm.fieldHasError(fieldName)) {
				String label = labelMap.get(fieldName);
				String error = simpleForm.getFieldError(fieldName, label);
				if (error.substring(0,1).matches("[A-Z]")) {
					errorMessage.append(label).append(" ").append(error).append("<br />");
				} else {
					logger.debug(error);
					ValidationUtil.replaceTokens(error, labelMap);
				}
			}
		}
		return errorMessage.toString();
	}

	public int doEndTag() throws JspException {
		try {
			// Get the current bodyContent for this tag and
			//wrap with form content
			JspWriter jspOut = pageContext.getOut();
			jspOut.print(getBeginForm());
			if (simpleForm.hasError()) jspOut.print(getError());
			jspOut.print(bodyContent.getString());
			jspOut.print(getHiddenMethod());
			jspOut.print(getSubmitValue());
			jspOut.print(getSubmitButton());
			jspOut.print(getEndForm());

			return EVAL_PAGE;
		} catch (IOException ioe) {
			throw new JspException(ioe.getMessage());
		}
	}
}

