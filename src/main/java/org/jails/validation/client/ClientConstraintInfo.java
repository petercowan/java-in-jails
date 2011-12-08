package org.jails.validation.client;

public interface ClientConstraintInfo<T,U> {
	public T getConstraint();

	public String getClientValidation();

	public String[] getAttributeNames();

	public boolean hasAttributes();

	public int attributeCount();

	public String parseClientValidation(U constraint);
}
