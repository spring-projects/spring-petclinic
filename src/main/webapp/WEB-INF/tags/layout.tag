<%@ tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="cheapy" tagdir="/WEB-INF/tags" %>

<%@ attribute name="pageName" required="true" %>
<%@ attribute name="customScript" required="false" fragment="true"%>

<!doctype html>
<html>
<cheapy:htmlHeader/>

<body>
<cheapy:bodyHeader menuName="${pageName}"/>

<div class="container-fluid">
    <div class="container xd-container">

        <jsp:doBody/>

        <cheapy:pivotal/>
    </div>
</div>
<cheapy:footer/>
<jsp:invoke fragment="customScript" />

</body>

</html>
