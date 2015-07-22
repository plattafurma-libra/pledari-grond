<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.Locale" %>

<%-- HTML HEADER --%>
<jsp:include page="/maalr_modules/misc/htmlhead.jsp" />

	<body>
		
		<%-- NAVIGATION --%>
		<div id="top"><jsp:include page="/maalr_modules/misc/header.jsp" /></div>

		<%-- CONTENT --%>		
		<div>
		
			<div class="container well information_container">
	
				<%
					String languageTag = (String) session.getAttribute("locale");
					Locale locale = Locale.forLanguageTag(languageTag);
				%>
				<% 
					if(languageTag.equals("st")){
				%>
						<%@ include file="/static/infos-st.jsp" %>
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