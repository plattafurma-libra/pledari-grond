<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<jsp:include page="jspstatic/htmlhead.jsp" />
<body>
	<jsp:include page="/jspstatic/header_small.jsp" />

		<%@ include file="/jspstatic/language_widget.jsp"%>
		<%@ include file="/jspstatic/login_widget.jsp"%>

	<div id="content" class="container container_entry">
	
		<jsp:include page="/maalr_modules/browse/entry.jsp" />
	
	</div>
	<div id="bottom"><jsp:include page="/jspstatic/footer.jsp" /></div>
	<iframe src="javascript:''" id="__gwt_historyFrame"
		style="width: 0; height: 0; border: 0"></iframe>
</body>
</html>