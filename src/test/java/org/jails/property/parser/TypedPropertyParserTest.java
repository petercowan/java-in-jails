package org.jails.property.parser;

import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TypedPropertyParserTest 		
		extends TestCase
{
	public TypedPropertyParserTest(String name) {
		super(name);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static junit.framework.Test suite()
	{
		return new TestSuite( TypedPropertyParserTest.class );
	}

	public void testParseProperty() {
		String propertyString = "type.property";

		TypedPropertyParser propertyParser = new TypedPropertyParser();

		String type = propertyParser.getRootProperty(propertyString);
		propertyString = propertyString.replaceAll(type, "");
		
		String propertyName = propertyParser.getPropertyName(propertyString);
		boolean hasNestedProperty = propertyParser.hasNestedProperty(propertyString);
		String nestedProperty = propertyParser.getNestedProperty(propertyString);
		String rootProperty = propertyParser.getRootProperty(propertyString);
		Integer propertyIndex = propertyParser.getPropertyIndex(propertyString);

		assertEquals("type", type);
		assertEquals("property", propertyName);
		assertFalse("has_nested", hasNestedProperty);
		assertNull("nested", nestedProperty);
		assertEquals(rootProperty, "property");
		assertNull("index", propertyIndex);
	}

	public void testParseNestedProperty() {
		String nestedPropertyString = "type.nested.property";

		TypedPropertyParser propertyParser = new TypedPropertyParser();

		String type = propertyParser.getRootProperty(nestedPropertyString);
		nestedPropertyString = nestedPropertyString.replaceAll(type, "");
		
		String propertyName = propertyParser.getPropertyName(nestedPropertyString);
		boolean hasNestedProperty = propertyParser.hasNestedProperty(nestedPropertyString);
		String nestedProperty = propertyParser.getNestedProperty(nestedPropertyString);
		String rootProperty = propertyParser.getRootProperty(nestedPropertyString);
		Integer propertyIndex = propertyParser.getPropertyIndex(nestedPropertyString);

		assertEquals("type", type);
		assertEquals("nested.property", propertyName);
		assertTrue("nested_has_nested", hasNestedProperty);
		assertTrue("nested_nested", nestedProperty.equals("property"));
		assertEquals(rootProperty, "nested");
		assertNull("nested_index", propertyIndex);
	}

	public void testParseIndexedProperty() {
		String propertyString = "type.property[0]";

		TypedPropertyParser propertyParser = new TypedPropertyParser();

		String type = propertyParser.getRootProperty(propertyString);
		propertyString = propertyString.replaceAll(type, "");
		
		String propertyName = propertyParser.getPropertyName(propertyString);
		boolean hasNestedProperty = propertyParser.hasNestedProperty(propertyString);
		String nestedProperty = propertyParser.getNestedProperty(propertyString);
		String rootProperty = propertyParser.getRootProperty(propertyString);
		int propertyIndex = propertyParser.getPropertyIndex(propertyString);

		assertEquals("type", type);
		assertNotNull(propertyName);
		assertEquals("property", propertyName);
		assertFalse("has_nested", hasNestedProperty);
		assertNull("nested", nestedProperty);
		assertEquals(rootProperty, "property[0]");
		assertNotNull(propertyIndex);
		assertEquals(propertyIndex, 0);
	}

	public void testParseIndexedNestedProperty() {
		String nestedPropertyString = "type.nested[0].property";

		TypedPropertyParser propertyParser = new TypedPropertyParser();

		String type = propertyParser.getRootProperty(nestedPropertyString);
		nestedPropertyString = nestedPropertyString.replaceAll(type, "");
		
		String propertyName = propertyParser.getPropertyName(nestedPropertyString);
		boolean hasNestedProperty = propertyParser.hasNestedProperty(nestedPropertyString);
		String nestedProperty = propertyParser.getNestedProperty(nestedPropertyString);
		String rootProperty = propertyParser.getRootProperty(nestedPropertyString);
		String rootPropertyName = propertyParser.getRootProperty(propertyName);
		Integer propertyIndex = propertyParser.getPropertyIndex(nestedPropertyString);

		assertEquals("type", type);
		assertNotNull(propertyName);
		assertEquals("nested.property", propertyName);
		assertTrue("nested_has_nested", hasNestedProperty);
		assertTrue("nested_nested_name", nestedProperty.equals("property"));
		assertEquals(rootProperty, "nested[0]");
		assertEquals(rootPropertyName, "nested");
		assertEquals(propertyIndex.intValue(), 0);
	}

	public void testParseNestedIndexedNestedProperty() {
		String nestedPropertyString = "type.nested[0].property[1]";

		TypedPropertyParser propertyParser = new TypedPropertyParser();

		String type = propertyParser.getRootProperty(nestedPropertyString);
		nestedPropertyString = nestedPropertyString.replaceAll(type, "");
		
		String propertyName = propertyParser.getPropertyName(nestedPropertyString);
		boolean hasNestedProperty = propertyParser.hasNestedProperty(nestedPropertyString);
		String nestedProperty = propertyParser.getNestedProperty(nestedPropertyString);
		String nestedPropertyName = propertyParser.getPropertyName(nestedProperty);
		String rootProperty = propertyParser.getRootProperty(nestedPropertyString);
		String rootPropertyName = propertyParser.getRootProperty(propertyName);
		Integer propertyIndex = propertyParser.getPropertyIndex(nestedPropertyString);

		assertEquals("type", type);
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

