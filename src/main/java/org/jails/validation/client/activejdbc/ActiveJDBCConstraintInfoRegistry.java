package org.jails.validation.client.activejdbc;

import org.jails.validation.client.ClientConstraintInfo;
import org.jails.validation.client.ClientConstraintInfoRegistry;
import org.jails.validation.client.posabsolute.PositionAbsolute;
import org.javalite.activejdbc.Registry;
import org.javalite.activejdbc.validation.AttributePresenceValidator;
import org.javalite.activejdbc.validation.EmailValidator;
import org.javalite.activejdbc.validation.NumericValidator;
import org.javalite.activejdbc.validation.RangeValidator;
import org.javalite.activejdbc.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActiveJDBCConstraintInfoRegistry
		extends ClientConstraintInfoRegistry<Class<? extends Validator>,ActiveJDBCConstraintInfo> {
	private static Logger logger = LoggerFactory.getLogger(ActiveJDBCConstraintInfoRegistry.class);

	private static ActiveJDBCConstraintInfoRegistry instance;

	private ActiveJDBCConstraintInfoRegistry() {
        super();
		registry = new HashMap<Class<? extends Validator>, ActiveJDBCConstraintInfo>();
		addClientConstraint(AttributePresenceValidator.class, PositionAbsolute.REQUIRED);
		addClientConstraint(EmailValidator.class, PositionAbsolute.EMAIL);
		addClientConstraint(RangeValidator.class, PositionAbsolute.MIN_VALUE + "," + PositionAbsolute.MAX_VALUE, "min", "max");
		addClientConstraint(NumericValidator.class, PositionAbsolute.INTEGER);
		//addClientConstraint(RegexpValidator.class,"custom[${regexp}]","regex");
//		addClientConstraint(CreditCardNumber.class, PositionAbsolute.CREDIT_CARD);
//		addClientConstraint(EmailValidator.class, PositionAbsolute.EMAIL);
//		addClientConstraint(Length.class, PositionAbsolute.MIN_SIZE + "," + PositionAbsolute.MAX_SIZE, "min", "max");
//		addClientConstraint(Min.class, PositionAbsolute.MIN_VALUE, "value");
//		addClientConstraint(DecimalMin.class, PositionAbsolute.MIN_VALUE, "value");
//		addClientConstraint(Max.class, PositionAbsolute.MAX_VALUE, "value");
//		addClientConstraint(DecimalMax.class, PositionAbsolute.MAX_VALUE, "value");
//		addClientConstraint(Past.class, PositionAbsolute.PAST);
//		addClientConstraint(Future.class, PositionAbsolute.FUTURE);
//		addClientConstraint(Size.class, PositionAbsolute.MIN_SIZE + "," + PositionAbsolute.MAX_SIZE, "min", "max");
//		addClientConstraint(FieldMatch.class, PositionAbsolute.EQUALS, "field", "matchField");
//		addClientConstraint(IsInteger.class, PositionAbsolute.INTEGER);

//		addClientConstraint(FieldMatch.class, "equals[${form.fieldMatch.id}]");
//		addClientConstraint(AssertFalse.class, "");
//		addClientConstraint(AssertTrue.class,"");
//		addClientConstraint(Digits.class,"future[now]","maxIntegerDigits","maxFractionDigits");
//		include both attributes, even if only using one in the validation field
//		addClientConstraint(StrongPassword.class,"custom[]");
		/**
		 .??[a-zA-Z]
		 .??[0-9]
		 .??[:,!,@,#,$,%,^,&,*,?,_,-,=,+,~]
		 **/
//		addClientConstraint(URL.class, "custom[url]");
		validationConstructor = new ActiveJDBCValidationConstructor();
	}

	public static ActiveJDBCConstraintInfoRegistry getInstance() {
		if (instance == null) {
			synchronized (ActiveJDBCConstraintInfoRegistry.class) {
				instance = new ActiveJDBCConstraintInfoRegistry();
			}
		}
		return instance;
	}

	protected ActiveJDBCConstraintInfo newClientConstraint(Class<? extends Validator> constraint, String clientFunction, String... attributeNames) {
		return new ActiveJDBCConstraintInfo(constraint, clientFunction, attributeNames);
	}

	//todo - dedup code, mapping constraints to validators
	protected List<String> _getClientValidations(Class classType, String property) {

		List<String> clientValidations = new ArrayList<String>();
		Registry registry = Registry.instance();
		List<Validator> validators = ActiveJDBCValidatorUtil.getValidators(classType, property);
		for (Validator validator : validators) {
			logger.debug("Searching ClientConstraintInfo for "
					+ validator.getClass());

			Class<? extends Validator> constraint = validator.getClass();
			ClientConstraintInfo clientConstraint = getClientConstraint(constraint);
			if (clientConstraint != null) {
				String clientValidation = clientConstraint.parseClientValidation(validator);
				clientValidations.add(clientValidation);
			}
		}
		return clientValidations;
	}

	protected List<ActiveJDBCConstraintInfo> _getClientConstraints(Class classType, String property) {
		List<ActiveJDBCConstraintInfo> clientConstriants = new ArrayList<ActiveJDBCConstraintInfo>();
		List<Validator> validators = ActiveJDBCValidatorUtil.getValidators(classType, property);
		clientConstriants.addAll(getClientConstraints(validators));
		return clientConstriants;
	}

	protected List<ActiveJDBCConstraintInfo> getClientConstraints(List<Validator> validators) {
		List<ActiveJDBCConstraintInfo> clientConstriants = new ArrayList<ActiveJDBCConstraintInfo>();
		for (Validator validator : validators) {
			logger.debug("Searching ClientConstraintInfo for " + validator.getClass());

			Class<? extends Validator> constraint = validator.getClass();
			ActiveJDBCConstraintInfo clientConstraint = getClientConstraint(constraint);
			if (clientConstraint != null) {
				clientConstriants.add(clientConstraint);
			}
		}
		return clientConstriants;
	}

}
