<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<petclinic:layout pageName="nuOfferDisable">

	<jsp:body>
        <h2> ¿Está seguro de que quiere dar de baja su offer? </h2>
      		
      		 <form:form modelAttribute="nuOffer" class="form-horizontal">
	            <input type="hidden" name="gold" value="${nuOffer.gold}" />
	            <input type="hidden" name="discountGold" value="${nuOffer.discountGold}" />
	            <input type="hidden" name="silver" value="${nuOffer.silver}" />
	            <input type="hidden" name="discountSilver" value="${nuOffer.discountSilver}" />
	            <input type="hidden" name="bronze" value="${nuOffer.bronze}" />
	            <input type="hidden" name="discountBronze" value="${nuOffer.discountBronze}" />

        		<button class="btn btn-default" type="submit">Dar de baja</button>
        	</form:form>
        
            <a class="btn btn-default" href='<spring:url value="/offers" htmlEscape="true"/>'>Volver</a>
           
    </jsp:body>
</petclinic:layout>
