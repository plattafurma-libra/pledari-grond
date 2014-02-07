<%@page import="de.uni_koeln.spinfo.maalr.common.server.util.Configuration"%>
<%@ page import="de.uni_koeln.spinfo.maalr.common.shared.Constants"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="javax.security.auth.login.LoginContext"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="de.uni_koeln.spinfo.maalr.login.LoginManager"%>
<%@ page import="java.util.Calendar"%>
<%-- Header included here --%>
<%@ include file="/jsp/modules/htmlhead.jsp" %>
<body>
	<div class="centering">
		<%@ include file="/jsp/modules/header_small.jsp" %>
		</div>
	<div id="content" class="container">
		
		<div class="well span7 offset2">
		
				<%@ include file="/maalr_modules/search/simple_search.jsp"  %>
			<div style="text-align:center;">
				<p>
					Welcome to <b><%=Configuration.getInstance().getLongName()%></b>
				</p>
			</div>
		</div>
	</div>
</body>
</html>

