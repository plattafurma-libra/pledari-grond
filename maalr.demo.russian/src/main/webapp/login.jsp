<%@page
	import="org.apache.taglibs.standard.tag.common.core.ForEachSupport"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ taglib prefix='c' uri='http://java.sun.com/jstl/core_rt'%>

<%@page import="org.springframework.security.openid.OpenIDAttribute"%>
<%@page import="java.util.List"%>
<%@page
	import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page
	import="org.springframework.security.openid.OpenIDAuthenticationToken"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%-- Header included here --%>
<jsp:include page="/jsp/modules/htmlhead.jsp" />
<body>
	<div id="top">
		<jsp:include page="/jsp/modules/header_small.jsp" />
	</div>
	<div id="content">
		<div class="container well">
			<h1>Login</h1>
			<p>Try editor/editor or admin/admin.
			<p>
				<c:if test="${not empty param.login_error}">
					<font color="red"> Your login attempt was not successful,
						try again.<br />
					</font>
				</c:if>
			<form name="f" action="<c:url value='j_spring_security_check'/>"
				method="POST">
				<table>
					<tr>
						<td>User:</td>
						<td><input id="uname" type='text' name='j_username'
							value='<c:if test="${not empty param.login_error}"><c:out value="${SPRING_SECURITY_LAST_USERNAME}"/></c:if>' />
						</td>
					</tr>
					<tr>
						<td>Password:</td>
						<td><input id="upwd" type='password' name='j_password'></td>
					</tr>
					<tr>
						<td><input type="checkbox"
							name="_spring_security_remember_me"></td>
						<td>Don't ask for my password for two weeks</td>
					</tr>

					<tr>
						<td colspan='2'><input name="submit" type="submit"
							id="internal_login"></td>
					</tr>
				</table>
			</form>

			<table>
				<tr>
					<td>
						<form action="<c:url value='j_spring_openid_security_check'/>"
							method="post">
							<input name="openid_identifier" type="hidden"
								value="https://www.google.com/accounts/o8/id" />
							<div style="height: 50px">
								<input type="image" src="/assets/img/login/google.png"
									alt="Sign in with Google">
							</div>
							<input type="submit" value="Sign in with Google" />
						</form>
					</td>
					<td>
						<form action="<c:url value='j_spring_openid_security_check'/>"
							method="post">
							<input name="openid_identifier" type="hidden"
								value="https://me.yahoo.com/" />
							<div style="height: 50px">
								<input type="image" src="/assets/img/login/yahoo.png"
									alt="Sign in with Yahoo">
							</div>
							<input type="submit" value="Sign in with Yahoo" />
						</form>
					</td>
				</tr>
			</table>
		</div>
	</div>
</body>
</html>

