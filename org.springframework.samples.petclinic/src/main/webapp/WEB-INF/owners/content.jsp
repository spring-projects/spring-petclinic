<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<div id="sidebar">
	<ul id="sub-nav">
		<li><a href="${pageContext.request.contextPath}/owners">Search</a></li>
		<li><a href="${pageContext.request.contextPath}/owners/new">Add New</a></li>
	</ul>
</div>
<div id="main">
	<tiles:insertAttribute name="main" />
</div>