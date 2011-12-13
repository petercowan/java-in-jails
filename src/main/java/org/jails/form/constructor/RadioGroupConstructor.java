package org.jails.form.constructor;

import org.jails.form.FormTag;
import org.jails.form.RadioGroup;
import org.jails.form.Repeater;
import org.jails.form.SimpleForm;

import javax.servlet.ServletRequest;

public class RadioGroupConstructor {
    protected RadioGroup tag;
    protected FormTag formTag;
    protected SimpleForm simpleForm;
    protected Repeater repeater;

    public RadioGroupConstructor(RadioGroup tag, FormTag formTag, Repeater repeater, ServletRequest request) {
        this.tag = tag;
        this.formTag = formTag;
        this.simpleForm = formTag.getSimpleForm();
        this.repeater = repeater;
    }

    public String getOpeningHtml(String inputTagHtml) {
        StringBuffer tagHtml = new StringBuffer();

        tagHtml.append("<div>\n\t");

        tagHtml.append("<label for=\"" + tag.getName() + "\">");
        tagHtml.append(tag.getLabel()).append(getLabelMarkerHtml());
        tagHtml.append("</label>\n\t");

        if (tag.isStacked() ||
                (formTag.isStacked() && !FormTag.SIDE_BY_SIDE.equals(tag.getStyle()))) {
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
}
