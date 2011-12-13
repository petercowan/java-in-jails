package org.jails.form.constructor;

import org.jails.form.FormInput;
import org.jails.form.FormTag;
import org.jails.form.Repeater;

import javax.servlet.ServletRequest;

public abstract class BodyTagInputConstructor<T extends FormInput> extends InputConstructor<T> {
	protected BodyTagInputConstructor(T tag, FormTag formTag, Repeater repeatTag, ServletRequest request) {
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
