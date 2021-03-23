<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cheapy" tagdir="/WEB-INF/tags" %>

<cheapy:layout pageName="error">

    <spring:url value="/resources/images/pets.png" var="petsImage"/>
    <img src="${petsImage}"/>

    <h2>Something happened...</h2>

    <p>${exception.message}</p>

</cheapy:layout>
