package org.jails.validation;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jails.util.AccountForm;
import org.jails.validation.constraint.FieldMatch;

import javax.validation.metadata.ConstraintDescriptor;
import java.util.Set;

public class BeanConstraintsTest
		extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public BeanConstraintsTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(BeanConstraintsTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testFindConstraints() {
		Set<ConstraintDescriptor<?>> constraints = BeanConstraints.getInstance()
				.findConstraints(AccountForm.class, "confirmPassword");
		assertNotNull(constraints);
		assertTrue(constraints.size() == 1);
		assertEquals(constraints.toArray(new ConstraintDescriptor[1])[0].getAnnotation().annotationType(), FieldMatch.class);
	}
}

