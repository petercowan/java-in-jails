package org.jails.property;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.lang.annotation.Annotation;

public class ReflectionUtilTest
		extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public ReflectionUtilTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(ReflectionUtilTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testReflectionUtil() {
		Annotation a1 = ReflectionUtil.getClassAnnotation(AcceptsNestedAttributes.class, MappingBean.class);
		Annotation a2 = ReflectionUtil.getMethodAnnotation(AcceptsNestedAttributes.class, MappingBean.class,
				"getMappingBean", null);
		Annotation a3 = ReflectionUtil.getFieldAnnotation(AcceptsNestedAttributes.class, MappingBean.class, "mappingBean");

		/**
		for (Annotation a : MappingBean.class.getAnnotations()) {
			System.out.println("Class Annotation: " + a.annotationType().getSimpleName());
		}

		try {
			for (Annotation a : MappingBean.class.getMethod("getMappingBean", null).getAnnotations()) {
				System.out.println("Method Annotation: " + a.annotationType().getSimpleName());
			}
			Field f = MappingBean.class.getDeclaredField("mappingBean");
			f.setAccessible(true);
			for (Annotation a :  f.getAnnotations()) {
				System.out.println("Field Annotation: " + a.annotationType().getSimpleName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//for (Field f : MappingBean.class.getDeclaredFields()) {
		//	System.out.println("Field: " + f.getName());
		//}


		/**
		assertNotNull(a1);
		assertNotNull(a2);
		assertNotNull(a3);
		assertEquals(a1, AcceptsNestedAttributes.class);
		assertEquals(a2, AcceptsNestedAttributes.class);
		assertEquals(a3, AcceptsNestedAttributes.class);
		   **/
	}
}
