<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="TimeOffers">
    <h2>
        <c:if test="${timeOffer['new']}">Nueva </c:if> Oferta por tiempo
    </h2>
    <form:form modelAttribute="timeOffer" class="form-horizontal" id="add-timeOffer-form">
        <div class="form-group has-feedback">
        	<form:hidden path="id"/>
            <form:hidden path="code"/>
            <form:hidden path="status"/>
            <petclinic:inputField label="Fecha de inicio" placeholder="15/06/2021 14:00" name="start"/>
            <petclinic:inputField label="Fecha de fin" placeholder="15/06/2021 16:00" name="end"/>
            
            <petclinic:inputField label="Hora de inicio" name="init"/>
            <petclinic:inputField label="Hora de final" name="finish"/>
            <petclinic:inputField label="Descuento" name="discount"/>


            </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${timeOffer['new']}">
                        <button class="btn btn-default" type="submit">Añadir oferta</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit">Actualizar oferta</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</petclinic:layout>
