<%@ page import="javax.security.auth.login.LoginContext" %>

<%@ page import="de.uni_koeln.spinfo.maalr.common.server.util.Configuration" %>
<%@ page import="de.uni_koeln.spinfo.maalr.common.shared.Role" %>

<%@ page import="de.uni_koeln.spinfo.maalr.login.LoginManager" %>
<%@ page import="de.uni_koeln.spinfo.maalr.login.MaalrUserInfo" %>

<%@ taglib prefix='cr' uri='http://java.sun.com/jstl/core_rt' %>
<%@ taglib prefix='fmt' uri='http://java.sun.com/jsp/jstl/fmt' %>
<%@ taglib prefix='fn' uri='http://java.sun.com/jsp/jstl/functions' %>

<fmt:setLocale value='<%=session.getAttribute("locale")%>' />
<fmt:setBundle basename="de.uni_koeln.spinfo.maalr.webapp.i18n.text" />

		<% 
			if(!session.getAttribute("locale").equals("ss")) { 
				session.setAttribute("locale","de");
			} 
		%>
		
<div id="navi_head">
	
	<%-- LANGUAGE SELECTION --%>
	<div id="languages-widget">
		<a href="?locale=ss" class='<%=(session.getAttribute("locale").equals("ss"))?"lang_selected":"navi_a_common"%>'>R</a>
		<span class="navi_a_common"> | </span>
		<a href="?locale=de" class='<%=(session.getAttribute("locale").equals("de"))?"lang_selected":"navi_a_common"%>'>D</a>
	</div>
	
	<%-- BRAND --%>
	<div id="brand_title">
		<a class="brand active" href="${dictContext}"><%=Configuration.getInstance().getLongName()%></a>
	</div>
	
	<%-- LOGIN --%>
	<div id="login-widget">
		<c:set var="user" value='<%=(MaalrUserInfo) session.getAttribute("user")%>'/>
		<c:set var="admin" value='<%=Role.ADMIN_5%>'/>
		<c:set var="editor" value='<%=Role.TRUSTED_IN_4%>'/>
		<%-- <jsp:useBean id="user" class="de.uni_koeln.spinfo.maalr.login.MaalrUserInfo" scope="request" /> --%>
		
		<cr:choose>
		    <cr:when test='${user != null}'>
		    	<cr:choose>
		    	 	<cr:when test='${user.role eq admin}'>
		    	 		<a href="${dictContext}/admin/admin.html" class="navi_a_common"><span id="u_name">Administrator</span></a>
		    	 	</cr:when>
		    	 	<cr:when test='${user.role eq editor}'>
		    	 		<a href="${dictContext}/editor/editor.html" class="navi_a_common"><span id="u_name">Editor</span></a>
		    	 	</cr:when>
		    	 	<cr:otherwise>
		    	 		<c:set var="userName" value="${user.getDisplayName()}"/>
		    	 		<a href="#" class="navi_a_common" title="${userName}">
								<span id="u_name">${userName}</span>
		    	 		</a>
		   			</cr:otherwise>
		    	</cr:choose>
		    	<span class="navi_a_common"> | </span>
		   		<a id="maalr-current-user" href="<cr:url value='/j_spring_security_logout'/>" title="Logout" class="navi_a_common login-icon"></a>
		    </cr:when>
		    <cr:otherwise>
		    	<a href="${dictContext}/login.html" class="navi_a_common login-icon" title="<fmt:message key='maalr.user.login' />">
					LOG
				</a>
		    </cr:otherwise>
		</cr:choose>
	</div>
</div>

<%-- SUB_NAVIGATION --%>
<div id="dic_navi_head">
	<div id="pt_brand_title" class="dict_navi_item"></div>
	<div id="rm_brand_title" class="dict_navi_item"></div>
	<div id="sm_brand_title" class="dict_navi_item"></div>
	<div id="sr_brand_title" class="dict_navi_item"></div>
	<div id="st_brand_title" class="dict_navi_item selected"></div>
	<div id="vl_brand_title" class="dict_navi_item"></div>
</div>