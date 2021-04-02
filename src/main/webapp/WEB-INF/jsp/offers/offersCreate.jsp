<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="cheapy" tagdir="/WEB-INF/tags" %>
<link href='https://fonts.googleapis.com/css?family=Lobster' rel='stylesheet'>

<cheapy:layout pageName="crearOfertas">
	<div class="btn-create-max">
	    <div class="btn-create">
	                <button type="button" role="link" onclick="window.location='/foodOffers/new'" style="font-family: 'Lobster'; font-size: 20px;">
	                <span class="glyphicon glyphicon-cutlery" aria-hidden="true" style="padding: 5px"> </span>
	                <fmt:message key="createFoodOffers"/> </button>
	   	</div>
	   	
	   	<div class="btn-create">
	                <button type="button" role="link" onclick="window.location='/nuOffers/new'" style="font-family: 'Lobster'; font-size: 20px;">
	                <span class="glyphicon glyphicon-cutlery" aria-hidden="true" style="padding: 5px"> </span>
	                <fmt:message key="createNuOffers"/> </button>
	   	</div>
	   	
	   	<div class="btn-create">
	                <button type="button" role="link" onclick="window.location='/speedOffers/new'" style="font-family: 'Lobster'; font-size: 20px;">
	                <span class="glyphicon glyphicon-cutlery" aria-hidden="true" style="padding: 5px"> </span>
	                <fmt:message key="createSpeedOffers"/> </button>
	   	</div>
	   	
		<div class="btn-create">
			     	<button type="button" role="link" onclick="window.location='/timeOffers/new'" style="font-family: 'Lobster'; font-size: 20px;">
	                <span class="glyphicon glyphicon-cutlery" aria-hidden="true" style="padding: 5px"> </span>
	                <fmt:message key="createTimeOffers"/> </button>
	   	</div>
		   	
   	</div>
   	<div class="btn-return">
		    <button type="button" role="link" onclick="goBack()" style="font-family: 'Lobster'; font-size: 20px;"> 
		    <span class="glyphicon glyphicon-arrow-left" aria-hidden="true" style="padding: 5px"> </span> 
		    <fmt:message key="return"/> </button>
	</div>
   	<script>
		function goBack() {
		  window.history.back()
		}
	</script>
</cheapy:layout>
