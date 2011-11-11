package org.jails.form.constructor;

import org.jails.form.input.FormInput;
import org.jails.form.input.FormTag;
import org.jails.form.input.Repeater;

import javax.servlet.ServletRequest;

public abstract class BodyTagInputConstructor<T extends FormInput> extends InputConstructor<T> {
	protected BodyTagInputConstructor(T tag, FormTag formTag, Repeater repeatTag, ServletRequest request) {
		super(tag, formTag, repeatTag, request);
	}

	public abstract String getOpeningHtml();

	public abstract String getClosingHtml();

	@Override
	public String wrapInputHtml(FormInput tag, String inputTagHtml) {
		return super.wrapInputHtml(tag, getOpeningHtml()
										+ inputTagHtml
										+ getClosingHtml());
	}

}
