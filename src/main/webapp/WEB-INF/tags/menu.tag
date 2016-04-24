<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core_1_1" %>

<%@ attribute name="name" required="true" rtexprvalue="true"
              description="Name of the active menu: home, owners, vets or error" %>

<%-- Static navbar --%>
<nav class="navbar navbar-default" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <a class="navbar-brand" href="<spring:url value="/" htmlEscape="true" />"><span></span></a>
            <button type="button" class="navbar-toggle" data-toggle="collapse">
                <span class="sr-only"><os-p>Toggle navigation</os-p></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
        </div>
        <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav navbar-right">
                <c:choose>
                    <c:when test="${name eq 'home'}">
                        <c:set var="cssMenu" value="active"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="cssMenu" value=""/>
                    </c:otherwise>
                </c:choose>
                <li class="${cssMenu}"><a href="<spring:url value="/" htmlEscape="true" />"><span class="glyphicon glyphicon-home" aria-hidden="true"></span><span> Home</span></a></li>
                <c:choose>
                    <c:when test="${name eq 'owners'}">
                        <c:set var="cssMenu" value="active"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="cssMenu" value=""/>
                    </c:otherwise>
                </c:choose>
                <li class="${cssMenu}"><a href="<spring:url value="/owners/find.html" htmlEscape="true" />"><span class="glyphicon glyphicon-search" aria-hidden="true"></span><span> Find owners</span></a></li>
                <c:choose>
                    <c:when test="${name eq 'vets'}">
                        <c:set var="cssMenu" value="active"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="cssMenu" value=""/>
                    </c:otherwise>
                </c:choose>
                <li class="${cssMenu}"><a href="<spring:url value="/vets.html" htmlEscape="true" />"><span class="glyphicon glyphicon-th-list" aria-hidden="true"></span><span> Veterinarians</span></a></li>
                <c:choose>
                    <c:when test="${name eq 'error'}">
                        <c:set var="cssMenu" value="active"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="cssMenu" value=""/>
                    </c:otherwise>
                </c:choose>
                <li class="${cssMenu}"><a href="<spring:url value="/oups.html" htmlEscape="true" />"
                       title="trigger a RuntimeException to see how it is handled"><span class="glyphicon glyphicon-warning-sign" aria-hidden="true"></span><span> Error</span></a></li>
            </ul>
        </div> <%--/.nav-collapse --%>
    </div> <%--/.container-fluid --%>
</nav>
