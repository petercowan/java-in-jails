package org.jails.form.taglib;

import org.jails.form.SimpleForm;
import org.jails.form.SimpleFormParams;
import org.jails.form.controller.SimpleFormRouter;
import org.jails.form.input.FormTag;
import org.jails.property.Mapper;
import org.jails.property.PropertiesWrapper;
import org.jails.property.ReflectionUtil;
import org.jails.property.SimpleMapper;
import org.jails.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SimpleFormTag
		extends BodyTagSupport
		implements FormTag {
	private static Logger logger = LoggerFactory.getLogger(SimpleFormTag.class);

	private static SimpleFormParams simpleFormParams = new SimpleFormParams();
	protected Mapper mapper = new SimpleMapper();
	protected PropertiesWrapper propertiesWrapper;


	public static String STACKED = "stacked";
	public static String SIDE_BY_SIDE = "side";

	protected Map<Integer, List<String>> formElementMap = new LinkedHashMap<Integer, List<String>>();

	protected Map<String, String> labelMap = new HashMap<String, String>();
	protected SimpleForm simpleForm;
	protected RepeaterTag repeatTag;
	protected String name;
	protected String action;
	protected String method;
	protected String style = STACKED;
	protected String errorMessage;

	protected void initFormFromRequest() {
		String simpleFormParam = "_" + name + "_form";
		logger.info("Getting SimpleForm: " + simpleFormParam);
		simpleForm = (SimpleForm) pageContext.getRequest().getAttribute(simpleFormParam);
		logger.info("Loaded form " + simpleForm);
		if (simpleForm != null && simpleForm.isBound()) {
			propertiesWrapper = new PropertiesWrapper(mapper.toMap(simpleForm.getObject()), simpleForm.getClassType());
		}
	}

	public void setName(String name) {
		this.name = name;
		initFormFromRequest();
	}

	public String getName() {
		return name;
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

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public boolean isStacked() {
		return style.equals(STACKED);
	}

	public SimpleForm getSimpleForm() {
		return simpleForm;
	}

	public String[] getInputValue(ServletRequest request, String elementName, Integer repeaterIndex) {
		if (request.getParameter(elementName) != null) {
			return new String[]{request.getParameter(elementName)};
		} else if (propertiesWrapper != null) {
			return  propertiesWrapper.get(elementName, repeaterIndex);
		}
		return null;
	}

	public void addElement(String fieldName, int index) {
		List<String> formElements = formElementMap.get(index);
		if (formElements == null) {
			formElements = new ArrayList<String>();
			formElementMap.put(index, formElements);
		}
		if (!formElements.contains(fieldName)) formElements.add(fieldName);
	}


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

		if (action != null) beginForm.append(" action=\"" + action + "\">");
		else if (formAction != null) beginForm.append(" action=\"" + formAction + "\">");
		else beginForm.append(">");
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
		if (errorMessage != null) {
			return errorMessage;
		} else {
			StringBuffer message = new StringBuffer();
			for (Integer index : formElementMap.keySet()) {
				List<String> formElements = formElementMap.get(index);
				if (formElements != null) {
					for (String fieldName : formElements) {
						if (simpleForm.fieldHasError(fieldName, index)) {
							String fieldError = simpleForm.getFieldError(fieldName, labelMap.get(fieldName), index);
							if (!Strings.isEmpty(fieldError)) message.append(fieldError);
						}
					}
				}
			}
			return message.toString();
		}
	}

	public int doStartTag() throws JspException {
		return EVAL_BODY_TAG;
	}

	public int doEndTag() throws JspException {
		try {
			// Get the current bodyContent for this tag and
			//wrap with form content
			JspWriter jspOut = pageContext.getOut();
			jspOut.print(getBeginForm());
			if (simpleForm != null && simpleForm.hasError()) jspOut.print(getError());
			if (bodyContent != null && !Strings.isEmpty(bodyContent.getString())) {
				jspOut.print(bodyContent.getString());
			} else {
				generateForm();
			}
			jspOut.print(getHiddenMethod());
			jspOut.print(getSubmitValue());
			jspOut.print(getEndForm());

			return EVAL_PAGE;
		} catch (IOException ioe) {
			throw new JspException(ioe.getMessage());
		}
	}

	protected void generateForm() throws JspException, IOException {
		if (simpleForm != null) {
			Map<String, Method> getters = ReflectionUtil.getGetterMethods(simpleForm.getClassType());

			for (String fieldName : getters.keySet()) {
				Method method = getters.get(fieldName);
				TextTag textTag = new TextTag();
				textTag.setParent(this);
				textTag.setPageContext(pageContext);
				textTag.setName(fieldName);
				textTag.setLabel(Strings.initCaps(
								Strings.join(" ",
										Strings.splitCamelCase(fieldName))));
				textTag.setSize("25");
				textTag.doStartTag();
				pageContext.getOut().print("\n");
			}
			SubmitButtonTag submitTag = new SubmitButtonTag();
			submitTag.setParent(this);
			submitTag.setPageContext(pageContext);
			submitTag.setLabel("Submit");
			submitTag.doStartTag();
		}
	}
}

