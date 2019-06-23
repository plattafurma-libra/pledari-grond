<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value='<%=session.getAttribute("locale")%>' />
<fmt:setBundle basename="de.uni_koeln.spinfo.maalr.webapp.i18n.text" />

<jsp:include page="/maalr_modules/misc/htmlhead.jsp" />
	<body>
		<div class="content">
			<div class="container-fluid">
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
					<div class="span8">
						<jsp:include page="/maalr_modules/search/results.jsp" />
					</div>		
				</div>
			</div>
		</div>
		
		<iframe src="javascript:''" id="__gwt_historyFrame" style="width: 0; height: 0; border: 0"></iframe>
		<jsp:include page="/analytics.jsp" />
	
	</body>
</html>