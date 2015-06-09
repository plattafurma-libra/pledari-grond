<%@ page import="de.uni_koeln.spinfo.maalr.common.shared.description.LemmaDescription.Language" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value='<%=session.getAttribute("locale")%>' />
<fmt:setBundle basename="de.uni_koeln.spinfo.maalr.webap.i18n.text" />


<%-- LANGUAGE SELECTION --%>
<div id="languages-widget">
	<ul>
		<li><a href="?locale=ss" class='<%=(session.getAttribute("locale").equals("ss"))?"lang_select active":"lang_select"%>'><fmt:message key="maalr.langSelect.sutsilvan" /></a></li>
		<li><a href="?locale=de" class='<%=(session.getAttribute("locale").equals("de"))?"lang_select active":"lang_select"%>'><fmt:message key="maalr.langSelect.german" /></a></li>
		<li><a href="?locale=en" class='<%=(session.getAttribute("locale").equals("en"))?"lang_select active":"lang_select"%>'><fmt:message key="maalr.langSelect.english" /></a></li>
	</ul>
</div>