package org.jails.form.input;

import javax.servlet.ServletRequest;

public class HiddenConstructor extends TagInputConstructor<HiddenInput>{

	public HiddenConstructor(HiddenInput tag, FormElement formTag, Repeater repeatTag, ServletRequest request) {
		super(tag, formTag, repeatTag, request);
	}

    @Override
    public String getOpeningCss() {
        return "";
    }

    @Override
    public String getClosingCss() {
        return "";
    }

    @Override
    public String getLabelHtml() {
        return "";
    }

    @Override
	public String getInputHtml() {
		return "<input" + getTypeAttr("hidden") +
				getFieldNameAttr() +
				getInputIdAttr() +
				getClientValidationAttr() +
				getValueAttr(getFieldValue(0)) + " />";
	}

	public String wrapInputHtml(InputElement tag) {
		return getInputHtml();
	}

}

