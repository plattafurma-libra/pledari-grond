<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page import="java.util.Locale"%>
<fmt:setLocale value="<%=session.getAttribute("pl") %>" />
<fmt:setBundle basename="de.uni_koeln.spinfo.maalr.webapp.i18n.text" />
<%
		String languageTag = (String) session.getAttribute("pl");
		if(languageTag == null) languageTag = "rm";
%>
<html lang="<%=languageTag %>">
	<body style="margin:0px">
	<div id="maalr_query_div" class="maalr_query_div"
    		data-source="http://localhost:8080"
    		data-locale="<%=languageTag%>"
    		data-button="<fmt:message key="maalr.query.search" />" 
    		data-autoquery="true" 
    		data-pagesize="10"
    		data-embedcss="true">
    		<script type="text/javascript" src="http://localhost:8080/assets/js/embed_pledari.js"></script>
    </div>
	</body>
</html>