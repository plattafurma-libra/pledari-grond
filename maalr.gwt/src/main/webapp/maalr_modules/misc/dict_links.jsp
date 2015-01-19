<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:setLocale value='<%=session.getAttribute("pl")%>' />
<fmt:setBundle basename="de.uni_koeln.spinfo.maalr.webapp.i18n.text" />

<div id="wraper_ext_links_dicts">
	<ul class="ext_links_dicts">
		<li><a href="http://pledarigrond.ch/surmiran" target="_blank"><fmt:message key="maalr.dict_links.surmiran" /></a></li>
		<li><a href="http://www.vocabularisursilvan.ch" target="_blank"><fmt:message key="maalr.dict_links.sursilvan" /></a></li>
		<li><a href="http://pledarigrond.ch/sutsilvan" target="_blank"><fmt:message key="maalr.dict_links.sutsilvan" /></a></li>
		<li><a href="http://www.udg.ch/dicziunari/puter" target="_blank"><fmt:message key="maalr.dict_links.puter" /></a></li>
		<li><a href="http://www.udg.ch/dicziunari/vallader" target="_blank"><fmt:message key="maalr.dict_links.vallader" /></a></li>
		<li><a href="http://www.pledari.ch" target="_blank"><fmt:message key="maalr.dict_links.ulteriurs" /></a></li>
	</ul>
</div>