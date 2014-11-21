<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.List" %>

<%@ page import="de.uni_koeln.spinfo.maalr.common.shared.LemmaVersion" %>
<%@ page import="de.uni_koeln.spinfo.maalr.common.shared.description.LemmaDescription" %>
<%@ page import="de.uni_koeln.spinfo.maalr.common.shared.description.UseCase" %>
<%@ page import="de.uni_koeln.spinfo.maalr.common.server.util.Configuration" %>
<%@ page import="de.uni_koeln.spinfo.maalr.lucene.query.MaalrQuery" %>
<%@ page import="de.uni_koeln.spinfo.maalr.lucene.query.MaalrQueryFormatter" %>
<%@ page import="de.uni_koeln.spinfo.maalr.lucene.query.QueryResult" %>
<%@ page import="de.uni_koeln.spinfo.maalr.lucene.util.QueryBuilder" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="<%=session.getAttribute("pl")%>" />
<fmt:setBundle basename="de.uni_koeln.spinfo.maalr.webapp.i18n.text" />

<%
	MaalrQuery mQuery = (MaalrQuery) request.getAttribute("search");
	QueryResult result = (QueryResult) request.getAttribute("result");
	List<LemmaVersion> entries = new ArrayList<LemmaVersion>();

	if (mQuery != null && result != null) {
		entries = result.getEntries();
		LemmaDescription description = Configuration.getInstance().getLemmaDescription();
%>

	<% 
		if (entries.size() > 0) { 
	%>
	<div class="well">
		<table style="width: 100%;" aria-hidden="false">
			<tbody>
				<tr>
					<td align="left" style="vertical-align: top;"><div
							class="container-fluid">
							<div class="row-fluid">
								<div class="span12">
									<div class="gwt-HTML">
										<span style="font-size: large; font-weight: bold; text-align: left;">
											<fmt:message key="maalr.query.results">
													<fmt:param><%=MaalrQueryFormatter.getQueryLabel(mQuery)%></fmt:param> 
	   												<fmt:param><%=(mQuery.getPageNr()*mQuery.getPageSize() + 1)%></fmt:param>
	   												<fmt:param><%=Math.min(result.getMaxEntries(),((mQuery.getPageNr()+1) * mQuery.getPageSize()))%></fmt:param>
	   												<fmt:param><%=result.getMaxEntries() + ""%></fmt:param>
											</fmt:message>
										</span>
									</div>
								</div>
							</div>
						</div>
					</td>
				</tr>
				<tr>
					<td align="center" style="vertical-align: top;" width="100%">
						<table class="GDPXEMADOP">
							<tbody>
								<tr>
									<td align="left" style="vertical-align: top;" width="100%">
										<table class="table resultlist" style="width: 100%;">
											<thead>
												<tr __gwt_header_row="0">
													<th colspan="1" class="GDPXEMADAC GDPXEMADOB"
														><span
														class="maalr_result_title"
														style="color: #002994; font-size: large;"><%=Configuration.getInstance().getLemmaDescription().getLanguageName(true)%></span></th>
													<th colspan="1" class="GDPXEMADAC"
														><span
														class="maalr_result_title"
														style="color: #002994; font-size: large;"><%=Configuration.getInstance().getLemmaDescription().getLanguageName(false)%></span></th>
													<th colspan="1" class="GDPXEMADAC GDPXEMADIC"
														></th>
												</tr>
											</thead>
											<tfoot style="display: none;" aria-hidden="true"></tfoot>
											<tbody style="">
												<%
													for (int i = 0; i < entries.size(); i++) {
														LemmaVersion e = entries.get(i);
												%>
												<tr  class="GDPXEMADKB">
													<td class="GDPXEMADJB GDPXEMADLB GDPXEMADMB">
														<div style="outline-style: none;" ><%=description.toString(e, UseCase.RESULT_LIST, true)%></div>
													</td>
													<td class="GDPXEMADJB GDPXEMADLB">
														<div style="outline-style: none;" ><%=description.toString(e, UseCase.RESULT_LIST, false)%></div>
													</td>
													<td class="GDPXEMADJB GDPXEMADLB GDPXEMADGC">
														<div style="outline-style: none;" ></div>
													</td>
												</tr>
												<%
													}
												%>
											</tbody>
										</table>
									</td>
								</tr>
								<%
									if (result.getEntries().size() < result.getMaxEntries()) {
								%>
								<tr>
									<td align="left" style="vertical-align: top;">
										<div style="" aria-hidden="false">
											<%-- PAGINATION --%>
											<div class="pagination pagination-centered">
												<ul>
													<%
														int max = result.getMaxEntries();
														int current = mQuery.getPageNr();
														int min = Math.max(0, current - 5);
														for (int i = min; i < current; i++) {
													%>
													<li class="a_pag" width="20">
														<a href="<%=QueryBuilder.getOffsetUrl(mQuery, i)%>"><%=i+1 %></a>
													</li>
													<%
														}
													%>
													<li width="20">
														<b class="a_pag"> 
															<a href="<%=QueryBuilder.getOffsetUrl(mQuery, current)%>"><%=current+1%></a>
														</b>
													</li>
													<%
														int pageNr = current+1;
														int maxPage = mQuery.getPageNr() + 6;
														for (int i = pageNr; i < maxPage && (i * mQuery.getPageSize() < max); i++) {
													%>
													<li class="a_pag" width="20"> 
														<a href="<%=QueryBuilder.getOffsetUrl(mQuery, i)%>"><%=i +1%></a>
													</li>
													<%
														}
													%>
												</ul>
											</div>
										</div>
									</td>
								</tr>
								<% 
									} 
								%>
							</tbody>
						</table>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<% 
		} else if(mQuery.getValue("searchPhrase") != null){ 
	%>
	<div class="well">
		<jsp:include page="/maalr_modules/misc/notfound.jsp" />
	</div>
	<% 
		} 
	%>
<!-- END -->
<%
	}
%>