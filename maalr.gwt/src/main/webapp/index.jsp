<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Locale" %>

<%@ page import="de.uni_koeln.spinfo.maalr.common.shared.searchconfig.Localizer" %>
<%@ page import="de.uni_koeln.spinfo.maalr.common.shared.Constants" %>

<%@ page import="de.uni_koeln.spinfo.maalr.mongo.stats.DictionaryStatistics" %>
<%@ page import="de.uni_koeln.spinfo.maalr.webapp.i18n.UrlGenerator" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value='<%=session.getAttribute("pl")%>' />
<fmt:setBundle basename="de.uni_koeln.spinfo.maalr.webapp.i18n.text" />

<%-- HTML HEADER --%>
<%@ include file="/maalr_modules/misc/htmlhead.jsp" %>

	<body>
	
		<%-- NAVIGATION --%>
		<%@ include file="/maalr_modules/misc/header.jsp" %>
	
		<div id="content" class="content">
			
			<%@ include file="/maalr_modules/misc/dict_links.jsp" %>
		
			<%@ include file="/maalr_modules/misc/language_widget.jsp" %>
			
			<%@ include file="/maalr_modules/misc/login_widget.jsp" %>
			
			<%-- SEARCH AREA --%>
			<div class="container-fluid" id="nojs_searchcontainer">
				<noscript>
					<div class="row-fluid">
						<div class="span12">
							<div class="alert container information_container">
								<strong><fmt:message key="maalr.misc.enable_js_header" /></strong>
								<fmt:message key="maalr.misc.enable_js_hint" />
							</div>
						</div>
					</div>
				</noscript>
				<div class="row-fluid">
					<div class="span4">
						<jsp:include page="/maalr_modules/search/complex_search.jsp" />
					</div>
					<div id="search-results" class="span8">
						<jsp:include page="/maalr_modules/search/results.jsp" />
					</div>
				</div>
			</div>
		</div>
		
		<jsp:include page="/maalr_modules/misc/footer.jsp" />

		<%-- GWT AJAX BROWSER HISTORY SUPPORT --%>
		<iframe src="javascript:''" id="__gwt_historyFrame" style="width: 0; height: 0; border: 0"></iframe>
		
		<jsp:include page="/analytics.jsp" />
	</body>
</html>