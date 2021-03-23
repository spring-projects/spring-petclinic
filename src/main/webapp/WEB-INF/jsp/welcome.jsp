<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="cheapy" tagdir="/WEB-INF/tags" %>
<link href='https://fonts.googleapis.com/css?family=Lobster' rel='stylesheet'>
<!-- %@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %-->  

<cheapy:layout pageName="home">
    <h2 class="text-center" style="font-family: 'Lobster'; font-size: 50px; color: rgb(0, 64, 128); padding:20px"><fmt:message key="welcome"/></h2>
    <div class="row">
        <div class="col-md-12">
            <div class="img-home">
                <spring:url value="/resources/images/Logo Cheapy.png" htmlEscape="true" var="cheapyImage"/>
                <img class="img-responsive" src="${cheapyImage}"/>
            </div>
            <div class="btn-home">
                <button type="button"><span class="glyphicon glyphicon-cutlery" aria-hidden="true" style="padding: 5px"></span>  Ver Ofertas</button>
                
            </div>
        </div>
    </div>
</cheapy:layout>
