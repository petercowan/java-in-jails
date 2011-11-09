package org.jails.validation.client;

import java.util.List;

public interface ClientValidationConstructor {
	public String getValidationHtml(List<ClientConstraintInfo> constraints,
									Class classType, String propertyName);
}
