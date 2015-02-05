<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ page import="de.uni_koeln.spinfo.maalr.common.server.util.Configuration"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value='<%=session.getAttribute("pl") %>' />
<fmt:setBundle basename="de.uni_koeln.spinfo.maalr.webapp.i18n.text" />

<table class="alpha_navi">
	<tr>
<%-- 			char[] chars = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
					'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
					'W', 'X', 'Y', 'Z', 'Ä', 'Ö', 'Ü', '0', '1', '2', '3', '4',
					'5', '6', '7', '8', '9' };
 --%>
		<%
			char[] chars = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
					'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
					'W', 'X', 'Y', 'Z', 'Ä', 'Ö', 'Ü' };
			String language = Configuration.getInstance().getLemmaDescription().getLanguageName(true);
			for (int i = 0; i < chars.length; i++) {
				char c = chars[i];
		%>
		<td>
			<fmt:message key="maalr.dict.letter_link" var="linkTitle">
				<fmt:param><%=c %></fmt:param>
			</fmt:message>
			<a href="${dictContext}/browse/<%=language %>/<%=c + ".html"%>" title="${linkTitle}"><%=c + ""%></a>
		</td>
		<%
			}
		%>
	</tr>
</table>

