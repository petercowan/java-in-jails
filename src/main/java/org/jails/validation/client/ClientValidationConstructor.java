package org.jails.validation.client;

import java.util.List;

public interface ClientValidationConstructor<T extends ClientConstraintInfo> {
	public String getValidationHtml(List<T> constraints,
									Class classType, String propertyName);

	public String getRequiredHtml(Class classType, String propertyName);
}
