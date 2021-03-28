<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<link href='https://fonts.googleapis.com/css?family=Lobster' rel='stylesheet'>

<petclinic:layout pageName="NumOffers">
    <h2 style="text-align:center;padding:5px">
        <c:if test="${nuOffer['new']}"><fmt:message key="new"/> </c:if> <fmt:message key="nuOffer"/>
    </h2>
    <form:form modelAttribute="nuOffer" class="form-horizontal" id="add-nuOffer-form">
        <div class="form-group has-feedback">
            <form:hidden path="id"/>
            <form:hidden path="code"/>
            <form:hidden path="status"/>
            <petclinic:inputField label="Fecha de inicio" name="start"/>
            <petclinic:inputField label="Fecha de fin" name="end"/>
            
            <petclinic:inputField label="Oro" name="gold"/>
            <petclinic:inputField label="descuento de oro" name="discountGold"/>
            <petclinic:inputField label="Plata" name="silver"/>
            <petclinic:inputField label="Descuento de plata" name="discountSilver"/>
            <petclinic:inputField label="Bronce" name="bronze"/>
			<petclinic:inputField label="Descuento de bronce" name="discountBronze"/>

            </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
            	<div class="btn-mod">
	                <c:choose>
	                    <c:when test="${nuOffer['new']}">
	                        <button class="btn btn-default" type="submit" style="font-family: 'Lobster'; font-size: 20px;">
	                        <span class="glyphicon glyphicon-floppy-save" aria-hidden="true" style="padding: 5px"> </span>
	                        Crear oferta</button>
	                    </c:when>
	                    <c:otherwise>
	                        <button class="btn btn-default" type="submit" style="font-family: 'Lobster'; font-size: 20px;">
	                        <span class="glyphicon glyphicon-floppy-save" aria-hidden="true" style="padding: 5px"> </span>
	                        Modificar</button>
	                    </c:otherwise>
	                </c:choose>
                </div>
            </div>
        </div>
    </form:form>
</petclinic:layout>
