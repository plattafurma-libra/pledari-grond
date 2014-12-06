<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:setLocale value='<%=session.getAttribute("pl")%>' />
<fmt:setBundle basename="de.uni_koeln.spinfo.maalr.webapp.i18n.text" />


<%-- FALLBACK LOCALE: accept "rm", otherwise force to "de" --%>
	<%if(!session.getAttribute("pl").equals("rm")){ 
		session.setAttribute("pl","de");} %>

<%-- LANGUAGE SELECTION --%>
<div id="languages-widget">
	<ul>
		<li><a href="?pl=rm" class='<%=(session.getAttribute("pl").equals("rm"))?"lang_select active":"lang_select"%>'><fmt:message key="maalr.langSelect.surmiran" /></a></li>
		<li><a href="?pl=de" class='<%=(session.getAttribute("pl").equals("de"))?"lang_select active":"lang_select"%>'><fmt:message key="maalr.langSelect.german" /></a></li>
	</ul>
</div>