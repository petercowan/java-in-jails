package org.jails.form.taglib;

import org.jails.form.FormTag;
import org.jails.form.SimpleForm;
import org.jails.form.constructor.FormConstructor;
import org.jails.property.Mapper;
import org.jails.property.PropertiesWrapper;
import org.jails.property.ReflectionUtil;
import org.jails.property.SimpleMapper;
import org.jails.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
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

	protected Mapper mapper = new SimpleMapper();
	protected PropertiesWrapper propertiesWrapper;

	public static String STACKED = "stacked";
	public static String SIDE_BY_SIDE = "side";

	protected String name;
	protected String action;
	protected String method;
	protected String style = STACKED;
	protected String legend;
	protected String errorMessage;

	protected Map<Integer, List<String>> elements = new LinkedHashMap<Integer, List<String>>();
	protected Map<String, String> labels = new HashMap<String, String>();
	protected SimpleForm simpleForm;
	protected RepeaterTag repeatTag;

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

	public void setLegend(String legend) {
		this.legend = legend;
	}

	public String getLegend() {
		return legend;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getAction() {
		return action;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getMethod() {
		return method;
	}

	public void setStyle(String style) {
		this.style = (SIDE_BY_SIDE.equals(style)) ? SIDE_BY_SIDE : STACKED;
	}

	public String getStyle() {
		return style;
	}

	public boolean isStacked() {
		return style.equals(STACKED);
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public SimpleForm getSimpleForm() {
		return simpleForm;
	}

	public void addElement(String fieldName, int index) {
		List<String> formElements = elements.get(index);
		if (formElements == null) {
			formElements = new ArrayList<String>();
			elements.put(index, formElements);
		}
		if (!formElements.contains(fieldName)) formElements.add(fieldName);
	}

	public Map<Integer, List<String>> getElements() {
		return elements;
	}

	public void addLabel(String inputName, String label) {
		labels.put(inputName, label);
	}

	public Map<String, String> getLabels() {
		return labels;
	}

	public String[] getInputValue(ServletRequest request, String elementName, Integer repeaterIndex) {
		if (request.getParameter(elementName) != null) {
			return new String[]{request.getParameter(elementName)};
		} else if (propertiesWrapper != null) {
			return  propertiesWrapper.get(elementName, repeaterIndex);
		}
		return null;
	}

	public int doStartTag() throws JspException {
		return EVAL_BODY_TAG;
	}

	public int doEndTag() throws JspException {
		try {
			// Get the current bodyContent for this tag and
			//wrap with form content
			JspWriter jspOut = pageContext.getOut();
			if (bodyContent != null && !Strings.isEmpty(bodyContent.getString())) {
				FormConstructor formConstructor = new FormConstructor(this);
				formConstructor.wrapFormContent(bodyContent.getString());
			} else {
				generateForm();
			}
			return EVAL_PAGE;
		} catch (IOException ioe) {
			throw new JspException(ioe.getMessage());
		}
	}

	protected FormConstructor getFormConstructor() throws JspTagException {
		return new FormConstructor(this);
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

