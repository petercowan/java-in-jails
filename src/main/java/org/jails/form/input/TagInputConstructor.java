package org.jails.form.input;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;

public abstract class TagInputConstructor<T extends InputElement> extends InputConstructor<T> {
	private static Logger logger = LoggerFactory.getLogger(TagInputConstructor.class);

    protected TagInputConstructor() {
    }

    protected TagInputConstructor(T tag, FormElement formTag, Repeater repeatTag, ServletRequest request) {
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
