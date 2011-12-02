<%@ taglib prefix="jg" uri="http://www.justgive.org/taglibs" %>
<%@ page import="org.justgive.form.simple.SimpleBeanForm" %>
<%
	SimpleBeanForm simpleForm = (SimpleBeanForm) request.getAttribute("_donor_form");
%>
<html>
<head>
	<link rel="stylesheet" href="/css/validationEngine.jquery.css" type="text/css"/>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.5.1/jquery.min.js" type="text/javascript"></script>
	<script src="/js/languages/jquery.validationEngine-en.js" type="text/javascript" charset="utf-8"></script>
	<script src="/js/jquery.validationEngine.js" type="text/javascript" charset="utf-8"></script>
	        <script>
            jQuery(document).ready(function(){
                // binds form submission and fields to the validation engine
                jQuery("#donor").validationEngine();
            });
        </script>
</head>
<body>
Edit Donor: <br/>

<jsp:include page="include/donor_form.jsp"/>

<br/>
<a href="/donor/<%=simpleForm.getBeanIdentity()%>/delete">Delete Donor</a>
</body>
</html>
