var maalr_headerData = '<div class="pledari_header"><div class="pledari_header_text">Search with Pledari Grond</div></div>';
var maalr_footerData = '<div class="pledari_footer"><div class="pledari_footer_text"><a class="pledari_footer_link" href="http://www.pledarigrond.ch">www.pledarigrond.ch</a><span class="pledari_cr">&copy; Lia Rumantscha</span></div></div>';

var maalr_script = document.createElement('script');
maalr_script.src =  document.getElementById("maalr_query_div").getAttribute("data-source") + "/sutsilvan/assets/js/embed_maalr.js";
document.head.appendChild(maalr_script);