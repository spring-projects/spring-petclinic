<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cheapy" tagdir="/WEB-INF/tags" %>


<cheapy:layout pageName="error">
	<h2 class="text-center" style="font-family: 'Lobster'; font-size: 30px; color: rgb(0, 64, 128); padding:30px"><em>Algo malo ha pasado...</em></h2>
    
    <spring:url value="/resources/images/Logo Cheapy.png" htmlEscape="true" var="cheapyImage"/>
    <img class="img-responsive" src="${cheapyImage}"/>

    <p>${exception.message}</p>
	
</cheapy:layout>
