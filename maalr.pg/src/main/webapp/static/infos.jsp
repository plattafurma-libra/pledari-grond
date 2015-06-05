<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.Locale" %>

<%@ taglib prefix='cr' uri='http://java.sun.com/jstl/core_rt' %>

<%-- HTML HEADER --%>
<jsp:include page="/maalr_modules/misc/htmlhead.jsp" />

	<body>
		<%-- NAVIGATION --%>
		<div id="top">
			<jsp:include page="/maalr_modules/misc/header.jsp" />
		</div>

		<%-- CONTENT --%>		
		<div>
		
			<%@ include file="/maalr_modules/misc/language_widget.jsp" %>
			<%@ include file="/maalr_modules/misc/login_widget.jsp" %>
			
			<%
				String languageTag = (String) session.getAttribute("locale");
				// Locale locale = Locale.forLanguageTag(languageTag);
			%>
			
			<c:set var="locale" value='<%=(String) session.getAttribute("locale")%>'/>
			
			<div class="container well information_container">
				<cr:choose>
		    	 	<cr:when test="${locale eq 'rm'}">
		    	 		<%@ include file="/static/infos-rm.jsp" %>
		    	 	</cr:when>
		    	 	<cr:when test="${locale eq 'de'}">
		    	 		<%@ include file="/static/infos-de.jsp" %>
		    	 	</cr:when>
		    	 	<cr:otherwise>
		    	 		<h1>Nothing to display, please contact us!</h1>
		   			</cr:otherwise>
		    	</cr:choose>				
			</div>
		</div>
		
		<%-- FOOTER --%>
		<div id="bottom"><jsp:include page="/maalr_modules/misc/footer.jsp" /></div>
		
	</body>
</html>