package org.jails.property;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.beanutils.MethodUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.jails.property.handler.PropertyHandler;
import org.jails.property.handler.SimplePropertyHandler;
import org.jails.property.parser.PropertyParser;
import org.jails.property.parser.SimplePropertyParser;
import org.jails.util.Strings;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MapperTest
		extends TestCase {
	private Mapper beanMapper;

	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public MapperTest(String testName) {
		super(testName);
		PropertyParser parser = new SimplePropertyParser();
		PropertyHandler handler = new SimplePropertyHandler();
		beanMapper = new Mapper(parser, handler);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(MapperTest.class);
	}

	public void testMapToBean() {
		Map<String, String[]> params = getParameters(null, "mappingBean");
		MappingBean bean = new MappingBean();
		beanMapper.toExistingObject(bean, params);

		assertBeanValues(bean);
	}

	public void testMapListToBean() {
		Map<String, String[]> paramMap = new LinkedHashMap<String, String[]>();
		for (int i = 0; i < 2; i++) {
			paramMap.putAll(getParameters(i, "mappingBean"));
			System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXindex: " + i);
		}

		List<MappingBean> beans = new ArrayList<MappingBean>();
		beans.add(new MappingBean());
		beans.add(new MappingBean());
		beanMapper.toExistingList(beans, paramMap);

		for (MappingBean bean : beans) {
			assertBeanValues(bean);
		}
	}

	private void assertBeanValues(MappingBean bean) {
		assertTrue("bool_prop", bean.getBooleanProperty());
//		assertTrue("date_prop", bean.getDate());
		assertTrue("float_prop", new Float(2.45).equals(bean.getFloatProperty()));
		assertTrue("int_prop", new Integer(123).equals(bean.getInteger()));
		assertTrue("string_prop", "Hello".equals(bean.getStringProperty()));

		assertNotNull(bean.getMappingBean());
		assertNotNull(bean.getMappingBean().getBooleanProperty());
		assertFalse("mapping_bean_bool_prop", bean.getMappingBean().getBooleanProperty());
//		assertTrue("mapping_bean_date_prop", bean.getDate());
		assertTrue("mapping_bean_float_prop", new Float(3.56).equals(bean.getMappingBean().getFloatProperty()));
		assertTrue("mapping_bean_int_prop", new Integer(234).equals(bean.getMappingBean().getInteger()));
		assertTrue("mapping_bean_string_prop", "Hi".equals(bean.getMappingBean().getStringProperty()));
	}

	private Map<String, String[]> getParameters(Integer index, String type) {
		String indexStr = (index != null) ? "[" + index + "]" : "";
		String prefix = (type != null) ? type + indexStr + "." : "";

		System.out.println("prefix: " + prefix);
		Map<String, String[]> parameters = new HashMap<String, String[]>();

		parameters.put(prefix + "booleanProperty", new String[]{"true"});
		parameters.put(prefix + "date", new String[]{"10-11-2011"});
		parameters.put(prefix + "floatProperty", new String[]{"2.45"});
		parameters.put(prefix + "integer", new String[]{"123"});
		parameters.put(prefix + "stringProperty", new String[]{"Hello"});

		parameters.put(prefix + "mappingBean.booleanProperty", new String[]{"false"});
		parameters.put(prefix + "mappingBean.date", new String[]{"1-12-2011"});
		parameters.put(prefix + "mappingBean.floatProperty", new String[]{"3.56"});
		parameters.put(prefix + "mappingBean.integer", new String[]{"234"});
		parameters.put(prefix + "mappingBean.stringProperty", new String[]{"Hi"});

		return parameters;
	}

	public void xConvertBeanToMap() {
		MappingBean bean = getBean();
		ObjectMapper m = new ObjectMapper();

		Map<String, Object> rawParamMap = m.convertValue(bean, Map.class);

		convertMap(rawParamMap, "mappingBean");
		//Map<String, String[]> map = beanMapper.toMap(bean);

		//assertMapValues(map, null);
	}

	public void convertMap(Map<String, Object> rawParamMap, String type) {
		String prefix = (type != null) ? type + "." : "";
		for (String param : rawParamMap.keySet()) {
			Object value = rawParamMap.get(param);
			if (value instanceof Map) {
				System.out.println(prefix + param + ": Collection " + value.getClass().getSimpleName());
				convertMap((Map<String, Object>) value, prefix + param);
			} else {
				if (value != null) {
					System.out.print(value.getClass().getSimpleName() + " - ");
					System.out.println(prefix + param + ": " + ConverterUtil.getInstance().convert(value));
				}
			}
			//paramMap.put(key, new String[]{value.toString()});
		}

		//Map<String, String[]> map = beanMapper.toMap(bean);

		//assertMapValues(map, null);
	}

	public void xDescribeBeanToMap() {
		MappingBean bean = getBean();

		Map<String, String> rawParamMap = describeMap(bean, "mappingBean");

		for (String key : rawParamMap.keySet()) {
			System.out.println(key + ": " + rawParamMap.get(key));
		}
		//Map<String, String[]> map = beanMapper.toMap(bean);

		//assertMapValues(map, null);
	}

	protected Map<String, String> describeMap(Object bean, String type) {
		String prefix = (type != null) ? type + "." : "";

		Map<String, String> paramMap = new LinkedHashMap<String, String>();
		try {
			Method[] methods = bean.getClass().getMethods();

			Map<Method, String> getters = new HashMap<Method, String>();
			for (int i = 0; i < methods.length; i++) {
				Method method = methods[i];
				if (method.getParameterTypes() == null || method.getParameterTypes().length == 0) {
					if (method.getName().startsWith("get") || method.getName().startsWith("is")) {
						String property = (method.getName().startsWith("get"))
								? Strings.initLowercase(method.getName().substring(3))
								: Strings.initLowercase(method.getName().substring(2));
						getters.put(method, property);
					}
				}
			}

			for (Method method : getters.keySet()) {
				try {
					Object value = MethodUtils.invokeMethod(bean, method.getName(), null);
					if (value == null) {
						//ignore for now
					} else if (ConverterUtil.getInstance().canConvert(value)) {
						paramMap.put(prefix + getters.get(method), ConverterUtil.getInstance().convert(value));
					} else {
						Map<String, String> nestedMap = describeMap(value, prefix + getters.get(method));
						paramMap.putAll(nestedMap);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return paramMap;
	}

	public void testBeanToMap() {
		Map<String, String[]> map = beanMapper.toMap(getBean());

		assertMapValues(map, null);
	}

	public void testBeanListToMap() {
		List<MappingBean> list = new ArrayList<MappingBean>();
		for (int i = 0; i < 2; i++) {
			list.add(getBean());
		}
		Map<String, String[]> map = beanMapper.toMap(list);

		for (int i = 0; i < 2; i++) {
			assertMapValues(map, i);
		}
	}

	private void assertMapValues(Map<String, String[]> map, Integer index) {
		String indexStr = (index != null) ? "[" + index + "]" : "";

		for (String key : map.keySet()) {
			String[] values = map.get(key);
			System.out.print(key + ": ");
			if (values != null && values.length > 0) System.out.println(values[0]);
			else System.out.println("" + null);
		}
		assertEquals("bool_prop", "true", map.get("mappingBean" + indexStr + ".booleanProperty")[0]);
		assertEquals("float_prop", "2.45", map.get("mappingBean" + indexStr + ".floatProperty")[0]);
		assertEquals("int_prop", "123", map.get("mappingBean" + indexStr + ".integer")[0]);
		assertEquals("string_prop", "Hello", map.get("mappingBean" + indexStr + ".stringProperty")[0]);

		assertEquals("bool_prop", "false", map.get("mappingBean" + indexStr + ".mappingBean.booleanProperty")[0]);
		assertEquals("float_prop", "3.56", map.get("mappingBean" + indexStr + ".mappingBean.floatProperty")[0]);
		assertEquals("int_prop", "234", map.get("mappingBean" + indexStr + ".mappingBean.integer")[0]);
		assertEquals("string_prop", "Hi", map.get("mappingBean" + indexStr + ".mappingBean.stringProperty")[0]);
	}

	private MappingBean getBean() {
		MappingBean bean = new MappingBean();

		bean.setBooleanProperty(true);
		bean.setFloatProperty(2.45f);
		bean.setInteger(123);
		bean.setStringProperty("Hello");
		bean.setIntegerArray(new Integer[]{1,2,3,4,5});

		bean.setMappingBean(new MappingBean());
		bean.getMappingBean().setBooleanProperty(false);
		bean.getMappingBean().setFloatProperty(3.56f);
		bean.getMappingBean().setInteger(234);
		bean.getMappingBean().setStringProperty("Hi");
		bean.getMappingBean().setIntegerArray(new Integer[]{1,2,3,4,5});
		return bean;
	}

}
