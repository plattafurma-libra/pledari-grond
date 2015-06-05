var retina = false;
var locale;

$(document).ready(function() {
	
	/*Get the current locale value*/
	locale = $('html').attr('lang');
	
	checkMobile();
	
	if (window.devicePixelRatio) {
		retina = window.devicePixelRatio > 1;
	}
	
	/* PAGINATION */
	$(window).resize(function() { 
		resizePagination();
	});
	function resizePagination() {
		if($(window).width() < 767) {
			$('div.pagination').addClass('pagination-mini');
		} else {
			$('div.pagination').removeClass('pagination-mini');
		}
	} /* PAGINATION */
	
	
	$('#content.content').on('click', '.row-fluid .span4 .well form #searchoptions + a.btn', function(event) {

		$('#searchoptions').toggleClass('open');
		$('#searchoptions').parents('.span4').toggleClass('open');

		if (!$('html').hasClass('lt-ie9')) {
			initCustomElements();
		}

	});

	$(document).keyup(function(event) {

		if (event.which == '27' && $('.modal').length) {
			console.log($('.modal.in .modal-footer a:first'));
			$('.modal.in .modal-footer a:first').trigger('click');
		}

	});

	$(window).resize(function() {
		checkMobile();
	});

	function checkMobile() {

		if ($(window).width() < 768) {			
			startMobile();
		} else if ($(window).width() >= 768) {
			stopMobile();
		}

	}

	function startMobile() {
		
		/*Rename dictionary names*/
		$('#sm_brand_title').html('<a href="/surmiran">SM</a>');
		$('#st_brand_title').html('<a href="/sutsilvan">ST</a>');
		$('#rm_brand_title').html('<a href="/rumantsch">RG</a>');
		$('#pt_brand_title').html('<a href="http://www.udg.ch/dicziunari/puter" target="_blank">PT</a>');
		$('#vl_brand_title').html('<a href="http://www.udg.ch/dicziunari/vallader" target="_blank">VL</a>');
		$('#sr_brand_title').html('<a href="http://www.vocabularisursilvan.ch" target="_blank">SR</a>');
		
		/*Remove link to grammar PDF*/
		$('#grammatica').hide();
	}

	function stopMobile() {
		console.log("stopMobile");
		
		/*Reverse settings from startMobile function*/

		if(locale == "rm") {
			$('#sm_brand_title').html('<a href="/surmiran">surmiran</a>');
			$('#st_brand_title').html('<a href="/sutsilvan">sutsilvan</a>');
			$('#rm_brand_title').html('<a href="/rumantsch">rumantsch grischun</a>');
			$('#pt_brand_title').html('<a href="http://www.udg.ch/dicziunari/puter" target="_blank">puter</a>');
			$('#vl_brand_title').html('<a href="http://www.udg.ch/dicziunari/vallader" target="_blank">vallader</a>');
			$('#sr_brand_title').html('<a href="http://www.vocabularisursilvan.ch" target="_blank">sursilvan</a>');
		} else {
			$('#sm_brand_title').html('<a href="/surmiran">Surmiran</a>');
			$('#st_brand_title').html('<a href="/sutsilvan">Sutsilvan</a>');
			$('#rm_brand_title').html('<a href="/rumantsch">Rumantsch Grischun</a>');
			$('#pt_brand_title').html('<a href="http://www.udg.ch/dicziunari/puter" target="_blank">Puter</a>');
			$('#vl_brand_title').html('<a href="http://www.udg.ch/dicziunari/vallader" target="_blank">Vallader</a>');
			$('#sr_brand_title').html('<a href="http://www.vocabularisursilvan.ch" target="_blank">Sursilvan</a>');
		}
		
		
		$('#grammatica').show();
	}

	function initCustomElements() {

		$('#searchoptions input[type="radio"]').each(function() {

			var checked = $(this).is(':checked');
			var checkedAttr = '';

			if (checked) {
				checkedAttr = 'checked';
			}

			var element = $('<div class="styledControl styledRadio"></div>').addClass(checkedAttr);

			element.insertBefore($(this));

		});

		$('#searchoptions input[type="checkbox"]').each(function() {

			var checked = $(this).is(':checked');
			var checkedAttr = '';

			if (checked) {
				checkedAttr = 'checked';
			}

			var element = $('<div class="styledControl styledCheckbox"></div>').addClass(checkedAttr);

			element.insertBefore($(this));

		});

		$('#searchoptions input[type="radio"]').hide();
		$('#searchoptions input[type="checkbox"]').hide();

		$('#searchoptions label.radio .styledRadio, #searchoptions label.radio span, #searchoptions label.radio').click(function(event) {
			var el = $(this);
			if ($(this).is('span')) {
				el = $(this).prev().prev();
			} else if ($(this).is('label')) {
				el = $('.styledRadio', this);
			}
			el.closest('table').find('.styledRadio').removeClass('checked');
			el.addClass('checked');
		});

		$('#searchoptions label.gwt-checkbox-maalr .styledCheckbox, #searchoptions label.gwt-checkbox-maalr span, #searchoptions label.gwt-checkbox-maalr').click(function(event) {
			var el = $(this);
			if ($(this).is('span')) {
				el = $(this).prev().prev();
			} else if ($(this).is('label')) {
				el = $('.styledCheckbox', this);
			}
			el.toggleClass('checked');
		});

		if (retina) {
			$('.styledRadio').css({
				'background-image': 'url(/rumantsch/assets/img/radiobutton_sprite2x.png)'
			});
			$('.styledCheckbox').css({
				'background-image': 'url(/rumantsch/assets/img/checkbox_sprite2x.png)'
			});
		}
		
	}

	// hack for safari modal window close on link click ext_links_dicts
	//$('body').on('click', '.ext_links_dicts a', function() {
	//	$('.modal, .modal-backdrop').remove();
	//});
	
});