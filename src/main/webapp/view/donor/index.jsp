<%@ taglib prefix="req" uri="request.jar" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="jg" uri="http://www.justgive.org/taglibs" %>
<req:setattribute name="title">Index</req:setattribute>
<req:setattribute name="metaDescription"></req:setattribute>
<req:setattribute name="metaKeywords"></req:setattribute>
<req:setattribute name="pageCode">INDEX</req:setattribute>

<req:setattribute name="subheader">true</req:setattribute>

<req:setattribute name="content">
	Listing Donors: <br />
	<jsp:useBean id="donors" scope="request" type="java.util.List"/>
	<c:forEach var="donor" items="${donors}">
	Donor: <br />
	(<c:out value="${donor.id}" />)
	<c:out value="${donor.firstName}" /> <c:out value="${donor.lastName}" /> :
	<a href="/donor/<c:out value="${donor.id}" />/show">Show</a> -
	<a href="/donor/<c:out value="${donor.id}" />/edit">Edit</a> -
	<a href="/donor/<c:out value="${donor.id}" />/delete">Delete</a>
	<br />
	</c:forEach>
</req:setattribute>

<req:setattribute name="section">give-now</req:setattribute>
<jg:templateforward template="template_noCol.jsp"/>