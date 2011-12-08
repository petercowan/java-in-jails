package org.jails.validation.client.activejdbc;

import org.jails.property.PropertyUtils;
import org.jails.property.parser.PropertyParser;
import org.jails.validation.client.AbstractClientValidationConstructor;
import org.jails.validation.client.posabsolute.PositionAbsolute;
import org.javalite.activejdbc.validation.AttributePresenceValidator;
import org.javalite.activejdbc.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ActiveJDBCValidationConstructor
	extends AbstractClientValidationConstructor<ActiveJDBCConstraintInfo> {
	private static Logger logger = LoggerFactory.getLogger(ActiveJDBCValidationConstructor.class);

	protected ActiveJDBCValidationConstructor(PropertyParser propertyParser, PropertyUtils propertyUtils) {
		super(propertyParser, propertyUtils);
	}

	@Override
	protected String _getValidationHtml(List<ActiveJDBCConstraintInfo> clientConstraints, Class classType, String property) {
		StringBuffer validationBuffer = null;

		for (ActiveJDBCConstraintInfo info : clientConstraints) {
			Validator validator = ActiveJDBCValidatorUtil.getValidators(classType, property,info.getConstraint());
			String validation = info.parseClientValidation(validator);

			if (validation != null) {
				if (validationBuffer == null) {
					validationBuffer = new StringBuffer();
					validationBuffer.append(validation);
				} else {
					validationBuffer.append(",").append(validation);
				}
			}
		}
		return (validationBuffer != null)
				? " " + PositionAbsolute.INPUT_ATTRIBUTE + "=\""
					+ PositionAbsolute.VALIDATION_FUNCTION + "[" + validationBuffer.toString() + "]\""
				: null;
	}

	public String getRequiredHtml(Class classType, String propertyName) {
		ActiveJDBCConstraintInfo constraintInfo =
				ActiveJDBCConstraintInfoRegistry.getInstance().getClientConstraint(AttributePresenceValidator.class);

		List<ActiveJDBCConstraintInfo>
				clientConstraints = new ArrayList<ActiveJDBCConstraintInfo>();
		clientConstraints.add(constraintInfo);
		return getValidationHtml(clientConstraints, classType, propertyName);
	}
}
