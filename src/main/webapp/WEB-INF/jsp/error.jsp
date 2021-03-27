<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cheapy" tagdir="/WEB-INF/tags" %>


<cheapy:layout pageName="error">

    <h2 style="text-align:center">Algo malo ha pasado...</h2>
    
    <spring:url value="/resources/images/Logo Cheapy.png" htmlEscape="true" var="cheapyImage"/>
    <img class="img-responsive" src="${cheapyImage}"/>

    <p>${exception.message}</p>
	
</cheapy:layout>
