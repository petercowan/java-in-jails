package org.jails.form.constructor;

import org.jails.form.SimpleBeanForm;
import org.jails.form.SimpleForm;
import org.jails.form.SimpleRepeatableForm;
import org.jails.form.taglib.FormInput;
import org.jails.form.taglib.SimpleBeanFormTag;
import org.jails.form.taglib.SimpleFormTag;
import org.jails.form.taglib.SimpleRepeatableFormTag;
import org.jails.property.BeanToMap;
import org.jails.property.SimpleBeanToMap;
import org.jails.validation.client.ClientConstraintInfo;
import org.jails.validation.client.ClientConstraintInfoRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.List;

/**
 * InputConstructor initializes a set of Strings to be used to construct
 * the HTML for associated FormInput.
 *
 * @param <T>
 */
public abstract class InputConstructor<T extends FormInput> {
	protected static Logger logger = LoggerFactory.getLogger(InputConstructor.class);

	protected T tag;
	protected SimpleFormTag formTag;
	protected SimpleForm simpleForm;
	protected String fieldName;
	protected String[] fieldValues;
	protected String labelCss;
	protected String inputId;
	protected String validation;

	protected BeanToMap beanMapper;

	public InputConstructor(T tag, ServletRequest request) throws JspTagException {
		this.tag = tag;
		this.beanMapper = new SimpleBeanToMap();
		initFormTag();
		initFieldName();
		initCssClass();
		initFieldValues(request);
		initInputId();
		initClientValidation();
	}

	protected void initFormTag() throws JspTagException {
		formTag = (SimpleFormTag) TagSupport.findAncestorWithClass(tag, SimpleFormTag.class);
		if (formTag == null) {
			formTag = (SimpleBeanFormTag) TagSupport.findAncestorWithClass(tag, SimpleBeanFormTag.class);
			if (formTag == null) {
				formTag = (SimpleRepeatableFormTag) TagSupport.findAncestorWithClass(tag, SimpleRepeatableFormTag.class);
				if (formTag == null) {
					throw new JspTagException("A FormInput tag must be nested within a FormTag.");
				}
			}
		}
		simpleForm = formTag.getSimpleForm();
		formTag.addElement(tag.getName());
		formTag.addLabel(tag.getName(), tag.getLabel());
		logger.info("added element: " + tag.getName());

	}

	public SimpleFormTag getFormTag() {
		return formTag;
	}

	protected void initFieldName() {
		if (simpleForm instanceof SimpleBeanForm) {
			fieldName = simpleForm.getParameterName(tag.getName());
		} else if (simpleForm instanceof SimpleRepeatableForm) {
			fieldName = simpleForm.getIndexedParameterName(
					tag.getName(),
					((SimpleRepeatableFormTag) formTag).getIndex());
		}
		logger.info("constructing " + fieldName);
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getFieldNameAttr() {
		return getAttribute("name", fieldName);
	}

	protected void initCssClass() {
		if (simpleForm != null && simpleForm.hasError()
				&& formTag.fieldHasError(tag.getName())) {
			labelCss = "error";
		} else {
			labelCss = "formField";
		}
		logger.info("set css class" + labelCss);
	}

	public String getLabelCss() {
		return labelCss;
	}

	public String getLabelCssAttr() {
		return getAttribute("class", labelCss);
	}

	/**
	 * The fieldValue is decided in order of precedence:
	 * 1. from the request
	 * 2. from an object bound to the form
	 * 3. from the default value specified from the form tag
	 * 4. empty
	 *
	 * @param request
	 */
	protected void initFieldValues(ServletRequest request) {

		String fieldName = getFieldName();
		if (request.getParameter(fieldName) != null) {

			fieldValues = new String[]{request.getParameter(fieldName)};
		} else if (simpleForm != null && simpleForm.isBoundToObject()) {

			if (simpleForm instanceof SimpleBeanForm) {
				SimpleBeanForm beanForm = (SimpleBeanForm) simpleForm;
				fieldValues = beanMapper.getBeanPropertyValues(
						fieldName, beanForm.getBean());

			} else if (simpleForm instanceof SimpleRepeatableForm) {
				SimpleRepeatableForm beanForm = (SimpleRepeatableForm) simpleForm;
				SimpleRepeatableFormTag beanFormTag = (SimpleRepeatableFormTag) formTag;
				fieldValues = beanMapper.getBeanPropertyValues(
						fieldName, beanForm.getBean(beanFormTag.getIndex()));
			} else {
				fieldValues = new String[]{};
			}
		} else if (tag.getDefaultValue() != null) {
			fieldValues = new String[]{tag.getDefaultValue()};
		} else {
			fieldValues = new String[]{};
		}
		logger.info("fieldValues initialized");
	}

	public String[] getFieldValues() {
		return fieldValues;
	}

	public String getFieldValue(int index) {
		return (fieldValues != null && fieldValues.length > 0 && fieldValues[index] != null)
				? fieldValues[index] : "";
	}

	protected void initClientValidation() {
		if (simpleForm != null && simpleForm.getClassType() != null) {
			StringBuffer validationBuffer = null;

			Class classType = simpleForm.getClassType();
			String property = tag.getName();
			logger.warn("Setting ClientConstraints for " + classType.getSimpleName() + ": " + property);

			ClientConstraintInfoRegistry constraintInfoRegistry = ClientConstraintInfoRegistry.getInstance();
			List<ClientConstraintInfo> clientConstraints = constraintInfoRegistry
					.getClientConstraints(classType, property);
			validation = constraintInfoRegistry.getValidationConstructor()
					.getValidationHtml(clientConstraints, classType, property);
			logger.info("clientValidation: " + validation);
		}
	}

	public String getClientValidationAttr() {
		return (validation != null)
				? validation
				: "";
	}

	protected void initInputId() {
		inputId = (fieldName != null) ? fieldName.replaceAll("[^a-zA-Z0-9]+", "_") : "";
		logger.info("inputId init: " + inputId);
	}

	public String getInputId() {
		return inputId;
	}

	public String getInputIdAttr() {
		return getAttribute("id", inputId);
	}

	public String getAttribute(String attrName, String attrValue) {
		return " " + attrName + "=\"" + attrValue + "\"";
	}

	public String getTypeAttr(String type) {
		return getAttribute("type", type);
	}

	public String getValueAttr(String value) {
		return getAttribute("value", value);
	}

	public String wrapInputHtml(FormInput tag, String inputTagHtml) {
		StringBuffer tagHtml = new StringBuffer();

		tagHtml.append("<p" +
				getLabelCssAttr() + "><label" +
				getAttribute("forName", tag.getName()) + ">");

		tagHtml.append(tag.getLabel());
		if (getFormTag().isStacked()) tagHtml.append("<br />");
		else tagHtml.append(": ");

		tagHtml.append(inputTagHtml);

		if (getFormTag().getSimpleForm() != null
				&& getFormTag().getSimpleForm().isFieldRequired(tag.getName())) {
			tagHtml.append(" *");
		}

		tagHtml.append("</label></p>");

		return tagHtml.toString();
	}
}
