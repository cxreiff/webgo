 
$(document).ready(function f() {

	$(window).on("load resize scroll",function(e){
    	var cw = $('.board').width();
		$('.board').css({
    		'height': cw + 'px'
		});
	});

	f.turn = true;

	$("div.tile").click(function () {

		if ((!($(this).hasClass("black"))&&(!($(this).hasClass("white")))))	//Turn Switching & Stone placement.
		{
			if (f.turn)
			{
				$(this).removeClass("black white");
				$(this).addClass("black");
				$(".marker").removeClass("black");
				$(".marker").addClass("white");

				f.turn = !f.turn;
			}
			else
			{
				$(this).removeClass("black white");
				$(this).addClass("white");
				$(".marker").removeClass("white");
				$(".marker").addClass("black");

				f.turn = !f.turn;
			}
		}

		var blackcheck = $(".tile.black");
		var whitecheck = $(".tile.white");

		for(i = 0; i < blackcheck.length; i++) {	//Capture of Black Pieces

			var $check = $("#"+blackcheck[i].id);

			var tid = parseInt(($check.attr('id')).slice(1));
			var up = (tid - 7).toString();
        	var down = (tid + 7).toString();
        	var left = (tid - 1).toString();
			var right = (tid + 1).toString();

			if ($("#t" + up).hasClass("white") && $("#t"+down).hasClass("white") && $("#t"+left).hasClass("white") && $("#t"+right).hasClass("white"))
			{
				$check.removeClass("black");
			}
		}
		for(i = 0; i < whitecheck.length; i++) {	//Capture of White Pieces

			var $check = $("#"+whitecheck[i].id);

			var tid = parseInt(($check.attr('id')).slice(1));
			var up = (tid - 7).toString();
        	var down = (tid + 7).toString();
        	var left = (tid - 1).toString();
			var right = (tid + 1).toString();

			if ($("#t" + up).hasClass("black") && $("#t"+down).hasClass("black") && $("#t"+left).hasClass("black") && $("#t"+right).hasClass("black"))
			{
				$check.removeClass("white");
			}
		}
	});
});