<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="cheapy" tagdir="/WEB-INF/tags" %>
<link href='https://fonts.googleapis.com/css?family=Lobster' rel='stylesheet'>

<cheapy:layout pageName="clients">
    <h2 style="text-align:center;padding:5px">
        <c:if test="${client['new']}"><fmt:message key="new"/> </c:if> <fmt:message key="client"/>
    </h2>
    
    <form:form modelAttribute="client" class="form-horizontal" id="add-client-form">
        <div class="form-group has-feedback">

			<form:hidden path="code"/>
			<cheapy:inputField label="Contraseña" placeholder="Restaurante pepito" name="usuar.password"/>
		

            <cheapy:inputField label="Hora de inicio" placeholder="HH:mm" name="init"/>
            <cheapy:inputField label="Hora de fin" placeholder="HH:mm" name="finish"/>
            <cheapy:inputField label="Name" placeholder="Restaurante pepito" name="name"/>
            <cheapy:inputField label="Email" placeholder="" name="email"/>
            <cheapy:inputField label="Dirección" placeholder="" name="address"/>
            <cheapy:inputField label="telephone" placeholder="" name="telephone"/>
            <cheapy:inputField label="description" placeholder="" name="description"/>
            <cheapy:inputField label="food" placeholder="food" name="food"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
            	<div class="btn-mod">
	                <c:choose>
	                    <c:when test="${client['new']}">
	                        <button class="btn btn-default" type="submit" style="font-family: 'Lobster'; font-size: 20px;">
	                        <span class="glyphicon glyphicon-floppy-save" aria-hidden="true" style="padding: 5px"> </span>
	                        Crear cliente</button>
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
    
</cheapy:layout>
