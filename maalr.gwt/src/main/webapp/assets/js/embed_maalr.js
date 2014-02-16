var maalr_queryDiv;
var maalr_input;
var maalr_max;
var maalr_headerData;
var maalr_footerData;
var maalr_auto_query;

injectQuery();

function injectQuery() {
	 maalr_queryDiv = document.getElementById("maalr_query_div");
	 var fieldLabel = maalr_queryDiv.getAttribute("data-label");
	 var buttonLabel = maalr_queryDiv.getAttribute("data-button");
	 var inputLabel = maalr_queryDiv.getAttribute("data-placeholder");
	 var autoQuery = maalr_queryDiv.getAttribute("data-autoquery");
	 var embedCss = maalr_queryDiv.getAttribute("data-embedcss");
	 var source = maalr_queryDiv.getAttribute("data-source");
	 
	 maalr_max = maalr_queryDiv.getAttribute("data-pagesize");
	 if(maalr_max == null || maalr_max < 1 || maalr_max > 20) {
			maalr_max = 5;
		}
	 if(maalr_headerData != null) {
		 var header = document.createElement("div");
		 header.setAttribute("id", "maalr_header");
		 header.setAttribute("class", "maalr_header");
		 header.innerHTML = maalr_headerData;
	 }
	 
	 maalr_queryDiv.appendChild(header);

	 if(embedCss == null || embedCss) {
		  var head  = document.head;
		  var link  = document.createElement('link');
		  link.rel  = 'stylesheet';
		  link.type = 'text/css';
		  source = "src/main/webapp"; // Development FIXME
		  link.href = source + '/assets/style/maalr_embedded.css';
		  link.media = 'all';
		  head.appendChild(link);
	 }
	 var qForm = document.createElement("form");
	 qForm.setAttribute("id", "maalr_query_form");
	 qForm.setAttribute("class", "maalr_query_form");
	 qForm.setAttribute("method","get");
	 qForm.setAttribute("action","dummy");
	 qForm.setAttribute("onsubmit","return false");
	 if(fieldLabel != null) {
		 var label = document.createElement("label");
		 label.setAttribute("for","query");
		 label.innerHTML = fieldLabel;
		 qForm.appendChild(label);
		 label.setAttribute("class", "maalr_query_label");
	 }
	 maalr_input = document.createElement("input");
	 maalr_input.setAttribute("type","text");
	 maalr_input.setAttribute("name","query");
	 maalr_input.setAttribute("id","query");
	 maalr_input.setAttribute("value","");
	 maalr_input.setAttribute("class", "maalr_query_input");
	 if(inputLabel != null) {
		 maalr_input.placeholder=inputLabel;
	 }
	 qForm.appendChild(maalr_input);
	 if(autoQuery != null && autoQuery) {
		 maalr_input.onkeyup=function() {
			clearTimeout(maalr_auto_query);
			maalr_auto_query = setTimeout("queryMaalr()",300);
		 }
	 }
	 if(buttonLabel != null) {
		 var button = document.createElement("button");
		 button.setAttribute("type","submit");
		 button.innerHTML = (buttonLabel);
		 button.setAttribute("class", "maalr_query_button");
		 qForm.appendChild(button);
	 }
	 maalr_queryDiv.appendChild(qForm);
	 var results = document.createElement("div");
	 results.setAttribute("id", "maalr_results");
	 results.setAttribute("class", "maalr_results");
	 maalr_queryDiv.appendChild(results);
	 if(maalr_footerData != null) {
		 var footer = document.createElement("div");
		 footer.setAttribute("id", "maalr_footer");
		 footer.setAttribute("class", "maalr_footer");
		 footer.innerHTML = maalr_footerData;
		 maalr_queryDiv.appendChild(footer);
	 }
	 qForm.addEventListener('submit', queryMaalr, false);
	 var maalr_queryLink = document.getElementById("maalr_query_link");
	 if(maalr_queryLink != null) {
		 maalr_queryLink.onclick = function(evt) {
			 var phrase = maalr_input.value;
			 if(phrase.length != 0) {
				 if(evt != null) {
					 evt.preventDefault();
				 }
				 var domain = maalr_queryDiv.getAttribute("data-source");
				 window.location = domain + '/translate.html#searchPhrase=' + phrase;
			 }
			}
	 }
}

function queryMaalr(evt) {
	if(evt != null) {
		evt.preventDefault(); // Don't do default action
	}
	var domain = maalr_queryDiv.getAttribute("data-source");
	var phrase = maalr_input.value;
	if(phrase.length == 0) {
		var results = document.getElementById('maalr_results');
		if(results != null) {
			results.innerHTML = "";
		}
		return;
	}
	var script = document.createElement('script');
	var locale = document.getElementById("maalr_query_div").getAttribute("data-locale");
	script.src = domain + "/json?pageNr=0&pageSize=" + maalr_max + "&locale=" + locale + "&values[searchPhrase]=" + phrase + "?&callback=maalrCallback";
	script.setAttribute("id","maalr_reply");
	document.head.appendChild(script);
}

	function maalrCallback(reply){
    	var results = reply.data;
        var len = results.length;
        var root = document.getElementById('maalr_results');
        if(len == 0) {
        	root.innerHTML = "";
        	var nothingFound = document.createElement("div");
        	nothingFound.setAttribute("class", "maalr_nothing_found");
        	nothingFound.innerHTML = reply.nothingFoundMessage;
        	root.appendChild(nothingFound);
        	return;
        }
        var table = document.createElement("table");
//        table.setAttribute("width","100%");
        table.setAttribute("class","maalr_result_table");
//        table.style.borderSpacing = "0px";
        root.innerHTML="";
        root.appendChild(table);
        for(var i=0;i<len;i++){
        	entry = results[i];
        	var row = document.createElement("tr");
        	row.setAttribute("class","maalr_result_row " + (i%2 == 0 ? "maalr_even_result_row" : "maalr_odd_result_row"));
        	table.appendChild(row);
        	var left = document.createElement("td");
        	row.appendChild(left);
        	left.innerHTML= entry.a;
        	var right = document.createElement("td");
        	right.innerHTML=entry.b;
        	row.appendChild(right);
        }
        var toRemove = document.getElementById("maalr_reply");
        document.head.removeChild(toRemove);
    }