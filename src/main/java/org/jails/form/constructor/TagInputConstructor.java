package org.jails.form.constructor;

import org.jails.form.FormInput;
import org.jails.form.FormTag;
import org.jails.form.Repeater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;

public abstract class TagInputConstructor<T extends FormInput> extends InputConstructor<T> {
	private static Logger logger = LoggerFactory.getLogger(TagInputConstructor.class);

	protected TagInputConstructor(T tag, FormTag formTag, Repeater repeatTag, ServletRequest request) {
		super(tag, formTag, repeatTag, request);
	}

	public abstract String getInputHtml();

	@Override
	public String wrapInputHtml(String inputTagHtml) {
		return (inputTagHtml == null)
				? super.wrapInputHtml(getInputHtml())
				: super.wrapInputHtml(getInputHtml() + inputTagHtml);
	}

	public String wrapInputHtml() {
		return super.wrapInputHtml(getInputHtml());
	}
}
