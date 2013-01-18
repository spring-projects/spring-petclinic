<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<spring:url value="/resources/images/banner-graphic.png" var="banner"/>
<img src="${banner}" />
<div class="navbar"  style="width: 601px;">
	<div class="navbar-inner">
		<ul class="nav">
			<li><a href="<spring:url value="/" htmlEscape="true" />"><i class="icon-home"></i> Home</a></li>
		  	<li><a href="<spring:url value="/owners/find.html" htmlEscape="true" />"><i class="icon-search"></i> Find owner</a></li>
		  	<li><a href="<spring:url value="/vets.html" htmlEscape="true" />"><i class="icon-th-list"></i> Display all veterinarians</a></li>
		  	<li><a href="<spring:url value="/resources/html/tutorial.html" htmlEscape="true" />"><i class=" icon-question-sign"></i> Tutorial</a></li>
		</ul>
	</div>
</div>
	
