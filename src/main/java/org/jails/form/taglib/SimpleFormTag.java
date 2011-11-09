package org.jails.form.taglib;

import org.jails.form.SimpleForm;
import org.jails.form.SimpleFormRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.util.HashMap;
import java.util.Map;

public abstract class SimpleFormTag<T extends SimpleForm> extends BodyTagSupport {
	private static Logger logger = LoggerFactory.getLogger(SimpleFormTag.class);

	public static String STACKED = "stacked";
	public static String SIDE_BY_SIDE = "side";

	protected Map<String, String> labelMap = new HashMap<String, String>();
	protected T simpleForm;
	protected String name;
	protected String submitButton;
	protected String action;
	protected String method;
	protected String style = STACKED;

	protected abstract void setFormFromRequest();

	public void setName(String name) {
		this.name = name;
		setFormFromRequest();
	}

	public void setSubmitButton(String submitButton) {
		logger.info("setSubmitButton:" + submitButton) ;
		this.submitButton = submitButton;
	}

	protected String getSubmitButton() {
		logger.info("submitButton:" + submitButton) ;
		return (submitButton == null) ? "" : submitButton;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void setStyle(String style) {
		this.style = (SIDE_BY_SIDE.equals(style)) ? SIDE_BY_SIDE : STACKED;
	}

	public boolean isStacked() {
		return style.equals(STACKED);
	}

	public SimpleForm getSimpleForm() {
		return simpleForm;
	}

	public abstract void addElement(String fieldName);

	public abstract boolean fieldHasError(String fieldName);

	public void addLabel(String inputName, String label) {
		labelMap.put(inputName, label);
	}

	protected String getBeginForm() {
		StringBuffer beginForm = new StringBuffer();

		beginForm.append("<div class=\"form\">").append("\n");
		beginForm.append("<form name=\"" + name + "\" id=\"" + name + "\"");
		logger.info("name: " + name);

		String formMethod = (this.method != null) ? this.method : "POST";
		beginForm.append(" method=\"" + formMethod + "\"");

		logger.info("method: " + formMethod);

		String formAction = (simpleForm != null) ? simpleForm.getAction() : null;

		if (action != null) beginForm.append(" action=\"" + action + "\"");
		else if (formAction != null) beginForm.append(" action=\"" + formAction + "\">");
		logger.info("action: " + formAction);
		beginForm.append("<fieldset>").append("\n");

		return beginForm.toString();
	}

	protected String getEndForm() {
		return "</fieldset></form>\n</div>";
	}

	protected String getHiddenMethod() {
		String hiddenMethod;

		if (simpleForm != null) {
			if (simpleForm.isBoundToObject()) {
				hiddenMethod = "POST";
			} else {
				hiddenMethod = "PUT";
			}
			return "<input type=\"hidden\" name=\"" + simpleForm.getMetaParameterName("method")
					+ "\" value=\"" + hiddenMethod + "\" />";
		} else {
			return "";
		}
	}

	protected String getSubmitValue() {
		String submitValue;

		if (simpleForm != null) {
			if (simpleForm.isBoundToObject()) {
				submitValue = simpleForm.getMetaParameterName(SimpleFormRequest.SUBMIT_EDIT);
			} else {
				submitValue = simpleForm.getMetaParameterName(SimpleFormRequest.SUBMIT_NEW);
			}
			return "<input type=\"hidden\" name=\"" + simpleForm.getMetaParameterName(SimpleFormRequest.ACTION_SUBMIT)
					+ "\" value=\"" + submitValue + "\" />";
		} else {
			return "";
		}
	}

	protected String getError() {
		StringBuffer errorMessage;
		if (simpleForm != null && simpleForm.hasError()) {
			errorMessage = new StringBuffer();
			errorMessage.append("<span class=\"error\">");
			errorMessage.append(getErrorMessage());
			errorMessage.append("</span>");
		} else {
			errorMessage = new StringBuffer("");
		}
		return errorMessage.toString();
	}

	protected abstract String getErrorMessage();

	public int doStartTag() throws JspException {
		return EVAL_BODY_TAG;
	}
}

