package org.jails.form.constructor;

import org.jails.form.FormTag;
import org.jails.form.RadioButtonInput;
import org.jails.form.Repeater;
import org.jails.form.taglib.RadioGroupTag;
import org.jails.util.Strings;

import javax.servlet.ServletRequest;

public class RadioButtonConstructor
extends TagInputConstructor<RadioButtonInput> {
    private RadioGroupTag radioGroupTag;

	public RadioButtonConstructor(RadioButtonInput tag, RadioGroupTag radioGroupTag, FormTag formTag, Repeater repeatTag, ServletRequest request) {
		super(tag, formTag, repeatTag, request);
        this.radioGroupTag = radioGroupTag;
	}

    @Override
    protected void initFieldName() {
        initFieldName((radioGroupTag == null) ? tag.getName() : radioGroupTag.getName());
    }

    @Override
    public String getOpeningCss() {
        return "<span" + getLabelCssAttr() + ">\n\t";
    }

    @Override
    public String getClosingCss() {
        return "</span>\n";
    }


    @Override
    public String getLabelHtml() {
        return (radioGroupTag == null) ? super.getLabelHtml(tag.getLabel()) : "";
    }

    @Override
    public String getRequiredMarker() {
        return (radioGroupTag == null) ? super.getRequiredMarker() : "";
    }

    @Override
	public String getInputHtml() {
		String value = getFieldValue(0);
		String checked = ((value != null && value.equals(tag.getValue()))
				|| (value == null && Strings.getBoolean(tag.getChecked()))) ? " CHECKED" : "";

		return "<input" + getTypeAttr("radio") +
				getFieldNameAttr() +
				getInputIdAttr() +
				getClientValidationAttr() +
				getValueAttr(tag.getValue()) +
				getAttribute("tabIndex", tag.getTabIndex()) + checked + " /> " + tag.getLabel() ;
	}
}
