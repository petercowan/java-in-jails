<%@ page import="org.justgive.model.Donor" %>
<%@ taglib prefix="req" uri="request.jar" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="jg" uri="http://www.justgive.org/taglibs" %>
<%
Donor donor = (Donor) request.getAttribute("donor");
%>
<req:setattribute name="title">Show</req:setattribute>
<req:setattribute name="metaDescription"></req:setattribute>
<req:setattribute name="metaKeywords"></req:setattribute>
<req:setattribute name="pageCode">SHOW</req:setattribute>

<req:setattribute name="subheader">true</req:setattribute>

<req:setattribute name="content">
	Donor: <br />
	<c:out value="${donor.id}" /><br />
	<c:out value="${donor.firstName}" /><br />
	<c:out value="${donor.lastName}" /><br />
	<c:out value="${donor.login}" /><br />
	<c:out value="${donor.address1}" /><br />
	<c:out value="${donor.city}" /><br />
	<c:out value="${donor.state}" /><br />
	<c:out value="${donor.zip}" /><br />

	<a href="/donor/<c:out value="${donor.id}" />/edit">Edit Donor</a> -
	<a href="/donor/<c:out value="${donor.id}" />/delete">Delete Donor</a>

</req:setattribute>

<req:setattribute name="section">give-now</req:setattribute>
<jg:templateforward template="template_noCol.jsp"/>
