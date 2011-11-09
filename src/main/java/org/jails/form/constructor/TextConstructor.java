package org.jails.form.constructor;

import org.jails.form.taglib.RepeaterTag;
import org.jails.form.taglib.SimpleFormTag;
import org.jails.form.taglib.TextTag;

import javax.servlet.ServletRequest;

public class TextConstructor extends TagInputConstructor<TextTag> {
	public TextConstructor(TextTag tag, SimpleFormTag formTag, RepeaterTag repeatTag, ServletRequest request) {
		super(tag, formTag, repeatTag, request);
	}

	@Override
	public String getInputHtml() {
		return "<input" + getTypeAttr("text") +
				getFieldNameAttr() +
				getInputIdAttr() +
				getClientValidationAttr() +
				getValueAttr(getFieldValue(0)) +
				getAttribute("size", tag.getSize()) + " />";
	}
}

