 
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

				
				var whitecheck = $(".tile.white");

				for(i = 0; i < whitecheck.length; i++) {	//Capture of White Pieces

					var $check = $("#"+whitecheck[i].id);

					var tid = parseInt(($check.attr('id')).slice(1));
					var up = (tid - 7).toString();
		        	var down = (tid + 7).toString();
		        	var left = (tid - 1).toString();
					var right = (tid + 1).toString();

					if (($("#t" + up).hasClass("black") || $("#t" + up).length == 0)
						&& ($("#t"+down).hasClass("black") || $("#t" + down).length == 0)
						&& ($("#t"+left).hasClass("black") || left % 7 == 0)
						&& ($("#t"+right).hasClass("black") || right % 7 == 1))
					{
						$check.removeClass("white");
					}
				}

				f.turn = !f.turn;
			}
			else
			{
				$(this).removeClass("black white");
				$(this).addClass("white");
				$(".marker").removeClass("white");
				$(".marker").addClass("black");


				var blackcheck = $(".tile.black");

				for(i = 0; i < blackcheck.length; i++) {	//Capture of Black Pieces

					var $check = $("#"+blackcheck[i].id);

					var tid = parseInt(($check.attr('id')).slice(1));
					var up = (tid - 7).toString();
		        	var down = (tid + 7).toString();
		        	var left = (tid - 1).toString();
					var right = (tid + 1).toString();

					if (($("#t" + up).hasClass("white") || $("#t" + up).length == 0)
						&& ($("#t"+down).hasClass("white") || $("#t" + down).length == 0)
						&& ($("#t"+left).hasClass("white") || left % 7 == 0)
						&& ($("#t"+right).hasClass("white") || right % 7 == 1))
					{
						$check.removeClass("black");
					}
				}

				f.turn = !f.turn;
			}
		}

		// var blackcheck = $(".tile.black");
		// var whitecheck = $(".tile.white");

		// for(i = 0; i < blackcheck.length; i++) {	//Capture of Black Pieces

		// 	var $check = $("#"+blackcheck[i].id);

		// 	var tid = parseInt(($check.attr('id')).slice(1));
		// 	var up = (tid - 7).toString();
  //       	var down = (tid + 7).toString();
  //       	var left = (tid - 1).toString();
		// 	var right = (tid + 1).toString();

		// 	if (($("#t" + up).hasClass("white") || $("#t" + up).length == 0)
		// 		&& ($("#t"+down).hasClass("white") || $("#t" + down).length == 0)
		// 		&& ($("#t"+left).hasClass("white") || left % 7 == 0)
		// 		&& ($("#t"+right).hasClass("white") || right % 7 == 1))
		// 	{
		// 		$check.removeClass("black");
		// 	}
		// }
		// for(i = 0; i < whitecheck.length; i++) {	//Capture of White Pieces

		// 	var $check = $("#"+whitecheck[i].id);

		// 	var tid = parseInt(($check.attr('id')).slice(1));
		// 	var up = (tid - 7).toString();
  //       	var down = (tid + 7).toString();
  //       	var left = (tid - 1).toString();
		// 	var right = (tid + 1).toString();

		// 	if (($("#t" + up).hasClass("black") || $("#t" + up).length == 0)
		// 		&& ($("#t"+down).hasClass("black") || $("#t" + down).length == 0)
		// 		&& ($("#t"+left).hasClass("black") || left % 7 == 0)
		// 		&& ($("#t"+right).hasClass("black") || right % 7 == 1))
		// 	{
		// 		$check.removeClass("white");
		// 	}
		// }
	});
});