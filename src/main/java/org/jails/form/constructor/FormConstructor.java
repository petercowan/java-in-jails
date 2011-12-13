package org.jails.form.constructor;

import org.jails.demo.controller.SimpleFormRouter;
import org.jails.form.FormTag;
import org.jails.form.SimpleForm;
import org.jails.form.SimpleFormParams;
import org.jails.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class FormConstructor {
	private static Logger logger = LoggerFactory.getLogger(FormConstructor.class);
	private static SimpleFormParams simpleFormParams = new SimpleFormParams();

	private FormTag formTag;
	private SimpleForm simpleForm;

	public FormConstructor(FormTag formTag) {
		this.formTag = formTag;
		this.simpleForm = formTag.getSimpleForm();
	}

	protected String getBeginForm() {
		StringBuffer beginForm = new StringBuffer();

		beginForm.append("<div class=\"form\">").append("\n");
		beginForm.append("<form name=\"" + formTag.getName() + "\" id=\"" + formTag.getName() + "\"");
		logger.info("name: " + formTag.getName());

		String formMethod = (formTag.getMethod() != null) ? formTag.getMethod() : "POST";
		beginForm.append(" method=\"" + formMethod + "\"");

		logger.info("method: " + formMethod);

		String formAction = (simpleForm != null) ? simpleForm.getAction() : null;

		if (formTag.getAction() != null) beginForm.append(" action=\"" + formTag.getAction() + "\">");
		else if (formAction != null) beginForm.append(" action=\"" + formAction + "\">");
		else beginForm.append(">");

		return beginForm.toString();
	}

    protected String getEndForm() {
		return "</form>\n</div>";
	}

	protected String getHiddenMethod() {
		String hiddenMethod;

		if (simpleForm != null) {
			if (simpleForm.isBound()) {
				hiddenMethod = "POST";
			} else {
				hiddenMethod = "PUT";
			}
			return "<input type=\"hidden\" name=\"" + simpleFormParams.getMetaParameterName("method")
					+ "\" value=\"" + hiddenMethod + "\" />";
		} else {
			return "";
		}
	}

	protected String getSubmitValue() {
		String submitValue;

		if (simpleForm != null) {
			if (simpleForm.isBound()) {
				submitValue = simpleFormParams.getMetaParameterName(SimpleFormRouter.SUBMIT_EDIT);
			} else {
				submitValue = simpleFormParams.getMetaParameterName(SimpleFormRouter.SUBMIT_NEW);
			}
			return "<input type=\"hidden\" name=\"" + simpleFormParams.getMetaParameterName(SimpleFormRouter.ACTION_SUBMIT)
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

	protected String getErrorMessage() {
		if (formTag.getErrorMessage() != null) {
			return formTag.getErrorMessage();
		} else {
			StringBuffer message = new StringBuffer();
			for (Integer index : formTag.getElements().keySet()) {
				List<String> formElements = formTag.getElements().get(index);
				if (formElements != null) {
					for (String fieldName : formElements) {
						if (simpleForm.fieldHasError(fieldName, index)) {
							String fieldError = simpleForm.getFieldError(fieldName, formTag.getLabels().get(fieldName), index);
							if (!Strings.isEmpty(fieldError)) message.append(fieldError);
						}
					}
				}
			}
			return message.toString();
		}
	}

	public String wrapFormContent(String formContent) {
		// Get the current bodyContent for this tag and
		//wrap with form content              '

		StringBuffer formHtml = new StringBuffer();
		if (simpleForm != null && simpleForm.hasError()) formHtml.append(getError());
        formHtml.append(getBeginForm());
		formHtml.append(formContent);
		formHtml.append(getHiddenMethod());
		formHtml.append(getSubmitValue());
		formHtml.append(getEndForm());

		return formHtml.toString();
	}
}
