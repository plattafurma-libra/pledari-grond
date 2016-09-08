<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@	page import="java.util.ArrayList" %>
<%@	page import="java.util.List" %>

<%@ taglib prefix='c' uri='http://java.sun.com/jstl/core_rt' %>
<%@ taglib prefix='fmt' uri='http://java.sun.com/jsp/jstl/fmt' %>

<fmt:setLocale value='<%=session.getAttribute("locale") %>' />
<fmt:setBundle basename="de.uni_koeln.spinfo.maalr.webapp.i18n.text" />

<%-- HTML HEADER --%>
<jsp:include page="/maalr_modules/misc/htmlhead.jsp" />

	<body>
	
		<%-- SIGNUP DIALOG --%>
		<%@ include file="/maalr_modules/misc/signup_dialog.jsp"%>
		
		<%-- EXPORT DIALOG --%>
		<%@ include file="/maalr_modules/misc/export_dialog.jsp"%>
	
		<%-- NAVIGATION --%>
		<jsp:include page="/maalr_modules/misc/header.jsp" />
		
		<%-- FOOTER --%>
		<jsp:include page="/maalr_modules/misc/footer.jsp" />
		
		<div>
			
			<%@ include file="/maalr_modules/misc/language_widget.jsp"%>
			
			<%-- INTERNAL SIGN IN --%>
			<div class="container well" id="login_container">
			
				<h1><fmt:message key="maalr.login.header" /></h1>
				<br>
				<c:if test="${not empty param.login_error}">
					<p id="error_font"> <fmt:message key="maalr.login.error" /><br /></p>
				</c:if>
				<form name="f" action="<c:url value='j_spring_security_check'/>" method="POST">
					<div id="login_input">
						<div class="input_wrapper">
							<label for="uname"><fmt:message key="maalr.login.name"/></label>
							<input id="uname" type='text' name='j_username' value='<c:if test="${not empty param.login_error}"><c:out value="${SPRING_SECURITY_LAST_USERNAME}"/></c:if>' />
						</div>
						<div class="input_wrapper">
							<label for="upwd"><fmt:message key="maalr.login.pwd"/></label>
							<input id="upwd" type='password' name='j_password'>
						</div>
						<div class="button_wrapper">
							<input name="submit" type="submit" value=<fmt:message key="maalr.login.sendButton"/> id="internal_login">
						</div>
					</div>
				</form>
				 
				<%-- OPEN-ID SIGN IN --%>
				<div id="openid_login">

					<div class="input_wrapper">
						<a id="signUp" href="#signupDialog" id="signUp" data-toggle="modal"><fmt:message key="maalr.signup"/></a>
					</div>
				</div>
				<%-- login info text  --%>
				<div id="maalr_login_info">
					<span class="glyphicon icon-info-sign"></span>
					<p><fmt:message key="maalr.login.welcome" /></p>
				</div>
			</div>
		</div>
		
		<jsp:include page="/analytics.jsp" />
	</body>
</html>