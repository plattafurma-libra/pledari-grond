<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ taglib prefix="sform" uri="http://www.springframework.org/tags/form" %>

<fmt:setLocale value='<%=session.getAttribute("pl")%>' />
<fmt:setBundle basename="de.uni_koeln.spinfo.maalr.webapp.i18n.text" />

<div align="center">
	<script lang="javascript">
		function doSubmit() {
			var input = document.getElementById("simple_search_input")
			window.location = (${dictContext} + '/translate.html#searchPhrase=' + input.value)
		}
		window.onload = function() {
			document.getElementById("simple_search_input").focus();
		}
	</script>
	<sform:form action="${dictContext}/translate.html" method="post" modelAttribute="query" onsubmit="javascript:doSubmit();return false;">
		<span class="input"><sform:input path="values['searchPhrase']" id="simple_search_input"/></span>
		<sform:button name="Submit"><fmt:message key="maalr.query.search"/></sform:button>
	</sform:form>
</div>