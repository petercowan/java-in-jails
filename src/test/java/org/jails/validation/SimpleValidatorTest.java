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
		extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public SimpleValidatorTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( SimpleValidatorTest.class );
    }

    public void testInvalidParameterMap()
    {
		SimpleValidator validator = new SimpleValidator();
		try {
			validator.validate(AccountForm.class, getInvalidParameterMap());
		} catch (ValidationException e) {
			System.out.println("\nTestForm Errors---------------------------------");
			for (String field : e.getErrorFields(0).keySet()) {
				System.out.println(field + ": " + e.getErrorFields(0).get(field));
			}
			System.out.println("\n------------------------------------------------");
			Map<String, List<String>> errorFields =  e.getErrorFields(0);
			assertEquals(errorFields.get("balance").get(0), "must be greater than or equal to 1000.00");
			assertEquals(errorFields.get("accountName").get(0), "may only contain letters and numbers.");
			for (String error : errorFields.get("confirmPassword")) {
				assertTrue("confirmPasswordTest", error.equals("") ||
				error.equals("There is a problem with your password, your password must be at least 7 characters, contain at least one letter, one number and one of the following special symbols (:,!,@,#,$,%,^,&,*,?,_,-,=,+,~,`,[,]).  Blank spaces and other symbols are not allowed."));
			}
			assertEquals(errorFields.get("name").get(0), "size must be between 2 and 25");
			assertEquals(errorFields.get("creditCardNumber").get(0), "invalid credit card number");
			assertEquals(errorFields.get("birthday").get(0), "must be in the past");
			for (String error : errorFields.get("password")) {
				assertTrue("passwordTest", error.equals("Passwords must match.") ||
				error.equals("There is a problem with your password, your password must be at least 7 characters, contain at least one letter, one number and one of the following special symbols (:,!,@,#,$,%,^,&,*,?,_,-,=,+,~,`,[,]).  Blank spaces and other symbols are not allowed."));
			}
			assertEquals(errorFields.get("age").get(0), "must be greater than or equal to 18");
		}
    }

	private Map<String, String[]> getInvalidParameterMap() {
		Map<String, String[]> params = new HashMap<String, String[]>();

		params.put("accountForm.name",new String[]{"P"});
		params.put("accountForm.accountName",new String[]{"petercowan$%^"});
		params.put("accountForm.password",new String[]{"123456"});
		params.put("accountForm.confirmPassword",new String[]{"654321"});
		params.put("accountForm.creditCardNumber",new String[]{"4111 2222 3333 4444"});
		params.put("accountForm.balance",new String[]{"0"});
		params.put("accountForm.age",new String[]{"0"});
		params.put("accountForm.birthday",new String[]{"11/12/2013"});

		return params;
	}

	public void testValidParameterMap()
	{
		SimpleValidator validator = new SimpleValidator();
		try {
			validator.validate(AccountForm.class, getValidParameterMap());
			assertTrue(true);
		} catch (ValidationException e) {
			assertTrue(false);
		}
	}

	private Map<String, String[]> getValidParameterMap() {
		Map<String, String[]> params = new HashMap<String, String[]>();

		params.put("accountForm.name",new String[]{"Peter Cowan"});
		params.put("accountForm.accountName",new String[]{"petercowan"});
		params.put("accountForm.password",new String[]{"123ABC$%^"});
		params.put("accountForm.confirmPassword",new String[]{"123ABC$%^"});
		params.put("accountForm.creditCardNumber",new String[]{"4111 1111 1111 1111"});
		params.put("accountForm.balance",new String[]{"5000"});
		params.put("accountForm.age",new String[]{"19"});
		params.put("accountForm.birthday",new String[]{"11/12/2010"});

		return params;
	}

	public void testIncorrectParameterMap()
	{
		SimpleValidator validator = new SimpleValidator();
		try {
			validator.validate(AccountForm.class, getIncorrectParameterMap());
			assertTrue(true);
		} catch (ValidationException e) {
			System.out.println("\nTestForm Errors---------------------------------");
			for (String field : e.getErrorFields(0).keySet()) {
				System.out.println(field + ": " + e.getErrorFields(0).get(field));
			}
			System.out.println("\n------------------------------------------------");
			AccountForm formTest = new SimpleMapper().toObject(AccountForm.class, getIncorrectParameterMap());
			System.out.println("formTest.getLimit()" + formTest.getBalance());
			System.out.println("formTest.getSize()" + formTest.getAge());
			System.out.println("formTest.getDateCreated()" + formTest.getBirthday());
			assertEquals(formTest.getBalance(), 5000.0);
			assertEquals(formTest.getAge(), new Integer(19));
			assertEquals(formTest.getBirthday(), null);

			//Map<String, List<String>> errorFields =  e.getErrorFields(0);
			//assertEquals(errorFields.get("limit").get(0), "may not be null");
			//assertEquals(errorFields.get("birthday").get(0), "may not be null");
		}
	}

	private Map<String, String[]> getIncorrectParameterMap() {
		Map<String, String[]> params = new HashMap<String, String[]>();

		params.put("accountForm.name",new String[]{"Peter Cowan"});
		params.put("accountForm.accountName",new String[]{"petercowan"});
		params.put("accountForm.password",new String[]{"123ABC$%^"});
		params.put("accountForm.confirmPassword",new String[]{"123ABC$%^"});
		params.put("accountForm.creditCardNumber",new String[]{"4111 1111 1111 1111"});
		params.put("accountForm.balance",new String[]{"acb5000abc"});
		params.put("accountForm.age",new String[]{"19acba"});
		params.put("accountForm.birthday",new String[]{"11/12/2010acbca"});

		return params;
	}

}

