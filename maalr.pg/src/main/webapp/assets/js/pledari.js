$(document).ready(function() {

	var mobile = false;
	var retina = false;

	if (window.devicePixelRatio) {
		retina = window.devicePixelRatio > 1;
	}
	
	$('#content.content').on('click', '.row-fluid .span4 .well form #searchoptions + a.btn', function(event) {

		$('#searchoptions').toggleClass('open');
		$('#searchoptions').parents('.span4').toggleClass('open');

		if (!$('html').hasClass('lt-ie9')) {
			initCustomElements();
		}

	});

	checkMobile();

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

		if ($(window).width() < 768 && mobile == false) {			
			startMobile();
		} else if ($(window).width() >= 768 && mobile == true) {
			stopMobile();
		}

	}

	function startMobile() {

		console.log("startMobile");

		mobile = true;

		$('#languages-widget li a.active').bind('click', function(event) {

			event.preventDefault();

			$('#languages-widget').toggleClass('open');

		});

		function checkLoginButton() {
			if (!$('#login-widget').length) {
				$('#languages-widget').css('width', '100%');
			}
		}

		checkLoginButton();

	}

	function stopMobile() {

		console.log("stopMobile");

		mobile = false;

		$('#languages-widget li:first-child').unbind('click');

		$('#languages-widget').css('width', '');

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
				'background-image': 'url(/assets/img/radiobutton_sprite2x.png)'
			});
			$('.styledCheckbox').css({
				'background-image': 'url(/assets/img/checkbox_sprite2x.png)'
			});
		}
		
	}
	
});