<%@ page import="java.util.Calendar" %>
<%@ page import="de.uni_koeln.spinfo.maalr.webapp.i18n.UrlGenerator" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value='<%=session.getAttribute("pl")%>' />
<fmt:setBundle basename="de.uni_koeln.spinfo.maalr.webapp.i18n.text" />

<div id="navi_bottom">
	<%--
	<div id="abc_list">
		<jsp:include page="/maalr_modules/browse/alist_main.jsp" />
	</div>
	--%>
	<ul id="navi_bottom_menu">
		<li><a href="#" id="propose_navi"><i></i><span class=""><fmt:message key="maalr.navi.suggest" /> </span></a></li>
		<li><a href="${dictContext}/help.html"><fmt:message key="maalr.navi.help" /></li>
		<li><a href="${dictContext}/infos.html"><i></i><span class=""><fmt:message key="maalr.navi.info" /> </span></a></li>
		<li><a href="${dictContext}/browse.html"><i></i><span class=""> a &ndash; z </span></a></li>
		<li><a href="http://www.liarumantscha.ch" target="_blank"> <span>&copy; Lia Rumantscha 1980 &ndash; <%Calendar calendar = Calendar.getInstance(); out.write(calendar.get(Calendar.YEAR) + "");%></span></a></li>
		<li><a href="http://spinfo.phil-fak.uni-koeln.de/maalr.html" target="_blank"> <span><fmt:message key="maalr.footer.maalr" /></span></a></li>
	</ul>
	<ul id="navi_bottom_social">
		<li><a href="#"><img src="${dictContext}/assets/img/twitter.png"></a></li>
		<li><a href="#"><img src="${dictContext}/assets/img/facebook.png"></a></li>
		<li><a href="#"><img src="${dictContext}/assets/img/like.png"><span class="count">4822</span></a></li>
	</ul>
</div>