<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<!-- %@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %-->  

<petclinic:layout pageName="home">
    <h2 class="text-center"><fmt:message key="welcome"/></h2>
    <div class="row">
        <div class="col-md-12">
            <div class="img-home">
                <spring:url value="/resources/images/Logo Cheapy.png" htmlEscape="true" var="cheapyImage"/>
                <img class="img-responsive" src="${cheapyImage}"/>
            </div>
            <div class="btn-home">
                <button type="button">Ver Ofertas</button>
            </div>
        </div>
    </div>
</petclinic:layout>
