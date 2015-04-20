<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.List" %>

<%-- HTML HEADER --%>
<jsp:include page="/maalr_modules/misc/htmlhead.jsp" />
	<body>
	
		<div id="top">
			<jsp:include page="/maalr_modules/misc/header.jsp" />
		</div>
		
		<div id="bottom"><jsp:include page="/maalr_modules/misc/footer.jsp" /></div>
		
		<div class="container">
		
			<%@ include file="/maalr_modules/misc/language_widget.jsp" %>
			
			<%@ include file="/maalr_modules/misc/login_widget.jsp" %>
	
			<div class="page-header">
				<h1>
					Maalr<small> A Modern Approach to Aggregate Lexical Resources</small>
				</h1>
			</div>
	
			<div class="well">
				<p>
					Alle wörter, namen, und arten zuo reden [...]: dem ABC nach
					ordenlich gestellt, [...] gantz fleissig unnd eigentlich
					vertolmetscht, dergleychen bishär nie gesähen. Durch <a
						href=http://en.wikipedia.org/wiki/Josua_Maaler target="_blank">Josua
						Maaler</a> (1529 - 1599).
				</p>
	
				<p>
					Developed at the <a
						href="http://www.spinfo.phil-fak.uni-koeln.de/maalr.html"
						target="_blank">Department of Computational Linguistics</a>,
					University of Cologne. Soon available as Open-Source through <a
						href="https://github.com/spinfo/maalr" target="_blank">GitHub</a>.
				</p>
	
			</div>
		</div>
	</body>
</html>