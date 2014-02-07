<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ page import="java.util.Locale"%>

<%@ page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@ page import="org.springframework.security.openid.OpenIDAttribute"%>
<%@ page import="org.springframework.security.openid.OpenIDAuthenticationToken"%>

<%@ page import="de.uni_koeln.spinfo.maalr.common.shared.searchconfig.Localizer"%>

<%-- HTML HEADER --%>
<jsp:include page="/jspstatic/htmlhead.jsp" />

	<body>
	
		<%-- NAVIGATION --%>
		<div id="top"><jsp:include page="/jspstatic/header_small.jsp" /></div>

		<%-- CONTENT --%>	
		<div id="content">
			
			<%@ include file="/jspstatic/language_widget.jsp"%>
			
			<%@ include file="/jspstatic/login_widget.jsp"%>
		
			<div class="container well information_container">
				<%
					String languageTag = (String) session.getAttribute("pl");
					Locale locale = Locale.forLanguageTag(languageTag);
				%>
				<%
					if (languageTag.equals("rm")) {
				%>
				<jsp:include page="/static/agid-rm.jsp" />
				<%
					} else if (languageTag.equals("de")) {
				%>
				<jsp:include page="/static/agid-de.jsp" />
				<%
					} else {
				%>
				<h1>No text found!</h1>
				<%
					}
				%>
			</div>
		</div>
		
		<%-- FOOTER --%>
		<div id="bottom"><jsp:include page="/jspstatic/footer.jsp" /></div>

	</body>
</html>