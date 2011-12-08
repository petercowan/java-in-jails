package org.jails.validation.client.activejdbc;

import org.jails.validation.client.AbstractClientConstraintInfo;
import org.javalite.activejdbc.validation.AttributePresenceValidator;
import org.javalite.activejdbc.validation.EmailValidator;
import org.javalite.activejdbc.validation.NumericValidator;
import org.javalite.activejdbc.validation.RangeValidator;
import org.javalite.activejdbc.validation.RegexpValidator;
import org.javalite.activejdbc.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActiveJDBCConstraintInfo extends AbstractClientConstraintInfo<Class<? extends Validator>, Validator> {
	private static Logger logger = LoggerFactory.getLogger(ActiveJDBCConstraintInfo.class);

	public ActiveJDBCConstraintInfo(Class<? extends Validator> constraint, String clientValidation, String... attributeNames) {
		super(constraint, clientValidation, attributeNames);
	}

	public String parseClientValidation(Validator constraint) {
		if (constraint instanceof AttributePresenceValidator)
			return parseAttributePresenceValidation((AttributePresenceValidator) constraint);
		if (constraint instanceof EmailValidator)
			return parseEmailValidation((EmailValidator) constraint);
		if (constraint instanceof RangeValidator)
			return parseRangeValidation((RangeValidator) constraint);
		if (constraint instanceof NumericValidator)
			return parseNumericValidation((NumericValidator) constraint);
		if (constraint instanceof RegexpValidator)
			return parseRegexpValidation((RegexpValidator) constraint);
		else
			return "";
	}

	public String parseAttributePresenceValidation(AttributePresenceValidator constraint) {
		return null;
	}

	public String parseEmailValidation(EmailValidator constraint) {
		return null;
	}

	public String parseRangeValidation(RangeValidator constraint) {
		//todo - get min/max fields
		return null;
	}

	public String parseNumericValidation(NumericValidator constraint) {
		return null;
	}

	private String parseRegexpValidation(RegexpValidator constraint) {
		//todo - get regexp field
		return null;  //To change body of created methods use File | Settings | File Templates.
	}
}
