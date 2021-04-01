<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<petclinic:layout pageName="nuOffer">

	<jsp:body>
        <h2> ¿Esta seguro de que quiere dar de baja su offer? </h2>
      		
      		 <form:form modelAttribute="nuOffer" class="form-horizontal">
	            <input type="hidden" name="gold" value="${nu_offer.gold}" />
	            <input type="hidden" name="discountGold" value="${nu_offer.discount_gold}" />
	            <input type="hidden" name="silver" value="${nu_offer.silver}" />
	            <input type="hidden" name="discountSilver" value="${nu_offer.discount_silver}" />
	            <input type="hidden" name="bronze" value="${nu_offer.bronze}" />
	            <input type="hidden" name="discountBronze" value="${nu_offer.discount_bronze}" />

        		<button class="btn btn-default" type="submit">Dar de baja</button>
        	</form:form>
        
            <a class="btn btn-default" href='<spring:url value="/offers" htmlEscape="true"/>'>Volver</a>
           
    </jsp:body>
</petclinic:layout>
