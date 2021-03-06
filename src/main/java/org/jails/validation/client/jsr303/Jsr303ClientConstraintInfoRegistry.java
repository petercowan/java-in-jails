package org.jails.validation.client.jsr303;

import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;
import org.jails.validation.client.ClientConstraintInfo;
import org.jails.validation.client.ClientConstraintInfoRegistry;
import org.jails.validation.client.posabsolute.PositionAbsolute;
import org.jails.validation.constraint.BeanConstraints;
import org.jails.validation.constraint.FieldMatch;
import org.jails.validation.constraint.IsDecimal;
import org.jails.validation.constraint.IsInteger;
import org.jails.validation.constraint.RequiredChecks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import javax.validation.metadata.ConstraintDescriptor;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Jsr303ClientConstraintInfoRegistry
		extends ClientConstraintInfoRegistry<Class<? extends Annotation>,Jsr303ClientConstraintInfo, ConstraintDescriptor> {
	private static Logger logger = LoggerFactory.getLogger(Jsr303ClientConstraintInfoRegistry.class);

	private static Jsr303ClientConstraintInfoRegistry instance;

	private Jsr303ClientConstraintInfoRegistry() {
        super();
        requiredConstraints = new HashSet<Class<? extends Annotation>>();
        requiredConstraints.add(NotNull.class);
        requiredConstraints.add(NotEmpty.class);
        requiredConstraints.add(NotBlank.class);

		registry = new HashMap<Class<? extends Annotation>, Jsr303ClientConstraintInfo>();
		addClientConstraint(NotNull.class, PositionAbsolute.REQUIRED);
		addClientConstraint(NotEmpty.class, PositionAbsolute.REQUIRED);
		addClientConstraint(NotBlank.class, PositionAbsolute.REQUIRED);
		addClientConstraint(CreditCardNumber.class, PositionAbsolute.CREDIT_CARD);
		addClientConstraint(Email.class, PositionAbsolute.EMAIL);
		addClientConstraint(Length.class, PositionAbsolute.MIN_SIZE + "," + PositionAbsolute.MAX_SIZE, "min", "max");
		addClientConstraint(Min.class, PositionAbsolute.MIN_VALUE, "value");
		addClientConstraint(DecimalMin.class, PositionAbsolute.MIN_VALUE, "value");
		addClientConstraint(Max.class, PositionAbsolute.MAX_VALUE, "value");
		addClientConstraint(DecimalMax.class, PositionAbsolute.MAX_VALUE, "value");
		addClientConstraint(Past.class, PositionAbsolute.PAST);
		addClientConstraint(Future.class, PositionAbsolute.FUTURE);
		addClientConstraint(Size.class, PositionAbsolute.MIN_SIZE + "," + PositionAbsolute.MAX_SIZE, "min", "max");
		addClientConstraint(Range.class, PositionAbsolute.MIN_VALUE + "," + PositionAbsolute.MAX_VALUE, "min", "max");
		addClientConstraint(FieldMatch.class, PositionAbsolute.EQUALS, "field", "matchField");
		addClientConstraint(IsInteger.class, PositionAbsolute.INTEGER);
		addClientConstraint(IsDecimal.class, PositionAbsolute.INTEGER);
		addClientConstraint(URL.class, PositionAbsolute.URL);
//		addClientConstraint(FieldMatch.class, "equals[${form.fieldMatch.id}]");
//		addClientConstraint(AssertFalse.class, "");
//		addClientConstraint(AssertTrue.class,"");
//		addClientConstraint(Pattern.class,"custom[${regexp}]","regex");
//		addClientConstraint(Digits.class,"future[now]","maxIntegerDigits","maxFractionDigits");
//		include both attributes, even if only using one in the validation field
//		addClientConstraint(StrongPassword.class,"custom[]");
		/**
		 .??[a-zA-Z]
		 .??[0-9]
		 .??[:,!,@,#,$,%,^,&,*,?,_,-,=,+,~]
		 **/
		validationConstructor = new Jsr303ClientValidationConstructor();
	}

	public static Jsr303ClientConstraintInfoRegistry getInstance() {
		if (instance == null) {
			synchronized (Jsr303ClientConstraintInfoRegistry.class) {
				instance = new Jsr303ClientConstraintInfoRegistry();
			}
		}
		return instance;
	}

	protected Jsr303ClientConstraintInfo newClientConstraint(Class<? extends Annotation> constraint, String clientFunction, String... attributeNames) {
		return new Jsr303ClientConstraintInfo(constraint, clientFunction, attributeNames);
	}

	protected List<String> getValidations(Class classType, String property) {

		List<String> clientValidations = new ArrayList<String>();
		Set<ConstraintDescriptor<?>> constraints = BeanConstraints
				.getInstance().getConstraints(classType, property);
		for (ConstraintDescriptor descriptor : constraints) {
			logger.debug("Searching ClientConstraintInfo for "
					+ descriptor.getAnnotation().annotationType());

			Class<? extends Annotation> constraint = descriptor.getAnnotation().annotationType();
			ClientConstraintInfo clientConstraint = getClientConstraint(constraint);
			if (clientConstraint != null) {
				String clientValidation = clientConstraint.parseClientValidation(descriptor);
				clientValidations.add(clientValidation);
			}
		}
		return clientValidations;
	}

	protected List<Jsr303ClientConstraintInfo> getConstraints(Class classType, String property) {
		List<Jsr303ClientConstraintInfo> clientConstriants = new ArrayList<Jsr303ClientConstraintInfo>();
        logger.info("getConstraints()");
		Set<ConstraintDescriptor<?>> constraints = BeanConstraints
				.getInstance().getConstraints(classType, property);
        logger.info("findConstraints()");
		Set<ConstraintDescriptor<?>> classConstraints = BeanConstraints
				.getInstance().findConstraints(classType, property);

        logger.info("add constraints");
		clientConstriants.addAll(getClientConstraints(constraints));
        logger.info("add class constraint");
		clientConstriants.addAll(getClientConstraints(classConstraints));
		return clientConstriants;
	}

	protected List<Jsr303ClientConstraintInfo> getClientConstraints(Set<ConstraintDescriptor<?>> constraints) {
		List<Jsr303ClientConstraintInfo> clientConstriants = new ArrayList<Jsr303ClientConstraintInfo>();
		for (ConstraintDescriptor descriptor : constraints) {
			logger.debug("Searching ClientConstraintInfo for "
					+ descriptor.getAnnotation().annotationType());

			Class<? extends Annotation> constraint = descriptor.getAnnotation().annotationType();
			Jsr303ClientConstraintInfo clientConstraint = getClientConstraint(constraint);
			if (clientConstraint != null) {
				clientConstriants.add(clientConstraint);
			}
		}
		return clientConstriants;
	}

    @Override
    protected boolean isPropertyRequired(Class classType, String property) {
        return (super.isPropertyRequired(classType, property))
                ? true
                :  hasRequiredChecks(classType, property);
    }

    protected boolean hasRequiredChecks(Class type, String paramName) {
        Set<Class<?>> constraints = BeanConstraints.getInstance()
                .getConstraintGroups(type, paramName);
        if (constraints != null) {
            for (Class<?> group : constraints) {
                logger.trace("Checking group " + group);
                if (RequiredChecks.class.equals(group)) {
                    logger.debug(paramName + " isRequired");
                    return true;
                }
            }
        }
        return false;
    }
}
