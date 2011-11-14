package org.jails.property.parser;

public interface PropertyParser {

	public String getPropertyName(String property);
	
	public boolean hasNestedProperty(String property);

	public String getNestedProperty(String property);
	
	public String getRootProperty(String property);

	public Integer getPropertyIndex(String property);
}
