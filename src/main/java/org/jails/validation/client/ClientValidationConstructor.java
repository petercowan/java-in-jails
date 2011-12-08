package org.jails.validation.client;

import java.util.List;

public interface ClientValidationConstructor<T> {
	public String getValidationHtml(List<ClientConstraintInfo<T>> constraints,
									Class classType, String propertyName);

	public String getRequiredHtml(Class classType, String propertyName);
}
