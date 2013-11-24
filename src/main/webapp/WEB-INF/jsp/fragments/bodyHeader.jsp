<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<spring:url value="/resources/images/banner-graphic.png" var="banner"/>
<img src="${banner}"/>

<div class="navbar navbar-default" style="width: 601px;">
     <ul class="nav nav-pills">
         <li style="width: 100px;"><a href="<spring:url value="/" htmlEscape="true" />">
         	<span class="glyphicon glyphicon-home"></span>
             Home</a>
         </li>
         <li style="width: 130px;"><a href="<spring:url value="/owners/find.html" htmlEscape="true" />"><i
                 class="glyphicon glyphicon-search"></i> Find owners</a></li>
         <li style="width: 140px;">
         	<a href="<spring:url value="/vets.html" htmlEscape="true" />">
         	<span class="glyphicon glyphicon-th-list"></span> Veterinarians</a>
         </li>
         <li style="width: 90px;">
         	<a href="<spring:url value="/oups.html" htmlEscape="true" />"
                                     title="trigger a RuntimeException to see how it is handled">
         	<i class="glyphicon glyphicon-warning-sign"></i> Error</a>
         </li>
         <li style="width: 80px;">
         	<a href="#" title="not available yet. Work in progress!!">
         		<i class=" glyphicon glyphicon-question-sign"></i> 
         		Help
         	</a>
         </li>
     </ul>
</div>
	
