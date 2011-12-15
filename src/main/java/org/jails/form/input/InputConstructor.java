package org.jails.form.input;

import org.jails.form.SimpleForm;
import org.jails.form.SimpleFormParams;
import org.jails.form.taglib.HiddenTag;
import org.jails.form.taglib.PasswordTag;
import org.jails.form.taglib.TextAreaTag;
import org.jails.form.taglib.TextTag;
import org.jails.property.CommonsPropertyUtils;
import org.jails.property.PropertyUtils;
import org.jails.property.ReflectionUtil;
import org.jails.util.SimpleFormatter;
import org.jails.validation.client.ClientConstraintInfo;
import org.jails.validation.client.ClientConstraintInfoRegistry;
import org.jails.validation.client.jsr303.Jsr303ClientConstraintInfoRegistry;
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
public abstract class InputConstructor<T extends InputElement> {
    protected static Logger logger = LoggerFactory.getLogger(InputConstructor.class);

    protected static SimpleFormParams simpleFormParams = new SimpleFormParams();
    protected static SimpleFormatter formatter = new SimpleFormatter();
    protected static PropertyUtils propertyUtils = new CommonsPropertyUtils();

    protected T tag;
    protected FormElement formTag;
    protected SimpleForm simpleForm;
    protected Repeater repeater;
    protected String fieldName;
    protected String[] fieldValues;
    protected String labelCss;
    protected String inputId;
    protected String validation;

    protected InputConstructor() {
    }

    public InputConstructor(T tag, FormElement formTag, Repeater repeatTag, ServletRequest request) {
        this.tag = tag;
        this.formTag = formTag;
        this.repeater = repeatTag;
        init(request);
    }

    protected void init(ServletRequest request) {
        initFormTag();
        initFieldName();
        initCssClass();
        initFieldValues(request);
        initInputId();
        initClientValidation();
    }

    protected void initFormTag() {
        simpleForm = formTag.getSimpleForm();
        formTag.addElement(tag.getName(), getIndex());
        formTag.addLabel(tag.getName(), tag.getLabel());
    }

    public FormElement getFormTag() {
        return formTag;
    }

    protected void initFieldName() {
        initFieldName(tag.getName());
    }

    protected void initFieldName(String tagName) {
        if (repeater != null) {
            fieldName = simpleFormParams.getFormIndexedParameterName(
                    formTag.getName(), tagName,
                    repeater.getIndex());
        } else {
            fieldName = simpleFormParams.getParameterName(formTag.getName(), tagName);
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
                && simpleForm.fieldHasError(tag.getName(), getIndex())) {
            labelCss = "error";
        } else {
            labelCss = "form_field";
        }
        logger.info("set css class " + labelCss);
    }

    public String getLabelCss() {
        return labelCss;
    }

    public String getLabelCssAttr() {
        return getAttribute("class", labelCss) + " " + getAttribute("id", "form_field_" + inputId);
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

            ClientConstraintInfoRegistry constraintInfoRegistry = Jsr303ClientConstraintInfoRegistry.getInstance();
            Class classType = simpleForm.getClassType();
            String property = tag.getName();
            logger.warn("Setting ClientConstraints for " + classType.getSimpleName() + ": " + property);
            if (tag instanceof TextTag || tag instanceof PasswordTag || tag instanceof TextAreaTag
                    || tag instanceof HiddenTag) {

                logger.warn("Loading ClientConstraints from " + constraintInfoRegistry);
                List<ClientConstraintInfo> clientConstraints = constraintInfoRegistry
                        .getClientConstraints(classType, property);

                if (clientConstraints != null) {
                    Class propertyType = propertyUtils.getPropertyType(classType, property);
                    if (ReflectionUtil.isDecimal(propertyType)) {
                        clientConstraints.add(constraintInfoRegistry.getClientConstraint(IsDecimal.class));
                    } else if (ReflectionUtil.isInteger(propertyType)) {
                        clientConstraints.add(constraintInfoRegistry.getClientConstraint(IsInteger.class));
                    }
                    logger.info("Getting Validation script");
                    validation = constraintInfoRegistry.getValidationConstructor()
                            .getValidationHtml(clientConstraints, classType, property);
                    logger.info("clientValidation: " + validation);
                }
            } else {
                if (formTag.getSimpleForm().isFieldRequired(tag.getName())) {
                    validation = constraintInfoRegistry
                            .getValidationConstructor().getRequiredHtml(classType, property);//" class=\"validate[required]\"";
                }
            }
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

    public String wrapInputHtml(String inputTagHtml) {
        StringBuffer tagHtml = new StringBuffer();

        tagHtml.append(getOpeningCss());

        tagHtml.append(getLabelHtml());

        tagHtml.append(getLineBreakHtml());

        tagHtml.append(inputTagHtml).append("\n");

        tagHtml.append(getRequiredMarker());

        tagHtml.append(getClosingCss());

        return tagHtml.toString();
    }

    public String getOpeningCss() {
        return "<div" + getLabelCssAttr() + ">\n\t";
    }

    public String getClosingCss() {
        return "</div>\n";
    }

    public String getLabelHtml() {
        if ("".equals(tag.getDisplayLabel())) return "";
        return getLabelHtml((tag.getDisplayLabel() != null) ? tag.getDisplayLabel() : tag.getLabel());
    }

    protected String getLabelHtml(String label) {
        StringBuffer labelHtml = new StringBuffer();

        labelHtml.append("<label" + getAttribute("for", tag.getName()) + " id=\"" + inputId + "_label" + "\">");
        labelHtml.append(label).append(getLabelMarkerHtml());
        labelHtml.append("</label>\n\t");

        return labelHtml.toString();
    }

    private String getLabelMarkerHtml() {
        return (tag.getLabelMarker() != null) ? tag.getLabelMarker() : (formTag.getLabelMarker());
    }

    public String getRequiredMarker() {
        StringBuffer tagHtml = new StringBuffer();

        if (formTag.getSimpleForm() != null
                && formTag.getSimpleForm().isFieldRequired(tag.getName())) {
            tagHtml.append(" *");
        }

        return tagHtml.toString();
    }

    public String getLineBreakHtml() {
        StringBuffer lineBreakHtml = new StringBuffer();

        if (tag.isStacked() ||
                (getFormTag().isStacked() && !FormElement.SIDE_BY_SIDE.equals(tag.getStyle()))) {
            lineBreakHtml.append("<br />");
        }

        return lineBreakHtml.toString();
    }
}
