<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="cheapy" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<link href='https://fonts.googleapis.com/css?family=Lobster' rel='stylesheet'>

<cheapy:layout pageName="client">

    <h2 style="font-family: 'Lobster'; text-align:center; font-size:200%;  color: rgb(0, 64, 128); padding:10px"><fmt:message key="client"/></h2>


	
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
    </div>
  	

</cheapy:layout>
