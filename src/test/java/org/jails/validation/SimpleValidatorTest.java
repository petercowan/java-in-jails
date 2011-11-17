package org.jails.validation;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jails.property.SimpleMapper;
import org.jails.util.TestForm;

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
			validator.validate(TestForm.class, getInvalidParameterMap());
		} catch (ValidationException e) {
			System.out.println("\nTestForm Errors---------------------------------");
			for (String field : e.getErrorFields(0).keySet()) {
				System.out.println(field + ": " + e.getErrorFields(0).get(field));
			}
			System.out.println("\n------------------------------------------------");
			Map<String, List<String>> errorFields =  e.getErrorFields(0);
			assertEquals(errorFields.get("limit").get(0), "must be greater than or equal to 1000.00");
			assertEquals(errorFields.get("accountName").get(0), "may only contain letters and numbers.");
			for (String error : errorFields.get("confirmPassword")) {
				assertTrue("confirmPasswordTest", error.equals("") ||
				error.equals("There is a problem with your password, your password must be at least 7 characters, contain at least one letter, one number and one of the following special symbols (:,!,@,#,$,%,^,&,*,?,_,-,=,+,~,`,[,]).  Blank spaces and other symbols are not allowed."));
			}
			assertEquals(errorFields.get("name").get(0), "size must be between 2 and 25");
			assertEquals(errorFields.get("creditCardNumber").get(0), "invalid credit card number");
			assertEquals(errorFields.get("dateCreated").get(0), "must be in the past");
			for (String error : errorFields.get("password")) {
				assertTrue("passwordTest", error.equals("Passwords must match.") ||
				error.equals("There is a problem with your password, your password must be at least 7 characters, contain at least one letter, one number and one of the following special symbols (:,!,@,#,$,%,^,&,*,?,_,-,=,+,~,`,[,]).  Blank spaces and other symbols are not allowed."));
			}
			assertEquals(errorFields.get("size").get(0), "must be greater than or equal to 3");
		}
    }

	private Map<String, String[]> getInvalidParameterMap() {
		Map<String, String[]> params = new HashMap<String, String[]>();

		params.put("testForm.name",new String[]{"P"});
		params.put("testForm.accountName",new String[]{"petercowan$%^"});
		params.put("testForm.password",new String[]{"123456"});
		params.put("testForm.confirmPassword",new String[]{"654321"});
		params.put("testForm.creditCardNumber",new String[]{"4111 2222 3333 4444"});
		params.put("testForm.limit",new String[]{"0"});
		params.put("testForm.size",new String[]{"0"});
		params.put("testForm.dateCreated",new String[]{"11/12/2013"});

		return params;
	}

	public void testValidParameterMap()
	{
		SimpleValidator validator = new SimpleValidator();
		try {
			validator.validate(TestForm.class, getValidParameterMap());
			assertTrue(true);
		} catch (ValidationException e) {
			assertTrue(false);
		}
	}

	private Map<String, String[]> getValidParameterMap() {
		Map<String, String[]> params = new HashMap<String, String[]>();

		params.put("testForm.name",new String[]{"Peter Cowan"});
		params.put("testForm.accountName",new String[]{"petercowan"});
		params.put("testForm.password",new String[]{"123ABC$%^"});
		params.put("testForm.confirmPassword",new String[]{"123ABC$%^"});
		params.put("testForm.creditCardNumber",new String[]{"4111 1111 1111 1111"});
		params.put("testForm.limit",new String[]{"5000"});
		params.put("testForm.size",new String[]{"15"});
		params.put("testForm.dateCreated",new String[]{"11/12/2010"});

		return params;
	}

	public void testIncorrectParameterMap()
	{
		SimpleValidator validator = new SimpleValidator();
		try {
			validator.validate(TestForm.class, getIncorrectParameterMap());
			assertTrue(true);
		} catch (ValidationException e) {
			System.out.println("\nTestForm Errors---------------------------------");
			for (String field : e.getErrorFields(0).keySet()) {
				System.out.println(field + ": " + e.getErrorFields(0).get(field));
			}
			System.out.println("\n------------------------------------------------");
			TestForm formTest = new SimpleMapper().toObject(TestForm.class, getIncorrectParameterMap());
			System.out.println("formTest.getLimit()" + formTest.getLimit());
			System.out.println("formTest.getSize()" + formTest.getSize());
			System.out.println("formTest.getDateCreated()" + formTest.getDateCreated());
			assertEquals(formTest.getLimit(), 5000.0);
			assertEquals(formTest.getSize(), new Integer(15));
			assertEquals(formTest.getDateCreated(), null);

			//Map<String, List<String>> errorFields =  e.getErrorFields(0);
			//assertEquals(errorFields.get("limit").get(0), "may not be null");
			//assertEquals(errorFields.get("dateCreated").get(0), "may not be null");
		}
	}

	private Map<String, String[]> getIncorrectParameterMap() {
		Map<String, String[]> params = new HashMap<String, String[]>();

		params.put("testForm.name",new String[]{"Peter Cowan"});
		params.put("testForm.accountName",new String[]{"petercowan"});
		params.put("testForm.password",new String[]{"123ABC$%^"});
		params.put("testForm.confirmPassword",new String[]{"123ABC$%^"});
		params.put("testForm.creditCardNumber",new String[]{"4111 1111 1111 1111"});
		params.put("testForm.limit",new String[]{"acb5000abc"});
		params.put("testForm.size",new String[]{"15acba"});
		params.put("testForm.dateCreated",new String[]{"11/12/2010acbca"});

		return params;
	}

}

