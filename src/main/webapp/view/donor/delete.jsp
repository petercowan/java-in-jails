<%@ page import="org.justgive.form.simple.SimpleBeanForm" %><%
	SimpleBeanForm simpleForm = (SimpleBeanForm) request.getAttribute("_donor_form");
%>
Are you sure you want to delete Donor? <a href="/donor/<%=simpleForm.getBeanIdentity()%>/delete?confirm_delete=true">Yes</a>
<a href="/donor/<%=simpleForm.getBeanIdentity()%>/show">No</a>