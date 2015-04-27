<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.Locale"%>

<%-- HTML HEADER --%>
<jsp:include page="/maalr_modules/misc/htmlhead.jsp" />

	<body>
		
		<%-- NAVIGATION --%>
		<div id="top"><jsp:include page="/maalr_modules/misc/header.jsp" /></div>

		<%-- CONTENT --%>		
		<div>
		
			<%@ include file="/maalr_modules/misc/language_widget.jsp"%>
			<%@ include file="/maalr_modules/misc/login_widget.jsp"%>
			
			<div class="container well information_container">
	
				<%
					String languageTag = (String) session.getAttribute("pl");
					Locale locale = Locale.forLanguageTag(languageTag);
				%>
				<% 
					if(languageTag.equals("ss")){
				%>
						<%@ include file="/static/json-ss.jsp" %>
				<% 
					} else if(languageTag.equals("de")){
				%>
						<%@ include file="/static/json-de.jsp" %>
				<% 
					} else {
				%>
					<h1>Nothing to display, please contact us!</h1>
				<% 
					}
				%>
			</div>
		</div>
		
					
					<%-- IMPRINT, COPYRIGHT, BASIC STATS --%>
		<%-- 
		<div id="imprint" class="span4">
			<p>
				<%
					String languageTag = (String) session.getAttribute("pl");
					Locale locale = Locale.forLanguageTag(languageTag);
				%>
				<%=Localizer.getEditorTranslations(languageTag).get("intro")%>
				<br>
				<%=Localizer.getEditorTranslations(languageTag).get("copyright")%>
				<%
					Calendar myCal = Calendar.getInstance();
					out.write(myCal.get(Calendar.YEAR) + "");
				%>

			</p>
			<p>
				<%NumberFormat nf = NumberFormat.getNumberInstance(locale);%>
				<fmt:message key="maalr.index.entry_count" var="numberOfEntries">
					<fmt:param><%=nf.format(DictionaryStatistics.getStatistics().entryCounter)%></fmt:param>
				</fmt:message>
				${numberOfEntries}
				<%=nf.format(DictionaryStatistics.getStatistics().overlayCount.get("V"))%>&nbsp;<%=Localizer.getEditorTranslations(languageTag).get("verbs")%>
			</p>
			<br/>
		</div>
		--%>
		
		<%-- Last update + Maalr Version - not needed in imprint
				<fmt:message key="maalr.index.last_update" var="lastUpdate">
				<fmt:param><%=DateFormat.getDateInstance(DateFormat.LONG,locale)
					.format(new Date(DictionaryStatistics.getStatistics().lastChange))%></fmt:param>
				</fmt:message>
				<b>${lastUpdate}</b>
								<br>
				Maalr <%=Localizer.getEditorTranslations(languageTag).get("version")%> ${pom.version} ${buildNumber}
		 --%>
			
		
		<%-- FOOTER --%>
		<div id="bottom"><jsp:include page="/maalr_modules/misc/footer.jsp" /></div>
		
	</body>
</html>