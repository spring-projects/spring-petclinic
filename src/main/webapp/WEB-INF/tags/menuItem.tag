<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ attribute name="active" required="true" rtexprvalue="true" %>
<%@ attribute name="url" required="true" rtexprvalue="true" %>
<%@ attribute name="title" required="false" rtexprvalue="true" %>

<li class="${active ? 'active' : ''}">
    <a href="<spring:url value="${url}" htmlEscape="true" />"
       title="${fn:escapeXml(title)}">
        <jsp:doBody/>
    </a>
</li>
