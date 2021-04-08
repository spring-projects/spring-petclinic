<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="cheapy" tagdir="/WEB-INF/tags" %>
<link href='https://fonts.googleapis.com/css?family=Lobster' rel='stylesheet'>
<!-- %@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %-->  
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<cheapy:layout pageName="home"> 
    <h2 class="text-center" style="font-family: 'Lobster'; font-size: 60px; color: rgb(0, 64, 128); padding:30px"><fmt:message key="welcome"/></h2>
    <div class="row">
        <div class="col-md-12">
            <div class="img-home">
                <spring:url value="/resources/images/Logo Cheapy.png" htmlEscape="true" var="cheapyImage"/>
                <img class="img-responsive" src="${cheapyImage}"/>
            </div>
            <div class="btn-home">
                <button type="button" role="link" onclick="window.location='/offers'" style="font-family: 'Lobster'; font-size: 20px;margin:5px;">
                <span class="glyphicon glyphicon-cutlery" aria-hidden="true" style="padding: 5px"> </span>
                <fmt:message key="listOffers"/> </button>
            </div>
            
   			<sec:authorize access="hasAnyAuthority('client')">
   			<div class="btn-home">
                <button type="button" role="link" onclick="window.location='/myOffers'" style="font-family: 'Lobster'; font-size: 20px;margin:5px;">
                <span class="glyphicon glyphicon-cutlery" aria-hidden="true" style="padding: 5px"> </span>
                <fmt:message key="myOffers"/> </button>
            </div>
	        <div class="btn-home">
                <button type="button" role="link" onclick="window.location='/offersCreate'" style="font-family: 'Lobster'; font-size: 20px;margin:5px;">
                <span class="glyphicon glyphicon-cutlery" aria-hidden="true" style="padding: 5px"> </span>
                <fmt:message key="createOffers"/> </button>
            </div>
 	        <div class="btn-home">
                <button type="button" role="link" onclick="window.location='/clients/show'" style="font-family: 'Lobster'; font-size: 20px;margin:5px;">
                <span class="glyphicon glyphicon-cutlery" aria-hidden="true" style="padding: 5px"> </span>
                <fmt:message key="clientShow"/> </button>
            </div>           
	        </sec:authorize>
		   
        </div>
    </div>
</cheapy:layout>
