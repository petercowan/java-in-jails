package org.jails.validation.client;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;
import org.jails.validation.BeanConstraints;
import org.jails.validation.constraint.FieldMatch;
import org.jails.validation.constraint.StrongPassword;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.*;
import javax.validation.metadata.ConstraintDescriptor;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClientConstraintInfoRegistry {
	private static Logger logger = LoggerFactory.getLogger(ClientConstraintInfoRegistry.class);

	private static ClientConstraintInfoRegistry instance;
	private Map<Class<? extends Annotation>, ClientConstraintInfo> registry;
	private ClientValidationConstructor validationConstructor;

	private ClientConstraintInfoRegistry() {
		registry = new HashMap<Class<? extends Annotation>, ClientConstraintInfo>();
		addClientConstraint(NotNull.class, "required");
		addClientConstraint(NotEmpty.class, "required");
		addClientConstraint(NotNull.class, "required");
		addClientConstraint(FieldMatch.class, "equals[${form.fieldMatch.id}]");
		//addClientConstraint(AssertFalse.class, "");
		//addClientConstraint(AssertTrue.class,"");
		//addClientConstraint(CreditCardNumber.class, "");
		addClientConstraint(Email.class, "custom[email]");
		addClientConstraint(Length.class, "minSize[${min}],maxSize[${max}]", "min", "max");
		addClientConstraint(Min.class,"min[${value}]","value");
		addClientConstraint(DecimalMin.class,"min[${value}]","value");
		addClientConstraint(Max.class,"max[${value}]","value");
		addClientConstraint(DecimalMax.class,"max[${value}]","value");
		addClientConstraint(Min.class,"min[${value}]","value");
		addClientConstraint(DecimalMin.class,"min[${value}]","value");
		addClientConstraint(Pattern.class,"custom[${regex}]","regex");
		addClientConstraint(Past.class,"past[now]");
		addClientConstraint(Future.class,"future[now]");
		addClientConstraint(Size.class, "minSize[${min}],maxSize[${max}]", "min", "max");
		//addClientConstraint(Digits.class,"future[now]","maxIntegerDigits","maxFractionDigits");
		addClientConstraint(Range.class,"min[${min}],max[${max}]", "min","max");
		addClientConstraint(StrongPassword.class,"custom[]");
		/**
		.??[a-zA-Z]
		.??[0-9]
		.??[:,!,@,#,$,%,^,&,*,?,_,-,=,+,~]
		 **/
		addClientConstraint(URL.class,"custom[url]");
		validationConstructor = new PositionRelativeValidationConstructor();
	}

	public static ClientConstraintInfoRegistry getInstance() {
		if (instance == null) {
			synchronized (ClientConstraintInfoRegistry.class) {
				instance = new ClientConstraintInfoRegistry();
			}
		}
		return instance;
	}

	public void addClientConstraint(Class<? extends Annotation> constraint,
									String clientFunction, String... attributeNames) {
		ClientConstraintInfo constriaintInfo = registry.get(constraint);
		if (constriaintInfo == null) {
			constriaintInfo = new PositionRelativeConstraintInfo(constraint, clientFunction, attributeNames);
			registry.put(constraint, constriaintInfo);
		}
	}

	public void addClientConstraint(Class<? extends Annotation> constraint,
									String clientFunction) {
		addClientConstraint(constraint, clientFunction, null);
	}

	public ClientConstraintInfo getClientConstraint(Class<? extends Annotation> constraint) {
		return registry.get(constraint);
	}

	public List<String> getClientValidations(Class classType, String property) {
		List<String> clientValidations = new ArrayList<String>();
		Set<ConstraintDescriptor<?>> constraints = BeanConstraints
				.getInstance().getConstraints(classType, property);
		for (ConstraintDescriptor descriptor : constraints) {
			logger.warn("Searching ClientConstraintInfo for "
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

	public List<ClientConstraintInfo> getClientConstraints(Class classType, String property) {
		List<ClientConstraintInfo> clientConstriants = new ArrayList<ClientConstraintInfo>();
		Set<ConstraintDescriptor<?>> constraints = BeanConstraints
				.getInstance().getConstraints(classType, property);
		for (ConstraintDescriptor descriptor : constraints) {
			logger.warn("Searching ClientConstraintInfo for "
					+ descriptor.getAnnotation().annotationType());

			Class<? extends Annotation> constraint = descriptor.getAnnotation().annotationType();
			ClientConstraintInfo clientConstraint = getClientConstraint(constraint);
			if (clientConstraint != null) {
				clientConstriants.add(clientConstraint);
			}
		}
		return clientConstriants;
	}

	public ClientValidationConstructor getValidationConstructor() {
		return validationConstructor;
	}
}
