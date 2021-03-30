<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<petclinic:layout pageName="foodOffer">

	<jsp:body>
        <h2> ¿Está seguro de que quiere eliminar su oferta? </h2>
      		
      		<form:form modelAttribute="foodOffer" class="form-horizontal">
	            <input type="hidden" name="food" value="${food_offer.food}" />
	            <input type="hidden" name="discount" value="${food_offer.discount}" />

        		<button class="btn btn-default" type="submit">Eliminar Oferta</button>
        	</form:form>
        
            <a class="btn btn-default" href='<spring:url value="/offers" htmlEscape="true"/>'>Volver</a>
           
    </jsp:body>
</petclinic:layout>
