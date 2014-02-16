$(document).ready(function() {

	var mobile = false;
	
	$('#content.content').on('click', '.row-fluid .span4 .well form #searchoptions + a.btn', function(event) {

		$('#searchoptions').toggleClass('open');

	});

	checkMobile();

	$(document).keyup(function(event) {

		if (event.which == '27' && $('.modal').length) {
			console.log($('.modal.in .modal-footer a:first'));
			$('.modal.in .modal-footer a:first').trigger('click');
		}

	});

	// $('input, textarea').focus(function(event) {
	// 	$(this).css('background-image', 'none');
	// });

	// $('input, textarea').blur(function(event) {
	// 	if ($(this).val() == '') {
	// 		$(this).css('background-image', '');
	// 	}
	// });

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
	
});