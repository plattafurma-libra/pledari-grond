<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value='<%=session.getAttribute("pl")%>' />
<fmt:setBundle basename="de.uni_koeln.spinfo.maalr.webapp.i18n.text" />

<div id="ext_links_container">
	<div style="margin-left: 24px; padding-bottom: 5px;">
		<span style="text-decoration: underline;"><fmt:message key="maalr.dict_links.lia" /></span>
	</div>
	<ul class="ext_links_dicts">
		<li><a href="http://pledarigrond.ch/rumantsch" target="_blank"><fmt:message key="maalr.dict_links.rumantsch" /></a></li>
		<li><a href="http://pledarigrond.ch/surmiran" target="_blank"><fmt:message key="maalr.dict_links.surmiran" /></a></li>
		<li><a href="http://pledarigrond.ch/sutsilvan" target="_blank"><fmt:message key="maalr.dict_links.sutsilvan" /></a></li>
	</ul>
	<div style="margin-left: 24px; padding-bottom: 5px;">
		<span style="text-decoration: underline;"><fmt:message key="maalr.dict_links.other" /></span>
	</div>
	<ul class="ext_links_dicts">
		<li><a href="http://www.vocabularisursilvan.ch" target="_blank"><fmt:message key="maalr.dict_links.sursilvan" /></a></li>
		<li><a href="http://www.udg.ch/dicziunari/puter" target="_blank"><fmt:message key="maalr.dict_links.puter" /></a></li>
		<li><a href="http://www.udg.ch/dicziunari/vallader" target="_blank"><fmt:message key="maalr.dict_links.vallader" /></a></li>
		<li><a href="http://www.pledari.ch" target="_blank"><fmt:message key="maalr.dict_links.ulteriurs" /></a></li>
	</ul>
</div>