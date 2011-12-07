package org.jails.validation;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jails.property.SimpleMapper;
import org.jails.util.AccountForm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleValidatorTest
		extends TestCase {
	private static String PASSWORD_ERROR = "There is a problem with your password, your password must be at least 7 characters, contain at least one letter, one number and one of the following special symbols (:,!,@,#,$,%,^,&,*,?,_,-,=,+,~,`,[,]).  Blank spaces and other symbols are not allowed.";
	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public SimpleValidatorTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(SimpleValidatorTest.class);
	}

	public void testInvalidParameterMap() {
		SimpleValidator validator = new SimpleValidator();
		Map<String, List<String>> errors = validator.validate(AccountForm.class, getInvalidParameterMap());
		assertNotNull(errors);

		System.out.println("\nTestForm Errors---------------------------------");
		for (String field : errors.keySet()) {
			System.out.println(field + ": " + errors.get(field));
		}
		System.out.println("\n------------------------------------------------");
		assertEquals(errors.get("balance").get(0), "must be greater than or equal to 1000.00");
		assertEquals(errors.get("accountName").get(0), "may only contain letters and numbers.");
		for (String error : errors.get("confirmPassword")) {
			System.out.println("confirmPassword error:" + error + ":");
			System.out.println(error.equals(""));
			System.out.println(error.equals(PASSWORD_ERROR));
			assertTrue("confirmPasswordTest", error.equals("") || error.equals(PASSWORD_ERROR));
		}
		assertEquals(errors.get("name").get(0), "size must be between 2 and 25");
		assertEquals(errors.get("creditCardNumber").get(0), "invalid credit card number");
		assertEquals(errors.get("birthday").get(0), "must be in the past");
		System.out.println("password errors: " + errors.get("password").size());
		for (String error : errors.get("password")) {
			System.out.println("password");
			System.out.println(error.equals(""));
			System.out.println(error.equals(PASSWORD_ERROR));
			assertTrue("passwordTest", error.equals("Passwords must match.") || error.equals(PASSWORD_ERROR));
		}
		assertEquals(errors.get("age").get(0), "must be greater than or equal to 18");
		assertEquals(errors.get("address.street").get(0), "size must be between 2 and 125");
		assertEquals(errors.get("address.city").get(0), "size must be between 2 and 75");
		assertEquals(errors.get("address.state").get(0), "size must be between 2 and 2");
		assertEquals(errors.get("address.zip").get(0), "size must be between 5 and 15");
		assertEquals(errors.get("address.country").get(0), "size must be between 2 and 2");
	}

	private Map<String, String[]> getInvalidParameterMap() {
		Map<String, String[]> params = new HashMap<String, String[]>();

		params.put("accountForm.name", new String[]{"P"});
		params.put("accountForm.accountName", new String[]{"petercowan$%^"});
		params.put("accountForm.password", new String[]{"123456"});
		params.put("accountForm.confirmPassword", new String[]{"654321"});
		params.put("accountForm.creditCardNumber", new String[]{"4111 2222 3333 4444"});
		params.put("accountForm.balance", new String[]{"0"});
		params.put("accountForm.age", new String[]{"0"});
		params.put("accountForm.birthday", new String[]{"11/12/2013"});
		params.put("accountForm.address.street", new String[]{"1"});
		params.put("accountForm.address.city", new String[]{"P"});
		params.put("accountForm.address.state", new String[]{"Oregon"});
		params.put("accountForm.address.zip", new String[]{"12345-54321-67890"});
		params.put("accountForm.address.country", new String[]{"USA"});

		return params;
	}

	public void testValidParameterMap() {
		SimpleValidator validator = new SimpleValidator();
		Map<String, List<String>> errors = validator.validate(AccountForm.class, getValidParameterMap());
		assertNull(errors);
	}

	public void testValidParameters() {
		SimpleValidator validator = new SimpleValidator();
		Map<String, List<String>> errors = validator.validate(AccountForm.class, getValidParameterMap());
		assertNull(errors);
		AccountForm account = validator.getMapper().toObject(AccountForm.class, getValidParameterMap());
		assertEquals(account.getName(), "Peter Cowan");
		assertEquals(account.getAccountName(), "petercowan");
		assertEquals(account.getPassword(), "123ABC$%^");
		assertEquals(account.getConfirmPassword(), "123ABC$%^");
		assertEquals(account.getCreditCardNumber(), "4111 1111 1111 1111");
		assertEquals(account.getBalance(), 5000.0);
		assertEquals(new Integer(account.getAge()), new Integer(19));
//		assertEquals(account.getBirthday(), );
		assertNotNull(account.getAddress());
		assertEquals(account.getAddress().getStreet(), "123 Main St");
		assertEquals(account.getAddress().getCity(), "Portland");
		assertEquals(account.getAddress().getState(), "OR");
		assertEquals(account.getAddress().getZip(), "12345");
		assertEquals(account.getAddress().getCountry(), "US");

	}

	private Map<String, String[]> getValidParameterMap() {
		Map<String, String[]> params = new HashMap<String, String[]>();

		params.put("accountForm.name", new String[]{"Peter Cowan"});
		params.put("accountForm.accountName", new String[]{"petercowan"});
		params.put("accountForm.password", new String[]{"123ABC$%^"});
		params.put("accountForm.confirmPassword", new String[]{"123ABC$%^"});
		params.put("accountForm.creditCardNumber", new String[]{"4111 1111 1111 1111"});
		params.put("accountForm.balance", new String[]{"5000"});
		params.put("accountForm.age", new String[]{"19"});
		params.put("accountForm.birthday", new String[]{"11/12/2010"});
		params.put("accountForm.address.street", new String[]{"123 Main St"});
		params.put("accountForm.address.city", new String[]{"Portland"});
		params.put("accountForm.address.state", new String[]{"OR"});
		params.put("accountForm.address.zip", new String[]{"12345"});
		params.put("accountForm.address.country", new String[]{"US"});

		return params;
	}

	public void testIncorrectParameterMap() {
		SimpleValidator validator = new SimpleValidator();
		Map<String, List<String>> errors = validator.validate(AccountForm.class, getIncorrectParameterMap());
		assertNull(errors);

		System.out.println("\n------------------------------------------------");
		AccountForm formTest = new SimpleMapper().toObject(AccountForm.class, getIncorrectParameterMap());
		System.out.println("formTest.getLimit()" + formTest.getBalance());
		System.out.println("formTest.getSize()" + formTest.getAge());
		System.out.println("formTest.getDateCreated()" + formTest.getBirthday());
		assertEquals(formTest.getBalance(), 5000.0);
		assertEquals(formTest.getAge(), new Integer(19));
		assertEquals(formTest.getBirthday(), null);
	}

	private Map<String, String[]> getIncorrectParameterMap() {
		Map<String, String[]> params = new HashMap<String, String[]>();

		params.put("accountForm.name", new String[]{"Peter Cowan"});
		params.put("accountForm.accountName", new String[]{"petercowan"});
		params.put("accountForm.password", new String[]{"123ABC$%^"});
		params.put("accountForm.confirmPassword", new String[]{"123ABC$%^"});
		params.put("accountForm.creditCardNumber", new String[]{"4111 1111 1111 1111"});
		params.put("accountForm.balance", new String[]{"acb5000abc"});
		params.put("accountForm.age", new String[]{"19acba"});
		params.put("accountForm.birthday", new String[]{"11/12/2010acbca"});
		params.put("accountForm.address.street", new String[]{"123 Main St"});
		params.put("accountForm.address.city", new String[]{"Portland"});
		params.put("accountForm.address.state", new String[]{"OR"});
		params.put("accountForm.address.zip", new String[]{"12345"});
		params.put("accountForm.address.country", new String[]{"US"});

		return params;
	}

}

