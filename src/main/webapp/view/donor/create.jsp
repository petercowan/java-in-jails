<%@ taglib prefix="req" uri="request.jar" %>
<%@ taglib prefix="jg" uri="http://www.justgive.org/taglibs" %>
<req:setattribute name="title">Create</req:setattribute>
<req:setattribute name="metaDescription"></req:setattribute>
<req:setattribute name="metaKeywords"></req:setattribute>
<req:setattribute name="pageCode">CREATE</req:setattribute>

<req:setattribute name="subheader">true</req:setattribute>

<req:setattribute name="content">

Create a Donor: <br />

<jsp:include page="include/donor_form.jsp" />
</req:setattribute>

<req:setattribute name="section">give-now</req:setattribute>
<jg:templateforward template="template_noCol.jsp"/>