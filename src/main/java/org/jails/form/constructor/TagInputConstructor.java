package org.jails.form.constructor;

import org.jails.form.FormInput;
import org.jails.form.FormTag;
import org.jails.form.Repeater;

import javax.servlet.ServletRequest;

public abstract class TagInputConstructor<T extends FormInput> extends InputConstructor<T> {
	protected TagInputConstructor(T tag, FormTag formTag, Repeater repeatTag, ServletRequest request) {
		super(tag, formTag, repeatTag, request);
	}

	public abstract String getInputHtml();

	@Override
	public String wrapInputHtml(FormInput tag, String inputTagHtml) {
		return (inputTagHtml == null)
				? super.wrapInputHtml(tag, getInputHtml())
				: super.wrapInputHtml(tag, getInputHtml() + inputTagHtml);
	}

	public String wrapInputHtml(FormInput tag) {
		return super.wrapInputHtml(tag, getInputHtml());
	}

}
