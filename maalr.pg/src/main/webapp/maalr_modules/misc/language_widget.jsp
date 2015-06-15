<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value='<%=session.getAttribute("locale")%>' />
<fmt:setBundle basename="de.uni_koeln.spinfo.maalr.webapp.i18n.text" />

	<%if(!session.getAttribute("locale").equals("ss")){ 
		session.setAttribute("locale","de");} %>

<%-- LANGUAGE SELECTION --%>
<div id="languages-widget">
	<ul>
		<li><a href="?locale=ss" class='<%=(session.getAttribute("locale").equals("ss"))?"lang_select active":"lang_select"%>'><fmt:message key="maalr.langSelect.sutsilvan" /></a></li>
		<li><a href="?locale=de" class='<%=(session.getAttribute("locale").equals("de"))?"lang_select active":"lang_select"%>'><fmt:message key="maalr.langSelect.german" /></a></li>
	</ul>
</div>