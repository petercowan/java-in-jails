package org.jails.validation.client.jsr303;

import org.hibernate.validator.constraints.NotBlank;
import org.jails.property.CommonsPropertyUtils;
import org.jails.property.PropertyUtils;
import org.jails.property.parser.PropertyParser;
import org.jails.property.parser.SimplePropertyParser;
import org.jails.validation.client.AbstractClientValidationConstructor;
import org.jails.validation.client.posabsolute.PositionAbsolute;
import org.jails.validation.constraint.BeanConstraints;
import org.jails.validation.constraint.FieldMatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.metadata.ConstraintDescriptor;
import java.util.ArrayList;
import java.util.List;

public class Jsr303ClientValidationConstructor
		extends AbstractClientValidationConstructor<Jsr303ClientConstraintInfo> {
	private static Logger logger = LoggerFactory.getLogger(Jsr303ClientValidationConstructor.class);

	public Jsr303ClientValidationConstructor() {
		this(new SimplePropertyParser(), new CommonsPropertyUtils());
	}

	public Jsr303ClientValidationConstructor(PropertyParser propertyParser, PropertyUtils propertyUtils) {
		super(propertyParser, propertyUtils);
	}

	protected String _getValidationHtml(List<Jsr303ClientConstraintInfo> clientConstraints,
										Class classType, String property) {
		StringBuffer validationBuffer = null;

		for (Jsr303ClientConstraintInfo info : clientConstraints) {
			ConstraintDescriptor<?> descriptor = BeanConstraints.getInstance()
					.getConstraint(classType, property, info.getConstraint());
			if (descriptor == null) descriptor = BeanConstraints.getInstance()
					.findConstraint(classType, property, info.getConstraint());
			String validation = null;
			if (descriptor == null) {
				validation = info.getClientValidation();
			} else {
				if (info.getConstraint().equals(FieldMatch.class)) {
					logger.info("Getting Validation for FieldMatch.class");
					String matchField = (String) descriptor.getAttributes().get("matchField");
					logger.info("matchField: " + matchField);
					logger.info("property: " + property);
					if (property.equals(descriptor.getAttributes().get("matchField"))) {
						validation = info.parseClientValidation(descriptor);
					}
				} else {
					validation = info.parseClientValidation(descriptor);
				}
			}

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
		Jsr303ClientConstraintInfo constraintInfo =
				Jsr303ClientConstraintInfoRegistry.getInstance().getClientConstraint(NotBlank.class);

		List<Jsr303ClientConstraintInfo>
				clientConstraints = new ArrayList<Jsr303ClientConstraintInfo>();
		clientConstraints.add(constraintInfo);
		return getValidationHtml(clientConstraints, classType, propertyName);
	}
}
