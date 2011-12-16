package org.jails.form.input;

import org.jails.form.taglib.RadioGroupTag;
import org.jails.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;

public class RadioButtonConstructor
extends TagInputConstructor<RadioButtonInput> {
    protected static Logger logger = LoggerFactory.getLogger(RadioButtonConstructor.class);

    private RadioGroupTag radioGroupTag;

	public RadioButtonConstructor(RadioButtonInput tag, RadioGroupTag radioGroupTag, FormElement formTag, Repeater repeatTag, ServletRequest request) {
        this.tag = tag;
        this.formTag = formTag;
        this.repeater = repeatTag;
        this.radioGroupTag = radioGroupTag;
        init(request);
	}

    @Override
    protected void initFormTag() {
        simpleForm = formTag.getSimpleForm();
    }

    @Override
    protected void initFieldName() {
        initFieldName((radioGroupTag == null) ? tag.getName() : radioGroupTag.getName());
    }

    @Override
    protected void initInputId() {
        super.initInputId();
        logger.info("inputId " + inputId);
        logger.info("radioGroupTag " + radioGroupTag);
        logger.info("radioGroupTag.getButtons() " + radioGroupTag.getButtons());
        inputId += "_" + radioGroupTag.getButtons().get(tag);
    }

    @Override
    public String getOpeningCss() {
        return "<span" + getAttribute("id", inputId + "_form_field") + ">\n\t";
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
