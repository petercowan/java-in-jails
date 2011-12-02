package org.jails.form;

import org.jails.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <code>SimpleForm</code> is a basic container class that that holds form meta data to be
 * passed to the client. This data may contain:
 *
 * <ul>
 * <li> One or more Objects that have been bound to the form, providing default values for form(s) fields
 * that match the objects getter methods</li>
 * <li> A Class<T> type that contains JSR 303 annotation to be used for client side validation.</li>
 * <li> A name for the form</li>
 * <li> An field name to idenityfy the bound Object(s) by</li>
 * <li> A a list of error messages mapped to the field names</li>
 * </ul>
 *
 * <pre>
 * SimpleForm does not act on any of this data.
 *
 * Basic usage:
 * <code>
 *     //if no object is yet available (ie: before creation)
 *     SimpleForm.validateAs(classType)
 *               .identifyBy(idField)
 *               .named("formName")
 *               .inRequest(request);
 *
 *     //with multiple repeitions
 *     SimpleForm.validateAs(classType)
 *               .identifyBy(idField)
 *               .named("formName")
 *               .repeat(3)
 *               .inRequest(request);
 *
 *     //if object is available
 *     Object object = //load Object();
 *     SimpleForm.bindTo(object)
 *               .identifyBy(idField)
 *               .named("formName")
 *               .inRequest(request);
 *
 *    //with multiple Objects
 *     Object object1 = //load Object();
 *     Object object2 = //...;
 *     Object object2 = //...;
 *
 *     SimpleForm.bindTo(object1, object2, object3)
 *               .identifyBy(idField)
 *               .named("formName")
 *               .inRequest(request);
 * </code>
 *
 * When binding to objects, validateAs is set the the Class of those objects.
 * If named is not called, then the name of the form is set as the simple name of the validating class.
 * ex: SimpleForm.getClass().getSimpleName() = "SimpleForm", form name = "simpleForm"
 *
 * If binded Objects or validateAs Class is annotated with IdentifyBy, then indentify will be set
 * automatically, meaning in most cases you will need to do no more than:
 *
 * <code>
 *     SimpleForm.validateAs(classType)
 *               .inRequest(request);
 *
 *     SimpleForm.bindTo(object)
 *               .inRequest(request);
 * </code>
 * </pre>
 *
 *
 * @see org.jails.property.IdentifyBy
 * @param <T> type of object or ParameterizedType of class that is bound to this form
 */
public abstract class SimpleForm<T> {
	private static Logger logger = LoggerFactory.getLogger(SimpleForm.class);

	protected SimpleForm() {
	}

	/*
	 * Use classType for clientSide validation.
	 */
	public static <T> SimpleForm<T> validateAs(Class<T> classType) {
		return new SimpleFormBuilder<T>(classType);
	}

	/*
	 * Bind to objects... and use Class<T> for clientSide validation.
	 */
	public static <T> SimpleForm<T> bindTo(T... objects) {
		return new SimpleFormBuilder<T>(objects);
	}

	/*
	 * Retrieve form from request, using name and classType.
	 */
	public static <T> SimpleForm<T> fromRequest(HttpServletRequest request, Class<T> classType, String name) {
		String simpleFormParam = "_" + name + "_form";
		logger.info("Getting SimpleForm: " + simpleFormParam);
		SimpleForm<T> simpleForm = (SimpleForm) request.getAttribute(simpleFormParam);
		logger.info("Loaded form " + simpleForm);
		return simpleForm;
	}

	/*
	 * Retrieve form from request, deriving name from classType.
	 */
	public static <T> SimpleForm<T> fromRequest(HttpServletRequest request, Class<T> classType) {
		return fromRequest(request, classType, Strings.toCamelCase(classType.getSimpleName()));
	}

	/*
	 * Set the forms name
	 */
	public abstract SimpleForm<T> named(String name);

	/*
	 * Set the forms identity field
	 */
	public abstract SimpleForm<T> identifyBy(String identityField);

	/*
	 * Set how many times the entire form should be repeated
	 */
	public abstract SimpleForm<T> repeat(Integer repeatCount);

	/*
	 * Set the form as a request attribute
	 */
	public abstract SimpleForm<T> inRequest(HttpServletRequest request);

	public abstract String getName();

	public abstract Class getClassType();

	public abstract T getObject(int index);

	public abstract Object getObject();

	public abstract T[] getObjects();

	public abstract Boolean isBound();

	public abstract String getIdentity(int index);

	public abstract String getIdentity();

	public abstract String[] getIdentities();

	public abstract Integer getTimesToRepeat();

	public abstract boolean isRepeatable();

	public abstract boolean isFieldRequired(String paramName);

	public abstract void setErrors(Map<String, List<String>> errorFieldMap);

	public abstract void addErrors(Map<String, List<String>> errorFieldMap);

	public abstract void addError(String fieldName, String errorMessage);

	public abstract void addError(String fieldName, String errorMessage, int index);

	public abstract boolean hasError();

	public abstract boolean fieldHasError(String paramName);

	public abstract boolean fieldHasError(String paramName, int index);

	public abstract String getFieldError(String paramName, String label);

	public abstract String getFieldError(String paramName, String label, int index);

	public abstract String getAction();
}
