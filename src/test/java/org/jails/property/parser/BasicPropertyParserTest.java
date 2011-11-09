package org.jails.property.parser;

import junit.framework.TestCase;
import junit.framework.TestSuite;

public class BasicPropertyParserTest
		extends TestCase
{

	public BasicPropertyParserTest(String name) {
		super(name);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static junit.framework.Test suite()
	{
		return new TestSuite( BasicPropertyParserTest.class );
	}

	public void testParseProperty() {
		String propertyString = "property";

		SimplePropertyParser propertyParser = new SimplePropertyParser();

		String type = propertyParser.getType(propertyString);
		String propertyName = propertyParser.getPropertyName(propertyString);
		boolean hasNestedProperty = propertyParser.hasNestedProperty(propertyString);
		String nestedProperty = propertyParser.getNestedProperty(propertyString);
		String rootProperty = propertyParser.getRootProperty(propertyString);
		Integer propertyIndex = propertyParser.getPropertyIndex(propertyString);

		assertNull("type_is_null", type);
		assertTrue("property_name", propertyString.equals(propertyName));
		assertFalse("has_nested", hasNestedProperty);
		assertNull("nested", nestedProperty);
		assertEquals(rootProperty, propertyString);
		assertNull("index", propertyIndex);
	}

	public void testParseNestedProperty() {
		String nestedPropertyString = "nested.property";

		SimplePropertyParser propertyParser = new SimplePropertyParser();

		String type = propertyParser.getType(nestedPropertyString);
		String propertyName = propertyParser.getPropertyName(nestedPropertyString);
		boolean hasNestedProperty = propertyParser.hasNestedProperty(nestedPropertyString);
		String nestedProperty = propertyParser.getNestedProperty(nestedPropertyString);
		String rootProperty = propertyParser.getRootProperty(nestedPropertyString);
		Integer propertyIndex = propertyParser.getPropertyIndex(nestedPropertyString);

		assertNull("nested_type_is_null", type);
		assertTrue("property_name", nestedPropertyString.equals(propertyName));
		assertTrue("nested_has_nested", hasNestedProperty);
		assertTrue("nested_nested", nestedProperty.equals("property"));
		assertEquals(rootProperty, "nested");
		assertNull("nested_index", propertyIndex);
	}

	public void testParseIndexedProperty() {
		String propertyString = "property[0]";

		SimplePropertyParser propertyParser = new SimplePropertyParser();

		String type = propertyParser.getType(propertyString);
		String propertyName = propertyParser.getPropertyName(propertyString);
		boolean hasNestedProperty = propertyParser.hasNestedProperty(propertyString);
		String nestedProperty = propertyParser.getNestedProperty(propertyString);
		String rootProperty = propertyParser.getRootProperty(propertyString);
		int propertyIndex = propertyParser.getPropertyIndex(propertyString);

		assertNull("type_is_null", type);
		assertNotNull(propertyName);
		assertTrue("property_name", propertyName.equals("property"));
		assertFalse("has_nested", hasNestedProperty);
		assertNull("nested", nestedProperty);
		assertEquals(rootProperty, propertyString);
		assertNotNull(propertyIndex);
		assertEquals(propertyIndex, 0);
	}

	public void testParseIndexedNestedProperty() {
		String nestedPropertyString = "nested[0].property";

		SimplePropertyParser propertyParser = new SimplePropertyParser();

		String type = propertyParser.getType(nestedPropertyString);
		String propertyName = propertyParser.getPropertyName(nestedPropertyString);
		boolean hasNestedProperty = propertyParser.hasNestedProperty(nestedPropertyString);
		String nestedProperty = propertyParser.getNestedProperty(nestedPropertyString);
		String rootProperty = propertyParser.getRootProperty(nestedPropertyString);
		String rootPropertyName = propertyParser.getRootProperty(propertyName);
		Integer propertyIndex = propertyParser.getPropertyIndex(nestedPropertyString);

		assertNull("nested_type_is_null", type);
		assertNotNull(propertyName);
		assertTrue("property_name", propertyName.equals("nested.property"));
		assertTrue("nested_has_nested", hasNestedProperty);
		assertTrue("nested_nested_name", nestedProperty.equals("property"));
		assertEquals(rootProperty, "nested[0]");
		assertEquals(rootPropertyName, "nested");
		assertNotNull(propertyIndex);
		assertEquals(propertyIndex.intValue(), 0);
	}

	public void testParseNestedIndexedNestedProperty() {
		String nestedPropertyString = "nested[0].property[1]";

		SimplePropertyParser propertyParser = new SimplePropertyParser();

		String type = propertyParser.getType(nestedPropertyString);
		String propertyName = propertyParser.getPropertyName(nestedPropertyString);
		boolean hasNestedProperty = propertyParser.hasNestedProperty(nestedPropertyString);
		String nestedProperty = propertyParser.getNestedProperty(nestedPropertyString);
		String nestedPropertyName = propertyParser.getPropertyName(nestedProperty);
		String rootProperty = propertyParser.getRootProperty(nestedPropertyString);
		String rootPropertyName = propertyParser.getRootProperty(propertyName);
		Integer propertyIndex = propertyParser.getPropertyIndex(nestedPropertyString);

		assertNull("nested_type_is_null", type);
		assertNotNull(propertyName);
		assertEquals(propertyName, "nested.property[1]");
		assertTrue("nested_has_nested", hasNestedProperty);
		assertEquals(nestedProperty, "property[1]");
		assertEquals(nestedPropertyName, "property");
		assertEquals(rootProperty, "nested[0]");
		assertEquals(rootPropertyName, "nested");
		assertEquals(propertyIndex.intValue(), 0);
	}
}
