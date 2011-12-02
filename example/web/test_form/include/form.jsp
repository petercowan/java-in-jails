<%@ taglib uri="http://org.jails.org/form/taglib" prefix="s" %>
<s:form name="test_form">
	<s:text name="firstName" label="First Name" size="15" />
	<s:text name="lastName" label="Last Name" size="15" />
	<s:text name="login" label="Login" size="50" />
	<s:password name="password" label="Password" size="15" />
	<s:text name="address1" label="Address" />
	<s:text name="city" label="City" size="50" />
	<s:state_select name="state" label="State"></s:state_select>
	<s:text name="zip" label="Zip Code" size="15" />
	<s:hidden name="id" />
	<s:submit label="Save Donor" />
</s:form>
