<%@ page import="java.util.Calendar" %>
<%@ page import="de.uni_koeln.spinfo.maalr.webapp.i18n.UrlGenerator" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value='<%=session.getAttribute("locale")%>' />
<fmt:setBundle basename="de.uni_koeln.spinfo.maalr.webapp.i18n.text" />

<div id="navi_bottom">

	<div id="abc_list">
		<jsp:include page="/maalr_modules/browse/alist_main.jsp" />
	</div>
	<ul id="navi_bottom_menu">
		<li><a href="#" id="propose_navi"><fmt:message key="maalr.navi.suggest" /></a></li>	
		<li><a href="${dictContext}/browse.html">a &ndash; z</a></li>
		<li><a href="http://spinfo.phil-fak.uni-koeln.de/maalr.html" target="_blank"><fmt:message key="maalr.footer.maalr" /></a></li>
	</ul>
</div>