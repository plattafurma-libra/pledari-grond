<%@ page import="java.util.Calendar" %>
<%@ page import="de.uni_koeln.spinfo.maalr.webapp.i18n.UrlGenerator" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value='<%=session.getAttribute("locale")%>' />
<fmt:setBundle basename="de.uni_koeln.spinfo.maalr.webapp.i18n.text" />

<div id="navi_bottom">
	
	<ul id="navi_bottom_menu">
		<li><a href="#" id="propose_navi"><fmt:message key="maalr.navi.suggest" /></a></li>
		<li><a href="${dictContext}/help.html"><fmt:message key="maalr.navi.help" /></a></li>
		<li><a href="${dictContext}/infos.html"><fmt:message key="maalr.navi.info" /></a></li>
		<li><a href="http://www.liarumantscha.ch" target="_blank"><span>&copy; Lia Rumantscha 1980 &ndash; <%Calendar calendar = Calendar.getInstance(); out.write(calendar.get(Calendar.YEAR) + "");%></span></a></li>
		<li><a href="http://spinfo.phil-fak.uni-koeln.de/maalr.html" target="_blank"><span><fmt:message key="maalr.footer.maalr" /></span></a></li>
	</ul>

</div>