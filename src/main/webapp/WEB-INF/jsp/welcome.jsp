<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="cheapy" tagdir="/WEB-INF/tags" %>
<link href='https://fonts.googleapis.com/css?family=Lobster' rel='stylesheet'>
<!-- %@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %-->  

<cheapy:layout pageName="home">
    <h2 class="text-center" style="font-family: 'Lobster'; font-size: 60px; color: rgb(0, 64, 128); padding:30px"><fmt:message key="welcome"/></h2>
    <div class="row">
        <div class="col-md-12">
            <div class="img-home">
                <spring:url value="/resources/images/Logo Cheapy.png" htmlEscape="true" var="cheapyImage"/>
                <img class="img-responsive" src="${cheapyImage}"/>
            </div>
            <div class="btn-home">
                <a href="/offers"><button type="button" style="font-family: 'Lobster'; font-size: 20px;">
                	<span class="glyphicon glyphicon-cutlery" aria-hidden="true" style="margin-right:8px"></span>Ver Ofertas</button></a>
            </div>
        </div>
    </div>
</cheapy:layout>
