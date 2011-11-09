package org.jails.validation.client;

import javax.validation.metadata.ConstraintDescriptor;
import java.lang.annotation.Annotation;

public interface ClientConstraintInfo {
	public Class<? extends Annotation> getConstraint();

	public String getClientValidation();

	public String[] getAttributeNames();

	public boolean hasAttributes();

	public int attributeCount();

	public String parseClientValidation(ConstraintDescriptor constraint);
}
