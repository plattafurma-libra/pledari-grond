<%@ page import="java.util.List" %>

<%@ page import="de.uni_koeln.spinfo.maalr.common.server.util.Configuration" %>
<%@ page import="de.uni_koeln.spinfo.maalr.common.shared.searchconfig.Localizer" %>
<%@ page import="de.uni_koeln.spinfo.maalr.common.shared.searchconfig.UiConfiguration" %>
<%@ page import="de.uni_koeln.spinfo.maalr.common.shared.searchconfig.UiField" %>
<%@ page import="de.uni_koeln.spinfo.maalr.common.shared.searchconfig.UiFieldType" %>
<%@ page import="de.uni_koeln.spinfo.maalr.lucene.query.MaalrQuery" %>


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sform" uri="http://www.springframework.org/tags/form" %>

<fmt:setLocale value="<%=session.getAttribute("pl")%>" />
<fmt:setBundle basename="de.uni_koeln.spinfo.maalr.webapp.i18n.text" />

<div id="head_search">
	<noscript>
		<%-- What is this for? --%>
		<div id="search_main"></div>
		<div id="search_details">
			<div class="well">
				<table cellspacing="0" cellpadding="0" style="height: 100%; width: 100%;">
					<tbody>
						<tr>
							<td align="left" style="vertical-align: top;">
							<sform:form action="/translate.html" method="get" modelAttribute="query" class="form-inline">
								<fieldset>
									<% 
										UiConfiguration uiConfig = Configuration.getInstance().getUserDefaultSearchUiConfig();
										uiConfig = Localizer.localize(uiConfig, (String)session.getAttribute("pl"));
										List<UiField> fields = uiConfig.getFields();
										for(UiField field : fields) {
											if(field.getType() == UiFieldType.CHECKBOX) continue;
									%>
										<div class="control-group" style="width: 420px;">
										<%
											pageContext.setAttribute("current_id", field.getId());
											switch(field.getType()) {
												case TEXT: {
										%> 
														<label class="control-label"><%=field.getLabel()%></label>
														<div class="controls">
															<sform:input id="<%=field.getId()%>" path="values[${current_id}]" class="gwt-TextBox"/>
														<% if(field.hasSubmitButton()) { %>
															<sform:button class="btn"><fmt:message key="maalr.query.search"/></sform:button>
														<% } %>
														</div>
										<%
													break;
												}
												case COMBO: 
												case RADIO: {
										%>
														<label class="control-label"><%=field.getLabel()%></label>
														<div class="controls">
															<sform:select id="<%=field.getId()%>" path="values[${field.getId()}]" class="h_fill" items="${methods}"></sform:select>
														</div>
										<%
													break;
												}
												default: break;
											}
										%>
										</div>
									<%
										} 
									%>
									</fieldset>
								</sform:form>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</noscript>
</div>