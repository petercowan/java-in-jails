package org.jails.property;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class PropertyWrapperTest     
		extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public PropertyWrapperTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( PropertyWrapperTest.class );
    }

	public void testFormatKey()
	{
		PropertiesWrapper map = getWrapper();

		assertEquals(map.formatKey("foo.bar.baz"), "foo.bar.baz");
		assertEquals(map.formatKey("bar.baz"), "foo.bar.baz");
		assertEquals(map.formatKey("foo.bar.baz", 0), "foo[0].bar.baz");
		assertEquals(map.formatKey("bar.baz", 0), "foo[0].bar.baz");

		System.out.println("total keys: " + map.size());

		for (String key : new TreeMap<String,String[]>(map).keySet()) {
			System.out.println(key + ": " + map.getValue(key));
		}
	}
	
	public void testContainsKey()
	{
		PropertiesWrapper map = getWrapper();

		System.out.println(map.formatKey("foo.bar.baz"));
		System.out.println(map.formatKey("bar.baz"));
		System.out.println(map.formatKey("foo[0].bar.baz"));
		System.out.println(map.formatKey("foo.bar.baz", 0));
		System.out.println(map.formatKey("bar.baz", 0));
		assertTrue(map.containsKey("foo.bar.baz"));
		assertTrue(map.containsKey("bar.baz"));
		assertTrue(map.containsKey("foo.bar.baz", 0));
		assertTrue(map.containsKey("bar.baz", 0));
		assertTrue(map.containsKey("foo.bat.bam"));
		assertTrue(map.containsKey("bat.bam"));
	}
	
    public void testGet()
    {
		PropertiesWrapper map = getWrapper();
		
        assertEquals(map.getValue("foo.bar.baz"), "1");
		assertEquals(map.getValue("bar.baz"), "1");
		assertEquals(map.getValue("foo.bar.baz", 0), "1");
		assertEquals(map.getValue("bar.baz", 0), "1");
		assertEquals(map.getValue("foo.bat.bam"), "1");
		assertEquals(map.getValue("bat.bam"), "1");
    }
	
	public void testPut()
	{
		PropertiesWrapper map = getWrapper();
		
		map.put("foo.bim.bop", "1");
		map.put("foo.bim.bop", 0, "1");
		assertEquals(map.getValue("foo.bim.bop"), "1");
		assertEquals(map.getValue("bim.bop"), "1");
		assertEquals(map.getValue("foo.bim.bop", 0), "1");
		assertEquals(map.getValue("bim.bop", 0), "1");
	}
	
	public void testRemove()
	{
		PropertiesWrapper map = getWrapper();
		
		assertEquals(map.removeValue("foo.bar.baz"), "1");
		assertEquals(map.removeValue("bar.baz"), null);
		assertEquals(map.removeValue("foo.bar.baz", 0), "1");
		assertEquals(map.removeValue("bar.baz", 0), null);
		assertEquals(map.removeValue("foo.bat.bam"), "1");
		assertEquals(map.removeValue("bat.bam"), null);
	}
	
	public void testRename()
	{
		PropertiesWrapper map = getWrapper();
		
		map.renameKey("foo.bar.baz", "foo.bim.bop");
		map.renameKey("foo.bar.baz", 0, "foo.bim.bop");
		assertEquals(map.getValue("foo.bim.bop"), "1");
		assertEquals(map.getValue("bim.bop"), "1");
		assertEquals(map.getValue("foo.bim.bop", 0), "1");
		assertEquals(map.getValue("bim.bop", 0), "1");
	}
	
	public void testComposeDate()
	{
		PropertiesWrapper map = getWrapper();
		
		assertEquals(map.composeDate("foo.bar.date"), "2010-11-12");
		assertEquals(map.composeDate("foo.bar.date", 0), "2010-11-12");
	}
	
	public void testSelectOther()
	{
		PropertiesWrapper map = getWrapper();

		assertEquals(map.getSelectOtherValue("foo.bar.select"), "haha");
		assertEquals(map.getSelectOtherValue("foo.bar.select", 0), "haha");
	}
	
	public void testGetList()
	{
		PropertiesWrapper map = getWrapper();

		String[] list = map.getList("foo.bar.list");
//		String[] indexedList = map.getList("foo[0].bar.list");
		assertEquals(list[0], "this");
		assertEquals(list[1], "is");
		assertEquals(list[2], "my");
		assertEquals(list[3], "list");
//		assertEquals(map.getSelectOtherValue("foo.bar.select", 0), "haha");
	}

	private PropertiesWrapper getWrapper() {

		Map<String,String[]> map = new HashMap<String, String[]>();
		map.put("foo.bar.baz", new String[]{"1"});
		map.put("foo[0].bar.baz", new String[]{"1"});

		map.put("bat.bam", new String[]{"1"});
		map.put("bat[0].bam", new String[]{"1"});

		map.put("foo.bar._date_date_day", new String[]{"12"});
		map.put("foo.bar._date_date_month", new String[]{"11"});
		map.put("foo.bar._date_date_year", new String[]{"2010"});
		map.put("foo[0].bar._date_date_day", new String[]{"12"});
		map.put("foo[0].bar._date_date_month", new String[]{"11"});
		map.put("foo[0].bar._date_date_year", new String[]{"2010"});

		map.put("foo.bar.select", new String[]{"other"});
		map.put("foo.bar._select_select_other", new String[]{"haha"});
		map.put("foo[0].bar.select", new String[]{"other"});
		map.put("foo[0].bar._select_select_other", new String[]{"haha"});
		
		map.put("foo.bar.list", new String[]{"this,is,my,list"});
		map.put("foo[0].bar.list", new String[]{"this,is,my,list"});

		return new PropertiesWrapper(map, Foo.class);
	}
	
	
}

class Foo {}