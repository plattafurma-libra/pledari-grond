<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.Locale" %>

<%-- HTML HEADER --%>
<jsp:include page="/maalr_modules/misc/htmlhead.jsp" />

	<body>
		
		<%-- NAVIGATION --%>
		<div id="top"><jsp:include page="/maalr_modules/misc/header.jsp" /></div>

		<%-- CONTENT --%>		
		<div>
		
			<%@ include file="/maalr_modules/misc/language_widget.jsp" %>
			<%@ include file="/maalr_modules/misc/login_widget.jsp" %>
			
			<div class="container well information_container">
	
				<%
					String languageTag = (String) session.getAttribute("pl");
					Locale locale = Locale.forLanguageTag(languageTag);
				%>
				<% 
					if(languageTag.equals("sm")){
				%>
						<%@ include file="/static/infos-rm.jsp" %>
				<% 
					} else if(languageTag.equals("de")){
				%>
						<%@ include file="/static/infos-de.jsp" %>
				<% 
					} else {
				%>
					<h1>Nothing to display, please contact us!</h1>
				<% 
					}
				%>
			</div>
		</div>
		
		<%-- FOOTER --%>
		<div id="bottom"><jsp:include page="/maalr_modules/misc/footer.jsp" /></div>
		
	</body>
</html>