<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="cheapy" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<petclinic:layout pageName="timeOffer">

	<jsp:body>
        <h2> �Est� seguro de que quiere eliminar su oferta? </h2>
      		
      		<form:form modelAttribute="timeOffer" class="form-horizontal">
	            <input type="hidden" name="init" value="${timeOffer.init}" />
	            <input type="hidden" name="finish" value="${timeOffer.finish}" />
	            <input type="hidden" name="discount" value="${timeOffer.discount}" />

        		<button class="btn btn-default" type="submit">Eliminar Oferta</button>
        	</form:form>
        
            <a class="btn btn-default" href='<spring:url value="/offers" htmlEscape="true"/>'>Volver</a>
           
    </jsp:body>
</cheapy:layout>
