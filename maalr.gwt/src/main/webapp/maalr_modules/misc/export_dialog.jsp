<%--  TRIGGERT BY LINK  IN "maalr.pg/maalr_modules/misc/footer.jsp" --%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div id="exportDialog" class="modal hide fade vertical-center" tabindex="-1" aria-hidden="true">
	<h3><fmt:message key="maalr.data.export"/></h3>
	<div class="modal-body">
		<div class="row-fluid">
		    <div class="span12">
		    	<p>
		    		<fmt:message key="maalr.data.export.information"/>
		   	 	</p>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span12">
				<ul class="ext_links_dicts">
					<li><a href="${dictContext}/export/data/csv">CSV</a></li>
					<li><a href="${dictContext}/export/data/xml">XML</a></li>
					<li><a href="${dictContext}/export/data/json">JSON</a></li>
				</ul>
			</div>
		</div>
	</div>
</div>