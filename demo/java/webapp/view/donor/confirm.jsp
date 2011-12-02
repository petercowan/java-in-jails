<%@ taglib prefix="req" uri="request.jar" %>
<%@ taglib prefix="jg" uri="http://www.justgive.org/taglibs" %>

<%@ page import="org.justgive.form.simple.SimpleBeanForm" %>
<%
	SimpleBeanForm simpleForm = (SimpleBeanForm) request.getAttribute("_donor_form");
%>
<req:setattribute name="title">Confirm Deletion?</req:setattribute>
<req:setattribute name="metaDescription"></req:setattribute>
<req:setattribute name="metaKeywords"></req:setattribute>
<req:setattribute name="pageCode">CONFIRM_DELETE</req:setattribute>

<req:setattribute name="subheader">true</req:setattribute>

<req:setattribute name="content">
	Are you sure you want to delete Donor? <a href="/donor/<%=simpleForm.getBeanIdentity()%>/delete?confirm_delete=true">Yes</a>
	<a href="/donor/<%=simpleForm.getBeanIdentity()%>/show">No</a>
</req:setattribute>

<req:setattribute name="section">give-now</req:setattribute>
<jg:templateforward template="template_noCol.jsp"/>