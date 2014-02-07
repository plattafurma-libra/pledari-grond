<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<jsp:include page="jsp/modules/htmlhead.jsp" />
<body>
	<jsp:include page="/jsp/modules/header_small.jsp" />
	<div class="content" id="content">
		<div class="container-fluid">
	<noscript>
			<div class="row-fluid">
				<div class="span12">
					<div class="alert">
    <strong>Please enable JavaScript! </strong> This browser does not have JavaScript turned on. To use all features of this page, and to improve the performance of your queries,
    please enable JavaScript.
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
	
	<iframe src="javascript:''" id="__gwt_historyFrame"
		style="width: 0; height: 0; border: 0"></iframe>
</body>
</html>