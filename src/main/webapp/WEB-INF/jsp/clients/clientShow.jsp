<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="cheapy" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<link href='https://fonts.googleapis.com/css?family=Lobster' rel='stylesheet'>

<cheapy:layout pageName="client">

    <h2 style="text-align:center;padding:5px"><fmt:message key="client"/></h2>


	
    <table class="table table-striped" id="clientTable">
    	<thead>
        <tr>
            <th><fmt:message key="clientInit"/></th>
            <td><c:out value="${client.init}"/></td>
        </tr>
        <tr>
            <th><fmt:message key="clientFinish"/></th>
            <td><c:out value="${client.finish}"/></td>
        </tr>
		<tr>
            <th><fmt:message key="nameClient"/></th>
            <td><c:out value="${client.name}"/></td>
        </tr>
        <tr>
            <th><fmt:message key="emailClient"/></th>
            <td><c:out value="${client.email}%"/> </td>
        </tr>
        
        <tr>
            <th><fmt:message key="addressClient"/></th>
            <td><c:out value="${client.address}%"/> </td>
        </tr><tr>
            <th><fmt:message key="telephoneClient"/></th>
            <td><c:out value="${client.telephone}%"/> </td>
        </tr><tr>
            <th><fmt:message key="descriptionClient"/></th>
            <td><c:out value="${client.description}%"/> </td>
        </tr><tr>
            <th><fmt:message key="foodClient"/></th>
            <td><c:out value="${client.food}%"/> </td>
        </tr>
        
        
        
        </thead>
    </table>

    <div class="btn-menu">
	    
	<sec:authorize access="hasAnyAuthority('client')">
	<sec:authentication var="principal" property="principal" />
      <div class="btns-edit">
      		
		        <spring:url value="edit" var="editUrl"/>
		        <button type="button" role="link" onclick="window.location='${fn:escapeXml(editUrl)}'" style="font-family: 'Lobster'; font-size: 20px;">
		            <span class="glyphicon 	glyphicon glyphicon-edit" aria-hidden="true" style="padding: 5px"> </span>
		          Editar cliente</button>
		

	        <spring:url value="disable" var="disableUrl"/>
	        <button type="button" role="link" onclick="window.location='${fn:escapeXml(disableUrl)}'" style="font-family: 'Lobster'; font-size: 20px;">
	            <span class="glyphicon 	glyphicon glyphicon-edit" aria-hidden="true" style="padding: 5px"> </span>
	          Borrar  cliente</button>
      </div>
      </sec:authorize>
      <sec:authorize access="hasAnyAuthority('admin')">
		<sec:authentication var="principal" property="principal" />
    	<div class="btns-edit">
		
	        <spring:url value="/administrators/clients/{username}/disable" var="deactivateUrl">
	        	<spring:param name="username" value="${client.usuar.username}"/>
	        </spring:url>
	        <button type="button" role="link" onclick="window.location='${fn:escapeXml(deactivateUrl)}'" style="font-family: 'Lobster'; font-size: 20px;">
	        	<span class="glyphicon glyphicon glyphicon-trash" aria-hidden="true" style="padding: 5px"> </span>
	         Desactivar cliente</button>
    	</div>
    </sec:authorize>
    </div>
  	

</cheapy:layout>
