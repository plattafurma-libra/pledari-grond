<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.List" %>

<%@ page import="de.uni_koeln.spinfo.maalr.common.server.util.Configuration" %>
<%@ page import="de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion" %>
<%@ page import="de.uni_koeln.spinfo.maalr.common.shared.description.LemmaDescription" %>
<%@ page import="de.uni_koeln.spinfo.maalr.common.shared.description.UseCase" %>
<%@ page import="de.uni_koeln.spinfo.maalr.lucene.query.QueryResult" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value='<%=session.getAttribute("pl")%>' />
<fmt:setBundle basename="de.uni_koeln.spinfo.maalr.webapp.i18n.text" />

<%-- 
		<jsp:include page="/maalr_modules/browse/alist_main.jsp" />
--%>
<div class="alpha_navi chars">
<%-- 			char[] chars = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
					'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
					'W', 'X', 'Y', 'Z', 'Ä', 'Ö', 'Ü', '0', '1', '2', '3', '4',
					'5', '6', '7', '8', '9' };
 --%>
		<%
			char[] chars = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
					'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
					'W', 'X', 'Y', 'Z', 'Ä', 'Ö', 'Ü' };
		String language = (String) request.getAttribute("language");
			for (int i = 0; i < chars.length; i++) {
				char c = chars[i];
		%>
		<div class="item">
			<fmt:message key="maalr.dict.letter_link" var="linkTitle">
				<fmt:param><%=c %></fmt:param>
			</fmt:message>
			<a href="${dictContext}/browse/<%=language %>/<%=c + ".html"%>" title="${linkTitle}"><%=c + ""%></a>
		</div>
		<%
			}
		%>
</div>		
			<div class="alpha_navi letters">
					<%
						LemmaDescription description = Configuration.getInstance().getLemmaDescription();
						String letter = (String) request.getAttribute("letter");
						if (letter == null) {
							letter = "A";
						}
						int pageNr = (Integer)request.getAttribute("page");
						QueryResult result = (QueryResult) request.getAttribute("result");
						int pageSize = result.getPageSize();
						int maxPage = result.getMaxEntries() / pageSize + 1;
						for(int i = 0; i < maxPage; i++) {
					%>
					<div class="item">
						<fmt:message key="maalr.dict.letter_page" var="linkTitle">
							<fmt:param><%=letter %></fmt:param>
							<fmt:param><%=(i+1) %></fmt:param>
						</fmt:message>
						
						<a href="${dictContext}/browse/<%=language +"/" + letter + ".html?page=" + i %>"
							title="${linkTitle}"><%=(i+1)%></a>
					</div>
					<%
						}
					%>
			</div>
			
			<div class="container browse_container">
				<div class="well">
					<div class="alphatable">
							<%
								int columns = 5;
								List<LemmaVersion> entries = result.getEntries();
								for (int i = 0; i < entries.size(); i++) {
									LemmaVersion entry = entries.get(i);
									String translated = description.toString(entry, UseCase.ALPHA_INDEX, language.equals(description.getLanguageName(true)));
										%>
									<fmt:message key="maalr.dict.translations_of" var="translationsOf"/>
									<div class="item"><a 
										href="${dictContext}<%="/dictionary/" + language + "/" + translated + ".html"%>"
											title="${translationsOf}&nbsp;<%=translated%>"><%=translated%>
										</a>
									</div>
							<% } %>
					</div>
				</div>
			
				<%-- INDEX LANGUAGE SELECTION --%>
				<div class="span12">
					<fmt:message key="maalr.dict.language"/>: 
					<a href="${dictContext}/browse/${otherLanguage}/${letter}.html"><fmt:message key="maalr.indexLang.${otherLanguage}" /></a>
					/
					<a href="${dictContext}/browse/${language}/${letter}.html"><fmt:message key="maalr.indexLang.${language}" /></a>	
				</div>
			
			</div>
