package org.jails.validation;

import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.ResourceBundleLocator;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Locale;
import java.util.ResourceBundle;

public class ValidatorInstance {
	private static ValidatorInstance instance;

	private Validator validator;


	private ValidatorInstance() {
		ValidatorFactory validatorFactory = Validation
				.byProvider(HibernateValidator.class)
				.configure()
				.messageInterpolator(
						new ResourceBundleMessageInterpolator(
								new ResourceBundleLocator() {
									public ResourceBundle getResourceBundle(Locale locale) {
										return ResourceBundle.getBundle("validation-messages");
									}
								}))
				.buildValidatorFactory();

		validator = validatorFactory.getValidator();
	}

	public static synchronized ValidatorInstance getInstance() {
		if (instance == null) instance = new ValidatorInstance();
		return instance;
	}

	public Validator getValidator() {
		return validator;
	}
}
