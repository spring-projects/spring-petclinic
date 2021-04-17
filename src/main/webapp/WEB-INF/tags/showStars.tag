<%@tag import="com.sun.org.apache.xalan.internal.xsltc.compiler.sym"%>
<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ attribute name="value" required="true" rtexprvalue="true" type="Integer"
              description="Number of starts" %>
<%@ attribute name="label" required="false" rtexprvalue="true"
              description="Label appears in red color if input is considered as invalid after submission" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<form>
    <c:if test="${label}">
        <label class="col-sm-2 control-label">${label}</label>
	</c:if>
<div class="rating"> 
	<input type="radio" class="form-control" disabled name="rating" value="5" id="5" <%= value.equals(5)  ?"checked='checked'" : ""%> /><label for="5"><span style="font-size: 100%" class="estrella_vacia">&#9734;</span></label> 		
	<input type="radio" class="form-control" disabled name="rating" value="4" id="4" <%= value.equals(4) ? "checked='checked'" : ""  %> /><label for="4"><span style="font-size: 100%" class="estrella_vacia">&#9734;</span></label> 
	<input type="radio" class="form-control" disabled name="rating" value="3" id="3" <%= value.equals(3)  ? "checked='checked'" : ""  %>/><label for="3"><span style="font-size: 100%" class="estrella_vacia">&#9734;</span></label> 
	<input type="radio" class="form-control" disabled name="rating" value="2" id="2" <%= value.equals(2)  ? "checked='checked'" : ""  %>/><label for="2"><span style="font-size: 100%" class="estrella_vacia">&#9734;</span></label> 
	<input type="radio" class="form-control" disabled name="rating" value="1" id="1" <%= value.equals(1)  ? "checked='checked'" : ""  %>/><label for="1"><span style="font-size: 100%" class="estrella_vacia">&#9734;</span></label> 
</div>       
    
</form>	
