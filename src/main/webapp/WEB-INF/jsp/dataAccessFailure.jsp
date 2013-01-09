<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<html lang="en">

<jsp:include page="header.jsp"/>

<body>
	<div id="main">
		<%
		Exception ex = (Exception) request.getAttribute("exception");
		%>
		
		<h2>Data access failure: <%= ex.getMessage() %></h2>
		<p/>
		
		<%
		ex.printStackTrace(new java.io.PrintWriter(out));
		%>
		
		<p/>
		<br/>
		<a href="<spring:url value="/" htmlEscape="true" />">Home</a>
		
  </div>
		<jsp:include page="footer.jsp"/>

</body>

</html>
