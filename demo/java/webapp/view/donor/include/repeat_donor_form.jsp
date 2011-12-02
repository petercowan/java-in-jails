<%@ taglib uri="justgive-taglib.jar" prefix="simple_form" %>
<simple_form:repeated_form name="donor" submitLabel="Save Donors">
	<simple_form:text name="firstName" label="First Name" />
	<simple_form:text name="lastName" label="Last Name" />
	<simple_form:text name="login" label="Login" />
	<simple_form:text name="password" label="Password" />
	<simple_form:text name="address1" label="Address" />
	<simple_form:text name="city" label="City" />
	<simple_form:text name="state" label="State" />
	<simple_form:text name="zip" label="Zip Code" />
	<hr />
	<simple_form:submit label="Save Donor" />
</simple_form:repeated_form>