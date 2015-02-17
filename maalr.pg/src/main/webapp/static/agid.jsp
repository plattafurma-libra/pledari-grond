<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.Locale" %>

<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%@ page import="org.springframework.security.openid.OpenIDAttribute" %>
<%@ page import="org.springframework.security.openid.OpenIDAuthenticationToken" %>

<%@ page import="de.uni_koeln.spinfo.maalr.common.shared.searchconfig.Localizer" %>

<%-- HTML HEADER --%>
<jsp:include page="/maalr_modules/misc/htmlhead.jsp" />

	<body>
	
		<%-- NAVIGATION --%>
		<div id="top"><jsp:include page="/maalr_modules/misc/header.jsp" /></div>

		<%-- CONTENT --%>	
		<div id="content">
			
			<%@ include file="/maalr_modules/misc/language_widget.jsp" %>
			
			<%@ include file="/maalr_modules/misc/login_widget.jsp" %>
		
			<div class="container well information_container">
				<%
					String languageTag = (String) session.getAttribute("pl");
					Locale locale = Locale.forLanguageTag(languageTag);
				%>
				<%
					if (languageTag.equals("ss")) {
				%>
				<jsp:include page="/static/agid-ss.jsp" />
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
		<div id="bottom"><jsp:include page="/maalr_modules/misc/footer.jsp" /></div>

	</body>
</html>