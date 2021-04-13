<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="cheapy" tagdir="/WEB-INF/tags" %>
<link href='https://fonts.googleapis.com/css?family=Lobster' rel='stylesheet'>

<cheapy:layout pageName="usuarios">
    <h2 style="text-align:center;padding:5px">
        <c:if test="${usuario['new']}"><fmt:message key="new"/> </c:if> <fmt:message key="usuario"/>
    </h2>
    
    <form:form modelAttribute="usuario" class="form-horizontal" id="add-usuario-form">
        <div class="form-group has-feedback">
            <cheapy:inputField label="Nombre" name="nombre"/>
            <cheapy:inputField label="Apellidos" name="apellidos"/>
	<div class="form-group">                   
            <label>Municipio: </label>
			<select name="municipio">
				<c:forEach items="${municipio}" var="entry">
					<option value="${entry}">${entry}</option>
				</c:forEach>
			</select>
			</div>
            <cheapy:inputField label="Direccion" name="direccion"/>
            <cheapy:inputField label="Email" name="email"/>
            <cheapy:passwordField label="Password" name="usuar.password"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
            	<div class="btn-mod">
	                        <button class="btn btn-default" type="submit" style="font-family: 'Lobster'; font-size: 20px;">
	                        <span class="glyphicon glyphicon-floppy-save" aria-hidden="true" style="padding: 5px"> </span>
	                        Modificar</button>
                </div>
            </div>
        </div>
    </form:form>
    
</cheapy:layout>
