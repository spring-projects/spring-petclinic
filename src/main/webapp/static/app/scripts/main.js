'use strict';

// Scroll Nav
function scrollNav() {
	$('.js-scroll-nav').click(function() {
		//Animate
		$('html, body').stop().animate({
			scrollTop: $($(this).attr('href')).offset().top - 20
		}, 1000);
		return false;
	});
}

// On Dom Ready
$(function() {

	scrollNav();

});
