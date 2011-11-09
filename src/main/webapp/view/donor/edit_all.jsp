<%@ taglib prefix="jg" uri="http://www.justgive.org/taglibs" %>
<%@ page import="org.justgive.form.simple.SimpleRepeatableForm" %><%
	SimpleRepeatableForm simpleForm = (SimpleRepeatableForm) request.getAttribute("_donor_form");
%>
Edit Donors: <br />

<jsp:include page="include/repeat_donor_form.jsp" />

<br />
