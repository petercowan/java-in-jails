package org.jails.form.constructor;

import org.jails.form.SimpleForm;
import org.jails.form.SimpleFormParams;
import org.jails.form.input.FormInput;
import org.jails.form.input.FormTag;
import org.jails.form.input.Repeater;
import org.jails.property.ReflectionUtil;
import org.jails.util.SimpleFormatter;
import org.jails.validation.client.ClientConstraintInfo;
import org.jails.validation.client.ClientConstraintInfoRegistry;
import org.jails.validation.constraint.IsDecimal;
import org.jails.validation.constraint.IsInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import java.util.List;

/**
 * InputConstructor initializes a set of Strings to be used to construct
 * the HTML for associated FormInput.
 *
 * @param <T>
 */
public abstract class InputConstructor<T extends FormInput> {
	protected static Logger logger = LoggerFactory.getLogger(InputConstructor.class);

	protected static SimpleFormParams simpleFormParams = new SimpleFormParams();
	protected static SimpleFormatter formatter = new SimpleFormatter();

	protected T tag;
	protected FormTag formTag;
	protected SimpleForm simpleForm;
	protected Repeater repeater;
	protected String fieldName;
	protected String[] fieldValues;
	protected String labelCss;
	protected String inputId;
	protected String validation;

	public InputConstructor(T tag, FormTag formTag, Repeater repeatTag, ServletRequest request) {
		this.tag = tag;
		this.formTag = formTag;
		this.repeater = repeatTag;
		initFormTag();
		initFieldName();
		initCssClass();
		initFieldValues(request);
		initInputId();
		initClientValidation();
	}

	protected void initFormTag() {
		simpleForm = formTag.getSimpleForm();
		logger.info("adding element: " + tag.getName() + " of type "
				+ tag.getClass().getSimpleName() + " to form: " + formTag.getClass().getSimpleName());
		logger.info("adding to element index: " + getIndex());
		formTag.addElement(tag.getName(), getIndex());
		logger.info("setting element label: " + tag.getLabel());
		formTag.addLabel(tag.getName(), tag.getLabel());
		logger.info("added element: " + tag.getName());
	}

	public FormTag getFormTag() {
		return formTag;
	}

	protected void initFieldName() {
		if (repeater != null) {
			fieldName = simpleFormParams.getFormIndexedParameterName(
					formTag.getName(), tag.getName(),
					repeater.getIndex());
		} else {
			fieldName = simpleFormParams.getParameterName(formTag.getName(), tag.getName());
		}
		logger.info("constructing " + fieldName);
	}

	public String getFieldName() {
		return fieldName;
	}

	private int getIndex() {
		return (repeater != null) ? repeater.getIndex() : 0;
	}

	protected void initCssClass() {
		if (simpleForm != null && simpleForm.hasError()
				&& simpleForm.fieldHasError(fieldName, getIndex())) {
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
		fieldValues = formTag.getInputValue(request, fieldName, (repeater == null) ? null : repeater.getIndex());
		if (fieldValues == null) {
			if (tag.getDefaultValue() != null) fieldValues = new String[]{tag.getDefaultValue()};
			else fieldValues = new String[]{};
		}
		if (tag.getFormat() != null) {
			for (int i = 0; i < fieldValues.length; i++) {
				String fieldValue = fieldValues[i];
				fieldValues[i] = formatter.format(fieldValue, tag.getFormat());
			}
		}
		logger.info("fieldValues initialized");
	}

	public String[] getFieldValues() {
		return fieldValues;
	}

	public String getFieldValue(int index) {
		String value = (fieldValues != null && fieldValues.length > 0 && fieldValues[index] != null)
				? fieldValues[index] : "";

		return value;
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
			Class returnType = ReflectionUtil.getGetterMethodReturnType(classType, property);
			if (ReflectionUtil.isDecimal(returnType)) {
				clientConstraints.add(constraintInfoRegistry.getClientConstraint(IsDecimal.class));
			} else if (ReflectionUtil.isInteger(returnType)) {
				clientConstraints.add(constraintInfoRegistry.getClientConstraint(IsInteger.class));
			}
			logger.info("Getting Validation script");
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

	public String getAttribute(String attrName) {
		String attr = (tag.getAttributes() != null) ? tag.getAttributes().get(attrName) : null;
		if (attr != null) logger.info("found attribute " + attrName + " with value " + attr);
		return attr;
	}

	public String getAttribute(String attrName, String attrValue) {
		if (getAttribute(attrName) != null) attrValue = getAttribute(attrName);
		return (attrValue != null) ? " " + attrName + "=\"" + attrValue + "\"" : "";
	}

	public String getAttributes() {
		StringBuffer attrString = new StringBuffer();
		for (String attrName : tag.getAttributes().keySet()) {
			getAttribute(attrName, tag.getAttributes().get(attrName));
		}
		return attrString.toString();
	}

	public String getFieldNameAttr() {
		return getAttribute("name", fieldName);
	}

	public String getInputIdAttr() {
		return getAttribute("id", inputId);
	}

	public String getTypeAttr(String type) {
		return getAttribute("type", type);
	}

	public String getValueAttr(String value) {
		return getAttribute("value", value);
	}

	public String wrapInputHtml(FormInput tag, String inputTagHtml) {
		StringBuffer tagHtml = new StringBuffer();

		logger.info("wrapInputHtml");
		tagHtml.append("<p" +
				getLabelCssAttr() + "><label" +
				getAttribute("forName", tag.getName()) + ">");

		logger.info("getLabel");
		tagHtml.append(tag.getLabel());
		logger.info("isStacked");
		if (getFormTag().isStacked()) tagHtml.append("<br />");
		else tagHtml.append(": ");

		tagHtml.append(inputTagHtml);

		logger.info("getFormTag");
		if (getFormTag().getSimpleForm() != null
				&& getFormTag().getSimpleForm().isFieldRequired(tag.getName())) {
			tagHtml.append(" *");
		}

		logger.info("end wrapInputHtml");
		tagHtml.append("</label></p>");

		return tagHtml.toString();
	}
}
