<%@ page import="org.justgive.model.Donor" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="req" uri="request.jar" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="jg" uri="http://www.justgive.org/taglibs" %>
<%
	List<Donor> donors = (List<Donor>) request.getAttribute("donors");
%>
<req:setattribute name="title">Show</req:setattribute>
<req:setattribute name="metaDescription"></req:setattribute>
<req:setattribute name="metaKeywords"></req:setattribute>
<req:setattribute name="pageCode">SHOW</req:setattribute>

<req:setattribute name="subheader">true</req:setattribute>

<req:setattribute name="content">
	<c:forEach items="${requestScope.donors}" var="donor">
		Donor: <br/>
		<c:out value="${donor.id}"/><br/>
		<c:out value="${donor.firstName}"/><br/>
		<c:out value="${donor.lastName}"/><br/>
		<c:out value="${donor.login}"/><br/>
		<c:out value="${donor.address1}"/><br/>
		<c:out value="${donor.city}"/><br/>
		<c:out value="${donor.state}"/><br/>
		<c:out value="${donor.zip}"/><br/>

		<a href="/donor/<c:out value="${donor.id}" />/edit">Edit Donor</a> -
		<a href="/donor/<c:out value="${donor.id}" />/delete">Delete Donor</a>
	</c:forEach>

</req:setattribute>

<req:setattribute name="section">give-now</req:setattribute>
<jg:templateforward template="template_noCol.jsp"/>
