package org.jails.property;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jails.property.handler.PropertyHandler;
import org.jails.property.handler.SimplePropertyHandler;
import org.jails.property.parser.PropertyParser;
import org.jails.property.parser.SimplePropertyParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MapperTest
		extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public MapperTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(MapperTest.class);
	}

	private void xMapToBean() {
		PropertyParser parser = new SimplePropertyParser();
		PropertyHandler handler = new SimplePropertyHandler();
		Mapper beanMapper = new Mapper(parser, handler);
		Map<String, String[]> params = getParameters(null, null);
		MappingBean bean = new MappingBean();
		beanMapper.toExistingObject(params, bean);

		assertBeanValues(bean);
	}

	public void testMapListToBean() {
		PropertyParser parser = new SimplePropertyParser();
		PropertyHandler handler = new SimplePropertyHandler();
		Mapper beanMapper = new Mapper(parser, handler);
		Map<String, String[]> paramMap = new LinkedHashMap<String, String[]>();
		for (int i = 0; i < 2; i++) {
			paramMap.putAll(getParameters(i, null));
			System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXindex: " + i);
		}

		List<MappingBean> beans = new ArrayList<MappingBean>();
		beans.add(new MappingBean());
		beans.add(new MappingBean());
		beanMapper.toExistingList(paramMap, beans);

		for (MappingBean bean : beans) {
			assertBeanValues(bean);
		}
	}

	private void assertBeanValues(MappingBean bean) {
		assertNotNull(bean.getBooleanProperty());
		System.out.println(bean.getBooleanProperty());
		assertTrue("bool_prop", bean.getBooleanProperty());
		//assertTrue("date_prop", bean.getDate());
		assertTrue("float_prop", new Float(2.45).equals(bean.getFloatProperty()));
		assertTrue("int_prop", new Integer(123).equals(bean.getInteger()));
		assertTrue("string_prop", "Hello".equals(bean.getStringProperty()));

		assertNotNull(bean.getMappingBean());
		assertNotNull(bean.getMappingBean().getBooleanProperty());
		assertTrue("mapping_bean_bool_prop", bean.getMappingBean().getBooleanProperty());
		//assertTrue("mapping_bean_date_prop", bean.getDate());
		assertTrue("mapping_bean_float_prop", new Float(2.45).equals(bean.getMappingBean().getFloatProperty()));
		assertTrue("mapping_bean_int_prop", new Integer(123).equals(bean.getMappingBean().getInteger()));
		assertTrue("mapping_bean_string_prop", "Hello".equals(bean.getMappingBean().getStringProperty()));
	}

	private Map<String, String[]> getParameters(Integer index, String type) {
		String indexStr = (index != null) ? "[" + index + "]" : "";
		String prefix = (type != null) ? type + "." : "";

		Map<String, String[]> parameters = new HashMap<String, String[]>();

		parameters.put(prefix + "booleanProperty" + indexStr, new String[]{"true"});
		parameters.put(prefix + "date" + indexStr, new String[]{"10-11-2011"});
		parameters.put(prefix + "floatProperty" + indexStr, new String[]{"2.45"});
		parameters.put(prefix + "integer" + indexStr, new String[]{"123"});
		parameters.put(prefix + "stringProperty" + indexStr, new String[]{"Hello"});

		parameters.put(prefix + "mappingBean"  + indexStr + ".booleanProperty", new String[]{"true"});
		parameters.put(prefix + "mappingBean"  + indexStr + ".date", new String[]{"10-11-2011"});
		parameters.put(prefix + "mappingBean"  + indexStr + ".floatProperty", new String[]{"2.45"});
		parameters.put(prefix + "mappingBean"  + indexStr + ".integer", new String[]{"123"});
		parameters.put(prefix + "mappingBean"  + indexStr + ".stringProperty", new String[]{"Hello"});

		return parameters;
	}
}
