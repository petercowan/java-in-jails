package org.jails.validation.client.jsr303;

import org.jails.validation.client.AbstractClientConstraintInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.metadata.ConstraintDescriptor;
import java.lang.annotation.Annotation;
import java.util.Map;

public class Jsr303ClientConstraintInfo
		extends AbstractClientConstraintInfo<Class<? extends Annotation>, ConstraintDescriptor> {
	private static Logger logger = LoggerFactory.getLogger(Jsr303ClientConstraintInfo.class);

	private Class<? extends Annotation> constraint;
	protected String clientValidation;
	protected String[] attributeNames;

	public Jsr303ClientConstraintInfo(Class<? extends Annotation> constraint, String clientValidation, String... attributeNames) {
		super(constraint, clientValidation, attributeNames);
	}

	public String parseClientValidation(ConstraintDescriptor constraint) {
		if (constraint == null) return "";
		String validation;
		Map<String, Object> attributes = constraint.getAttributes();
		if (hasAttributes() && attributes != null
					&& attributeCount() <= attributes.size()) {
				validation = parseClientValidation(attributes);
		} else {
			 validation = getClientValidation();
		}
		return validation;
	}
}


