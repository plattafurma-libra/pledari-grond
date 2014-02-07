<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="de.uni_koeln.spinfo.maalr.webapp.i18n.UrlGenerator"%>
<%@ page import="java.util.Calendar"%>

<fmt:setLocale value="<%=session.getAttribute("pl")%>" />
<fmt:setBundle basename="de.uni_koeln.spinfo.maalr.webapp.i18n.text" />

<div class="navbar-fixed-bottom">
	<div class="navbar" style="margin-bottom: 0px;">
		<div class="navbar-inner">
			<div class="container" style="width:100%">
				
				<div>
					<jsp:include page="/maalr_modules/browse/alist_main.jsp" />
				</div>
				
				<ul class="nav">
					<li><a href="#" id="propose_navi"><i></i><span class=""><fmt:message key="maalr.navi.suggest" /> </span></a></li>	
					<%-- <li><a href="/browse.html"><i></i><span class=""><fmt:message key="maalr.navi.dictionary" /> </span></a></li> --%>
					<li><a href="/assets/binary/grammatica.pdf" target="_blank"><i></i><span class=""><fmt:message key="maalr.navi.grammatica" /> </span></a></li>		
					<li><%=UrlGenerator.getHelpLink()%></li>
					<li><a href="/infos.html"><i></i><span class=""><fmt:message key="maalr.navi.info" /> </span></a></li>
					<li><a href="/agidplug-in.html"><i></i><span class=""><fmt:message key="maalr.navi.plugin" /> </span></a></li>
					<li><a href="http://www.liarumantscha.ch"> <span>&copy;Lia Rumantscha 1980 - <%Calendar calendar = Calendar.getInstance(); out.write(calendar.get(Calendar.YEAR) + "");%></span></a></li>
					<li><a href="https://github.com/spinfo/maalr" target="_blank"> <span><fmt:message key="maalr.footer.maalr" /></span></a></li>
				</ul>
				
				<ul class="socialmedia">
					<li><a href="#"><img src="/assets/img/twitter.png"></a></li>
					<li><a href="#"><img src="/assets/img/facebook.png"></a></li>
					<li><a href="#"><img src="/assets/img/like.png"><span class="count">2422</span></a></li>
				</ul>
				
			</div>
		</div>
	</div>
</div>