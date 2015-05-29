<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<spring:url value="/resources/images/banner-graphic.png" var="banner"/>
<img src="${banner}"/>

<div class="navbar" style="width: 601px;">
    <div class="navbar-inner">
        <ul class="nav">
            <li style="width: 120px;"><a href="<spring:url value="/" htmlEscape="true" />"><i class="icon-home"></i>
                <fmt:message key="home"/></a></li>
            <li style="width: 150px;"><a href="<spring:url value='/owners/find.html' htmlEscape='true' />"><i
                    class="icon-search"></i> <fmt:message key="findOwners"/></a></li>
            <li style="width: 160px;"><a href="<spring:url value='/vets.html' htmlEscape='true' />"><i
                    class="icon-th-list"></i> <fmt:message key="veterinarians"/> </a></li>
            <li style="width: 110px;"><a href="<spring:url value='/oups.html' htmlEscape='true' />"
                                        title="<fmt:message key='errorTitle'/>"><i
                    class="icon-warning-sign"></i> <fmt:message key="error"/> </a></li>
        </ul>
    </div>
</div>
	
