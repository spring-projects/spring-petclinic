<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="cheapy" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<link href='https://fonts.googleapis.com/css?family=Lobster' rel='stylesheet'>

<cheapy:layout pageName="crearOfertas">
	<div class="btn-create-max">
	    <div class="btn-create">
	                <button type="button" role="link" onclick="window.location='/offers/food/new'" style="font-family: 'Lobster'; font-size: 20px;">
	                <span class="glyphicon glyphicon-cutlery" aria-hidden="true" style="padding: 5px"> </span>
	                <fmt:message key="createFoodOffers"/> </button>
	   	</div>
	   	
	   	<div class="btn-create">
	                <button type="button" role="link" onclick="window.location='/offers/nu/new'" style="font-family: 'Lobster'; font-size: 20px;">
	                <span class="glyphicon glyphicon-cutlery" aria-hidden="true" style="padding: 5px"> </span>
	                <fmt:message key="createNuOffers"/> </button>
	   	</div>
	   	
	   	<div class="btn-create">
	                <button type="button" role="link" onclick="window.location='/offers/speed/new'" style="font-family: 'Lobster'; font-size: 20px;">
	                <span class="glyphicon glyphicon-cutlery" aria-hidden="true" style="padding: 5px"> </span>
	                <fmt:message key="createSpeedOffers"/> </button>
	   	</div>
	   	
		<div class="btn-create">
			     	<button type="button" role="link" onclick="window.location='/offers/time/new'" style="font-family: 'Lobster'; font-size: 20px;">
	                <span class="glyphicon glyphicon-cutlery" aria-hidden="true" style="padding: 5px"> </span>
	                <fmt:message key="createTimeOffers"/> </button>
	   	</div>
		   	
   	</div>
   	
</cheapy:layout>
