package org.jails.form.input;

import javax.servlet.ServletRequest;

public abstract class BodyTagInputConstructor<T extends InputElement> extends InputConstructor<T> {
    protected BodyTagInputConstructor() {
    }

    protected BodyTagInputConstructor(T tag, FormElement formTag, Repeater repeatTag, ServletRequest request) {
		super(tag, formTag, repeatTag, request);
	}

	public abstract String getOpeningHtml();

	public abstract String getClosingHtml();

	@Override
	public String wrapInputHtml(String inputTagHtml) {
		return super.wrapInputHtml(getOpeningHtml()
										+ inputTagHtml
										+ getClosingHtml());
	}

}
