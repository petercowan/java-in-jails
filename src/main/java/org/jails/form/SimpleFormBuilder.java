package org.jails.form;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleFormBuilder<T> extends AbstractFormBuilder<T> {
	private static Logger logger = LoggerFactory.getLogger(SimpleFormBuilder.class);

	protected SimpleFormBuilder(Class classType) {
		_validateAs(classType);
	}

	protected SimpleFormBuilder(T bean) {
		_bindTo(bean);
	}

	protected SimpleFormBuilder(T[] beanArray) {
		_bindTo(beanArray);
	}
}
