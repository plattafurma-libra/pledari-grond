<%@page import="de.uni_koeln.spinfo.maalr.lucene.query.MaalrQuery"%>
<%@page
	import="de.uni_koeln.spinfo.maalr.lucene.query.MaalrQueryFormatter"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>	
<fmt:setLocale value='<%=session.getAttribute("locale") %>' />
<fmt:setBundle basename="de.uni_koeln.spinfo.maalr.webapp.i18n.text" />
<%
	MaalrQuery pgq = (MaalrQuery) request.getAttribute("search");
%>
<div align="left">
	<h3>
		<fmt:message key="maalr.query.noresults_header">
			<fmt:param><%=MaalrQueryFormatter.getQueryLabel(pgq) %></fmt:param>
		</fmt:message>
	</h3>
	<p>
		<fmt:message key="maalr.query.noresults_hint" />
	</p>
	<fmt:message key="maalr.misc.requires_js" var="js"/>
	<noscript><p>(${js})</p></noscript>
</div>
