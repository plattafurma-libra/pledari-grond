<%@ page import="java.util.Locale" %>
<%@ page import="de.uni_koeln.spinfo.maalr.common.server.util.Configuration" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
		if(request.getParameter("locale") != null) {
			session.setAttribute("locale", request.getParameter("locale"));
		}
		if(session.getAttribute("locale") == null) {
			session.setAttribute("locale", request.getLocale().getLanguage());
		}
		String languageTag = (String) session.getAttribute("locale");
%>
<fmt:setLocale value="<%=session.getAttribute("locale") %>" />
<fmt:setBundle basename="de.uni_koeln.spinfo.maalr.webapp.i18n.text" />

<html lang="<%=languageTag %>">
	<body style="margin:0px">
	<div id="maalr_query_div" class="maalr_query_div"
    		data-source="<%=Configuration.getInstance().getServerInetAddress() %>"
    		data-locale="<%=languageTag%>"
    		data-button="<fmt:message key="maalr.query.search" />"
    		data-autoquery="true" 
    		data-pagesize="10"
    		data-embedcss="true">
    		<script type="text/javascript" src="<%=Configuration.getInstance().getServerInetAddress()%>/assets/js/embed_pledari.js"></script>
    </div>
	</body>
</html>