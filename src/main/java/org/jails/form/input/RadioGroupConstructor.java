package org.jails.form.input;

import org.jails.form.SimpleForm;

import javax.servlet.ServletRequest;

public class RadioGroupConstructor {
    protected RadioGroup tag;
    protected FormElement formTag;
    protected SimpleForm simpleForm;
    protected Repeater repeater;
    protected String inputId;

    public RadioGroupConstructor(RadioGroup tag, FormElement formTag, Repeater repeater, ServletRequest request) {
        this.tag = tag;
        this.formTag = formTag;
        this.repeater = repeater;
        initFormTag();
        initInputId();
    }

    private boolean hasError() {
        return simpleForm != null && simpleForm.hasError()
                && simpleForm.fieldHasError(tag.getName(), getIndex());
    }

    private boolean isRequired() {
        return simpleForm != null
                && simpleForm.isFieldRequired(tag.getName());
    }

    protected void initInputId() {
        inputId = formTag.getName() + "_" + tag.getName().replaceAll("[^a-zA-Z0-9]+", "_");

    }
    protected void initFormTag() {
        simpleForm = formTag.getSimpleForm();
        formTag.addElement(tag.getName(), getIndex());
        formTag.addLabel(tag.getName(), tag.getLabel());
    }

    public String getHtml(String inputTagHtml) {
        StringBuffer tagHtml = new StringBuffer();

        tagHtml.append(getOpeningHtml());

        tagHtml.append(getLabelHtml());

        tagHtml.append(getButtonHtml(inputTagHtml));

        tagHtml.append(getRequiredHtml());

        tagHtml.append(getClosingHtml());

        return tagHtml.toString();
    }

    public String getOpeningHtml() {
        return "<div class=\"form_field\" id=\"" + inputId + "_form_field\">\n";
    }

    public String getClosingHtml() {
        return "</div>\n";
    }

    private String getLabelHtml() {
        String label;
        if ("".equals(tag.getDisplayLabel())) label = "";
        else label = (tag.getDisplayLabel() != null) ? tag.getDisplayLabel() : tag.getLabel();

        StringBuffer tagHtml = new StringBuffer();
        if (!label.equals("")) {
            tagHtml.append("\t<label for=\"" + tag.getName() + "\" id=\"" + inputId + "_label\">\n");
            if (hasError()) tagHtml.append("\t\t<div class=\"error\" id=\"" + inputId + "_field_error\">");
            tagHtml.append(label).append(getLabelMarkerHtml());
            if (hasError()) tagHtml.append("</div>\n");
            tagHtml.append("\t</label>\n\t");
        }
        return tagHtml.toString();
    }

    private String getLabelMarkerHtml() {
        return (tag.getLabelMarker() != null) ? tag.getLabelMarker() : (formTag.getLabelMarker());
    }

    private String getButtonHtml(String inputTagHtml) {

        StringBuffer tagHtml = new StringBuffer();

        tagHtml.append("\t<div class=\"radio_button_group\" id=\"" + inputId + "_form_field_group\">");
        tagHtml.append("\t\t").append(inputTagHtml).append("\n");
        tagHtml.append("\t</div>");

        return tagHtml.toString();
    }

    public String getRequiredHtml() {
        if (isRequired()) {
            StringBuffer tagHtml = new StringBuffer();
            String css = (hasError()) ? "error" : "form_field_label";
            tagHtml.append("\t<span class=\"" + css + "\" id=\"" + inputId + "_field_required\">");
            tagHtml.append(" *");
            tagHtml.append("</span>\n");
            return tagHtml.toString();
        }
        return "";
    }

    private int getIndex() {
        return (repeater != null) ? repeater.getIndex() : 0;
    }
}
