package org.jails.form.taglib;

import org.jails.form.SimpleRepeatableForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SimpleRepeatableFormTag
		extends SimpleFormTag<SimpleRepeatableForm> {

	private static Logger logger = LoggerFactory.getLogger(SimpleRepeatableFormTag.class);

	protected Map<Integer, List<String>> formElementMap = new LinkedHashMap<Integer, List<String>>();

	private int index = 0;
	private StringBuffer contentBuffer = new StringBuffer();

	public int getIndex() {
		return index;
	}

	@Override
	protected void setFormFromRequest() {
		String simpleFormParam = "_" + name + "_form";
		logger.info("Getting SimpleForm: " + simpleFormParam);
		simpleForm = (SimpleRepeatableForm) pageContext.getRequest().getAttribute(simpleFormParam);
		logger.info("Loaded form " + simpleForm);
	}

	@Override
	public SimpleRepeatableForm getSimpleForm() {
		return simpleForm;
	}

	@Override
	public void addElement(String fieldName) {
		List<String> formElements = formElementMap.get(index);
		if (formElements == null) {
			formElements = new ArrayList<String>();
			formElementMap.put(index, formElements);
		}
		if (!formElements.contains(fieldName)) formElements.add(fieldName);
	}

	@Override
	public boolean fieldHasError(String fieldName) {
		return simpleForm.fieldHasError(fieldName, index);
	}


	@Override
	protected String getErrorMessage() {
		StringBuffer errorMessage = new StringBuffer();
		for (Integer key : formElementMap.keySet()){
			List<String> formElements = formElementMap.get(key);
			if (formElements != null) {
				for (String fieldName : formElements) {
					if (simpleForm.fieldHasError(fieldName, index)) {
						errorMessage.append(simpleForm.getFieldError(fieldName, labelMap.get(fieldName), index));
					}
				}
			}
		}
		return errorMessage.toString();
	}

	@Override
	public int doAfterBody() throws JspException {
		logger.info("appending content for index: " + index + " of " + simpleForm.getTimesToRepeat());
		//contentBuffer.append(bodyContent.getString());
		if (++index < simpleForm.getTimesToRepeat()) {
			return EVAL_BODY_AGAIN;
		} else {
			logger.info("ending evaluation");
			return SKIP_BODY;
		}
	}

	@Override
	public int doEndTag() throws JspException {
		try {
			// Get the bodyContent for this tag and
			//wrap with form content
			JspWriter jspOut = pageContext.getOut();
			jspOut.print(getBeginForm());
			if (simpleForm.hasError()) jspOut.print(getError());
			jspOut.print(bodyContent.getString());
			jspOut.print(getHiddenMethod());
			jspOut.print(getSubmitValue());
			jspOut.print(getSubmitButton());
			jspOut.print(getEndForm());

			index = 0;
			return EVAL_PAGE;
		} catch (IOException ioe) {
			throw new JspException(ioe.getMessage());
		}
	}
}

