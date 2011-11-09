package org.jails.validation.client;

import org.jails.validation.BeanConstraints;

import javax.validation.metadata.ConstraintDescriptor;
import java.util.List;

public class PositionRelativeValidationConstructor
	implements ClientValidationConstructor {
	public String getValidationHtml(List<ClientConstraintInfo> clientConstraints,
									Class classType, String property) {
		StringBuffer validationBuffer = null;

		for (ClientConstraintInfo info : clientConstraints) {
			ConstraintDescriptor<?> descriptor = BeanConstraints.getInstance()
					.getConstraint(classType, property, info.getConstraint());
			String validation = info.parseClientValidation(descriptor);

			if (validationBuffer == null) {
				validationBuffer = new StringBuffer();
				validationBuffer.append(validation);
			} else {
				validationBuffer.append(",").append(validation);
			}
		}
		return (validationBuffer != null)
				? " class=\"validate[" + validationBuffer.toString() + "]\""
				: null;
	}
}
