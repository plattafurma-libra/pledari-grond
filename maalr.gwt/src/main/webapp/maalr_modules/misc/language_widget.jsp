<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="de.uni_koeln.spinfo.maalr.common.shared.description.LemmaDescription.Language"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:setLocale value='<%=session.getAttribute("pl")%>' />
<fmt:setBundle basename="de.uni_koeln.spinfo.maalr.webapp.i18n.text" />


<%-- LANGUAGE SELECTION --%>
<div id="languages-widget">
	<ul>
		<li><a href="?pl=rm" class='<%=(session.getAttribute("pl").equals("rm"))?"lang_select active":"lang_select"%>'><fmt:message key="maalr.langSelect.romansh" /></a></li>
		<li><a href="?pl=de" class='<%=(session.getAttribute("pl").equals("de"))?"lang_select active":"lang_select"%>'><fmt:message key="maalr.langSelect.german" /></a></li>
		<li><a href="?pl=en" class='<%=(session.getAttribute("pl").equals("en"))?"lang_select active":"lang_select"%>'><fmt:message key="maalr.langSelect.english" /></a></li>
	</ul>
</div>