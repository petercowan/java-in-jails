package org.jails.validation.client;

import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;
import org.jails.property.ReflectionUtil;
import org.jails.property.parser.PropertyParser;
import org.jails.property.parser.SimplePropertyParser;
import org.jails.validation.BeanConstraints;
import org.jails.validation.constraint.FieldMatch;
import org.jails.validation.constraint.IsDecimal;
import org.jails.validation.constraint.IsInteger;
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
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClientConstraintInfoRegistry {
	private static Logger logger = LoggerFactory.getLogger(ClientConstraintInfoRegistry.class);

	private static ClientConstraintInfoRegistry instance;
	private Map<Class<? extends Annotation>, ClientConstraintInfo> registry;
	private ClientValidationConstructor validationConstructor;
	protected PropertyParser propertyParser = new SimplePropertyParser();

	private ClientConstraintInfoRegistry() {
		registry = new HashMap<Class<? extends Annotation>, ClientConstraintInfo>();
		addClientConstraint(NotNull.class, "required");
		addClientConstraint(NotEmpty.class, "required");
		addClientConstraint(NotBlank.class, "required");
//		addClientConstraint(FieldMatch.class, "equals[${form.fieldMatch.id}]");
		//addClientConstraint(AssertFalse.class, "");
		//addClientConstraint(AssertTrue.class,"");
		addClientConstraint(CreditCardNumber.class, "creditCard");
		addClientConstraint(Email.class, "custom[email]");
		addClientConstraint(Length.class, "minSize[${min}],maxSize[${max}]", "min", "max");
		addClientConstraint(Min.class, "min[${value}]", "value");
		addClientConstraint(DecimalMin.class, "min[${value}]", "value");
		addClientConstraint(Max.class, "max[${value}]", "value");
		addClientConstraint(DecimalMax.class, "max[${value}]", "value");
		addClientConstraint(Min.class, "min[${value}]", "value");
		addClientConstraint(DecimalMin.class, "min[${value}]", "value");
		//addClientConstraint(Pattern.class,"custom[${regexp}]","regex");
		addClientConstraint(Past.class, "past[now]");
		addClientConstraint(Future.class, "future[now]");
		addClientConstraint(Size.class, "minSize[${min}],maxSize[${max}]", "min", "max");
		//addClientConstraint(Digits.class,"future[now]","maxIntegerDigits","maxFractionDigits");
		addClientConstraint(Range.class, "min[${min}],max[${max}]", "min", "max");
		//include both attributes, even if only using one in the validation field
		addClientConstraint(FieldMatch.class, "equals[${field}]", "field", "matchField");
		//addClientConstraint(StrongPassword.class,"custom[]");
		addClientConstraint(IsInteger.class, "custom[integer]");
		addClientConstraint(IsDecimal.class, "custom[number]");
		/**
		 .??[a-zA-Z]
		 .??[0-9]
		 .??[:,!,@,#,$,%,^,&,*,?,_,-,=,+,~]
		 **/
		addClientConstraint(URL.class, "custom[url]");
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
		if (propertyParser.hasNestedProperty(property)) {
			Class nestedClass = ReflectionUtil.getPropertyType(classType, property);
			String nestedProperty = (property.lastIndexOf(".") > 0)
					? property.substring(property.lastIndexOf(".") + 1)
					: property;

			return _getClientValidations(nestedClass, nestedProperty);
		} else {
			return _getClientValidations(classType, property);
		}
	}

	protected List<String> _getClientValidations(Class classType, String property) {

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

	public List<ClientConstraintInfo> getClientConstraints(Class classType, String property) {
		if (propertyParser.hasNestedProperty(property)) {
			Class nestedClass = ReflectionUtil.getPropertyType(classType, property);
			String nestedProperty = (property.lastIndexOf(".") > 0)
					? property.substring(property.lastIndexOf(".") + 1)
					: property;

			return _getClientConstraints(nestedClass, nestedProperty);
		} else {
			return _getClientConstraints(classType, property);
		}
	}

	protected List<ClientConstraintInfo> _getClientConstraints(Class classType, String property) {
		List<ClientConstraintInfo> clientConstriants = new ArrayList<ClientConstraintInfo>();
		Set<ConstraintDescriptor<?>> constraints = BeanConstraints
				.getInstance().getConstraints(classType, property);
		Set<ConstraintDescriptor<?>> classConstraints = BeanConstraints
				.getInstance().findConstraints(classType, property);

		clientConstriants.addAll(getClientConstraints(constraints));
		clientConstriants.addAll(getClientConstraints(classConstraints));
		return clientConstriants;
	}

	protected List<ClientConstraintInfo> getClientConstraints(Set<ConstraintDescriptor<?>> constraints) {
		List<ClientConstraintInfo> clientConstriants = new ArrayList<ClientConstraintInfo>();
		for (ConstraintDescriptor descriptor : constraints) {
			logger.debug("Searching ClientConstraintInfo for "
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
