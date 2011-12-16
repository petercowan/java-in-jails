package org.jails.form.taglib;

import org.jails.form.input.FormElement;
import org.jails.form.input.RadioButtonInput;
import org.jails.form.input.RadioGroup;
import org.jails.form.input.RadioGroupConstructor;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RadioGroupTag
    extends BodyTagSupport
        implements RadioGroup {
    private String name;
    private String label;
    private String displayLabel;
    private String labelMarker;
    private Map<RadioButtonInput, Integer> buttons = new HashMap<RadioButtonInput, Integer>();
    private String style;

    FormTag formTag;
    RepeaterTag repeatTag;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDisplayLabel() {
        return displayLabel;
    }

    public void setDisplayLabel(String displayLabel) {
        this.displayLabel = displayLabel;
    }

    public String getLabelMarker() {
        return labelMarker;
    }

    public void setLabelMarker(String labelMarker) {
        this.labelMarker = labelMarker;
    }

    public Map<RadioButtonInput, Integer> getButtons() {
        return buttons;
    }

    public void setButtons(Map<RadioButtonInput, Integer> buttons) {
        this.buttons = buttons;
    }

    public void addButton(RadioButtonInput button) {
        if (buttons == null) buttons = new HashMap<RadioButtonInput, Integer>();
        buttons.put(button, buttons.size());
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public boolean isStacked() {
        return FormElement.STACKED.equals(style);
    }

    @Override
    public int doStartTag() throws JspException {
        formTag = (FormTag) TagSupport.findAncestorWithClass(this, FormTag.class);
        if (formTag == null) {
            if (formTag == null) {
                throw new JspTagException("A RepeatTag tag must be nested within a FormTag.");
            }
        }
        repeatTag = (RepeaterTag) TagSupport.findAncestorWithClass(this, RepeaterTag.class);

        return EVAL_BODY_TAG;
    }

    public int doEndTag() throws JspException {
        try {
            // Get the current bodyContent for this tag and
            //wrap with form content
            JspWriter out = pageContext.getOut();
            RadioGroupConstructor radioGroupConstructor = new RadioGroupConstructor(this, formTag, repeatTag, pageContext.getRequest());
            String content = (bodyContent == null) ? "" : bodyContent.getString();
            out.print(radioGroupConstructor.getHtml(content));

            return EVAL_PAGE;
        } catch (IOException ioe) {
            throw new JspException(ioe.getMessage());
        }
    }


}
