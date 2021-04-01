<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<link href='https://fonts.googleapis.com/css?family=Lobster' rel='stylesheet'>

<petclinic:layout pageName="speedOffers">
    <h2 style="text-align:center;padding:5px">
        <c:if test="${speedOffer['new']}"><fmt:message key="new"/> </c:if> <fmt:message key="speedOffer"/>
    </h2>
    <form:form modelAttribute="speedOffer" class="form-horizontal" id="add-speedOffer-form">
        <div class="form-group has-feedback">
            <form:hidden path="id"/>
            <form:hidden path="code"/>
            <form:hidden path="status"/>
            <petclinic:inputField label="Fecha de Inicio" placeholder="dd/MM/yyyy HH:mm" name="start"/>
            <petclinic:inputField label="Fecha de Fin"  placeholder="dd/MM/yyyy HH:mm" name="end"/>
            <petclinic:inputField label="Tiempo para comer (nivel Oro)" placeholder="XX minutos (Ej. 5)" name="gold"/>
            <petclinic:inputField label="Descuento nivel Oro" placeholder="XX% (Ej. 35)" name="discountGold"/>
            <petclinic:inputField label="Tiempo para comer (nivel Plata)" placeholder="XX minutos (Ej. 10)" name="silver"/>
            <petclinic:inputField label="Descuento nivel Plata" placeholder="XX% (Ej. 15)" name="discountSilver"/>
            <petclinic:inputField label="Tiempo para comer (nivel Bronce)" placeholder="XX minutos (Ej. 20)" name="bronze"/>
            <petclinic:inputField label="Descuento nivel Bronce" placeholder="XX% (Ej. 5)" name="discountBronze"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
            	<div class="btn-mod">
	                <c:choose>
	                    <c:when test="${speedOffer['new']}">
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
