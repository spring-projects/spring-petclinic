<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ attribute name="name" required="true" rtexprvalue="true"
              description="Name of corresponding property in bean object" %>
<%@ attribute name="disabled" required="false" rtexprvalue="true"
              description="Disable the input" %>
<%@ attribute name="label" required="true" rtexprvalue="true"
              description="Label appears in red color if input is considered as invalid after submission" %>
<%@ attribute name="placeholder" required="false" rtexprvalue="true"
              description="Example for input field" %>

<spring:bind path="${name}">
    <c:set var="cssGroup" value="form-group ${status.error ? 'has-error' : '' }"/>
    <c:set var="valid" value="${not status.error and not empty status.actualValue}"/>
    <div class="${cssGroup}">
    <c:if test="${fn:length(label)!=0}">
        <label class="col-sm-2 control-label">${label}</label>
	</c:if>
        <div class="col-sm-10">
			<div class="rating"> 
				<form:radiobutton class="form-control" disabled="${disabled}" name="rating" value="5" id="5" path="${name}"/><label for="5"><span style="font-size: 100%" class="estrella_vacia">&#9734;</span></label> 
				<form:radiobutton class="form-control" disabled="${disabled}" name="rating" value="4" id="4" path="${name}"/><label for="4"><span style="font-size: 100%" class="estrella_vacia">&#9734;</span></label> 
				<form:radiobutton class="form-control" disabled="${disabled}" name="rating" value="3" id="3" path="${name}"/><label for="3"><span style="font-size: 100%" class="estrella_vacia">&#9734;</span></label> 
				<form:radiobutton class="form-control" disabled="${disabled}" name="rating" value="2" id="2" path="${name}"/><label for="2"><span style="font-size: 100%" class="estrella_vacia">&#9734;</span></label> 
				<form:radiobutton class="form-control" disabled="${disabled}" name="rating" value="1" id="1" path="${name}"/><label for="1"><span style="font-size: 100%" class="estrella_vacia">&#9734;</span></label>
				
			</div>            
		
            <c:if test="${status.error}">
                <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                <span class="help-inline">${status.errorMessage}</span>
            </c:if>
        </div>
    </div>
</spring:bind>
