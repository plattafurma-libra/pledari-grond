<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.List" %>

<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%@ page import="org.springframework.security.openid.OpenIDAttribute" %>
<%@ page import="org.springframework.security.openid.OpenIDAuthenticationToken" %>

<%-- HTML HEADER --%>
<jsp:include page="/maalr_modules/misc/htmlhead.jsp" />

	<body>
		
		<%-- NAVIGATION --%>
		<div id="top"><jsp:include page="/maalr_modules/misc/header.jsp" /></div>
		
		<%-- CONTENT --%>		
		<div id="content">
		
			<%@ include file="/maalr_modules/misc/language_widget.jsp" %>
			<%@ include file="/maalr_modules/misc/login_widget.jsp" %>
		
			<div class="container well container_plugin">
				<h2>L'installaziun dal plugin da tschertgar dal Pledari Grond
					en Firefox</h2>
				<h3>1. Uschè sa preschenta la trav da tschertgar en Firefox
					sin ina pagina senza plugin.</h3>
				<img src="../assets/img/giuru-betg-blau3-neu.gif" alt="" border="0">
				<h3>2. Uschespert che vus visitais la pagina dal Pledari
					Grond, survegn la frizza da la trav da tschertgar in fund blau.</h3>
				<img src="../assets/img/pledari-google-blau3-neu.gif" alt="" border="0">
				<h3>3. Lura pudais vus cliccar sin la frizza e tscherner
					"Pledari Grond hinzufügen".</h3>
				<img src="../assets/img/pledari-tscherner-installar-neu.gif" alt=""
					border="0">
				<h3>4. Il plugin è installà e vus pudais tschertgar cun la
					trav da tschertgar directamain en il Pledari Grond.</h3>
				<img src="../assets/img/pledari-cun-plugin3-neu.gif" alt="" border="0">
			</div>
		</div>
		
		<%-- FOOTER --%>
		<div id="bottom"><jsp:include page="/maalr_modules/misc/footer.jsp" /></div>
	
	</body>
</html>