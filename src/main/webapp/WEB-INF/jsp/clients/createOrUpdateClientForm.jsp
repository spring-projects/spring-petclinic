<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="cheapy" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<link href='https://fonts.googleapis.com/css?family=Lobster' rel='stylesheet'>

<cheapy:layout pageName="clients">
    <h2 style="font-family: 'Lobster'; text-align:center; font-size:200%;  color: rgb(0, 64, 128); padding:10px">
        <fmt:message key="client"/>
    </h2>
    
    <form:form modelAttribute="client" class="form-horizontal" id="add-client-form">
        <div class="form-group has-feedback">
			                
            <cheapy:passwordField label="Contrase�a" placeholder="Restaurante pepito" name="usuar.password"/>		
            <cheapy:inputField label="Hora de inicio" placeholder="HH:mm" name="init"/>
            <cheapy:inputField label="Hora de fin" placeholder="HH:mm" name="finish"/>
            <cheapy:inputField label="Nombre" placeholder="Restaurante pepito" name="name"/>
            <cheapy:inputField label="Email" placeholder="" name="email"/>
            <cheapy:inputField label="Direcci�n" placeholder="" name="address"/>           
            <cheapy:inputField label="Tel�fono" placeholder="" name="telephone"/>
            <cheapy:inputField label="descripci�n" placeholder="" name="description"/>
            <cheapy:inputField label="Comida" placeholder="food" name="food"/>
            <div class="form-group">                   
	            <label class="col-sm-2 control-label">Municipio: </label>
	            <div class="col-sm-10">
					<select name="municipio" class="select-municipio" style="width:80%;text-align-last:center;">
						<c:forEach items="${municipio}" var="entry">
							<option value="${entry}">${entry.toString()}</option>
						</c:forEach>
					</select>
				</div>
			</div>
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
