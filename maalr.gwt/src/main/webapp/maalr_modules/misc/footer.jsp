<%@ page import="java.util.Calendar" %>
<%@ page import="de.uni_koeln.spinfo.maalr.webapp.i18n.UrlGenerator" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value='<%=session.getAttribute("pl")%>' />
<fmt:setBundle basename="de.uni_koeln.spinfo.maalr.webapp.i18n.text" />

<div id="navi_bottom">
	
	<div id="abc_list">
		<jsp:include page="/maalr_modules/browse/alist_main.jsp" />
	</div>
	<ul id="navi_bottom_menu">
		<li><a href="#" id="propose_navi"><i></i><span class=""><fmt:message key="maalr.navi.suggest" /> </span></a></li>	
		<li><a href="/browse.html"><i></i><span class=""> a &ndash; z </span></a></li>
		<li><a href="http://spinfo.phil-fak.uni-koeln.de/maalr.html" target="_blank"> <span><fmt:message key="maalr.footer.maalr" /></span></a></li>
		<!-- <li><a href="https://github.com/spinfo/maalr" target="_blank"> <span><fmt:message key="maalr.footer.maalr" /></span></a></li> -->	
	</ul>
</div>