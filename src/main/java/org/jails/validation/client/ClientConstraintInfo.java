package org.jails.validation.client;

import javax.validation.metadata.ConstraintDescriptor;

public interface ClientConstraintInfo<T> {
	public T getConstraint();

	public String getClientValidation();

	public String[] getAttributeNames();

	public boolean hasAttributes();

	public int attributeCount();

	public String parseClientValidation(ConstraintDescriptor constraint);
}
