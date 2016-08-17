<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Locale" %>

<%@ page import="de.uni_koeln.spinfo.maalr.common.shared.searchconfig.Localizer" %>
<%@ page import="de.uni_koeln.spinfo.maalr.common.shared.Constants" %>

<%@ page import="de.uni_koeln.spinfo.maalr.mongo.stats.DictionaryStatistics" %>
<%@ page import="de.uni_koeln.spinfo.maalr.webapp.i18n.UrlGenerator" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value='<%=session.getAttribute("locale")%>' />
<fmt:setBundle basename="de.uni_koeln.spinfo.maalr.webapp.i18n.text" />

<%-- HTML HEADER --%>
<%@ include file="/maalr_modules/misc/htmlhead.jsp" %>

	<body>
	
		<%-- NAVIGATION --%>
		<%@ include file="/maalr_modules/misc/header.jsp" %>

		<div id="gridWrapper">
			<%@ include file="/maalr_modules/misc/dict_links.jsp"%>
			<div id="mock_middle_container"></div>
			<div id="feed_container" style="visibility: hidden;">
<!-- 				<div id="feed"> -->
<!-- 					<p id="pubDate"></p> -->
<!-- 					<p id="excerpt"></p> -->
<!-- 					<p> -->
<%-- 						<a id="readMore" href="/" target="_blank"><fmt:message key="maalr.feed.readmore" /></a> --%>
<!-- 					</p> -->
<!-- 				</div> -->
			</div>
		</div>

	<div id="content" class="content">
			
<%-- 			<%@ include file="/maalr_modules/misc/dict_links.jsp" %> --%>
<%-- 			<%@ include file="/maalr_modules/misc/language_widget.jsp" %> --%>
<%-- 			<%@ include file="/maalr_modules/misc/login_widget.jsp" %> --%>
			
			<%-- SEARCH AREA --%>
			<div class="container-fluid" id="nojs_searchcontainer">
				<noscript>
					<div class="row-fluid">
						<div class="span12">
							<div class="alert container information_container">
								<strong><fmt:message key="maalr.misc.enable_js_header" /></strong>
								<fmt:message key="maalr.misc.enable_js_hint" />
							</div>
						</div>
					</div>
				</noscript>
				<div class="row-fluid">
					<div class="span4">
						<jsp:include page="/maalr_modules/search/complex_search.jsp" />
					</div>
					<div id="search-results" class="span8">
						<jsp:include page="/maalr_modules/search/results.jsp" />
					</div>
				</div>
			</div>
		</div>
		<jsp:include page="/maalr_modules/misc/footer.jsp" />

		<%-- GWT AJAX BROWSER HISTORY SUPPORT --%>
		<iframe src="javascript:''" id="__gwt_historyFrame" style="width: 0; height: 0; border: 0"></iframe>
		
		<jsp:include page="/analytics.jsp" />
		
		<script type="text/javascript">
			
			$.get("/rumantschgrischun/feeds/latest", function(feedList) {
				for (var i = 0; i < feedList.length; i++) {
					var feed =  feedList[i];
					var objects = JSON.parse(feed);
					for (var o in objects) {
						if(objects[o].Text) {
							if(objects[o].Language == 'de' || 
								objects[o].Language == 'fr' ||
								objects[o].Language == 'en' ||
								objects[o].Language == 'it') {
								continue;
							}
							var color = objects[o].Color;
							var text = '&laquo;' + objects[o].Text.substr(0, 60) + '...&raquo;';
							var url = objects[o].Url;
							var date = !objects[o].Date_From ? '' : objects[o].Date_From.split(' ')[0];
							if(date.length > 0) {
								var parts = date.split('-');
								date = parts[2] + '-' + parts[1] + '-' + parts[0];
							}
							var template = "<div class='feed' data-color='" + color + "' >"
								+ "<div class='pubDate'>" + date + "</div>"
								+ "<div class='excerpt'>" + text + "</div>"
								+ "<div class='feedLinkWrap'><a class='readMore' href='" + url + "' target='_blank'>legia dapli</a></div>"
								+ "</div>";
							template = $(template).hover(function(){
								var bColor = $(this).data('color');
								$(this).css('background-color', bColor);
		 					}, function(){
		 						$(this).css('background-color', '#bcbcbc');
		 					});
							$('#feed_container').append(template);
						}
					}
				}
				
				$('.feed').each(function( index ) {
					  console.log( index + ": " + $( this ).height() );
				});
				// $('#feed_container').css('margin-top', -(76 / 4));
				$("#feed_container > div.feed:gt(0)").hide();
				var timer = setInterval(showFeeds, 5000);
				
				function showFeeds() {
					 var _feed = $('#feed_container > div.feed:first')
					    .fadeOut(500)
					    .next()
					    .fadeIn(500)
					    .end()
					    .appendTo('#feed_container');
				}
				
				$('.readMore').hover(function() {
					$(this).css({'color' : 'white' , 'opacity' : '0.5'});
				}, function () {
					$(this).css({'color' : 'white', 'opacity' : '1'});
				});
				
				$('.feed').hover(function(){
				    clearInterval(timer);
				}, function(){
				    timer = setInterval(showFeeds, 5000);
				});
			});
			
		</script>
	</body>
</html>