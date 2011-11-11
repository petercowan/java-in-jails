package org.jails.form;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jails.property.MappingBean;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SimpleFormTest 
		extends TestCase
	{
		/**
		 * Create the test case
		 *
		 * @param testName name of the test case
		 */
		public SimpleFormTest( String testName )
		{
			super( testName );
		}

		/**
		 * @return the suite of tests being tested
		 */
		public static Test suite()
		{
			return new TestSuite( SimpleFormTest.class );
		}

		/**
		 * Rigourous Test :-)
		 */
		public void testBindSimpleForm()
		{
			List<MappingBean> objects = new ArrayList();
			MappingBean bean = new MappingBean();
			bean.setInteger(346);
			objects.add(bean);
			MappingBean[] array = (MappingBean[]) Array.newInstance(MappingBean.class, 1);

			int index = 0;
			for (MappingBean o : objects) {
				array[index++] = o;
			}

			SimpleForm.bindTo(array)
					.identifyBy("integer");

			assertTrue( true );
		}
	}
