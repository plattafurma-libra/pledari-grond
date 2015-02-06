<!DOCTYPE html>

<%@ page import="de.uni_koeln.spinfo.maalr.common.server.util.Configuration" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
if(session.getAttribute("pl") == null) {
	session.setAttribute("pl", request.getLocale().getLanguage());
	//session.setAttribute("pl", "rm");
}
if(request.getParameter("pl") != null) {
	session.setAttribute("pl", request.getParameter("pl"));
}
%>
<html lang='<%=session.getAttribute("pl")%>'>
<fmt:setLocale value='<%=session.getAttribute("pl")%>' />
<fmt:setBundle basename="de.uni_koeln.spinfo.maalr.webapp.i18n.text" />
<head>
<!--[if lt IE 9]>
<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->
<!--[if IE 7]>
	<link rel="stylesheet" href="${dictContext}/de.uni_koeln.spinfo.maalr.user/css/font-awesome-ie7.css">
<![endif]-->
<title><%=Configuration.getInstance().getShortName()%> - Editor Backend</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<!-- SET DEFAULT LOCALE FOR EDITOR BACKEND -->
<meta name="gwt:property" content="locale=<%=request.getLocale() %>">
<link rel="shortcut icon" type="image/x-icon" href="./favicon.ico">
<link href="${dictContext}/assets/style/backend/font-awesome.css" rel="stylesheet" type="text/css">
<link href="${dictContext}/assets/style/backend/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="${dictContext}/assets/style/backend/bootstrap-responsive.min.css" rel="stylesheet" type="text/css">
<link href="${dictContext}/assets/style/backend/datetimepicker.css" rel="stylesheet" type="text/css">
<link href="${dictContext}/assets/style/backend/editor_style.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script type="text/javascript" src="${dictContext}/assets/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${dictContext}/assets/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" language="javascript" src="../de.uni_koeln.spinfo.maalr.editor/de.uni_koeln.spinfo.maalr.editor.nocache.js"></script>
</head>