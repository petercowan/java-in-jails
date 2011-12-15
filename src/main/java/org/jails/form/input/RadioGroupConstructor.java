package org.jails.form.input;

import org.jails.form.SimpleForm;

import javax.servlet.ServletRequest;

public class RadioGroupConstructor {
    protected RadioGroup tag;
    protected FormElement formTag;
    protected SimpleForm simpleForm;
    protected Repeater repeater;

    public RadioGroupConstructor(RadioGroup tag, FormElement formTag, Repeater repeater, ServletRequest request) {
        this.tag = tag;
        this.formTag = formTag;
        this.repeater = repeater;
        initFormTag();
    }

    protected void initFormTag() {
        simpleForm = formTag.getSimpleForm();
        formTag.addElement(tag.getName(), getIndex());
        formTag.addLabel(tag.getName(), tag.getLabel());
    }

    public String getOpeningHtml(String inputTagHtml) {
        StringBuffer tagHtml = new StringBuffer();

        String divCss;
        if (simpleForm != null && simpleForm.hasError()
                && simpleForm.fieldHasError(tag.getName(), getIndex())) {
            divCss = "error";
        } else {
            divCss = "form_field";
        }

        String id = formTag.getName() + "_" + tag.getName().replaceAll("[^a-zA-Z0-9]+", "_");
        tagHtml.append("<div class=\"" + divCss + "\" id=\""
                + "form_field_" + id + "\">\n\t");

        String label;
        if ("".equals(tag.getDisplayLabel())) label = "";
        else label = (tag.getDisplayLabel() != null) ? tag.getDisplayLabel() : tag.getLabel();

        if (!label.equals("")) {
            tagHtml.append("<label for=\"" + tag.getName() + "\" id=\"" + id + "_label\">");
            tagHtml.append(label).append(getLabelMarkerHtml());
            tagHtml.append("</label>\n\t");
        }

        if (tag.isStacked() ||
                (formTag.isStacked() && !FormElement.SIDE_BY_SIDE.equals(tag.getStyle()))) {
            tagHtml.append("<br />");
        }

        tagHtml.append(inputTagHtml).append("\n");

        if (formTag.getSimpleForm() != null
                && formTag.getSimpleForm().isFieldRequired(tag.getName())) {
            tagHtml.append(" *");
        }

        return tagHtml.toString();
    }

    private String getLabelMarkerHtml() {
        return (tag.getLabelMarker() != null) ? tag.getLabelMarker() : (formTag.getLabelMarker());
    }


    public String getClosingHtml() {
        StringBuffer tagHtml = new StringBuffer();

        tagHtml.append("</div>\n");

        return tagHtml.toString();
    }

    private int getIndex() {
        return (repeater != null) ? repeater.getIndex() : 0;
    }

}
